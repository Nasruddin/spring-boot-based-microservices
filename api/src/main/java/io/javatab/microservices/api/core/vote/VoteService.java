package io.javatab.microservices.api.core.vote;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

public interface VoteService {

    @GetMapping("/vote/{courseId}")
    Mono<Vote> getVote(@PathVariable("courseId") int courseId);
}
