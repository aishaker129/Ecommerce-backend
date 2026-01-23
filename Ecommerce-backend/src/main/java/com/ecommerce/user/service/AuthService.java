package com.ecommerce.user.service;

import com.ecommerce.user.dto.LoginResult;
import com.ecommerce.user.dto.RefreshTokenResult;
import com.ecommerce.user.dto.request.LoginRequest;
import com.ecommerce.user.dto.request.RegisterUserRequest;
import com.ecommerce.user.dto.response.LoginResponse;
import com.ecommerce.user.dto.response.RegisterUserResponse;
import jakarta.validation.Valid;

public interface AuthService {
    RegisterUserResponse registerUser(@Valid RegisterUserRequest request);

    LoginResult login(@Valid LoginRequest request);

    void logout(String authorization);

    RefreshTokenResult refreshToken(String rawRefreshToken);
}
