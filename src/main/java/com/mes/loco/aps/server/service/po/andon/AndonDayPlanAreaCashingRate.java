package com.mes.loco.aps.server.service.po.andon;

import java.io.Serializable;

/**
 * 工区日计划兑现率
 */
public class AndonDayPlanAreaCashingRate implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 工区ID
	 */
	public int AreaID = 0;
	/**
	 * 工区名称
	 */
	public String AreaName = "";
	/**
	 * 工区日计划完成数
	 */
	public int FinishNumber = 0;
	/**
	 * 工区日计划数
	 */
	public int TotalNumber = 0;
	/**
	 * 兑现率
	 */
	public double Rate = 0.0;
	/**
	 * 班次
	 */
	public int ShiftID = 0;

	public AndonDayPlanAreaCashingRate() {
		super();
	}

	public AndonDayPlanAreaCashingRate(int areaID, String areaName, int finishNumber, int totalNumber, double rate) {
		super();
		AreaID = areaID;
		AreaName = areaName;
		FinishNumber = finishNumber;
		TotalNumber = totalNumber;
		Rate = rate;
	}

	@Override
	public String toString() {
		return "AndonDayPlanAreaCashingRate [AreaID=" + AreaID + ", AreaName=" + AreaName + ", FinishNumber="
				+ FinishNumber + ", TotalNumber=" + TotalNumber + ", Rate=" + Rate + "]";
	}

	public int getFinishNumber() {
		return FinishNumber;
	}

	public int getShiftID() {
		return ShiftID;
	}

	public void setShiftID(int shiftID) {
		ShiftID = shiftID;
	}

	public int getTotalNumber() {
		return TotalNumber;
	}

	public double getRate() {
		return Rate;
	}

	public void setFinishNumber(int finishNumber) {
		FinishNumber = finishNumber;
	}

	public void setTotalNumber(int totalNumber) {
		TotalNumber = totalNumber;
	}

	public void setRate(double rate) {
		Rate = rate;
	}

	public int getAreaID() {
		return AreaID;
	}

	public String getAreaName() {
		return AreaName;
	}

	public void setAreaID(int areaID) {
		AreaID = areaID;
	}

	public void setAreaName(String areaName) {
		AreaName = areaName;
	}
}
