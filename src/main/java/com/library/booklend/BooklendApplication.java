package com.library.booklend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.library.booklend.Repository")
@EntityScan(basePackages = "com.library.booklend.Entity")
@SpringBootApplication
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
