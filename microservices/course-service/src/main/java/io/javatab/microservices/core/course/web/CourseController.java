package io.javatab.microservices.core.course.web;

import io.javatab.microservices.core.course.domain.Course;
import io.javatab.microservices.core.course.domain.CourseService;
import io.javatab.util.http.NetworkUtility;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/courses")
public class CourseController {

    private final NetworkUtility utility;
    private CourseService courseService;

    public CourseController(NetworkUtility utility, CourseService courseService) {
        this.utility = utility;
        this.courseService = courseService;
    }

    @GetMapping
    public Iterable<Course> get() {
        return courseService.viewCourses();
    }

    /*
    * Make sure application is running in localhost mode to test and not in docker
    * http GET ':9001/api/courses/Microservices with Spring Boot'
    * */
    @GetMapping("/{title}")
    public Course getByTitle(@PathVariable String title) {
        return courseService.viewCourseDetails(title);
    }

    /*
    * http POST :9001/api/courses title="Microservices with Spring Boot" author="John Doe" price:=29.79 publisher="Manning"
    * */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Course post(@Valid @RequestBody Course course) {
        return courseService.addCourse(course);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        courseService.removeCourse(id);
    }

    @PutMapping("/{id}")
    public Course put(@PathVariable Long id, @Valid @RequestBody Course course) {
        return courseService.editCourseDetails(id, course);
    }

}
