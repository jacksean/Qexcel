package com.qexcel.core.cell.resolver;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.qexcel.core.cell.adapter.CellAdapter;
import com.qexcel.core.cell.adapter.DateAdapter;
import com.qexcel.core.cell.adapter.NumberAdapter;
import com.qexcel.core.cell.adapter.StringAdapter;
import com.qexcel.core.enums.CellDataType;

public class DateResolver implements TypeResolver<Date>{
    
    public static final DateResolver INSTANCE = new DateResolver();
    
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static Date convert(CellAdapter<?> adapter,String format) {
        return INSTANCE.read(adapter, format);
    }
    
    public static Date convert(CellAdapter<?> adapter) {
        return convert(adapter, null);
    }
    
    @Override
    public Date read(CellAdapter<?> adapter,String format) {
        CellDataType cellType = adapter.getCellType();
        switch(cellType) {
            case BLANK:
                return null;
            case DATE:
                return ((DateAdapter)adapter).getValue();
            case STRING:
                try {
                    return new SimpleDateFormat(StringUtils.defaultString(format,DEFAULT_DATETIME_FORMAT))
                            .parse(((StringAdapter)adapter).getValue().trim());
                } catch (ParseException e) {
                    return null;
                }
            case NUMBER:
                return new Date(((NumberAdapter)adapter).getValue().longValue());
            default:
                throw new IllegalStateException(MessageFormat.format("not supported {0} -> Date", cellType));
        }
    }

    @Override
    public void write(CellAdapter<?> adapter,Date val, String format) {
        CellDataType cellType = adapter.getCellType();
        switch(cellType) {
            case DATE:
                ((DateAdapter)adapter).setValue(val);
                break;
            case NUMBER:
                ((NumberAdapter)adapter).setValue((double)val.getTime());
                break;
            case STRING:
                SimpleDateFormat sdf = new SimpleDateFormat(StringUtils.defaultString(format,DEFAULT_DATETIME_FORMAT));
                ((StringAdapter)adapter).setValue(sdf.format(val));
                break;
            default:
                return;
        }
    }

    @Override
    public String getDefaultFormat(CellDataType cellType) {
        switch(cellType) {
            case DATE:
            case STRING:
                return DEFAULT_DATETIME_FORMAT;
            default:
                return null;
        }
    }
}
