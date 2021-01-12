package com.intraway.technicaltest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class IntrawayApplication {

	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(IntrawayApplication.class, args);
	}

}
