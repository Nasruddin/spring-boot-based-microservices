package com.assignment.courseservice.service;

import com.assignment.courseservice.dto.StudentCourseDto;
import com.assignment.courseservice.model.Student;
import com.assignment.courseservice.repository.StudentCourseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.assignment.courseservice.repository.StudentRepository;

@Service
@AllArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentCourseRepository studentCourseRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public List<StudentCourseDto> getAllRegisteredCoursesForAStudent(String username) {
        return studentCourseRepository.findAllCoursesRegistered(username);
    }

    public Optional<Student> getUserByUsername(String username) {
        return studentRepository.findByUsername(username);
    }

}
