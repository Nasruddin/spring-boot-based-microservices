package io.javatab.microservices.composite.course.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "open-api")
public record OpenApiConfigProperties(String version,
                                      String title,
                                      String description,
                                      String termsOfService,
                                      String license,
                                      String licenseUrl,
                                      String externalDocDesc,
                                      String externalDocUrl,
                                      String contactName,
                                      String contactUrl,
                                      String contactEmail) {
}
