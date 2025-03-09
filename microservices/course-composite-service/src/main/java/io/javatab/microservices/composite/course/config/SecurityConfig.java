package io.javatab.microservices.composite.course.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private String jwkSetUri;

    public SecurityConfig(@Value("${app.jwk-set-uri}") String jwkSetUri) {
        this.jwkSetUri = jwkSetUri;
    }

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/actuator/**", "/api/metrics/**").permitAll()
                        .pathMatchers("/api/course-aggregate/**").hasAnyRole("COURSE-READ", "REVIEW-READ")
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(grantedAuthoritiesExtractor()))
                );

        // Add filter to log roles
        http.addFilterAt((exchange, chain) -> logRoles(exchange).then(chain.filter(exchange)),
                SecurityWebFiltersOrder.AUTHORIZATION);

        return http.build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        return NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    @Bean
    public Converter<Jwt, Mono<JwtAuthenticationToken>> grantedAuthoritiesExtractor() {
        return new Converter<Jwt, Mono<JwtAuthenticationToken>>() {
            @Override
            public Mono<JwtAuthenticationToken> convert(Jwt jwt) {
                Collection<GrantedAuthority> authorities = new ArrayList<>();

                // Extract realm roles
                Map<String, Object> realmAccess = jwt.getClaim("realm_access");
                if (realmAccess != null && realmAccess.containsKey("roles")) {
                    List<String> roles = (List<String>) realmAccess.get("roles");
                    authorities.addAll(roles.stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                            .toList());
                }

                // Extract client roles (replace "my-resource-server" with your client ID)
                /*Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
                if (resourceAccess != null) {
                    Map<String, Object> clientRoles = (Map<String, Object>) resourceAccess.get("my-resource-server");
                    if (clientRoles != null && clientRoles.containsKey("roles")) {
                        List<String> roles = (List<String>) clientRoles.get("roles");
                        authorities.addAll(roles.stream()
                                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                                .toList());
                    }
                }*/
                Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
                if (resourceAccess != null) {
                    resourceAccess.forEach((resource, access) -> {
                        if (access instanceof Map) {
                            Map<String, Object> clientRoles = (Map<String, Object>) access;
                            if (clientRoles.containsKey("roles")) {
                                List<String> roles = (List<String>) clientRoles.get("roles");
                                authorities.addAll(roles.stream()
                                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                                        .toList());
                            }
                        }
                    });
                }

                return Mono.just(new JwtAuthenticationToken(jwt, authorities));
            }
        };
    }

    private Mono<Void> logRoles(ServerWebExchange exchange) {
        return exchange.getPrincipal()
                .cast(JwtAuthenticationToken.class)
                .doOnNext(jwtAuth -> {
                    Collection<? extends GrantedAuthority> authorities = jwtAuth.getAuthorities();
                    logger.info("Roles in Resource Server: {}", authorities);
                })
                .then();
    }
}