package com.xdpsx.onlineshop.security;

import com.xdpsx.onlineshop.entities.User;
import com.xdpsx.onlineshop.entities.enums.AuthProvider;
import com.xdpsx.onlineshop.entities.enums.Role;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

import static com.xdpsx.onlineshop.constants.SecurityConstants.ROLE_PREFIX;

@Data @Builder
public class CustomUserDetails implements UserDetails {
    private String username;
    private String password;
    private AuthProvider authProvider;
    private Role role;

    public static CustomUserDetails buildFromUser(final User user) {
        return CustomUserDetails.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authProvider(user.getAuthProvider())
                .role(user.getRole())
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(ROLE_PREFIX + role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
