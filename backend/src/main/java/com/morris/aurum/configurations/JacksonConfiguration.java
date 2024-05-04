package com.morris.aurum.configurations;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.morris.aurum.models.serializers.BsonDateTimeDeserializer;
import org.bson.BsonDateTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuration to create a {@link Bean} for Hibernate Module for Jackson. SpringBoot's
 * {@link org.springframework.http.converter.json.Jackson2ObjectMapperBuilder} will use
 * this via autoconfiguration, and all ObjectMapper instance will use the Spring Boot
 * defaults plus customizations.
 */
@Configuration
public class JacksonConfiguration {

    @Bean
    public Module hibernateModule() {
        return new Hibernate6Module();
    }

    /**
     * Add Java Time Module for Date and Time manipulations with {@link ObjectMapper}
     *
     * @return {@link ObjectMapper}
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(BsonDateTime.class, new BsonDateTimeDeserializer());
        return JsonMapper.builder()
                .addModules(new JavaTimeModule(), simpleModule)
                .build();
    }
}
