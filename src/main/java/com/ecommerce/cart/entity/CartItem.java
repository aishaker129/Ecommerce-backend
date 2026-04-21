package com.ecommerce.cart.entity;


import com.ecommerce.common.entity.BaseEntity;
import com.ecommerce.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart_items",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_cart_product",
                columnNames = {"cart_id","product_id"}
        )
)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItem extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "cart_id",nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "product_id",nullable = false)
    private Product product;

    @Column(name = "quantity",nullable = false)
    private Integer quantity;

    @Column(name = "unit_price",nullable = false)
    private Double unitPrice;
}
