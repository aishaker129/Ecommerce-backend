package com.ecommerce.product.entity;

import com.ecommerce.common.entity.AuditableEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category extends AuditableEntity {

    @Column(name = "category_name", nullable = false, length = 120)
    private String name;

    @Column(name = "category_code",nullable = false)
    private String code;

    @Column(name = "is_Active")
    private Boolean isActive = true;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Product> products = new ArrayList<>();
}
