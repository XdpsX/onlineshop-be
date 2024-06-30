package com.xdpsx.ecommerce.services.impl;

import com.xdpsx.ecommerce.dtos.auth.LoginRequest;
import com.xdpsx.ecommerce.dtos.auth.RegisterRequest;
import com.xdpsx.ecommerce.dtos.auth.TokenResponse;
import com.xdpsx.ecommerce.entities.Role;
import com.xdpsx.ecommerce.entities.User;
import com.xdpsx.ecommerce.exceptions.BadRequestException;
import com.xdpsx.ecommerce.exceptions.ResourceNotFoundException;
import com.xdpsx.ecommerce.repositories.RoleRepository;
import com.xdpsx.ecommerce.repositories.UserRepository;
import com.xdpsx.ecommerce.security.JwtProvider;
import com.xdpsx.ecommerce.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
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

        String accessToken = jwtProvider.generateToken(savedUser);
        return TokenResponse.builder()
                .accessToken(accessToken)
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
            String accessToken = jwtProvider.generateToken(user);
            return TokenResponse.builder()
                    .accessToken(accessToken)
                    .build();
        }else {
            throw new UsernameNotFoundException("Email not found");
        }
    }
}
