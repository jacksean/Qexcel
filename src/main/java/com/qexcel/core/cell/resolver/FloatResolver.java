package com.qexcel.core.cell.resolver;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.qexcel.core.cell.adapter.CellAdapter;
import com.qexcel.core.cell.adapter.DateAdapter;
import com.qexcel.core.cell.adapter.NumberAdapter;
import com.qexcel.core.cell.adapter.StringAdapter;
import com.qexcel.core.enums.CellDataType;

public class FloatResolver implements TypeResolver<Float>{

    public static final FloatResolver INSTANCE = new FloatResolver();
    
    public static final String DEFAULT_NUMBER_FORMAT = "#.00";
    
    public static Float convert(CellAdapter<?> adapter,String format) {
        return INSTANCE.read(adapter, format);
    }
    
    public static Float convert(CellAdapter<?> adapter) {
        return convert(adapter, null);
    }
    
    @Override
    public Float read(CellAdapter<?> adapter,String format) {
        CellDataType cellType = adapter.getCellType();
        switch(cellType) {
            case BLANK:
                return null;
            case DATE:
                return (float)((DateAdapter)adapter).getValue().getTime();
            case STRING:
                return Float.valueOf(((StringAdapter)adapter).getValue().trim());
            case NUMBER:
                return ((NumberAdapter)adapter).getValue().floatValue();
            default:
                throw new IllegalStateException(MessageFormat.format("not supported {0} -> Float", cellType));
        }
    }

    @Override
    public void write(CellAdapter<?> adapter,Float val, String format) {
        CellDataType cellType = adapter.getCellType();
        switch(cellType) {
            case DATE:
                ((DateAdapter)adapter).setValue(new Date(val.longValue()));
                break;
            case NUMBER:
                ((NumberAdapter)adapter).setValue(val.doubleValue());
                break;
            case STRING:
                DecimalFormat df = new DecimalFormat(StringUtils.defaultString(format,DEFAULT_NUMBER_FORMAT));
                ((StringAdapter)adapter).setValue(df.format(val));
                break;
            default:
                return;
        }
    }

    @Override
    public String getDefaultFormat(CellDataType cellType) {
        switch(cellType) {
            case NUMBER:
            case STRING:
                return DEFAULT_NUMBER_FORMAT;
            default:
                return null;
        }
    }
}
