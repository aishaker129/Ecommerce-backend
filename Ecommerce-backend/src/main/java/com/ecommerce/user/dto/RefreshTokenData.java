package com.ecommerce.user.dto;

import com.ecommerce.user.dto.response.RefreshTokenResponse;
import lombok.Builder;

@Builder
public record RefreshTokenData(
        RefreshTokenResponse refreshTokenResponse,
        String rawToken,
        long expirySeconds
) {
}
