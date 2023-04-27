package com.acme.importer.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.acme.importer.entity.Policy;
import com.acme.importer.exception.CsvImporterException;

/**
 * Class responsible for orchestrating the import and persist of an input file
 * The file is supposed to be a policy file
 */
@Component
public class PolicyOrchestrator {

    @Autowired
    private PolicyImporter policyImporter;

    @Autowired
    private PolicyService policyService;

    private Logger logger = LoggerFactory.getLogger(PolicyOrchestrator.class);

    public void handle(String fileToImport) {

        logger.info("start importing file {}", fileToImport);

        List<Policy> policiesToStore = new ArrayList<>();
        try {
            policiesToStore = policyImporter.doImport(fileToImport);
        } catch (CsvImporterException e) {
            logger.error(e.getMessage());
            if (e.getCause() != null) {
                logger.debug(e.getCause().getMessage());
            }
            System.exit(1);
        }

        if (policiesToStore.size() == 0) {
            logger.warn("nothing to import after processing input file {}", fileToImport);
            return;
        }

        logger.info("start saving records from file {}", fileToImport);

        try {
            policyService.savePolicies(policiesToStore);
        } catch (Exception e) {
            logger.error("could not write imported records to the database");
            logger.error(e.getMessage());
            System.exit(1);
        }

        logger.info("finished importing and saving file {}", fileToImport);
    }
}
