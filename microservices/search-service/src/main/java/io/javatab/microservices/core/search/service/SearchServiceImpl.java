package io.javatab.microservices.core.search.service;

import io.javatab.microservices.api.core.search.SearchRecord;
import io.javatab.microservices.api.core.search.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class SearchServiceImpl implements SearchService {
    private static final Logger LOG = LoggerFactory.getLogger(SearchServiceImpl.class);


    @Override
    public Mono<SearchRecord> getCourse(String courseName) {
        LOG.info("In log service");
        return Mono.just(new SearchRecord(1, "course name", 1, 2));
    }
}
