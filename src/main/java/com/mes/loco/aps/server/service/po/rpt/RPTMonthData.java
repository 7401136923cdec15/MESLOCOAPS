package com.mes.loco.aps.server.service.po.rpt;

/**
 * 季度完成数
 * 
 * @author PengYouWang
 * @CreateTime 2020-6-17 17:05:34
 * @LastEditTime 2020-6-17 17:08:17
 *
 */
public class RPTMonthData {
	/**
	 * 年份
	 */
	public int Year = 0;
	/**
	 * 月份
	 */
	public int Month = 0;
	/**
	 * 修程
	 */
	public int LineID = 0;
	/**
	 * 完成数
	 */
	public int Finish = 0;

	// 辅助属性
	/**
	 * 季度文本
	 */
	public String MonthName = "";
	/**
	 * 修程文本
	 */
	public String LineName = "";

	public RPTMonthData() {
	}
	
	public RPTMonthData(int year, int month, int lineID, int finish, String monthName, String lineName) {
		super();
		Year = year;
		Month = month;
		LineID = lineID;
		Finish = finish;
		MonthName = monthName;
		LineName = lineName;
	}

	public int getYear() {
		return Year;
	}

	public void setYear(int year) {
		Year = year;
	}

	public int getLineID() {
		return LineID;
	}

	public void setLineID(int lineID) {
		LineID = lineID;
	}

	public int getFinish() {
		return Finish;
	}

	public void setFinish(int finish) {
		Finish = finish;
	}

	public String getLineName() {
		return LineName;
	}

	public void setLineName(String lineName) {
		LineName = lineName;
	}

	public int getMonth() {
		return Month;
	}

	public void setMonth(int month) {
		Month = month;
	}

	public String getMonthName() {
		return MonthName;
	}

	public void setMonthName(String monthName) {
		MonthName = monthName;
	}
}
