package com.qexcel.core.template.context;

import com.qexcel.util.ReflectUtil;

public class ObjectWrapper {
    private final Object source;
    
    public static ObjectWrapper wrapper(Object source) {
        return new ObjectWrapper(source);
    }

    public ObjectWrapper(Object source) {
        super();
        this.source = source;
    }
    
    public Object getVal(String fieldName) {
        return ReflectUtil.getFieldValue(source, fieldName);
    }

    public Object getSource() {
        return source;
    }
}
