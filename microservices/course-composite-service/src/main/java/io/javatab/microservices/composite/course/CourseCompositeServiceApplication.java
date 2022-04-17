package io.javatab.microservices.composite.course;

import io.javatab.microservices.composite.course.configuration.OpenApiConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@ComponentScan("io.javatab")
@EnableConfigurationProperties(OpenApiConfigProperties.class)
public class CourseCompositeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CourseCompositeServiceApplication.class, args);
	}

	@Bean
	@LoadBalanced
	public WebClient.Builder loadBalancedWebClientBuilder() {
		return WebClient.builder();
	}

}
