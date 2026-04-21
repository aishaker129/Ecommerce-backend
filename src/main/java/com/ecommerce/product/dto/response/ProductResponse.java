package com.ecommerce.product.dto.response;

import java.time.LocalDateTime;

public record ProductResponse(
        Long id,
        String sku,
        String name,
        String description,
        Double price,
        Boolean isActive,
        String imageUrl,

        Long categoryId,
        String categoryName,

        LocalDateTime createAt,
        String createdByName
) {}