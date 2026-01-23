package com.ecommerce.cart.mapper;

import com.ecommerce.cart.dto.response.CartItemsResponse;
import com.ecommerce.cart.dto.response.CartResponse;
import com.ecommerce.cart.entity.Cart;
import com.ecommerce.cart.entity.CartItems;
import com.ecommerce.product.entity.Product;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-24T03:09:44+0600",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.14.3.jar, environment: Java 25.0.2 (Oracle Corporation)"
)
@Component
public class CartMapperImpl implements CartMapper {

    @Override
    public CartResponse toDto(Cart cart) {
        if ( cart == null ) {
            return null;
        }

        CartResponse.CartResponseBuilder cartResponse = CartResponse.builder();

        cartResponse.items( toItemDtoList( cart.getItems() ) );
        cartResponse.id( cart.getId() );
        cartResponse.userId( cart.getUserId() );
        cartResponse.totalPrice( cart.getTotalPrice() );

        return cartResponse.build();
    }

    @Override
    public CartItemsResponse toItemDto(CartItems item) {
        if ( item == null ) {
            return null;
        }

        Long productId = null;
        String productName = null;
        Double unitPrice = null;
        Integer quantity = null;

        productId = itemProductId( item );
        productName = itemProductName( item );
        unitPrice = itemProductPrice( item );
        quantity = item.getQuantity();

        CartItemsResponse cartItemsResponse = new CartItemsResponse( productId, productName, quantity, unitPrice );

        return cartItemsResponse;
    }

    @Override
    public List<CartItemsResponse> toItemDtoList(List<CartItems> items) {
        if ( items == null ) {
            return null;
        }

        List<CartItemsResponse> list = new ArrayList<CartItemsResponse>( items.size() );
        for ( CartItems cartItems : items ) {
            list.add( toItemDto( cartItems ) );
        }

        return list;
    }

    private Long itemProductId(CartItems cartItems) {
        Product product = cartItems.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getId();
    }

    private String itemProductName(CartItems cartItems) {
        Product product = cartItems.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getName();
    }

    private Double itemProductPrice(CartItems cartItems) {
        Product product = cartItems.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getPrice();
    }
}
