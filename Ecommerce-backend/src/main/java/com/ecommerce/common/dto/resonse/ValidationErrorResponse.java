package com.ecommerce.common.dto.resonse;

import lombok.Builder;

@Builder
public record ValidationErrorResponse(String field, String message) {
}
