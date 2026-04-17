package com.ecommerce.product.entity;

import com.ecommerce.common.entity.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product extends AuditableEntity {

   @Column(name = "sku",nullable = false,unique = true)
    private String sku;

    @Column(name = "product_name",nullable = false)
    private String name;

   @Column(name = "product_description")
    private String description;

   @Column(name = "product_price",nullable = false)
    private Double price;

   @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "product_image")
    private String imageUrl;

    @Column(name = "image_public_id")
    private String imagePublicId;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;
}
