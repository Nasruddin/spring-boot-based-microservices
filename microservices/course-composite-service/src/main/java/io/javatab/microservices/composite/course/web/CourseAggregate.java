package io.javatab.microservices.composite.course.web;


import java.util.List;

public class CourseAggregate {
    private Long id;
    private String title;
    private String author;
    private Double price;
    private String publisher;
    private List<Review> reviews;
}
