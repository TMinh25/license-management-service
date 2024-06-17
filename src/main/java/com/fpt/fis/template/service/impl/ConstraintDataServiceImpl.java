package com.fpt.fis.template.service.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpt.fis.template.exception.ConstraintDataIsNotFoundException;
import com.fpt.fis.template.model.request.ConstraintDataRequest;
import com.fpt.fis.template.model.response.ConstraintDataResponse;
import com.fpt.fis.template.repository.ConstraintDataRepository;
import com.fpt.fis.template.repository.entity.ConstraintData;
import com.fpt.fis.template.service.ConstraintDataService;

import reactor.core.publisher.Mono;

@Service
public class ConstraintDataServiceImpl implements ConstraintDataService {

    @Autowired
    private ConstraintDataRepository constraintDataRepository;

    @Override
    public Mono<ConstraintDataResponse> createConstraintData(ConstraintDataRequest request) {
        return constraintDataRepository.insert(mapConstraintDataRequestToConstraintData(request, null)).map(this::mapConstraintDataToConstraintDataResponse);
    }

    @Override
    public Mono<ConstraintDataResponse> updateConstraintData (String id, ConstraintDataRequest request) {
        return constraintDataRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new ConstraintDataIsNotFoundException(id)))
                .flatMap(t -> constraintDataRepository.save(mapConstraintDataRequestToConstraintData(request, t))).map(this::mapConstraintDataToConstraintDataResponse);
    }

    private ConstraintData mapConstraintDataRequestToConstraintData(ConstraintDataRequest request, ConstraintData oldConstraintData) {
        ConstraintData constraintData = Objects.requireNonNullElseGet(oldConstraintData, ConstraintData::new);
        constraintData.setUsageId(request.getUsageId());
        constraintData.setResourceId(request.getResourceId());
        constraintData.setUsageType(request.getUsageType());
        return constraintData;
    }

    private ConstraintDataResponse mapConstraintDataToConstraintDataResponse(ConstraintData constraintData) {
        ConstraintDataResponse constraintDataResponse = new ConstraintDataResponse();
        constraintDataResponse.setId(constraintData.getId());
        constraintDataResponse.setUsageId(constraintData.getUsageId());
        constraintDataResponse.setResourceId(constraintData.getResourceId());
        constraintDataResponse.setUsageType(constraintData.getUsageType());
        return constraintDataResponse;
    }
}
