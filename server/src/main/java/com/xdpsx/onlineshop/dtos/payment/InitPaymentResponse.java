package com.xdpsx.onlineshop.dtos.payment;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class InitPaymentResponse {
    private String vnpUrl;
}
