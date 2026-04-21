package com.ecommerce.user.controller;

import com.ecommerce.common.constants.ApiEndPoints;
import com.ecommerce.common.dto.response.ApiResponse;
import com.ecommerce.user.dto.LoginResult;
import com.ecommerce.user.dto.RefreshTokenResult;
import com.ecommerce.user.dto.request.LoginRequest;
import com.ecommerce.user.dto.request.UserRegisterRequest;
import com.ecommerce.user.dto.response.LoginResponse;
import com.ecommerce.user.dto.response.RefreshTokenResponse;
import com.ecommerce.user.dto.response.RegisteredUserResponse;
import com.ecommerce.user.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiEndPoints.Auth.BASE_AUTH)
@Tag(
        name = "Authentication",
        description = "Endpoints for authentication like user register and login"
)
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account with profile information.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "User registered successfully",
                            content = @Content(schema = @Schema(implementation = RegisteredUserResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "409",
                            description = "Conflict - Username or email already exists",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    )
            }
    )
    @PostMapping(ApiEndPoints.Auth.REGISTER)
    public ResponseEntity<ApiResponse<RegisteredUserResponse>> registerUser(@Valid @RequestBody UserRegisterRequest request){
        RegisteredUserResponse userResponse = authService.registerUser(request);
        return ResponseEntity.ok(ApiResponse.success("User register successfully",userResponse));
    }

    @Operation(
            summary = "Authenticate user",
            description = "Authenticates a user and returns a JWT access token.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "User logged in successfully",
                            content = @Content(schema = @Schema(implementation = LoginResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - Invalid credentials",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    )
            }
    )
    @PostMapping(ApiEndPoints.Auth.LOGIN)
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request,
                                                            HttpServletResponse response){
        LoginResult result = authService.login(request);

        ResponseCookie responseCookie = ResponseCookie.from("refresh_token",result.refreshToken())
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path(ApiEndPoints.Auth.BASE_AUTH + ApiEndPoints.Auth.TOKEN_REFRESH)
                .maxAge(result.refreshTokenDuration())
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE,responseCookie.toString());

        return ResponseEntity.ok(ApiResponse.success("User logged in successfully.",result.loginResponse()));
    }

    @PostMapping(ApiEndPoints.Auth.LOGOUT)
    public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        authService.logout(authorizationHeader);
        return ResponseEntity.ok(ApiResponse.success("User logged out successfully."));
    }

    @PostMapping(ApiEndPoints.Auth.TOKEN_REFRESH)
    public ResponseEntity<ApiResponse<RefreshTokenResponse>> refreshToken(@CookieValue(name = "refresh_token",required = false) String rawRefreshToken,
                                                                          HttpServletResponse response){
        if(rawRefreshToken == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("Refresh token missing"));
        }

        RefreshTokenResult result = authService.refreshToken(rawRefreshToken);
        ResponseCookie cookie = ResponseCookie.from("refresh_token",result.refreshToken())
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path(ApiEndPoints.Auth.BASE_AUTH + ApiEndPoints.Auth.TOKEN_REFRESH)
                .maxAge(result.refreshTokenDuration())
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE,cookie.toString());
        return ResponseEntity.ok(ApiResponse.success("Token refresh successfully",result.RefreshtokenResponse()));

    }
}
