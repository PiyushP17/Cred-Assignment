package com.credassignment.buysell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import lombok.extern.log4j.Log4j2;

@SpringBootApplication
@Log4j2
public class BuysellApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuysellApplication.class, args);
		log.info("Congrats! Your server is started");
	}
}
