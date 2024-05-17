package com.fpt.fis.template.service;

import java.util.List;

public interface TemplateVariableExtractor {
    List<String> extractVariables(String template);
}
