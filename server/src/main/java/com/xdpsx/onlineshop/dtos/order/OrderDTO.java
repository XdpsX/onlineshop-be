package com.xdpsx.onlineshop.dtos.order;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @Builder
public class OrderDTO {
    private Long id;
    private String trackingNumber;
    private String status;
    private BigDecimal total;
    private String address;
    private String mobileNumber;
    private LocalDateTime createdAt;
    private LocalDateTime deliveredAt;
}
