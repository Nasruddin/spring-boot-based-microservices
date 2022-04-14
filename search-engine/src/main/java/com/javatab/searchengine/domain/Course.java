package com.javatab.searchengine.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Course {

    private String courseId;
    private String courseName;
    private String description;
}
