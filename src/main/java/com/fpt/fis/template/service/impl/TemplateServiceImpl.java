package com.fpt.fis.template.service.impl;

import com.fpt.fis.template.model.request.TemplateRequest;
import com.fpt.fis.template.model.response.TemplateResponsePage;
import com.fpt.fis.template.model.response.TemplateResponse;
import com.fpt.fis.template.repository.TemplateRepository;
import com.fpt.fis.template.repository.entity.Template;
import com.fpt.fis.template.service.TemplateService;
import com.fpt.framework.data.exception.DataIsNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
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
                .error(new DataIsNotFoundException("Template", String.format("Not found template with id: %s", id))));
    }

    @Override
    public Mono<TemplateResponsePage> readAllTemplates(String query, Pageable pageable) {
        return templateRepository.findAllByNameContainingOrDescriptionContaining(query, query, pageable).map(this::mapTemplateToTemplateResponse).collectList().zipWith(templateRepository.countAllByNameContainingOrDescriptionContaining(query, query)).map(p-> new TemplateResponsePage(p.getT1(), p.getT2()));
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
    public Mono<Void> deleteTemplateById(String id) {
        return templateRepository.findById(id).switchIfEmpty(Mono.error(
                new DataIsNotFoundException("Template", String.format("Not found template with id: %s", id)))).flatMap(t -> templateRepository.deleteTemplateById(t.getId()));
    }

    @Override
    public Flux<String> readAllParameters(String id) {
        return templateRepository.findById(id).switchIfEmpty(Mono.error(
                new DataIsNotFoundException("Template", String.format("Not found template with id: %s", id)))).map(Template::getParamerters).flatMapMany(Flux::fromIterable);
    }

    private TemplateResponse mapTemplateToTemplateResponse(Template template) {
        TemplateResponse templateResponse = new TemplateResponse();
        templateResponse.setTemplateId(template.getId());
        templateResponse.setName(template.getName());
        templateResponse.setDescription(template.getDescription());
        templateResponse.setContent(template.getContent());
        templateResponse.setType(template.getType());
        templateResponse.setEngine(template.getEngine());
        return templateResponse;
    }

    private Template mapTemplateRequestToTemplate(TemplateRequest request,Template oldTemplate) {
        Template template = Objects.requireNonNullElseGet(oldTemplate, Template::new);
        template.setName(request.getName());
        template.setDescription(request.getDescription());
        template.setContent(request.getContent());
        template.setType(request.getType());
        template.setEngine(request.getEngine());
        template.setParamerters(findParamerters(template.getContent()));
        return template;
    }

    private List<String> findParamerters(String content) {
        List<String> paramerters = new ArrayList<>();

        // Define the regex pattern to match strings starting with a $ character
        Pattern pattern = Pattern.compile("\\$\\S+");
        Matcher matcher = pattern.matcher(content);

        // Find all matches in the content
        while (matcher.find()) {
            paramerters.add(matcher.group());
        }

        return paramerters;
    }
}
