package com.fpt.fis.template.service;

import com.fpt.fis.template.model.request.TemplateRequest;
import com.fpt.fis.template.model.response.TemplateListFilterResponse;
import com.fpt.fis.template.model.response.TemplateResponse;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TemplateService {
    Mono<TemplateResponse> readTemplateById(String id);

    Mono<TemplateResponse> createTemplate(TemplateRequest request);

    Mono<TemplateResponse> updateTemplate(String id, TemplateRequest request);

    Mono<TemplateListFilterResponse> readAllTemplates(String searchText, Pageable pageable);

    Mono<Void> deleteTemplate(String id);

    Mono<List<String>> getParamertersById(String id);
}
