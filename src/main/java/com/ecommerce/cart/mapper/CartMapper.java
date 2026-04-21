package com.ecommerce.cart.mapper;

import com.ecommerce.cart.dto.response.CartResponse;
import com.ecommerce.cart.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",uses = CartItemMapper.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CartMapper {
    @Mapping(source = "id", target = "cartId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "items", target = "items")
//    @Mapping(target = "summary", ignore = true) // we’ll calculate manually
    CartResponse toResponse(Cart cart);
}
