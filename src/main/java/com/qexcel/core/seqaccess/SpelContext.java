package com.qexcel.core.seqaccess;

import java.util.Map;

public interface SpelContext {
    
    /**
     * Spel变量
     * @return
     */
    Map<String,Object> getVariables();
    
    /**
     * root变量
     * @return
     */
    Object getRootObject();
}
