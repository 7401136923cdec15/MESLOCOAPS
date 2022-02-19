package com.mes.loco.aps.server.service.po.aps;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.mes.loco.aps.server.service.po.bpm.BPMTaskBase;

/**
 * 日计划审批表
 * 
 * @author YouWang·Peng
 * @CreateTime 2020-6-3 15:05:03
 * @LastEditTime 2020-6-3 15:05:08
 *
 */
public class APSDayPlanAuditBPM extends BPMTaskBase implements Serializable {
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
	 * 班次
	 */
	public int ShiftID = 0;
	/**
	 * 创建日期
	 */
	public Calendar ShiftDate = Calendar.getInstance();
	/**
	 * 审批人ID
	 */
	public int AuditID = 0;
	/**
	 * 审批人名称
	 */
	public String Auditor = "";
	/**
	 * 审批时刻
	 */
	public Calendar AuditTime = Calendar.getInstance();
	/**
	 * 审批子项
	 */
	public List<APSDayPlanAuditItemBPM> AuditItemBPMList = new ArrayList<>();

	public APSDayPlanAuditBPM() {
		AuditTime.set(2000, 0, 1, 0, 0, 0);
		ShiftDate.set(2000, 0, 1, 0, 0, 0);
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getAreaID() {
		return AreaID;
	}

	public void setAreaID(int areaID) {
		AreaID = areaID;
	}

	public String getAreaName() {
		return AreaName;
	}

	public void setAreaName(String areaName) {
		AreaName = areaName;
	}

	public int getShiftID() {
		return ShiftID;
	}

	public void setShiftID(int shiftID) {
		ShiftID = shiftID;
	}

	public Calendar getShiftDate() {
		return ShiftDate;
	}

	public void setShiftDate(Calendar shiftDate) {
		ShiftDate = shiftDate;
	}

	public int getAuditID() {
		return AuditID;
	}

	public void setAuditID(int auditID) {
		AuditID = auditID;
	}

	public String getAuditor() {
		return Auditor;
	}

	public void setAuditor(String auditor) {
		Auditor = auditor;
	}

	public Calendar getAuditTime() {
		return AuditTime;
	}

	public void setAuditTime(Calendar auditTime) {
		AuditTime = auditTime;
	}

	public List<APSDayPlanAuditItemBPM> getAuditItemBPMList() {
		return AuditItemBPMList;
	}

	public void setAuditItemBPMList(List<APSDayPlanAuditItemBPM> auditItemBPMList) {
		AuditItemBPMList = auditItemBPMList;
	}
}
