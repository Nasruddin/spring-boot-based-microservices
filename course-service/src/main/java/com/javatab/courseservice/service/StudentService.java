package com.javatab.courseservice.service;

import com.javatab.courseservice.dto.StudentCourseDto;
import com.javatab.courseservice.model.Student;
import com.javatab.courseservice.repository.StudentCourseRepository;
import com.javatab.courseservice.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
