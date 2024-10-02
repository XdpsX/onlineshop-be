package com.xdpsx.onlineshop.mappers;

import com.xdpsx.onlineshop.dtos.order.OrderRequest;
import com.xdpsx.onlineshop.entities.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class OrderMapper {
    public abstract Order fromRequestToEntity(OrderRequest request);
}
