package com.acme.importer.validator;

import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.acme.importer.entity.InputFileType;

@Component
public class CommandLineParamsValidator {

    private Logger logger = LoggerFactory.getLogger(CommandLineParamsValidator.class);

    public boolean isInputValid(String[] args) {

        if (args.length < 2) {
            logger.error("not enough command line arguments, 2 are needed - input file type and path");
            return false;
        }

        if (!isInputFileTypeValid(args[0])) {
            logger.error("received input file type {} is invalid", args[0]);
            return false;
        }

        if(!isInputFileLocationValid(args[1])){
            logger.error("received input file location {} does not exist", args[1]);
            return false;
        }

        return true;
    }

    private boolean isInputFileTypeValid(String inputFileType) {
        for (InputFileType type : InputFileType.values()) {
            if (type.getValue().equalsIgnoreCase(inputFileType)) {
                return true;
            }
        }
        return false;
    }

    private boolean isInputFileLocationValid(String inputFileLocation) {
        return Files.exists(Path.of(inputFileLocation));
    }
}
