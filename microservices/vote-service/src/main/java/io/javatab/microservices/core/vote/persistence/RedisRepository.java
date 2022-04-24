package io.javatab.microservices.core.vote.persistence;

import io.javatab.microservices.api.core.vote.Vote;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class RedisRepository {

    private final static String KEY = "VOTE";

    private final ReactiveRedisOperations<String, Vote> redisOperations;
    private final ReactiveHashOperations<String, String, Vote> hashOperations;

    public RedisRepository(ReactiveRedisOperations<String, Vote> redisOperations) {
        this.redisOperations = redisOperations;
        this.hashOperations = redisOperations.opsForHash();
    }

    public Mono<Long> save(Vote post){
        return this.redisOperations.opsForList().rightPush(KEY, post);
    }

    public Mono<Vote> getVote(Long id) {
        return this.redisOperations.opsForList().rightPop(KEY);
    }
}
