package com.qexcel.template.anno;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;

import com.qexcel.core.enums.CellDataType;
import com.qexcel.core.enums.HorizontalAlign;
import com.qexcel.core.enums.VerticalAlign;

@Retention(RUNTIME)
@Target({ ElementType.ANNOTATION_TYPE,FIELD })
public @interface AnnoCell {
	/**
	 * excel中对应的类型
	 * @return
	 */
	CellDataType type() default CellDataType.STRING;
	int width() default -1;
	String text() default "";
	String property() default "";
	String writeSpel() default "";
	String format() default "";
	HorizontalAlign halign() default HorizontalAlign.DEFAULT;
	VerticalAlign valign() default VerticalAlign.DEFAULT;
	String font() default "";
	short fontSize() default -1;
	boolean fontBold() default false;
	HSSFColorPredefined fontColor() default HSSFColorPredefined.BLACK;
}
