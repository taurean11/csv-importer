package com.acme.importer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.acme.importer.entity.OutPayHeader;
import com.acme.importer.repository.OutPayHeaderRepository;

@Component
public class OutPayHeaderService {

    @Autowired
    private OutPayHeaderRepository outPayHeaderRepository;

    public List<OutPayHeader> getAllOutPayHeaders() {
        return (List<OutPayHeader>) outPayHeaderRepository.findAll();
    }

    public OutPayHeader saveOutPayHeader(OutPayHeader outPayHeader) {
        return outPayHeaderRepository.save(outPayHeader);
    }

    public void saveOutPayHeaders(List<OutPayHeader> outPayHeaders) {
        outPayHeaderRepository.saveAll(outPayHeaders);
    }
}
