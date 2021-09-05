package com.javatab.student;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class MessageFunctionConfiguration {

    @Bean
    Consumer<UserEvent> getNewlyCreatedUser() {
        return userEvent -> System.out.println("New created user " + userEvent);
    }
}
