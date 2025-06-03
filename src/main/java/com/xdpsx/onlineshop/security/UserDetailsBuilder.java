package com.xdpsx.onlineshop.security;

import com.xdpsx.onlineshop.entities.User;

public class UserDetailsBuilder {
    public static CustomUserDetails fromUser(final User user) {
        return CustomUserDetails.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .enabled(user.isEnabled())
                .locked(user.isLocked())
                .roles(user.getRoles())
                .build();
    }
}
