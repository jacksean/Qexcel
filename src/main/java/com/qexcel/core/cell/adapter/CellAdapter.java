package com.qexcel.core.cell.adapter;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qexcel.core.ExcelAdapter;
import com.qexcel.core.RowAdapter;
import com.qexcel.core.cell.resolver.TypeResolver;
import com.qexcel.core.cell.resolver.TypeResolverFactory;
import com.qexcel.core.enums.CellDataType;

/**
 * 类CellAdapter.java的实现描述：单元格装饰器,每个实现类对应excel中cell的一个类型 
 * @author sean 2018年10月31日 上午10:52:55
 */
public abstract class CellAdapter<T> {
	
    static Logger logger = LoggerFactory.getLogger(CellAdapter.class);
    
	private final RowAdapter rowAdapter;
	private final Cell cell;
	
	public CellAdapter(RowAdapter rowAdapter,Cell cell) {
		super();
		this.rowAdapter = rowAdapter;
		this.cell = cell;
	}
	
	public abstract CellDataType getCellType();
	
	public abstract T getValue();
	
	public abstract void setValue(T value);
	
	/**
     * 设置样式/格式
     * @param meta
     * @return
     */
    public CellAdapter setStyle(CellStyleAdapter meta) {
        if(null == meta)
            return this;
        ExcelAdapter wb = getRowAdapter().getSheetAdapter().getExcelAdapter();
        CellStyle cs = wb.createCellStyle(meta);
        Font font = wb.createFont(meta);
        cs.setFont(font);
        if(StringUtils.isNotBlank(meta.getFormat()) && !CellDataType.STRING.equals(getCellType())) {
            DataFormat df = wb.createDataFormat();
            cs.setDataFormat(df.getFormat(meta.getFormat()));
        }
        if(meta.getWidth() > 0)
            getCell().getSheet().setColumnWidth(getCell().getColumnIndex(), 256*meta.getWidth()+184);
        getCell().setCellStyle(cs);
        return this;
    }
    
    @SuppressWarnings({ "unchecked"})
    public <C> C readVal(Class<C> clz,String format) {
        TypeResolver<?> typeResolver = TypeResolverFactory.getTypeResolver(clz);
        if(typeResolver == null)
            return null;
        return (C) typeResolver.read(this, format);
    }
    
    public <C> C readVal(Class<C> clz) {
        return readVal(clz, null);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void writeVal(TypeResolver typeResolver,String format,Object val) {
        if(typeResolver != null)
            typeResolver.write(this, val, format);
    }
    
	public Cell getCell() {
		return cell;
	}
	public RowAdapter getRowAdapter() {
		return rowAdapter;
	}
}
