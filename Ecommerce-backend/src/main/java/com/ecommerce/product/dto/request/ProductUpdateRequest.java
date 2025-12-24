package com.ecommerce.product.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductUpdateRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 200, message = "Name cannot exceed 200 characters")
        String name,

        @Size(max = 1000, message = "Description cannot exceed 1000 characters")
        String description,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
        Double price
) {
}
