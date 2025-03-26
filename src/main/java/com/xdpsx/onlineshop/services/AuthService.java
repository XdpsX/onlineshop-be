package com.xdpsx.onlineshop.services;

import com.xdpsx.onlineshop.dtos.auth.LoginRequest;
import com.xdpsx.onlineshop.dtos.auth.RegisterRequest;
import com.xdpsx.onlineshop.dtos.auth.TokenResponse;

public interface AuthService {
    TokenResponse register(RegisterRequest request);

    TokenResponse login(LoginRequest request);
}
