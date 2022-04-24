package io.javatab.microservices.core.student;

import io.javatab.microservices.core.student.persistence.StudentEntity;
import io.javatab.microservices.core.student.persistence.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@Transactional(propagation = NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest(properties = {"spring.jpa.hibernate.ddl-auto=update", "spring.cloud.config.enabled=false"})
public class PersistenceTests extends PostgresTestBase {

    @Autowired
    private StudentRepository repository;

    private StudentEntity savedEntity;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        StudentEntity entity = new StudentEntity(1, "Student Name", "student@email.com");
        savedEntity = repository.save(entity);
        assertEquals(entity, savedEntity);
    }

    @Test
    void create() {
        StudentEntity newEntity = new StudentEntity(1, "Student Name", "student@email.com");
        repository.save(newEntity);

        StudentEntity foundEntity = repository.findById(newEntity.getId()).get();
        assertEquals(newEntity.getId(), newEntity.getId());
    }
}
