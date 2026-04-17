package com.ecommerce.cart.dto.response;

public record CartSummaryResponse(
        Integer totalItems,
        Double totalPrice
) {
}
