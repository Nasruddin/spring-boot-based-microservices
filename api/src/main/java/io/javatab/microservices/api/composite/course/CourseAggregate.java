package io.javatab.microservices.api.composite.course;

import io.javatab.microservices.api.core.student.Student;

import java.util.List;

public record CourseAggregate(int courseId, int like, int dislike, int registeredUserNumber, List<Student> registerUserDetails) {
}
