package com.mes.loco.aps.server.service.po.andon;

import java.io.Serializable;

/**
 * 报表日志子项
 */

public class AndonJournalItem implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 订单ID
	 */
	public int ID = 0;
	/**
	 * 父项ID
	 */
	public int ParentID = 0;
	/**
	 * 修程ID
	 */
	public int LineID = 0;
	/**
	 * 工位ID
	 */
	public int PartID = 0;
	/**
	 * 工位名称
	 */
	public String PartName = "";
	/**
	 * 计划数
	 */
	public int FQTYPlan = 0;
	/**
	 * 实际数
	 */
	public int FQTYReal = 0;
	/**
	 * 本月数
	 */
	public int FQTYMonthTask = 0;
	/**
	 * 今日计划数
	 */
	public int FQTYDayPlan = 0;
	/**
	 * 今日完工数
	 */
	public int FQTYDayComplete = 0;

	public AndonJournalItem() {
		super();
	}

	public AndonJournalItem(int iD, int parentID, int lineID, int partID, String partName, int fQTYPlan, int fQTYReal,
			int fQTYMonthTask, int fQTYDayPlan, int fQTYDayComplete) {
		super();
		ID = iD;
		ParentID = parentID;
		LineID = lineID;
		PartID = partID;
		PartName = partName;
		FQTYPlan = fQTYPlan;
		FQTYReal = fQTYReal;
		FQTYMonthTask = fQTYMonthTask;
		FQTYDayPlan = fQTYDayPlan;
		FQTYDayComplete = fQTYDayComplete;
	}

	public int getID() {
		return ID;
	}

	public int getLineID() {
		return LineID;
	}

	public void setLineID(int lineID) {
		LineID = lineID;
	}

	public int getParentID() {
		return ParentID;
	}

	public int getPartID() {
		return PartID;
	}

	public String getPartName() {
		return PartName;
	}

	public int getFQTYPlan() {
		return FQTYPlan;
	}

	public int getFQTYReal() {
		return FQTYReal;
	}

	public int getFQTYMonthTask() {
		return FQTYMonthTask;
	}

	public int getFQTYDayPlan() {
		return FQTYDayPlan;
	}

	public int getFQTYDayComplete() {
		return FQTYDayComplete;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public void setParentID(int parentID) {
		ParentID = parentID;
	}

	public void setPartID(int partID) {
		PartID = partID;
	}

	public void setPartName(String partName) {
		PartName = partName;
	}

	public void setFQTYPlan(int fQTYPlan) {
		FQTYPlan = fQTYPlan;
	}

	public void setFQTYReal(int fQTYReal) {
		FQTYReal = fQTYReal;
	}

	public void setFQTYMonthTask(int fQTYMonthTask) {
		FQTYMonthTask = fQTYMonthTask;
	}

	public void setFQTYDayPlan(int fQTYDayPlan) {
		FQTYDayPlan = fQTYDayPlan;
	}

	public void setFQTYDayComplete(int fQTYDayComplete) {
		FQTYDayComplete = fQTYDayComplete;
	}
}
