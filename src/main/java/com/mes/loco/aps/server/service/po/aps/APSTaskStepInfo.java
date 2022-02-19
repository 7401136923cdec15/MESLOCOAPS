package com.mes.loco.aps.server.service.po.aps;

import java.io.Serializable;
import java.util.Calendar;

/**
 * 日计划 详情
 * 
 * @author ShrisJava
 *
 */
public class APSTaskStepInfo implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 工序任务ID
	 */
	public int ID = 0;
	/**
	 * 订单号
	 */
	public String OrderNo = "";
	/**
	 * 修程名称
	 */
	public String LineName = "";
	/**
	 * 局段名称
	 */
	public String CustomerName = "";
	/**
	 * 车号
	 */
	public String PartNo = "";
	/**
	 * 任务下达时刻
	 */
	public Calendar ReadyTime = Calendar.getInstance();
	/**
	 * 任务实际开始时刻
	 */
	public Calendar StartTime = Calendar.getInstance();
	/**
	 * 任务实际完成时刻
	 */
	public Calendar EndTime = Calendar.getInstance();
	/**
	 * 工区ID
	 */
	public int AreaID = 0;
	/**
	 * 工区名称
	 */
	public String Area = "";
	/**
	 * 工位ID
	 */
	public int PartID = 0;
	/**
	 * 工位名称
	 */
	public String PartName = "";
	/**
	 * 工序名称
	 */
	public String StepName = "";
	/**
	 * 执行班组
	 */
	public String ClassName = "";
	/**
	 * 派工人
	 */
	public String Dispather = "";
	/**
	 * 执行人
	 */
	public String Operators = "";
	/**
	 * 订单任务状态
	 */
	public int Status = 0;
	/**
	 * 是否已派工
	 */
	public int IsDispatched = 0;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getOrderNo() {
		return OrderNo;
	}

	public void setOrderNo(String orderNo) {
		OrderNo = orderNo;
	}

	public String getLineName() {
		return LineName;
	}

	public void setLineName(String lineName) {
		LineName = lineName;
	}

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}

	public String getPartNo() {
		return PartNo;
	}

	public void setPartNo(String partNo) {
		PartNo = partNo;
	}

	public Calendar getReadyTime() {
		return ReadyTime;
	}

	public void setReadyTime(Calendar readyTime) {
		ReadyTime = readyTime;
	}

	public Calendar getStartTime() {
		return StartTime;
	}

	public void setStartTime(Calendar startTime) {
		StartTime = startTime;
	}

	public Calendar getEndTime() {
		return EndTime;
	}

	public void setEndTime(Calendar endTime) {
		EndTime = endTime;
	}

	public String getPartName() {
		return PartName;
	}

	public void setPartName(String partName) {
		PartName = partName;
	}

	public String getStepName() {
		return StepName;
	}

	public void setStepName(String stepName) {
		StepName = stepName;
	}

	public String getClassName() {
		return ClassName;
	}

	public void setClassName(String className) {
		ClassName = className;
	}

	public String getOperators() {
		return Operators;
	}

	public void setOperators(String operators) {
		Operators = operators;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public String getDispather() {
		return Dispather;
	}

	public void setDispather(String dispather) {
		Dispather = dispather;
	}

	public int getIsDispatched() {
		return IsDispatched;
	}

	public void setIsDispatched(int isDispatched) {
		IsDispatched = isDispatched;
	}

	public int getAreaID() {
		return AreaID;
	}

	public void setAreaID(int areaID) {
		AreaID = areaID;
	}

	public String getArea() {
		return Area;
	}

	public void setArea(String area) {
		Area = area;
	}

	public int getPartID() {
		return PartID;
	}

	public void setPartID(int partID) {
		PartID = partID;
	}
}
