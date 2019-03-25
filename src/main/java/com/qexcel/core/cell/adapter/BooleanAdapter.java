package com.qexcel.core.cell.adapter;

import org.apache.poi.ss.usermodel.Cell;

import com.qexcel.core.RowAdapter;
import com.qexcel.core.enums.CellDataType;

public class BooleanAdapter extends CellAdapter<Boolean>{

	public BooleanAdapter(RowAdapter rowAdapter, Cell cell) {
		super(rowAdapter, cell);
	}
	
	@Override
    public CellDataType getCellType() {
        return CellDataType.BOOLEAN;
    }

    @Override
    public Boolean getValue() {
        return getCell().getBooleanCellValue();
    }

    @Override
    public void setValue(Boolean value) {
        getCell().setCellValue(value);
    }
}
