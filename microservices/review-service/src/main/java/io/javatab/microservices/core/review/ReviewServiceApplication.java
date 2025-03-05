package io.javatab.microservices.core.review;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"io.javatab"})
public class ReviewServiceApplication {

	private static final Logger logger = LoggerFactory.getLogger(ReviewServiceApplication.class);

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(ReviewServiceApplication.class, args);

		String mongoDbUri = ctx.getEnvironment().getProperty("spring.data.mongodb.uri");
        logger.info("Connected to MongoDb ===> {}", mongoDbUri);
	}

}
