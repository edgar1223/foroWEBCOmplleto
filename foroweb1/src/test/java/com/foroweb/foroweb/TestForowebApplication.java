package com.foroweb.foroweb;

import org.springframework.boot.SpringApplication;

public class TestForowebApplication {

	public static void main(String[] args) {
		SpringApplication.from(ForowebApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
