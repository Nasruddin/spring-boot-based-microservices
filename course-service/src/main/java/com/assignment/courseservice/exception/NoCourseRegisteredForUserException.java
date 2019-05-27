package com.assignment.courseservice.exception;

public class NoCourseRegisteredForUserException extends RuntimeException {

    public NoCourseRegisteredForUserException() {
        super("No courses found for the user. May be invalid user id!!!");
    }
}
