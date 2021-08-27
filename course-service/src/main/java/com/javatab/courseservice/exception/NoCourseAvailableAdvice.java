package com.javatab.courseservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class NoCourseAvailableAdvice {
    @ResponseBody
    @ExceptionHandler(NoCourseAvailableException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String courseNotFoundHandler(NoCourseAvailableException ex) {
        return ex.getMessage();
    }
}
