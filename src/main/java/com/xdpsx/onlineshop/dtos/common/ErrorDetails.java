package com.xdpsx.onlineshop.dtos.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Setter @Getter @NoArgsConstructor
public class ErrorDetails extends ErrorDTO{
    private Map<String, String> details;
}
