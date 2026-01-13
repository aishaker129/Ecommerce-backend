package com.ecommerce.cart.mapper;

import com.ecommerce.cart.dto.response.CartItemsResponse;
import com.ecommerce.cart.dto.response.CartResponse;
import com.ecommerce.cart.entity.Cart;
import com.ecommerce.cart.entity.CartItems;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CartMapper {
    @Mapping(target = "items", source = "items")
    CartResponse toDto(Cart cart);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "unitPrice", source = "product.price")
    @Mapping(target = "quantity", source = "quantity")
    CartItemsResponse toItemDto(CartItems item);

    List<CartItemsResponse> toItemDtoList(List<CartItems> items);
}
