package com.xdpsx.onlineshop.exceptions.handlers;

import com.xdpsx.onlineshop.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.xdpsx.onlineshop.dtos.common.ErrorDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler({DuplicateException.class, InUseException.class, ModifyExclusiveException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO handleDuplicateException(DuplicateException e) {
        log.error(e.getMessage(), e);
        return new ErrorDTO(HttpStatus.CONFLICT, e.getMessage(), e.getArgs());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleNotFoundException(NotFoundException e) {
        log.error(e.getMessage(), e);
        return new ErrorDTO(HttpStatus.NOT_FOUND, e.getMessage(), e.getArgs());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleBadRequestException(BadRequestException e) {
        log.error(e.getMessage(), e);
        return new ErrorDTO(HttpStatus.BAD_REQUEST, e.getMessage(), e.getArgs());
    }
}
