package com.xdpsx.onlineshop.dtos.payment;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data @Builder
public class InitPaymentRequest {
    private String requestId;

    private String ipAddress;

    private Long userId;

    private String txnRef;

    private BigDecimal amount;
}
