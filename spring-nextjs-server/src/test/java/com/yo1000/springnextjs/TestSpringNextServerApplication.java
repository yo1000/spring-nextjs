package com.yo1000.springnextjs;

import org.springframework.boot.SpringApplication;

public class TestSpringNextServerApplication {

	public static void main(String[] args) {
		SpringApplication.from(SpringNextjsServerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
