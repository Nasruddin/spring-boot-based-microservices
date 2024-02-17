package io.javatab.microservices.core.course;


import io.javatab.microservices.core.course.persistence.CourseEntity;
import io.javatab.microservices.core.course.persistence.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest(
        properties = {"spring.cloud.config.enabled=false"}
)
public class PersistenceTests extends MongoDbTestBase {

    @Autowired
    private CourseRepository repository;
    private CourseEntity savedEntity;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        CourseEntity entity = new CourseEntity(1, "Course Name", "Author", 4, "Content");
        savedEntity = repository.save(entity);
        assertEquals(entity, savedEntity);
    }

    @Test
    void create() {
        CourseEntity newEntity = new CourseEntity(1, "Course Name", "Author", 4, "Content");
        repository.save(newEntity);

        CourseEntity foundEntity = repository.findById(newEntity.getId()).get();
        assertEquals(newEntity.getId(), foundEntity.getId());
        assertEquals(2, repository.count());
    }
}
