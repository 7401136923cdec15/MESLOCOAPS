package com.mes.loco.aps.server.service.po.aps;

import java.io.Serializable;
import java.util.Calendar;

/**
 * 日计划取消日志
 */
public class APSTaskStepCancelLog implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	public int ID = 0;
	public int OrderID = 0;
	public String PartNo = "";
	public int PartID = 0;
	public String PartName = "";
	public String StepIDs = "";
	public String StepNames = "";
	public int CancelType = 0;
	public String CancelTypeName = "";
	public String Remark = "";
	public int CreateID = 0;
	public String CreatorName = "";
	public Calendar CreateTime = Calendar.getInstance();

	public APSTaskStepCancelLog() {
		super();
	}

	public APSTaskStepCancelLog(int iD, int orderID, String partNo, int partID, String partName, String stepIDs,
			String stepNames, int cancelType, String cancelTypeName, String remark, int createID, String creatorName,
			Calendar createTime) {
		super();
		ID = iD;
		OrderID = orderID;
		PartNo = partNo;
		PartID = partID;
		PartName = partName;
		StepIDs = stepIDs;
		StepNames = stepNames;
		CancelType = cancelType;
		CancelTypeName = cancelTypeName;
		Remark = remark;
		CreateID = createID;
		CreatorName = creatorName;
		CreateTime = createTime;
	}

	public int getID() {
		return ID;
	}

	public int getOrderID() {
		return OrderID;
	}

	public String getPartNo() {
		return PartNo;
	}

	public int getPartID() {
		return PartID;
	}

	public String getPartName() {
		return PartName;
	}

	public String getStepIDs() {
		return StepIDs;
	}

	public String getStepNames() {
		return StepNames;
	}

	public int getCancelType() {
		return CancelType;
	}

	public String getCancelTypeName() {
		return CancelTypeName;
	}

	public String getRemark() {
		return Remark;
	}

	public int getCreateID() {
		return CreateID;
	}

	public String getCreatorName() {
		return CreatorName;
	}

	public Calendar getCreateTime() {
		return CreateTime;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public void setOrderID(int orderID) {
		OrderID = orderID;
	}

	public void setPartNo(String partNo) {
		PartNo = partNo;
	}

	public void setPartID(int partID) {
		PartID = partID;
	}

	public void setPartName(String partName) {
		PartName = partName;
	}

	public void setStepIDs(String stepIDs) {
		StepIDs = stepIDs;
	}

	public void setStepNames(String stepNames) {
		StepNames = stepNames;
	}

	public void setCancelType(int cancelType) {
		CancelType = cancelType;
	}

	public void setCancelTypeName(String cancelTypeName) {
		CancelTypeName = cancelTypeName;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	public void setCreateID(int createID) {
		CreateID = createID;
	}

	public void setCreatorName(String creatorName) {
		CreatorName = creatorName;
	}

	public void setCreateTime(Calendar createTime) {
		CreateTime = createTime;
	}
}
