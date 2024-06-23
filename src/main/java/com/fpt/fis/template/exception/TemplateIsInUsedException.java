package com.fpt.fis.template.exception;

import com.fpt.framework.data.exception.DataException;

public class TemplateIsInUsedException extends DataException {

    public TemplateIsInUsedException(String resource, String usageType) {
        super(resource, "It is in used by an other service: %s".formatted(usageType));
    }
}
