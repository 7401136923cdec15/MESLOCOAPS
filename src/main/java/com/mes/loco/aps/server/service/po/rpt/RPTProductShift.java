package com.mes.loco.aps.server.service.po.rpt;

import java.util.Calendar;

/**
 * 日、周报表
 * 
 * @author PengYouWang
 * @CreateTime 2020-6-17 17:05:34
 * @LastEditTime 2020-6-17 17:08:17
 *
 */
public class RPTProductShift {
	/**
	 * 主键
	 */
	public int ID = 0;
	/**
	 * 班次日期
	 */
	public Calendar ShiftDate = Calendar.getInstance();
	/**
	 * 日班次
	 */
	public int ShiftID = 0;
	/**
	 * 修程
	 */
	public int LineID = 0;
	/**
	 * 修程名称
	 */
	public String LineName = "";
	/**
	 * 修竣数
	 */
	public int Finsh = 0;
	/**
	 * 进厂数
	 */
	public int Enter = 0;
	/**
	 * 本周修竣数
	 */
	public int WeekFinsh = 0;
	/**
	 * 本周进厂数
	 */
	public int WeekEnter = 0;
	/**
	 * 本月修竣数
	 */
	public int MonthFinsh = 0;
	/**
	 * 本月进厂数
	 */
	public int MonthEnter = 0;
	/**
	 * 年度修竣数
	 */
	public int YearFinsh = 0;
	/**
	 * 年度进厂数
	 */
	public int YearEnter = 0;
	/**
	 * 在厂
	 */
	public int RealPlant = 0;
	/**
	 * 在修
	 */
	public int RealRepair = 0;
	/**
	 * 年度平均周期(实际平均停时)
	 */
	public double AvgRepairPeriod = 0.0;
	/**
	 * 累计停时
	 */
	public double SumRepairPeriod = 0.0;
	/**
	 * 平均电报停时
	 */
	public double AvgTelegraphPeriod = 0.0;
	/**
	 * 累计电报停时
	 */
	public double SumTelegraphPeriod = 0.0;

	/**
	 * 平均月停时
	 */
	public double AvgMonthRepairPeriod = 0.0;
	/**
	 * 平均月电报停时
	 */
	public double AvgMonthTelegraphPeriod = 0.0;
	/**
	 * 是否为每月最后一天
	 */
	public int IsMonthLastDay = 0;
	/**
	 * 月出厂数
	 */
	public int MonthOut = 0;
	/**
	 * 年出厂数
	 */
	public int YearOut = 0;

	public RPTProductShift() {
		ShiftDate.set(2000, 0, 1);
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public double getAvgRepairPeriod() {
		return AvgRepairPeriod;
	}

	public void setAvgRepairPeriod(double avgRepairPeriod) {
		AvgRepairPeriod = avgRepairPeriod;
	}

	public Calendar getShiftDate() {
		return ShiftDate;
	}

	public void setShiftDate(Calendar shiftDate) {
		ShiftDate = shiftDate;
	}

	public int getShiftID() {
		return ShiftID;
	}

	public void setShiftID(int shiftID) {
		ShiftID = shiftID;
	}

	public int getLineID() {
		return LineID;
	}

	public void setLineID(int lineID) {
		LineID = lineID;
	}

	public int getFinsh() {
		return Finsh;
	}

	public void setFinsh(int finsh) {
		Finsh = finsh;
	}

	public int getEnter() {
		return Enter;
	}

	public void setEnter(int enter) {
		Enter = enter;
	}

	public int getWeekFinsh() {
		return WeekFinsh;
	}

	public void setWeekFinsh(int weekFinsh) {
		WeekFinsh = weekFinsh;
	}

	public int getWeekEnter() {
		return WeekEnter;
	}

	public void setWeekEnter(int weekEnter) {
		WeekEnter = weekEnter;
	}

	public int getMonthFinsh() {
		return MonthFinsh;
	}

	public void setMonthFinsh(int monthFinsh) {
		MonthFinsh = monthFinsh;
	}

	public int getMonthEnter() {
		return MonthEnter;
	}

	public void setMonthEnter(int monthEnter) {
		MonthEnter = monthEnter;
	}

	public int getYearFinsh() {
		return YearFinsh;
	}

	public void setYearFinsh(int yearFinsh) {
		YearFinsh = yearFinsh;
	}

	public int getYearEnter() {
		return YearEnter;
	}

	public void setYearEnter(int yearEnter) {
		YearEnter = yearEnter;
	}

	public int getRealPlant() {
		return RealPlant;
	}

	public void setRealPlant(int realPlant) {
		RealPlant = realPlant;
	}

	public int getRealRepair() {
		return RealRepair;
	}

	public void setRealRepair(int realRepair) {
		RealRepair = realRepair;
	}

	public double getSumRepairPeriod() {
		return SumRepairPeriod;
	}

	public void setSumRepairPeriod(double sumRepairPeriod) {
		SumRepairPeriod = sumRepairPeriod;
	}

	public double getAvgTelegraphPeriod() {
		return AvgTelegraphPeriod;
	}

	public void setAvgTelegraphPeriod(double avgTelegraphPeriod) {
		AvgTelegraphPeriod = avgTelegraphPeriod;
	}

	public double getSumTelegraphPeriod() {
		return SumTelegraphPeriod;
	}

	public void setSumTelegraphPeriod(double sumTelegraphPeriod) {
		SumTelegraphPeriod = sumTelegraphPeriod;
	}

	public String getLineName() {
		return LineName;
	}

	public void setLineName(String lineName) {
		LineName = lineName;
	}

	public double getAvgMonthRepairPeriod() {
		return AvgMonthRepairPeriod;
	}

	public void setAvgMonthRepairPeriod(double avgMonthRepairPeriod) {
		AvgMonthRepairPeriod = avgMonthRepairPeriod;
	}

	public double getAvgMonthTelegraphPeriod() {
		return AvgMonthTelegraphPeriod;
	}

	public void setAvgMonthTelegraphPeriod(double avgMonthTelegraphPeriod) {
		AvgMonthTelegraphPeriod = avgMonthTelegraphPeriod;
	}

	public int getIsMonthLastDay() {
		return IsMonthLastDay;
	}

	public void setIsMonthLastDay(int isMonthLastDay) {
		IsMonthLastDay = isMonthLastDay;
	}

	public int getMonthOut() {
		return MonthOut;
	}

	public void setMonthOut(int monthOut) {
		MonthOut = monthOut;
	}

	public int getYearOut() {
		return YearOut;
	}

	public void setYearOut(int yearOut) {
		YearOut = yearOut;
	}
}
