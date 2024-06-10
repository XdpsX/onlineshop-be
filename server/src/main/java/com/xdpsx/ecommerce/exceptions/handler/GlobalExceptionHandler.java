package com.xdpsx.ecommerce.exceptions.handler;

import com.xdpsx.ecommerce.dtos.error.ErrorDetails;
import com.xdpsx.ecommerce.exceptions.BadRequestException;
import com.xdpsx.ecommerce.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDetails handleResourceNotFound(HttpServletRequest request, ResourceNotFoundException e) {
        ErrorDetails errorDetails = new ErrorDetails(e.getMessage());
        errorDetails.setStatus(HttpStatus.NOT_FOUND.value());
        errorDetails.setPath(request.getServletPath());
        return errorDetails;
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDetails handleBadRequest(HttpServletRequest request, BadRequestException e){
        ErrorDetails errorDetails = new ErrorDetails(e.getMessage());
        errorDetails.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDetails.setPath(request.getServletPath());
        return errorDetails;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDetails handleMethodArgumentNotValid(HttpServletRequest request, MethodArgumentNotValidException e) {
        ErrorDetails errorDetails = new ErrorDetails("Validation Error");
        errorDetails.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDetails.setPath(request.getServletPath());

        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        errorDetails.setDetails(errors.stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
        return errorDetails;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDetails handleException(HttpServletRequest request, Exception e) {
        ErrorDetails errorDetails = new ErrorDetails("Internal Server Error");
        errorDetails.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDetails.setPath(request.getServletPath());
        return errorDetails;
    }
}
