package com.qexcel.core;

import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import com.qexcel.core.cell.adapter.CellAdapter;
import com.qexcel.core.cell.adapter.CellStyleAdapter;
import com.qexcel.core.enums.CellDataType;
import com.qexcel.core.seqaccess.SheetSeqAccessAdapter;
import com.qexcel.core.seqaccess.SpelContext;
import com.qexcel.core.seqaccess.SheetSeqAccess.SheetReadAccess;
import com.qexcel.core.seqaccess.SheetSeqAccess.SheetWriteAccess;
import com.qexcel.core.template.TemplateEngine;
import com.qexcel.core.template.context.SeqaccessContext;

/**
 * 类SheetAdapter.java的实现描述：页装饰器 
 * @author sean 2018年10月31日 上午10:50:30
 */
public class SheetAdapter {
	private final int selfIndex;
	private final ExcelAdapter excelAdapter;
	private final Sheet sheet;
	
	public SheetAdapter(Sheet sheet,int selfIndex, ExcelAdapter excelAdapter) {
		super();
		this.sheet = sheet;
		this.selfIndex = selfIndex;
		this.excelAdapter = excelAdapter;
	}
	
	/**
	 * 获取指定索引的row
	 * @param rowIndex 行索引 从0开始
	 * @return
	 */
	public RowAdapter getRow(int rowIndex) {
		Row row = getSheet().getRow(rowIndex);
		return new RowAdapter(rowIndex, this, row);
	}
	
	/**
	 * 获取cell
	 * @param rowIndex
	 * @param columIndex
	 * @return
	 */
	public CellAdapter getCell(int rowIndex,int columIndex) {
        return getRow(rowIndex).getCell(columIndex);
    }
	
	/**
	 * 在指定索引处创建row
	 * @param rowIndex 行索引 从0开始
	 * @return
	 */
	public RowAdapter createRow(int rowIndex) {
		Row row = getSheet().createRow(rowIndex);
		return new RowAdapter(rowIndex, this, row);
	}
	
    public CellAdapter createCell(int rowIndex,int columIndex,CellDataType cellDataType) {
        return getRow(rowIndex).createCell(columIndex, cellDataType);
    }
    
    public void createMergedRegion(int firstRow, int lastRow, int firstCol, int lastCol) {
        getSheet().addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
    }
    
    public CellAdapter createCell(int rowIndex,int columIndex,CellStyleAdapter metaAdapter,SpelContext context,Object obj) {
        return getRow(rowIndex).createCell(columIndex, metaAdapter, context, obj);
    }
	
	/**
	 * 开启顺序读工具
	 * @return
	 */
	public SheetReadAccess openReadSequential() {
		return new SheetSeqAccessAdapter.SheetReadAccessAdapter(this);
	}
	
	/**
	 * 开启顺序写工具
	 * @return
	 */
	public SheetWriteAccess openWriteSequential() {
		return new SheetSeqAccessAdapter.SheetWriteAccessAdapter(this);
	}
	
	public <R> List<R> readJavaObjList(TemplateEngine tplEngine, String sheetName, int rowStartIndex,
                                       Class<R> javaObjClz) {
        return readJavaObjList(tplEngine, rowStartIndex, -1, javaObjClz);
    }

    public <R> List<R> readJavaObjList(TemplateEngine tplEngine,int rowStartIndex, int rowEndIndex,
                                       Class<R> javaObjClz) {
        int fixStartRow = rowStartIndex < 0 ? 0 : rowStartIndex;
        SheetReadAccess sra = openReadSequential().jumpTo(fixStartRow);
        SeqaccessContext<SheetReadAccess> ctx = SeqaccessContext.build(sra);
        ctx.setRowEndIndex(rowEndIndex);
        return tplEngine.readObjectList(ctx, javaObjClz);
    }
    
    public <R> void readJavaObjList(TemplateEngine tplEngine,int rowStartIndex, Class<R> objClz, int batchSize,
                                    Consumer<List<R>> sink) {
        readJavaObjList(tplEngine, rowStartIndex, -1, objClz, batchSize, sink);
    }
    
    public <R> void readJavaObjList(TemplateEngine tplEngine,int rowStartIndex, int rowEndIndex, Class<R> objClz, int batchSize,
                                    Consumer<List<R>> sink) {
        int fixStartRow = rowStartIndex < 0 ? 0 : rowStartIndex;
        SheetReadAccess sra = openReadSequential().jumpTo(fixStartRow);
        SeqaccessContext<SheetReadAccess> ctx = SeqaccessContext.build(sra);
        ctx.setRowEndIndex(rowEndIndex);
        tplEngine.readObjectList(ctx, objClz, batchSize, sink);
    }

    public <R> R readJavaObj(TemplateEngine tplEngine, int rowStartIndex, Class<R> javaObjClz) {
        R javaObj = null;
        try {
            javaObj = javaObjClz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        readToJavaObj(tplEngine, rowStartIndex, javaObj);
        return javaObj;
    }

    public void readToJavaObj(TemplateEngine tplEngine , int rowStartIndex, Object javaObj) {
        int fixStartIndex = fixRowIndex(rowStartIndex);
        SheetReadAccess sra = openReadSequential();
        SeqaccessContext<SheetReadAccess> ctx = SeqaccessContext.build(sra);
        if (fixStartIndex > 0) {
            sra.jumpTo(fixStartIndex);
        }
        tplEngine.readToObject(ctx, javaObj);
    }

    public void writeToWorkBook(TemplateEngine tplEngine, Map<String, Object> variables,
                                int rowStartIndex, Object javaObj) {
        int fixStartIndex = fixRowIndex(rowStartIndex);
        SheetWriteAccess swa = openWriteSequential();
        if (fixStartIndex > 0)
            swa.jumpTo(fixStartIndex);
        SeqaccessContext<SheetWriteAccess> ctx = SeqaccessContext.build(swa);
        ctx.addParams(variables);
        tplEngine.writeToWb(ctx, javaObj);
    }

    public void writeToWorkBook(TemplateEngine tplEngine, Map<String, Object> variables,
                                Object javaObj) {
        this.writeToWorkBook(tplEngine, variables, 0, javaObj);
    }

    public void writeToWorkBook(TemplateEngine tplEngine, int initStatus,
                                BiPredicate<Integer,Consumer<List>> appender) {
        this.writeToWorkBook(tplEngine, null, initStatus, appender);
    }

    public void writeToWorkBook(TemplateEngine tplEngine, Map<String, Object> variables, int initStatus,
                                BiPredicate<Integer,Consumer<List>> appender) {
        SheetWriteAccess swa = openWriteSequential();
        SeqaccessContext<SheetWriteAccess> ctx = SeqaccessContext.build(swa);
        ctx.addParams(variables);
        tplEngine.writeToWb(ctx, initStatus, appender);
    }

    /**
     * 修正index
     * @param index
     * @return
     */
    private int fixRowIndex(int index) {
        return index < 0 ? 0 : index;
    }
	
	/**
	 * 获取最后一行的索引
	 * @return
	 */
	public int getLastRowIndex() {
		return getSheet().getLastRowNum();
	}
	
	/**
	 * 当前sheet的索引
	 * @return
	 */
	public int getSelfIndex() {
		return selfIndex;
	}

	public ExcelAdapter getExcelAdapter() {
		return excelAdapter;
	}

	public Sheet getSheet() {
		return sheet;
	}
}
