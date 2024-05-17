package com.fpt.fis.template.service.impl;

import com.fpt.fis.template.service.TemplateVariableExtractor;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeSingleton;

import java.io.StringWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class VelocityVariableExtractor implements TemplateVariableExtractor {

    private String macroBodyReferent = UUID.randomUUID().toString();

    public List<String> extractVariables(String content) {

        StringWriter writer = new StringWriter();
        VariableCollector variableLogger = new VariableCollector(macroBodyReferent);
        RuntimeSingleton.setProperty("velocimacro.body_reference", macroBodyReferent);
        Velocity.evaluate(variableLogger, writer, "", content);
        return variableLogger.variables.stream().toList();
    }
    static class VariableCollector extends  VelocityContext {
        Set<String> variables = new HashSet<>();
        Set<String> tempVariableInTemplate = new HashSet<>();

        VariableCollector(String macroBodyReferent) {
            tempVariableInTemplate.add(macroBodyReferent);
        }

        @Override
        public Object put(String key, Object value) {
            tempVariableInTemplate.add(key);
            return super.put(key, value);
        }
        @Override
        public Object get(String key) {
            if (!tempVariableInTemplate.contains(key)) {
                variables.add(key); // collect variable access
            }
            return null; // Return null to satisfy the requirement of the collector
        }
    }
}
