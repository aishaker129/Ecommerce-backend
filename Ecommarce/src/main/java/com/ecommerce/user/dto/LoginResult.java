package com.ecommerce.user.dto;

import com.ecommerce.user.dto.response.LoginResponse;
import lombok.Builder;

import java.time.Duration;

@Builder
public record LoginResult(
        LoginResponse loginResponse,
        String refreshToken,
        Duration refreshTokenDuration
) {
}
