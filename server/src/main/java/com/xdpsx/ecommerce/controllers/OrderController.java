package com.xdpsx.ecommerce.controllers;

import com.xdpsx.ecommerce.dtos.order.OrderRequest;
import com.xdpsx.ecommerce.dtos.order.OrderResponse;
import com.xdpsx.ecommerce.entities.User;
import com.xdpsx.ecommerce.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
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
}
