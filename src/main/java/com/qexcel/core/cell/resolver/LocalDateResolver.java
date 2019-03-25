package com.qexcel.core.cell.resolver;

import java.text.MessageFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.qexcel.core.cell.adapter.CellAdapter;
import com.qexcel.core.cell.adapter.DateAdapter;
import com.qexcel.core.cell.adapter.StringAdapter;
import com.qexcel.core.enums.CellDataType;

public class LocalDateResolver implements TypeResolver<LocalDate>{

    public static final LocalDateResolver INSTANCE = new LocalDateResolver();
    
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    
    public static LocalDate convert(CellAdapter<?> adapter,String format) {
        return INSTANCE.read(adapter, format);
    }
    
    public static LocalDate convert(CellAdapter<?> adapter) {
        return convert(adapter, null);
    }
    
    @Override
    public LocalDate read(CellAdapter<?> adapter,String format) {
        CellDataType cellType = adapter.getCellType();
        switch(cellType) {
            case BLANK:
                return null;
            case DATE:
                Date d = ((DateAdapter)adapter).getValue();
                if(d != null) {
                    Instant instant = d.toInstant();
                    ZoneId zoneId = ZoneId.systemDefault();
                    return instant.atZone(zoneId).toLocalDate();
                }
                return null;
            case STRING:
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern(StringUtils.defaultString(format,DEFAULT_DATE_FORMAT));     
                return LocalDate.parse(((StringAdapter)adapter).getValue().trim(), dtf);
            default:
                throw new IllegalStateException(MessageFormat.format("not supported {0} -> LocalDate", cellType));
        }
    }

    @Override
    public void write(CellAdapter<?> adapter,LocalDate val, String format) {
        CellDataType cellType = adapter.getCellType();
        switch(cellType) {
            case DATE:
                ZoneId zoneId = ZoneId.systemDefault();
                ZonedDateTime zdt = val.atStartOfDay(zoneId);
                ((DateAdapter)adapter).setValue(Date.from(zdt.toInstant()));
                break;
            case STRING:
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern(StringUtils.defaultString(format,DEFAULT_DATE_FORMAT));
                ((StringAdapter)adapter).setValue(val.format(dtf));
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
                return DEFAULT_DATE_FORMAT;
            default:
                return null;
        }
    }
}
