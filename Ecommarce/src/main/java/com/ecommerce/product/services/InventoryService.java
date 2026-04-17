package com.ecommerce.product.services;

import com.ecommerce.common.dto.resquest.StockReservationRequest;
import com.ecommerce.product.dto.request.InventoryRequest;
import com.ecommerce.product.dto.response.InventoryResponse;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface InventoryService {
    void updateStock(Long id, InventoryRequest request) throws AccessDeniedException;

    void checkAndReserveStock(List<StockReservationRequest> requests);

    void finalizedReserveStock(Long id, Integer quantity);

    void releaseReservedStock(Long productId, Integer quantity);

    void increaseStock(Long id, Integer quantity);

    InventoryResponse productStock(Long productId);
}
