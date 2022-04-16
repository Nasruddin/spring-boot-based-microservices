package io.javatab.microservices.core.vote;

import io.javatab.microservices.api.core.vote.Vote;
import io.javatab.microservices.core.vote.persistence.RedisRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VoteServiceApplication implements CommandLineRunner {

	private final RedisRepository repository;

	public VoteServiceApplication(RedisRepository repository) {
		this.repository = repository;
	}

	public static void main(String[] args) {
		SpringApplication.run(VoteServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Vote vote = new Vote(1, 1, 3, 9);
		repository.save(vote).subscribe(aLong -> System.out.println(repository.getVote(aLong).subscribe(vote1 -> System.out.println("Vote " + vote1.courseId()))));
	}
}
