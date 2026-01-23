package com.ecommerce.user.controller;

import com.ecommerce.common.constants.ApiEndpoint;
import com.ecommerce.common.dto.resonse.ApiResponse;
import com.ecommerce.user.dto.request.ProfileUpdateRequest;
import com.ecommerce.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiEndpoint.User.BASE_USER)
@RequiredArgsConstructor
@Tag(
        name = "User",
        description = "Operation for managing user profile"
)
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Update user profile",
            description = "Updated the profile of the  currently logged-in user."
    )
    @PutMapping(ApiEndpoint.User.PROFILE)
    public ResponseEntity<ApiResponse<Void>> updateProfile(@PathVariable String username, @Valid @RequestBody ProfileUpdateRequest request){
        userService.updateProfile(username,request);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully",null));
    }
}
