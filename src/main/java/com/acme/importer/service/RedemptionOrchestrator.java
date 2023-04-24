package com.acme.importer.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.acme.importer.entity.Redemption;
import com.acme.importer.exception.CsvImporterException;

/**
 * Class responsible for orchestrating the import and persist of an input file
 * The file is supposed to be a redemption file
 */
@Component
public class RedemptionOrchestrator {

    @Autowired
    private RedemptionImporter RedemptionImporter;

    @Autowired
    private RedemptionService RedemptionService;

    private Logger logger = LoggerFactory.getLogger(RedemptionOrchestrator.class);

    public void handle(String fileToImport) {

        logger.info("start importing file {}", fileToImport);

        List<Redemption> redemptionsToStore = new ArrayList<>();
        try {
            redemptionsToStore = RedemptionImporter.doImport(fileToImport);
        } catch (CsvImporterException e) {
            logger.error(e.getMessage());
            logger.debug(e.getCause().getMessage());
            System.exit(1);
        }

        if (redemptionsToStore.size() == 0) {
            logger.warn("nothing to import after processing input file {}", fileToImport);
            return;
        }

        logger.info("start saving records from file {}", fileToImport);

        try {
            RedemptionService.saveRedemptions(redemptionsToStore);
        } catch (Exception e) {
            logger.error("could not write imported records to the database");
            logger.error(e.getMessage());
            System.exit(1);
        }

        logger.info("finished importing and saving file {}", fileToImport);
    }
}
