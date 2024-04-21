package com.fpt.fis.template.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FilterConjunctionType {

    AND("and"), OR("or");

    private final String value;

    FilterConjunctionType(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }
}
