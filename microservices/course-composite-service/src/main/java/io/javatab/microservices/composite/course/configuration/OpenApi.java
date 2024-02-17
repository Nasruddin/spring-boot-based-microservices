package io.javatab.microservices.composite.course.configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SecurityScheme(
        name = "security_auth", type = SecuritySchemeType.OAUTH2,
        flows = @OAuthFlows(
                authorizationCode = @OAuthFlow(
                        authorizationUrl = "${springdoc.oAuthFlow.authorizationUrl}",
                        tokenUrl = "${springdoc.oAuthFlow.tokenUrl}",
                        scopes = {
                                @OAuthScope(name = "course:read", description =
                                        "read scope"),
                                @OAuthScope(name = "course:write", description =
                                        "write scope")
                        }
                )))
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
