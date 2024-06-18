package com.fpt.fis.template.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpt.fis.template.model.request.ConstraintDataRequest;
import com.fpt.fis.template.model.response.ConstraintDataResponse;
import com.fpt.fis.template.repository.ConstraintDataRepository;
import com.fpt.fis.template.repository.entity.ConstraintData;
import com.fpt.fis.template.service.ConstraintDataService;

import reactor.core.publisher.Flux;

@Service
public class ConstraintDataServiceImpl implements ConstraintDataService {

    @Autowired
    private ConstraintDataRepository constraintDataRepository;

    @Override
    public Flux<ConstraintDataResponse> createConstraintData(ConstraintDataRequest request) {
        return constraintDataRepository.insert(mapConstraintDataRequestToConstraintData(request)).map(this::mapConstraintDataToConstraintDataResponse);
    }

    @Override
    public Flux<ConstraintDataResponse> updateConstraintData(ConstraintDataRequest request) {
        return constraintDataRepository.deleteByUsageIdAndUsageType(request.getUsageId(), request.getUsageType()).thenMany(constraintDataRepository.insert(mapConstraintDataRequestToConstraintData(request)).map(this::mapConstraintDataToConstraintDataResponse));
    }

    private List<ConstraintData> mapConstraintDataRequestToConstraintData(ConstraintDataRequest request) {
        List<ConstraintData> result = new ArrayList<>();
        for (String resourceId : request.getResourceIds()) {
            if(StringUtils.isNotBlank(resourceId)) {
                ConstraintData newConstraintData = new ConstraintData();
                newConstraintData.setUsageId(request.getUsageId());
                newConstraintData.setResourceId(resourceId);
                newConstraintData.setUsageType(request.getUsageType());
                result.add(newConstraintData);
            }
        }
        return result;
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
