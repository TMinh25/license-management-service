package com.fpt.fis.template.exception;

import com.fpt.framework.data.exception.DataIsNotFoundException;

public class ConstraintDataIsNotFoundException extends DataIsNotFoundException {
    public ConstraintDataIsNotFoundException(String id) {
        super("ConstraintData", String.format("Not found constraint data with id: %s", id));
    }
}
