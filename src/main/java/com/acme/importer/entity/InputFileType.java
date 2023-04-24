package com.acme.importer.entity;

public enum InputFileType {
    
    POLICY("policy"), OUTPAY_HEADER("outpay_header");

    private final String value;

    InputFileType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
