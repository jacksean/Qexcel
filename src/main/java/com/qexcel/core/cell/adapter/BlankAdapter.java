package com.qexcel.core.cell.adapter;

import org.apache.poi.ss.usermodel.Cell;

import com.qexcel.core.RowAdapter;
import com.qexcel.core.cell.resolver.TypeResolver;
import com.qexcel.core.enums.CellDataType;

public class BlankAdapter extends CellAdapter<Object>{

	public BlankAdapter(RowAdapter rowAdapter, Cell cell) {
		super(rowAdapter, cell);
	}

    @Override
    public CellDataType getCellType() {
        return CellDataType.BLANK;
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public void setValue(Object value) {
    }

    @Override
    public CellAdapter setStyle(CellStyleAdapter meta) {
        return this;
    }

    @Override
    public <C> C readVal(Class<C> clz, String format) {
        return null;
    }

    @Override
    public <C> C readVal(Class<C> clz) {
        return null;
    }

    @Override
    public void writeVal(TypeResolver typeResolver, String format, Object val) {
        //do nothing
    }
}
