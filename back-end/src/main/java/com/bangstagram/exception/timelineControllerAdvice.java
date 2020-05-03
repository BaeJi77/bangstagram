package com.bangstagram.exception;

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
@ControllerAdvice(basePackages = {"com.bangstagram.timeline"})
public class timelineControllerAdvice {
    @ExceptionHandler(DoNotExistException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String requestNotExistId(DoNotExistException e) {
        log.error("{}", e.getMessage());
        return e.getMessage();
    }
}
