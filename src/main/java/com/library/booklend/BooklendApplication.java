package com.library.booklend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.library.booklend.Entity") // Ensure this points to your entity package
public class BooklendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BooklendApplication.class, args);
	}
	@Bean
	public FlywayMigrationStrategy repairStrategy() {
		return flyway -> {
			flyway.repair();
			flyway.migrate();
		};
	}
}
