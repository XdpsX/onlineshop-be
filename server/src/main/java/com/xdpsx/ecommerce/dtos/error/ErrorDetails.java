package com.xdpsx.ecommerce.dtos.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDetails {
    private Date timestamp;
    private int status;
    private String path;
    private String message;
    private Map<String, String> details;

    public ErrorDetails(String message) {
        this.timestamp = new Date();
        this.message = message;
    }
}
