package io.javatab.springcloud.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class GatewayApplication {

	private static final Logger LOG = LoggerFactory.getLogger(GatewayApplication.class);

	@Bean
	@LoadBalanced
	public WebClient.Builder loadBalancedWebClientBuilder() {
		return WebClient.builder();
	}

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(GatewayApplication.class, args);

		String testdata = ctx.getEnvironment().getProperty("test1.data");
		LOG.info("testdata ====> " + testdata);
	}
}
