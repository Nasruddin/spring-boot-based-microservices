package io.javatab.microservices.core.course.persistence;

import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<CourseEntity, String> {
}
