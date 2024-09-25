package com.fpt.fis.license_management.repository.impl;

import com.fpt.fis.license_management.repository.LicenseDataRepository;
import com.fpt.fis.license_management.repository.entity.License;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class LicenseDataRepositoryImpl implements LicenseDataRepository {
    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    private Query buildQuery(String queryString) {
        Query query = new Query();
        query.addCriteria(
                new Criteria().andOperator(
                        Criteria.where("active").is(true),
                        new Criteria().orOperator(
                                Criteria.where("companyCode").regex(queryString, "i"),
                                Criteria.where("companyName").regex(queryString, "i"),
                                Criteria.where("currentLicense").regex(queryString, "i")
                        )
                ));
        return query;
    }

    @Override
    public Flux<License> find(String queryString, Pageable pageable) {
        Query query = buildQuery(queryString)
                .with(pageable)
                .with(Sort.by(Sort.Direction.DESC, "createdAt"));
        return reactiveMongoTemplate.find(query, License.class);
    }

    @Override
    public Mono<Long> count(String queryString, Pageable pageable) {
        Query query = buildQuery(queryString).with(pageable);
        return reactiveMongoTemplate.count(query, License.class);
    }
}
