package com.ecommerce.cart.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record CartResponse(
        Long id,
        Long userId,
        List<CartItemsResponse> items,
        Double totalPrice
) {
}
