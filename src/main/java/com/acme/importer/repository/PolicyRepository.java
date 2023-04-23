package com.acme.importer.repository;

import org.springframework.data.repository.CrudRepository;

import com.acme.importer.entity.Policy;

public interface PolicyRepository extends CrudRepository<Policy, Integer> {
}
