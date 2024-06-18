package com.fpt.fis.template.repository.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fpt.fis.template.repository.entity.enums.UsageType;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "constraint-data")
public class ConstraintData {

    @Id
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String id;

    private String resourceId;

    private int usageId;

    private UsageType usageType;

}