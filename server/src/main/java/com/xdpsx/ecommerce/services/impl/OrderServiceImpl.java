package com.xdpsx.ecommerce.services.impl;

import com.xdpsx.ecommerce.dtos.common.PageResponse;
import com.xdpsx.ecommerce.dtos.order.OrderPageParams;
import com.xdpsx.ecommerce.dtos.order.OrderRequest;
import com.xdpsx.ecommerce.dtos.order.OrderResponse;
import com.xdpsx.ecommerce.entities.*;
import com.xdpsx.ecommerce.exceptions.ResourceNotFoundException;
import com.xdpsx.ecommerce.mappers.OrderMapper;
import com.xdpsx.ecommerce.repositories.OrderRepository;
import com.xdpsx.ecommerce.repositories.ProductRepository;
import com.xdpsx.ecommerce.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public OrderResponse createOrder(OrderRequest request, User user) {
        Order order = new Order();
        double total = 0;
        for (var item: request.getItems()) {
            Product product = productRepository.findEnabledProductsById(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product with id=%s not found!".formatted(item.getProductId())));
            total += product.getPrice() * ((100 - product.getDiscountPercent()) / 100) * item.getQuantity();
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(item.getQuantity())
                    .build();
            order.addItems(orderItem);
        }
        order.setAddress(request.getAddress());
        order.setMobileNumber(request.getMobileNumber());
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setTotal(total);
        String trackingNumber = UUID.randomUUID().toString().replace("-", "")
                .substring(0, 10);
        order.setTrackingNumber(trackingNumber);

        Order savedOrder = orderRepository.save(order);
        OrderResponse orderResponse = orderMapper.fromEntityToResponse(savedOrder);
        return orderResponse;
    }

    @Override
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order with id=%s not found!".formatted(orderId)));
        order.setStatus(status);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.fromEntityToResponse(savedOrder);
    }

    @Override
    public PageResponse<OrderResponse> getListOrders(OrderPageParams params) {
        return null;
    }

}
