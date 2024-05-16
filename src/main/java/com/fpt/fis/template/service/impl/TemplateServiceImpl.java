package com.fpt.fis.template.service.impl;

import com.fpt.fis.template.exception.TemplateIsNotFoundException;
import com.fpt.fis.template.model.request.TemplateRequest;
import com.fpt.fis.template.model.response.TemplateResponse;
import com.fpt.fis.template.model.response.TemplateResponsePage;
import com.fpt.fis.template.repository.TemplateRepository;
import com.fpt.fis.template.repository.entity.Template;
import com.fpt.fis.template.service.TemplateService;
import org.apache.commons.lang3.StringUtils;
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
                .error(new TemplateIsNotFoundException(id)));
    }

    @Override
    public Mono<TemplateResponsePage> readAllTemplates(String nameOrDescription, Pageable pageable) {
        Flux<Template> findData;
        Mono<Long> totalCount;
        if (StringUtils.isBlank(nameOrDescription)) {
            findData = templateRepository.findAllBy(pageable);
            totalCount = templateRepository.countAllBy();
        } else {
            findData = templateRepository.findByNameContainingOrDescriptionContaining(nameOrDescription, nameOrDescription, pageable);
            totalCount = templateRepository.countByNameContainingOrDescriptionContaining(nameOrDescription, nameOrDescription);
        }
        return findData.map(this::mapTemplateToTemplateResponse).collectList()
                .zipWith(totalCount, (content, total) ->
                        new TemplateResponsePage(content, total));
    }

    @Override
    public Mono<TemplateResponse> createTemplate(TemplateRequest request) {
        return templateRepository.insert(mapTemplateRequestToTemplate(request, null)).map(this::mapTemplateToTemplateResponse);
    }

    @Override
    public Mono<TemplateResponse> updateTemplate(String id, TemplateRequest request) {
        return templateRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new TemplateIsNotFoundException(id)))
                .flatMap(t -> templateRepository.save(mapTemplateRequestToTemplate(request, t))).map(this::mapTemplateToTemplateResponse);
    }

    @Override
    public Mono<Void> deleteTemplateById(String id) {
        return templateRepository.findById(id).switchIfEmpty(Mono.error(
                new TemplateIsNotFoundException(id))).flatMap(t -> templateRepository.deleteById(id));
    }

    @Override
    public Mono<List<String>> readAllParameters(String id) {
        Mono<Template> template = templateRepository.findById(id).switchIfEmpty(Mono.error(new TemplateIsNotFoundException(id)));
        return template.map(t -> {
            List<String> parameters = t.getParameters();
            List<String> modifiedParameters = new ArrayList<>();

            // Iterate through each parameter and remove the first '$' character
            for (String parameter : parameters) {
                if (parameter.startsWith("$")) {
                    modifiedParameters.add(parameter.substring(1));
                } else {
                    modifiedParameters.add(parameter);
                }
            }

            return modifiedParameters;
        });
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
        template.setParameters(findParameters(template.getContent()));
        return template;
    }

    private List<String> findParameters(String content) {
        List<String> parameters = new ArrayList<>();

        // Define the regex pattern to match strings starting with a $ character
        Pattern pattern = Pattern.compile("\\$\\S+");
        Matcher matcher = pattern.matcher(content);

        // Find all matches in the content
        while (matcher.find()) {
            parameters.add(matcher.group());
        }

        return parameters;
    }
}
