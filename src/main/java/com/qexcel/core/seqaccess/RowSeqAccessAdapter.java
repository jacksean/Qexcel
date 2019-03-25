package com.qexcel.core.seqaccess;

import com.qexcel.core.RowAdapter;
import com.qexcel.core.cell.adapter.CellAdapter;
import com.qexcel.core.cell.adapter.CellStyleAdapter;
import com.qexcel.core.enums.CellDataType;
import com.qexcel.core.seqaccess.SheetSeqAccessAdapter.SheetReadAccessAdapter;
import com.qexcel.core.seqaccess.SheetSeqAccessAdapter.SheetWriteAccessAdapter;

/**
 * 类RowSeqAccessAdapter.java的实现描述：行顺序读写访问器（针对列的读写） 
 * @author sean 2018年10月31日 上午11:07:09
 */
public abstract class RowSeqAccessAdapter<T,F> implements RowReqAccess<T,F>{

	private final RowAdapter rowAdapter;
	private final F parant;
	private final Cursor cursor;

	public RowSeqAccessAdapter(F parant,RowAdapter rowAdapter) {
		super();
		this.cursor = new Cursor(0);
		this.parant = parant;
		this.rowAdapter = rowAdapter;
	}
	
	@Override
    public F complete() {
        return getParant();
    }
	
    protected RowAdapter getRowAdapter() {
		return rowAdapter;
	}
	
	protected F getParant() {
		return parant;
	}

	protected Cursor getCursor() {
		return cursor;
	}

	public static class RowSeqWriteAccessAdapter extends RowSeqAccessAdapter<RowWriteAccess,SheetSeqAccessAdapter.SheetWriteAccessAdapter> implements RowWriteAccess{

        public RowSeqWriteAccessAdapter(SheetWriteAccessAdapter parant, RowAdapter rowAdapter) {
            super(parant, rowAdapter);
        }

        @Override
        public RowWriteAccess jumpOne() {
            getCursor().next();
            return this;
        }

        @Override
        public RowWriteAccess jump(int nums) {
            getCursor().jumpNums(nums);
            return this;
        }

        @Override
        public RowWriteAccess jumpTo(int columIndex) {
            getCursor().setIndex(columIndex);
            return this;
        }

        @Override
        public RowWriteAccess createCell(CellStyleAdapter metaAdapter, SpelContext context, Object obj) {
            getRowAdapter().createCell(getCursor().getIndex(), metaAdapter, context, obj);
            getCursor().next();
            return this;
        }

        @Override
        public RowWriteAccess createFormatCell(CellDataType cellDataType, Object val, String format) {
            getRowAdapter().createCell(getCursor().getIndex(), cellDataType, val, format);
            getCursor().next();
            return this;
        }

        @Override
        public RowWriteAccess createCell(CellDataType cellDataType, Object val) {
                return createFormatCell(cellDataType, val, null);
        }
	}
	
	public static class RowSeqReadAccessAdapter extends RowSeqAccessAdapter<RowReadAccess,SheetSeqAccessAdapter.SheetReadAccessAdapter> implements RowReadAccess{

        public RowSeqReadAccessAdapter(SheetReadAccessAdapter parant, RowAdapter rowAdapter) {
            super(parant, rowAdapter);
        }

        @Override
        public RowReadAccess jumpOne() {
            getCursor().next();
            return this;
        }

        @Override
        public RowReadAccess jump(int nums) {
            getCursor().jumpNums(nums);
            return this;
        }

        @Override
        public RowReadAccess jumpTo(int columIndex) {
            getCursor().setIndex(columIndex);
            return this;
        }

        @Override
        public CellAdapter readCell() {
            CellAdapter cellAdapter = getRowAdapter().getCell(getCursor().getIndex());
            getCursor().next();
            return cellAdapter;
        }
        
        @Override
        public boolean isBlank() {
            return getRowAdapter().isBlank();
        }

        @Override
        public RowReadAccess readCellValToObject(CellStyleAdapter meta, Object obj) {
            getRowAdapter().readCellToObject(getCursor().getIndex(), meta, obj);
            getCursor().next();
            return this;
        }
	}
}
