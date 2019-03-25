package com.qexcel.template.table;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

import org.apache.commons.collections4.CollectionUtils;
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
import com.qexcel.util.ReflectUtil;
import com.qexcel.util.TypeReference;

public class TableParser implements TemplateParser<TableConfig>{
	
	public static final TableParser instance = new TableParser();
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public <R> List<R> readObjectList(SeqaccessContext<SheetReadAccess> ctx, TableConfig tpl, Class<R> objClz) {
	    List datas = new ArrayList<>();
        boolean hasTbHead = CollectionUtils.isNotEmpty(tpl.getHeads());
        ctx.getSheetSeqAccess().jump(hasTbHead?1:0);
        RowReadAccess rra = null;
        try {
            do {
                rra = ctx.getSheetSeqAccess().readRow();
                if(rra.isBlank()) {
                    ctx.getSheetSeqAccess().jump(-1);
                    break;
                }
                Object object = objClz.newInstance();
                for(CellDataConfig dataTpl : tpl.getDatas()) {
                    if(StringUtils.isNotBlank(dataTpl.getProperty())) {
                        rra.readCellValToObject(dataTpl.buildStyle(), object);
                    }else {
                        rra.jumpOne();
                    }
                }
                datas.add(object);
            }while( (ctx.getRowEndIndex()<0 || ctx.getRowEndIndex() - ctx.getSheetSeqAccess().currentIndex()>=0 )
                    && ctx.getSheetSeqAccess().hasNextRow());
            return datas;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
	
    @Override
    public <R> void readObjectList(SeqaccessContext<SheetReadAccess> ctx, TableConfig tpl, Class<R> objClz,
                                   int batchSize, Consumer<List<R>> sink) {
        boolean hasTbHead = CollectionUtils.isNotEmpty(tpl.getHeads());
        ctx.getSheetSeqAccess().jump(hasTbHead?1:0);
        RowReadAccess rra = null;
        List<R> batchDatas = new ArrayList<>(batchSize);
        try {
            do {
                rra = ctx.getSheetSeqAccess().readRow();
                if(rra.isBlank()) {
                    ctx.getSheetSeqAccess().jump(-1);
                    break;
                }
                R object = objClz.newInstance();
                for(CellDataConfig dataTpl : tpl.getDatas()) {
                    if(StringUtils.isNotBlank(dataTpl.getProperty())) {
                        rra.readCellValToObject(dataTpl.buildStyle(), object);
                    }else {
                        rra.jumpOne();
                    }
                }
                batchDatas.add(object);
                if(batchDatas.size() == batchSize) {
                    sink.accept(batchDatas);
                    batchDatas.clear();
                }
            }while( (ctx.getRowEndIndex()<0 || ctx.getRowEndIndex() - ctx.getSheetSeqAccess().currentIndex()>=0 )
                    && ctx.getSheetSeqAccess().hasNextRow());
            if(!batchDatas.isEmpty()) {
                sink.accept(batchDatas);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    @SuppressWarnings("unchecked")
	@Override
	public <R> R readObject(SeqaccessContext<SheetReadAccess> ctx,TableConfig tpl,TypeReference<R> typeRef) {
		if(typeRef.getType() instanceof ParameterizedType) {
			ParameterizedType pt = ParameterizedType.class.cast(typeRef.getType());
			if(!Collection.class.isAssignableFrom(Class.class.cast(pt.getRawType()))) {
				throw new RuntimeException("just support TypeReference<List<?>>");
			}
			Class<?> genericClazz = (Class<?>)pt.getActualTypeArguments()[0];
			return (R)readObjectList(ctx, tpl, genericClazz);
		}else {
			return (R)readObject(ctx, tpl, Class.class.cast(typeRef.getType()));
		}
	}

	@Override
	public <R> R readObject(SeqaccessContext<SheetReadAccess> ctx,TableConfig tpl,Class<R> objClz) {
		try {
			R object = objClz.newInstance();
			Field datasField = ReflectUtil.getField(object, tpl.getDataProperty());
	        if(!Collection.class.isAssignableFrom(datasField.getType())) 
	            throw new RuntimeException("dataProperty param must extends Collection");
	        ParameterizedType pt = (ParameterizedType)datasField.getGenericType();
	        Class<?> genericClazz = (Class<?>)pt.getActualTypeArguments()[0];
	        List datas = readObjectList(ctx, tpl, objClz);
	        datasField.set(object, datas);
			return object;
		} catch (Exception e) {
			throw new RuntimeException("readWithObjectClz error",e);
		}
	}
	
	@Override
	public void writeToWb(SeqaccessContext<SheetWriteAccess> ctx, TableConfig tpl, Object obj) {
		Collection<?> datas = getDataCollection(tpl,obj);
		RowWriteAccess rwa = ctx.getSheetSeqAccess().createRow();
		boolean hasTbHead = !tpl.getHeads().isEmpty();
		DefaultSpelContext spelCtx = ctx.buildSpelContext(ObjectWrapper.wrapper(obj));
		if(hasTbHead) {
			for(CellDataConfig dataTpl:tpl.getHeads()) {
			    if(StringUtils.isNotBlank(dataTpl.getText())
	                    || StringUtils.isNotBlank(dataTpl.getWriteSpel())) {
			        rwa.createCell(dataTpl.buildStyle(),spelCtx,obj);
			    }else {
			        rwa.jumpOne();
			    }
			}
		}
		if(datas != null) {
    		for(Object dataObj:datas) {
    			rwa = ctx.getSheetSeqAccess().createRow();
    			for(CellDataConfig dataTpl:tpl.getDatas()) {
    			    if(StringUtils.isNotBlank(dataTpl.getProperty())
    	                    || StringUtils.isNotBlank(dataTpl.getText())
    	                    || StringUtils.isNotBlank(dataTpl.getWriteSpel())) {
    			        spelCtx.setRootObject(ObjectWrapper.wrapper(dataObj));
    			        rwa.createCell(dataTpl.buildStyle(),spelCtx,dataObj);
    			    }else {
    			        rwa.jumpOne();
    			    }
    			}
    		}
		}
	}
	

    @Override
    public <F> void writeToWb(SeqaccessContext<SheetWriteAccess> ctx, TableConfig tpl, int initStatus,BiPredicate<Integer,Consumer<List<F>>> appender) {
        RowWriteAccess rwa = ctx.getSheetSeqAccess().createRow();
        boolean hasTbHead = !tpl.getHeads().isEmpty();
        DefaultSpelContext spelCtx = ctx.buildSpelContext(null);
        if(hasTbHead) {
            for(CellDataConfig dataTpl:tpl.getHeads()) {
                if(StringUtils.isNotBlank(dataTpl.getText())
                        || StringUtils.isNotBlank(dataTpl.getWriteSpel())) {
                    rwa.createCell(dataTpl.buildStyle(),spelCtx,null);
                }else {
                    rwa.jumpOne();
                }
            }
        }
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
	public void readToObject(SeqaccessContext<SheetReadAccess> ctx,TableConfig tpl, Object obj) {
        if(StringUtils.isBlank(tpl.getDataProperty()))
			throw new RuntimeException("must set dataProperty");
        Field datasField = ReflectUtil.getField(obj, tpl.getDataProperty());
		if(!Collection.class.isAssignableFrom(datasField.getType())) 
			throw new RuntimeException("dataProperty param must extends Collection");
		Type paramType = datasField.getGenericType();
		ParameterizedType pt = (ParameterizedType)paramType;
		Class<?> genericClazz = (Class<?>)pt.getActualTypeArguments()[0];
		List datas = new ArrayList<>();
		boolean hasTbHead = !tpl.getHeads().isEmpty();
		ctx.getSheetSeqAccess().jump(hasTbHead?1:0);
		RowReadAccess rra = null;
		try {
			do {
				rra = ctx.getSheetSeqAccess().readRow();
				if(rra.isBlank()) {
				    ctx.getSheetSeqAccess().jump(-1);
				    break;
				}
				Object object = genericClazz.newInstance();
				for(CellDataConfig dataTpl : tpl.getDatas()) {
				    if(StringUtils.isNotBlank(dataTpl.getProperty())) {
				        rra.readCellValToObject(dataTpl.buildStyle(), object);
				    }else {
				        rra.jumpOne();
				    }
				}
				datas.add(object);
			}while(	(ctx.getRowEndIndex()<0 || ctx.getRowEndIndex() >= ctx.getSheetSeqAccess().currentIndex() )
					&& ctx.getSheetSeqAccess().hasNextRow());
			datasField.set(obj, datas);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Collection<?> getDataCollection(TableConfig tpl, Object obj){
		try {
			if(obj instanceof Collection) {//优先判断是否为Collection
				return (Collection<?>)obj;
			}else if(StringUtils.isNotBlank(tpl.getDataProperty())){
			    Field dataField = ReflectUtil.getField(obj, tpl.getDataProperty());
				Object dataList = dataField.get(obj);
				if(dataList == null)
				    return null;
				if(dataList instanceof Collection) {
				    return (Collection<?>)dataList;
				}
				throw new RuntimeException("dataProperty must return collection object");
			}else {
				throw new Exception("can not get datas collection");
			}
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
