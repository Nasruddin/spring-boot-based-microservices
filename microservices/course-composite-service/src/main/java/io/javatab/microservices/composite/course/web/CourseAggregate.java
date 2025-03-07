package io.javatab.microservices.composite.course.web;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CourseAggregate {
    private Course course;
    private List<Review> reviews;
}
