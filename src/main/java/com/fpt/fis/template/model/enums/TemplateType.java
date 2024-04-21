package com.fpt.fis.template.model.enums;

public enum TemplateType {
    MAIL("mail"), PRINT("print");

    private final String type;

    TemplateType(String type) {
        this.type = type;
    }

    public String engine() {
        return type;
    }
}
