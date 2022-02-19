package com.mes.loco.aps.server.service.po.rpt;

import java.util.ArrayList;
import java.util.List;

/**
 * 机车进出厂数量趋势图
 * 
 * @author PengYouWang
 * @CreateTime 2020-7-13 23:41:44
 * @LastEditTime 2020-7-13 23:41:48
 *
 */
public class RPTYearNumberTrend {
	/**
	 * 年度进厂台车数
	 */
	public int YearEnterC5 = 0;
	/**
	 * 年度出厂台车数
	 */
	public int YearOutC5 = 0;
	/**
	 * 年度在厂台车数
	 */
	public int YearInC5 = 0;
	/**
	 * 年度进厂台车数
	 */
	public int YearEnterC6 = 0;
	/**
	 * 年度出厂台车数
	 */
	public int YearOutC6 = 0;
	/**
	 * 年度在厂台车数
	 */
	public int YearInC6 = 0;
	/**
	 * 每月数据统计
	 */
	public List<RPTMonthNumberTrend> RPTMonthNumberTrendList = new ArrayList<RPTMonthNumberTrend>();

	public RPTYearNumberTrend() {
	}

	public List<RPTMonthNumberTrend> getRPTMonthNumberTrendList() {
		return RPTMonthNumberTrendList;
	}

	public void setRPTMonthNumberTrendList(List<RPTMonthNumberTrend> rPTMonthNumberTrendList) {
		RPTMonthNumberTrendList = rPTMonthNumberTrendList;
	}

	public int getYearEnterC5() {
		return YearEnterC5;
	}

	public void setYearEnterC5(int yearEnterC5) {
		YearEnterC5 = yearEnterC5;
	}

	public int getYearOutC5() {
		return YearOutC5;
	}

	public void setYearOutC5(int yearOutC5) {
		YearOutC5 = yearOutC5;
	}

	public int getYearInC5() {
		return YearInC5;
	}

	public void setYearInC5(int yearInC5) {
		YearInC5 = yearInC5;
	}

	public int getYearEnterC6() {
		return YearEnterC6;
	}

	public void setYearEnterC6(int yearEnterC6) {
		YearEnterC6 = yearEnterC6;
	}

	public int getYearOutC6() {
		return YearOutC6;
	}

	public void setYearOutC6(int yearOutC6) {
		YearOutC6 = yearOutC6;
	}

	public int getYearInC6() {
		return YearInC6;
	}

	public void setYearInC6(int yearInC6) {
		YearInC6 = yearInC6;
	}
}
