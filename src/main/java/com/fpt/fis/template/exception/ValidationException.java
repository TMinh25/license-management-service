package com.fpt.fis.template.exception;

import com.fpt.framework.web.api.exception.InvalidParameterException;

public class ValidationException extends InvalidParameterException {

    public ValidationException(String code, String message) {
        super(code, message);
    }
}


