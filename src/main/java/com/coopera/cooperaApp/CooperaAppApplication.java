package com.coopera.cooperaApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableMongoRepositories

public class CooperaAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(CooperaAppApplication.class, args);
	}

}
