package io.javatab.microservices.composite.course.configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApi {

    private final OpenApiConfigProperties configuration;

    public OpenApi(OpenApiConfigProperties configuration) {
        this.configuration = configuration;
    }

    @Bean
    public OpenAPI getOpenApiDocumentation() {
        return new OpenAPI()
                .info(new Info().title(configuration.title())
                        .description(configuration.description())
                        .version(configuration.version())
                        .contact(new Contact()
                                .name(configuration.contactName())
                                .url(configuration.contactUrl())
                                .email(configuration.contactEmail()))
                        .termsOfService(configuration.termsOfService())
                        .license(new License()
                                .name(configuration.license())
                                .url(configuration.licenseUrl())))
                .externalDocs(new ExternalDocumentation()
                        .description(configuration.externalDocDesc())
                        .url(configuration.externalDocUrl()));
    }

}
