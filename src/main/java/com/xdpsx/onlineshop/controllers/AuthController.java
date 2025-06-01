package com.xdpsx.onlineshop.controllers;

import com.xdpsx.onlineshop.dtos.common.APIResponse;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.xdpsx.onlineshop.dtos.auth.LoginRequest;
import com.xdpsx.onlineshop.dtos.auth.RegisterRequest;
import com.xdpsx.onlineshop.dtos.auth.TokenResponse;
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

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    //    @GetMapping("/nopage")
    //    public void nopageRedirect(HttpServletResponse response) throws IOException {
    //        response.sendRedirect(ERROR_URL);
    //    }
}
