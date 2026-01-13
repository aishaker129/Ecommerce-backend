package com.ecommerce.cart.entity;

import com.ecommerce.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@NamedEntityGraph(
        name = "Cart.withItems",
        attributeNodes = @NamedAttributeNode("items")
)
public class Cart extends BaseEntity {

    @Column(name = "user_id",nullable = false)
    private Long userId;

    @Builder.Default
    @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    private List<CartItems> items = new ArrayList<>();

    @Builder.Default
    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    @Column(name = "modified_date")
    private LocalDateTime modifyAt;

    @PrePersist
    protected void onCreate() {
        this.createAt = LocalDateTime.now();
        this.modifyAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifyAt = LocalDateTime.now();
    }

    @Transient
    public double getTotalPrice(){
        return items.stream().mapToDouble(items-> items.getQuantity() * items.getUnitPrice()).sum();
    }



}
