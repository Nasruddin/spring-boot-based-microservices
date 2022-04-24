package io.javatab.microservices.api.core.course;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

public interface CourseService {

    @GetMapping("/course/{courseId}")
    Mono<Course> getCourse(@PathVariable(value = "courseId", required = true) int courseId);

}
