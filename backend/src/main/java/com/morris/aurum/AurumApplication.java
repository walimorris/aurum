package com.morris.aurum;

import com.morris.aurum.models.properties.CountryCodeCurrencyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:currency.properties")
@EnableConfigurationProperties({CountryCodeCurrencyProperties.class})
public class AurumApplication {
	public static void main(String[] args) {
		SpringApplication.run(AurumApplication.class, args);
	}
}
