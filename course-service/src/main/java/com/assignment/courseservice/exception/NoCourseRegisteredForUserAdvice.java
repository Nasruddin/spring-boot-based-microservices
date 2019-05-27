package com.assignment.courseservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class NoCourseRegisteredForUserAdvice {

    @ResponseBody
    @ExceptionHandler(NoCourseRegisteredForUserException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String registeredCourseNotFoundHandler(NoCourseRegisteredForUserException ex) {
        return ex.getMessage();
    }
}
