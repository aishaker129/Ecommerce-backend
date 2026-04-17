package com.ecommerce.product.dto.response;

import lombok.Builder;

@Builder
public record InventoryResponse(
        Long productId,
        String productName,
        Integer totalQuantity,
        Integer reservedQuantity
) {
}
