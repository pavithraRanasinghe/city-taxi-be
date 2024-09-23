package com.esoft.citytaxi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@SpringBootApplication
public class CityTaxiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CityTaxiApplication.class, args);
	}

}
