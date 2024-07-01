package com.fpt.fis.template.exception;

import com.fpt.framework.data.exception.DataIsNotFoundException;

public class TemplateIsNotFoundException extends DataIsNotFoundException {
    public TemplateIsNotFoundException() {
        super("Print template is not found");
    }
}
