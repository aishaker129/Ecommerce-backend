package com.ecommerce.user.mapper;

import com.ecommerce.user.dto.request.UserRegisterRequest;
import com.ecommerce.user.dto.response.RegisteredUserResponse;
import com.ecommerce.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "id",ignore = true)
    User toEntity(UserRegisterRequest request);

    RegisteredUserResponse toDto(User user);
}
