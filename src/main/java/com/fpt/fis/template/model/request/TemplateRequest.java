package com.fpt.fis.template.model.request;

import com.fpt.fis.template.repository.entity.enums.TemplateEngine;
import com.fpt.fis.template.repository.entity.enums.TemplateType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TemplateRequest {

    private String templateId;

    private String name;

    private String description;

    private String content;

    private TemplateType type;

    private TemplateEngine engine;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    private String createdBy;

    private String updatedBy;

    private List<String> paramerters;

}
