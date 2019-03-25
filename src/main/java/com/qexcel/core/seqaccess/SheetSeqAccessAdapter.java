package com.qexcel.core.seqaccess;

import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

import com.qexcel.core.ExcelAdapter;
import com.qexcel.core.SheetAdapter;
import com.qexcel.core.seqaccess.RowReqAccess.RowReadAccess;
import com.qexcel.core.seqaccess.RowReqAccess.RowWriteAccess;
import com.qexcel.core.seqaccess.RowSeqAccessAdapter.RowSeqReadAccessAdapter;
import com.qexcel.core.seqaccess.RowSeqAccessAdapter.RowSeqWriteAccessAdapter;
import com.qexcel.core.template.TemplateEngine;
import com.qexcel.core.template.context.SeqaccessContext;

/**
 * 类SheetSeqAccessAdapter.java的实现描述：页顺序读写访问器（针对行的读写） 
 * @author sean 2018年10月31日 上午11:07:27
 */
public abstract class SheetSeqAccessAdapter<T> implements SheetSeqAccess<T>{
	
	private final SheetAdapter self;
	private final Cursor cursor;
	
	public SheetSeqAccessAdapter(SheetAdapter sheetAdapter) {
		super();
		this.self = sheetAdapter;
		this.cursor = new Cursor(0);
	}
	
	@Override
    public ExcelAdapter complete() {
        return getSelf().getExcelAdapter();
    }
	
	protected SheetAdapter getSelf() {
		return self;
	}
	
	protected Cursor getCursor() {
		return cursor;
	}
	
	public static class SheetReadAccessAdapter extends SheetSeqAccessAdapter<SheetReadAccess> implements SheetReadAccess{

        public SheetReadAccessAdapter(SheetAdapter sheetAdapter) {
            super(sheetAdapter);
        }
        
        @Override
        public SheetAdapter getSheet() {
            return getSelf();
        }

        @Override
        public int currentIndex() {
            return getCursor().getIndex();
        }

        @Override
        public SheetReadAccess jump(int rowNums) {
            getCursor().jumpNums(rowNums);
            return this;
        }

        @Override
        public SheetReadAccess jumpTo(int rowIndex) {
            getCursor().setIndex(rowIndex);
            return this;
        }

        @Override
        public ExcelAdapter complete() {
            return getSelf().getExcelAdapter();
        }

        @Override
        public RowReadAccess readRow() {
            RowSeqReadAccessAdapter rowSeq = new RowSeqReadAccessAdapter(this, getSelf().getRow(getCursor().getIndex()));
            getCursor().next();
            return rowSeq;
        }
        
        @Override
        public boolean hasNextRow() {
            return getCursor().getIndex() <= getSelf().getLastRowIndex();
        }
        
        @SuppressWarnings("unchecked")
        @Override
        public SheetReadAccess readToObject(TemplateEngine tplEngine,int rowEndIndex, Object obj) {
            SeqaccessContext<SheetReadAccessAdapter> ctx = SeqaccessContext.build(this).setRowEndIndex(rowEndIndex);
            tplEngine.readToObject(ctx, obj);
            return this;
        }
	}
	
	public static class SheetWriteAccessAdapter extends SheetSeqAccessAdapter<SheetWriteAccess> implements SheetWriteAccess{
	    
	    public SheetWriteAccessAdapter(SheetAdapter sheetAdapter) {
            super(sheetAdapter);
        }
	    
        @Override
        public SheetAdapter getSheet() {
            return getSelf();
        }

        @Override
        public int currentIndex() {
            return getCursor().getIndex();
        }

        @Override
        public SheetWriteAccess jump(int rowNums) {
            getCursor().jumpNums(rowNums);
            return this;
        }

        @Override
        public SheetWriteAccess jumpTo(int rowIndex) {
            getCursor().setIndex(rowIndex);
            return this;
        }
        
        @Override
        public RowWriteAccess createRow() {
            RowSeqWriteAccessAdapter rowSeq = new RowSeqWriteAccessAdapter(this, getSelf().createRow(getCursor().getIndex()));
            getCursor().next();
            return rowSeq;
        }
        
        @Override
        public SheetWriteAccess writeToWorkBook(TemplateEngine tplEngine, Object obj) {
            return this.writeToWorkBook(tplEngine, null, obj);
        }

        @Override
        public SheetWriteAccess writeToWorkBook(TemplateEngine tplEngine,Map<String,Object> variables,Object obj) {
            SeqaccessContext<SheetWriteAccessAdapter> ctx = SeqaccessContext.build(this);
            ctx.addParams(variables);
            tplEngine.writeToWb(ctx, obj);
            return this;
        }

        @Override
        public SheetWriteAccess writeToWorkBook(TemplateEngine tplEngine, int initStatus,
                                                BiPredicate<Integer,Consumer<List>> appender) {
           return this.writeToWorkBook(tplEngine, null, initStatus, appender);
        }

        @Override
        public SheetWriteAccess writeToWorkBook(TemplateEngine tplEngine, Map<String, Object> variables, int initStatus,
                                                BiPredicate<Integer,Consumer<List>> appender) {
            SeqaccessContext<SheetWriteAccessAdapter> ctx = SeqaccessContext.build(this);
            ctx.addParams(variables);
            tplEngine.writeToWb(ctx, initStatus, appender);
            return this;
        }
	}
}
