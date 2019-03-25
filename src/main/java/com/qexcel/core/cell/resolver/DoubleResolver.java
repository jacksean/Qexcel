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

public class DoubleResolver implements TypeResolver<Double>{
    
    public static final DoubleResolver INSTANCE = new DoubleResolver();
    
    public static final String DEFAULT_NUMBER_FORMAT = "#.00";

    public static Double convert(CellAdapter<?> adapter,String format) {
        return INSTANCE.read(adapter, format);
    }
    
    public static Double convert(CellAdapter<?> adapter) {
        return convert(adapter, null);
    }
    
    @Override
    public Double read(CellAdapter<?> adapter,String format) {
        CellDataType cellType = adapter.getCellType();
        switch(cellType) {
            case BLANK:
                return null;
            case DATE:
                return (double)((DateAdapter)adapter).getValue().getTime();
            case STRING:
                return Double.valueOf(((StringAdapter)adapter).getValue().trim());
            case NUMBER:
                return ((NumberAdapter)adapter).getValue();
            default:
                throw new IllegalStateException(MessageFormat.format("not supported {0} -> Double", cellType));
        }
    }

    @Override
    public void write(CellAdapter<?> adapter,Double val, String format) {
        CellDataType cellType = adapter.getCellType();
        switch(cellType) {
            case DATE:
                ((DateAdapter)adapter).setValue(new Date(val.longValue()));
                break;
            case NUMBER:
                ((NumberAdapter)adapter).setValue(val);
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
