package com.fpt.fis.template.service.impl;

import com.fpt.fis.template.exception.TemplateIsDuplicatedName;
import com.fpt.fis.template.exception.TemplateIsInUsedException;
import com.fpt.fis.template.exception.TemplateIsNotFoundException;
import com.fpt.fis.template.model.request.TemplateRequest;
import com.fpt.fis.template.model.response.TemplateResponse;
import com.fpt.fis.template.model.response.TemplateResponsePage;
import com.fpt.fis.template.repository.TemplateRepository;
import com.fpt.fis.template.repository.entity.Template;
import com.fpt.fis.template.repository.entity.enums.TemplateEngine;
import com.fpt.fis.template.repository.entity.enums.TemplateType;
import com.fpt.fis.template.service.TemplateService;
import com.fpt.fis.template.service.TemplateVariableExtractor;
import com.fpt.fis.template.service.TemplateVariableExtractorFactory;
import com.fpt.framework.data.constraint.service.DataConstraintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Service
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private DataConstraintService dataConstraintService;

    @Override
    public Mono<TemplateResponse> readTemplateById(String id) {
        return templateRepository.findById(id).map(this::mapTemplateToTemplateResponse).switchIfEmpty(Mono
                .error(new TemplateIsNotFoundException(id)));
    }

    @Override
    public Mono<TemplateResponsePage> readAllTemplates(String nameOrDescription, TemplateType type, Pageable pageable) {
        Flux<Template> findData = templateRepository.findByTypeAndNameOrDescription(type, nameOrDescription, pageable);
        Mono<Long> totalCount = templateRepository.countByAndNameOrDescription(type, nameOrDescription);

        return findData.map(this::mapTemplateToTemplateResponse).collectList()
                .zipWith(totalCount, (content, total) ->
                        new TemplateResponsePage(content, total));
    }

    @Override
    public Mono<TemplateResponse> createTemplate(TemplateRequest request) {
        String name = request.getName();
        return templateRepository.existsByName(name).flatMap(exists -> {
            if (exists) {
                return Mono.error(new TemplateIsDuplicatedName(name));
            } else {
                return templateRepository.insert(mapTemplateRequestToTemplate(request, null)).map(this::mapTemplateToTemplateResponse);
            }
        });
    }

    @Override
    public Mono<TemplateResponse> updateTemplate(String id, TemplateRequest request) {
        return dataConstraintService.hasConstraint(id).flatMap(exists -> {
            if (exists) {
                return Mono.error(new TemplateIsInUsedException("Cannot edit this print template because it's being used"));
            } else {        
                String name = request.getName();
                return templateRepository.findByName(name).filter(template -> !id.equals(template.getId()))
                .map(template -> false).defaultIfEmpty(true).flatMap(valid -> {
                    if (valid) {
                        return templateRepository.findById(id)
                                .switchIfEmpty(Mono.error(new TemplateIsNotFoundException(id)))
                                .flatMap(t -> templateRepository.save(mapTemplateRequestToTemplate(request, t))
                                        .map(this::mapTemplateToTemplateResponse));
                    } else {
                        return Mono.error(new TemplateIsDuplicatedName(name));
                    }
                });
            }
        });
    }

    @Override
    public Mono<Void> deleteTemplateById(String id) {
        return dataConstraintService.hasConstraint(id).flatMap(exists -> {
            if (exists) {
                return Mono.error(new TemplateIsInUsedException("Cannot delete this print template because it's being used"));
            } else {
                return templateRepository.findById(id).switchIfEmpty(Mono.error(
                    new TemplateIsNotFoundException(id))).flatMap(t -> templateRepository.deleteById(id));            }
        });
    }

    @Override
    public Mono<List<String>> readAllParameters(String id) {
        return templateRepository.findById(id).switchIfEmpty(Mono.error(new TemplateIsNotFoundException(id)))
                .map(template -> template.getParameters());

    }

    @Override
    public Mono<String> readTemplateContentById(String id) {
        return templateRepository.findById(id).switchIfEmpty(Mono.error(
                new TemplateIsNotFoundException(id))).map(t ->
                t.getContent());
    }

    private TemplateResponse mapTemplateToTemplateResponse(Template template) {
        TemplateResponse templateResponse = new TemplateResponse();
        templateResponse.setId(template.getId());
        templateResponse.setName(template.getName());
        templateResponse.setDescription(template.getDescription());
        templateResponse.setContent(template.getContent());
        templateResponse.setType(template.getType());
        templateResponse.setEngine(template.getEngine());
        return templateResponse;
    }

    private Template mapTemplateRequestToTemplate(TemplateRequest request, Template oldTemplate) {
        Template template = Objects.requireNonNullElseGet(oldTemplate, Template::new);
        template.setName(request.getName());
        template.setDescription(request.getDescription());
        template.setContent(request.getContent());
        template.setType(request.getType());
        template.setEngine(request.getEngine());
        template.setParameters(findParameters(template.getContent(), template.getEngine()));
        return template;
    }

    private List<String> findParameters(String content, TemplateEngine engine) {
        TemplateVariableExtractor templateVariableExtractor = TemplateVariableExtractorFactory.factoryExtractor(engine);
        return templateVariableExtractor.extractVariables(content);
    }
}
