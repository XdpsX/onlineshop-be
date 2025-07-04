package com.xdpsx.onlineshop.controllers;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.xdpsx.onlineshop.constants.messages.SMessage;
import com.xdpsx.onlineshop.dtos.auth.*;
import com.xdpsx.onlineshop.dtos.common.APIResponse;
import com.xdpsx.onlineshop.services.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    //    @Value("${app.oauth2.error-uri}")
    //    private String ERROR_URL;

    @PostMapping("/register")
    public APIResponse<String> register(@Valid @RequestBody RegisterRequest request) {
        String data = authService.register(request);
        return APIResponse.ok(data);
    }

    @PostMapping("/send-otp")
    public APIResponse<Void> sendVerifyEmailOTP(@Valid @RequestBody SendOtpRequest request) {
        authService.sendVerifyEmailOTP(request);
        return APIResponse.noContent(SMessage.SUCCESS);
    }

    @PostMapping("/verify-email")
    public APIResponse<Void> verifyEmail(@Valid @RequestBody VerifyEmailRequest request) {
        authService.verifyEmail(request);
        return APIResponse.noContent(SMessage.SUCCESS);
    }

    @PostMapping("/login")
    public APIResponse<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse data = authService.login(request);
        return APIResponse.ok(data);
    }

    @PostMapping("/logout")
    public APIResponse<Void> logout(
            @RequestHeader("Authorization") String bearerToken, @Valid @RequestBody LogoutRequest request) {
        String accessToken = bearerToken.startsWith("Bearer ") ? bearerToken.substring(7) : bearerToken;
        authService.logout(accessToken, request);
        return APIResponse.noContent(SMessage.SUCCESS);
    }

    @PostMapping("/refresh")
    public APIResponse<TokenResponse> refreshToken(@RequestHeader("Authorization") String bearerToken, @Valid @RequestBody RefreshTokenRequest request) {
        String accessToken = bearerToken.startsWith("Bearer ") ? bearerToken.substring(7) : bearerToken;
        TokenResponse data = authService.refreshToken(accessToken, request);
        return APIResponse.ok(data);
    }

    //    @GetMapping("/nopage")
    //    public void nopageRedirect(HttpServletResponse response) throws IOException {
    //        response.sendRedirect(ERROR_URL);
    //    }
}
