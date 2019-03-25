package com.qexcel.template.anno;

import java.lang.annotation.Annotation;

import com.qexcel.core.template.TemplateConfig;

public interface AnnoTemplateConfigFactory<A extends Annotation,C extends TemplateConfig> {
    C getTemplateConfig(A annotation);
}
