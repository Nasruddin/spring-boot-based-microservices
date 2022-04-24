package io.javatab.microservices.core.vote;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class RedisTestBase {

    private static GenericContainer redisContainer = new GenericContainer(
            DockerImageName.parse("redis:6.2.6-alpine")
    ).withExposedPorts(6379);

    static {
        redisContainer.start();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("redis.host", redisContainer::getContainerIpAddress);
        registry.add("redis.port", () -> redisContainer.getMappedPort(6379));
        registry.add("redis.database", () -> "test");
    }
}
