package com.xdpsx.onlineshop.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.xdpsx.onlineshop.constants.AuthConstants;

public record VerifyEmailRequest(
        @NotBlank @Size(max = 64) String email,
        @NotBlank @Size(max = AuthConstants.VERIFY_EMAIL_OTP_LENGTH) String otp) {}
