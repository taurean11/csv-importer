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
 * Class responsible for reading and parsing the contents of the received input file
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
     * @throws CsvImporterException if there's an IOException related to the input file
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

            Redemption redemption = new Redemption();

            // the line must contain enough characters to cover the mandatory fields
            if (line.length() < SURVALUE_END) {
                logger.warn("not enough characters in input file {} line number {} : {}, skipping line",
                        fileToImport, i, line);
                continue;
            }

            redemption.setCompany(line.substring(COMPANY_START, COMPANY_END));
            redemption.setChdrnum(line.substring(CHDRNUM_START, CHDRNUM_END));

            Double normalizedSurvalue = normalizeSurvalue(
                    line.substring(SURVALUE_START, SURVALUE_END));

            // survalue is mandatory, can not remain NULL in the database
            if (normalizedSurvalue.equals(Double.NaN)) {
                logger.warn("could not parse mandatory decimal field in input file {} line {} : {}, skipping line",
                        fileToImport, i, line);
                continue;
            } else {
                redemption.setSurvalue(
                        Double.valueOf(line.substring(SURVALUE_START, SURVALUE_END)));
            }

            // set optional field currency - it's always HUF
            redemption.setCurrency(currency);

            // set optional field validDate if it's provided in the input
            if(line.length() >= VALIDDATE_END){
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
