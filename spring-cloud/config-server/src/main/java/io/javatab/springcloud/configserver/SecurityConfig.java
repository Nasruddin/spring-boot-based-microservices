package io.javatab.springcloud.configserver;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    protected void configure(HttpSecurity http) throws Exception {
        http
                // Disable CSRF to allow POST to /encrypt and /decrypt endpoints
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(expressionInterceptUrlRegistry -> {
                    try {
                        expressionInterceptUrlRegistry
                                .requestMatchers(
                                        antMatcher("/config/**")
                                ).permitAll()
                                .anyRequest().authenticated()
                                .and()
                                .httpBasic(Customizer.withDefaults());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }
}


