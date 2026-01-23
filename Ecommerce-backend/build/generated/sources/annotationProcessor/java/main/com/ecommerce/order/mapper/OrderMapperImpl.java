package com.ecommerce.order.mapper;

import com.ecommerce.cart.entity.CartItems;
import com.ecommerce.order.entity.OrderItem;
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
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderItem toOrderItem(CartItems cartItem) {
        if ( cartItem == null ) {
            return null;
        }

        OrderItem orderItem = new OrderItem();

        orderItem.setProductId( cartItemProductId( cartItem ) );
        orderItem.setProductName( cartItemProductName( cartItem ) );
        orderItem.setSku( cartItemProductSku( cartItem ) );
        orderItem.setQuantity( cartItem.getQuantity() );
        orderItem.setUnitPrice( cartItem.getUnitPrice() );

        return orderItem;
    }

    @Override
    public List<OrderItem> toOrderItems(List<CartItems> cartItems) {
        if ( cartItems == null ) {
            return null;
        }

        List<OrderItem> list = new ArrayList<OrderItem>( cartItems.size() );
        for ( CartItems cartItems1 : cartItems ) {
            list.add( toOrderItem( cartItems1 ) );
        }

        return list;
    }

    private Long cartItemProductId(CartItems cartItems) {
        Product product = cartItems.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getId();
    }

    private String cartItemProductName(CartItems cartItems) {
        Product product = cartItems.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getName();
    }

    private String cartItemProductSku(CartItems cartItems) {
        Product product = cartItems.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getSku();
    }
}
