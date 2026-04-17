package com.ecommerce.order.dto;

import java.util.List;

public record OrderResponse(
        Long orderId,
        Long userId,
        List<OrderItemResponse> items,
        Double totalAmount,
        String status
) {
}
