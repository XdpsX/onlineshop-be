package com.xdpsx.onlineshop.dtos.order;

import com.xdpsx.onlineshop.dtos.payment.InitPaymentResponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponse {
    private OrderDTO order;
    private InitPaymentResponse payment;
}
