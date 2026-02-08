package com.xmcy.controller;

import com.xmcy.model.ApiError;
import com.xmcy.service.exception.CryptoNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class CryptoServiceExceptionHandler {

    /**
     * @return Http 500 with error message in case no other handler activated
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException() {
        return new ResponseEntity<>(new ApiError().message("Unexpected exception"), INTERNAL_SERVER_ERROR);
    }

    /**
     * @return Http 404 with error message in case result expected but not found
     */
    @ExceptionHandler(CryptoNotFoundException.class)
    public ResponseEntity<ApiError> handleCryptNotFound() {
        return new ResponseEntity<>(new ApiError().message("Crypto not found"), NOT_FOUND);
    }

    /**
     * @return Http 400 with error message in case input is invalid
     */
    @ExceptionHandler({ValidationException.class,
        MethodArgumentTypeMismatchException.class,
        MissingServletRequestParameterException.class})
    public ResponseEntity<ApiError> handleInvalidInput() {
        return new ResponseEntity<>(new ApiError().message("Invalid input"), BAD_REQUEST);
    }
}
