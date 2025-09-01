package com.example.redmoon.exceptions;

public class UserAlreadySignedInException extends RuntimeException{

    public UserAlreadySignedInException(String message) {
        super(message);
    }
}
