package com.fpt.fis.template.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FilterOperatorEnum {

    EQUAL("eq"), NOT_EQUAL("neq"), GREATER_THAN("gt"), LESS_THAN("lt"), CONTAINS("contains"), IN("in"), LIKE("like"),
    EXIST("exist"), GREATER_THAN_EQUALS("gte"), LESS_THAN_EQUALS("lte");

    private final String value;

    FilterOperatorEnum(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    public static FilterOperatorEnum fromValue(String value) {
        for (FilterOperatorEnum op : FilterOperatorEnum.values()) {

            // Case insensitive operation name
            if (String.valueOf(op.value).equalsIgnoreCase(value)) {
                return op;
            }
        }
        return null;
    }
}
