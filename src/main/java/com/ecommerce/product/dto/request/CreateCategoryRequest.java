package com.ecommerce.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequest(
        @NotBlank(message = "Category name is required.")
        @Size(max = 120, message = "category name does not exceed 120 characters.")
        String name,
        @NotBlank(message = "Category code is required.")
        String code
) {
}
