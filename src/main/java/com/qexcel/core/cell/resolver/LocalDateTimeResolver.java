package com.qexcel.core.cell.resolver;

import java.text.MessageFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.qexcel.core.cell.adapter.CellAdapter;
import com.qexcel.core.cell.adapter.DateAdapter;
import com.qexcel.core.cell.adapter.NumberAdapter;
import com.qexcel.core.cell.adapter.StringAdapter;
import com.qexcel.core.enums.CellDataType;

public class LocalDateTimeResolver implements TypeResolver<LocalDateTime>{

    public static final LocalDateTimeResolver INSTANCE = new LocalDateTimeResolver();
    
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    public static LocalDateTime convert(CellAdapter<?> adapter,String format) {
        return INSTANCE.read(adapter, format);
    }
    
    public static LocalDateTime convert(CellAdapter<?> adapter) {
        return convert(adapter, null);
    }
    
    @Override
    public LocalDateTime read(CellAdapter<?> adapter,String format) {
        CellDataType cellType = adapter.getCellType();
        switch(cellType) {
            case BLANK:
                return null;
            case DATE:
                Date d = ((DateAdapter)adapter).getValue();
                if(d != null) {
                    Instant instant = d.toInstant();
                    ZoneId zoneId = ZoneId.systemDefault();
                    return instant.atZone(zoneId).toLocalDateTime();
                }
                return null;
            case STRING:
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern(StringUtils.defaultString(format,DEFAULT_DATETIME_FORMAT));     
                return LocalDateTime.parse(((StringAdapter)adapter).getValue().trim(), dtf);
            case NUMBER:
                return LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(((NumberAdapter)adapter).getValue().longValue()),
                        ZoneId.systemDefault());
            default:
                throw new IllegalStateException(MessageFormat.format("not supported {0} -> LocalDateTime", cellType));
        }
    }

    @Override
    public void write(CellAdapter<?> adapter,LocalDateTime val, String format) {
        CellDataType cellType = adapter.getCellType();
        switch(cellType) {
            case DATE:
                ZoneId zoneId = ZoneId.systemDefault();
                ZonedDateTime zdt = val.atZone(zoneId);
                ((DateAdapter)adapter).setValue(Date.from(zdt.toInstant()));
                break;
            case STRING:
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern(StringUtils.defaultString(format,DEFAULT_DATETIME_FORMAT));
                ((StringAdapter)adapter).setValue(val.format(dtf));
                break;
            case NUMBER:
                ((NumberAdapter)adapter).setValue((double)val.toInstant(ZoneOffset.of("+8")).toEpochMilli());
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
