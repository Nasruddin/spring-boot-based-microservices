package io.javatab.springcloud.eurekaserver.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${app.eureka-username}")
    private String username;

    @Value("${app.eureka-password}")
    private String password;

    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
                .withUser(username).password(password)
                .authorities("USER");
    }

    protected void configure(HttpSecurity http) throws Exception {
        http
                // Disable CSRF to allow services to register themselves with Eureka
                .csrf()
                .disable()
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
                    try {
                        authorizationManagerRequestMatcherRegistry
                                .anyRequest().authenticated().and().httpBasic(Customizer.withDefaults());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }
}
