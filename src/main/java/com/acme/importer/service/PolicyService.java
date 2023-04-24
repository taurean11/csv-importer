package com.acme.importer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.acme.importer.entity.Policy;
import com.acme.importer.repository.PolicyRepository;

@Component
public class PolicyService {

    @Autowired
    private PolicyRepository policyRepository ;

    public List<Policy> getAllPolicies() {
        return (List<Policy>) policyRepository.findAll();
    }

    public void savePolicy(Policy policy) {
        policyRepository.save(policy);
    }

    public void savePolicies(List<Policy> policies) {
        policyRepository.saveAll(policies);
    }
}
