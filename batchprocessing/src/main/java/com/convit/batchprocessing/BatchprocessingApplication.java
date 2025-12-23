package com.convit.batchprocessing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BatchprocessingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatchprocessingApplication.class, args);
	}

}
