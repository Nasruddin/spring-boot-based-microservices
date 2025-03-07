package io.javatab.microservices.core.course.domain;

public class CourseAlreadyExitsException extends RuntimeException {
    public CourseAlreadyExitsException(String title) {
        super("A course with title " + title + " already exists.");
    }
}
