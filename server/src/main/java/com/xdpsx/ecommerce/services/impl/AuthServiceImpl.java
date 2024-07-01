package com.xdpsx.ecommerce.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xdpsx.ecommerce.dtos.auth.LoginRequest;
import com.xdpsx.ecommerce.dtos.auth.RegisterRequest;
import com.xdpsx.ecommerce.dtos.auth.TokenResponse;
import com.xdpsx.ecommerce.entities.Role;
import com.xdpsx.ecommerce.entities.Token;
import com.xdpsx.ecommerce.entities.TokenType;
import com.xdpsx.ecommerce.entities.User;
import com.xdpsx.ecommerce.exceptions.BadRequestException;
import com.xdpsx.ecommerce.exceptions.ResourceNotFoundException;
import com.xdpsx.ecommerce.exceptions.TokenException;
import com.xdpsx.ecommerce.repositories.RoleRepository;
import com.xdpsx.ecommerce.repositories.TokenRepository;
import com.xdpsx.ecommerce.repositories.UserRepository;
import com.xdpsx.ecommerce.security.JwtProvider;
import com.xdpsx.ecommerce.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    @Override
    public TokenResponse register(RegisterRequest request) {
        Role role = roleRepository.findByName(Role.USER)
                .orElseThrow(() -> new ResourceNotFoundException("Role %s not found!".formatted(Role.USER)));
        if (userRepository.existsByEmail(request.getEmail())){
            throw new BadRequestException("User with email=%s already existed".formatted(request.getEmail()));
        }
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();
        User savedUser = userRepository.save(user);

        String accessToken = jwtProvider.generateAccessToken(savedUser);
        String refreshToken = jwtProvider.generateRefreshToken(savedUser);
        saveUserToken(savedUser, accessToken, refreshToken);
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public TokenResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        if (authentication.isAuthenticated()){
            User user = (User) authentication.getPrincipal();
            String accessToken = jwtProvider.generateAccessToken(user);
            String refreshToken = jwtProvider.generateRefreshToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, accessToken, refreshToken);
            return TokenResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }else {
            throw new UsernameNotFoundException("Email not found");
        }
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new TokenException("Refresh token is not provided");
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtProvider.extractUsername(refreshToken);
        if (userEmail != null){
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new TokenException("Refresh token is wrong"));
            boolean isTokenValid = tokenRepository.findByRefreshToken(refreshToken)
                    .map(t -> !t.isExpired() && !t.isRevoked())
                    .orElse(false);
            if (isTokenValid && jwtProvider.isTokenValid(refreshToken, user)) {
                String newAccessToken = jwtProvider.generateAccessToken(user);
                String newRefreshToken = jwtProvider.generateRefreshToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, newAccessToken, newRefreshToken);

                TokenResponse tokenResponse = TokenResponse.builder()
                        .accessToken(newAccessToken)
                        .refreshToken(newRefreshToken)
                        .build();
                // Sử dụng ObjectMapper để chuyển đổi AuthResponse thành chuỗi JSON
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonResponse = objectMapper.writeValueAsString(tokenResponse);

                // Thiết lập các header cho response
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                // Ghi chuỗi JSON vào OutputStream của HttpServletResponse
                response.getWriter().write(jsonResponse);
                return;
            }
            throw new TokenException("Refresh token failed");
        }
    }

    private void revokeAllUserTokens(User user){
        var validUserToken = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validUserToken.isEmpty())
            return;
        validUserToken.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(validUserToken);
    }

    private void saveUserToken(User user, String accessToken, String refreshToken) {
        var token = Token.builder()
                .user(user)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }
}
