package com.ecommerce.user.services;

import com.ecommerce.product.dto.response.ImageUploadResponse;
import com.ecommerce.user.dto.request.ProfileUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

public interface UserService extends UserDetailsService {
    void updateProfile(String username, @Valid ProfileUpdateRequest request);

    ImageUploadResponse uploadImage(MultipartFile file);

    ImageUploadResponse updateProfileImage(MultipartFile file);
}
