package com.qexcel.template.table;

import java.util.List;

import com.qexcel.core.template.TemplateConfig;
import com.qexcel.template.anno.CellDataConfig;

public class TableConfig extends TemplateConfig{
    private String dataProperty;
    private List<CellDataConfig> heads;
    private List<CellDataConfig> datas;
    
    public TableConfig() {
        super("excelTable");
    }
    
    public String getDataProperty() {
        return dataProperty;
    }

    public void setDataProperty(String dataProperty) {
        this.dataProperty = dataProperty;
    }

    public List<CellDataConfig> getHeads() {
        return heads;
    }
    public void setHeads(List<CellDataConfig> heads) {
        this.heads = heads;
    }
    public List<CellDataConfig> getDatas() {
        return datas;
    }
    public void setDatas(List<CellDataConfig> datas) {
        this.datas = datas;
    }
}
