package com.ecommerce.cart.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartRequest(
        @NotNull(message = "User Id must not be null")
        Long userId,
        @NotNull(message = "Quantity must not be null")
        @Min(value = 1,message = "Quantity must be at least 1")
        Integer quantity
) {
}
