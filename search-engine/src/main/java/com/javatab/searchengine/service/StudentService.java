package com.javatab.searchengine.service;

import com.javatab.searchengine.domain.Student;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.collect.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final RestHighLevelClient client;
    private final ObjectMapper mapper;

    public String createStudentDocument(Student student) throws IOException {
        Student aStudent = Student.builder()
                .id(UUID.randomUUID().toString())
                .username(student.getUsername())
                .email(student.getEmail()).build();

        Map documentMapper = mapper.convertValue(student, Map.class);

        IndexRequest indexRequest = new IndexRequest("student-document").id(student.getId())
                .source(documentMapper, XContentType.JSON);

        IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);

        return indexResponse
                .getResult()
                .name();
    }

    public Student findById(String id) throws IOException {
        GetRequest getRequest = new GetRequest("student-document", id);

        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        return mapper
                .convertValue(getResponse.getSource(), Student.class);
    }

    public Student findByUsername(String username) throws IOException {
        GetRequest getRequest = new GetRequest("student-document", username);

        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        return mapper
                .convertValue(getResponse.getSource(), Student.class);
    }
}
