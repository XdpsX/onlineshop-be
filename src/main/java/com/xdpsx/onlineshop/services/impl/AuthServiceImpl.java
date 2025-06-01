package com.xdpsx.onlineshop.services.impl;

import com.xdpsx.onlineshop.constants.messages.EMessage;
import com.xdpsx.onlineshop.entities.Role;
import com.xdpsx.onlineshop.entities.enums.RoleName;
import com.xdpsx.onlineshop.exceptions.NotFoundException;
import com.xdpsx.onlineshop.repositories.RoleRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.xdpsx.onlineshop.dtos.auth.LoginRequest;
import com.xdpsx.onlineshop.dtos.auth.RegisterRequest;
import com.xdpsx.onlineshop.dtos.auth.TokenResponse;
import com.xdpsx.onlineshop.entities.User;
import com.xdpsx.onlineshop.entities.enums.AuthProvider;
import com.xdpsx.onlineshop.exceptions.DuplicateException;
import com.xdpsx.onlineshop.repositories.UserRepository;
import com.xdpsx.onlineshop.security.CustomUserDetails;
import com.xdpsx.onlineshop.security.TokenProvider;
import com.xdpsx.onlineshop.services.AuthService;

import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;

    @Override
    public String register(RegisterRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElse(null);

        // User already exists
        if (user != null) {
            // User is already verified
            if (user.isEnabled()) {
                throw new DuplicateException(EMessage.DATA_EXISTS, request.email());
            } else { // User is not verified
                user.setName(request.name());
                user.setPassword(passwordEncoder.encode(request.password()));
                userRepository.save(user);
            }
        }else { // User does not exist
            Role userRole = roleRepository.findByName(RoleName.USER)
                    .orElseThrow(() -> new NotFoundException(EMessage.NOT_FOUND, "Role User"));
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);

            User newUser = User.builder()
                    .email(request.email())
                    .name(request.name())
                    .password(passwordEncoder.encode(request.password()))
                    .enabled(false)
                    .roles(roles)
                    .authProvider(AuthProvider.SYSTEM)
                    .build();
            userRepository.save(newUser);
        }
        return request.email();
    }

    @Override
    public TokenResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        String accessToken = tokenProvider.generateToken(user);
        return TokenResponse.builder().accessToken(accessToken).build();
    }
}
