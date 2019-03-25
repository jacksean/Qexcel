package com.qexcel.template.anno;

import com.qexcel.core.template.TemplateParser;

@SuppressWarnings("rawtypes")
public class AnnoTemplateMeta<F extends AnnoTemplateConfigFactory,S extends TemplateParser> {
    private final F factory;
    private final S resolver;
    
    public AnnoTemplateMeta(F factory, S resolver) {
        super();
        this.factory = factory;
        this.resolver = resolver;
    }

    public F getFactory() {
        return factory;
    }

    public S getResolver() {
        return resolver;
    }
}
