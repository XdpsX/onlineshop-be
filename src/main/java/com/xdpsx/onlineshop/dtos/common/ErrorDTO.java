package com.xdpsx.onlineshop.dtos.common;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
public class ErrorDTO {
    private int status;
    private String message;

    public ErrorDTO(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
