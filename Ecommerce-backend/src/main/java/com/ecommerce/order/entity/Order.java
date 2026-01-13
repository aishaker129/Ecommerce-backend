package com.ecommerce.order.entity;

import com.ecommerce.common.entity.BaseEntity;
import com.ecommerce.order.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order extends BaseEntity {
    @Column(name = "user_id",nullable = false)
    private Long userId;

    @Builder.Default
    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(name = "sub_total",nullable = false)
    private Double subTotal;

    @Column(name = "discount_amount",nullable = false)
    private Double discountAmount;

    @Column(name = "delivery_charge",nullable = false)
    private Double deliveryCharge;

    @Column(name = "total_price",nullable = false)
    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false)
    private OrderStatus status;

    @Column(name = "create_date")
    private LocalDateTime createAt;

    @PrePersist
    public void onCreate(){
        createAt = LocalDateTime.now();
    }

}
