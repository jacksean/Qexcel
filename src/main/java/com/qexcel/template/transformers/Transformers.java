package com.qexcel.template.transformers;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.qexcel.template.anno.AnnoTemplate;
import com.qexcel.template.anno.AnnoTemplateList;

/**
 * 类Transformers.java的实现描述：不规则单元格组合体模板,提供支持合并单元格
 * @author sean 2019年3月20日 下午1:34:31
 */
@Retention(RUNTIME)
@Target({ ElementType.TYPE })
@Repeatable(Transformers.List.class)
@AnnoTemplate(factory = AnnoTemplateFactoryImpl.class,parser=TransformersParser.class)
public @interface Transformers {
    int order() default 0;
    
    AnnoPositionCell[] body() default {};
	
	@Retention(RUNTIME)
	@Target({ ElementType.TYPE })
	@AnnoTemplateList
	@interface List{
		Transformers[] value();
	}
}
