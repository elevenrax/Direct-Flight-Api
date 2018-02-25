package com.elevenrax.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.elevenrax.booking","com.elevenrax.commit"})
public class DirectflightApplication {

	public static void main(String[] args) {
		SpringApplication.run(DirectflightApplication.class, args);
	}
}
