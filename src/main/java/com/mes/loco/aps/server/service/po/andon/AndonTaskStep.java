package com.mes.loco.aps.server.service.po.andon;

import java.io.Serializable;

/**
 * 工区的工序任务详情
 */
public class AndonTaskStep implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 工区
	 */
	public int WorkAreaID = 0;
	public String WorkArea = "";
	/**
	 * 车
	 */
	public int OrderID = 0;
	public String PartNo = "";
	/**
	 * 班组
	 */
	public String ClassNames = "";
	/**
	 * 工位
	 */
	public int PartID = 0;
	public String PartName = "";
	/**
	 * 工序
	 */
	public int StepID = 0;
	public String StepName = "";

	/**
	 * 顺序ID
	 */
	public int OrderNum = 0;

	public AndonTaskStep() {
	}

	public AndonTaskStep(int workAreaID, String workArea, int orderID, String partNo, String classNames, int partID,
			String partName, int stepID, String stepName) {
		super();
		WorkAreaID = workAreaID;
		WorkArea = workArea;
		OrderID = orderID;
		PartNo = partNo;
		ClassNames = classNames;
		PartID = partID;
		PartName = partName;
		StepID = stepID;
		StepName = stepName;
	}

	public int getWorkAreaID() {
		return WorkAreaID;
	}

	public String getWorkArea() {
		return WorkArea;
	}

	public int getOrderNum() {
		return OrderNum;
	}

	public void setOrderNum(int orderNum) {
		OrderNum = orderNum;
	}

	public int getOrderID() {
		return OrderID;
	}

	public String getPartNo() {
		return PartNo;
	}

	public String getClassNames() {
		return ClassNames;
	}

	public int getPartID() {
		return PartID;
	}

	public String getPartName() {
		return PartName;
	}

	public int getStepID() {
		return StepID;
	}

	public String getStepName() {
		return StepName;
	}

	public void setWorkAreaID(int workAreaID) {
		WorkAreaID = workAreaID;
	}

	public void setWorkArea(String workArea) {
		WorkArea = workArea;
	}

	public void setOrderID(int orderID) {
		OrderID = orderID;
	}

	public void setPartNo(String partNo) {
		PartNo = partNo;
	}

	public void setClassNames(String classNames) {
		ClassNames = classNames;
	}

	public void setPartID(int partID) {
		PartID = partID;
	}

	public void setPartName(String partName) {
		PartName = partName;
	}

	public void setStepID(int stepID) {
		StepID = stepID;
	}

	public void setStepName(String stepName) {
		StepName = stepName;
	}
}
