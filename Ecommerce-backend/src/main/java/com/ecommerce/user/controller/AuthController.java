package com.ecommerce.user.controller;

import com.ecommerce.common.constants.ApiEndpoint;
import com.ecommerce.common.dto.resonse.ApiResponse;
import com.ecommerce.user.dto.LoginResult;
import com.ecommerce.user.dto.RefreshTokenResult;
import com.ecommerce.user.dto.request.LoginRequest;
import com.ecommerce.user.dto.request.RegisterUserRequest;
import com.ecommerce.user.dto.response.LoginResponse;
import com.ecommerce.user.dto.response.RefreshTokenResponse;
import com.ecommerce.user.dto.response.RegisterUserResponse;
import com.ecommerce.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiEndpoint.Auth.BASE_AUTH)
@RequiredArgsConstructor
@Tag(
        name = "Authentication",
        description = "Endpoints for user registration, login and logut"
)
public class AuthController {
    private final AuthService authService;

    @Operation(
            summary = "Register a new user",
            description = "Create a new user with profile information",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "User registered successfully",
                            content = @Content(schema = @Schema(implementation = RegisterUserResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "409",
                            description = "Username or email already exists",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    )
            }
    )
    @PostMapping(ApiEndpoint.Auth.REGISTER)
    public ResponseEntity<ApiResponse<RegisterUserResponse>> register(@Valid @RequestBody RegisterUserRequest request){
        RegisterUserResponse user = authService.registerUser(request);
        return ResponseEntity.ok(ApiResponse.success("User register successfully",user));
    }

    @Operation(
            summary = "Authenticate user",
            description = "Authenticated a user and return a JWT access token.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "User logged in successfully.",
                            content = @Content(schema = @Schema(implementation = LoginResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - Invalid credential.",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    )
            }
    )
    @PostMapping(ApiEndpoint.Auth.LOGIN)
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response){
        LoginResult loggedinUser = authService.login(request);

        ResponseCookie responseCookie = ResponseCookie.from("refresh-token",loggedinUser.refreshToken())
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path(ApiEndpoint.Auth.BASE_AUTH+ApiEndpoint.Auth.TOKEN_REFRESH)
                .maxAge(loggedinUser.refreshTokenDuration())
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE,responseCookie.toString());

        return ResponseEntity.ok(ApiResponse.success("User logged in successfully",loggedinUser.loginResponse()));
    }

    @PostMapping(ApiEndpoint.Auth.LOGOUT)
    public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        authService.logout(authorization);
        return ResponseEntity.ok(ApiResponse.success("User logout successfully."));
    }

    @PostMapping(ApiEndpoint.Auth.TOKEN_REFRESH)
    public ResponseEntity<ApiResponse<RefreshTokenResponse>> refreshToken(@CookieValue(value = "refresh-token",required = false) String rawRefreshToken, HttpServletResponse response){
        if(rawRefreshToken == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("Refresh token missing"));
        }

        RefreshTokenResult refreshTokenResult = authService.refreshToken(rawRefreshToken);

        ResponseCookie responseCookie = ResponseCookie.from("refresh-token",refreshTokenResult.refreshToken())
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path(ApiEndpoint.Auth.BASE_AUTH + ApiEndpoint.Auth.TOKEN_REFRESH)
                .maxAge(refreshTokenResult.refreshTokenDuration())
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE,responseCookie.toString());

        return ResponseEntity.ok(ApiResponse.success("Token refresh successfully",refreshTokenResult.refreshTokenResponse()));
    }
}
