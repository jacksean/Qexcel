package com.qexcel.core.cell.adapter;

import org.apache.poi.ss.usermodel.Cell;

import com.qexcel.core.RowAdapter;
import com.qexcel.core.enums.CellDataType;

public class NumberAdapter extends CellAdapter<Double>{
	
	public NumberAdapter(RowAdapter rowAdapter, Cell cell) {
		super(rowAdapter, cell);
	}
	
	@Override
    public CellDataType getCellType() {
        return CellDataType.NUMBER;
    }
	
    @Override
    public Double getValue() {
        return getCell().getNumericCellValue();
    }

    @Override
    public void setValue(Double value) {
        getCell().setCellValue(value);
    }
}
