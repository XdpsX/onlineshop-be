package com.xdpsx.onlineshop.services;

import com.xdpsx.onlineshop.dtos.common.PageResponse;
import com.xdpsx.onlineshop.dtos.order.OrderDTO;
import com.xdpsx.onlineshop.dtos.order.OrderDetailsDTO;
import com.xdpsx.onlineshop.dtos.order.OrderRequest;
import com.xdpsx.onlineshop.dtos.order.OrderResponse;

public interface OrderService {
    OrderResponse placeOrder(String userEmail, OrderRequest orderRequest);
    void payment(String userEmail, long orderId);
    PageResponse<OrderDTO> getMyOrders(String userEmail, int pageNum, int pageSize);
    OrderDetailsDTO getOrderByTrackingNumber(String name, String trackingNumber);
    OrderDetailsDTO getOrderById(Long orderId);
}
