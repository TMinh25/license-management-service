package com.fpt.fis.template.service.impl;

import com.fpt.fis.template.exception.TemplateIsNotFoundException;
import com.fpt.fis.template.exception.ValidationException;
import com.fpt.fis.template.model.request.TemplateRequest;
import com.fpt.fis.template.model.response.TemplateResponse;
import com.fpt.fis.template.model.response.TemplateResponsePage;
import com.fpt.fis.template.repository.ConstraintDataRepository;
import com.fpt.fis.template.repository.TemplateRepository;
import com.fpt.fis.template.repository.entity.Template;
import com.fpt.fis.template.repository.entity.enums.TemplateEngine;
import com.fpt.fis.template.repository.entity.enums.TemplateType;
import com.fpt.fis.template.service.TemplateService;
import com.fpt.fis.template.service.TemplateVariableExtractor;
import com.fpt.fis.template.service.TemplateVariableExtractorFactory;
import com.fpt.fis.template.utils.Constants;
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
    private ConstraintDataRepository constraintDataRepository;

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
        return templateRepository.existsByName(request.getName()).flatMap(exists -> {
            if (exists) {
                return Mono.error(new ValidationException(Constants.ErrorType.DUPLICATED_NAME.getCode(), Constants.ErrorType.DUPLICATED_NAME.getMessage()));
            } else {
                return templateRepository.insert(mapTemplateRequestToTemplate(request, null)).map(this::mapTemplateToTemplateResponse);
            }
        });
    }

    @Override
    public Mono<TemplateResponse> updateTemplate(String id, TemplateRequest request) {
        return constraintDataRepository.existsByResourceId(id).flatMap(constraintDataExists -> {
            if (constraintDataExists) {
                return Mono.error(new ValidationException(Constants.ErrorType.EDIT_USED.getCode(), Constants.ErrorType.EDIT_USED.getMessage()));
            } else {
                return templateRepository.findById(id).switchIfEmpty(Mono.error(new TemplateIsNotFoundException(id))).flatMap(t -> {
                    return templateRepository.existsByName(request.getName()).flatMap(templateExists -> {
                        if (templateExists) {
                            return Mono.error(new ValidationException(Constants.ErrorType.DUPLICATED_NAME.getCode(), Constants.ErrorType.DUPLICATED_NAME.getMessage()));
                        } else {
                            return templateRepository.save(mapTemplateRequestToTemplate(request, t)).map(this::mapTemplateToTemplateResponse);
                        }
                    });
                });
            }
        });
        
    }

    @Override
    public Mono<Void> deleteTemplateById(String id) {
        return constraintDataRepository.existsByResourceId(id).flatMap(exists -> {
            if (exists) {
                return Mono.error(new ValidationException(Constants.ErrorType.DELETE_USED.getCode(), Constants.ErrorType.DELETE_USED.getMessage()));
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
