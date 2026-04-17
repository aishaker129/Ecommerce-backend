package com.ecommerce.product.dto.response;

public record ImageUploadResponse(
        String imageUrl,
        String imagePublicId
) {
}
