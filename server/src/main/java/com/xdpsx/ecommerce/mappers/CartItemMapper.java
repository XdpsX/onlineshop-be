package com.xdpsx.ecommerce.mappers;

import com.xdpsx.ecommerce.dtos.cart.CartItemRequest;
import com.xdpsx.ecommerce.dtos.cart.CartItemResponse;
import com.xdpsx.ecommerce.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    CartItem fromRequestToEntity(CartItemRequest request);

    @Mapping(target = "product", source = "entity.product")
    CartItemResponse fromEntityToResponse(CartItem entity);
}
