package com.fpt.fis.license_management.service.impl;

import com.fpt.fis.license_management.model.LicenseModel;
import com.fpt.fis.license_management.repository.LicenseRepository;
import com.fpt.fis.license_management.repository.entity.License;
import com.fpt.fis.license_management.service.LicenseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class LicenseServiceImpl implements LicenseService {
    @Autowired
    private LicenseRepository licenseRepository;

    @Override
    public Flux<License> readAll() {
        return licenseRepository.findAll();
    }

    @Override
    public Mono<Page<License>> read(String query, Pageable pageable) {
        return licenseRepository.find(query, pageable).collectList()
                .zipWith(licenseRepository.count(query, pageable))
                .map(tuple -> new PageImpl<>(tuple.getT1(), pageable, tuple.getT2()));
    }

    @Override
    public Mono<LicenseModel> create(LicenseModel request) {
        return licenseRepository.findByCompanyCode(request.getCompanyCode())
                .flatMap(license -> {
                    license.setActive(false);
                    return licenseRepository.save(license);
                })
                .then(licenseRepository.insert(request.toEntity()))
                .map(LicenseModel::fromEntity);
    }
}
