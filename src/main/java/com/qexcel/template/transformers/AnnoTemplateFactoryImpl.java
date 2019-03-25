package com.qexcel.template.transformers;

import java.util.ArrayList;

import com.qexcel.template.anno.AnnoTemplateConfigFactory;
import com.qexcel.template.anno.CellDataConfig;
import com.qexcel.template.transformers.TransformersConfig.PositionCellConfig;

public class AnnoTemplateFactoryImpl implements AnnoTemplateConfigFactory<Transformers,TransformersConfig>{

    @Override
    public TransformersConfig getTemplateConfig(Transformers annotation) {
        TransformersConfig config = new TransformersConfig();
        config.setDatas(new ArrayList<>(annotation.body().length));
        for(AnnoPositionCell posCell : annotation.body()) {
            PositionCellConfig posCellConfig = new TransformersConfig.PositionCellConfig();
            posCellConfig.setColumnOffset(posCell.columnOffset());
            posCellConfig.setStartColumn(posCell.startColumn());
            posCellConfig.setStartRow(posCell.startRow());
            posCellConfig.setRowOffset(posCell.rowOffset());
            posCellConfig.setCellData(new CellDataConfig());
            posCellConfig.getCellData().setFont(posCell.cellData().font());
            posCellConfig.getCellData().setFontBold(posCell.cellData().fontBold());
            posCellConfig.getCellData().setFontColor(posCell.cellData().fontColor());
            posCellConfig.getCellData().setFontSize(posCell.cellData().fontSize());
            posCellConfig.getCellData().setFormat(posCell.cellData().format());
            posCellConfig.getCellData().sethAlign(posCell.cellData().halign());
            posCellConfig.getCellData().setProperty(posCell.cellData().property());
            posCellConfig.getCellData().setWriteSpel(posCell.cellData().writeSpel());
            posCellConfig.getCellData().setText(posCell.cellData().text());
            posCellConfig.getCellData().setType(posCell.cellData().type());
            posCellConfig.getCellData().setvAlign(posCell.cellData().valign());
            posCellConfig.getCellData().setWidth(posCell.cellData().width());
            config.getDatas().add(posCellConfig);
        }
        return config;
    }
    
}
