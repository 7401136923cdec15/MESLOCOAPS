package com.mes.loco.aps.server.service.po.rpt;

import java.util.ArrayList;
import java.util.List;

/**
 * 机车每月停时天数统计
 * 
 * @author PengYouWang
 * @CreateTime 2020-7-13 23:41:44
 * @LastEditTime 2020-7-13 23:41:48
 *
 */
public class RPTMonthStopTrend {
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
	public List<RPTLineStopTrend> RPTLineStopTrendList = new ArrayList<>();

	public RPTMonthStopTrend() {
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

	public List<RPTLineStopTrend> getRPTLineStopTrendList() {
		return RPTLineStopTrendList;
	}

	public void setRPTLineStopTrendList(List<RPTLineStopTrend> rPTLineStopTrendList) {
		RPTLineStopTrendList = rPTLineStopTrendList;
	}
}
