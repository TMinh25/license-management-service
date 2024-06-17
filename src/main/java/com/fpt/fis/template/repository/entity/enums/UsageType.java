package com.fpt.fis.template.repository.entity.enums;

public enum UsageType {
    PROCESS("process");

    private final String type;

    UsageType(String type) {
        this.type = type;
    }

    public String type() {
        return type;
    }
}
