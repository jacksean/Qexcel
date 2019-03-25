package com.qexcel.core.enums;

import org.apache.poi.ss.usermodel.CellType;

public enum CellDataType {
	DATE(CellType.NUMERIC),
	STRING(CellType.STRING),
	NUMBER(CellType.NUMERIC),
	BOOLEAN(CellType.BOOLEAN),
	FORMULA(CellType.FORMULA),
	BLANK(CellType.BLANK);
	
	private final CellType cellType;

	CellDataType(CellType cellType) {
		this.cellType = cellType;
	}

	public CellType getCellType() {
		return cellType;
	}
}
