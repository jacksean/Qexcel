package com.qexcel.template.transformers;

import java.util.List;

import com.qexcel.core.template.TemplateConfig;
import com.qexcel.template.anno.CellDataConfig;

public class TransformersConfig extends TemplateConfig{
    
    private List<PositionCellConfig> datas;
    
    public TransformersConfig() {
        super("transformers");
    }

    public List<PositionCellConfig> getDatas() {
        return datas;
    }

    public void setDatas(List<PositionCellConfig> datas) {
        this.datas = datas;
    }
    
    public static class PositionCellConfig {
        private CellDataConfig cellData;
        private int startRow;
        private int startColumn;
        private int rowOffset;
        private int columnOffset;
        public CellDataConfig getCellData() {
            return cellData;
        }
        public void setCellData(CellDataConfig cellData) {
            this.cellData = cellData;
        }
        public int getStartRow() {
            return startRow;
        }
        public void setStartRow(int startRow) {
            this.startRow = startRow;
        }
        public int getStartColumn() {
            return startColumn;
        }
        public void setStartColumn(int startColumn) {
            this.startColumn = startColumn;
        }
        public int getRowOffset() {
            return rowOffset;
        }
        public void setRowOffset(int rowOffset) {
            this.rowOffset = rowOffset;
        }
        public int getColumnOffset() {
            return columnOffset;
        }
        public void setColumnOffset(int columnOffset) {
            this.columnOffset = columnOffset;
        }
    }
}
