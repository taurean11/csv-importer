package com.acme.importer.repository;

import org.springframework.data.repository.CrudRepository;

import com.acme.importer.entity.Redemption;

public interface RedemptionRepository extends CrudRepository<Redemption, Integer> {
}
