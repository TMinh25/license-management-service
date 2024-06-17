package com.fpt.fis.template.service;


import com.fpt.fis.template.model.request.ConstraintDataRequest;
import com.fpt.fis.template.model.response.ConstraintDataResponse;

import reactor.core.publisher.Mono;

public interface ConstraintDataService {

    Mono<ConstraintDataResponse> createConstraintData(ConstraintDataRequest request);

    Mono<ConstraintDataResponse> updateConstraintData(String id, ConstraintDataRequest request);

}
