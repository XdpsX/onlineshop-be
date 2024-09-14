package com.xdpsx.onlineshop.apis;

import com.xdpsx.onlineshop.dtos.user.UserProfile;
import com.xdpsx.onlineshop.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users/me")
    public ResponseEntity<UserProfile> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserProfile profile = userService.getUserByEmail(authentication.getName());
        return ResponseEntity.ok(profile);
    }
}
