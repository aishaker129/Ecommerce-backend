package com.ecommerce.user.dto;

import com.ecommerce.user.dto.response.RefreshTokenResponse;
import lombok.Builder;

import java.time.Duration;

@Builder
public record RefreshTokenResult(
        RefreshTokenResponse RefreshtokenResponse,
        String refreshToken,
        Duration refreshTokenDuration
) {
}
