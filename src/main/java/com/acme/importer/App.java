package com.acme.importer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.acme.importer.entity.InputFileType;
import com.acme.importer.service.OutPayHeaderOrchestrator;
import com.acme.importer.service.RedemptionOrchestrator;
import com.acme.importer.validator.CommandLineParamsValidator;

@SpringBootApplication
public class App  implements CommandLineRunner {

	@Autowired
	CommandLineParamsValidator validator;

	@Autowired
	private RedemptionOrchestrator policyOrchestrator;

	@Autowired
	private OutPayHeaderOrchestrator outpayHeaderOrchestrator;

	@Autowired
	private RedemptionOrchestrator redemptionOrchestrator;

	private Logger logger = LoggerFactory.getLogger(RedemptionOrchestrator.class);

	/**
	 * The received command line arguments are:
	 * type of input file
	 * file system path (as a string) of the input file
	 * e.g. "redemption", "./Rimport.txt"
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@Override
	public void run(String... args) {

		logger.info("csv importer application has started");

		if(!validator.isInputValid(args)){
			System.exit(1);
		}

		if (args[0].toUpperCase().equals(InputFileType.POLICY.toString())) {
			policyOrchestrator.handle(args[1]);
		} else if (args[0].toUpperCase().equals(InputFileType.OUTPAY_HEADER.toString())) {
			outpayHeaderOrchestrator.handle(args[1]);
		} else if (args[0].toUpperCase().equals(InputFileType.REDEMPTION.toString())) {
			redemptionOrchestrator.handle(args[1]);
		}
	}
}
