package com.xdpsx.onlineshop.exceptions.handlers;

import com.xdpsx.onlineshop.dtos.common.ErrorDTO;
import com.xdpsx.onlineshop.exceptions.BadRequestException;
import com.xdpsx.onlineshop.exceptions.DuplicateException;
import com.xdpsx.onlineshop.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(DuplicateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO handleDuplicate(HttpServletRequest request, DuplicateException e) {
        log.error(e.getMessage(), e);
        ErrorDTO error = new ErrorDTO();
        error.setStatus(HttpStatus.CONFLICT.value());
        error.setMessage(e.getMessage());
        return error;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleResourceNotFound(HttpServletRequest request, ResourceNotFoundException e) {
        log.error(e.getMessage(), e);
        ErrorDTO error = new ErrorDTO();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(e.getMessage());
        return error;
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleBadRequest(HttpServletRequest request, BadRequestException e){
        log.error(e.getMessage(), e);
        ErrorDTO error = new ErrorDTO();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(e.getMessage());
        return error;
    }
}
