package io.javatab.microservices.core.course;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan({"io.javatab"})
public class CourseServiceApplication {

	private static final Logger LOG = LoggerFactory.getLogger(CourseServiceApplication.class);

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(CourseServiceApplication.class, args);

		String postgresUri = ctx.getEnvironment().getProperty("spring.datasource.url");
		LOG.info("Connected to Postgres: " + postgresUri);
	}

}
