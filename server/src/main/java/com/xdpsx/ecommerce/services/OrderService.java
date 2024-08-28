package com.xdpsx.ecommerce.services;

import com.xdpsx.ecommerce.dtos.common.PageResponse;
import com.xdpsx.ecommerce.dtos.order.OrderPageParams;
import com.xdpsx.ecommerce.dtos.order.OrderRequest;
import com.xdpsx.ecommerce.dtos.order.OrderResponse;
import com.xdpsx.ecommerce.entities.OrderStatus;
import com.xdpsx.ecommerce.entities.User;

public interface OrderService {
    OrderResponse createOrder(OrderRequest request, User user);
    OrderResponse updateOrderStatus(Long orderId, OrderStatus status);
    PageResponse<OrderResponse> getListOrders(OrderPageParams params);
}
