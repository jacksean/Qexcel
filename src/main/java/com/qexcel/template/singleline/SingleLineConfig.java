package com.qexcel.template.singleline;

import java.util.List;

import com.qexcel.core.template.TemplateConfig;
import com.qexcel.template.anno.CellDataConfig;

public class SingleLineConfig extends TemplateConfig{
    
    private List<CellDataConfig> datas;
    
    public SingleLineConfig() {
        super("singleLine");
    }

    public List<CellDataConfig> getDatas() {
        return datas;
    }

    public void setDatas(List<CellDataConfig> datas) {
        this.datas = datas;
    }
}
