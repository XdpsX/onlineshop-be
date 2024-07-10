package com.xdpsx.ecommerce.mappers;

import com.xdpsx.ecommerce.dtos.order.OrderResponse;
import com.xdpsx.ecommerce.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "items", source = "entity.items")
    OrderResponse fromEntityToResponse(Order entity);
}
