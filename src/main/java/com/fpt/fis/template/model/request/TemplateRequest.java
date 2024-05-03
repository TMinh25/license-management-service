package com.fpt.fis.template.model.request;

import com.fpt.fis.template.repository.entity.enums.TemplateEngine;
import com.fpt.fis.template.repository.entity.enums.TemplateType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TemplateRequest {

    private String name;

    private String description;

    private String content;

    private TemplateType type;

    private TemplateEngine engine;

}
