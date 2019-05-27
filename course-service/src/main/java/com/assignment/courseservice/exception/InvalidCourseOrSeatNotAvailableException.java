package com.assignment.courseservice.exception;

public class InvalidCourseOrSeatNotAvailableException extends RuntimeException {

    public InvalidCourseOrSeatNotAvailableException() {
        super("Invalid course or, seat is not available for this course. May be you are trying to re-register the same course." +
                " Please contact administrator");
    }
}
