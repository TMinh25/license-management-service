package com.fpt.fis.template.controller.rest;

import com.fpt.fis.template.model.request.TemplateRequest;
import com.fpt.fis.template.model.response.TemplateListFilterResponse;
import com.fpt.fis.template.model.response.TemplateResponse;
import com.fpt.fis.template.service.TemplateService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("templates")
public class TemplateRestController {

    @Autowired
    private TemplateService templateService;

    @GetMapping("{id}")
    @PreAuthorize("@author.hasPermission('template', 'view')")
    public Mono<TemplateResponse> readById(@Parameter @PathVariable("id") Long templateId) {
        return templateService.readById(templateId);
    }

    @PostMapping()
    @PreAuthorize("@author.hasPermission('template', 'create')")
    public Mono<TemplateResponse> create(@RequestBody @Valid TemplateRequest request) {
        return templateService.create(request);
    }

    @PutMapping("{id}")
    @PreAuthorize("@author.hasPermission('template', 'update')")
    public Mono<TemplateResponse> update(@Parameter @PathVariable("id") Long templateId, @RequestBody @Valid TemplateRequest request) {
        return templateService.update(templateId, request);
    }

    @GetMapping("")
    @PreAuthorize("@author.hasPermission('template', 'view')")
    public Mono<TemplateListFilterResponse> read(@ParameterObject Pageable pageable,
                                                 @RequestParam(defaultValue = "") String fql) {
        return this.templateService.read(pageable, fql);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("@author.hasPermission('template', 'update')")
    public Mono<Void> delete(@Parameter @PathVariable("id") Long templateId) {
        return templateService.delete(templateId);
    }

    @GetMapping("parameters/{id}")
    @PreAuthorize("@author.hasPermission('template', 'view')")
    public Mono<List<String>> getParamertersById(@Parameter @PathVariable("id") Long templateId) {
        return templateService.getParamertersById(templateId);
    }
}
