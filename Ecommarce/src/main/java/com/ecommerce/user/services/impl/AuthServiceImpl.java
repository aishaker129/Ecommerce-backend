package com.ecommerce.user.services.impl;

import com.ecommerce.common.config.JwtProperties;
import com.ecommerce.common.constants.ApiEndPoints;
import com.ecommerce.common.dto.response.ApiResponse;
import com.ecommerce.common.exceptions.InvalidRefreshTokenException;
import com.ecommerce.common.exceptions.ResourceconflictException;
import com.ecommerce.common.service.JwtService;
import com.ecommerce.user.dto.LoginResult;
import com.ecommerce.user.dto.RefreshTokenData;
import com.ecommerce.user.dto.RefreshTokenResult;
import com.ecommerce.user.dto.request.LoginRequest;
import com.ecommerce.user.dto.request.UserRegisterRequest;
import com.ecommerce.user.dto.response.LoginResponse;
import com.ecommerce.user.dto.response.RefreshTokenResponse;
import com.ecommerce.user.dto.response.RegisteredUserResponse;
import com.ecommerce.user.entity.RefreshToken;
import com.ecommerce.user.entity.Role;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.entity.UserProfile;
import com.ecommerce.user.enums.RoleType;
import com.ecommerce.user.mapper.ProfileMapper;
import com.ecommerce.user.mapper.UserMapper;
import com.ecommerce.user.repository.UserProfileRepository;
import com.ecommerce.user.repository.UserRepository;
import com.ecommerce.user.services.AuthService;
import com.ecommerce.user.services.BlackListedTokenService;
import com.ecommerce.user.services.RefreshTokenService;
import com.ecommerce.user.services.RoleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

import static com.ecommerce.common.constants.ApplicationConstants.BEARER_PREFIX;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserProfileRepository profileRepository;
    private final RoleService roleService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JwtProperties jwtProperties;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final ProfileMapper profileMapper;
    private final RefreshTokenService refreshTokenService;
    private final BlackListedTokenService blackListedTokenService;


    @Override
    public RegisteredUserResponse registerUser(UserRegisterRequest request) {
        if(userRepository.existsByUsername(request.username())){
            throw new ResourceconflictException("User with username '"+request.username()+"' already exists");
        }

        if(userRepository.existsByEmail(request.email())){
            throw new ResourceconflictException("User with email '"+request.email()+"' already exists");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));

        Role role = roleService.findByCode(RoleType.CUSTOMER)
                .orElseThrow( ()-> new EntityNotFoundException("Customer role not fouond"));

        user.setRoles(Set.of(role));
        User saved = userRepository.save(user);

        UserProfile profile = profileMapper.toEntity(request.profile(),saved);
        profileRepository.save(profile);

        return userMapper.toDto(saved);
    }

    @Override
    public LoginResult login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(),request.password()));
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        String token = jwtService.generateToken(user);

        LocalDateTime expireAt = LocalDateTime.now().plus(Duration.ofMillis(jwtProperties.getExpirationMs()));

        RefreshTokenData refreshTokenData = refreshTokenService.create(user);
        return LoginResult.builder()
                .loginResponse(
                        LoginResponse.builder()
                                .accessToken(token)
                                .expiresAt(expireAt)
                                .build()
                )
                .refreshToken(refreshTokenData.rawToken())
                .refreshTokenDuration(Duration.ofSeconds(refreshTokenData.expirySeconds()))
                .build();
    }

    @Override
    public void logout(String authorizationHeader) {
        String token = authorizationHeader.substring(BEARER_PREFIX.length());
        Date expirationDate = jwtService.extractExpiration(token);
        blackListedTokenService.markBlackListed(token,expirationDate);
    }

    @Override
    public RefreshTokenResult refreshToken(String rawRefreshToken) {
        // validate refresh token
        RefreshToken refreshToken = refreshTokenService.findByToken(rawRefreshToken).orElseThrow(
                ()-> new InvalidRefreshTokenException("Refresh token invalid or expired.")
        );

        if(Boolean.TRUE.equals(refreshToken.getIsRevoked()) || refreshToken.getExpiryDate().isBefore(LocalDateTime.now())){
            throw new InvalidRefreshTokenException("Refresh token is revoked or expired");
        }

        // get user
        User user = refreshToken.getUser();

        // generate new access token
        String newAccessToken = jwtService.generateToken(user);
        LocalDateTime accessTokenExpiry = LocalDateTime.now().plus(Duration.ofMillis(jwtProperties.getExpirationMs()));

        // totate refresh token
        RefreshTokenData refreshTokenData = refreshTokenService.rotate(refreshToken);

        // return result data
        return RefreshTokenResult.builder()
                .RefreshtokenResponse(
                        RefreshTokenResponse.builder()
                                .accessToken(newAccessToken)
                                .expiresAt(accessTokenExpiry)
                                .build()
                )
                .refreshToken(refreshTokenData.rawToken())
                .refreshTokenDuration(Duration.ofSeconds(refreshTokenData.expirySeconds()))
                .build();
    }


}
