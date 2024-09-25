package com.fpt.fis.license_management.controller.rest;

import com.fpt.fis.license_management.model.LicenseModel;
import com.fpt.fis.license_management.repository.entity.License;
import com.fpt.fis.license_management.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("licenses")
public class LicenseRestController {

    @Autowired
    private LicenseService licenseService;

    @GetMapping()
    public Mono<Page<License>> read(@RequestParam(required = false, defaultValue = "") String query,
                                    @RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        return licenseService.read(query, PageRequest.of(page - 1, size));
    }

    @PostMapping()
    public Mono<LicenseModel> create(@RequestBody LicenseModel license) {
        return licenseService.create(license);
    }
}
