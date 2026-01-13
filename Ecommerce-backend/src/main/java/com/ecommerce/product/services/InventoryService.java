package com.ecommerce.product.services;

import com.ecommerce.common.dto.request.StockReservationRequest;
import com.ecommerce.product.dto.request.InventoryUpdateRequest;

import java.util.List;

public interface InventoryService {
    void updateStock(Long productId, InventoryUpdateRequest request);

    void checkAndReserveStock(List<StockReservationRequest> requests);

    void finalizeReservedStock(Long productId, int quantity);
}
