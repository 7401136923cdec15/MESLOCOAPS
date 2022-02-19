package com.mes.loco.aps.server.service.po.rpt;

import java.util.ArrayList;
import java.util.List;

/**
 * 机车进出厂每月数量统计
 * 
 * @author PengYouWang
 * @CreateTime 2020-7-13 23:41:44
 * @LastEditTime 2020-7-13 23:41:48
 *
 */
public class RPTMonthNumberTrend {
	/**
	 * 年份
	 */
	public int Year;
	/**
	 * 月份
	 */
	public int Month;
	/**
	 * 修程数据统计
	 */
	public List<RPTLineNumberTrend> RPTLineNumberTrendList = new ArrayList<>();

	public RPTMonthNumberTrend() {
	}

	public int getYear() {
		return Year;
	}

	public void setYear(int year) {
		Year = year;
	}

	public int getMonth() {
		return Month;
	}

	public void setMonth(int month) {
		Month = month;
	}

	public List<RPTLineNumberTrend> getRPTLineNumberTrendList() {
		return RPTLineNumberTrendList;
	}

	public void setRPTLineNumberTrendList(List<RPTLineNumberTrend> rPTLineNumberTrendList) {
		RPTLineNumberTrendList = rPTLineNumberTrendList;
	}
}
