package com.mes.loco.aps.server.service.po.rpt;

import java.util.ArrayList;
import java.util.List;

/**
 * 机车停时趋势图
 * 
 * @author PengYouWang
 * @CreateTime 2020-7-13 23:41:44
 * @LastEditTime 2020-7-13 23:41:48
 *
 */
public class RPTYearStopTrend {
	/**
	 * C5年度检修停时
	 */
	public double C5RepairPeriod = 0;
	/**
	 * C5年度电报停时
	 */
	public double C5TelegraphPeriod = 0;
	/**
	 * C6年度检修停时
	 */
	public double C6RepairPeriod = 0;
	/**
	 * C6年度电报停时
	 */
	public double C6TelegraphPeriod = 0;
	/**
	 * 每月数据统计
	 */
	public List<RPTMonthStopTrend> RPTMonthNumberTrendList = new ArrayList<RPTMonthStopTrend>();

	public RPTYearStopTrend() {
	}

	public double getC5RepairPeriod() {
		return C5RepairPeriod;
	}

	public void setC5RepairPeriod(double c5RepairPeriod) {
		C5RepairPeriod = c5RepairPeriod;
	}

	public double getC5TelegraphPeriod() {
		return C5TelegraphPeriod;
	}

	public void setC5TelegraphPeriod(double c5TelegraphPeriod) {
		C5TelegraphPeriod = c5TelegraphPeriod;
	}

	public double getC6RepairPeriod() {
		return C6RepairPeriod;
	}

	public void setC6RepairPeriod(double c6RepairPeriod) {
		C6RepairPeriod = c6RepairPeriod;
	}

	public double getC6TelegraphPeriod() {
		return C6TelegraphPeriod;
	}

	public void setC6TelegraphPeriod(double c6TelegraphPeriod) {
		C6TelegraphPeriod = c6TelegraphPeriod;
	}

	public List<RPTMonthStopTrend> getRPTMonthNumberTrendList() {
		return RPTMonthNumberTrendList;
	}

	public void setRPTMonthNumberTrendList(List<RPTMonthStopTrend> rPTMonthNumberTrendList) {
		RPTMonthNumberTrendList = rPTMonthNumberTrendList;
	}
}
