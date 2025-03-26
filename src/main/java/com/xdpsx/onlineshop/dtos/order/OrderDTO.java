package com.xdpsx.onlineshop.dtos.order;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@SuperBuilder
public class OrderDTO {
    private Long id;
    private String trackingNumber;
    private String status;
    private BigDecimal total;
    private String address;
    private String mobileNumber;
    private String paymentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime deliveredAt;
}
