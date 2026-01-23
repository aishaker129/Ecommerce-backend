package com.ecommerce.user.service.impl;

import com.ecommerce.user.entity.Role;
import com.ecommerce.user.enums.RoleType;
import com.ecommerce.user.repository.RoleRepository;
import com.ecommerce.user.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    @Override
    public Optional<Role> findByCode(RoleType code) {
        return roleRepository.findByCode(code);
    }
}
