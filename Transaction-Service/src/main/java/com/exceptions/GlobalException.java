package com.exceptions;

import com.document.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalException {

    Logger logger = LoggerFactory.getLogger(GlobalException.class);

    @ExceptionHandler(InvalidException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidArgs(InvalidException ex){
        logger.error("Invalid arguments provided");
        return ResponseEntity.badRequest().body(ApiResponse.failure(ex.getMessage()));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgs(MethodArgumentNotValidException ex){
        logger.error("Failed, Missing fields detected");
        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error->{
            String field = ((FieldError) error).getField();
            String value = error.getDefaultMessage();
            errors.put(field,value);
        });
        return ResponseEntity.badRequest().body(ApiResponse.failure("Validation errors "+ errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleExceptions(Exception ex){
        logger.error("{}",ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.failure(ex.getMessage()));
    }



}
