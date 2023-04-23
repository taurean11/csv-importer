package com.acme.importer;

import com.acme.importer.service.PolicyImporter;

public class App {

    public static void main(String[] args) {

        PolicyImporter importer = new PolicyImporter("./CCimport.txt");
        importer.doImport();
    }
}
