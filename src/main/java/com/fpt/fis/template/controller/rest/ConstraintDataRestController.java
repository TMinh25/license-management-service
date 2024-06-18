package com.fpt.fis.template.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpt.fis.template.model.request.ConstraintDataRequest;
import com.fpt.fis.template.model.response.ConstraintDataResponse;
import com.fpt.fis.template.service.ConstraintDataService;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("constraint-data")
public class ConstraintDataRestController {

    @Autowired
    private ConstraintDataService constraintDataService;

    @PostMapping("")
    public Flux<ConstraintDataResponse> createConstraintData(@RequestBody @Valid ConstraintDataRequest request) {
        return constraintDataService.createConstraintData(request);
    }

    @PutMapping("")
    public Flux<ConstraintDataResponse> updateConstraintData(@RequestBody @Valid ConstraintDataRequest request) {
        return constraintDataService.updateConstraintData(request);
    }
}
