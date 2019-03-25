package com.qexcel.template.anno;

import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;

import com.qexcel.core.cell.adapter.CellStyleAdapter;
import com.qexcel.core.enums.CellDataType;
import com.qexcel.core.enums.HorizontalAlign;
import com.qexcel.core.enums.VerticalAlign;

public class CellDataConfig {
    private CellDataType type;
    private int width;
    
    private String text;
    private String property;
    private String writeSpel;
    private String format;
    private HorizontalAlign hAlign;
    private VerticalAlign vAlign;
    private String font;
    private short fontSize;
    private boolean fontBold;
    private HSSFColorPredefined fontColor;
    public CellDataType getType() {
        return type;
    }
    public void setType(CellDataType type) {
        this.type = type;
    }
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getProperty() {
        return property;
    }
    public void setProperty(String property) {
        this.property = property;
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
    public boolean isFontBold() {
        return fontBold;
    }
    public void setFontBold(boolean fontBold) {
        this.fontBold = fontBold;
    }
    public HSSFColorPredefined getFontColor() {
        return fontColor;
    }
    public void setFontColor(HSSFColorPredefined fontColor) {
        this.fontColor = fontColor;
    }
    public String getWriteSpel() {
        return writeSpel;
    }
    public void setWriteSpel(String writeSpel) {
        this.writeSpel = writeSpel;
    }
    
    public CellStyleAdapter buildStyle() {
        CellStyleAdapter meta = new CellStyleAdapter();
        meta.setDataType(this.getType());
        meta.setFont(this.getFont());
        meta.setFontBold(this.isFontBold());
        meta.setFontColor(this.getFontColor());
        meta.setFontSize(this.getFontSize());
        meta.setFormat(this.getFormat());
        meta.setProperty(this.getProperty());
        meta.setWriteSpel(this.getWriteSpel());
        meta.sethAlign(this.gethAlign());
        meta.setText(this.getText());
        meta.setvAlign(this.getvAlign());
        meta.setWidth(this.getWidth());
        return meta;
    }
}
