package com.ecommerce.product.services.impl;

import com.ecommerce.common.dto.resquest.StockReservationRequest;
import com.ecommerce.common.exceptions.OutOfStockException;
import com.ecommerce.product.dto.request.InventoryRequest;
import com.ecommerce.product.dto.response.InventoryResponse;
import com.ecommerce.product.entity.Inventory;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.repository.InventoryRepository;
import com.ecommerce.product.repository.ProductRepository;
import com.ecommerce.product.services.InventoryService;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;


    @Override
    public void updateStock(Long productId, InventoryRequest request) throws AccessDeniedException{
        Inventory inventory = inventoryRepository.findByProduct_Id(productId).orElseThrow(
                ()-> new EntityNotFoundException("Inventory not found with product Id: "+productId)
        );

        Product product = productRepository.findById(productId).orElseThrow(
                ()-> new EntityNotFoundException("Product not found with this id: "+ productId)
        );

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username).orElseThrow(
                ()-> new EntityNotFoundException("User not found with this username '"+username+"'")
        );

        if(user.getId() != product.getCreateBy()){
            throw new AccessDeniedException("You are not allowed to update this product inventory.");
        }

        inventory.setTotalQuantity(inventory.getTotalQuantity() + request.quantity());
        inventoryRepository.save(inventory);
    }
    @Override
    public void checkAndReserveStock(List<StockReservationRequest> requests) {

        List<Long> productIds = requests.stream()
                .map(StockReservationRequest::productId)
                .toList();

        List<Inventory> inventories = inventoryRepository.findAllByIdIn(productIds);

        Map<Long, Inventory> inventoryMap = inventories.stream()
                .collect(Collectors.toMap(
                        inv -> inv.getProduct().getId(),
                        inv -> inv
                ));

        for (StockReservationRequest request : requests) {

            Inventory inventory = inventoryMap.get(request.productId());

            if (inventory == null) {
                throw new IllegalStateException("Inventory not found for product: " + request.productId());
            }

            int available = inventory.getTotalQuantity(); // ✅ FIXED

            if (request.quantity() > available) {
                throw new OutOfStockException(
                        String.format("Insufficient stock for product (ID: %d). Requested: %d, Available: %d.",
                                request.productId(), request.quantity(), available)
                );
            }

            // ✅ decrease available stock
            inventory.setTotalQuantity(inventory.getTotalQuantity() - request.quantity());

            // ✅ increase reserved stock
            inventory.setReservedQuantity(inventory.getReservedQuantity() + request.quantity());
        }

        inventoryRepository.saveAll(inventories);
    }
    @Override
    public void finalizedReserveStock(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProduct_Id(productId).orElseThrow(
                ()-> new IllegalStateException("Inventory not found for product id: '"+productId+"'.")
        );

        inventory.setReservedQuantity(inventory.getReservedQuantity() - quantity);
        inventoryRepository.save(inventory);
    }

    @Override
    public void releaseReservedStock(Long productId, Integer quantity) {

        Inventory inventory = inventoryRepository.findByProduct_Id(productId)
                .orElseThrow(() ->
                        new IllegalStateException("Inventory not found for product id: '" + productId + "'.")
                );

        // ✅ decrease reserved
        inventory.setReservedQuantity(inventory.getReservedQuantity() - quantity);

        // ✅ increase available stock
        inventory.setTotalQuantity(inventory.getTotalQuantity() + quantity);

        inventoryRepository.save(inventory);
    }

    @Override
    public void increaseStock(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProduct_Id(productId)
                .orElseThrow(() ->
                        new IllegalStateException("Inventory not found for product id: '" + productId + "'.")
                );
        inventory.setTotalQuantity(inventory.getTotalQuantity() + quantity);

        inventoryRepository.save(inventory);
    }

    @Override
    public InventoryResponse productStock(Long productId) {
        Inventory inventory = inventoryRepository.findByProduct_Id(productId)
                .orElseThrow(() ->
                        new IllegalStateException("Inventory not found for product id: '" + productId + "'.")
                );

        return InventoryResponse.builder()
                .productId(productId)
                .productName(inventory.getProduct().getName())
                .totalQuantity(inventory.getTotalQuantity())
                .reservedQuantity(inventory.getReservedQuantity())
                .build();
    }
}
