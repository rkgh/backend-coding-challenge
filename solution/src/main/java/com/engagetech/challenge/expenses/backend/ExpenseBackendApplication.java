package com.engagetech.challenge.expenses.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@EntityScan(basePackageClasses = {ExpenseBackendApplication.class, Jsr310JpaConverters.class})
@SpringBootApplication
public class ExpenseBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpenseBackendApplication.class, args);
	}
}
