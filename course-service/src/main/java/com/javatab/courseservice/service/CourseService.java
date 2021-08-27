package com.javatab.courseservice.service;

import com.javatab.courseservice.model.Course;
import com.javatab.courseservice.model.Status;
import com.javatab.courseservice.model.StudentCourse;
import com.javatab.courseservice.repository.CourseRepository;
import com.javatab.courseservice.repository.StudentCourseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final StudentCourseRepository studentCourseRepository;

    public List<Course> getAllCourse(){
        return this.courseRepository.findAll();
    }

    public StudentCourse register(String courseId, String username) {
        StudentCourse studentCourse = StudentCourse.builder()
                    .courseId(courseId)
                    .username(username)
                    .status(Status.PENDING)
                    .build();
        courseRepository.findById(courseId).ifPresent(course ->  {
            course.setAvailableSeats(course.getAvailableSeats() - 1);
            courseRepository.save(course);
        });
        return studentCourseRepository.save(studentCourse);
    }

    public List<Course> findAllNotRegisteredCourses(String username) {
        return courseRepository.findAllCourseNotRegistered(username);
    }

    public Optional<Course> findCourse(String courseId) {
        return courseRepository.findById(courseId);
    }

    public Optional<StudentCourse> findStudentCourse(String courseId, String studentUsername) {
        return studentCourseRepository.findByCourseIdAndStudentId(courseId, studentUsername);
    }
}
