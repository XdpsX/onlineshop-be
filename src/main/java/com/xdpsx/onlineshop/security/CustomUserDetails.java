package com.xdpsx.onlineshop.security;

import com.xdpsx.onlineshop.entities.User;
import com.xdpsx.onlineshop.entities.enums.AuthProvider;
import com.xdpsx.onlineshop.entities.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.xdpsx.onlineshop.constants.SecurityConstants.ROLE_PREFIX;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CustomUserDetails implements UserDetails, OAuth2User {
    private Long id;
    private String name;
    private String avatar;
    private String username;
    private String password;
    private AuthProvider authProvider;
    private Role role;

    public static CustomUserDetails buildFromUser(final User user) {
        return CustomUserDetails.builder()
                .id(user.getId())
                .username(user.getEmail())
                .password(user.getPassword())
                .authProvider(user.getAuthProvider())
                .role(user.getRole())
                .build();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
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
