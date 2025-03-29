package com.xdpsx.onlineshop.dtos.common;

import org.springframework.http.HttpStatus;

import com.xdpsx.onlineshop.constants.messages.APIMessage;
import com.xdpsx.onlineshop.constants.messages.SMessage;

public record APIResponse<T>(int status, String message, T data) {

    public APIResponse(HttpStatus status, T data, APIMessage apiMessage) {
        this(status.value(), apiMessage.message(), data);
    }

    public static <T> APIResponse<T> ok(T data) {
        return new APIResponse<>(HttpStatus.OK, data, SMessage.SUCCESS);
    }

    public static <T> APIResponse<T> ok(T data, APIMessage message) {
        return new APIResponse<>(HttpStatus.OK, data, message);
    }

    public static <T> APIResponse<T> noContent() {
        return new APIResponse<>(HttpStatus.OK, null, SMessage.SUCCESS);
    }

    public static <T> APIResponse<T> noContent(APIMessage message) {
        return new APIResponse<>(HttpStatus.OK, null, message);
    }
}
