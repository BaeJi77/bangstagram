package com.bangstagram.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class CommonControllerAdvice {
    @ExceptionHandler(DoNotExistException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String requestNotExistId(DoNotExistException e) {
        log.error("{}", e.getMessage());
        return e.getMessage();
    }
}
