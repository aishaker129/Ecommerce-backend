package com.ecommerce.user.repository;

import com.ecommerce.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);

    @Query("""
    SELECT u FROM User u
    JOIN FETCH u.roles r
    JOIN FETCH r.permissions
    WHERE u.username = :username
""")
    Optional<User> findByUsernameWithRolesAndPermissions(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
