package com.ecommerce.cart.dto.response;

public record CartItemsResponse(
        Long productId,
        String productName,
        Integer quantity,
        Double unitPrice
) {
}
