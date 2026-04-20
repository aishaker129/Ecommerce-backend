package com.ecommerce.user.repository;

import com.ecommerce.user.entity.Role;
import com.ecommerce.user.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByCode(RoleType code);
}
