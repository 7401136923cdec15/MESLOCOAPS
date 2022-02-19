package com.mes.loco.aps.server.service.po.excel;

import java.io.Serializable;

/**
 * 有颜色的单元格
 */
public class ExcelColorCol implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 数据列表
	 */
	public String Value = "";
	/**
	 * 颜色
	 */
	public int Color = 0;

	public ExcelColorCol() {
		super();
	}

	public ExcelColorCol(String value, int color) {
		super();
		Value = value;
		Color = color;
	}

	public String getValue() {
		return Value;
	}

	public int getColor() {
		return Color;
	}

	public void setValue(String value) {
		Value = value;
	}

	public void setColor(int color) {
		Color = color;
	}
}
