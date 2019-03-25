package com.qexcel.util;

import java.util.Map;

import org.springframework.expression.BeanResolver;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class SpelUtil {
    
    private static ParserContext DEFAULT_PARSERCONTEXT = new TemplateParserContext();
    
    public static <T> T parse(String spel,Map<String,Object> params,Class<T> retClz) {
        return (T) parse(spel, null, params, null,retClz);
    }
    
    public static Object parse(String spel,Map<String,Object> params) {
        return parse(spel, null, params);
    }
    
    public static Object parse(String spel,Object root,Map<String,Object> params) {
       return parse(spel, root, params,null,null);
    }
    
    public static <T> T parse(String spel,Object root,Map<String,Object> params,Class<T> retClz) {
        return (T) parse(spel, root, params,null,retClz);
     }
    
    public static Object parse(String spel,
                               Object root,
                               Map<String,Object> params,
                               BeanResolver beanResolver,
                               Class retClz) {
        //获取被拦截方法参数名列表(使用Spring支持类库)
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        if(beanResolver != null)
            context.setBeanResolver(beanResolver);
        if(root != null)
            context.setRootObject(root);
        if(params != null) {
            context.setVariables(params);
        }
        Expression express = parser.parseExpression(spel,DEFAULT_PARSERCONTEXT);
        if(retClz != null)
            return express.getValue(context,retClz);
        else
            return express.getValue(context);
    }
}
