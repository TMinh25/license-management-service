package com.fpt.fis.template.controller.rest;

import com.fpt.fis.template.model.request.TemplateRequest;
import com.fpt.fis.template.model.response.TemplateListFilterResponse;
import com.fpt.fis.template.model.response.TemplateResponse;
import com.fpt.fis.template.repository.entity.Template;
import com.fpt.fis.template.service.TemplateService;
import com.fpt.fis.template.utils.Constant;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("templates")
public class TemplateRestController {

    @Autowired
    private TemplateService templateService;

    @GetMapping("{id}")
//    @PreAuthorize("@author.hasPermission('template', 'view')")
    public Mono<TemplateResponse> readTemplateById(@Parameter @PathVariable("id") String id) {
        return templateService.readTemplateById(id);
    }

    @PostMapping()
//    @PreAuthorize("@author.hasPermission('template', 'create')")
    public Mono<TemplateResponse> createTemplate(@RequestBody @Valid TemplateRequest request) {
        return templateService.createTemplate(request);
    }

    @PutMapping("{id}")
//    @PreAuthorize("@author.hasPermission('template', 'update')")
    public Mono<TemplateResponse> updateTemplate(@Parameter @PathVariable("id") String id, @RequestBody @Valid TemplateRequest request) {
        return templateService.updateTemplate(id, request);
    }

    @GetMapping("")
//    @PreAuthorize("@author.hasPermission('template', 'view')")
    public Mono<TemplateListFilterResponse> readAllTemplates(
            @RequestParam(name = "query", required = false, defaultValue = Constant.BLANK) String searchText,
            @ParameterObject Pageable pageable) {
        return templateService.readAllTemplates(searchText, pageable);
    }

    @DeleteMapping("{id}")
//    @PreAuthorize("@author.hasPermission('template', 'update')")
    public Mono<Void> deleteTemplate(@Parameter @PathVariable("id") String id) {
        return templateService.deleteTemplate(id);
    }

    @GetMapping("{id}/parameters")
//    @PreAuthorize("@author.hasPermission('template', 'view')")
    public Mono<List<String>> getParamertersById(@Parameter @PathVariable("id") String id) {
        return templateService.getParamertersById(id);
    }
}
