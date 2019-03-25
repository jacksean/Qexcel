package com.qexcel.core.cell.resolver;

import java.text.MessageFormat;
import java.util.Date;

import com.qexcel.core.cell.adapter.CellAdapter;
import com.qexcel.core.cell.adapter.DateAdapter;
import com.qexcel.core.cell.adapter.NumberAdapter;
import com.qexcel.core.cell.adapter.StringAdapter;
import com.qexcel.core.enums.CellDataType;

public class IntegerResolver implements TypeResolver<Integer>{

    public static final IntegerResolver INSTANCE = new IntegerResolver();
    
    public static Integer convert(CellAdapter<?> adapter) {
        return INSTANCE.read(adapter, null);
    }
    
    @Override
    public Integer read(CellAdapter<?> adapter,String format) {
        CellDataType cellType = adapter.getCellType();
        switch(cellType) {
            case BLANK:
                return null;
            case DATE:
                return (int)((DateAdapter)adapter).getValue().getTime();
            case STRING:
                return Integer.valueOf(((StringAdapter)adapter).getValue().trim());
            case NUMBER:
                return ((NumberAdapter)adapter).getValue().intValue();
            default:
                throw new IllegalStateException(MessageFormat.format("not supported {0} -> Integer", cellType));
        }
    }

    @Override
    public void write(CellAdapter<?> adapter,Integer val, String format) {
        CellDataType cellType = adapter.getCellType();
        switch(cellType) {
            case DATE:
                ((DateAdapter)adapter).setValue(new Date(val));
                break;
            case NUMBER:
                ((NumberAdapter)adapter).setValue(val.doubleValue());
                break;
            case STRING:
                ((StringAdapter)adapter).setValue(String.valueOf(val));
                break;
            default:
                return;
        }
    }

    @Override
    public String getDefaultFormat(CellDataType cellType) {
        return null;
    }
}
