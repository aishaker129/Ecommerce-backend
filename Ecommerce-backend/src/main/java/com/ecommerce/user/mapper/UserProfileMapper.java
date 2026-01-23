package com.ecommerce.user.mapper;

import com.ecommerce.user.dto.request.ProfileCreateRequest;
import com.ecommerce.user.dto.request.ProfileUpdateRequest;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.entity.UserProfile;
import org.mapstruct.*;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserProfileMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    UserProfile toEntity(ProfileCreateRequest request, User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(ProfileUpdateRequest request, @MappingTarget UserProfile entity);
}
