package com.assignment.courseservice.repository;

import com.assignment.courseservice.dto.StudentCourseDto;
import com.assignment.courseservice.model.StudentCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {
    @Query("SELECT new com.assignment.courseservice.dto.StudentCourseDto(c.courseId, c.courseName, c.description, sc.status) "
            + "FROM Course c INNER JOIN StudentCourse sc "
            + "ON c.courseId = sc.courseId "
            + "WHERE sc.username = :username")
    List<StudentCourseDto> findAllCoursesRegistered(@Param("username") String username);

    @Query("SELECT sc "
            + "FROM  StudentCourse sc where sc.courseId = :courseId "
            + "AND sc.username = :username")
    Optional<StudentCourse> findByCourseIdAndStudentId(@Param("courseId") String courseId,
                                                        @Param("username") String username);
}
