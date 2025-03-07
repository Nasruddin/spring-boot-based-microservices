package io.javatab.microservices.core.review.web;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewResponse {
    private String id;
    // Add other response related object data
}
