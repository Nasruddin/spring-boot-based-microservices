package com.javatab.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/students")
public class StudentController {

    @Autowired
    StudentRepository studentRepository;

    @PostMapping
    public Student save(@RequestBody Student student) {
        return studentRepository.save(student);
    }

    @GetMapping("{username}")
    public Student getUsername(@PathVariable("username") String username) {
            return studentRepository.findByUsername(username).get();
    }
}
