package com.ecommerce.cart.services.impl;

import com.ecommerce.cart.dto.request.CartRequest;
import com.ecommerce.cart.dto.response.CartResponse;
import com.ecommerce.cart.entity.Cart;
import com.ecommerce.cart.entity.CartItems;
import com.ecommerce.cart.mapper.CartMapper;
import com.ecommerce.cart.repository.CartRepository;
import com.ecommerce.cart.services.CartService;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartMapper mapper;

    @Transactional
    @Override
    public void addCreateOrUpdateCart(Long productId, CartRequest request) {
        Cart cart = cartRepository.findByUserId(request.userId())
                .orElseGet(()-> cartRepository.save(
                        Cart.builder()
                                .userId(request.userId())
                                .build())
                );

        Product product = productRepository.findById(productId).orElseThrow(
                ()-> new EntityNotFoundException("Product with id '"+productId+"' is not found")
        );

        CartItems exixtingCartItem = cart.getItems().stream()
                .filter(
                    cartItems -> cartItems.getProduct().getId().equals(product.getId())
                ).findFirst().orElse(null);

        if (exixtingCartItem != null){
            if(request.quantity() == 0){
                cart.getItems().remove(exixtingCartItem);
            }
            else{
                exixtingCartItem.setQuantity(request.quantity());
            }
        }
        else{
            CartItems newItems = CartItems.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.quantity())
                    .unitPrice(product.getPrice())
                    .build();
            cart.getItems().add(newItems);
        }
        cartRepository.save(cart);
    }

    @Override
    public CartResponse getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(
                ()-> new EntityNotFoundException("Cart not found for user :"+userId)
        );
        CartResponse response = mapper.toDto(cart);
        return response;
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(
                ()-> new EntityNotFoundException("Cart not found for user :"+userId)
        );
        cart.getItems().clear();
        cartRepository.save(cart);
    }

}
