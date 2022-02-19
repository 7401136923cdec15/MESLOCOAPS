package com.mes.loco.aps.server.service.po.aps;

import java.io.Serializable;

/**
 * 车号分类日计划任务详情
 * 
 * @author ShrisJava
 *
 */
public class APSPartNoDetails implements Serializable {
	private static final long serialVersionUID = 1L;

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
	 * 车号
	 */
	public String PartNo = "";
	/**
	 * 订单号
	 */
	public int OrderID = 0;

	public APSPartNoDetails() {
	}

	public int getStepSize() {
		return StepSize;
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

	public int getOrderID() {
		return OrderID;
	}

	public void setOrderID(int orderID) {
		OrderID = orderID;
	}
}
