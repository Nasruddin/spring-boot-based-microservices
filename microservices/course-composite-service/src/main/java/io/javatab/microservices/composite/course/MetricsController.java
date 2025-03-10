package io.javatab.microservices.composite.course;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


import java.util.Random;


/*
* Just for manual test for metrics and errors which doesn't sit under security
* */
@RestController
@RequestMapping("/api/metrics")
public class MetricsController {
    private final Logger logger = LoggerFactory.getLogger(MetricsController.class);
    private final MeterRegistry meterRegistry;
    private Counter requestCounter;
    private Timer requestTimer;

    public MetricsController(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    public void init() {
        // Initialize custom metrics
        requestCounter = Counter
                .builder("api.requests.total")
                .description("Total number of API requests")
                .tags("endpoint", "/hello")
                .register(meterRegistry);

        requestTimer = Timer
                .builder("api.request.duration")
                .description("Time taken to process requests")
                .tags("endpoint", "/hello")
                .register(meterRegistry);
    }

    @GetMapping("/hello")
    public String hello() {
        logger.info("Hello endpoint called");
        logger.warn("This is a warning log");
        // Record request count
        requestCounter.increment();

        // Record execution time
        return requestTimer.record(() -> {
            try {
                // Simulate some work
                int sleepTime = new Random().nextInt(1000);
                Thread.sleep(sleepTime);
                return "Hello, World!";
            } catch (InterruptedException e) {
                return "Error occurred";
            }
        });
    }

    @GetMapping("/runtime-error")
    public String error() {
        logger.error("An error occurred", new RuntimeException("Test exception"));
        return "Error logged";
    }

    @GetMapping("/error")
    public String triggerError() {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong!");
    }
}