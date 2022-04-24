package io.javatab.microservices.core.vote.persistence;

import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("vote")
public class VoteEntity {

    @Id
    private String id;

    @Version
    private Integer version;

    private int courseId;

    private int studentId;

    private int like;

    private int dislike;
}
