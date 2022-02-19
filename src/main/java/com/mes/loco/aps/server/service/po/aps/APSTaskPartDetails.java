package com.mes.loco.aps.server.service.po.aps;

import java.io.Serializable;
import java.util.Calendar;

/**
 * 工位任务详情
 * 
 * @author ShrisJava
 *
 */
public class APSTaskPartDetails implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 工位任务
	 */
	public APSTaskPart APSTaskPart = new APSTaskPart();
	/**
	 * 工序总数
	 */
	public int StepSize = 0;
	/**
	 * 工序完成数
	 */
	public int StepFinish = 0;
	/**
	 * 工序已排数
	 */
	public int StepSchedule = 0;
	/**
	 * 在制数
	 */
	public int StepMaking = 0;
	/**
	 * 工区ID
	 */
	public int AreaID = 0;
	public String AreaName = "";
	/**
	 * 车号
	 */
	public String PartNo = "";
	/**
	 * 订单号
	 */
	public int OrderID = 0;
	/**
	 * 工位
	 */
	public int PartID = 0;
	/**
	 * 计划日期
	 */
	public Calendar ShiftDate = Calendar.getInstance();
	/**
	 * 顺序
	 */
	public int OrderNum = 0;

	public APSTaskPartDetails() {
	}

	public APSTaskPart getAPSTaskPart() {
		return APSTaskPart;
	}

	public void setAPSTaskPart(APSTaskPart aPSTaskPart) {
		APSTaskPart = aPSTaskPart;
	}

	public int getStepSize() {
		return StepSize;
	}

	public int getAreaID() {
		return AreaID;
	}

	public void setAreaID(int areaID) {
		AreaID = areaID;
	}

	public void setStepSize(int stepSize) {
		StepSize = stepSize;
	}

	public int getStepFinish() {
		return StepFinish;
	}

	public void setStepFinish(int stepFinish) {
		StepFinish = stepFinish;
	}

	public int getStepSchedule() {
		return StepSchedule;
	}

	public void setStepSchedule(int stepSchedule) {
		StepSchedule = stepSchedule;
	}

	public int getStepMaking() {
		return StepMaking;
	}

	public void setStepMaking(int stepMaking) {
		StepMaking = stepMaking;
	}

	public String getPartNo() {
		return PartNo;
	}

	public void setPartNo(String partNo) {
		PartNo = partNo;
	}

	public String getAreaName() {
		return AreaName;
	}

	public void setAreaName(String areaName) {
		AreaName = areaName;
	}

	public int getOrderID() {
		return OrderID;
	}

	public void setOrderID(int orderID) {
		OrderID = orderID;
	}

	public int getPartID() {
		return PartID;
	}

	public void setPartID(int partID) {
		PartID = partID;
	}

	public Calendar getShiftDate() {
		return ShiftDate;
	}

	public void setShiftDate(Calendar shiftDate) {
		ShiftDate = shiftDate;
	}

	public int getOrderNum() {
		return OrderNum;
	}

	public void setOrderNum(int orderNum) {
		OrderNum = orderNum;
	}
}
