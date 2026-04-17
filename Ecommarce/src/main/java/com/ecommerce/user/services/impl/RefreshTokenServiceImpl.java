package com.ecommerce.user.services.impl;

import com.ecommerce.common.config.RefreshTokenProperties;
import com.ecommerce.user.dto.RefreshTokenData;
import com.ecommerce.user.entity.RefreshToken;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.repository.RefreshTokenRepository;
import com.ecommerce.user.services.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;


import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository repository;
    private final RefreshTokenProperties properties;
    @Override
    public RefreshTokenData create(User user) {
        String rawToken = generateToken();
        String hashedToken = hashToken(rawToken);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(hashedToken)
                .expiryDate(LocalDateTime.now().plusSeconds(properties.getExpirationSecond()))
                .user(user)
                .build();

        repository.save(refreshToken);
        return RefreshTokenData.builder()
                .rawToken(rawToken)
                .expirySeconds(properties.getExpirationSecond())
                .build();
    }

    @Override
    public Optional<RefreshToken> findByToken(String rawRefreshToken) {
        String hashedToken = hashToken(rawRefreshToken);
        return repository.findByToken(hashedToken);
    }

    @Override
    public RefreshTokenData rotate(RefreshToken refreshToken) {
        //revoked old token
        refreshToken.setIsRevoked(true);
        refreshToken.setRevokedAt(LocalDateTime.now());
        repository.save(refreshToken);

        // issue new token for user
        return create(refreshToken.getUser());
    }

    private String hashToken(String rawToken) {
        return DigestUtils.sha256Hex(rawToken);
    }

    private String generateToken() {
        byte[] randomBytes = new byte[64];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
