package com.assignment.courseservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class InvalidCourseOrSeatNotAvailableAdvice {

    @ResponseBody
    @ExceptionHandler(InvalidCourseOrSeatNotAvailableException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String courseNotFoundHandler(InvalidCourseOrSeatNotAvailableException ex) {
        return ex.getMessage();
    }
}
