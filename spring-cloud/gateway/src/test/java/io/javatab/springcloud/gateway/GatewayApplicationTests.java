package io.javatab.springcloud.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(
		webEnvironment = RANDOM_PORT,
		properties = {
				"spring.security.oauth2.resourceserver.jwt.issuer-uri=",
				"spring.security.oauth2.resourceserver.jwt.jwk-set-uri=some-url",
				"spring.main.allow-bean-definition-overriding=true",
				"eureka.client.enabled=false",
				"spring.cloud.config.enabled=false"})
class GatewayApplicationTests {

}
