package com.xdpsx.ecommerce.dtos.order;

import com.xdpsx.ecommerce.entities.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderStatusUpdateRequest {
    @NotNull(message = "Order status must not be null")
    private OrderStatus status;
}
