package com.bangstagram.common.exception;

/**
 * author: Ji-Hoon Bae
 * Date: 2020.04.29
 */

public class DoNotExistException extends RuntimeException {
    public DoNotExistException(String message) {
        super(message);
    }
}
