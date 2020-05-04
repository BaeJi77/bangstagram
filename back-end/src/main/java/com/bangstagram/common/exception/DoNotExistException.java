package com.bangstagram.common.exception;

public class DoNotExistException extends RuntimeException {
    public DoNotExistException(String message, Long id) {
        super(message + id);
    }
}
