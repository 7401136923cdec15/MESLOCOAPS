package com.mes.loco.aps.server.service.po.sfc;

import java.io.Serializable;

/**
 * 试运返修项
 */
public class SFCRepairItem implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	public int ID = 0;

	/**
	 * 试运单ID
	 */
	public int TaskID = 0;

	/**
	 * 返修项任务ID
	 */
	public int ItemTaskID = 0;

	public String Code = "";

	public String Name = "";

	public String Detail = "";

	public int PartID = 0;

	public String PartName = "";

	public String TechnicianIDs = "";// 默认工艺人员

	public int TechnicianID;// 当前工艺员ID

	public String TechnicianName = "";

	/**
	 * 0默认 1 未完成 2已完成 3 同意试运 4 不同意试运
	 */
	public int Result = 0;

	/**
	 * 备注
	 */
	public String Remark = "";

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getCode() {
		return Code;
	}

	public void setCode(String code) {
		Code = code;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getDetail() {
		return Detail;
	}

	public void setDetail(String detail) {
		Detail = detail;
	}

	public int getPartID() {
		return PartID;
	}

	public void setPartID(int partID) {
		PartID = partID;
	}

	public String getPartName() {
		return PartName;
	}

	public void setPartName(String partName) {
		PartName = partName;
	}

	public String getTechnicianIDs() {
		return TechnicianIDs;
	}

	public void setTechnicianIDs(String technicianIDs) {
		TechnicianIDs = technicianIDs;
	}

	public int getTechnicianID() {
		return TechnicianID;
	}

	public void setTechnicianID(int technicianID) {
		TechnicianID = technicianID;
	}

	public String getTechnicianName() {
		return TechnicianName;
	}

	public void setTechnicianName(String technicianName) {
		TechnicianName = technicianName;
	}

	public int getResult() {
		return Result;
	}

	public void setResult(int result) {
		Result = result;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	public int getTaskID() {
		return TaskID;
	}

	public void setTaskID(int taskID) {
		TaskID = taskID;
	}

	public int getItemTaskID() {
		return ItemTaskID;
	}

	public void setItemTaskID(int itemTaskID) {
		ItemTaskID = itemTaskID;
	}
}
