package com.ecommerce.cart.dto.response;

public record CartItemResponse(
        Long id,
        Long productId,
        String productName,
        String productImage,
        Double unitPrice,
        Integer quantity,
        Double subtotal
) {
}
