package com.qexcel.core.cell.adapter;

import org.apache.poi.ss.usermodel.Cell;

import com.qexcel.core.RowAdapter;
import com.qexcel.core.enums.CellDataType;

public class FormulaAdapter extends CellAdapter<String>{
	
	public FormulaAdapter(RowAdapter rowAdapter, Cell cell) {
		super(rowAdapter, cell);
	}
	
	@Override
    public CellDataType getCellType() {
        return CellDataType.FORMULA;
    }
	
    @Override
    public String getValue() {
        return null;
    }

    @Override
    public void setValue(String value) {
        getCell().setCellFormula(value);
    }
}
