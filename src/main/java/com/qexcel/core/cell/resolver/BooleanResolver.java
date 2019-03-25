package com.qexcel.core.cell.resolver;

import java.text.MessageFormat;

import com.qexcel.core.cell.adapter.BooleanAdapter;
import com.qexcel.core.cell.adapter.CellAdapter;
import com.qexcel.core.enums.CellDataType;

public class BooleanResolver implements TypeResolver<Boolean>{

    public static final BooleanResolver INSTANCE = new BooleanResolver();
    
    public static Boolean convert(CellAdapter<?> adapter) {
        return INSTANCE.read(adapter, null);
    }
    
    @Override
    public Boolean read(CellAdapter<?> adapter,String format) {
        CellDataType cellType = adapter.getCellType();
        switch(cellType) {
            case BLANK:
                return Boolean.FALSE;
            case BOOLEAN:
                return ((BooleanAdapter)adapter).getValue();
            default:
                throw new IllegalStateException(MessageFormat.format("not supported {0} -> Boolean", cellType));
        }
    }

    @Override
    public void write(CellAdapter<?> adapter,Boolean val, String format) {
        CellDataType cellType = adapter.getCellType();
        switch(cellType) {
            case BOOLEAN:
                ((BooleanAdapter)adapter).setValue(val);
                break;
            default:
                return;
        }
    }

    @Override
    public String getDefaultFormat(CellDataType type) {
        return null;
    }
}
