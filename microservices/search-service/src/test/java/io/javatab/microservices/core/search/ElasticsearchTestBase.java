package io.javatab.microservices.core.search;

import org.apache.http.HttpHost;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import java.io.IOException;
import java.util.Collections;

public abstract class ElasticsearchTestBase {
    private static ElasticsearchContainer database = new ElasticsearchContainer("elasticsearch:7.17.2");

    static {
        database.start();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.elasticsearch.uris", () -> Collections.singletonList("http://" + database.getContainerIpAddress() + "/9200"));
    }

    RestClient client = RestClient.builder(HttpHost.create(database.getHttpHostAddress()))
            .build();

    Response response;

    {
        try {
            response = client.performRequest(new Request("GET", "/_cluster/health"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
