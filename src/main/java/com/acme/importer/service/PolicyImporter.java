package com.acme.importer.service;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.acme.importer.dto.Policy;

public class PolicyImporter {

    private String fileToImport;
    private List<Policy> policiesToStore;

    public PolicyImporter(String fileToImport) {
        this.fileToImport = fileToImport;
        policiesToStore = new ArrayList<>();
    }

    public void doImport() {

        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setDelimiter("|")
                .build();

        try (CSVParser csvParser = new CSVParser(new FileReader(fileToImport), csvFormat)) {
            for (CSVRecord record : csvParser) {
                Policy policy = new Policy();

                // Chdrnum and Cownnum are mandatory
                if(record.get(0).trim().isBlank() || record.get(1).trim().isBlank()){
                    System.out.println("warning: mandatory field is empty, skipping");
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

                System.out.println(policy);

                policiesToStore.add(policy);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}