package com.morris.aurum.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.HashMap;
import java.util.Map;

@Configuration
@PropertySource("classpath:currency.properties")
public class CountryCodeProperties {
    private final Map<String, String> countryCodeMap = new HashMap<>();

    @Bean
    public Map<String, String> getCountryCodeMap() {
        return countryCodeMap;
    }
}
