package com.bangstagram.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * author: Ji-Hoon Bae
 * Date: 2020.04.29
 */

@Slf4j
@ControllerAdvice
public class CommonControllerAdvice {

    @ExceptionHandler(DoNotExistException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String requestNotExistId(DoNotExistException e) {
        log.error("{}", e.getMessage());
        return e.getMessage();
    }

    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Already exists")
    public String requestAlreadyExists(AlreadyExistsException e) {
        log.error("{}", e.getMessage());
        return e.getMessage();
    }
}
