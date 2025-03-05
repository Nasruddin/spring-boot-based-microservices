package io.javatab.microservices.composite.course;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan({"io.javatab"})
public class CourseCompositeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CourseCompositeServiceApplication.class, args);
	}

}
