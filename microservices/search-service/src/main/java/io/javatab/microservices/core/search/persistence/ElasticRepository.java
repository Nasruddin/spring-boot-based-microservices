package io.javatab.microservices.core.search.persistence;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.javatab.microservices.api.core.course.Course;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

@Repository
public class ElasticRepository {

    public static final String COURSE_INDEX = "course-store";
    private final ReactiveElasticsearchClient client;
    private final ObjectMapper objectMapper;

    public ElasticRepository(ReactiveElasticsearchClient client, ObjectMapper objectMapper) {
        this.client = client;
        this.objectMapper = objectMapper;
    }

    public Mono<IndexResponse> createCourse(SearchEntity course) {
        String id = UUID.randomUUID().toString();
        Map documentMapper = objectMapper.convertValue(course,
                Map.class);
        return client.index(indexRequest -> indexRequest.index(COURSE_INDEX).id(id).source(documentMapper));
    }

    public Mono<DeleteResponse> deleteCourse() {
        return client.delete(deleteRequest -> deleteRequest.index(COURSE_INDEX));
    }
}
