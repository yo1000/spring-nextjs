package com.yo1000.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"spring.sql.init.mode=never",
		"app.security.idp=test"
})
class SpringNextjsServerApplicationTests {
	@Test
	void contextLoads() {
		// Dependency checks only
	}
}
