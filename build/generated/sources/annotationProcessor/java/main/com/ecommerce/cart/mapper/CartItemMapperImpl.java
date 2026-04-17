package com.ecommerce.cart.mapper;

import com.ecommerce.cart.dto.response.CartItemResponse;
import com.ecommerce.cart.entity.CartItem;
import com.ecommerce.product.entity.Product;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T02:19:37+0600",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-9.3.1.jar, environment: Java 21.0.10 (Ubuntu)"
)
@Component
public class CartItemMapperImpl implements CartItemMapper {

    @Override
    public CartItemResponse toResponse(CartItem item) {
        if ( item == null ) {
            return null;
        }

        Long productId = null;
        String productName = null;
        String productImage = null;
        Long id = null;
        Double unitPrice = null;
        Integer quantity = null;

        productId = itemProductId( item );
        productName = itemProductName( item );
        productImage = itemProductImageUrl( item );
        id = item.getId();
        unitPrice = item.getUnitPrice();
        quantity = item.getQuantity();

        Double subtotal = item.getUnitPrice() * item.getQuantity();

        CartItemResponse cartItemResponse = new CartItemResponse( id, productId, productName, productImage, unitPrice, quantity, subtotal );

        return cartItemResponse;
    }

    private Long itemProductId(CartItem cartItem) {
        Product product = cartItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getId();
    }

    private String itemProductName(CartItem cartItem) {
        Product product = cartItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getName();
    }

    private String itemProductImageUrl(CartItem cartItem) {
        Product product = cartItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getImageUrl();
    }
}
