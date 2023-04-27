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

import com.acme.importer.entity.Redemption;
import com.acme.importer.exception.CsvImporterException;

/**
 * Class responsible for reading and parsing the contents of the received input
 * file
 * The file is supposed to be a redemption file
 */
@Component
public class RedemptionImporter {

    private Logger logger = LoggerFactory.getLogger(RedemptionImporter.class);

    private List<Redemption> redemptionsToStore;
    private String currency = "HUF";

    // input file structure
    private int COMPANY_START = 0;
    private int COMPANY_END = 1;
    private int CHDRNUM_START = 1;
    private int CHDRNUM_END = 9;
    private int SURVALUE_START = 9;
    private int SURVALUE_END = 24;
    private int VALIDDATE_START = 44;
    private int VALIDDATE_END = VALIDDATE_START + 10;

    public RedemptionImporter() {
        redemptionsToStore = new ArrayList<>();
    }

    /**
     * Tries to import the contents of the received input file
     * The file is supposed to be a redemption file
     *
     * @param fileToImport import file path
     * @return a List of redemptions parsed from the input
     * @throws CsvImporterException if there's an IOException related to the input
     *                              file, or mandatory data is missing from it
     */
    public List<Redemption> doImport(String fileToImport) throws CsvImporterException {

        Path path = Paths.get(fileToImport);

        List<String> inputLines = new ArrayList<>();
        try {
            inputLines = Files.readAllLines(path);
        } catch (IOException e) {
            throw new CsvImporterException(String.format("could not process import file %s", fileToImport), e);
        }

        String line;

        for (int i = 0; i < inputLines.size(); i++) {

            line = inputLines.get(i);

            // skip empty (whitespace-only) lines
            if (line.trim().isEmpty()) {
                logger.warn("skipping empty line in input {} line number {}", fileToImport, i + 1);
                continue;
            }

            Redemption redemption = new Redemption();

            // validate company
            if (line.length() < COMPANY_END) {
                logger.error("not enough characters in input file {} line number {} : {}",
                        fileToImport, i + 1, line);
                throw new CsvImporterException(
                        String.format(
                                "could not process input file %s (type=redemption) no data for company in line=%s",
                                fileToImport, line));
            }
            String rawCompany = line.substring(COMPANY_START, COMPANY_END);
            String normalizedCompany = rawCompany.trim();
            if (normalizedCompany.isEmpty()) {
                logger.error("mandatory field company is empty in input file {} line number {} : {}",
                        fileToImport, i + 1, line);
                throw new CsvImporterException(
                        String.format(
                                "could not process input file %s (type=redemption) no data for company in line=%s",
                                fileToImport, line));
            } else {
                redemption.setCompany(normalizedCompany);
            }

            // validate chdrnum
            if (line.length() < CHDRNUM_END) {
                logger.error("not enough characters in input file {} line number {} : {}",
                        fileToImport, i + 1, line);
                throw new CsvImporterException(
                        String.format(
                                "could not process input file %s (type=redemption) no data for chdrnum in line=%s",
                                fileToImport, line));
            }
            String rawChdrnum = line.substring(CHDRNUM_START, CHDRNUM_END);
            String normalizedChdrnum = rawChdrnum.trim();
            if (normalizedChdrnum.isEmpty()) {
                logger.error("mandatory field chdrnum is empty in input file {} line number {} : {}",
                        fileToImport, i + 1, line);
                throw new CsvImporterException(
                        String.format(
                                "could not process input file %s (type=redemption) no data for chdrnum in line=%s",
                                fileToImport, line));
            } else {
                redemption.setChdrnum(normalizedChdrnum);
            }

            // validate survalue
            if (line.length() < SURVALUE_END) {
                logger.error("not enough characters in input file {} line number {} : {}",
                        fileToImport, i + 1, line);
                throw new CsvImporterException(
                        String.format(
                                "could not process input file %s (type=redemption) no data for survalue in line=%s",
                                fileToImport, line));
            }
            String rawSurvalue = line.substring(SURVALUE_START, SURVALUE_END);
            Double normalizedSurvalue = normalizeSurvalue(rawSurvalue);
            if (normalizedSurvalue.equals(Double.NaN)) {
                logger.error(
                        "could not parse mandatory decimal field survalue {} in input file {} line {} : {}",
                        rawSurvalue, fileToImport, i + 1, line);
                throw new CsvImporterException(
                        String.format(
                                "could not process input file %s (type=redemption) could not parse survalue %s in line=%s",
                                fileToImport, rawSurvalue, line));
            } else {
                redemption.setSurvalue(
                        Double.valueOf(rawSurvalue));
            }

            // set optional field currency - it's always HUF
            redemption.setCurrency(currency);

            // set optional field validDate if it's provided in the input
            if (line.length() >= VALIDDATE_END) {
                redemption.setValidDate(line.substring(VALIDDATE_START, VALIDDATE_END));
            }

            logger.debug("processed redemption: {}", redemption);

            redemptionsToStore.add(redemption);
        }

        logger.info("finished processing input file {}", fileToImport);

        return redemptionsToStore;
    }

    /**
     * Tries to create a Java double value from the received survalue string
     *
     * @param originalSurvalue received survalue, e.g. 3276866.00
     * @return survalue as Java double, or Double.Nan if it cannot be parsed to
     *         double
     */
    private Double normalizeSurvalue(String originalSurvalue) {

        Double result = Double.NaN;

        try {
            result = Double.valueOf(originalSurvalue);
        } catch (NumberFormatException e) {
            logger.warn("could not parse sur value {}",
                    originalSurvalue);
        }
        return result;
    }
}
