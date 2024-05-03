package com.fpt.fis.template.repository.entity.enums;

public enum TemplateEngine {
    VELOCITY("velocity");

    private final String engine;

    TemplateEngine(String engine) {
        this.engine = engine;
    }

    public String engine() {
        return engine;
    }
}
