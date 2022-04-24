package io.javatab.microservices.core.search;

import io.javatab.microservices.core.search.persistence.ElasticRepository;
import io.javatab.microservices.core.search.persistence.SearchEntity;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersistenceTests extends ElasticsearchTestBase {

    @Autowired
    private ElasticRepository repository;
    private SearchEntity savedEntity;

    @BeforeEach
    void setUp() {
        repository.deleteCourse();
        SearchEntity entity = new SearchEntity("Course Name", 1);
    }
}
