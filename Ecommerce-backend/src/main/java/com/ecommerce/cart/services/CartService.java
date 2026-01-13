package com.ecommerce.cart.services;

import com.ecommerce.cart.dto.request.CartRequest;
import com.ecommerce.cart.dto.response.CartResponse;
import com.ecommerce.cart.entity.Cart;
import jakarta.validation.Valid;

public interface CartService {
    void addCreateOrUpdateCart(Long productId, @Valid CartRequest request);

    CartResponse getCartByUserId(Long userId);
    void clearCart(Long userId);
}
