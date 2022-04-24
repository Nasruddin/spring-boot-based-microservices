package io.javatab.microservices.api.core.search;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

public interface SearchService {

    @GetMapping("/search")
    Mono<SearchRecord> getCourse(@RequestParam("courseName") String courseName);
}
