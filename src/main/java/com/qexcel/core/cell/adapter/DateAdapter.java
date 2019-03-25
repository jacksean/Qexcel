package com.qexcel.core.cell.adapter;

import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;

import com.qexcel.core.RowAdapter;
import com.qexcel.core.enums.CellDataType;

public class DateAdapter extends CellAdapter<Date>{

	public DateAdapter(RowAdapter rowAdapter, Cell cell) {
		super(rowAdapter, cell);
	}
	
	@Override
    public CellDataType getCellType() {
        return CellDataType.DATE;
    }
	
	@Override
    public Date getValue() {
	    return getCell().getDateCellValue();
    }

    @Override
    public void setValue(Date value) {
        getCell().setCellValue(value);
    }
}
