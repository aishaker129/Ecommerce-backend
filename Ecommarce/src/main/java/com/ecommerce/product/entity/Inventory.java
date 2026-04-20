package com.ecommerce.product.entity;

import com.ecommerce.common.entity.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "inventories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Inventory extends AuditableEntity {
    @OneToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "product_id",nullable = false)
    private Product product;

    @Builder.Default
    @Column(name = "total_quantity",nullable = false)
    private Integer totalQuantity = 0;

    @Builder.Default
    @Column(name = "reserved_quantity",nullable = false)
    private Integer reservedQuantity = 0;

    @Version
    private Long version;
}
