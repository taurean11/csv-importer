package com.acme.importer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.acme.importer.entity.Redemption;
import com.acme.importer.repository.RedemptionRepository;

@Component
public class RedemptionService {

    @Autowired
    private RedemptionRepository redemptionRepository ;

    public List<Redemption> getAllRedemptions() {
        return (List<Redemption>) redemptionRepository.findAll();
    }

    public void saveRedemption(Redemption redemption) {
        redemptionRepository.save(redemption);
    }

    public void saveRedemptions(List<Redemption> redemptions) {
        redemptionRepository.saveAll(redemptions);
    }
}
