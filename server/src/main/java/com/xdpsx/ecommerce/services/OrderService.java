package com.xdpsx.ecommerce.services;

import com.xdpsx.ecommerce.dtos.order.OrderRequest;
import com.xdpsx.ecommerce.dtos.order.OrderResponse;
import com.xdpsx.ecommerce.entities.User;

public interface OrderService {
    OrderResponse createOrder(OrderRequest request, User user);
}
