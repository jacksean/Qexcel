package com.qexcel.template.table;

import java.util.ArrayList;

import com.qexcel.template.anno.AnnoCell;
import com.qexcel.template.anno.AnnoTemplateConfigFactory;
import com.qexcel.template.anno.CellDataConfig;

public class TemplateConfigFactoryImpl implements AnnoTemplateConfigFactory<ExcelTable,TableConfig>{

    @Override
    public TableConfig getTemplateConfig(ExcelTable annotation) {
        TableConfig config = new TableConfig();
        config.setHeads(new ArrayList<>());
        config.setDatas(new ArrayList<>());
        config.setDataProperty(annotation.dataProperty());
        AnnoCell[] datas = annotation.body();
        for(AnnoCell data:datas) {
            CellDataConfig cellData = new CellDataConfig();
            cellData.setFont(data.font());
            cellData.setFontBold(data.fontBold());
            cellData.setFontColor(data.fontColor());
            cellData.setFontSize(data.fontSize());
            cellData.setFormat(data.format());
            cellData.setProperty(data.property());
            cellData.setWriteSpel(data.writeSpel());
            cellData.sethAlign(data.halign());
            cellData.setText(data.text());
            cellData.setType(data.type());
            cellData.setvAlign(data.valign());
            cellData.setWidth(data.width());
            config.getDatas().add(cellData);
        }
        AnnoCell[] heads = annotation.head();
        for(AnnoCell head:heads) {
            CellDataConfig cellData = new CellDataConfig();
            cellData.setFont(head.font());
            cellData.setFontBold(head.fontBold());
            cellData.setFontColor(head.fontColor());
            cellData.setFontSize(head.fontSize());
            cellData.setFormat(head.format());
            cellData.setProperty(head.property());
            cellData.setWriteSpel(head.writeSpel());
            cellData.sethAlign(head.halign());
            cellData.setText(head.text());
            cellData.setType(head.type());
            cellData.setvAlign(head.valign());
            cellData.setWidth(head.width());
            config.getHeads().add(cellData);
        }
        return config;
    }
    
}
