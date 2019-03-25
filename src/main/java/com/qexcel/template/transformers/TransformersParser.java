package com.qexcel.template.transformers;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;

import com.qexcel.core.SheetAdapter;
import com.qexcel.core.seqaccess.RowReqAccess.RowReadAccess;
import com.qexcel.core.seqaccess.SheetSeqAccess.SheetReadAccess;
import com.qexcel.core.seqaccess.SheetSeqAccess.SheetWriteAccess;
import com.qexcel.core.template.TemplateParser;
import com.qexcel.core.template.context.ObjectWrapper;
import com.qexcel.core.template.context.SeqaccessContext;
import com.qexcel.template.anno.CellDataConfig;
import com.qexcel.template.transformers.TransformersConfig.PositionCellConfig;
import com.qexcel.util.TypeReference;

public class TransformersParser implements TemplateParser<TransformersConfig>{

	public static final TransformersParser instance = new TransformersParser();
	
    @Override
    public boolean canReadObjectList() {
        return false;
    }

    @Override
    public void readToObject(SeqaccessContext<SheetReadAccess> ctx, TransformersConfig tpl, Object obj) {
        int baseRowIndex = ctx.getSheetSeqAccess().currentIndex();
        int maxRowIndex = baseRowIndex;
        List<PositionCellConfig> cellConfigs = tpl.getDatas();
        for(PositionCellConfig cellConfig : cellConfigs) {
            int fixStartRow = baseRowIndex+cellConfig.getStartRow();
            int fixEndRow = fixStartRow+cellConfig.getRowOffset();
            if(StringUtils.isNotBlank(cellConfig.getCellData().getProperty())) {
                ctx.getSheetSeqAccess().jumpTo(fixStartRow).readRow().jumpTo(cellConfig.getStartColumn())
                .readCellValToObject(cellConfig.getCellData().buildStyle(), obj);
            }
            maxRowIndex = Math.max(maxRowIndex, fixEndRow);
        }
        ctx.getSheetSeqAccess().jumpTo(maxRowIndex + 1);
    }

    @Override
    public <R> R readObject(SeqaccessContext<SheetReadAccess> ctx, TransformersConfig tpl, TypeReference<R> typeRef) {
        if(typeRef.getType() instanceof ParameterizedType) {
            return null;
        }else {
            return (R)readObject(ctx,tpl, Class.class.cast(typeRef.getType()));
        }
    }

    @Override
    public <R> R readObject(SeqaccessContext<SheetReadAccess> ctx, TransformersConfig tpl, Class<R> objClz) {
        try {
            R object = objClz.newInstance();
            readToObject(ctx,tpl, object);
            return object;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeToWb(SeqaccessContext<SheetWriteAccess> ctx, TransformersConfig tpl, Object obj) {
        int baseRowIndex = ctx.getSheetSeqAccess().currentIndex();
        int maxRowIndex = baseRowIndex;
        List<PositionCellConfig> cellConfigs = tpl.getDatas();
        SheetAdapter sheet = ctx.getSheetSeqAccess().getSheet();
        for(PositionCellConfig cellConfig : cellConfigs) {
            int fixStartRow = baseRowIndex+cellConfig.getStartRow();
            int fixEndRow = fixStartRow+cellConfig.getRowOffset();
            if(cellConfig.getRowOffset() > 0 || cellConfig.getColumnOffset() > 0)
                sheet.createMergedRegion(fixStartRow,fixEndRow,cellConfig.getStartColumn(), cellConfig.getColumnOffset());
            sheet.createCell(fixStartRow, cellConfig.getStartColumn(), cellConfig.getCellData().buildStyle(), 
                    ctx.buildSpelContext(ObjectWrapper.wrapper(obj)), obj);
            maxRowIndex = Math.max(maxRowIndex, fixEndRow);
        }
        ctx.getSheetSeqAccess().jumpTo(maxRowIndex+1);
    }
}
