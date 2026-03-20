package io.javatab.microservices.core.course.domain;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(String identifier) {
        super("The course with identifier " + identifier + " was not found.");
    }
}
