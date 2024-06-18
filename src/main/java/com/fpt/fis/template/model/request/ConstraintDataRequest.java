package com.fpt.fis.template.model.request;
import java.util.List;

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

    private List<String> resourceIds;

    private int usageId;

    private UsageType usageType;

}
