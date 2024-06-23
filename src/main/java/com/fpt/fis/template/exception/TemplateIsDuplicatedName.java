package com.fpt.fis.template.exception;

import com.fpt.framework.data.exception.DataException;

public class TemplateIsDuplicatedName extends DataException {

    public TemplateIsDuplicatedName(String name) {
        super("Template", "Name %s is duplicated".formatted(name));
    }
}
