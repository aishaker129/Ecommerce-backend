package com.ecommerce.user.mapper;

import com.ecommerce.user.dto.request.RegisterUserRequest;
import com.ecommerce.user.dto.response.RegisterUserResponse;
import com.ecommerce.user.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-24T03:09:44+0600",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.14.3.jar, environment: Java 25.0.2 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(RegisterUserRequest registerUserRequest) {
        if ( registerUserRequest == null ) {
            return null;
        }

        User user = new User();

        user.setFirstName( registerUserRequest.firstName() );
        user.setLastName( registerUserRequest.lastName() );
        user.setEmail( registerUserRequest.email() );
        user.setUsername( registerUserRequest.username() );
        user.setPassword( registerUserRequest.password() );

        return user;
    }

    @Override
    public RegisterUserResponse toResponse(User user) {
        if ( user == null ) {
            return null;
        }

        String username = null;
        String email = null;

        username = user.getUsername();
        email = user.getEmail();

        RegisterUserResponse registerUserResponse = new RegisterUserResponse( username, email );

        return registerUserResponse;
    }
}
