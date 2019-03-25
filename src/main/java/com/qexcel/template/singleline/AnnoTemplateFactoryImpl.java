package com.qexcel.template.singleline;

import java.util.ArrayList;

import com.qexcel.template.anno.AnnoCell;
import com.qexcel.template.anno.AnnoTemplateConfigFactory;
import com.qexcel.template.anno.CellDataConfig;

public class AnnoTemplateFactoryImpl implements AnnoTemplateConfigFactory<SingleLine,SingleLineConfig>{

    @Override
    public SingleLineConfig getTemplateConfig(SingleLine annotation) {
        SingleLineConfig config = new SingleLineConfig();
        config.setDatas(new ArrayList<>());
        AnnoCell[] datas = annotation.body();
        for(AnnoCell data:datas) {
            CellDataConfig cellData = new CellDataConfig();
            cellData.setFont(data.font());
            cellData.setFontBold(data.fontBold());
            cellData.setFontColor(data.fontColor());
            cellData.setFontSize(data.fontSize());
            cellData.setFormat(data.format());
            cellData.sethAlign(data.halign());
            cellData.setProperty(data.property());
            cellData.setWriteSpel(data.writeSpel());
            cellData.setText(data.text());
            cellData.setType(data.type());
            cellData.setvAlign(data.valign());
            cellData.setWidth(data.width());
            config.getDatas().add(cellData);
        }
        return config;
    }
    
}
