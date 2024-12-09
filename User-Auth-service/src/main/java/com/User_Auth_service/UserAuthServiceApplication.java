package com.User_Auth_service;

import com.User_Auth_service.dto.ReqRes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserAuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserAuthServiceApplication.class, args);
		System.out.println(ReqRes.class.getName());

	}

}
