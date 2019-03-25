package com.qexcel.core.template.context;

import com.qexcel.core.seqaccess.SheetSeqAccess;

public class SeqaccessContext<T extends SheetSeqAccess> extends Context{
    
    private final T sheetSeqAccess;
    
    private int rowEndIndex = -1;

    public SeqaccessContext(T sheetSeqAccess) {
        super();
        this.sheetSeqAccess = sheetSeqAccess;
    }

    public T getSheetSeqAccess() {
        return sheetSeqAccess;
    }

    public static <T extends SheetSeqAccess> SeqaccessContext<T> build(T sheetSeqAccess) {
        return new SeqaccessContext<T>(sheetSeqAccess);
    }

    public int getRowEndIndex() {
        return rowEndIndex;
    }

    public SeqaccessContext<T> setRowEndIndex(int rowEndIndex) {
        this.rowEndIndex = rowEndIndex;
        return this;
    }
}
