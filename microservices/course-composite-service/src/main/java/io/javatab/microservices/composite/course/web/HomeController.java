package io.javatab.microservices.composite.course.web;

import io.javatab.util.http.NetworkUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HomeController {

    private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);


    private final CourseCompositeIntegration integration;
    private final NetworkUtility utility;

    public HomeController(CourseCompositeIntegration integration, NetworkUtility utility) {
        this.integration = integration;
        this.utility = utility;
    }

    @GetMapping("/")
    public List<Review> getGreeting() {
        LOG.info("Info");
        LOG.debug("Debug");
        return integration.getReviewsByCourseId(Long.valueOf(1));
    }
}
