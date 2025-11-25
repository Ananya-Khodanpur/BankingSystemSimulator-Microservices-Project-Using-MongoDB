package com.exceptions;

public class InvalidAccountStatusException extends RuntimeException{
    public InvalidAccountStatusException(String message) {
        super(message);
    }
}
