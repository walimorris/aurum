package com.morris.aurum;

import com.morris.aurum.configurations.ApplicationPropertiesConfiguration;
import com.morris.aurum.configurations.JacksonConfiguration;
import com.morris.aurum.configurations.SpringMongoConfig;
import com.morris.aurum.models.properties.CountryCodeCurrencyProperties;
import com.morris.aurum.models.types.CountryCode;
import com.morris.aurum.models.types.CurrencyType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class AurumApplicationTests {

	@Autowired
	ApplicationContext applicationContext;

	@Test
	void contextLoads() {
		ApplicationPropertiesConfiguration appConfigs = applicationContext.getBean(ApplicationPropertiesConfiguration.class);
		CountryCodeCurrencyProperties countryCodeProperties = applicationContext.getBean(CountryCodeCurrencyProperties.class);
		JacksonConfiguration jacksonConfiguration = applicationContext.getBean(JacksonConfiguration.class);
		SpringMongoConfig springMongoConfig = applicationContext.getBean(SpringMongoConfig.class);

		assertAll(
				() -> Assertions.assertNotNull(applicationContext),
				() -> Assertions.assertNotNull(appConfigs),
				() -> Assertions.assertNotNull(countryCodeProperties),
				() -> Assertions.assertNotNull(jacksonConfiguration),
				() -> Assertions.assertNotNull(springMongoConfig)
		);

		Map<CountryCode, CurrencyType> countryCodeMap = countryCodeProperties.codes();
		Assertions.assertNotNull(countryCodeMap);
		Assertions.assertEquals(CurrencyType.USD, countryCodeMap.get(CountryCode.USA));
	}

}
