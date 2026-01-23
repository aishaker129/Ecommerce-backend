package com.ecommerce.user.service.impl;

import com.ecommerce.common.config.JwtProperties;
import com.ecommerce.common.constants.ApplicationConstant;
import com.ecommerce.common.exceptions.InvalidRefreshTokenException;
import com.ecommerce.common.exceptions.ResourceConflictException;
import com.ecommerce.common.service.JwtService;
import com.ecommerce.user.dto.LoginResult;
import com.ecommerce.user.dto.RefreshTokenData;
import com.ecommerce.user.dto.RefreshTokenResult;
import com.ecommerce.user.dto.request.LoginRequest;
import com.ecommerce.user.dto.request.ProfileCreateRequest;
import com.ecommerce.user.dto.request.RegisterUserRequest;
import com.ecommerce.user.dto.response.LoginResponse;
import com.ecommerce.user.dto.response.RefreshTokenResponse;
import com.ecommerce.user.dto.response.RegisterUserResponse;
import com.ecommerce.user.entity.RefreshToken;
import com.ecommerce.user.entity.Role;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.entity.UserProfile;
import com.ecommerce.user.enums.RoleType;
import com.ecommerce.user.mapper.UserMapper;
import com.ecommerce.user.mapper.UserProfileMapper;
import com.ecommerce.user.repository.UserProfileRepository;
import com.ecommerce.user.repository.UserRepository;
import com.ecommerce.user.service.AuthService;
import com.ecommerce.user.service.BlackListedTokenService;
import com.ecommerce.user.service.RefreshTokenService;
import com.ecommerce.user.service.RoleService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final UserProfileMapper userProfileMapper;
    private final UserProfileRepository userProfileRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final RefreshTokenService refreshTokenService;
    private final BlackListedTokenService blackListedTokenService;


    @Transactional
    @Override
    public RegisterUserResponse registerUser(RegisterUserRequest request) {
        if(userRepository.existsByUsername(request.username())){
            throw new ResourceConflictException("User with username '"+request.username()+"' already exists");
        }
        if(userRepository.existsByEmail(request.email())){
            throw new ResourceConflictException("User with email '"+request.email()+"' already exists");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));

        Role customRole = roleService.findByCode(RoleType.CUSTOMER)
                .orElseThrow(() -> new EntityNotFoundException("Customer role not found."));
        user.setRoles(Set.of(customRole));

        User savedUser = userRepository.save(user);

        UserProfile profile = userProfileMapper.toEntity(request.profile(),savedUser);
        userProfileRepository.save(profile);

        return userMapper.toResponse(savedUser);
    }

    @Override
    public LoginResult login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(),request.password())
        );

        User user = (User) authentication.getPrincipal();

        String accessToken = jwtService.generateToken(user);
        LocalDateTime expireAt = LocalDateTime.now().plus(Duration.ofMillis(jwtProperties.getExpirationMs()));

        RefreshTokenData refreshTokenData =refreshTokenService.create(user);

        return LoginResult.builder()
                .loginResponse(
                        LoginResponse.builder()
                                .accessToken(accessToken)
                                .expiresAt(expireAt)
                                .build()
                )
                .refreshToken(refreshTokenData.rawToken())
                .refreshTokenDuration(Duration.ofMillis(refreshTokenData.expirySeconds()))
                .build();
    }

    @Override
    public void logout(String authorization) {
        String token = authorization.substring(ApplicationConstant.BEARER_PREFIX.length());
        Date expirationDate = jwtService.extractExpiration(token);

        blackListedTokenService.markAsBlackListed(token,expirationDate);
    }

    @Override
    public RefreshTokenResult refreshToken(String rawRefreshToken) {
        // step:1 validate refresh token
        RefreshToken refreshToken = refreshTokenService.findByToken(rawRefreshToken).orElseThrow(
                ()->new InvalidRefreshTokenException("Invalid or expired refresh token")
        );

        if(Boolean.TRUE.equals(refreshToken.getIsRevoked() || refreshToken.getExpiryDate().isBefore(LocalDateTime.now()))){
            throw new InvalidRefreshTokenException("Refresh token revoked or expired.");
        }

        // setp:2 get user
        User user = refreshToken.getUser();

        // setp:3 generate new accesstoken
        String newAccessToken = jwtService.generateToken(user);
        LocalDateTime accessTokenExpiry = LocalDateTime.now().plus(Duration.ofMillis(jwtProperties.getExpirationMs()));

        // setp:4 rotate refresh token
        RefreshTokenData newRefreshTokenData = refreshTokenService.rotate(refreshToken);

        // step:5 return refresh token result
        return RefreshTokenResult.builder()
                .refreshTokenResponse(
                        RefreshTokenResponse.builder()
                                .accessToken(newAccessToken)
                                .expiresAt(accessTokenExpiry)
                                .build()
                )
                .refreshToken(newRefreshTokenData.rawToken())
                .refreshTokenDuration(Duration.ofSeconds(newRefreshTokenData.expirySeconds()))
                .build();
    }


}
