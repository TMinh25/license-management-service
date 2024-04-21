package com.fpt.fis.template.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fpt.fis.template.model.FilterDTO;
import org.apache.commons.lang3.math.NumberUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.function.Function;

public enum FilterDataType {
    NUMBER(FilterDataType::processNumber), TEXTFIELD(FilterDataType::processTextfield),
    DATE(FilterDataType::processDate), DATETIME(FilterDataType::processDateTime), TIME(FilterDataType::processTime),
    BOOLEAN(FilterDataType::processBoolean);

    private final Function<FilterDTO, Object> value;

    FilterDataType(Function<FilterDTO, Object> value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    public Function<FilterDTO, Object> getHandleMethod() {
        return value;
    }

    public static FilterDataType fromValue(String value) {
        for (FilterDataType op : FilterDataType.values()) {

            // Case insensitive filter data type name
            if (String.valueOf(op.name()).equalsIgnoreCase(value)) {
                return op;
            }
        }
        return FilterDataType.TEXTFIELD;
    }

    private static Number processNumber(FilterDTO filterDto) {
        try {
            return NumberUtils.createNumber(String.valueOf(filterDto.getValue()));
        } catch (Exception e) {
            return -1;
        }
    }

    private static String processTextfield(FilterDTO filterDto) {
        if (filterDto.getOperator() == FilterOperatorEnum.LIKE) {
            return String.valueOf(filterDto.getValue()).replace("%28", "\\(").replace("%29", "\\)");
        } else {
            return String.valueOf(filterDto.getValue()).replace("%28", "(").replace("%29", ")");
        }
    }

    private static LocalDateTime processDate(FilterDTO filterDto) {
        if (filterDto.getOperator() == FilterOperatorEnum.GREATER_THAN) {
            return LocalTime.MAX.atDate(LocalDate.parse(String.valueOf(filterDto.getValue())).minusDays(1))
                    .atZone(ZoneId.systemDefault()).toLocalDateTime();
        } else if (filterDto.getOperator() == FilterOperatorEnum.LESS_THAN) {
            return LocalTime.MAX.atDate(LocalDate.parse(String.valueOf(filterDto.getValue())))
                    .atZone(ZoneId.systemDefault()).toLocalDateTime();
        } else if (filterDto.getOperator() == FilterOperatorEnum.GREATER_THAN_EQUALS) {
            return LocalDate.parse(String.valueOf(filterDto.getValue())).atStartOfDay(ZoneId.systemDefault())
                    .toLocalDateTime();
        } else if (filterDto.getOperator() == FilterOperatorEnum.LESS_THAN_EQUALS) {
            return LocalTime.MAX.atDate(LocalDate.parse(String.valueOf(filterDto.getValue())))
                    .atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
        return LocalTime.MAX.atDate(LocalDate.parse(String.valueOf(filterDto.getValue())).minusDays(1))
                .atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    private static LocalDateTime processDateTime(FilterDTO filterDto) {
        return LocalDateTime.parse(String.valueOf(filterDto.getValue()));
    }

    private static LocalTime processTime(FilterDTO filterDto) {
        return LocalTime.parse(String.valueOf(filterDto.getValue()));
    }

    private static Boolean processBoolean(FilterDTO filterDto) {
        return Boolean.parseBoolean(String.valueOf(filterDto.getValue()));
    }
}
