package com.qexcel.template.transformers;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.qexcel.template.anno.AnnoCell;

@Retention(RUNTIME)
@Target({ ElementType.ANNOTATION_TYPE,FIELD })
public @interface AnnoPositionCell {
    AnnoCell cellData();
    int startRow();
    int startColumn();
    int rowOffset() default 0;
    int columnOffset() default 0;
}
