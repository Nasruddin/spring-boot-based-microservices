package io.javatab.microservices.composite.course.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class CourseCompositeIntegration {

    private static final Logger logger = LoggerFactory.getLogger(CourseCompositeIntegration.class);

    private final String courseServiceUrl;
    private final String reviewServiceUrl;
    private final WebClient webClient;

    public CourseCompositeIntegration(
            @Value("${app.course-service.uri}") String courseServiceUrl,
            @Value("${app.review-service.uri}") String reviewServiceUrl,
            WebClient.Builder webClient
    ) {
        this.webClient = webClient.build();
        this.courseServiceUrl = courseServiceUrl;
        this.reviewServiceUrl = reviewServiceUrl;
    }

    public Mono<CourseAggregate> getCourseDetails(Long id, Jwt jwt) {
        logger.debug("JWT ===> {}", jwt.getTokenValue());
        String courseUrl = courseServiceUrl + "/api/courses/" + id;
        String reviewUrl = reviewServiceUrl + "/api/reviews?course=" + id;
        logger.debug("Course URL ===> {}", courseUrl);
        logger.debug("Review URL ===> {}", reviewUrl);
        Mono<Course> courseMono = webClient.get()
                .uri(courseUrl)
                .header("Authorization", "Bearer " + jwt.getTokenValue())
                .retrieve()
                .bodyToMono(Course.class);

        Mono<List<Review>> reviewsMono = webClient.get()
                .uri(reviewUrl)
                .header("Authorization", "Bearer " + jwt.getTokenValue())
                .retrieve()
                .bodyToFlux(Review.class)
                .collectList();

        return Mono.zip(courseMono, reviewsMono)
                .map(tuple -> CourseAggregate
                        .builder()
                        .course(tuple.getT1())
                        .reviews(tuple.getT2())
                        .build());
    }
}
