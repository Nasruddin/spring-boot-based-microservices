package com.assignment.courseservice.repository;

import com.assignment.courseservice.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {

    @Query( "SELECT c FROM Course c WHERE "
            + "c.courseId NOT IN (SELECT sc.courseId FROM "
            + "StudentCourse sc where sc.username = :username)"
    )
    List<Course> findAllCourseNotRegistered(@Param("username") String username);
}
