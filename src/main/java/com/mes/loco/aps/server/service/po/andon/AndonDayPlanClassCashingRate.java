package com.mes.loco.aps.server.service.po.andon;

import java.io.Serializable;

/**
 * 班组日计划兑现率
 */
public class AndonDayPlanClassCashingRate implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 班组ID
	 */
	public int ClassID = 0;
	/**
	 * 班组名称
	 */
	public String ClassName = "";
	/**
	 * 班组日计划完成数
	 */
	public int FinishNumber = 0;
	/**
	 * 班组日计划数
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

	public AndonDayPlanClassCashingRate() {
		super();
	}

	public AndonDayPlanClassCashingRate(int classID, String className, int finishNumber, int totalNumber, double rate) {
		super();
		ClassID = classID;
		ClassName = className;
		FinishNumber = finishNumber;
		TotalNumber = totalNumber;
		Rate = rate;
	}

	@Override
	public String toString() {
		return "AndonDayPlanClassCashingRate [ClassID=" + ClassID + ", ClassName=" + ClassName + ", FinishNumber="
				+ FinishNumber + ", TotalNumber=" + TotalNumber + ", Rate=" + Rate + "]";
	}

	public int getClassID() {
		return ClassID;
	}

	public int getShiftID() {
		return ShiftID;
	}

	public void setShiftID(int shiftID) {
		ShiftID = shiftID;
	}

	public String getClassName() {
		return ClassName;
	}

	public int getFinishNumber() {
		return FinishNumber;
	}

	public int getTotalNumber() {
		return TotalNumber;
	}

	public double getRate() {
		return Rate;
	}

	public void setClassID(int classID) {
		ClassID = classID;
	}

	public void setClassName(String className) {
		ClassName = className;
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
}
