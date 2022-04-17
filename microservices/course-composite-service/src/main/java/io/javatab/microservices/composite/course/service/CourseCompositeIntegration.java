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
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import static java.util.logging.Level.FINE;

@Component
public class CourseCompositeIntegration implements CourseService, StudentService, VoteService {

    private static final Logger LOG = LoggerFactory.getLogger(CourseCompositeIntegration.class);

    private WebClient webClient;
    private final ObjectMapper objectMapper;

    private static final String COURSE_SERVICE_URL = "http://course";
    private static final String STUDENT_SERVICE_URL = "http://student";
    private static final String VOTE_SERVICE_URL = "http://vote";

    public CourseCompositeIntegration(
            WebClient.Builder webClientBuilder,
            ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.build();
        this.objectMapper = objectMapper;
    }


    @Override
    public Mono<Course> getCourse(int courseId) {
        LOG.info("Fetching courses in Integration layer");
        var url = COURSE_SERVICE_URL + "/course/" + courseId;
        return webClient.get().uri(url).retrieve().bodyToMono(Course.class).log(LOG.getName(), FINE);
    }

    @Override
    public Mono<Student> getStudent(String studentId) {
        LOG.info("Fetching students in Integration layer");
        var url = STUDENT_SERVICE_URL + "/student/" + studentId;
        return webClient.get().uri(url).retrieve().bodyToMono(Student.class).log(LOG.getName(), FINE);
    }

    @Override
    public Mono<Vote> getVote(int courseId) {
        LOG.info("Fetching votes in Integration layer");
        var url = VOTE_SERVICE_URL + "/vote/" + courseId;
        return webClient.get().uri(url).retrieve().bodyToMono(Vote.class).log(LOG.getName(), FINE);
    }
}
