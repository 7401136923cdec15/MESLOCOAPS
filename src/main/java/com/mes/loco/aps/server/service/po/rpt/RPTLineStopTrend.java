package com.mes.loco.aps.server.service.po.rpt;

/**
 * 机车每月停时天数统计
 * 
 * @author PengYouWang
 * @CreateTime 2020-7-13 23:41:44
 * @LastEditTime 2020-7-13 23:41:48
 *
 */
public class RPTLineStopTrend {
	/**
	 * 检修停时
	 */
	public double RepairPeriod = 0;
	/**
	 * 电报停时
	 */
	public double TelegraphPeriod = 0;
	/**
	 * 修程ID
	 */
	public int LineID = 0;
	/**
	 * 修程名称
	 */
	public String LineName = "";

	public RPTLineStopTrend() {
	}

	public int getLineID() {
		return LineID;
	}

	public void setLineID(int lineID) {
		LineID = lineID;
	}

	public String getLineName() {
		return LineName;
	}

	public void setLineName(String lineName) {
		LineName = lineName;
	}

	public double getRepairPeriod() {
		return RepairPeriod;
	}

	public void setRepairPeriod(double repairPeriod) {
		RepairPeriod = repairPeriod;
	}

	public double getTelegraphPeriod() {
		return TelegraphPeriod;
	}

	public void setTelegraphPeriod(double telegraphPeriod) {
		TelegraphPeriod = telegraphPeriod;
	}
}
