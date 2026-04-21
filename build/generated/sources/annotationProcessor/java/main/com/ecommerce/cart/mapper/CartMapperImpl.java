package com.ecommerce.cart.mapper;

import com.ecommerce.cart.dto.response.CartItemResponse;
import com.ecommerce.cart.dto.response.CartResponse;
import com.ecommerce.cart.dto.response.CartSummaryResponse;
import com.ecommerce.cart.entity.Cart;
import com.ecommerce.cart.entity.CartItem;
import com.ecommerce.user.entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T02:19:37+0600",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-9.3.1.jar, environment: Java 21.0.10 (Ubuntu)"
)
@Component
public class CartMapperImpl implements CartMapper {

    @Autowired
    private CartItemMapper cartItemMapper;

    @Override
    public CartResponse toResponse(Cart cart) {
        if ( cart == null ) {
            return null;
        }

        Long cartId = null;
        Long userId = null;
        List<CartItemResponse> items = null;

        cartId = cart.getId();
        userId = cartUserId( cart );
        items = cartItemListToCartItemResponseList( cart.getItems() );

        CartSummaryResponse summary = null;

        CartResponse cartResponse = new CartResponse( cartId, userId, items, summary );

        return cartResponse;
    }

    private Long cartUserId(Cart cart) {
        User user = cart.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getId();
    }

    protected List<CartItemResponse> cartItemListToCartItemResponseList(List<CartItem> list) {
        if ( list == null ) {
            return null;
        }

        List<CartItemResponse> list1 = new ArrayList<CartItemResponse>( list.size() );
        for ( CartItem cartItem : list ) {
            list1.add( cartItemMapper.toResponse( cartItem ) );
        }

        return list1;
    }
}
