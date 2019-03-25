package com.qexcel.core;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

import com.qexcel.core.cell.adapter.CellAdapter;
import com.qexcel.core.cell.adapter.CellStyleAdapter;
import com.qexcel.core.enums.CellDataType;
import com.qexcel.core.enums.HorizontalAlign;
import com.qexcel.core.enums.VerticalAlign;
import com.qexcel.core.template.TemplateEngine;

/**
 * 类ExcelAdapter.java的实现描述：工作簿装饰器
 * 
 * @author sean 2018年10月31日 上午9:24:38
 */
public class ExcelAdapter {

    /**
     * 缓存style xls设置又限制,不能超过4000个
     */
    private Map<String, CellStyle> cacheStyle = new HashMap<>();
    private Map<String, Font>      cacheFont  = new HashMap<>();

    private final Workbook         wb;
    
    private final FormulaEvaluator evaluator;
    
    private PropertyInterceptor propertyInterceptor;
    
    public ExcelAdapter(Workbook wb) {
        this.wb = wb;
        this.evaluator = wb.getCreationHelper().createFormulaEvaluator();
    }
    
    /**
     * 获取sheet装饰器
     * 
     * @param sheetIndex sheet索引 从0开始
     * @return
     */
    public SheetAdapter getSheet(int sheetIndex) {
        return new SheetAdapter(getWb().getSheetAt(sheetIndex), sheetIndex, this);
    }

    public RowAdapter getRow(int sheetIndex, int rowIndex) {
        return getSheet(sheetIndex).getRow(rowIndex);
    }

    public CellAdapter getCell(int sheetIndex, int rowIndex, int columIndex) {
        return getSheet(sheetIndex).getRow(rowIndex).getCell(columIndex);
    }

    /**
     * 获取sheet装饰器
     * 
     * @param sheetName sheet名称
     * @return
     */
    public SheetAdapter getSheet(String sheetName) {
        int sheetIndex = getWb().getSheetIndex(sheetName);
        return new SheetAdapter(getWb().getSheetAt(sheetIndex), sheetIndex, this);
    }

    /**
     * 创建sheet
     * 
     * @param sheetname sheet名称
     * @return
     */
    public SheetAdapter createSheet(String sheetname) {
        Sheet sheet = getWb().createSheet(sheetname);
        int sheetIndex = getWb().getSheetIndex(sheet);
        return new SheetAdapter(sheet, sheetIndex, this);
    }

    public RowAdapter createRow(int sheetIndex, int rowIndex) {
        SheetAdapter sheet = getSheet(sheetIndex);
        return sheet.createRow(rowIndex);
    }

    public CellAdapter createCell(int sheetIndex, int rowIndex, int columIndex, CellDataType cellDataType) {
        RowAdapter row = getRow(sheetIndex, rowIndex);
        return row.createCell(columIndex, cellDataType);
    }
    
    public <R> List<R> readJavaObjList(TemplateEngine tplEngine, String sheetName, int rowStartIndex,
                                       Class<R> javaObjClz) {
        return readJavaObjList(tplEngine, sheetName, rowStartIndex, -1, javaObjClz);
    }

    public <R> List<R> readJavaObjList(TemplateEngine tplEngine, String sheetName, int rowStartIndex, int rowEndIndex,
                                       Class<R> javaObjClz) {
        return getSheet(sheetName).readJavaObjList(tplEngine, rowStartIndex, rowEndIndex, javaObjClz);
    }
    
    public <R> void readJavaObjList(TemplateEngine tplEngine, String sheetName,int rowStartIndex, Class<R> objClz, int batchSize,
                                    Consumer<List<R>> sink) {
        readJavaObjList(tplEngine, sheetName, rowStartIndex, -1, objClz, batchSize, sink);
    }
    
    public <R> void readJavaObjList(TemplateEngine tplEngine, String sheetName,int rowStartIndex, int rowEndIndex, Class<R> objClz, int batchSize,
                                    Consumer<List<R>> sink) {
        getSheet(sheetName).readJavaObjList(tplEngine, rowStartIndex, objClz, batchSize, sink);
    }

    public <R> R readJavaObj(TemplateEngine tplEngine, String sheetName, int rowStartIndex, Class<R> javaObjClz) {
        R javaObj = null;
        try {
            javaObj = javaObjClz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        readToJavaObj(tplEngine, sheetName, rowStartIndex, javaObj);
        return javaObj;
    }

    public void readToJavaObj(TemplateEngine tplEngine, String sheetName, int rowStartIndex, Object javaObj) {
        getSheet(sheetName).readToJavaObj(tplEngine, rowStartIndex, javaObj);
    }

    public void readToJavaObj(TemplateEngine tplEngine, int sheetIndex, int rowStartIndex, Object javaObj) {
        getSheet(sheetIndex).readToJavaObj(tplEngine, rowStartIndex, javaObj);
    }

    public void writeToWorkBook(TemplateEngine tplEngine, Map<String, Object> variables, String sheetName,
                                int rowStartIndex, Object javaObj) {
        getSheet(sheetName).writeToWorkBook(tplEngine, variables, rowStartIndex, javaObj);
    }

    public void writeToWorkBook(TemplateEngine tplEngine, Map<String, Object> variables, String sheetName,
                                Object javaObj) {
        this.writeToWorkBook(tplEngine, variables, sheetName, 0, javaObj);
    }

    public void writeToWorkBook(TemplateEngine tplEngine, Map<String, Object> variables, int sheetIndex,
                                Object javaObj) {
        getSheet(sheetIndex).writeToWorkBook(tplEngine, variables, javaObj);
    }

    public void writeToWorkBook(TemplateEngine tplEngine, int sheetIndex, int initStatus,
                                BiPredicate<Integer,Consumer<List>> appender) {
        this.writeToWorkBook(tplEngine, null, sheetIndex, initStatus, appender);
    }

    public void writeToWorkBook(TemplateEngine tplEngine, Map<String, Object> variables, int sheetIndex, int initStatus,
                                BiPredicate<Integer,Consumer<List>> appender) {
        getSheet(sheetIndex).writeToWorkBook(tplEngine, variables, initStatus, appender);
    }

    /**
     * 输出到流
     * 
     * @param os
     * @throws IOException
     */
    public void flushTo(OutputStream os) throws IOException {
        wb.write(os);
    }

    /**
     * 输出到文件
     * 
     * @param file
     * @throws IOException
     */
    public void flushTo(String file) throws IOException {
        try (OutputStream os = new FileOutputStream(file)) {
            flushTo(os);
        }
    }

    public Workbook getWb() {
        return wb;
    }

    public CellStyle createCellStyle(CellStyleAdapter meta) {
        return cacheStyle.computeIfAbsent(meta.styleHashCode(), k -> {
            CellStyle newStyle = wb.createCellStyle();
            if (null != meta.gethAlign() && !meta.gethAlign().equals(HorizontalAlign.DEFAULT))
                newStyle.setAlignment(HorizontalAlignment.valueOf(meta.gethAlign().name()));
            if (null != meta.getvAlign() && !meta.getvAlign().equals(VerticalAlign.DEFAULT))
                newStyle.setVerticalAlignment(VerticalAlignment.valueOf(meta.getvAlign().name()));
            return newStyle;
        });
    }

    public Font createFont(CellStyleAdapter meta) {
        return cacheFont.computeIfAbsent(meta.fontHashCode(), k -> {
            Font font = wb.createFont();
            if (StringUtils.isNotBlank(meta.getFont()))
                font.setFontName(meta.getFont());
            if (meta.getFontSize() > 0)
                font.setFontHeightInPoints(meta.getFontSize());//设置字体大小
            if (null != meta.getFontColor())
                font.setColor(meta.getFontColor().getIndex());
            font.setBold(meta.isFontBold());
            return font;
        });
    }

    public DataFormat createDataFormat() {
        return wb.createDataFormat();
    }

    public FormulaEvaluator getEvaluator() {
        return evaluator;
    }
    
    public void setPropertyInterceptor(PropertyInterceptor propertyInterceptor) {
        this.propertyInterceptor = propertyInterceptor;
    }
    
    public PropertyInterceptor getPropertyInterceptor() {
        return propertyInterceptor;
    }

    public static interface PropertyInterceptor{
        default Object getPropertyValue(Object obj,Field f) throws IllegalArgumentException, IllegalAccessException  {
            return f.get(obj);
        }
        
        default void setPropertyValue(Object obj,Field f,Object val) throws IllegalArgumentException, IllegalAccessException {
            if(val != null)
                f.set(obj,val);
        }
    }
}
