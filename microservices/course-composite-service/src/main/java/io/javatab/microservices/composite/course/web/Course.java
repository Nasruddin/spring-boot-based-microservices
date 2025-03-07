package io.javatab.microservices.composite.course.web;

import lombok.Data;

@Data
public class Course {
    private Long id;
    private String title;
    private String author;
    private Double price;
    private String publisher;
}
