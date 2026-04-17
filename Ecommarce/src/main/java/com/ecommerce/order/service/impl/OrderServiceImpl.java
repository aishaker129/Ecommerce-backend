package com.ecommerce.order.service.impl;

import com.ecommerce.cart.entity.Cart;
import com.ecommerce.order.entity.Order;
import com.ecommerce.order.entity.OrderItem;
import com.ecommerce.order.enums.OrderStatus;
import com.ecommerce.order.mapper.OrderMapper;
import com.ecommerce.order.repository.OrderRepository;
import com.ecommerce.order.service.OrderService;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;

    @Override
    public Order createOrderFromCart(Long userId, Cart cart) {
        double subTotal = cart.getItems().stream().mapToDouble(item-> item.getQuantity() * item.getUnitPrice()).sum();

        double discount = 0.0;
        double delivaryCharge = 50.0;
        double totalPrice = subTotal - discount + delivaryCharge;

        User user = userRepository.findById(userId).orElseThrow(
                ()-> new EntityNotFoundException("User not found with id '"+userId+"'")
        );

        Order order = Order.builder()
                .user(user)
                .subTotal(subTotal)
                .discountAmount(discount)
                .deliveryCharge(delivaryCharge)
                .totalPrice(totalPrice)
                .status(OrderStatus.NEW)
                .build();

        List<OrderItem> items =orderMapper.toOrderItems(cart.getItems());
        items.forEach(item -> item.setOrder(order));

        order.setItems(items);

        return orderRepository.save(order);
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }
}
