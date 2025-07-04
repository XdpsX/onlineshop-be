package com.xdpsx.onlineshop.services;

import com.xdpsx.onlineshop.dtos.auth.*;

public interface AuthService {
    String register(RegisterRequest request);

    void sendVerifyEmailOTP(SendOtpRequest request);

    void verifyEmail(VerifyEmailRequest request);

    TokenResponse login(LoginRequest request);

    void logout(String accessToken, LogoutRequest request);

    TokenResponse refreshToken(String accessToken, RefreshTokenRequest request);
}
