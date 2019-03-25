package com.qexcel.template.singleline;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.qexcel.template.anno.AnnoCell;
import com.qexcel.template.anno.AnnoTemplate;
import com.qexcel.template.anno.AnnoTemplateList;

@Retention(RUNTIME)
@Target({ ElementType.TYPE })
@Repeatable(SingleLine.List.class)
@AnnoTemplate(factory = AnnoTemplateFactoryImpl.class,parser=SingleLineParser.class)
public @interface SingleLine {
    int order() default 0;
    
	AnnoCell[] body() default {};
	
	@Retention(RUNTIME)
	@Target({ ElementType.TYPE })
	@AnnoTemplateList
	@interface List{
		SingleLine[] value();
	}
}
