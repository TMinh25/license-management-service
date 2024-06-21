package com.fpt.fis.template.model.response;

import com.fpt.fis.template.repository.entity.enums.UsageType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConstraintDataResponse {
    private String id;

    private String resourceId;

    private int usageId;

    private UsageType usageType;
}
