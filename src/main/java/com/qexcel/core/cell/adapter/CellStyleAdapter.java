package com.qexcel.core.cell.adapter;

import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;

import com.qexcel.core.enums.CellDataType;
import com.qexcel.core.enums.HorizontalAlign;
import com.qexcel.core.enums.VerticalAlign;

public class CellStyleAdapter{
    
	/**
	 * 单元格类型
	 */
	private CellDataType cellDataType = CellDataType.STRING;
	/**
	 * 文本描述
	 */
	private String text = "";
	
	private String property;
	
	private String writeSpel;
	/**
	 * 单元格所在列宽
	 */
	private int width = -1;
	/**
	 * 格式化字符串
	 */
	private String format = "";
	/**
	 * 内容水平对齐方式
	 */
	private HorizontalAlign hAlign = HorizontalAlign.DEFAULT;
	/**
	 * 内容垂直对齐方式
	 */
	private VerticalAlign vAlign = VerticalAlign.DEFAULT;
	/**
	 * 字体
	 */
	private String font = "";
	/**
	 * 字体大小
	 */
	private short fontSize = -1;
	/**
	 * 字体颜色
	 */
	private HSSFColorPredefined fontColor = HSSFColorPredefined.BLACK;
	/**
	 * 字体是否加粗
	 */
	private boolean fontBold = false;
	
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public HorizontalAlign gethAlign() {
		return hAlign;
	}
	public void sethAlign(HorizontalAlign hAlign) {
		this.hAlign = hAlign;
	}
	public VerticalAlign getvAlign() {
		return vAlign;
	}
	public void setvAlign(VerticalAlign vAlign) {
		this.vAlign = vAlign;
	}
	public String getFont() {
		return font;
	}
	public void setFont(String font) {
		this.font = font;
	}
	public short getFontSize() {
		return fontSize;
	}
	public void setFontSize(short fontSize) {
		this.fontSize = fontSize;
	}
	public HSSFColorPredefined getFontColor() {
		return fontColor;
	}
	public void setFontColor(HSSFColorPredefined fontColor) {
		this.fontColor = fontColor;
	}
	public boolean isFontBold() {
		return fontBold;
	}
	public void setFontBold(boolean fontBold) {
		this.fontBold = fontBold;
	}
	public CellDataType getDataType() {
		return cellDataType;
	}
	public void setDataType(CellDataType cellDataType) {
		this.cellDataType = cellDataType;
	}
    public String getProperty() {
        return property;
    }
    public void setProperty(String property) {
        this.property = property;
    }
    public CellDataType getCellDataType() {
        return cellDataType;
    }
    public void setCellDataType(CellDataType cellDataType) {
        this.cellDataType = cellDataType;
    }
    public String getWriteSpel() {
        return writeSpel;
    }
    public void setWriteSpel(String writeSpel) {
        this.writeSpel = writeSpel;
    }
    
    public String styleHashCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("hAlign="+hAlign+"&");
        sb.append("vAlign="+vAlign+"&");
        sb.append("font="+font+"&");
        sb.append("fontSize="+fontSize+"&");
        sb.append("fontBold="+fontBold+"&");
        sb.append("fontColor="+fontColor+"&");
        sb.append("format="+format+"&");
        return sb.toString().hashCode()+"";
    }
    
    public String fontHashCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("font="+font+"&");
        sb.append("fontSize="+fontSize+"&");
        sb.append("fontBold="+fontBold+"&");
        sb.append("fontColor="+fontColor+"&");
        return sb.toString().hashCode()+"";
    }
}
