package com.xdpsx.onlineshop.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank
        @Size(max = 64)
        String name,

        @NotBlank
        @Size(max = 64)
        @Email
        String email,

        @NotBlank
        @Size(min = 8, max = 255)
        String password
) {}
