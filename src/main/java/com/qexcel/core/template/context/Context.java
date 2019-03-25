package com.qexcel.core.template.context;

import java.util.HashMap;
import java.util.Map;

import com.qexcel.core.seqaccess.SpelContext;

public class Context{
    
    private Map<String,Object> params = new HashMap<>();
    
    public Context addParam(String k,Object v) {
        params.put(k, v);
        return this;
    }
    
    public Context addParams(Map<String,Object> kvs) {
        if(kvs != null)
            params.putAll(kvs);
        return this;
    }
    
    public Object getParam(String k) {
        return params.get(k);
    }
    
    public Map<String,Object> getAllParams(){
        return params;
    }
    
    public DefaultSpelContext buildSpelContext(Object rootObj) {
        return new DefaultSpelContext(getAllParams(), rootObj);
    }
    
    public static class DefaultSpelContext implements SpelContext{
        private Map<String,Object> variables;
        private Object rootObject;
        public DefaultSpelContext(Map<String, Object> variables, Object rootObject) {
            super();
            this.variables = variables;
            this.rootObject = rootObject;
        }
        
        public void setRootObject(Object rootObject) {
            this.rootObject = rootObject;
        }
        
        @Override
        public Object getRootObject() {
            return this.rootObject;
        }
        public void setVariables(Map<String, Object> variables) {
            this.variables = variables;
        }
        @Override
        public Map<String, Object> getVariables() {
            return this.variables;
        }
    }
}
