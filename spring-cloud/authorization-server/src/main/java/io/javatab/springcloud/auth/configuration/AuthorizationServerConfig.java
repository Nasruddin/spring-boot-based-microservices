package io.javatab.springcloud.auth.configuration;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import io.javatab.springcloud.auth.jose.Jwks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;

import java.time.Duration;
import java.util.UUID;

@Configuration(proxyBeanMethods = false)
@Import(OAuth2AuthorizationServerConfiguration.class)
public class AuthorizationServerConfig {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorizationServerConfig.class);

    // @formatter:off
    @Bean
    public RegisteredClientRepository registeredClientRepository() {

        LOG.info("register OAUth client allowing all grant flows...");
        RegisteredClient writerClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("writer")
                .clientSecret("secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .redirectUri("https://my.redirect.uri")
                .redirectUri("https://localhost:8443/webjars/swagger-ui/oauth2-redirect.html")
                .scope(OidcScopes.OPENID)
                .scope("product:read")
                .scope("product:write")
                .clientSettings(clientSettings -> clientSettings.requireUserConsent(true))
                .tokenSettings(ts -> ts.accessTokenTimeToLive(Duration.ofHours(1)))
                .build();

        RegisteredClient readerClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("reader")
                .clientSecret("secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .redirectUri("https://my.redirect.uri")
                .redirectUri("https://localhost:8443/webjars/swagger-ui/oauth2-redirect.html")
                .scope(OidcScopes.OPENID)
                .scope("product:read")
                .clientSettings(clientSettings -> clientSettings.requireUserConsent(true))
                .tokenSettings(ts -> ts.accessTokenTimeToLive(Duration.ofHours(1)))
                .build();
        return new InMemoryRegisteredClientRepository(writerClient, readerClient);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = Jwks.generateRsa();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    @Bean
    public ProviderSettings providerSettings() {
        return new ProviderSettings().issuer("http://auth-server:9999");
    }
}