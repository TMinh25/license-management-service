package com.fpt.fis.template.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TemplateListFilterResponse {
    private List<TemplateListResponse> content;
    private Long total;
}
