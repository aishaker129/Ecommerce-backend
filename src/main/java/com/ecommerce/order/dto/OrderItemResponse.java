package com.ecommerce.order.dto;

public record OrderItemResponse(
        Long productId,
        Integer quantity,
        Double price
) {
}
