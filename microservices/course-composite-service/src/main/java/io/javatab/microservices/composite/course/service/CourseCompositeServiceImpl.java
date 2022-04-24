package io.javatab.microservices.composite.course.service;

import io.javatab.microservices.api.composite.course.CourseAggregate;
import io.javatab.microservices.api.composite.course.CourseCompositeService;
import io.javatab.microservices.api.core.student.Student;
import io.javatab.microservices.util.http.ServiceUtil;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class CourseCompositeServiceImpl implements CourseCompositeService {

    private final CourseCompositeIntegration integration;
    private final ServiceUtil serviceUtil;

    public CourseCompositeServiceImpl(final CourseCompositeIntegration integration, ServiceUtil serviceUtil) {
        this.integration = integration;
        this.serviceUtil = serviceUtil;
    }

    @Override
    public Mono<String> createProduct(CourseAggregate body) {
        return Mono.just("CREATED");
    }

    @Override
    public Mono<CourseAggregate> getCourse(int courseId) {
        // Call integration apis
        integration.getCourse(1).subscribe(course -> System.out.println("Course " + course));
        integration.getStudent("name").subscribe(course -> System.out.println("student " + course));
        integration.getVote(1).subscribe(course -> System.out.println("vote " + course));
        return Mono.just(new CourseAggregate(1, 1, 3, 3,
                List.of(new Student(1, "Student Name", "email", "password"))));
    }

    @Override
    public Mono<String> deleteCourse(int courseId) {
        return Mono.just("DELETED");
    }
}
