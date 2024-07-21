package com.foroweb.foroweb.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    @Bean(name = "customObjectMapper")
    public ObjectMapper customObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        StreamWriteConstraints constraints = StreamWriteConstraints.builder()
                .maxNestingDepth(2000) // Aumenta el límite de profundidad de anidamiento según tus necesidades
                .build();
        mapper.getFactory().setStreamWriteConstraints(constraints);
        return mapper;
    }
}

