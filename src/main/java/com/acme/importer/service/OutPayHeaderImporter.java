package com.acme.importer.service;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.acme.importer.entity.OutPayHeader;
import com.acme.importer.exception.CsvImporterException;

/**
 * Class responsible for reading and parsing the contents of the received input file
 * The file is supposed to be an outpay header file
 */
@Component
public class OutPayHeaderImporter {

    private Logger logger = LoggerFactory.getLogger(OutPayHeaderImporter.class);

    private List<OutPayHeader> outPayHeadersToStore;

    public OutPayHeaderImporter() {
        outPayHeadersToStore = new ArrayList<>();
    }

    /**
     * Tries to import the contents of the received input file
     * The file is supposed to be an outpay header file
     *
     * @param fileToImport import file path
     * @return a List of policies parsed from the input
     * @throws CsvImporterException if there's an IOException related to the input file
     */
    public List<OutPayHeader> doImport(String fileToImport) throws CsvImporterException {

        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setDelimiter(";")
                .build();

        try (CSVParser csvParser = new CSVParser(new FileReader(fileToImport), csvFormat)) {

            logger.info("start processing input file {}", fileToImport);

            int line = 1;
            for (CSVRecord record : csvParser) {

                OutPayHeader outPayHeader = new OutPayHeader();

                // Clntnum, Chdrnum, LetterType and PrintDate are mandatory
                if (record.size() < 4 || record.get(0).trim().isBlank() || record.get(1).trim().isBlank()
                        || record.get(2).trim().isBlank() || record.get(3).trim().isBlank()) {
                    logger.warn("mandatory field is empty in input file {} line {} : {}, skipping line",
                            fileToImport, line, record);
                    continue;
                }

                outPayHeader.setClntnum(record.get(0));
                outPayHeader.setChdrnum(record.get(1));
                outPayHeader.setLetterType(record.get(2));

                // printDate is mandatory, can not remain NULL in the database
                String printDate = normalizeDate(record.get(3), fileToImport, record);
                if (printDate.isEmpty()) {
                    logger.warn("could not parse mandatory date field in input file {} line {} : {}, skipping line",
                            fileToImport, line, record);
                    continue;
                }

                outPayHeader.setPrintDate(Date.valueOf(printDate));
                outPayHeader.setDataID(record.get(4));
                outPayHeader.setClntName(record.get(5));
                outPayHeader.setClntAddress(record.get(6));

                // regDate is not mandatory, can remain NULL in the database
                String regDate = normalizeDate(record.get(7), fileToImport, record);
                if (!regDate.isEmpty()) {
                    outPayHeader.setRegDate(Date.valueOf(record.get(7)));
                }

                // benPerct is not mandatory, can remain NULL in the database
                Double benPercent = normalizePercent(record.get(8), fileToImport, record);
                if (!benPercent.equals(Double.NaN)) {
                    outPayHeader.setBenPercent(benPercent);
                }

                outPayHeader.setRole1(record.get(9));
                outPayHeader.setRole2(record.get(10));
                outPayHeader.setCownNum(record.get(11));
                outPayHeader.setCownName(record.get(12));

                // leaving remaining fields NULL

                logger.debug("processed outpay_header: {}", outPayHeader);

                line++;
                outPayHeadersToStore.add(outPayHeader);
            }
        } catch (IOException e) {
            throw new CsvImporterException(String.format("could not process import file %s", fileToImport), e);
        }

        logger.info("finished processing input file {}", fileToImport);

        return outPayHeadersToStore;
    }

    /**
     * Tries to create the expected date format yyyy-MM-dd from the received date
     * string
     * The expected format is needed to save the date as java.sql.date
     *
     * @param originalDate received date, e.g. 20201010
     * @param fileToImport import file path
     * @param record       record that contains the benPercent
     * @return formatted date string, or empty string if the formatting is not
     *         successful
     */
    private String normalizeDate(String originalDate, String fileToImport, CSVRecord record) {

        SimpleDateFormat fromCsv = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat toDatabase = new SimpleDateFormat("yyyy-MM-dd");

        String result = "";

        try {
            result = toDatabase.format(fromCsv.parse(originalDate));
        } catch (ParseException e) {
            logger.warn("could not parse date {} in input file {} line {}",
                    originalDate, fileToImport, record);
        }
        return result;
    }

    /**
     * Tries to create a Java double value from the received benPercent string
     *
     * @param originalPercent received benPercent, e.g. 100.00
     * @param fileToImport    import file path
     * @param record          record that contains the benPercent
     * @return benPercent value as Java double, or Double.Nan if it cannot be parsed
     *         to double
     */
    private Double normalizePercent(String originalPercent, String fileToImport, CSVRecord record) {

        Double result = Double.NaN;

        try {
            result = Double.valueOf(originalPercent);
        } catch (NumberFormatException e) {
            logger.warn("could not parse percent value {} in input file {} line {}",
                    originalPercent, fileToImport, record);
        }
        return result;
    }
}
