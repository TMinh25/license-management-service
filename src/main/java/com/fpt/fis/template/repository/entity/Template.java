package com.fpt.fis.template.repository.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fpt.fis.template.repository.entity.enums.TemplateEngine;
import com.fpt.fis.template.repository.entity.enums.TemplateType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Document(collection = "template")
public class Template {

    @Id
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String id;

    @NotEmpty(message = "Template's name must not be empty")
    @Size(min=1, max=200)
    private String name;

    @Size(max=300)
    private String description;

    @NotEmpty(message = "Template's content must not be empty")
    @Size(max=3000000)
    private String content;

    private TemplateType type;

    private TemplateEngine engine;

    @CreatedDate
    private LocalDateTime createdTime;

    @LastModifiedDate
    private LocalDateTime updatedTime;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;

    private List<String> paramerters;
}
