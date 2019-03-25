package com.qexcel.template.anno;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.qexcel.core.template.TemplateParser;

@Retention(RUNTIME)
@Target(ANNOTATION_TYPE)
public @interface AnnoTemplate {
    Class<? extends AnnoTemplateConfigFactory> factory();
    Class<? extends TemplateParser> parser();
}
