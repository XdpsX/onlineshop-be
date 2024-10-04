package com.xdpsx.onlineshop.services.impl;

import com.xdpsx.onlineshop.constants.VNPayParams;
import com.xdpsx.onlineshop.exceptions.BadRequestException;
import com.xdpsx.onlineshop.services.IpnHandler;
import com.xdpsx.onlineshop.services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class VNPayIpnHandler implements IpnHandler {
    private final VNPayService vnPayService;

    private final OrderService orderService;

    @Override
    public String process(Map<String, String> params, String userEmail) {
        if (!vnPayService.verifyIpn(params)) {
            throw new BadRequestException("Ipn is not valid");
        }

        var txnRef = params.get(VNPayParams.TXN_REF);
        var orderId = Long.parseLong(txnRef);
        orderService.payment(userEmail, orderId);
        return "Success";
    }
}
