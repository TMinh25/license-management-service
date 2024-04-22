package com.fpt.fis.template.model;

import com.fpt.fis.template.model.enums.TemplateEngine;
import com.fpt.fis.template.model.enums.TemplateType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TemplateDTO {

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
}
