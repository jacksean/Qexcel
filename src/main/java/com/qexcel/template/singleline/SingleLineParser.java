package com.qexcel.template.singleline;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;

import com.qexcel.core.seqaccess.RowReqAccess.RowReadAccess;
import com.qexcel.core.seqaccess.RowReqAccess.RowWriteAccess;
import com.qexcel.core.seqaccess.SheetSeqAccess.SheetReadAccess;
import com.qexcel.core.seqaccess.SheetSeqAccess.SheetWriteAccess;
import com.qexcel.core.template.TemplateParser;
import com.qexcel.core.template.context.ObjectWrapper;
import com.qexcel.core.template.context.SeqaccessContext;
import com.qexcel.core.template.context.Context.DefaultSpelContext;
import com.qexcel.template.anno.CellDataConfig;
import com.qexcel.util.TypeReference;

public class SingleLineParser implements TemplateParser<SingleLineConfig>{

	public static final SingleLineParser instance = new SingleLineParser();
	
	@Override
    public <R> List<R> readObjectList(SeqaccessContext<SheetReadAccess> ctx, SingleLineConfig tpl, Class<R> objClz) {
	    try {
	        List<R> list = new ArrayList<>();
	        RowReadAccess rra = null;
	        R object = null;
	        do {
	            object = objClz.newInstance();
	            rra = ctx.getSheetSeqAccess().readRow();
	            for(CellDataConfig dataTpl:tpl.getDatas()) {
	                if(StringUtils.isNotBlank(dataTpl.getProperty())) {
	                    rra.readCellValToObject(dataTpl.buildStyle(), object);
	                }else {
	                    rra.jumpOne();
	                }
	            }
	            list.add(object);
	        }while((ctx.getRowEndIndex() > 0 && ctx.getSheetSeqAccess().currentIndex() < ctx.getRowEndIndex())
	                ||( ctx.getRowEndIndex() < 0 && ctx.getSheetSeqAccess().hasNextRow()));
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
	
	@Override
    public <R> void readObjectList(SeqaccessContext<SheetReadAccess> ctx, SingleLineConfig tpl,
                                   Class<R> objClz, int batchSize, Consumer<List<R>> sink) {
	    try {
            List<R> list = new ArrayList<>();
            RowReadAccess rra = null;
            R object = null;
            do {
                object = objClz.newInstance();
                rra = ctx.getSheetSeqAccess().readRow();
                for(CellDataConfig dataTpl:tpl.getDatas()) {
                    if(StringUtils.isNotBlank(dataTpl.getProperty())) {
                        rra.readCellValToObject(dataTpl.buildStyle(), object);
                    }else {
                        rra.jumpOne();
                    }
                }
                list.add(object);
                if(list.size() >= batchSize) {
                    sink.accept(list);
                    list.clear();
                }
            }while( (ctx.getRowEndIndex()<0 || ctx.getRowEndIndex() >= ctx.getSheetSeqAccess().currentIndex() )
                    && ctx.getSheetSeqAccess().hasNextRow());
            if(!list.isEmpty())
                sink.accept(list);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <R> R readObject(SeqaccessContext<SheetReadAccess> ctx, SingleLineConfig tpl, TypeReference<R> typeRef) {
	    if(typeRef.getType() instanceof ParameterizedType) {
            return null;
        }else {
            return (R)readObject(ctx,tpl, Class.class.cast(typeRef.getType()));
        }
    }

    @Override
    public <R> R readObject(SeqaccessContext<SheetReadAccess> ctx, SingleLineConfig tpl, Class<R> objClz) {
        try {
            R object = objClz.newInstance();
            readToObject(ctx,tpl, object);
            return object;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
	public void writeToWb(SeqaccessContext<SheetWriteAccess> ctx, SingleLineConfig tpl, Object obj){
		RowWriteAccess rwa = ctx.getSheetSeqAccess().createRow();
		List<CellDataConfig> dataTpls = tpl.getDatas();
		for(CellDataConfig dataTpl:dataTpls) {
		    if(StringUtils.isNotBlank(dataTpl.getProperty())
		            || StringUtils.isNotBlank(dataTpl.getText())
		            || StringUtils.isNotBlank(dataTpl.getWriteSpel())) {
		        rwa.createCell(dataTpl.buildStyle(),ctx.buildSpelContext(ObjectWrapper.wrapper(obj)),obj);
		    }else {
		        rwa.jumpOne();
		    }
		}
	}
	
    @Override
    public <F> void writeToWb(SeqaccessContext<SheetWriteAccess> ctx, SingleLineConfig tpl, int initStatus,
                              BiPredicate<Integer,Consumer<List<F>>> appender) {
        List<CellDataConfig> dataTpls = tpl.getDatas();
        DefaultSpelContext spelCtx = ctx.buildSpelContext(null);
        int status = initStatus;
        Consumer<List<F>> appendSink = (datas)->{
            datas.forEach(data->{
                RowWriteAccess dataRow = ctx.getSheetSeqAccess().createRow();
                for(CellDataConfig dataTpl : tpl.getDatas()) {
                    if(StringUtils.isNotBlank(dataTpl.getProperty())
                            || StringUtils.isNotBlank(dataTpl.getText())
                            || StringUtils.isNotBlank(dataTpl.getWriteSpel())) {
                        spelCtx.setRootObject(ObjectWrapper.wrapper(data));
                        dataRow.createCell(dataTpl.buildStyle(),spelCtx,data);
                    }else {
                        dataRow.jumpOne();
                    }
                }
            });
        };
        while(!appender.test(status, appendSink)) { 
            status++;
        }
    }

    @Override
	public void readToObject(SeqaccessContext<SheetReadAccess> ctx,SingleLineConfig tpl, Object obj) {
	    RowReadAccess rra = ctx.getSheetSeqAccess().readRow();
		for(CellDataConfig dataTpl:tpl.getDatas()) {
		    if(StringUtils.isNotBlank(dataTpl.getProperty())) {
		        rra.readCellValToObject(dataTpl.buildStyle(), obj);
		    }else {
		        rra.jumpOne();
            }
		}
	}
}
