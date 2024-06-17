package com.fpt.fis.template.service;

import com.fpt.fis.template.model.request.TemplateRequest;
import com.fpt.fis.template.model.response.TemplateResponsePage;
import com.fpt.fis.template.model.response.TemplateResponse;
import com.fpt.fis.template.repository.entity.enums.TemplateType;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TemplateService {
    Mono<TemplateResponse> readTemplateById(String id);

    Mono<TemplateResponse> createTemplate(TemplateRequest request);

    Mono<TemplateResponse> updateTemplate(String id, TemplateRequest request);

    Mono<TemplateResponsePage> readAllTemplates(String nameOrDescription, TemplateType type, Pageable pageable);

    Mono<Void> deleteTemplateById(String id);

    Mono<List<String>> readAllParameters(String id);

    Mono<String> readTemplateContentById(String id);
}
