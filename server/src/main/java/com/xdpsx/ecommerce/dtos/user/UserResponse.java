package com.xdpsx.ecommerce.dtos.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String name;
    private String photo;
    private String email;
}
