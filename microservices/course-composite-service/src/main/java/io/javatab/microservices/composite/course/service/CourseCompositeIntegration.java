package io.javatab.microservices.composite.course.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javatab.microservices.api.core.course.Course;
import io.javatab.microservices.api.core.course.CourseService;
import io.javatab.microservices.api.core.student.Student;
import io.javatab.microservices.api.core.student.StudentService;
import io.javatab.microservices.api.core.vote.Vote;
import io.javatab.microservices.api.core.vote.VoteService;
import io.javatab.microservices.api.exceptions.InvalidInputException;
import io.javatab.microservices.api.exceptions.NotFoundException;
import io.javatab.microservices.util.http.HttpErrorInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static java.util.logging.Level.FINE;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Component
public class CourseCompositeIntegration implements CourseService, StudentService, VoteService {

    private static final Logger LOG = LoggerFactory.getLogger(CourseCompositeIntegration.class);

    private WebClient webClient;
    private final ObjectMapper objectMapper;

    private static final String COURSE_SERVICE_URL = "http://course";
    private static final String STUDENT_SERVICE_URL = "http://student";
    private static final String VOTE_SERVICE_URL = "http://vote";

    public CourseCompositeIntegration(
            WebClient.Builder webClient,
            ObjectMapper objectMapper) {
        this.webClient = webClient.build();
        this.objectMapper = objectMapper;
    }

    private String getErrorMessage(WebClientResponseException ex) {
        try {
            return objectMapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        } catch (IOException ioex) {
            return ex.getMessage();
        }
    }

    private Throwable handleException(Throwable ex) {

        if (!(ex instanceof WebClientResponseException webClientResponseException)) {
            LOG.warn("Got a unexpected error: {}, will rethrow it", ex.toString());
            return ex;
        }

        HttpStatusCode statusCode = webClientResponseException.getStatusCode();
        if (NOT_FOUND.equals(statusCode)) {
            return new NotFoundException(getErrorMessage(webClientResponseException));
        } else if (UNPROCESSABLE_ENTITY.equals(statusCode)) {
            return new InvalidInputException(getErrorMessage(webClientResponseException));
        }
        LOG.warn("Got an unexpected HTTP error: {}, will rethrow it", webClientResponseException.getStatusCode());
        LOG.warn("Error body: {}", webClientResponseException.getResponseBodyAsString());
        return ex;
    }
    @Override
    public Mono<Course> getCourse(int courseId) {
        LOG.info("Fetching courses in Integration layer");
        var url = COURSE_SERVICE_URL + "/course/" + courseId;
        return webClient.get().uri(url).retrieve().bodyToMono(Course.class).map(course -> {
            System.out.println("Course" + course);
            return new Course(1, "d", "f", "r", 2);
        }).log(LOG.getName(), FINE).onErrorMap(WebClientResponseException.class, this::handleException);
    }

    @Override
    public Mono<Student> getStudent(String studentId) {
        LOG.info("Fetching students in Integration layer");
        var url = STUDENT_SERVICE_URL + "/student/" + studentId;
        return webClient.get().uri(url).retrieve().bodyToMono(Student.class).log(LOG.getName(), FINE).onErrorMap(WebClientResponseException.class, this::handleException);
    }

    @Override
    public Mono<Vote> getVote(int courseId) {
        LOG.info("Fetching votes in Integration layer");
        var url = VOTE_SERVICE_URL + "/vote/" + courseId;
        return webClient.get().uri(url).retrieve().bodyToMono(Vote.class).log(LOG.getName(), FINE).onErrorMap(WebClientResponseException.class, this::handleException);
    }
}
