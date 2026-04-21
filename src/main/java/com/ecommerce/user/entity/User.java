package com.ecommerce.user.entity;

import com.ecommerce.common.entity.AuditableEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends AuditableEntity implements UserDetails {

    @Column(name = "first_name",nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name",nullable = false,length = 50)
    private String lastName;

    @Column(name = "email",nullable = false,unique = true,length = 50)
    private String email;

    @Column(name = "username",nullable = false,unique = true,length = 50)
    private String username;

    @Column(name = "password",nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        roles.forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getCode().name()));
            role.getPermissions().forEach(permission -> {
                authorities.add(new SimpleGrantedAuthority(permission.getCode().name()));
            });
        });
        return authorities;
    }
}
