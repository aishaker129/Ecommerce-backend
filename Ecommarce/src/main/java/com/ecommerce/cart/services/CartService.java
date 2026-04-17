package com.ecommerce.cart.services;

import com.ecommerce.cart.dto.request.CartRequest;
import com.ecommerce.cart.dto.request.UpdateCartItemRequest;
import com.ecommerce.cart.dto.response.CartResponse;
import com.ecommerce.cart.entity.Cart;
import jakarta.validation.Valid;

public interface CartService {
    CartResponse getCart(Long userId);
    Cart getCartByUserId(Long userId);

    CartResponse addToCart(@Valid CartRequest request);

    CartResponse updateCart(Long cartItemId, @Valid UpdateCartItemRequest request);

//    CartResponse deleteCart(Long cartItemId);
//
//    void clearCart();

    void clearCart(Long userId);
}
