package com.javatab.courseservice.exception;

public class NoCourseAvailableException extends RuntimeException {

    public NoCourseAvailableException() {
        super("No courses available at the moment. Keep looking the awesome app for upcoming courses");
    }
}
