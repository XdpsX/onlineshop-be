package com.xdpsx.onlineshop.apis;

import com.xdpsx.onlineshop.dtos.common.PageResponse;
import com.xdpsx.onlineshop.dtos.order.OrderDTO;
import com.xdpsx.onlineshop.dtos.order.OrderDetailsDTO;
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
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/me")
    public ResponseEntity<PageResponse<OrderDTO>> getMyOrders(
            Authentication authentication,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "5") int pageSize
    ){
        PageResponse<OrderDTO> response = orderService.getMyOrders(authentication.getName(), pageNum, pageSize);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/code/{trackingNumber}")
    public ResponseEntity<OrderDetailsDTO> getOrderByTrackNumber(
            Authentication authentication,
            @PathVariable String trackingNumber
    ){
        OrderDetailsDTO response = orderService.getOrderByTrackingNumber(authentication.getName(), trackingNumber);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailsDTO> getOrderById(@PathVariable Long orderId){
        OrderDetailsDTO response = orderService.getOrderById(orderId);
        return ResponseEntity.ok(response);
    }
}
