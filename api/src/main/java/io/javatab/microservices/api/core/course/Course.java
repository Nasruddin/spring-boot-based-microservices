package io.javatab.microservices.api.core.course;

public record Course(int courseId, String courseName, String author, String content, int voteId) {
}
