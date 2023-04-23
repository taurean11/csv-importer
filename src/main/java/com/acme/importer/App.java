package com.acme.importer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.acme.importer.service.PolicyOrchestrator;

@SpringBootApplication
public class App  implements CommandLineRunner {

	@Autowired
	private PolicyOrchestrator policyOrchestrator;

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		policyOrchestrator.handlePolicy("./CCimport.txt");
	}
}
