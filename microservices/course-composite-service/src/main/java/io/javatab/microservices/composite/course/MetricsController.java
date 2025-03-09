package io.javatab.microservices.composite.course;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.Random;

@RestController
@RequestMapping("/api/metrics")
public class MetricsController {

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
}