package com.ecommerce.product.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record InventoryRequest(
        @NotNull
        @Min(value = 1, message = "Quantity must be grater than or equal 1.")
        Integer quantity
) {
}
