package com.qexcel.template.table;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.qexcel.template.anno.AnnoCell;
import com.qexcel.template.anno.AnnoTemplate;
import com.qexcel.template.anno.AnnoTemplateList;

/**
 * 类ExcelTable.java的实现描述：table模板注解 
 * @author sean 2018年10月31日 上午11:17:26
 */
@Retention(RUNTIME)
@Target({ ElementType.TYPE })
@Repeatable(ExcelTable.List.class)
@AnnoTemplate(factory=TemplateConfigFactoryImpl.class,parser=TableParser.class)
public @interface ExcelTable {
    int order() default 0;
	
	/**
	 * 表格数据属性
	 * @return
	 */
	String dataProperty() default "";
	
	AnnoCell[] head() default {};
	AnnoCell[] body();
	
	@Retention(RUNTIME)
	@Target({ ElementType.TYPE })
	@AnnoTemplateList
	@interface List{
		ExcelTable[] value();
	}
}
