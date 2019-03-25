package com.qexcel.core.template;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

import com.qexcel.core.seqaccess.SheetSeqAccess.SheetReadAccess;
import com.qexcel.core.seqaccess.SheetSeqAccess.SheetWriteAccess;
import com.qexcel.core.template.context.SeqaccessContext;

/**
 * 类TemplateEngine.java的实现描述：模板处理引擎 
 * 
 * @author sean 2019年3月20日 下午1:36:55
 */
public interface TemplateEngine {

    void readToObject(SeqaccessContext<? extends SheetReadAccess> ctx, Object obj);

    <R> List<R> readObjectList(SeqaccessContext<? extends SheetReadAccess> ctx, Class<R> objClz);

    <R> void readObjectList(SeqaccessContext<? extends SheetReadAccess> ctx, Class<R> objClz, int batchSize,
                            Consumer<List<R>> sink);

    void writeToWb(SeqaccessContext<? extends SheetWriteAccess> ctx, Object obj);

    void writeToWb(SeqaccessContext<? extends SheetWriteAccess> ctx, int initStatus,
                   BiPredicate<Integer,Consumer<List>> appender);
}
