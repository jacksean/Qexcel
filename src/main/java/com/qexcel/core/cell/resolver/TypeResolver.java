package com.qexcel.core.cell.resolver;

import com.qexcel.core.cell.adapter.CellAdapter;
import com.qexcel.core.enums.CellDataType;

public interface TypeResolver<T> {
    T read(CellAdapter<?> adapter,String format);
    void write(CellAdapter<?> adapter,T val,String format);
    String getDefaultFormat(CellDataType type);
}
