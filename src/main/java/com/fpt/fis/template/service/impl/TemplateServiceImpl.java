package com.fpt.fis.template.service.impl;

import com.fpt.fis.template.model.enums.TemplateEngine;
import com.fpt.fis.template.model.enums.TemplateType;
import com.fpt.fis.template.model.request.TemplateRequest;
import com.fpt.fis.template.model.response.TemplateListFilterResponse;
import com.fpt.fis.template.model.response.TemplateResponse;
import com.fpt.fis.template.repository.TemplateRepository;
import com.fpt.fis.template.repository.entity.Template;
import com.fpt.fis.template.service.TemplateService;
import com.fpt.framework.data.exception.DataIsNotFoundException;
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
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    private TemplateRepository templateRepository;

    @Override
    public Mono<TemplateResponse> readTemplateById(String id) {
        return templateRepository.findById(id).map(this::mapTemplateToTemplateResponse).switchIfEmpty(Mono
                .error(new DataIsNotFoundException("The request does not exist or you do not have permission to view")));
    }

    @Override
    public Mono<TemplateListFilterResponse> readAllTemplates(String searchText, Pageable pageable) {
        return templateRepository.findTemplatesWithFilter(searchText, pageable);
    }

    @Override
    public Mono<TemplateResponse> createTemplate(TemplateRequest request) {
        return templateRepository.insert(mapTemplateRequestToTemplate(request, null)).map(this::mapTemplateToTemplateResponse);
    }

    @Override
    public Mono<TemplateResponse> updateTemplate(String id, TemplateRequest request) {
        return templateRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new DataIsNotFoundException("Template", String.format("Not found template with id: %s", id))))
                .flatMap(t -> templateRepository.save(mapTemplateRequestToTemplate(request, t))).map(this::mapTemplateToTemplateResponse);
    }

    @Override
    public Mono<Void> deleteTemplate(String id) {
        return templateRepository.findById(id).switchIfEmpty(Mono.error(
                new DataIsNotFoundException("Template", String.format("Not found template with id: %s", id)))).flatMap(t -> templateRepository.deleteTemplateById(t.getId()));
    }

    @Override
    public Mono<List<String>> getParamertersById(String id) {
        return templateRepository.findById(id).switchIfEmpty(Mono.error(
                new DataIsNotFoundException("Template", String.format("Not found template with id: %s", id)))).map((Template::getParamerters));
    }

    private TemplateResponse mapTemplateToTemplateResponse(Template template) {
        TemplateResponse templateDTO = new TemplateResponse();
        templateDTO.setTemplateId(template.getId());
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

    private Template mapTemplateRequestToTemplate(TemplateRequest request,Template oldTemplate) {
        Template template = Objects.requireNonNullElseGet(oldTemplate, Template::new);
        template.setTemplateId(template.getId());
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
