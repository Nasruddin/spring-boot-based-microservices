package io.javatab.microservices.core.student;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@ComponentScan("io.javatab")
public class StudentServiceApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(StudentServiceApplication.class, args);
		String postgresqlUri = ctx.getEnvironment().getProperty("spring.datasource.url");
		System.out.println("Connected to Postgres SQL: " + postgresqlUri);
	}

}
