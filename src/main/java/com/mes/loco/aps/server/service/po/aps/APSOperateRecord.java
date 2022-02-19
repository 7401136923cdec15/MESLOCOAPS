package com.mes.loco.aps.server.service.po.aps;

import java.io.Serializable;
import java.util.Calendar;

/**
 * 计划审批操作记录
 * 
 * @author YouWang·Peng
 * @CreateTime 2020-1-19 10:22:38
 * @LastEditTime 2020-1-19 10:22:43
 *
 */
public class APSOperateRecord implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	public int ID = 0;
	/**
	 * 工位计划ID
	 */
	public int APSTaskPartID = 0;
	/**
	 * 操作人
	 */
	public int OperateID = 0;
	/**
	 * 操作类型
	 */
	public int OperateType = 0;
	/**
	 * 操作时刻
	 */
	public Calendar OperateTime = Calendar.getInstance();
	/**
	 * 备注
	 */
	public String Remark = "";
	/**
	 * 操作级别(对应配置表)
	 */
	public int OperateLevel = 0;
	// 辅助属性
	public String Operator = "";
	public String Department = "";
	public String Position = "";
	public String OperateTypeText = "";
	public String OperateTimeText = "";

	public APSOperateRecord() {
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getAPSTaskPartID() {
		return APSTaskPartID;
	}

	public void setAPSTaskPartID(int aPSTaskPartID) {
		APSTaskPartID = aPSTaskPartID;
	}

	public int getOperateID() {
		return OperateID;
	}

	public void setOperateID(int operateID) {
		OperateID = operateID;
	}

	public int getOperateType() {
		return OperateType;
	}

	public void setOperateType(int operateType) {
		OperateType = operateType;
	}

	public Calendar getOperateTime() {
		return OperateTime;
	}

	public void setOperateTime(Calendar operateTime) {
		OperateTime = operateTime;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	public String getOperator() {
		return Operator;
	}

	public void setOperator(String operator) {
		Operator = operator;
	}

	public String getDepartment() {
		return Department;
	}

	public void setDepartment(String department) {
		Department = department;
	}

	public String getPosition() {
		return Position;
	}

	public void setPosition(String position) {
		Position = position;
	}

	public String getOperateTypeText() {
		return OperateTypeText;
	}

	public void setOperateTypeText(String operateTypeText) {
		OperateTypeText = operateTypeText;
	}

	public String getOperateTimeText() {
		return OperateTimeText;
	}

	public void setOperateTimeText(String operateTimeText) {
		OperateTimeText = operateTimeText;
	}

	public int getOperateLevel() {
		return OperateLevel;
	}

	public void setOperateLevel(int operateLevel) {
		OperateLevel = operateLevel;
	}
}
