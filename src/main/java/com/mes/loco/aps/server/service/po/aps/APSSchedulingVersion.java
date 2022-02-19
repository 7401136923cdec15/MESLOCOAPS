package com.mes.loco.aps.server.service.po.aps;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.mes.loco.aps.server.service.po.oms.OMSOrder;

/**
 * 排程版本
 * 
 * @author PengYouWang
 * @CreateTime 2020-5-8 22:39:50
 * @LastEditTime 2020-5-8 22:39:56
 *
 */
public class APSSchedulingVersion implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	public int ID = 0;
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
	 * 创建人ID
	 */
	public int CreateID = 0;
	/**
	 * 创建人名称
	 */
	public String Creator = "";
	/**
	 * 创建时刻
	 */
	public Calendar CreateTime = Calendar.getInstance();
	/**
	 * 排程开始时间
	 */
	public Calendar StartTime = Calendar.getInstance();
	/**
	 * 排程结束时间
	 */
	public Calendar EndTime = Calendar.getInstance();

	/**
	 * 创建人ID
	 */
	public int AuditID = 0;
	/**
	 * 创建人名称
	 */
	public String Auditor = "";

	public Calendar AuditTime = Calendar.getInstance();

	/**
	 * 版本状态
	 */
	public int Status = 0;

	// 辅助属性
	public List<APSTaskPart> APSTaskPartList = new ArrayList<APSTaskPart>();
	public List<OMSOrder> OMSOrderList = new ArrayList<OMSOrder>();

	public APSSchedulingVersion() {
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
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

	public List<APSTaskPart> getAPSTaskPartList() {
		return APSTaskPartList;
	}

	public void setAPSTaskPartList(List<APSTaskPart> aPSTaskPartList) {
		APSTaskPartList = aPSTaskPartList;
	}

	public List<Integer> getTaskPartIDList() {
		return TaskPartIDList;
	}

	public void setTaskPartIDList(List<Integer> taskPartIDList) {
		TaskPartIDList = taskPartIDList;
	}

	public int getCreateID() {
		return CreateID;
	}

	public void setCreateID(int createID) {
		CreateID = createID;
	}

	public String getCreator() {
		return Creator;
	}

	public void setCreator(String creator) {
		Creator = creator;
	}

	public Calendar getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(Calendar createTime) {
		CreateTime = createTime;
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

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}
}
