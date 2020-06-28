package com.bangstagram.common.exception;

/**
 * author: Hyo-Jin Kim
 * Date: 2020.06.18
 */

public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}
