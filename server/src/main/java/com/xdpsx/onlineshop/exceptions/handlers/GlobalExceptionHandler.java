package com.xdpsx.onlineshop.exceptions.handlers;

import com.xdpsx.onlineshop.dtos.common.ErrorDTO;
import com.xdpsx.onlineshop.dtos.common.ErrorDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO handleBadCredentials(HttpServletRequest request, BadCredentialsException e){
        log.error(e.getMessage(), e);
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setStatus(HttpStatus.UNAUTHORIZED.value());
        errorDTO.setMessage("Wrong email or password"); // e.getMessage() = "Bad credentials"
        return errorDTO;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDetails handleMethodArgumentNotValid(HttpServletRequest request, MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        ErrorDetails error = new ErrorDetails();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage("Validation Error");

        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        error.setDetails(errors.stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
        return error;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleException(HttpServletRequest request, Exception e) {
        log.error(e.getMessage(), e);
        ErrorDTO error = new ErrorDTO();
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setMessage(e.getMessage());
        return error;
    }
}
