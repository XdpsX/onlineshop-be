package com.xdpsx.onlineshop.services;

import com.xdpsx.onlineshop.dtos.payment.InitPaymentRequest;
import com.xdpsx.onlineshop.dtos.payment.InitPaymentResponse;

public interface PaymentService {
    InitPaymentResponse init(InitPaymentRequest request);
}
