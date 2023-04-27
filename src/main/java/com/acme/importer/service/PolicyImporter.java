package com.acme.importer.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.acme.importer.entity.Policy;
import com.acme.importer.exception.CsvImporterException;

/**
 * Class responsible for reading and parsing the contents of the received input
 * file
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
     * @throws CsvImporterException if there's an IOException related to the input
     *                              file, or mandatory data is missing from it
     */
    public List<Policy> doImport(String fileToImport) throws CsvImporterException {

        Path path = Paths.get(fileToImport);

        logger.info("start processing input file {}", fileToImport);

        List<String> inputLines = new ArrayList<>();
        try {
            inputLines = Files.readAllLines(path);
        } catch (IOException e) {
            throw new CsvImporterException(String.format("could not process import file %s", fileToImport), e);
        }

        String line;

        for (int i = 0; i < inputLines.size(); i++) {

            line = inputLines.get(i);

            String[] policyLineSplitted = line.split("\\|");

            // skip empty (whitespace-only) lines
            if (line.trim().isEmpty()) {
                logger.warn("skipping empty line in input {} line number {}", fileToImport, i + 1);
                continue;
            }

            Policy policy = new Policy();

            // validate chdrnum
            if (policyLineSplitted.length < 1) {
                logger.error("could not parse chdrnum in input file {} line number {} : {}",
                        fileToImport, i + 1, line);
                throw new CsvImporterException(
                        String.format(
                                "could not process input file %s (type=policy) no data for chdrnum in line=%s",
                                fileToImport, line));

            }
            String rawChdrnum = policyLineSplitted[0];
            String normalizedChdrnum = rawChdrnum.trim();
            if (normalizedChdrnum.length() == 0) {
                logger.error("could not parse chdrnum in input file {} line number {} : {}",
                        fileToImport, i + 1, line);
                throw new CsvImporterException(
                        String.format(
                                "could not process input file %s (type=policy) no data for chdrnum in line=%s",
                                fileToImport, line));
            } else if (normalizedChdrnum.length() > 8) {
                logger.error("could not parse chdrnum in input file {} line number {} : {}",
                        fileToImport, i + 1, line);
                throw new CsvImporterException(
                        String.format(
                                "could not process input file %s (type=policy) chdrnum %s is longer than 8 chars in line=%s",
                                fileToImport, normalizedChdrnum, line));
            } else {
                policy.setChdrnum(normalizedChdrnum);
            }

            // validate cownnum
            if (policyLineSplitted.length < 2) {
                logger.error("could not parse cownnum in input file {} line number {} : {}",
                        fileToImport, i + 1, line);
                throw new CsvImporterException(
                        String.format(
                                "could not process input file %s (type=policy) no data for cownnum in line=%s",
                                fileToImport, line));

            }
            String rawCownnum = policyLineSplitted[1];
            String normalizedCownnum = rawCownnum.trim();
            if (normalizedCownnum.length() == 0) {
                logger.error("could not parse cownnum in input file {} line number {} : {}",
                        fileToImport, i + 1, line);
                throw new CsvImporterException(
                        String.format(
                                "could not process input file %s (type=policy) no data for cownnum in line=%s",
                                fileToImport, line));
            } else if (normalizedCownnum.length() > 8) {
                logger.error("could not parse cownnum in input file {} line number {} : {}",
                        fileToImport, i + 1, line);
                throw new CsvImporterException(
                        String.format(
                                "could not process input file %s (type=policy) cownnum %s is longer than 8 chars in line=%s",
                                fileToImport, normalizedCownnum, line));
            } else {
                policy.setCownnum(normalizedCownnum);
            }

            // from now on, the fields are optional
            if (policyLineSplitted.length >= 3) {
                String ownerName = policyLineSplitted[2].trim();
                if (ownerName.length() > 50) {
                    logger.warn(
                            "optional field ownerName {} was longer that the maximum 50 chars in input file {} line number {} : {}",
                            ownerName, fileToImport, i + 1, line);
                    ownerName = ownerName.substring(0, 50);
                }
                policy.setOwnerName(ownerName);
            }

            if (policyLineSplitted.length >= 4) {
                String lifcNum = policyLineSplitted[3].trim();
                if (lifcNum.length() > 8) {
                    logger.warn(
                            "optional field lifcNum {} was longer that the maximum 8 chars in input file {} line number {} : {}",
                            lifcNum, fileToImport, i + 1, line);
                    lifcNum = lifcNum.substring(0, 8);
                }
                policy.setLifcNum(lifcNum);
            }

            if (policyLineSplitted.length >= 5) {
                String lifcName = policyLineSplitted[4].trim();
                if (lifcName.length() > 50) {
                    logger.warn(
                            "optional field lifcName {} was longer that the maximum 50 chars in input file {} line number {} : {}",
                            lifcName, fileToImport, i + 1, line);
                    lifcName = lifcName.substring(0, 50);
                }
                policy.setLifcName(lifcName);
            }

            if (policyLineSplitted.length >= 6) {
                String aracde = policyLineSplitted[5].trim();
                if (aracde.length() > 3) {
                    logger.warn(
                            "optional field aracde {} was longer that the maximum 3 chars in input file {} line number {} : {}",
                            aracde, fileToImport, i + 1, line);
                    aracde = aracde.substring(0, 3);
                }
                policy.setAracde(aracde);
            }

            if (policyLineSplitted.length >= 7) {
                String agntnum = policyLineSplitted[6].trim();
                if (agntnum.length() > 5) {
                    logger.warn(
                            "optional field agntnum {} was longer that the maximum 5 chars in input file {} line number {} : {}",
                            agntnum, fileToImport, i + 1, line);
                    agntnum = agntnum.substring(0, 5);
                }
                policy.setAgntnum(agntnum);
            }

            if (policyLineSplitted.length >= 8) {
                String mailAddress = policyLineSplitted[7].trim();
                if (mailAddress.length() > 50) {
                    logger.warn(
                            "optional field mailAddress {} was longer that the maximum 50 chars in input file {} line number {} : {}",
                            mailAddress, fileToImport, i + 1, line);
                    mailAddress = mailAddress.substring(0, 50);
                }
                policy.setMailAddress(mailAddress);
            }

            logger.debug("processed policy: {}", policy);

            policiesToStore.add(policy);
        }

        logger.info("finished processing input file {}", fileToImport);

        return policiesToStore;
    }
}
