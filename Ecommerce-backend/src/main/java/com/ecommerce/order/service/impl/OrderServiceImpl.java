package com.ecommerce.order.service.impl;

import com.ecommerce.cart.entity.Cart;
import com.ecommerce.order.entity.Order;
import com.ecommerce.order.entity.OrderItem;
import com.ecommerce.order.enums.OrderStatus;
import com.ecommerce.order.mapper.OrderMapper;
import com.ecommerce.order.repository.OrderRepository;
import com.ecommerce.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of {@link OrderService} for order processing.
 *
 * <p>Handles order creation from cart with pricing calculations
 * including subtotal, discount, and delivery charges.</p>
 *
 * @author Md. Akhlakul Islam
 */

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository repository;
    private final OrderMapper mapper;

    @Override
    public Order createOrderFromCart(Long userId, Cart cart) {
        double subTotal = cart.getItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getUnitPrice())
                .sum();

        double discount = 0;
        double deliveryCharge = 50.0;
        double totalPrice = subTotal - deliveryCharge + deliveryCharge;

        Order order = Order.builder()
                .userId(userId)
                .subTotal(subTotal)
                .discountAmount(discount)
                .deliveryCharge(deliveryCharge)
                .totalPrice(totalPrice)
                .status(OrderStatus.NEW)
                .build();

        List<OrderItem> orderItems = mapper.toOrderItems(cart.getItems());
        orderItems.forEach(item -> item.setOrder(order));

        order.setOrderItems(orderItems);

        return repository.save(order);
    }

    @Override
    public Order save(Order order) {
        return repository.save(order);
    }

}
