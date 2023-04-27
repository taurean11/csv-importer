package com.acme.importer.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.acme.importer.entity.OutPayHeader;
import com.acme.importer.exception.CsvImporterException;

/**
 * Class responsible for reading and parsing the contents of the received input
 * file
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
     * @throws CsvImporterException if there's an IOException related to the input
     *                              file, or mandatory data is missing from it
     */
    public List<OutPayHeader> doImport(String fileToImport) throws CsvImporterException {

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

            String[] outPayHeaderLineSplitted = line.split(";");

            // skip empty (whitespace-only) lines
            if (line.trim().isEmpty()) {
                logger.warn("skipping empty line in input {} line number {}", fileToImport, i + 1);
                continue;
            }

            OutPayHeader outPayHeader = new OutPayHeader();

            // validate clntnum
            if (outPayHeaderLineSplitted.length < 1) {
                logger.error("could not parse clntnum in input file {} line number {} : {}",
                        fileToImport, i + 1, line);
                throw new CsvImporterException(
                        String.format(
                                "could not process input file %s (type=outpay_header) no data for clntnum in line=%s",
                                fileToImport, line));
            }
            String rawClntnum = outPayHeaderLineSplitted[0];
            String normalizedClntnum = rawClntnum.trim();
            if (normalizedClntnum.length() == 0) {
                logger.error("could not parse clntnum in input file {} line number {} : {}",
                        fileToImport, i + 1, line);
                throw new CsvImporterException(
                        String.format(
                                "could not process input file %s (type=outpay_header) no data for clntnum in line=%s",
                                fileToImport, line));
            } else if (normalizedClntnum.length() > 8) {
                logger.error("could not parse clntnum in input file {} line number {} : {}",
                        fileToImport, i + 1, line);
                throw new CsvImporterException(
                        String.format(
                                "could not process input file %s (type=outpay_header) clntnum %s is longer than 8 chars in line=%s",
                                fileToImport, normalizedClntnum, line));
            } else {
                outPayHeader.setClntnum(normalizedClntnum);
            }

            // validate chdrnum
            if (outPayHeaderLineSplitted.length < 2) {
                logger.error("could not parse chdrnum in input file {} line number {} : {}",
                        fileToImport, i + 1, line);
                throw new CsvImporterException(
                        String.format(
                                "could not process input file %s (type=outpay_header) no data for chdrnum in line=%s",
                                fileToImport, line));
            }
            String rawChdrnum = outPayHeaderLineSplitted[1];
            String normalizedChdrnum = rawChdrnum.trim();
            if (normalizedChdrnum.length() == 0) {
                logger.error("could not parse chdrnum in input file {} line number {} : {}",
                        fileToImport, i + 1, line);
                throw new CsvImporterException(
                        String.format(
                                "could not process input file %s (type=outpay_header) no data for chdrnum in line=%s",
                                fileToImport, line));
            } else if (normalizedChdrnum.length() > 8) {
                logger.error("could not parse chdrnum in input file {} line number {} : {}",
                        fileToImport, i + 1, line);
                throw new CsvImporterException(
                        String.format(
                                "could not process input file %s (type=outpay_header) chdrnum %s is longer than 8 chars in line=%s",
                                fileToImport, normalizedChdrnum, line));
            } else {
                outPayHeader.setChdrnum(normalizedChdrnum);
            }

            // validate letterType
            if (outPayHeaderLineSplitted.length < 3) {
                logger.error("could not parse letterType in input file {} line number {} : {}",
                        fileToImport, i + 1, line);
                throw new CsvImporterException(
                        String.format(
                                "could not process input file %s (type=outpay_header) no data for letterType in line=%s",
                                fileToImport, line));
            }
            String rawLetterType = outPayHeaderLineSplitted[2];
            String normalizedLetterType = rawLetterType.trim();
            if (normalizedLetterType.length() == 0) {
                logger.error("could not parse letterType in input file {} line number {} : {}",
                        fileToImport, i + 1, line);
                throw new CsvImporterException(
                        String.format(
                                "could not process input file %s (type=outpay_header) no data for letterType in line=%s",
                                fileToImport, line));
            } else if (normalizedLetterType.length() > 12) {
                logger.error("could not parse letterType in input file {} line number {} : {}",
                        fileToImport, i + 1, line);
                throw new CsvImporterException(
                        String.format(
                                "could not process input file %s (type=outpay_header) letterType %s is longer than 12 chars in line=%s",
                                fileToImport, normalizedLetterType, line));
            } else {
                outPayHeader.setClntnum(normalizedLetterType);
            }

            // validate printDate
            if (outPayHeaderLineSplitted.length < 4) {
                throw new CsvImporterException(
                        String.format(
                                "could not process input file %s (type=outpay_header) no data for printDate in line=%s",
                                fileToImport, line));
            }
            String normalizedPrintDate = normalizeDate(outPayHeaderLineSplitted[3].trim(), fileToImport, line);
            if (normalizedPrintDate.isEmpty()) {
                logger.error("could not parse printDate in input file {} line number {} : {}",
                        fileToImport, i + 1, line);
                throw new CsvImporterException(
                        String.format(
                                "could not process input file %s (type=outpay_header) printDate %s cannot be parsed to date in line=%s",
                                fileToImport, normalizedPrintDate, line));
            } else {
                outPayHeader.setPrintDate(Date.valueOf(normalizedPrintDate));
            }

            // from now on, the fields are optional
            if (outPayHeaderLineSplitted.length >= 5) {
                String dataId = outPayHeaderLineSplitted[4].trim();
                if (dataId.length() > 6) {
                    logger.warn(
                            "optional field dataId {} was longer that the maximum 6 chars in input file {} line number {} : {}",
                            dataId, fileToImport, i + 1, line);
                    dataId = dataId.substring(0, 6);
                }
                outPayHeader.setDataID(dataId);
            }

            if (outPayHeaderLineSplitted.length >= 6) {
                String clntName = outPayHeaderLineSplitted[5].trim();
                if (clntName.length() > 80) {
                    logger.warn(
                            "optional field clntName {} was longer that the maximum 80 chars in input file {} line number {} : {}",
                            clntName, fileToImport, i + 1, line);
                    clntName = clntName.substring(0, 80);
                }
                outPayHeader.setClntName(clntName);
            }

            if (outPayHeaderLineSplitted.length >= 7) {
                String clntAddress = outPayHeaderLineSplitted[6].trim();
                if (clntAddress.length() > 80) {
                    logger.warn(
                            "optional field clntAddress {} was longer that the maximum 80 chars in input file {} line number {} : {}",
                            clntAddress, fileToImport, i + 1, line);
                    clntAddress = clntAddress.substring(0, 80);
                }
                outPayHeader.setClntAddress(clntAddress);
            }

            if (outPayHeaderLineSplitted.length >= 8) {
                String normalizedRegDate = normalizeDate(outPayHeaderLineSplitted[7].trim(), fileToImport, line);
                if (normalizedRegDate.isEmpty()) {
                    logger.warn("could not parse optional field regDate in input file {} line number {} : {}",
                            fileToImport, i + 1, line);
                } else {
                    outPayHeader.setRegDate(Date.valueOf(normalizedRegDate));
                }
            }

            if (outPayHeaderLineSplitted.length >= 9) {
                Double normalizedBenPercent = normalizePercent(outPayHeaderLineSplitted[8].trim(), fileToImport, line);
                if (normalizedBenPercent.equals(Double.NaN)) {
                    logger.warn("could not parse optional field benPercent in input file {} line number {} : {}",
                            fileToImport, i + 1, line);
                } else {
                    outPayHeader.setBenPercent(normalizedBenPercent);
                }
            }

            // not trimming here, spaces can be kept
            if (outPayHeaderLineSplitted.length >= 10) {
                String role1 = outPayHeaderLineSplitted[9];
                if (role1.length() > 2) {
                    logger.warn(
                            "optional field role1 {} was longer that the maximum 2 chars in input file {} line number {} : {}",
                            role1, fileToImport, i + 1, line);
                    role1 = role1.substring(0, 2);
                }
                outPayHeader.setRole1(role1);
            }

            // not trimming here, spaces can be kept
            if (outPayHeaderLineSplitted.length >= 11) {
                String role2 = outPayHeaderLineSplitted[10];
                if (role2.length() > 6) {
                    logger.warn(
                            "optional field role2 {} was longer that the maximum 2 chars in input file {} line number {} : {}",
                            role2, fileToImport, i + 1, line);
                    role2 = role2.substring(0, 2);
                }
                outPayHeader.setRole2(role2);
            }

            if (outPayHeaderLineSplitted.length >= 12) {
                String cownNum = outPayHeaderLineSplitted[11].trim();
                if (cownNum.length() > 8) {
                    logger.warn(
                            "optional field cownNum {} was longer that the maximum 8 chars in input file {} line number {} : {}",
                            cownNum, fileToImport, i + 1, line);
                    cownNum = cownNum.substring(0, 8);
                }
                outPayHeader.setCownNum(cownNum);
            }

            if (outPayHeaderLineSplitted.length >= 13) {
                String cownName = outPayHeaderLineSplitted[12].trim();
                if (cownName.length() > 80) {
                    logger.warn(
                            "optional field cownName {} was longer that the maximum 80 chars in input file {} line number {} : {}",
                            cownName, fileToImport, i + 1, line);
                    cownName = cownName.substring(0, 8);
                }
                outPayHeader.setCownName(cownName);
            }

            // leaving remaining fields - Notice01...06, Claim_ID, TP2ProcessDate - NULL

            logger.debug("processed outpay_header: {}", outPayHeader);

            outPayHeadersToStore.add(outPayHeader);
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
     * @param line         the line (string) being processed
     * @return formatted date string, or empty string if the formatting is not
     *         successful
     */
    private String normalizeDate(String originalDate, String fileToImport, String line) {

        SimpleDateFormat fromCsv = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat toDatabase = new SimpleDateFormat("yyyy-MM-dd");

        String result = "";

        try {
            result = toDatabase.format(fromCsv.parse(originalDate));
        } catch (ParseException e) {
            logger.warn("could not parse date ({}) in input file {} line ({})",
                    originalDate, fileToImport, line);
        }
        return result;
    }

    /**
     * Tries to create a Java double value from the received benPercent string
     *
     * @param originalPercent received benPercent, e.g. 100.00
     * @param fileToImport    import file path
     * @param line            the line (string) being processed
     * @return benPercent value as Java double, or Double.Nan if it cannot be parsed
     *         to double
     */
    private Double normalizePercent(String originalPercent, String fileToImport, String line) {

        Double result = Double.NaN;

        try {
            result = Double.valueOf(originalPercent);
        } catch (NumberFormatException e) {
            logger.warn("could not parse percent value ({}) in input file {} line ({})",
                    originalPercent, fileToImport, line);
        }
        return result;
    }
}
