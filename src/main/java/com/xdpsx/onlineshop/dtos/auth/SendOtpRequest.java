package com.xdpsx.onlineshop.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SendOtpRequest(@NotBlank @Size(max = 64) String email) {}
