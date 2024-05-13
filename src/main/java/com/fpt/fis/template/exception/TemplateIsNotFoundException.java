package com.fpt.fis.template.exception;

import com.fpt.framework.data.exception.DataIsNotFoundException;

public class TemplateIsNotFoundException extends DataIsNotFoundException {
    public TemplateIsNotFoundException(String id) {
        super("Template", String.format("Not found template with id: %s", id));
    }
}
