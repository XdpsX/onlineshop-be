package com.xdpsx.onlineshop.mappers;

import com.xdpsx.onlineshop.dtos.order.OrderDetailsDTO;
import com.xdpsx.onlineshop.dtos.order.OrderItemResponse;
import com.xdpsx.onlineshop.dtos.order.OrderRequest;
import com.xdpsx.onlineshop.entities.Order;
import com.xdpsx.onlineshop.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class OrderMapper {
    @Autowired
    private ProductMapper productMapper;

    public abstract Order fromRequestToEntity(OrderRequest request);

    @Mapping(target = "status", source = "entity.status")
    @Mapping(target = "paymentStatus", source = "entity.payment.status")
    @Mapping(target = "total", source = "entity.totalAmount")
    @Mapping(target = "username", source = "entity.user.name")
    @Mapping(target = "items", ignore = true)
    abstract OrderDetailsDTO toDetails(Order entity);

    public OrderDetailsDTO fromEntityToDetails(Order entity) {
        OrderDetailsDTO dto = toDetails(entity);
        List<OrderItemResponse> items = entity.getItems().stream()
            .map(orderItem -> {
                Product product = orderItem.getProduct();
                return OrderItemResponse.builder()
                        .id(orderItem.getId())
                        .product(productMapper.fromEntityToResponse(product))
                        .quantity(orderItem.getQuantity())
                        .build();
            })
            .toList();
        dto.setItems(items);
        return dto;
    }
}
