package com.qexcel.core.template;

public abstract class TemplateConfig {
    private final String name;
    
    public TemplateConfig(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
