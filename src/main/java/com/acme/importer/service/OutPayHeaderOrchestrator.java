package com.acme.importer.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.acme.importer.entity.OutPayHeader;
import com.acme.importer.exception.CsvImporterException;

@Component
public class OutPayHeaderOrchestrator {

    @Autowired
    private OutPayHeaderImporter outPayHeaderImporter;

    @Autowired
    private OutPayHeaderService outPayHeaderService;

    private Logger logger = LoggerFactory.getLogger(OutPayHeaderOrchestrator.class);

    public void handle(String fileToImport) {

        logger.info("start importing file {}", fileToImport);

        List<OutPayHeader> outPayHeadersToStore = new ArrayList<>();
        try {
            outPayHeadersToStore = outPayHeaderImporter.doImport(fileToImport);
        } catch (CsvImporterException e) {
            logger.error(e.getMessage());
            logger.debug(e.getCause().getMessage());
            System.exit(1);
        }

        if (outPayHeadersToStore.size() == 0) {
            logger.warn("nothing to import after processing input file {}", fileToImport);
            return;
        }

        logger.info("start saving records from file {}", fileToImport);

        try {
            outPayHeaderService.saveOutPayHeaders(outPayHeadersToStore);
        } catch (Exception e) {
            logger.error("could not write imported records to the database");
            logger.error(e.getMessage());
            System.exit(1);
        }

        logger.info("finished importing and saving file {}", fileToImport);
    }
}
