package com.ecommerce.user.services;

import com.ecommerce.user.dto.LoginResult;
import com.ecommerce.user.dto.RefreshTokenResult;
import com.ecommerce.user.dto.request.LoginRequest;
import com.ecommerce.user.dto.request.UserRegisterRequest;
import com.ecommerce.user.dto.response.RegisteredUserResponse;
import jakarta.validation.Valid;

public interface AuthService {
    RegisteredUserResponse registerUser(@Valid UserRegisterRequest request);

    LoginResult login(@Valid LoginRequest request);

    void logout(String authorizationHeader);

    RefreshTokenResult refreshToken(String rawRefreshToken);
}
