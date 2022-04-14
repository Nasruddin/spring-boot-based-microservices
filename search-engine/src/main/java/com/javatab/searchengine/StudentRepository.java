package com.javatab.searchengine;

import com.javatab.searchengine.domain.Student;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface StudentRepository extends ReactiveCrudRepository<Student, String> {
    Flux<Student> findStudentByUsername(String username);
}
