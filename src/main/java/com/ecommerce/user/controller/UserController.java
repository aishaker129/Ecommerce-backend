package com.ecommerce.user.controller;

import com.ecommerce.common.constants.ApiEndPoints;
import com.ecommerce.common.dto.response.ApiResponse;
import com.ecommerce.product.dto.response.ImageUploadResponse;
import com.ecommerce.user.dto.request.ProfileUpdateRequest;
import com.ecommerce.user.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiEndPoints.User.BASE_USER)
@Tag(
        name = "User",
        description = "Operation for managing user profile."
)
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Update user profile.",
            description = "Update the current logged in user profile."
    )
    @PreAuthorize("hasAuthority(T(com.ecommerce.user.enums.PermissionType).UPDATE_PROFILE.name()) and #username == authentication.name")
    @PutMapping(ApiEndPoints.User.PROFILE)
    public ResponseEntity<ApiResponse<Void>> updateProfile(@PathVariable String username,@Valid @RequestBody ProfileUpdateRequest request){
        userService.updateProfile(username,request);
        return ResponseEntity.ok(ApiResponse.success("User profile updated successfully",null));
    }

    @PreAuthorize("hasAuthority(T(com.ecommerce.user.enums.PermissionType).UPLOAD_PROFILE_IMAGE.name())")
    @PatchMapping(path = ApiEndPoints.User.UPLOAD_PROFILE_IMAGE,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ImageUploadResponse>> uploadImage(@RequestParam MultipartFile file){
        return ResponseEntity.ok(ApiResponse.success("Upload profile image successfully.",userService.uploadImage(file)));
    }

    @PreAuthorize("hasAuthority(T(com.ecommerce.user.enums.PermissionType).UPDATE_PROFILE_IMAGE.name())")
    @PatchMapping(path = ApiEndPoints.User.UPDATE_PROFILE_IMAGE,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ImageUploadResponse>> updateProfileImage(@RequestParam MultipartFile file){
        return ResponseEntity.ok(ApiResponse.success(" Profile image updated successfully.",userService.updateProfileImage(file)));
    }
}
