package com.ecommerce.user.services;

import com.ecommerce.user.entity.Role;
import com.ecommerce.user.enums.RoleType;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findByCode(RoleType roleType);
}
