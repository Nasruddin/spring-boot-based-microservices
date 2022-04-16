package io.javatab.microservices.core.course;

import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class CourseServiceImplApplicationTests extends MongoDbTestBase {

}
