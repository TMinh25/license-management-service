package com.fpt.fis.license_management.service;

import com.fpt.fis.license_management.model.LicenseModel;
import com.fpt.fis.license_management.repository.entity.License;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LicenseService {
    Flux<License> readAll();

    Mono<Page<License>> read(String query, Pageable pageable);

    Mono<LicenseModel> create(LicenseModel request);
}
