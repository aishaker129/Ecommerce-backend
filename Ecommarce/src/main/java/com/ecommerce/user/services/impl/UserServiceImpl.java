package com.ecommerce.user.services.impl;

import com.ecommerce.product.dto.response.ImageUploadResponse;
import com.ecommerce.product.services.impl.ImageUploadService;
import com.ecommerce.user.dto.request.ProfileUpdateRequest;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.entity.UserProfile;
import com.ecommerce.user.mapper.ProfileMapper;
import com.ecommerce.user.repository.UserProfileRepository;
import com.ecommerce.user.repository.UserRepository;
import com.ecommerce.user.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Primary
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final ProfileMapper profileMapper;
    private final ImageUploadService imageUploadService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {

        User user = userRepository.findByUsernameWithRolesAndPermissions(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Set<GrantedAuthority> authorities = new HashSet<>();

        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getCode().name()));

            role.getPermissions().forEach(p ->
                    authorities.add(new SimpleGrantedAuthority(p.getCode().name()))
            );
        });

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
    @Override
    public void updateProfile(String username, ProfileUpdateRequest request) {
        User user = userRepository.findByUsername(username).orElseThrow(
                ()-> new EntityNotFoundException("User not found with username '"+username+"'.")
        );

        UserProfile profile = userProfileRepository.findByUser(user).orElseGet(
                ()->{
                    UserProfile userProfile = new UserProfile();
                    userProfile.setUser(user);
                    return userProfile;
                }
        );

        profileMapper.updateEntityFromRequest(request,profile);
        userProfileRepository.save(profile);
    }

    @Override
    public ImageUploadResponse uploadImage(MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username).orElseThrow(
                ()-> new EntityNotFoundException("User not found with username '"+username+"'.")
        );

        validateImageFile(file);

        UserProfile profile = userProfileRepository.findByUser(user).orElseGet(
                ()->{
                    UserProfile profile1 = new UserProfile();
                    profile1.setUser(user);
                    return profile1;
                }
        );

        if(profile.getImagePublicId() != null){
            imageUploadService.deleteImage(profile.getImagePublicId());
        }

        ImageUploadResponse response = imageUploadService.uploadImage(file,"CampusKart/profiles");

        profile.setImageUrl(response.imageUrl());
        profile.setImagePublicId(response.imagePublicId());
        profile.setModifiedAt(LocalDateTime.now());
        userProfileRepository.save(profile);

        return response;
    }

    @Override
    public ImageUploadResponse updateProfileImage(MultipartFile file) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username).orElseThrow(
                ()-> new EntityNotFoundException("User not found with username '"+username+"'.")
        );

        validateImageFile(file);

        UserProfile profile = userProfileRepository.findByUser(user).orElseGet(
                ()->{
                    UserProfile profile1 = new UserProfile();
                    profile1.setUser(user);
                    return profile1;
                }
        );

        if(profile.getImagePublicId() != null){
            imageUploadService.deleteImage(profile.getImagePublicId());
        }

        ImageUploadResponse response = imageUploadService.uploadImage(file,"CampusKart/profiles");

        profile.setImageUrl(response.imageUrl());
        profile.setImagePublicId(response.imagePublicId());
        profile.setModifiedAt(LocalDateTime.now());
        userProfileRepository.save(profile);

        return response;
    }

    private void validateImageFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Image is required");
        }

        if (!file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Only image files allowed");
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("Max size is 2MB");
        }

    }
}
