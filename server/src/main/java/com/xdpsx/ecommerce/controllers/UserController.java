package com.xdpsx.ecommerce.controllers;

import com.xdpsx.ecommerce.dtos.user.UserResponse;
import com.xdpsx.ecommerce.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(Principal principal){
        return ResponseEntity.ok(userService.getUserProfile(principal));
    }
}
