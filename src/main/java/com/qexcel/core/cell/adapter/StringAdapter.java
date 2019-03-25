package com.qexcel.core.cell.adapter;

import org.apache.poi.ss.usermodel.Cell;

import com.qexcel.core.RowAdapter;
import com.qexcel.core.enums.CellDataType;

public class StringAdapter extends CellAdapter<String>{

	public StringAdapter(RowAdapter rowAdapter, Cell cell) {
		super(rowAdapter, cell);
	}
	
	@Override
    public CellDataType getCellType() {
        return CellDataType.STRING;
    }
	
    @Override
    public String getValue() {
        return getCell().getRichStringCellValue().getString();
    }

    @Override
    public void setValue(String value) {
        getCell().setCellValue(value);
    }
}
