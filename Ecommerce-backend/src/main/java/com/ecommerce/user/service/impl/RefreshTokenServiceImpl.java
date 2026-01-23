package com.ecommerce.user.service.impl;

import com.ecommerce.common.config.RefreshTokenProperties;
import com.ecommerce.user.dto.RefreshTokenData;
import com.ecommerce.user.entity.RefreshToken;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.repository.RefreshTokenRepository;
import com.ecommerce.user.service.RefreshTokenService;
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
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenProperties refreshTokenProperties;
    @Override
    public RefreshTokenData create(User user) {
        String rawToken = generateSecureToken();
        String hashedToken = hashToken(rawToken);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(hashedToken)
                .expiryDate(
                        LocalDateTime.now()
                                .plusSeconds(refreshTokenProperties.getExpirationSecond())
                )
                .user(user)
                .build();

        refreshTokenRepository.save(refreshToken);

        return RefreshTokenData.builder()
                .rawToken(rawToken)
                .expirySeconds(refreshTokenProperties.getExpirationSecond())
                .build();
    }

    @Override
    public Optional<RefreshToken> findByToken(String rawRefreshToken) {
        String hashedToken = hashToken(rawRefreshToken);
        return refreshTokenRepository.findByToken(hashedToken);
    }

    @Override
    public RefreshTokenData rotate(RefreshToken refreshToken) {
        // revoke old token
        refreshToken.setIsRevoked(true);
        refreshToken.setRevokedAt(LocalDateTime.now());
        refreshTokenRepository.save(refreshToken);
        // issued new token for same user
        return create(refreshToken.getUser());
    }


    private String generateSecureToken() {
        byte[] randomBytes = new byte[64]; // 512 bits
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    private String hashToken(String token) {
        return DigestUtils.sha256Hex(token);
    }
}
