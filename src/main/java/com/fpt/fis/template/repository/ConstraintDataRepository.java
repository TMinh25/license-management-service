package com.fpt.fis.template.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.fpt.fis.template.repository.entity.ConstraintData;

@Repository
public interface ConstraintDataRepository extends ReactiveMongoRepository<ConstraintData, String> {}
