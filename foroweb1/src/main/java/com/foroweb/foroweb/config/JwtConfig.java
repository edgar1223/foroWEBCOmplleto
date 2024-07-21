package com.foroweb.foroweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.Key;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;

@Configuration
public class JwtConfig {

    @Bean
    public Key jwtKey() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }
}