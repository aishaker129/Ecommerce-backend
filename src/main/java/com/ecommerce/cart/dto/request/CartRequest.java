package com.ecommerce.cart.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartRequest(
        @NotNull(message = "User id can't be null")
        Long productId,
        @NotNull(message = "Quantity must not be null")
        @Min(value = 1,message = "quantity must be at least 1")
        Integer quantity
) {
}
