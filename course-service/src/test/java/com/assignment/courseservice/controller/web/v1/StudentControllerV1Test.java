package com.assignment.courseservice.controller.web.v1;

import com.assignment.courseservice.dto.StudentCourseDto;
import com.assignment.courseservice.model.Student;
import com.assignment.courseservice.service.StudentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(StudentControllerV1.class)
public class StudentControllerV1Test {

    @MockBean
    StudentService studentService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void it_Should_return_all_students() throws Exception {
        List<Student> lStudents = Arrays.asList(
                Student.builder()
                    .department("Mechanical")
                    .studentId("123456789")
                    .build()
                );
        when(studentService.getAllStudents()).thenReturn(lStudents);

        mockMvc.perform(get("/v1/students")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void it_should_return_all_registered_courses_for_username() throws Exception{
        List<StudentCourseDto> studentCourseDto = Arrays.asList(
                StudentCourseDto.builder()
                    .courseId("12345")
                    .courseName("Test course name")
                    .description("Test course desc")
                    .build()
                );
        when(studentService.getAllRegisteredCoursesForAStudent(
                any(String.class)))
                .thenReturn(studentCourseDto);

        mockMvc.perform(get("/v1/students/")
                .param("username", "username"))
                .andExpect(status().isOk());
    }
}
