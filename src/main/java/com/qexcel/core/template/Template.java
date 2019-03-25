package com.qexcel.core.template;

public class Template<T extends TemplateConfig> {

    private TemplateParser<T> parser;

    private TemplateConfig config;

    public Template(TemplateParser<T> parser, TemplateConfig config) {
        super();
        this.parser = parser;
        this.config = config;
    }
    
    public Template() {
        super();
    }

    public TemplateParser getParser() {
        return parser;
    }

    public void setParser(TemplateParser parser) {
        this.parser = parser;
    }

    public TemplateConfig getConfig() {
        return config;
    }

    public void setConfig(TemplateConfig config) {
        this.config = config;
    }
}
