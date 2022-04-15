package io.javatab.microservices.core.vote.service;

import io.javatab.microservices.api.core.vote.Vote;
import io.javatab.microservices.api.core.vote.VoteService;
import io.javatab.microservices.util.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@ComponentScan("io.javatab")
public class VoteServiceImpl implements VoteService {

    private static final Logger LOG = LoggerFactory.getLogger(VoteServiceImpl.class);

    private final ServiceUtil serviceUtil;

    public VoteServiceImpl(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    @Override
    public Mono<Vote> getVote(int courseId) {
        LOG.info("In vote service");
        return Mono.just(new Vote(1, 123, 1, 2));
    }
}
