package com.xdpsx.ecommerce.dtos.order;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderResponse {
    private Long id;
    private String trackingNumber;
    private String status;
    private Double total;
    private String address;
    private String mobileNumber;
    private List<OrderItemResponse> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
