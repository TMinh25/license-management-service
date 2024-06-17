package com.fpt.fis.template.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpt.fis.template.model.request.ConstraintDataRequest;
import com.fpt.fis.template.model.response.ConstraintDataResponse;
import com.fpt.fis.template.service.ConstraintDataService;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("constraint-data")
public class ConstraintDataRestController {

    @Autowired
    private ConstraintDataService constraintDataService;

    @PostMapping("")
    @PreAuthorize("@author.hasPermission('template', 'create-constraint-data')")
    public Mono<ConstraintDataResponse> createConstraintData(@RequestBody @Valid ConstraintDataRequest request) {
        return constraintDataService.createConstraintData(request);
    }

    @PutMapping("{id}")
    @PreAuthorize("@author.hasPermission('template', 'update-constraint-data')")
    public Mono<ConstraintDataResponse> updateConstraintData(@Parameter @PathVariable("id") String id, @RequestBody @Valid ConstraintDataRequest request) {
        return constraintDataService.updateConstraintData(id, request);
    }
}
