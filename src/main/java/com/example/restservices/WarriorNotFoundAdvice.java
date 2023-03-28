package com.example.restservices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class WarriorNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(WarriorNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String warriorNotFoundHandler(WarriorNotFoundException exception) {
        return exception.getMessage();
    }
}
