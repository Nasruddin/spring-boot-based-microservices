package io.javatab.microservices.core.vote.configuration;

import io.javatab.microservices.api.core.vote.Vote;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
public class RedisConfig {

    @Bean
    public ReactiveRedisTemplate<String, Vote> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
        return new ReactiveRedisTemplate<String, Vote>(
                factory,
                RedisSerializationContext.fromSerializer(new Jackson2JsonRedisSerializer(Vote.class))
        );
    }
}
