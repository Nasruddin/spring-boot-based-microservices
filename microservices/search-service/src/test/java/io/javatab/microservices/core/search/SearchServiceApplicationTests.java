package io.javatab.microservices.core.search;

import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class SearchServiceApplicationTests extends ElasticsearchTestBase {

}
