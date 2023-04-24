package com.acme.importer.entity;

/**
 * Enum that lists the possible types of input files
 */
public enum InputFileType {

    POLICY("policy"),
    OUTPAY_HEADER("outpay_header"),
    REDEMPTION("redemption");

    private final String value;

    InputFileType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
