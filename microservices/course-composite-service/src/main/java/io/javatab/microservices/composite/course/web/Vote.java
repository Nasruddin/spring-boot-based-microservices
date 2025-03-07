package io.javatab.microservices.composite.course.web;

import lombok.Builder;
import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

@Data
@Builder
public class Vote {
    private AtomicInteger likes;
    private AtomicInteger dislikes;
}
