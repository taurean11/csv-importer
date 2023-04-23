package com.acme.importer.exception;

public class CsvImporterException extends Exception {

    public CsvImporterException(String message){
        super(message);
    }

    public CsvImporterException(String message, Throwable cause) {
        super(message, cause);
    }
}
