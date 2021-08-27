package com.javatab.courseservice.controller.web.v1;


import com.javatab.courseservice.dto.StudentCourseDto;
import com.javatab.courseservice.exception.InvalidCourseOrSeatNotAvailableException;
import com.javatab.courseservice.exception.NoCourseRegisteredForUserException;
import com.javatab.courseservice.model.Student;
import com.javatab.courseservice.service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/students")
@AllArgsConstructor
public class StudentControllerV1 {

    private final StudentService studentService;


    @GetMapping()
    List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{username}/courses")
    List<StudentCourseDto> getRegisteredCourses(@PathVariable("username") String username) {
        if (studentService.getAllRegisteredCoursesForAStudent(username).isEmpty())
            throw new NoCourseRegisteredForUserException();
        else return studentService.getAllRegisteredCoursesForAStudent(username);
    }

    @GetMapping("/{username}")
    Student getStudentByUsername(@PathVariable("username") String username) {
        return studentService.getUserByUsername(username)
                .orElseThrow(InvalidCourseOrSeatNotAvailableException::new);
    }

}
