package com.fpt.fis.template.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.fpt.fis.template.repository.entity.ConstraintData;
import com.fpt.fis.template.repository.entity.enums.UsageType;

import reactor.core.publisher.Mono;


@Repository
public interface ConstraintDataRepository extends ReactiveMongoRepository<ConstraintData, String> {

    Mono<Void> deleteByUsageIdAndUsageType(int usageId, UsageType usageType);

    Mono<Boolean> existsByResourceId(String resourceId);

}
