package com.ecommerce.cart.entity;

import com.ecommerce.common.entity.BaseEntity;
import com.ecommerce.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cart extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false,unique = true)
    private User user;

    @Builder.Default
    @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifedAt;

    @PrePersist
    private void onCreate(){
        createdAt = LocalDateTime.now();
        modifedAt = LocalDateTime.now();
    }

    @PreUpdate
    private void onUpdate(){
        modifedAt = LocalDateTime.now();
    }
}
