package com.acme.importer.repository;

import org.springframework.data.repository.CrudRepository;

import com.acme.importer.entity.OutPayHeader;

public interface OutPayHeaderRepository extends CrudRepository<OutPayHeader, Integer> {
}
