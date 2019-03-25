package com.qexcel.core.enums;

public enum ExcelVersion {
	XLS("xls"),//max row nums is 65536
	XLSX("xlsx");
	private String postfix;

	private ExcelVersion(String postfix) {
		this.postfix = postfix;
	}

	public String getPostfix() {
		return postfix;
	}
}
