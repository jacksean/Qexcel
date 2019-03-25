package com.qexcel.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.qexcel.core.template.FieldRename;

public class ReflectUtil {
    
    static ThreadLocal<Map<Class,Map<String,Field>>> fieldsCache = ThreadLocal.withInitial(HashMap::new);
   
    public static Field getField(Object obj,String fieldName) {
        Map<String,Field> fieldMapping = getFieldMappingWithCache(obj,true);
        if(!fieldMapping.containsKey(fieldName))
            throw new RuntimeException("no such field ["+fieldName+"] in "+obj.getClass());
        return fieldMapping.get(fieldName);
    }
    
    public static Object getFieldValue(Object obj,String fieldName) {
        Field f = getField(obj, fieldName);
        try {
            return f.get(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static Map<String,Field> getFieldMappingWithCache(Object obj,boolean accessable) {
        Class clazz = obj.getClass();
        if(fieldsCache.get().containsKey(clazz)) {
            return fieldsCache.get().get(clazz);
        }
        Map<String,Field> newMapping = new HashMap<>();
        for(; clazz != Object.class ; clazz = clazz.getSuperclass()) {  
            try {
                Field[] fields = clazz.getDeclaredFields();
                for(Field field:fields) {
                   if(accessable)
                        field.setAccessible(true);
                   FieldRename anno = field.getAnnotation(FieldRename.class);
                    if(anno != null && StringUtils.isNotBlank(anno.value()))
                        newMapping.put(anno.value(), field);
                    else
                        newMapping.put(field.getName(), field);
                }
            } catch (Exception e) {} 
        }
        fieldsCache.get().put(clazz, newMapping);
        return newMapping;
    }
    
	public static Object invoke(Object obj,Method m,Object...args) {
	    try {
	        return m.invoke(obj,args);
	    }catch (Exception e) {
	        throw new RuntimeException(e);
        }
	}
	
	public static Class<?> getRawType(Type type) {
        if (type instanceof Class) {
            return (Class) type;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            return (Class) rawType;
        } else if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            return Array.newInstance(getRawType(componentType), 0).getClass();
        } else if (type instanceof TypeVariable) {
            return Object.class;
        } else if (type instanceof WildcardType) {
            return getRawType(((WildcardType) type).getUpperBounds()[0]);
        } else {
            String className = type == null ? "null" : type.getClass().getName();
            throw new IllegalArgumentException("Expected a Class, ParameterizedType, or GenericArrayType, but <" + type + "> is of type " + className);
        }
    }
	
	public static boolean isWrapClass(Class clz) {
        try {
           return ((Class) clz.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }
}
