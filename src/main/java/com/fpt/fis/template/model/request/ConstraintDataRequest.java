package com.fpt.fis.template.model.request;

import com.fpt.fis.template.repository.entity.enums.UsageType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConstraintDataRequest {

    private String resourceId;

    private String usageId;

    private UsageType usageType;

}
