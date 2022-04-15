package io.javatab.microservices.composite.course.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javatab.microservices.api.core.course.Course;
import io.javatab.microservices.api.core.course.CourseService;
import io.javatab.microservices.api.core.student.Student;
import io.javatab.microservices.api.core.student.StudentService;
import io.javatab.microservices.api.core.vote.Vote;
import io.javatab.microservices.api.core.vote.VoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

@Component
public class CourseCompositeIntegration implements CourseService, StudentService, VoteService {

    private static final Logger LOG = LoggerFactory.getLogger(CourseCompositeIntegration.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private final String courseServiceUrl;
    private final String studentServiceUrl;
    private final String voteServiceUrl;

    public CourseCompositeIntegration(
            RestTemplate restTemplate,
            ObjectMapper objectMapper,
            @Value("${app.course-service.host}")
            String courseServiceHost,
            @Value("${app.course-service.port}")
            String courseServicePort,
            @Value("${app.student-service.host}")
            String studentServiceHost,
            @Value("${app.student-service.port}")
            String studentServicePort,
            @Value("${app.vote-service.host}")
            String voteServiceHost,
            @Value("${app.vote-service.port}")
            String voteServicePort) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.courseServiceUrl = "http://" + courseServiceHost + ":" + courseServicePort + "/course/";
        this.studentServiceUrl = "http://" + studentServiceHost + ":" + studentServicePort + "/student/";
        this.voteServiceUrl = "http://" + voteServiceHost + ":" + voteServicePort + "/vote/";
    }


    @Override
    public Mono<Course> getCourse(int courseId) {
        LOG.info("Fetching courses in Integration layer");
        var url = this.courseServiceUrl + courseId;
        var course = restTemplate.getForObject(url, Course.class);
        return Mono.just(course);
    }

    @Override
    public Mono<Student> getStudent(String studentId) {
        LOG.info("Fetching students in Integration layer");
        var url = this.studentServiceUrl + studentId;
        var student = restTemplate.getForObject(url, Student.class);
        return Mono.just(student);
    }

    @Override
    public Mono<Vote> getVote(int courseId) {
        LOG.info("Fetching voting details in Integration layer");
        var url = this.voteServiceUrl + courseId;
        var vote = restTemplate.getForObject(url, Vote.class);
        return Mono.just(vote);
    }
}
