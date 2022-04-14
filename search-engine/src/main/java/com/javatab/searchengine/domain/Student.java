package com.javatab.searchengine.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Builder
@Document(indexName = "student")
public class Student {

    private String id;
    private String username;
    private String email;
}
