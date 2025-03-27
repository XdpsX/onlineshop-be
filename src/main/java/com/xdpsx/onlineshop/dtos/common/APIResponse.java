package com.xdpsx.onlineshop.dtos.common;

public record APIResponse<T> (
    int status,
    String message,
    T data
){
}
