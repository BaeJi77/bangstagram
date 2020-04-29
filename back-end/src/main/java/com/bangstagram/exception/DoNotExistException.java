package com.bangstagram.exception;

/**
 author: Ji-Hoon Bae
 Date: 2020.04.29
 */

public class DoNotExistException extends RuntimeException {
    public DoNotExistException(String message, Long timelineId) {
        super(message + timelineId);
    }
}
