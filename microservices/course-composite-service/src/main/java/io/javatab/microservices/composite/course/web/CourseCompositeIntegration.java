package io.javatab.microservices.composite.course.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CourseCompositeIntegration {

    private static final Logger LOG = LoggerFactory.getLogger(CourseCompositeIntegration.class);

    private final String courseServiceUrl;
    private final String reviewServiceUrl;
    private final RestTemplate restTemplate;

    public CourseCompositeIntegration(
            @Value("${app.course-service.host}") String courseServiceHost,
            @Value("${app.course-service.port}") String courseServicePort,
            @Value("${app.review-service.host}") String reviewServiceHost,
            @Value("${app.review-service.port}") String reviewServicePort, RestTemplate restTemplate
    ) {
        this.restTemplate = restTemplate;
        courseServiceUrl = "http://" + courseServiceHost + ":" + courseServicePort;
        reviewServiceUrl = "http://" + reviewServiceHost + ":" + reviewServicePort;
    }

    public String getCourse() {
        return restTemplate.getForObject(courseServiceUrl, String.class);
    }

    public String getReview() {
        return restTemplate.getForObject(reviewServiceUrl, String.class);
    }

    public List<Review> getReviewsByCourseId(Long courseId) {
        String url = reviewServiceUrl + "/api/reviews?courseId=" + courseId;

        ResponseEntity<List<Review>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {} // Helps deserialize JSON List
        );

        return response.getBody(); // Returns list of reviews
    }
}
