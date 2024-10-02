package com.xdpsx.onlineshop.services;

import com.xdpsx.onlineshop.dtos.order.OrderRequest;
import com.xdpsx.onlineshop.dtos.order.OrderResponse;

public interface OrderService {
    OrderResponse placeOrder(String userEmail, OrderRequest orderRequest);
}
