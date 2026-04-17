package com.ecommerce.order.service;

import com.ecommerce.cart.entity.Cart;
import com.ecommerce.order.entity.Order;

public interface OrderService {
    Order createOrderFromCart(Long userId, Cart cart);

    Order save(Order order);
}
