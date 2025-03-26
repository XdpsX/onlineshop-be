package com.xdpsx.onlineshop.dtos.common;

import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ErrorDetails extends ErrorDTO {
    private Map<String, String> details;
}
