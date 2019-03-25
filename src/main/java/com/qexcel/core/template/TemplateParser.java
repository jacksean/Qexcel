package com.qexcel.core.template;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

import com.qexcel.core.seqaccess.SheetSeqAccess.SheetReadAccess;
import com.qexcel.core.seqaccess.SheetSeqAccess.SheetWriteAccess;
import com.qexcel.core.template.context.SeqaccessContext;
import com.qexcel.util.TypeReference;

public interface TemplateParser<T extends TemplateConfig> {
    
    default boolean canReadObject() {return true; }
    
    default boolean canReadObjectList() {return true; }

    default void readToObject(SeqaccessContext<SheetReadAccess> ctx, T tpl, Object obj) {}

    default <R> R readObject(SeqaccessContext<SheetReadAccess> ctx, T tpl, TypeReference<R> typeRef) { return null;}

    default <R> R readObject(SeqaccessContext<SheetReadAccess> ctx, T tpl, Class<R> objClz) { return null;}

    default <R> List<R> readObjectList(SeqaccessContext<SheetReadAccess> ctx, T tpl, Class<R> objClz) { return new ArrayList<R>();}

    default <R> void readObjectList(SeqaccessContext<SheetReadAccess> ctx, T tpl, Class<R> objClz, int batchSize,
                            Consumer<List<R>> sink) {};

    default void writeToWb(SeqaccessContext<SheetWriteAccess> ctx, T tpl, Object obj) {};

    default <F> void writeToWb(SeqaccessContext<SheetWriteAccess> ctx, T tpl, int initStatus,BiPredicate<Integer,Consumer<List<F>>> appender) {};
}
