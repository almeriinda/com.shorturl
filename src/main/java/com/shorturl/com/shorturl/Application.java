package com.shorturl.com.shorturl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.shorturl.com.shorturl")
@EntityScan(basePackages = "com.shorturl.com.shorturl")

public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}

