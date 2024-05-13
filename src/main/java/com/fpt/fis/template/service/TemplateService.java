package com.fpt.fis.template.service;

import com.fpt.fis.template.model.request.TemplateRequest;
import com.fpt.fis.template.model.response.TemplateResponsePage;
import com.fpt.fis.template.model.response.TemplateResponse;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TemplateService {
    Mono<TemplateResponse> readTemplateById(String id);

    Mono<TemplateResponse> createTemplate(TemplateRequest request);

    Mono<TemplateResponse> updateTemplate(String id, TemplateRequest request);

    Mono<TemplateResponsePage> readAllTemplates(String query, Pageable pageable);

    Mono<Void> deleteTemplateById(String id);

    Flux<String> readAllParameters(String id);

    Mono<String> readTemplateContentById(String id);
}
