package com.ecommerce.order.mapper;

import com.ecommerce.cart.entity.CartItem;
import com.ecommerce.order.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

    @Mapping(target = "order", ignore = true)
    @Mapping(target = "product", source = "product")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "sku", source = "product.sku")
    @Mapping(target = "unitPrice", source = "unitPrice")
    @Mapping(target = "quantity", source = "quantity")
    OrderItem toOrderItem(CartItem cartItem);

    List<OrderItem> toOrderItems(List<CartItem> cartItems);
}