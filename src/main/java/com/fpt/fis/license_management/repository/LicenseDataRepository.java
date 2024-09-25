package com.fpt.fis.license_management.repository;

import com.fpt.fis.license_management.repository.entity.License;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LicenseDataRepository {
    Flux<License> find(String queryString, Pageable pageable);

    Mono<Long> count(String queryString, Pageable pageable);

}
