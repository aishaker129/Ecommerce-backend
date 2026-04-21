package com.ecommerce.cart.services.impl;

import com.ecommerce.cart.mapper.CartMapper;
import com.ecommerce.cart.dto.request.CartRequest;
import com.ecommerce.cart.dto.request.UpdateCartItemRequest;
import com.ecommerce.cart.dto.response.CartResponse;
import com.ecommerce.cart.dto.response.CartSummaryResponse;
import com.ecommerce.cart.entity.Cart;
import com.ecommerce.cart.entity.CartItem;
import com.ecommerce.cart.repository.CartItemRepository;
import com.ecommerce.cart.repository.CartRepository;
import com.ecommerce.cart.services.CartService;
import com.ecommerce.common.exceptions.InsufficientStockException;
import com.ecommerce.common.exceptions.ProductInActiveException;
import com.ecommerce.product.entity.Inventory;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.repository.InventoryRepository;
import com.ecommerce.product.repository.ProductRepository;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;
    private final InventoryRepository inventoryRepository;

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCart(Long userId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(()-> createCart(user));


        if (!user.getId().equals(cart.getUser().getId())) {
            throw new AccessDeniedException("You are not allowed to access this cart.");
        }
        CartResponse response = cartMapper.toResponse(cart);

        CartSummaryResponse summary = calculateSummary(cart);
        
        return new CartResponse(
                response.cartId(),
                response.userId(),
                response.items(),
                summary
        );
    }

    @Override
    public Cart getCartByUserId(Long userId){
        return cartRepository.findByUserId(userId).orElseThrow(
                ()-> new EntityNotFoundException("Cart not found for user '"+userId+"'")
        );
    }

    @Override
    public CartResponse addToCart(CartRequest request) throws AccessDeniedException{
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = getOrCreateCart(user);
        
        Product product = productRepository.findById(request.productId()).orElseThrow(
                ()-> new EntityNotFoundException("Product not found with this id '"+request.productId()+"'")
        );

        validateProduct(product,user,request.quantity());

        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(
                cart.getId(),product.getId()
        ).orElse(null);

        if(cartItem != null){
            int newQuantity = cartItem.getQuantity() + request.quantity();

            validateStock(product, newQuantity);
            cartItem.setQuantity(newQuantity);
        }else {
            cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.quantity())
                    .unitPrice(product.getPrice())
                    .build();
            cart.getItems().add(cartItem);
        }
        cartItemRepository.save(cartItem);
        cartItemRepository.flush();

        return refreshCart(user.getId());
    }

    @Transactional
    @Override public CartResponse updateCart(Long cartItemId, UpdateCartItemRequest request) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(
                () -> new EntityNotFoundException("CartItem not found with this id '" + cartItemId + "'")
        );

        validateOwnerShip(cartItem);

        validateStock(cartItem.getProduct(), request.quantity());
        cartItem.setQuantity(request.quantity());

        return refreshCart(cartItem.getCart().getUser().getId());
    }

//    @Override
//    public CartResponse deleteCart(Long cartItemId) {
//        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(
//                () -> new EntityNotFoundException("CartItem not found with this id '" + cartItemId + "'")
//        );
//
//        validateOwnerShip(cartItem);
//
//        cartItemRepository.delete(cartItem);
//        return refreshCart(cartItem.getCart().getUser().getId());
//    }

//    @Override
//    public void clearCart() {
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
//                .getAuthentication()
//                .getPrincipal();
//
//        User user = userRepository.findByUsername(userDetails.getUsername())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Cart cart = getOrCreateCart(user);
//
//        cartItemRepository.deleteAllByCartId(cart.getId());
//    }

    @Override
    public void clearCart(Long userId) {
        Cart cart = getCartByUserId(userId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    private void validateOwnerShip(CartItem cartItem) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!cartItem.getCart().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access to cart.");
        }
    }

    private CartResponse refreshCart(Long userId) {

        Cart cart = cartRepository.findByUserId(userId) // 🔥 use fetch join
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartResponse response = cartMapper.toResponse(cart);

        // 🔥 calculate summary
        int totalItems = cart.getItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();

        double totalPrice = cart.getItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getUnitPrice())
                .sum();

        CartSummaryResponse summary = new CartSummaryResponse(totalItems, totalPrice);

        // 🔥 return new response with summary
        return new CartResponse(
                response.cartId(),
                response.userId(),
                response.items(),
                summary
        );
    }

    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUserId(user.getId()).orElseGet(
                () -> createCart(user)
        );
    }

    private void validateProduct(Product product, User user,Integer quantity) throws AccessDeniedException{
        if(product.getIsActive() != true){
            throw new ProductInActiveException("Product not available");
        }

        if(product.getCreateBy().equals(user.getId())){
            throw new AccessDeniedException("You can not add own product");
        }

        validateStock(product,quantity);
    }

    private void validateStock(Product product, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProduct_Id(product.getId()).orElseThrow(
                ()-> new EntityNotFoundException("Product not found.")
        );
        int available = inventory.getTotalQuantity() - inventory.getReservedQuantity();

        if(quantity > available){
            throw new InsufficientStockException("Not enough stock");
        }
    }

    private CartSummaryResponse calculateSummary(Cart cart) {
        int totalItems = cart.getItems().size();
        double totalPrice = cart.getItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getUnitPrice())
                .sum();
        return new CartSummaryResponse(totalItems, totalPrice);
    }

    private Cart createCart(User user) {
        Cart cart = Cart.builder()
                .user(user)
                .build();
        return cartRepository.save(cart);
    }
}
