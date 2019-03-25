package com.qexcel.core.cell.adapter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

import com.qexcel.core.RowAdapter;
import com.qexcel.core.cell.adapter.BlankAdapter;
import com.qexcel.core.cell.adapter.BooleanAdapter;
import com.qexcel.core.cell.adapter.CellAdapter;
import com.qexcel.core.cell.adapter.DateAdapter;
import com.qexcel.core.cell.adapter.NumberAdapter;
import com.qexcel.core.cell.adapter.StringAdapter;
import com.qexcel.core.enums.CellDataType;

public class CellAdapterFactory {
    
    /**
     * 根据指定单元格类型构建单元格装饰器
     * @param rowAdapter 行装饰器
     * @param type 数据类型
     * @param cell 单元格对象
     * @return
     */
    public static CellAdapter buildWithType(RowAdapter rowAdapter,CellDataType type,Cell cell){
        CellAdapter cellAdapter = null;
        if(type == null)
            cellAdapter = new BlankAdapter(rowAdapter,cell);
        else {
            switch (type) {
            case BLANK:
                cellAdapter = new BlankAdapter(rowAdapter,cell);
                break;
            case BOOLEAN:
                cellAdapter = new BooleanAdapter(rowAdapter,cell);
                break;
            case NUMBER:
                cellAdapter = new NumberAdapter(rowAdapter,cell);
                break;
            case DATE:
                cellAdapter = new DateAdapter(rowAdapter,cell);
                break;
            case STRING:
                cellAdapter = new StringAdapter(rowAdapter,cell);
                break;
            case FORMULA:
                cellAdapter = new FormulaAdapter(rowAdapter,cell);
                break;
            default:
                cellAdapter = new BlankAdapter(rowAdapter,cell);
                break;
            }
        }
        return cellAdapter;
    }
    
    /**
     * 根据单元格类型构建单元格装饰器
     * @param rowAdapter 行装饰器
     * @param cell 单元格对象
     * @return
     */
    public static CellAdapter buildWithCell(RowAdapter rowAdapter,Cell cell){
        CellAdapter cellAdapter = null;
        if(cell == null)
            cellAdapter = new BlankAdapter(rowAdapter,cell);
        else {
            switch (cell.getCellType()) {
            case _NONE:
            case BLANK:
                cellAdapter = new BlankAdapter(rowAdapter,cell);
                break;
            case BOOLEAN:
                cellAdapter = new BooleanAdapter(rowAdapter,cell);
                break;
            case FORMULA:
                FormulaEvaluator evaluator = rowAdapter.getSheetAdapter().getExcelAdapter().getEvaluator();
                cellAdapter = buildWithCell(rowAdapter,evaluator.evaluateInCell(cell));
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    cellAdapter = new DateAdapter(rowAdapter,cell);
                } else {
                    cellAdapter = new NumberAdapter(rowAdapter,cell);
                }
                break;
            case STRING:
                cellAdapter = new StringAdapter(rowAdapter,cell);
                break;
            default:
                cellAdapter = new BlankAdapter(rowAdapter,cell);
                break;
            }
        }
        return cellAdapter;
    }
}
