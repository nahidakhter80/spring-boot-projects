package com.schedule.generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScheduleGeneratorApplication {

	public static void main(String[] args) {
		System.out.println("\n\nPlease wait while we setup the envrionment...");
		SpringApplication.run(ScheduleGeneratorApplication.class, args);
		System.out.println("\n\nEnvrionment is ready...");
		System.out.println("\nSet Generator application can be accessed on below URL:\nhttp://localhost:8080/schedule/index.html");
	}	
}
