package com.fpt.fis.template.service;

import com.fpt.fis.template.repository.entity.enums.TemplateEngine;
import com.fpt.fis.template.service.impl.VelocityVariableExtractor;
import org.apache.commons.lang3.NotImplementedException;

public class TemplateVariableExtractorFactory {
    public static final TemplateVariableExtractor factoryExtractor(TemplateEngine engine) {
        switch (engine) {
            case VELOCITY -> {
                return new VelocityVariableExtractor();
            }
            default -> throw new NotImplementedException(
                    "Variable extractor not yet implemented for engine: %s. Have a look this trace".formatted(engine));
        }
    }
}
