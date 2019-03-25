package com.qexcel.helper;

import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.qexcel.core.ExcelAdapter;
import com.qexcel.core.ExcelAdapter.PropertyInterceptor;
import com.qexcel.core.enums.ExcelVersion;


/**
 * 类ExcelHelper.java的实现描述：excel工具类 
 * @author sean 2018年10月31日 上午10:50:08
 */
public class ExcelHelper{

	public static final String POINT = ".";
	
	public static interface Builder<T>{
		T build();
	}
	
	public static ReadBuilder readBuilder() {
		return new ReadBuilder();
	}
	
	public static WriteBuilder writeBuilder() {
		return new WriteBuilder();
	}
	
	public static class ReadBuilder implements Builder<ExcelAdapter>{ 
		private ExcelVersion version;
		private String filePath;
		private InputStream fileIs;
		
		public ReadBuilder version(ExcelVersion version) {
			this.version = version;
			return this;
		}

		public Builder<ExcelAdapter> withInput(String filePath) {
			this.version = getExcelVersion(filePath);
			this.filePath = filePath;
			return this;
		}
		
		public Builder<ExcelAdapter> withInput(ExcelVersion version,InputStream is) {
			this.version = version;
			this.fileIs = is;
			return this;
		}

		public ExcelAdapter build() {
			try(InputStream is = 
					(filePath != null && !filePath.isEmpty())?new FileInputStream(filePath):fileIs){
				if(this.version.equals(ExcelVersion.XLSX))
					return new ExcelAdapter(new XSSFWorkbook(is));
				else if(this.version.equals(ExcelVersion.XLS))
					return new ExcelAdapter(new HSSFWorkbook(is));
				return null;
			}catch (Exception e) {
				return null;
			}
		}
	}
	
	public static class WriteBuilder implements Builder<ExcelAdapter>{
		private ExcelVersion version;
		private boolean bigData = false;
		private  PropertyInterceptor  propertyInterceptor;
		public WriteBuilder version(ExcelVersion version) {
			this.version = version;
			return this;
		}
		
		public WriteBuilder bigData() {
			this.bigData = true;
			return this;
		}
		
		public WriteBuilder propertyInterceptor(PropertyInterceptor  interceptor) {
		    this.propertyInterceptor = interceptor;
		    return this;
		}
		
		@Override
		public ExcelAdapter build() {
		    ExcelAdapter excelAdapter = null;
			if(bigData) {
			    excelAdapter = new ExcelAdapter(new SXSSFWorkbook());
			}else if(this.version.equals(ExcelVersion.XLSX))
			    excelAdapter = new ExcelAdapter(new XSSFWorkbook());
			else if(this.version.equals(ExcelVersion.XLS))
			    excelAdapter = new ExcelAdapter(new HSSFWorkbook());
			else 
			    return null;
			excelAdapter.setPropertyInterceptor(propertyInterceptor);
			return excelAdapter;
		}
	}
	
	public static ExcelVersion getExcelVersion(String path) {
		if (StringUtils.isBlank(path)) {
			return null;
		}
		if (path.contains(POINT)) {
			String posix = path.substring(path.lastIndexOf(POINT) + 1, path.length());
			if (posix.equals(ExcelVersion.XLS.getPostfix()))
				return ExcelVersion.XLS;
			else if (posix.equals(ExcelVersion.XLSX.getPostfix()))
				return ExcelVersion.XLSX;
			return null;
		}
		return null;
	}
}
