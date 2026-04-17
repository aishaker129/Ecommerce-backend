package com.ecommerce.order.mapper;

import com.ecommerce.cart.entity.CartItem;
import com.ecommerce.order.entity.OrderItem;
import com.ecommerce.product.entity.Product;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T02:19:37+0600",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-9.3.1.jar, environment: Java 21.0.10 (Ubuntu)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderItem toOrderItem(CartItem cartItem) {
        if ( cartItem == null ) {
            return null;
        }

        OrderItem.OrderItemBuilder orderItem = OrderItem.builder();

        orderItem.product( cartItem.getProduct() );
        orderItem.productName( cartItemProductName( cartItem ) );
        orderItem.sku( cartItemProductSku( cartItem ) );
        orderItem.unitPrice( cartItem.getUnitPrice() );
        orderItem.quantity( cartItem.getQuantity() );

        return orderItem.build();
    }

    @Override
    public List<OrderItem> toOrderItems(List<CartItem> cartItems) {
        if ( cartItems == null ) {
            return null;
        }

        List<OrderItem> list = new ArrayList<OrderItem>( cartItems.size() );
        for ( CartItem cartItem : cartItems ) {
            list.add( toOrderItem( cartItem ) );
        }

        return list;
    }

    private String cartItemProductName(CartItem cartItem) {
        Product product = cartItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getName();
    }

    private String cartItemProductSku(CartItem cartItem) {
        Product product = cartItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getSku();
    }
}
