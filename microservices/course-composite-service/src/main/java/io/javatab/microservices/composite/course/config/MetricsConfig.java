package io.javatab.microservices.composite.course.config;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class MetricsConfig {

    private final MeterRegistry meterRegistry;
    private final AtomicInteger activeUsers = new AtomicInteger(0);

    public MetricsConfig(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    public void init() {
        // Register a gauge for active users
        Gauge.builder("application.active.users", activeUsers::get)
                .description("Number of active users")
                .register(meterRegistry);
    }

    // Method to update active users (could be called from your service layer)
    public void updateActiveUsers(int count) {
        activeUsers.set(count);
    }
}