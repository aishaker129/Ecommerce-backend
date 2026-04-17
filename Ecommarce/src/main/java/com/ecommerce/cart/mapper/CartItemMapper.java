package com.ecommerce.cart.mapper;

import com.ecommerce.cart.dto.response.CartItemResponse;
import com.ecommerce.cart.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.imageUrl", target = "productImage")
    @Mapping(target = "subtotal", expression = "java(item.getUnitPrice() * item.getQuantity())")
    CartItemResponse toResponse(CartItem item);
}
