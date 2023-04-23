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

@Component
public class PolicyImporter {

    private Logger logger = LoggerFactory.getLogger(PolicyOrchestrator.class);

    private List<Policy> policiesToStore;

    public PolicyImporter() {
        policiesToStore = new ArrayList<>();
    }

    public List<Policy> doImport(String fileToImport) {

        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setDelimiter("|")
                .build();

        try (CSVParser csvParser = new CSVParser(new FileReader(fileToImport), csvFormat)) {

            logger.info("start processing input file {}", fileToImport);

            int line = 1;
            for (CSVRecord record : csvParser) {

                Policy policy = new Policy();

                // Chdrnum and Cownnum are mandatory
                if(record.get(0).trim().isBlank() || record.get(1).trim().isBlank()){
                    logger.warn("warning: mandatory field is empty in input file {} line {} : {}, skipping line", fileToImport, line, record);
                    continue;
                }

                policy.setChdrnum(record.get(0));
                policy.setCownnum(record.get(1));
                policy.setOwnerName(record.get(2));
                policy.setLifcNum(record.get(3));
                policy.setLifcName(record.get(4));
                policy.setAracde(record.get(5));
                policy.setAgntnum(record.get(6));
                policy.setMailAddress(record.get(7));

                logger.debug("processed policy: {}", policy);

                line++;
                policiesToStore.add(policy);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("finished processing input file {}", fileToImport);

        return policiesToStore;
    }
}
