package com.cabioglu.tefas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TefasApplication {

	public static void main(String[] args) {
		SpringApplication.run(TefasApplication.class, args);

	}

}
