package com.xdpsx.onlineshop.services.impl;

import com.xdpsx.onlineshop.dtos.order.OrderDTO;
import com.xdpsx.onlineshop.dtos.order.OrderRequest;
import com.xdpsx.onlineshop.dtos.order.OrderResponse;
import com.xdpsx.onlineshop.dtos.payment.InitPaymentRequest;
import com.xdpsx.onlineshop.dtos.payment.InitPaymentResponse;
import com.xdpsx.onlineshop.entities.*;
import com.xdpsx.onlineshop.entities.enums.OrderStatus;
import com.xdpsx.onlineshop.entities.enums.PaymentMethod;
import com.xdpsx.onlineshop.entities.enums.PaymentStatus;
import com.xdpsx.onlineshop.exceptions.BadRequestException;
import com.xdpsx.onlineshop.exceptions.ResourceNotFoundException;
import com.xdpsx.onlineshop.mappers.OrderMapper;
import com.xdpsx.onlineshop.repositories.CartItemRepository;
import com.xdpsx.onlineshop.repositories.OrderRepository;
import com.xdpsx.onlineshop.repositories.PaymentRepository;
import com.xdpsx.onlineshop.repositories.UserRepository;
import com.xdpsx.onlineshop.services.OrderService;
import com.xdpsx.onlineshop.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;

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
            BigDecimal total;
            if (item.getProduct().getDiscountPercent() > 0){
                total = item.getProduct().getDiscountedPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            }else {
                total = item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            }
            totalAmount = totalAmount.add(total);
        }
        order.setTrackingNumber(UUID.randomUUID().toString());
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(totalAmount);

        Payment payment = Payment.builder()
                .status(PaymentStatus.UNPAID)
                .order(order)
                .build();
        order.setPayment(payment);

        Order savedOrder = orderRepository.save(order);

//        cartItemRepository.deleteInStockCartByUserId(user.getId());

        var initPaymentRequest = InitPaymentRequest.builder()
                .userId(savedOrder.getUser().getId())
                .amount(savedOrder.getTotalAmount())
                .txnRef(String.valueOf(savedOrder.getId()))
                .requestId(String.valueOf(savedOrder.getId()))
                .ipAddress(orderRequest.getIpAddress())
                .build();
        InitPaymentResponse initPaymentResponse = paymentService.init(initPaymentRequest);

        OrderDTO orderDTO = OrderDTO.builder()
                .id(savedOrder.getId())
                .trackingNumber(order.getTrackingNumber())
                .status(savedOrder.getStatus().name())
                .total(savedOrder.getTotalAmount())
                .mobileNumber(savedOrder.getMobileNumber())
                .address(savedOrder.getAddress())
                .createdAt(savedOrder.getCreatedAt())
                .deliveredAt(order.getDeliveredAt())
                .build();
        return OrderResponse.builder()
                .order(orderDTO)
                .payment(initPaymentResponse)
                .build();
    }

    @Override
    public void payment(String userEmail, long orderId) {
        User user = getUser(userEmail);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order id=%s not found".formatted(orderId)));
        if (!user.getId().equals(order.getUser().getId())){
            throw new BadRequestException("You are not authorized to pay this order");
        }
        Payment payment = order.getPayment();
        payment.setStatus(PaymentStatus.PAID);
        payment.setPaymentMethod(PaymentMethod.VNPAY);
        payment.setPaymentDate(LocalDateTime.now());

        paymentRepository.save(payment);
    }

    private User getUser(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User with email=%s not found".formatted(userEmail)));
    }
}
