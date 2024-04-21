package com.fpt.fis.template.service.impl;

import com.fpt.fis.template.model.FilterDTO;
import com.fpt.fis.template.model.enums.TemplateEngine;
import com.fpt.fis.template.model.enums.TemplateType;
import com.fpt.fis.template.model.request.TemplateRequest;
import com.fpt.fis.template.model.response.TemplateListFilterResponse;
import com.fpt.fis.template.model.response.TemplateResponse;
import com.fpt.fis.template.repository.TemplateDataRepository;
import com.fpt.fis.template.repository.TemplateRepository;
import com.fpt.fis.template.repository.entity.Template;
import com.fpt.fis.template.repository.helper.FilterConditionHelper;
import com.fpt.fis.template.service.SequenceGeneratorService;
import com.fpt.fis.template.service.TemplateService;
import com.fpt.framework.data.exception.DataIsNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Log4j2
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @Override
    public Mono<TemplateResponse> readById(Long templateId) {
        return templateRepository.findByTemplateId(templateId).map(this::mapTemplateToTemplateResponse).switchIfEmpty(Mono
                .error(new DataIsNotFoundException("The request does not exist or you do not have permission to view")));
    }

    @Override
    public Mono<TemplateListFilterResponse> read(Pageable pageable, String fql) {
        log.info("Start readTemplatesByFilter with fql: {} \t page index: {} \t page size: {}", fql,
                pageable.getPageNumber(), pageable.getPageSize());
        Mono<FilterDTO> filterListPublisher = FilterConditionHelper.convertToMonoFilter(fql);
        return filterListPublisher.flatMap(listFilter -> templateRepository.findWithFilter(listFilter, pageable))
                .doOnSuccess(item -> {
                    log.info("Finished readTemplatesByFilter");
                });
    }

    @Override
    public Mono<TemplateResponse> create(TemplateRequest request) {
        return sequenceGeneratorService.generateSequence(Template.SEQUENCE_NAME)
                .flatMap(seqId -> templateRepository.insert(mapTemplateRequestToTemplate(request, seqId, null)).map(this::mapTemplateToTemplateResponse));
    }

    @Override
    public Mono<TemplateResponse> update(Long templateId, TemplateRequest request) {
        return templateRepository.findByTemplateId(templateId)
                .switchIfEmpty(Mono.error(
                        new DataIsNotFoundException("Template", String.format("Not found template with id: %s", templateId))))
                .flatMap(t -> templateRepository.save(mapTemplateRequestToTemplate(request, templateId, t))).map(this::mapTemplateToTemplateResponse);
    }

    @Override
    public Mono<Void> delete(Long templateId) {
        return templateRepository.findByTemplateId(templateId).switchIfEmpty(Mono.error(
                new DataIsNotFoundException("Template", String.format("Not found template with id: %s", templateId)))).flatMap(t -> templateRepository.deleteByTemplateId(t.getTemplateId()));
    }

    @Override
    public Mono<List<String>> getParamertersById(Long templateId) {
        return templateRepository.findByTemplateId(templateId).switchIfEmpty(Mono.error(
                new DataIsNotFoundException("Template", String.format("Not found template with id: %s", templateId)))).map((Template::getParamerters));
    }

    private TemplateResponse mapTemplateToTemplateResponse(Template template) {
        TemplateResponse templateDTO = new TemplateResponse();
        templateDTO.setTemplateId(template.getTemplateId());
        templateDTO.setName(template.getName());
        templateDTO.setDescription(template.getDescription());
        templateDTO.setContent(template.getContent());
        templateDTO.setType(template.getType());
        templateDTO.setEngine(template.getEngine());
        templateDTO.setCreatedTime(template.getCreatedTime());
        templateDTO.setUpdatedTime(template.getUpdatedTime());
        templateDTO.setCreatedBy(template.getCreatedBy());
        templateDTO.setUpdatedBy(template.getUpdatedBy());
        templateDTO.setParamerters(template.getParamerters());
        return templateDTO;
    }

    private Template mapTemplateRequestToTemplate(TemplateRequest request, Long templateId,Template oldTemplate) {
        Template template = Objects.requireNonNullElseGet(oldTemplate, Template::new);
        template.setTemplateId(templateId);
        template.setName(request.getName());
        template.setDescription(request.getDescription());
        template.setContent(request.getContent());
        template.setType(TemplateType.PRINT);
        template.setEngine(TemplateEngine.VELOCITY);
        template.setParamerters(findParamerters(request.getContent()));
        return template;
    }

    public List<String> findParamerters(String content) {
        List<String> paramerters = new ArrayList<>();

        // Define the regex pattern to match strings starting with a $ character
        Pattern pattern = Pattern.compile("\\$\\w+");
        Matcher matcher = pattern.matcher(content);

        // Find all matches in the content
        while (matcher.find()) {
            paramerters.add(matcher.group());
        }

        return paramerters;
    }
}
