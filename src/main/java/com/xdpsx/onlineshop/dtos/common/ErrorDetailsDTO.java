package com.xdpsx.onlineshop.dtos.common;

import java.util.Map;

import org.springframework.http.HttpStatus;

import com.xdpsx.onlineshop.constants.messages.APIMessage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ErrorDetailsDTO extends ErrorDTO {
    private Map<String, String> fieldErrors;

    public ErrorDetailsDTO(HttpStatus status, APIMessage apiMessage, String... args) {
        super(status, apiMessage, args);
    }
}
