package com.ecommerce.product.dto.response;

import java.time.LocalDateTime;

public record CategoryResponse(
        Long id,
        String name,
        String code,
        LocalDateTime createAt,
        LocalDateTime updateAt
){
}
