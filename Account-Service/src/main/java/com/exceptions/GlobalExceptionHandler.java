package com.exceptions;

import com.document.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleAccountNotFound(AccountNotFoundException ex){
        return ResponseEntity.ok().body(ApiResponse.failure(ex.getMessage()));
    }

    @ExceptionHandler(InvalidBalnceException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidBalance(InvalidBalnceException ex){
        logger.error("Failed to update balance, due to invalid balance");
        return ResponseEntity.ok().body(ApiResponse.failure(ex.getMessage()));
    }

    @ExceptionHandler(InvalidAccountStatusException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidStatus(InvalidAccountStatusException ex){
        logger.error("Failed to update balance, due to inactive account status");
        return ResponseEntity.ok().body(ApiResponse.failure(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgs(MethodArgumentNotValidException ex){
        logger.error("Failed to created Account, Missing fields detected");
        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error->{
            String field = ((FieldError) error).getField();
            String value = error.getDefaultMessage();
            errors.put(field,value);
        });
        return ResponseEntity.ok().body(ApiResponse.failure("Validation errors "+ errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception ex){
        logger.error("Failed to created Account, Internal server error");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.failure(ex.getMessage()));
    }

}
