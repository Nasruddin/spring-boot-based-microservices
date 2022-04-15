package io.javatab.microservices.core.course.service;

import io.javatab.microservices.api.core.course.Course;
import io.javatab.microservices.api.core.course.CourseService;
import io.javatab.microservices.util.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class CourseServiceImpl implements CourseService {

    private static final Logger LOG = LoggerFactory.getLogger(CourseServiceImpl.class);

    private final ServiceUtil serviceUtil;

    public CourseServiceImpl(final ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    @Override
    public Mono<Course> getCourse(int courseId) {
        LOG.info("In course service");
        return Mono.just(new Course(123, "Test", "Test", "Test content", 1));
    }
}
