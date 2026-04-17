package com.ecommerce.common.dto.resquest;

import lombok.Builder;

@Builder
public record StockReservationRequest(
        Long productId,
        int quantity
) {
}
