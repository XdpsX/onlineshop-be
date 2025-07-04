package com.xdpsx.onlineshop.filter;

import java.io.IOException;
import java.io.OutputStream;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xdpsx.onlineshop.constants.CacheKey;
import com.xdpsx.onlineshop.dtos.common.ErrorDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtBlacklistFilter extends OncePerRequestFilter {

    private final RedisTemplate<String, String> redisTemplateString;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            Jwt jwt = jwtAuth.getToken();
            String jti = jwt.getClaimAsString("jti");

            if (jti != null && Boolean.TRUE.equals(redisTemplateString.hasKey(CacheKey.buildLogoutKey(jti)))) {
                SecurityContextHolder.clearContext();
                ErrorDTO errorDTO = new ErrorDTO(HttpStatus.UNAUTHORIZED, "Token is revoked");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                OutputStream out = response.getOutputStream();
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(out, errorDTO);
                out.flush();
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
