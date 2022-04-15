package io.javatab.microservices.api.composite.course;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

public interface CourseCompositeService {

    @GetMapping("/course-composite/{courseId}")
    Mono<CourseAggregate> getCourse(@PathVariable("courseId") int courseId);
}
