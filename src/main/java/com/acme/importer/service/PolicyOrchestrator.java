package com.acme.importer.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.acme.importer.entity.Policy;

@Component
public class PolicyOrchestrator {

    @Autowired
    private PolicyImporter policyImporter;

    @Autowired
    private PolicyService policyService;

    private Logger logger = LoggerFactory.getLogger(PolicyOrchestrator.class);

    public void handlePolicy(String fileToImport){

        logger.info("start importing file {}", fileToImport);

        List<Policy> policiesToStore = policyImporter.doImport(fileToImport);

        if(policiesToStore.size() == 0){
            logger.warn("nothing to import after processing input file {}", fileToImport);
            return;
        }

        logger.info("start saving records from file {}", fileToImport);
        
        policyService.savePolicies(policiesToStore);

        logger.info("finished importing and saving file {}", fileToImport);
    }
}
