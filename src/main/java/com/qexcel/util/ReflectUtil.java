package com.qexcel.util;

import com.qexcel.core.template.FieldRename;
import org.apache.commons.lang3.StringUtils;

import java.lang.ref.SoftReference;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

public class ReflectUtil {

    static Map<Class, SoftReference<Map<String,Field>>> fieldsCache = new HashMap<>();

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
        SoftReference<Map<String, Field>> cache = fieldsCache.get(clazz);
        Map<String, Field> fieldMapping = cache != null ? cache.get() : null;
        if(cache == null || fieldMapping == null) {
            synchronized (fieldsCache) {
                cache = fieldsCache.get(clazz);
                fieldMapping = cache != null ? cache.get() : null;
                if(cache == null || fieldMapping == null) {
                    fieldMapping = new HashMap<>();
                    for(; clazz != Object.class ; clazz = clazz.getSuperclass()) {
                        try {
                            Field[] fields = clazz.getDeclaredFields();
                            for(Field field:fields) {
                                if(accessable)
                                    field.setAccessible(true);
                                FieldRename anno = field.getAnnotation(FieldRename.class);
                                if(anno != null && StringUtils.isNotBlank(anno.value()))
                                    fieldMapping.putIfAbsent(anno.value(), field);
                                else
                                    fieldMapping.putIfAbsent(field.getName(), field);
                            }
                        } catch (Exception e) {}
                    }
                    cache = new SoftReference<Map<String,Field>>(fieldMapping);
                    fieldsCache.put(clazz, cache);
                }
            }
        }
        return fieldMapping;
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
