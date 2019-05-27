package com.assignment.courseservice.controller.web.v1;

import com.assignment.courseservice.model.Course;
import com.assignment.courseservice.model.Status;
import com.assignment.courseservice.model.StudentCourse;
import com.assignment.courseservice.service.CourseService;
import com.assignment.courseservice.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CourseControllerV1.class)
public class CourseControllerV1Test {

    @MockBean
    CourseService courseService;

    @MockBean
    StudentService studentService;

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void it_should_register_course_for_a_user() throws Exception {

        StudentCourse studentCourse = StudentCourse.builder()
                .username("username")
                .courseId("12345")
                .status(Status.PENDING)
                .build();

        Course course = Course.builder()
                .courseName("Test")
                .availableSeats(100)
                .expired(false)
                .build();

        when(courseService.findStudentCourse(any(String.class), any(String.class))).thenReturn(Optional.empty());
        when(courseService.findCourse(any(String.class))).thenReturn(Optional.of(course));
        when(courseService.register(any(String.class), any(String.class))).thenReturn(studentCourse);

        mockMvc.perform(post("/v1/courses/register")
                .content(objectMapper.writeValueAsString(studentCourse))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseId").value(studentCourse.getCourseId()));
    }

}
