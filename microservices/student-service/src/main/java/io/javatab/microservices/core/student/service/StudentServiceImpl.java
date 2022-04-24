package io.javatab.microservices.core.student.service;

import io.javatab.microservices.api.core.student.Student;
import io.javatab.microservices.api.core.student.StudentService;
import io.javatab.microservices.util.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class StudentServiceImpl implements StudentService {

    private static final Logger LOG = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final ServiceUtil serviceUtil;

    public StudentServiceImpl(final ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    @Override
    public Mono<Student> getStudent(String studentId) {
        LOG.info("In student service");
        return Mono.just(new Student(1, "Nasir", "nasir@gmail.com", "pass00d"));
    }
}
