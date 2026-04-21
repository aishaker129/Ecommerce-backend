package com.ecommerce.product.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ProductCreateRequest(
        @NotBlank(message = "SKU is required")
        @Size(max = 50, message = "SKU cannot exceed 100 characters")
        String sku,

        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name cannot exceed 200 characters")
        String name,

        @Size(max = 255, message = "Description cannot exceed 1000 characters")
        String description,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
        Double price,

        @NotNull(message = "Category ID is required")
        @JsonProperty("category_code")
        String categoryCode,

        @NotBlank(message = "Image is required.")
        String imageUrl

) {
}
