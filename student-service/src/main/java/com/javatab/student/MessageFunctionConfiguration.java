package com.javatab.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class MessageFunctionConfiguration {

    @Autowired
    private StreamBridge streamBridge;

    static final String STUDENT_CREATED_OUTPUT = "studentCreatedOutput";

    @Bean
    Consumer<UserEvent> getNewlyCreatedUser() {
        return userEvent -> {
            System.out.println("New created user " + userEvent);
            Student student = Student.builder().username(userEvent.getUsername()).build();
            boolean isSent = streamBridge.send(STUDENT_CREATED_OUTPUT, student);
            System.out.println("Is sent " + isSent);
        };
    }
}
