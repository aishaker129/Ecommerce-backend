package com.ecommerce.user.service;

import com.ecommerce.user.dto.request.ProfileUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    void updateProfile(String username, @Valid ProfileUpdateRequest request);
}
