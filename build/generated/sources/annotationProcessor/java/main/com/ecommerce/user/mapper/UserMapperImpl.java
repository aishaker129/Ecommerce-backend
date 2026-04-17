package com.ecommerce.user.mapper;

import com.ecommerce.user.dto.request.UserRegisterRequest;
import com.ecommerce.user.dto.response.RegisteredUserResponse;
import com.ecommerce.user.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T02:19:37+0600",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-9.3.1.jar, environment: Java 21.0.10 (Ubuntu)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(UserRegisterRequest request) {
        if ( request == null ) {
            return null;
        }

        User user = new User();

        user.setFirstName( request.firstName() );
        user.setLastName( request.lastName() );
        user.setEmail( request.email() );
        user.setUsername( request.username() );
        user.setPassword( request.password() );

        return user;
    }

    @Override
    public RegisteredUserResponse toDto(User user) {
        if ( user == null ) {
            return null;
        }

        String username = null;
        String email = null;

        username = user.getUsername();
        email = user.getEmail();

        RegisteredUserResponse registeredUserResponse = new RegisteredUserResponse( username, email );

        return registeredUserResponse;
    }
}
