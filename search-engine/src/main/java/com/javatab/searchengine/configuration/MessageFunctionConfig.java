package com.javatab.searchengine.configuration;

import com.javatab.searchengine.domain.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class MessageFunctionConfig {

    @Bean
    Consumer<Student> getNewlyCreatedStudent() { // change to event class instead of student
        return student -> System.out.println("New created student " + student);
    }
}