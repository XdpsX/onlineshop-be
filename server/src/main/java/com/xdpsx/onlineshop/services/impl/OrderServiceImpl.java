package com.xdpsx.onlineshop.services.impl;

import com.xdpsx.onlineshop.dtos.order.OrderDTO;
import com.xdpsx.onlineshop.dtos.order.OrderRequest;
import com.xdpsx.onlineshop.dtos.order.OrderResponse;
import com.xdpsx.onlineshop.entities.*;
import com.xdpsx.onlineshop.entities.enums.OrderStatus;
import com.xdpsx.onlineshop.exceptions.BadRequestException;
import com.xdpsx.onlineshop.exceptions.ResourceNotFoundException;
import com.xdpsx.onlineshop.mappers.OrderMapper;
import com.xdpsx.onlineshop.repositories.CartItemRepository;
import com.xdpsx.onlineshop.repositories.OrderRepository;
import com.xdpsx.onlineshop.repositories.UserRepository;
import com.xdpsx.onlineshop.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional
    @Override
    public OrderResponse placeOrder(String userEmail, OrderRequest orderRequest) {
        User user = getUser(userEmail);
        List<CartItem> cartItems = cartItemRepository.findInStockCartByUserId(user.getId());
        if (cartItems.isEmpty()){
            throw new BadRequestException("Cart item is empty");
        }
        Order order = orderMapper.fromRequestToEntity(orderRequest);
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            OrderItem orderItem = OrderItem.builder()
                    .quantity(item.getQuantity())
                    .product(item.getProduct())
                    .order(order)
                    .build();
            order.getItems().add(orderItem);
            if (item.getProduct().getDiscountPercent() > 0){
                BigDecimal total = item.getProduct().getDiscountedPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                totalAmount = totalAmount.add(total);
            }else {
                BigDecimal total = item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                totalAmount = totalAmount.add(total);
            }
        }
        order.setTrackingNumber(UUID.randomUUID().toString());
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);

        Order savedOrder = orderRepository.save(order);

//        cartItemRepository.deleteInStockCartByUserId(user.getId());

        OrderDTO orderDTO = OrderDTO.builder()
                .id(savedOrder.getId())
                .trackingNumber(order.getTrackingNumber())
                .status(savedOrder.getStatus().name())
                .total(totalAmount)
                .mobileNumber(savedOrder.getMobileNumber())
                .address(savedOrder.getAddress())
                .createdAt(savedOrder.getCreatedAt())
                .deliveredAt(order.getDeliveredAt())
                .build();
        return OrderResponse.builder()
                .order(orderDTO)
                .build();
    }

    private User getUser(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User with email=%s not found".formatted(userEmail)));
    }
}
