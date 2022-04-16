package io.javatab.microservices.core.vote;

import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class VoteServiceApplicationTests extends RedisTestBase {


}
