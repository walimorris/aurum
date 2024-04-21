package com.morris.aurum;

import com.morris.aurum.configurations.ApplicationPropertiesConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class AurumApplicationTests {

	@Autowired
	ApplicationContext applicationContext;

	@Test
	void contextLoads() {
		ApplicationPropertiesConfiguration appConfigs = applicationContext.getBean(ApplicationPropertiesConfiguration.class);

	}

}
