package com.fpt.fis.template.exception;

import com.fpt.framework.data.exception.DataException;

public class TemplateIsInUsedException extends DataException {

    public TemplateIsInUsedException(String message) {
        super(message);
    }
}
