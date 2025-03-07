package com.example.springcloud.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/public").permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(grantedAuthoritiesExtractor()))
                );

        return http.build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        return NimbusReactiveJwtDecoder.withJwkSetUri("http://localhost:8081/realms/course-management-realm/protocol/openid-connect/certs").build();
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
}