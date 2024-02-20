package com.shop.online.demo.controller;

import com.shop.online.demo.error.ApiError;
import com.shop.online.demo.error.ApiSubError;
import com.shop.online.demo.error.ApiValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(BindException ex) {
        List<ApiSubError> errorDetails = new ArrayList<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorDetails.add(new ApiValidationError(fieldError.getField(),
                    fieldError.getDefaultMessage()));
        }
        return new ApiError(HttpStatus.BAD_REQUEST, "Validation errors", errorDetails);
    }


    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(NotFoundException ex) {
        return new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleRuntimeException(RuntimeException ex) {
        return new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
    }
}
