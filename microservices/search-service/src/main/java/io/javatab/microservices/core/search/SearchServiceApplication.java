package io.javatab.microservices.core.search;

import io.javatab.microservices.core.search.persistence.ElasticRepository;
import io.javatab.microservices.core.search.persistence.SearchEntity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("io.javatab")
public class SearchServiceApplication implements CommandLineRunner {

	private ElasticRepository repository;

	public SearchServiceApplication(ElasticRepository repository) {
		this.repository = repository;
	}

	public static void main(String[] args) {
		SpringApplication.run(SearchServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		repository.deleteCourse().block();
		repository.createCourse(new SearchEntity("Course Name", 1))
				.subscribe(indexResponse -> System.out.println("Index Response " + indexResponse));
	}
}
