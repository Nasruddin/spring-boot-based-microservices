package io.javatab.microservices.composite.course.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/course-aggregate")
public class CourseAggregateController {

    private static final Logger logger = LoggerFactory.getLogger(CourseAggregateController.class);


    private final CourseCompositeIntegration integration;
    //private final NetworkUtility utility;

    public CourseAggregateController(CourseCompositeIntegration integration) {
        this.integration = integration;
    }

    @GetMapping("/{id}/with-details")
    public Mono<CourseAggregate> getCourses(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        logger.info("Fetching course and review details for course id ===> {}", id);
        return integration.getCourseDetails(id, jwt);
    }
}
