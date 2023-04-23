package com.acme.importer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.acme.importer.entity.InputFileType;
import com.acme.importer.service.PolicyOrchestrator;
import com.acme.importer.validator.CommandLineParamsValidator;

@SpringBootApplication
public class App  implements CommandLineRunner {

	@Autowired
	CommandLineParamsValidator validator;

	@Autowired
	private PolicyOrchestrator policyOrchestrator;

	private Logger logger = LoggerFactory.getLogger(PolicyOrchestrator.class);

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		logger.info("csv importer application has started");

		if(!validator.isInputValid(args)){
			System.exit(1);
		}

		if (args[0].toUpperCase().equals(InputFileType.POLICY.toString())) {
			policyOrchestrator.handlePolicy(args[1]);
		}
	}
}
