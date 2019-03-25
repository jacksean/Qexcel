package com.qexcel.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TypeReference<T> {

    static ConcurrentMap<Type, Type> classTypeCache = new ConcurrentHashMap<Type, Type>(16, 0.75f, 1);

    protected final Type             type;

    protected TypeReference() {
        Type superClass = getClass().getGenericSuperclass();
        Type tmpType = ((ParameterizedType) superClass).getActualTypeArguments()[0];

        Type cachedType = classTypeCache.get(tmpType);
        if (cachedType == null) {
            classTypeCache.putIfAbsent(tmpType, tmpType);
            cachedType = classTypeCache.get(tmpType);
        }

        this.type = cachedType;
    }

    public Type getType() {
        return type;
    }
}
