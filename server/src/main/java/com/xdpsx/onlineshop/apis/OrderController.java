package com.xdpsx.onlineshop.apis;

import com.xdpsx.onlineshop.dtos.order.OrderRequest;
import com.xdpsx.onlineshop.dtos.order.OrderResponse;
import com.xdpsx.onlineshop.services.OrderService;
import com.xdpsx.onlineshop.utils.RequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> placeOrder(
            Authentication authentication,
            @Valid @RequestBody OrderRequest orderRequest,
            HttpServletRequest httpServletRequest
    ) {
        var ipAddress = RequestUtil.getIpAddress(httpServletRequest);
        orderRequest.setIpAddress(ipAddress);
        return new ResponseEntity<>(
                orderService.placeOrder(authentication.getName(), orderRequest),
                HttpStatus.CREATED
        );
    }
}
