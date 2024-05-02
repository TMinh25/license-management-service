package com.fpt.fis.template.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TemplateResponsePage {
    private List<TemplateResponse> content;
    private Long total;
}
