package io.javatab.microservices.api.core.student;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

public interface StudentService {

    @GetMapping("/student/{studentId}")
    Mono<Student> getStudent(@PathVariable("studentId") String studentId);
}
