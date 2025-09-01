package com.example.redmoon.exceptions;

public class UserNotFoundInSystemException extends RuntimeException{

    public UserNotFoundInSystemException(String message) {
        super(message);
    }
}
