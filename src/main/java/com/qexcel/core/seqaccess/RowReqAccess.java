package com.qexcel.core.seqaccess;

import com.qexcel.core.cell.adapter.CellAdapter;
import com.qexcel.core.cell.adapter.CellStyleAdapter;
import com.qexcel.core.enums.CellDataType;

/**
 * 类RowAccess.java的实现描述：行访问器 
 * @author sean 2018年10月31日 上午11:03:12
 */
public interface RowReqAccess<T,F>{
	
    /**
     * 跳1列
     * @return
     */
    T jumpOne();
    /**
     * 跳过指定列数
     * @param nums
     * @return
     */
    T jump(int nums);
    /**
     * 跳到指定列索引
     * @param columIndex
     * @return
     */
    T jumpTo(int columIndex);
    
	/**
	 * 完成
	 * @return
	 */
	F complete();
	
	/**
	 * 类RowAccess.java的实现描述：行写访问器 
	 * @author sean 2018年10月31日 上午11:04:12
	 */
	public static interface RowWriteAccess extends RowReqAccess<RowWriteAccess,SheetSeqAccessAdapter.SheetWriteAccessAdapter>{
		
		/**
		 * @param metaAdapter
		 * @param context
		 * @param obj
		 * @return
		 */
		RowWriteAccess createCell(CellStyleAdapter metaAdapter,SpelContext context,Object obj);
		
		/**
		 * 创建单元格
		 * @param cellDataType
		 * @param val
		 * @param format
		 * @return
		 */
		RowWriteAccess createFormatCell(CellDataType cellDataType,Object val,String format);
		
		RowWriteAccess createCell(CellDataType cellDataType,Object val);
	}
	
	/**
	 * 类RowAccess.java的实现描述：行读访问器 
	 * @author sean 2018年10月31日 上午11:04:26
	 */
	public static interface RowReadAccess extends RowReqAccess<RowReadAccess,SheetSeqAccessAdapter.SheetReadAccessAdapter>{
		/**
		 * 读取单元格
		 * @return
		 */
		CellAdapter<?> readCell();
		/**
		 * 是否空行
		 * @return
		 */
		boolean isBlank();
		/**
		 * 读单元格取数据到bean
		 * @param metaAdapter
		 * @param obj
		 * @return
		 */
		RowReadAccess readCellValToObject(CellStyleAdapter metaAdapter,Object obj);
	}
}
