package com.ecommerce.user.mapper;

import com.ecommerce.user.dto.request.ProfileCreateRequest;
import com.ecommerce.user.dto.request.ProfileUpdateRequest;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.entity.UserProfile;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-24T03:09:44+0600",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.14.3.jar, environment: Java 25.0.2 (Oracle Corporation)"
)
@Component
public class UserProfileMapperImpl implements UserProfileMapper {

    @Override
    public UserProfile toEntity(ProfileCreateRequest request, User user) {
        if ( request == null && user == null ) {
            return null;
        }

        UserProfile userProfile = new UserProfile();

        if ( request != null ) {
            userProfile.setPhoneNumber( request.phoneNumber() );
            userProfile.setDateOfBirth( request.dateOfBirth() );
            userProfile.setGender( request.gender() );
        }
        userProfile.setUser( user );

        return userProfile;
    }

    @Override
    public void updateEntityFromRequest(ProfileUpdateRequest request, UserProfile entity) {
        if ( request == null ) {
            return;
        }

        if ( request.phoneNumber() != null ) {
            entity.setPhoneNumber( request.phoneNumber() );
        }
        if ( request.dateOfBirth() != null ) {
            entity.setDateOfBirth( request.dateOfBirth() );
        }
        if ( request.gender() != null ) {
            entity.setGender( request.gender() );
        }
    }
}
