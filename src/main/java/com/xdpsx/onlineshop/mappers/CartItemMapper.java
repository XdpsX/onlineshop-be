package com.xdpsx.onlineshop.mappers;

import com.xdpsx.onlineshop.dtos.cart.CartItemResponse;
import com.xdpsx.onlineshop.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class CartItemMapper {
    @Autowired
    private ProductMapper productMapper;

    @Mapping(target = "product", ignore = true)
    abstract CartItemResponse toResponse(CartItem entity);

    public CartItemResponse fromEntityToResponse(CartItem entity) {
        CartItemResponse response = toResponse(entity);
        response.setProduct(productMapper.fromEntityToResponse(entity.getProduct()));
        return response;
    }
}
