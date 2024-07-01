package com.fpt.fis.template.exception;

import com.fpt.framework.data.exception.DataException;

public class TemplateIsDuplicatedName extends DataException {

    public TemplateIsDuplicatedName() {
        super("Print template name is duplicated");
    }
}
