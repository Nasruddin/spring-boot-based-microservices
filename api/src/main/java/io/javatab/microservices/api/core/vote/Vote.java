package io.javatab.microservices.api.core.vote;

public record Vote(int courseId, int studentId, int like, int dislike) {
}
