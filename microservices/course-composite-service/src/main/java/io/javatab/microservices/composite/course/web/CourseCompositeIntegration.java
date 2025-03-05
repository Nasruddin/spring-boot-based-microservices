package io.javatab.microservices.composite.course.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class CourseCompositeIntegration {

    private static final Logger LOG = LoggerFactory.getLogger(CourseCompositeIntegration.class);

    private final String courseServiceUrl;
    private final String reviewServiceUrl;
    private final WebClient webClient;

    public CourseCompositeIntegration(
            @Value("${app.course-service.host}") String courseServiceHost,
            @Value("${app.course-service.port}") String courseServicePort,
            @Value("${app.review-service.host}") String reviewServiceHost,
            @Value("${app.review-service.port}") String reviewServicePort,
            WebClient.Builder webClient
    ) {
        this.webClient = webClient.build();
        courseServiceUrl = "http://" + courseServiceHost + ":" + courseServicePort;
        reviewServiceUrl = "http://" + reviewServiceHost + ":" + reviewServicePort;
    }

    public Mono<CourseAggregate> getCourseDetails(Long id) {
        String courseUrl = courseServiceUrl + "/api/courses/" + id;
        String reviewUrl = reviewServiceUrl + "/api/reviews?course=" + id;
        Mono<Course> courseMono = webClient.get()
                .uri(courseUrl)
                .retrieve()
                .bodyToMono(Course.class);

        Mono<List<Review>> reviewsMono = webClient.get()
                .uri(reviewUrl)
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
