package com.qexcel.core.seqaccess;

import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

import com.qexcel.core.ExcelAdapter;
import com.qexcel.core.SheetAdapter;
import com.qexcel.core.seqaccess.RowReqAccess.RowReadAccess;
import com.qexcel.core.seqaccess.RowReqAccess.RowWriteAccess;
import com.qexcel.core.template.TemplateEngine;

/**
 * 类SheetAccess.java的实现描述：页访问器 
 * @author sean 2018年10月31日 上午11:04:53
 */
public interface SheetSeqAccess<T>{
    /**
     * 当前行索引
     * @return
     */
    int currentIndex();
    /**
     * 跳过指定行数
     * @param rowNums 行数
     * @return
     */
    T jump(int rowNums);
    /**
     * 跳到指定行索引
     * @param rowIndex
     * @return
     */
    T jumpTo(int rowIndex);
	
	/**
	 * 完成
	 * @return
	 */
	ExcelAdapter complete();
	
	/**
	 * 类SheetAccess.java的实现描述：页读访问器 
	 * @author sean 2018年10月31日 上午11:06:41
	 */
	public static interface SheetReadAccess extends SheetSeqAccess<SheetReadAccess>{
	    
        public SheetAdapter getSheet();
		/**
		 * 读取当前行
		 * @return
		 */
		RowReadAccess readRow();
		/**
		 * 是否还有下一行
		 * @return
		 */
		boolean hasNextRow();
		
		/**
		 * 根据模板读取数据到bean
		 * @param rowEndIndex 结束行索引
		 * @param tplEngine 模板引擎
		 * @param obj 对象bean
		 * @return
		 */
		SheetReadAccess readToObject(TemplateEngine tplEngine,int rowEndIndex,Object obj);
	}
	
	/**
	 * 类SheetAccess.java的实现描述：页写访问器 
	 * @author sean 2018年10月31日 上午11:06:58
	 */
	public static interface SheetWriteAccess extends SheetSeqAccess<SheetWriteAccess>{
	    
	    SheetAdapter getSheet();
	    
		/**
		 * 创建行
		 * @return
		 */
		RowWriteAccess createRow();
		/**
		 * 根据模板从bean获取数据写入excel
		 * @param tplEngine 模板引擎
		 * @param obj bean对象
		 * @return
		 */
		SheetWriteAccess writeToWorkBook(TemplateEngine tplEngine,Object obj);
		
		SheetWriteAccess writeToWorkBook(TemplateEngine tplEngine,Map<String,Object> variables,Object obj);
		
		SheetWriteAccess writeToWorkBook(TemplateEngine tplEngine, int initStatus,BiPredicate<Integer,Consumer<List>> appender);
		
		SheetWriteAccess writeToWorkBook(TemplateEngine tplEngine, Map<String,Object> variables,int initStatus,BiPredicate<Integer,Consumer<List>> appender);
	}
}
