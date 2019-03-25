package com.qexcel.core.cell.resolver;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;

import com.qexcel.core.cell.adapter.BooleanAdapter;
import com.qexcel.core.cell.adapter.CellAdapter;
import com.qexcel.core.cell.adapter.DateAdapter;
import com.qexcel.core.cell.adapter.FormulaAdapter;
import com.qexcel.core.cell.adapter.NumberAdapter;
import com.qexcel.core.cell.adapter.StringAdapter;
import com.qexcel.core.enums.CellDataType;

public class StringResolver implements TypeResolver<String>{
    
    public static final StringResolver INSTANCE = new StringResolver();
    
    public static String convert(CellAdapter<?> adapter,String format) {
        return INSTANCE.read(adapter, format);
    }
    
    public static String convert(CellAdapter<?> adapter) {
        return convert(adapter, null);
    }
    
    @Override
    public String read(CellAdapter<?> adapter,String format) {
        CellDataType cellType = adapter.getCellType();
        switch(cellType) {
            case BLANK:
                return null;
            case DATE:
                SimpleDateFormat sdf = new SimpleDateFormat(StringUtils.defaultString(format,DateResolver.DEFAULT_DATETIME_FORMAT));
                return sdf.format(((DateAdapter)adapter).getValue());
            case STRING:
                return ((StringAdapter)adapter).getValue();
            case NUMBER:
                DecimalFormat df = new DecimalFormat(StringUtils.defaultString(format,DoubleResolver.DEFAULT_NUMBER_FORMAT));
                return df.format(((NumberAdapter)adapter).getValue());
            case BOOLEAN:
                return String.valueOf(((BooleanAdapter)adapter).getValue());
            default:
                throw new IllegalStateException(MessageFormat.format("not supported {0} -> String", cellType));
        }
    }

    @Override
    public void write(CellAdapter<?> adapter,String val, String format) {
        CellDataType cellType = adapter.getCellType();
        switch(cellType) {
            case DATE:
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat(StringUtils.defaultString(format,DateResolver.DEFAULT_DATETIME_FORMAT));
                    ((DateAdapter)adapter).setValue(sdf.parse(val));
                }catch (Exception e) {}
                break;
            case STRING:
                ((StringAdapter)adapter).setValue(val);
                break;
            case NUMBER:
                ((NumberAdapter)adapter).setValue(Double.valueOf(val));
                break;
            case BOOLEAN:
                ((BooleanAdapter)adapter).setValue(Boolean.valueOf(val));
                break;
            case FORMULA:
                ((FormulaAdapter)adapter).setValue(val);
                break;
            default:
                return;
        }
    }
    
    @Override
    public String getDefaultFormat(CellDataType cellType) {
        switch(cellType) {
            case DATE:
                return DateResolver.DEFAULT_DATETIME_FORMAT;
            default:
                return null;
        }
    }
}
