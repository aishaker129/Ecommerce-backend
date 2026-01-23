package com.ecommerce.product.services.impl;

import com.ecommerce.common.dto.request.StockReservationRequest;
import com.ecommerce.common.exceptions.OutOfStockException;
import com.ecommerce.product.dto.request.InventoryUpdateRequest;
import com.ecommerce.product.entity.Inventory;
import com.ecommerce.product.repository.InventoryRepository;
import com.ecommerce.product.services.InventoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of {@link InventoryService} for stock management.
 *
 * <p>Handles stock updates, reservations, and finalization
 * after payment completion.</p>
 *
 * @author Md.Akhlakul Islam
 */

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;


    @Override
    public void updateStock(Long productId, InventoryUpdateRequest request) {
        Inventory inventory = inventoryRepository.findByProductId(productId).orElseThrow(
                ()-> new EntityNotFoundException("Inventory not found with product Id: "+productId)
        );
        inventory.setTotalQuantity(inventory.getTotalQuantity() + request.quantity());
        inventoryRepository.save(inventory);
    }

    @Override
    public void checkAndReserveStock(List<StockReservationRequest> requests) {

        List<Long> productIds = requests.stream()
                .map(StockReservationRequest::productId).toList();

        List<Inventory> inventories = inventoryRepository.findAllByIdIn(productIds);

        Map<Long,Inventory> inventoryMap = inventories.stream()
                .collect(Collectors.toMap(
                        inventory -> inventory.getProduct().getId(),inventory->inventory));

        for(StockReservationRequest request:requests){
            Inventory inventory = inventoryMap.get(request.productId());

            int available = inventory.getTotalQuantity() - inventory.getReservedQuantity();

            if (request.quantity() > available) {
                throw new OutOfStockException(
                        String.format("Insufficient stock for product (ID: %d). Requested: %d, Available: %d.",
                                request.productId(), request.quantity(), available));
            }

            inventory.setReservedQuantity(inventory.getReservedQuantity() + request.quantity());
        }

        inventoryRepository.saveAll(inventories);

    }

    @Override
    public void finalizeReservedStock(Long productId, int quantity) {

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new IllegalStateException("Inventory not found for product " + productId));

        inventory.setReservedQuantity(inventory.getReservedQuantity() - quantity);
        inventory.setTotalQuantity(inventory.getTotalQuantity() - quantity);

        inventoryRepository.save(inventory);
    }
}
