package com.xdpsx.onlineshop.dtos.order;

import com.xdpsx.onlineshop.entities.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderStatusUpdate {
    @NotNull
    private OrderStatus status;
}
