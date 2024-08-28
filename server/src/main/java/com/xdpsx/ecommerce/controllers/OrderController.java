package com.xdpsx.ecommerce.controllers;

import com.xdpsx.ecommerce.dtos.common.PageResponse;
import com.xdpsx.ecommerce.dtos.order.OrderPageParams;
import com.xdpsx.ecommerce.dtos.order.OrderRequest;
import com.xdpsx.ecommerce.dtos.order.OrderResponse;
import com.xdpsx.ecommerce.dtos.order.OrderStatusUpdateRequest;
import com.xdpsx.ecommerce.entities.User;
import com.xdpsx.ecommerce.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderRequest request
            ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedUser = (User) authentication.getPrincipal();
        OrderResponse response = orderService.createOrder(request, loggedUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderStatusUpdateRequest request) {
        OrderResponse updatedOrder = orderService.updateOrderStatus(orderId, request.getStatus());
        return ResponseEntity.ok(updatedOrder);
    }

    @GetMapping
    public ResponseEntity<PageResponse<OrderResponse>> getListOrders(
            @Valid OrderPageParams params
            ){
        PageResponse<OrderResponse> response = orderService.getListOrders(params);
        return ResponseEntity.ok(response);
    }
}
