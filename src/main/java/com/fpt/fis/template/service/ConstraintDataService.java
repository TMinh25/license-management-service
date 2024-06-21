package com.fpt.fis.template.service;


import com.fpt.fis.template.model.request.ConstraintDataRequest;
import com.fpt.fis.template.model.response.ConstraintDataResponse;

import reactor.core.publisher.Flux;

public interface ConstraintDataService {

    Flux<ConstraintDataResponse> createConstraintData(ConstraintDataRequest request);

    Flux<ConstraintDataResponse> updateConstraintData(ConstraintDataRequest request);

}
