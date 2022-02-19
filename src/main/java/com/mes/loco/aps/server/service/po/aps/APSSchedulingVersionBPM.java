package com.mes.loco.aps.server.service.po.aps;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.mes.loco.aps.server.service.po.bpm.BPMTaskBase;

/**
 * 排程版本
 * 
 * @author PengYouWang
 * @CreateTime 2020-5-8 22:39:50
 * @LastEditTime 2020-5-8 22:39:56
 *
 */
public class APSSchedulingVersionBPM extends BPMTaskBase implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 版本号
	 */
	public String VersionNo = "";
	/**
	 * 排程性质
	 */
	public int APSShiftPeriod = 0;
	/**
	 * 任务ID集合
	 */
	public List<Integer> TaskPartIDList = new ArrayList<Integer>();
	/**
	 * 排程开始时间
	 */
	public Calendar StartTime = Calendar.getInstance();
	/**
	 * 排程结束时间
	 */
	public Calendar EndTime = Calendar.getInstance();
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

	public APSSchedulingVersionBPM() {
		StartTime.set(2000, 0, 1, 0, 0, 0);
		EndTime.set(2000, 0, 1, 0, 0, 0);
		AuditTime.set(2000, 0, 1, 0, 0, 0);
	}

	public String getVersionNo() {
		return VersionNo;
	}

	public void setVersionNo(String versionNo) {
		VersionNo = versionNo;
	}

	public int getAPSShiftPeriod() {
		return APSShiftPeriod;
	}

	public void setAPSShiftPeriod(int aPSShiftPeriod) {
		APSShiftPeriod = aPSShiftPeriod;
	}

	public List<Integer> getTaskPartIDList() {
		return TaskPartIDList;
	}

	public void setTaskPartIDList(List<Integer> taskPartIDList) {
		TaskPartIDList = taskPartIDList;
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
}
