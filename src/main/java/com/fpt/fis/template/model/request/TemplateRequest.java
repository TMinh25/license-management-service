package com.fpt.fis.template.model.request;

import com.fpt.fis.template.repository.entity.enums.TemplateEngine;
import com.fpt.fis.template.repository.entity.enums.TemplateType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TemplateRequest {

    @NotEmpty(message = "EMPTY_NAME")
    @Size(max=200, message = "NAME_LENGTH")
    private String name;

    @Size(max=300)
    private String description;

    @NotEmpty(message = "EMPTY_CONTENT")
    @Size(max=3000000, message = "CONTENT_LENGTH")
    private String content;

    private TemplateType type;

    private TemplateEngine engine;

}
