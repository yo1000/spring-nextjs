package com.yo1000.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class SpringNextjsServerApplicationTests {

	@Test
	void contextLoads() {
	}

}
