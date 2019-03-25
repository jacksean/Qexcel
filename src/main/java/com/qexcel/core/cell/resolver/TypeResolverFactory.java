package com.qexcel.core.cell.resolver;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qexcel.core.cell.adapter.CellAdapter;

public class TypeResolverFactory {
    
    static Logger logger = LoggerFactory.getLogger(CellAdapter.class);
    
    private static Map<Class,TypeResolver> mapping = new HashMap<>();
    static {
        registerTypeResolver(Integer.class, IntegerResolver.INSTANCE);
        registerTypeResolver(int.class,IntegerResolver.INSTANCE);
        registerTypeResolver(Double.class,DoubleResolver.INSTANCE);
        registerTypeResolver(double.class,DoubleResolver.INSTANCE);
        registerTypeResolver(String.class,StringResolver.INSTANCE);
        registerTypeResolver(Boolean.class,BooleanResolver.INSTANCE);
        registerTypeResolver(boolean.class,BooleanResolver.INSTANCE);
        registerTypeResolver(Float.class,FloatResolver.INSTANCE);
        registerTypeResolver(float.class,FloatResolver.INSTANCE);
        registerTypeResolver(Date.class,DateResolver.INSTANCE);
        registerTypeResolver(LocalDate.class,LocalDateResolver.INSTANCE);
        registerTypeResolver(LocalDateTime.class,LocalDateTimeResolver.INSTANCE);
    }
    
    public static void registerTypeResolver(Class<?> clz,TypeResolver<?> resolver) {
        mapping.put(clz, resolver);
    }
    
    public static TypeResolver getTypeResolver(Class<?> clz) {
        TypeResolver r = mapping.get(clz);
        if(r == null)
            logger.warn("not find TypeResolver for {}",clz);
        return r;
    }
}
