package com.qexcel.core;

import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.qexcel.core.cell.adapter.CellAdapter;
import com.qexcel.core.cell.adapter.CellAdapterFactory;
import com.qexcel.core.cell.adapter.CellStyleAdapter;
import com.qexcel.core.cell.resolver.TypeResolver;
import com.qexcel.core.cell.resolver.TypeResolverFactory;
import com.qexcel.core.enums.CellDataType;
import com.qexcel.core.seqaccess.SpelContext;
import com.qexcel.util.ReflectUtil;
import com.qexcel.util.SpelUtil;

/**
 * 类RowAdapter.java的实现描述：行包装器 
 * @author sean 2018年10月31日 上午10:47:38
 */
public class RowAdapter {
	private final int selfIndex;
	private final SheetAdapter sheetAdapter;
	private Row row;
	
	public RowAdapter(int selfIndex, SheetAdapter sheetAdapter, Row row) {
		super();
		this.selfIndex = selfIndex;
		this.sheetAdapter = sheetAdapter;
		this.row = row;
	}
	
	/**
	 * 获取指定列单元格
	 * @param columIndex 列索引 从0开始
	 * @return
	 */
	@SuppressWarnings("rawtypes")
    public CellAdapter getCell(int columIndex) {
	    if(!isExistRow()) {
	        RowAdapter ra = sheetAdapter.createRow(getSelfIndex());
	        this.row = ra.getRow();
	    }
		Cell cell = getRow().getCell(columIndex);
		return CellAdapterFactory.buildWithCell(this,cell);
	}
	
	/**
	 * 在指定列索引处创建单元格
	 * @param columIndex 列索引 从0开始
	 * @param cellDataType 单元格类型
	 * @return
	 */
	@SuppressWarnings("rawtypes")
    public CellAdapter createCell(int columIndex,CellDataType cellDataType) {
	    if(!isExistRow()) {
            RowAdapter ra = sheetAdapter.createRow(getSelfIndex());
            this.row = ra.getRow();
        }
		Cell cell = getRow().createCell(columIndex,cellDataType.getCellType());
		return CellAdapterFactory.buildWithType(this,cellDataType,cell);
	}
	
	@SuppressWarnings({"rawtypes" })
    public CellAdapter createCell(int columIndex,CellDataType cellDataType, Object val, String format) {
        CellAdapter cellAdapter=null;
        if(val == null) {
            cellAdapter = createCell(columIndex,CellDataType.BLANK);
        }else {
            cellAdapter = createCell(columIndex,cellDataType);
            TypeResolver typeResolver = TypeResolverFactory.getTypeResolver(val.getClass());
            if(typeResolver != null) {
                String fixFormat = fixFormat(cellAdapter.getCellType(), typeResolver, format);
                cellAdapter.writeVal(typeResolver, fixFormat, val);
                if(StringUtils.isNotBlank(fixFormat)) {
                    CellStyleAdapter meta = new CellStyleAdapter();
                    meta.setFormat(fixFormat);
                    cellAdapter.setStyle(meta);
                }
            }
        }
        return cellAdapter;
    }
	
	@SuppressWarnings({ "rawtypes"})
    public CellAdapter createCell(int columIndex,CellStyleAdapter metaAdapter,SpelContext context,Object obj) {
        try {
            CellAdapter cellAdapter = null;
            Object val = null;
            if(StringUtils.isNotBlank(metaAdapter.getProperty()) 
                    && !CellDataType.FORMULA.equals(metaAdapter.getCellDataType())) {
                if(StringUtils.isBlank(metaAdapter.getWriteSpel())){
                    val = getPropertyValue(metaAdapter.getProperty(), obj);
                }else {
                    val = SpelUtil.parse(metaAdapter.getWriteSpel(), context.getRootObject(), context.getVariables());
                }
            }else {
                val = SpelUtil.parse(metaAdapter.getText(), context.getRootObject(), context.getVariables(),String.class);
            }
            if(!isExistRow()) {
                RowAdapter ra = sheetAdapter.createRow(getSelfIndex());
                this.row = ra.getRow();
            }
            if(val == null) {
                cellAdapter = createCell(columIndex,CellDataType.BLANK);
            }else {
                cellAdapter =  createCell(columIndex,metaAdapter.getDataType());
                TypeResolver typeResolver = TypeResolverFactory.getTypeResolver(val.getClass());
                if(typeResolver != null) {
                    String fixFormat = fixFormat(cellAdapter.getCellType(), typeResolver, metaAdapter.getFormat());
                    cellAdapter.writeVal(typeResolver, fixFormat, val);
                    metaAdapter.setFormat(fixFormat);
                    cellAdapter.setStyle(metaAdapter);
                }
            }
            return cellAdapter;
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
	
	public void readCellToObject(int columIndex,CellStyleAdapter meta, Object obj) {
        CellAdapter cellAdapter =this.getCell(columIndex);
        if(StringUtils.isNotBlank(meta.getProperty())) {
            try {
                Field f = ReflectUtil.getField(obj, meta.getProperty());
                TypeResolver<?> typeResolver = TypeResolverFactory.getTypeResolver(f.getType());
                if(typeResolver != null) {
                    Object val = typeResolver.read(cellAdapter, meta.getFormat());
                    setPropertyValue(f, obj, val);
                }
            }catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 获取对象的属性值，提供可由外部改变写入值的入口
     * @param metaAdapter
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    protected Object getPropertyValue(String property, Object obj) throws IllegalAccessException {
        Object val;
        Field field = ReflectUtil.getField(obj, property);
        if(getSheetAdapter().getExcelAdapter().getPropertyInterceptor() != null) {
            val = getSheetAdapter().getExcelAdapter().getPropertyInterceptor().getPropertyValue(obj, field);
        }else {
            val = field.get(obj);
        }
        return val;
    }
    
    protected void setPropertyValue(Field field,Object obj,Object propertyValue) throws IllegalAccessException {
        if(getSheetAdapter().getExcelAdapter().getPropertyInterceptor() != null) {
            getSheetAdapter().getExcelAdapter().getPropertyInterceptor().setPropertyValue(obj, field, propertyValue);
        }else {
            if(propertyValue != null) {
                field.set(obj, propertyValue);
            }
        }
    }
    
    protected String fixFormat(CellDataType cellType,TypeResolver typeResolver,String format) {
        return StringUtils.isNotBlank(format)?format : typeResolver.getDefaultFormat(cellType);
    }
    
	/**
	 * 是否为空行
	 * @return
	 */
	public boolean isBlank() {
		return !isExistRow() || getRow().getPhysicalNumberOfCells()<=0;
	}
	
	public boolean isExistRow() {
	    return getRow() != null;
	}
	
	/**
	 * 是否是最后一行
	 * @return
	 */
	public boolean isLastRow() {
		return getSheetAdapter().getLastRowIndex() == getSelfIndex();
	}
	
	/**
	 * 获取当前行索引
	 * @return
	 */
	public int getSelfIndex() {
		return selfIndex;
	}
	public SheetAdapter getSheetAdapter() {
		return sheetAdapter;
	}
	public Row getRow() {
		return row;
	}
}
