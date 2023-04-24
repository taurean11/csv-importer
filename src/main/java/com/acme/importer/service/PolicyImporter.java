package com.acme.importer.service;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.acme.importer.entity.Policy;
import com.acme.importer.exception.CsvImporterException;

/**
 * Class responsible for reading and parsing the contents of the received input file
 * The file is supposed to be a policy file
 */
@Component
public class PolicyImporter {

    private Logger logger = LoggerFactory.getLogger(PolicyImporter.class);

    private List<Policy> policiesToStore;

    public PolicyImporter() {
        policiesToStore = new ArrayList<>();
    }

    /**
     * Tries to import the contents of the received input file
     * The file is supposed to be a policy file
     *
     * @param fileToImport import file path
     * @return a List of policies parsed from the input
     * @throws CsvImporterException if there's an IOException related to the input file
     */
    public List<Policy> doImport(String fileToImport) throws CsvImporterException {

        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setDelimiter("|")
                .build();

        try (CSVParser csvParser = new CSVParser(new FileReader(fileToImport), csvFormat)) {

            logger.info("start processing input file {}", fileToImport);

            int line = 1;
            for (CSVRecord record : csvParser) {

                Policy policy = new Policy();

                // Chdrnum and Cownnum are mandatory
                if(record.size() < 2 || record.get(0).trim().isBlank() || record.get(1).trim().isBlank()){
                    logger.warn("mandatory field is empty in input file {} line {} : {}, skipping line", fileToImport, line, record);
                    continue;
                }

                policy.setChdrnum(record.get(0));
                policy.setCownnum(record.get(1));

                // leaving ownerName NULL if there are only spaces for it in the input
                if(!record.get(2).trim().isBlank()){
                    policy.setOwnerName(record.get(2).trim());
                }

                // [TODO] decide if these also need to be NULL for only spaces in the input
                // in the example file they are correct
                policy.setLifcNum(record.get(3));
                policy.setLifcName(record.get(4).trim());
                policy.setAracde(record.get(5));
                policy.setAgntnum(record.get(6));

                // leaving mailAddress NULL if there are only spaces for it in the input
                if(!record.get(7).trim().isBlank()){
                    policy.setMailAddress(record.get(7).trim());
                }

                logger.debug("processed policy: {}", policy);

                line++;
                policiesToStore.add(policy);
            }
        } catch (IOException e) {
            throw new CsvImporterException(String.format("could not process import file %s", fileToImport), e);
        }

        logger.info("finished processing input file {}", fileToImport);

        return policiesToStore;
    }
}
