package com.financial.andrew;

import org.financial.common.configs.FirebaseConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(FirebaseConfig.class)
public class AndrewApplication {

	public static void main(String[] args) {
		SpringApplication.run(AndrewApplication.class, args);
	}



}
