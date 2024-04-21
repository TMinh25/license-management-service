package com.fpt.fis.template.service;

import com.fpt.fis.template.model.request.TemplateRequest;
import com.fpt.fis.template.model.response.TemplateListFilterResponse;
import com.fpt.fis.template.model.response.TemplateResponse;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TemplateService {
    Mono<TemplateResponse> readById(Long templateId);

    Mono<TemplateResponse> create(TemplateRequest request);

    Mono<TemplateResponse> update(Long templateId, TemplateRequest request);

    Mono<TemplateListFilterResponse> read(Pageable pageable, String fql);

    Mono<Void> delete(Long templateId);

    Mono<List<String>> getParamertersById(Long templateId);
}
