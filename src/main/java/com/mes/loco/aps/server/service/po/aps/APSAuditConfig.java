package com.mes.loco.aps.server.service.po.aps;

import java.io.Serializable;
import java.util.Calendar;

/**
 * 计划审批配置
 * 
 * @author YouWang·Peng
 * @CreateTime 2020-1-19 09:43:10
 * @LastEditTime 2020-1-19 09:43:18
 *
 */
public class APSAuditConfig implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	public int ID = 0;
	/**
	 * 计划性质
	 */
	public int APSShiftPeriod = 0;
	/**
	 * 审批岗位
	 */
	public int AuditPositionID = 0;
	/**
	 * 审批层级
	 */
	public int AuditLevel = 0;
	/**
	 * 创建人
	 */
	public int CreateID = 0;
	/**
	 * 创建时刻
	 */
	public Calendar CreateTime = Calendar.getInstance();
	/**
	 * 修改人
	 */
	public int EditID = 0;
	/**
	 * 修改时刻
	 */
	public Calendar EditTime = Calendar.getInstance();

	// 辅助属性
	public String ShiftPeriodText = "";
	public String AuditPosition = "";
	public String Creator = "";
	public String CreateTimeText = "";
	public String Editor = "";
	public String EditTimeText = "";

	public APSAuditConfig() {
		CreateTime.set(2000, 0, 1);
		EditTime.set(2000, 0, 1);
	}

	public int getAPSShiftPeriod() {
		return APSShiftPeriod;
	}

	public void setAPSShiftPeriod(int aPSShiftPeriod) {
		APSShiftPeriod = aPSShiftPeriod;
	}

	public int getAuditPositionID() {
		return AuditPositionID;
	}

	public void setAuditPositionID(int auditPositionID) {
		AuditPositionID = auditPositionID;
	}

	public int getAuditLevel() {
		return AuditLevel;
	}

	public void setAuditLevel(int auditLevel) {
		AuditLevel = auditLevel;
	}

	public int getCreateID() {
		return CreateID;
	}

	public void setCreateID(int createID) {
		CreateID = createID;
	}

	public Calendar getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(Calendar createTime) {
		CreateTime = createTime;
	}

	public String getShiftPeriodText() {
		return ShiftPeriodText;
	}

	public void setShiftPeriodText(String shiftPeriodText) {
		ShiftPeriodText = shiftPeriodText;
	}

	public String getAuditPosition() {
		return AuditPosition;
	}

	public void setAuditPosition(String auditPosition) {
		AuditPosition = auditPosition;
	}

	public String getCreator() {
		return Creator;
	}

	public void setCreator(String creator) {
		Creator = creator;
	}

	public String getCreateTimeText() {
		return CreateTimeText;
	}

	public void setCreateTimeText(String createTimeText) {
		CreateTimeText = createTimeText;
	}

	public int getEditID() {
		return EditID;
	}

	public void setEditID(int editID) {
		EditID = editID;
	}

	public Calendar getEditTime() {
		return EditTime;
	}

	public void setEditTime(Calendar editTime) {
		EditTime = editTime;
	}

	public String getEditor() {
		return Editor;
	}

	public void setEditor(String editor) {
		Editor = editor;
	}

	public String getEditTimeText() {
		return EditTimeText;
	}

	public void setEditTimeText(String editTimeText) {
		EditTimeText = editTimeText;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
}
