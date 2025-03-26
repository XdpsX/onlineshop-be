package com.xdpsx.onlineshop.dtos.order;

import jakarta.validation.constraints.NotNull;

import com.xdpsx.onlineshop.entities.enums.OrderStatus;

import lombok.Data;

@Data
public class OrderStatusUpdate {
    @NotNull
    private OrderStatus status;
}
