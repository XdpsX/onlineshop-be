package com.xdpsx.ecommerce.services;

import com.xdpsx.ecommerce.dtos.auth.LoginRequest;
import com.xdpsx.ecommerce.dtos.auth.RegisterRequest;
import com.xdpsx.ecommerce.dtos.auth.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthService {
    TokenResponse register(RegisterRequest request);
    TokenResponse login(LoginRequest request);
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
