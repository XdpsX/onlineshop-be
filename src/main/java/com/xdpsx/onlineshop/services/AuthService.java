package com.xdpsx.onlineshop.services;

import com.xdpsx.onlineshop.dtos.auth.LoginRequest;
import com.xdpsx.onlineshop.dtos.auth.RegisterRequest;
import com.xdpsx.onlineshop.dtos.auth.SendOtpRequest;
import com.xdpsx.onlineshop.dtos.auth.TokenResponse;

public interface AuthService {
    String register(RegisterRequest request);

    void sendVerifyEmailOTP(SendOtpRequest request);

    TokenResponse login(LoginRequest request);
}
