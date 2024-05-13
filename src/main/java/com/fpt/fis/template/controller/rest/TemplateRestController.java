package com.fpt.fis.template.controller.rest;

import com.fpt.fis.template.model.request.TemplateRequest;
import com.fpt.fis.template.model.response.TemplateResponsePage;
import com.fpt.fis.template.model.response.TemplateResponse;
import com.fpt.fis.template.service.TemplateService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("templates")
public class TemplateRestController {
    @Autowired

    private TemplateService templateService;

    @GetMapping("{id}")
    @PreAuthorize("@author.hasPermission('template', 'view-detail')")
    public Mono<TemplateResponse> readTemplateById(@Parameter @PathVariable("id") String id) {
        return templateService.readTemplateById(id);
    }

    @GetMapping("{id}/content")
    @PreAuthorize("@author.hasPermission('template', 'view-detail')")
    public Mono<String> readTemplateContentById(@Parameter @PathVariable("id") String id) {
        return templateService.readTemplateContentById(id);
    }

    @PostMapping()
    @PreAuthorize("@author.hasPermission('template', 'create')")
    public Mono<TemplateResponse> createTemplate(@RequestBody @Valid TemplateRequest request) {
        return templateService.createTemplate(request);
    }

    @PutMapping("{id}")
    @PreAuthorize("@author.hasPermission('template', 'update')")
    public Mono<TemplateResponse> updateTemplate(@Parameter @PathVariable("id") String id, @RequestBody @Valid TemplateRequest request) {
        return templateService.updateTemplate(id, request);
    }

    @GetMapping("")
    @PreAuthorize("@author.hasPermission('template', 'view')")
    public Mono<TemplateResponsePage> readAllTemplates(
            @RequestParam(required = false) String nameOrDescription,
            @ParameterObject Pageable pageable) {
        return templateService.readAllTemplates(nameOrDescription, pageable);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("@author.hasPermission('template', 'delete')")
    public Mono<Void> deleteTemplate(@Parameter @PathVariable("id") String id) {
        return templateService.deleteTemplateById(id);
    }

    @GetMapping("{id}/parameters")
    @PreAuthorize("@author.hasPermission('template', 'view')")
    public Flux<String> readAllParameters(@Parameter @PathVariable("id") String id) {
        return templateService.readAllParameters(id);
    }
}
