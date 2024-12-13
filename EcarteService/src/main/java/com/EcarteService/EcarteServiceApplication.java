package com.EcarteService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class EcarteServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcarteServiceApplication.class, args);
	}

}
