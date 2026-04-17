package com.ecommerce.payment.dto;

import lombok.Builder;

@Builder
public record CheckoutResponse(
        String checkoutUrl
) {
}
