package io.javatab.microservices.core.review.domain;

public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException(String id) {
        super("The review with id " + id + " was not found.");
    }
}
