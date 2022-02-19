package com.mes.loco.aps.server.service.po.andon;

import java.io.Serializable;
import java.util.Calendar;

/**
 * 生产日报第一页-日期结构
 */
public class AndonDayReportDate implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 日期
	 */
	public Calendar Date = Calendar.getInstance();
	/**
	 * 颜色
	 */
	public int Color = 0;

	public AndonDayReportDate() {
		super();
	}

	public AndonDayReportDate(Calendar date, int color) {
		super();
		Date = date;
		Color = color;
	}

	public Calendar getDate() {
		return Date;
	}

	public int getColor() {
		return Color;
	}

	public void setDate(Calendar date) {
		Date = date;
	}

	public void setColor(int color) {
		Color = color;
	}

}
