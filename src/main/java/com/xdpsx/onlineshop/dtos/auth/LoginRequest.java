package com.xdpsx.onlineshop.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(@NotBlank @Size(max = 64) @Email String email, @NotBlank @Size(max = 255) String password) {}
