package com.javatab.searchengine.api;

import com.javatab.searchengine.StudentRepository;
import com.javatab.searchengine.domain.Student;
import com.javatab.searchengine.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.function.Supplier;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchApi {

    private final StudentService studentService;
    private final StudentRepository studentRepository;

    @PostMapping("/create-index")
    public Mono<Student> createStudentIndex(@RequestBody Student student) throws IOException {
        return studentRepository.save(student);
    }

    @GetMapping("/id/{userid}")
    public Mono<Student> getUserById(@PathVariable("userid") String id) throws IOException {
        return studentRepository.findById(id);
    }

    @GetMapping("/username/{username}")
    public Flux<Student> getUserByUsername(@PathVariable("username") String username) throws IOException {
        return studentRepository.findStudentByUsername(username);
    }
}
