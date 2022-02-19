package com.mes.loco.aps.server.service.po.rpt;

/**
 * 季度完成数
 * 
 * @author PengYouWang
 * @CreateTime 2020-6-17 17:05:34
 * @LastEditTime 2020-6-17 17:08:17
 *
 */
public class RPTQuarterData {
	/**
	 * 年份
	 */
	public int Year = 0;
	/**
	 * 季度
	 */
	public int Quarter = 0;
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
	public String QuarterName = "";
	/**
	 * 修程文本
	 */
	public String LineName = "";

	public RPTQuarterData() {
	}
	
	public RPTQuarterData(int year, int quarter, int lineID, int finish, String quarterName, String lineName) {
		super();
		Year = year;
		Quarter = quarter;
		LineID = lineID;
		Finish = finish;
		QuarterName = quarterName;
		LineName = lineName;
	}



	public int getYear() {
		return Year;
	}

	public void setYear(int year) {
		Year = year;
	}

	public int getQuarter() {
		return Quarter;
	}

	public void setQuarter(int quarter) {
		Quarter = quarter;
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

	public String getQuarterName() {
		return QuarterName;
	}

	public void setQuarterName(String quarterName) {
		QuarterName = quarterName;
	}

	public String getLineName() {
		return LineName;
	}

	public void setLineName(String lineName) {
		LineName = lineName;
	}
}
