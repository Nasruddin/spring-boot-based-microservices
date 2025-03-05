package io.javatab.microservices.composite.course.web;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class Review {

    private String id;
    private int courseId;
    private String author;
    private String content;
    private String email;

}
