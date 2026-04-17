package com.ecommerce.cart.dto.response;

import java.util.List;

public record CartResponse(
        Long cartId,
        Long userId,
        List<CartItemResponse> items,
        CartSummaryResponse summary
) {
}
