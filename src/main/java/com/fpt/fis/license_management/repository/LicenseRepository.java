package com.fpt.fis.license_management.repository;

import com.fpt.fis.license_management.repository.entity.License;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface LicenseRepository extends ReactiveMongoRepository<License, String>, LicenseDataRepository {
    @Query("{ 'companyCode': ?0 }")
    public Flux<License> findByCompanyCode(String companyCode);
}
