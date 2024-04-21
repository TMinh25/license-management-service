package com.fpt.fis.template.service;

import com.fpt.fis.template.model.response.TemplateResponse;
import reactor.core.publisher.Mono;

public interface SequenceGeneratorService {
    Mono<Long> generateSequence(String seqName);
}
