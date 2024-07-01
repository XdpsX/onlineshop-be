package com.xdpsx.ecommerce.dtos.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
}
