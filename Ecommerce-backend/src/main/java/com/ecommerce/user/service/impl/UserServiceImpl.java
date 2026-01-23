package com.ecommerce.user.service.impl;

import com.ecommerce.user.dto.request.ProfileUpdateRequest;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.entity.UserProfile;
import com.ecommerce.user.mapper.UserProfileMapper;
import com.ecommerce.user.repository.UserProfileRepository;
import com.ecommerce.user.repository.UserRepository;
import com.ecommerce.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(
                ()-> new EntityNotFoundException("User not found with username: "+username)
        );
    }

    @Override
    @Transactional
    public void updateProfile(String username, ProfileUpdateRequest request) {
        User user = userRepository.findByUsername(username).orElseThrow(
                ()-> new EntityNotFoundException("User not found with username: "+username)
        );

        UserProfile userProfile = userProfileRepository.findByUser(user).orElseGet(
                ()->{
                    UserProfile profile = new UserProfile();
                    profile.setUser(user);
                    return profile;
                }
        );

        userProfileMapper.updateEntityFromRequest(request,userProfile);
        userProfileRepository.save(userProfile);

    }


}
