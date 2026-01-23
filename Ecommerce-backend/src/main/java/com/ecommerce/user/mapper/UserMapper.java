package com.ecommerce.user.mapper;

import com.ecommerce.user.dto.request.RegisterUserRequest;
import com.ecommerce.user.dto.response.RegisterUserResponse;
import com.ecommerce.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    @Mapping(target = "id",ignore = true)
    User toEntity(RegisterUserRequest registerUserRequest);

    RegisterUserResponse toResponse(User user);
}
