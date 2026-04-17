package com.ecommerce.user.services;

import com.ecommerce.user.dto.RefreshTokenData;
import com.ecommerce.user.entity.RefreshToken;
import com.ecommerce.user.entity.User;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshTokenData create(User user);

    Optional<RefreshToken> findByToken(String rawRefreshToken);

    RefreshTokenData rotate(RefreshToken refreshToken);
}
