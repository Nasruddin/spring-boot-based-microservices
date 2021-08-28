package com.javatab.authservice.security;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;

@Getter        // lombok will create getters auto.
@Setter
@ToString        // [IMP] You need to install lombok jar file: https://stackoverflow.com/a/11807022
public class JwtConfig {

    // Spring doesn't inject/autowire to "static" fields.
    // Link: https://stackoverflow.com/a/6897406
    @Value("${security.jwt.uri:/auth/**}")
    private String Uri;

    @Value("${security.jwt.header:Authorization}")
    private String header;

    @Value("${security.jwt.prefix:Bearer }")
    private String prefix;

    @Value("${security.jwt.expiration:#{24*60*60}}")
    private int expiration;

    @Value("${security.jwt.secret:JwtSecretKey}")
    private String secret;

}