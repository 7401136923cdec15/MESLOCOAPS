package com.mes.loco.aps.server.service.po.aps;

import java.io.Serializable;
import java.util.Calendar;

/**
 * 工序工时
 * 
 * @author YouWang·Peng
 * @CreateTime 2021-7-15 16:45:26
 */
public class APSManuCapacityStep implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 序号
	 */
	public int ID = 0;

	/**
	 * 修程
	 */
	public int LineID = 0;

	/**
	 * 修程名称
	 */
	public String LineName = "";

	/**
	 * 工位（工段表）
	 */
	public int PartID = 0;

	/**
	 * 工位名称
	 */
	public String PartName = "";

	/**
	 * 工序ID
	 */
	public int StepID = 0;
	/**
	 * 工序名称
	 */
	public String StepName = "";

	/**
	 * 数量
	 */
	public double FQTY = 0;

	/**
	 * 周期（单位小时）
	 */
	public double Period = 0.0;

	/**
	 * 创建人
	 */
	public int CreatorID = 0;

	public String Creator = "";

	/**
	 * 创建时间
	 */
	public Calendar CreateTime = Calendar.getInstance();

	public Calendar EditTime = Calendar.getInstance();

	public String Editor = "";

	public int EditorID = 0;

	public Calendar AuditTime = Calendar.getInstance();

	public String Auditor = "";

	public int AuditorID = 0;

	/**
	 * 申请状态状态
	 */
	public int Status = 0;

	/**
	 * 激活、关闭
	 */
	public int Active = 0;
	/**
	 * 加工周期（单位小时）
	 */
	public double WorkHour = 0.0;

	/**
	 * 工时(单位小时) 单人干完所需工时 用于派工计算工时
	 */
	public double WorkHours = 0.0;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getLineID() {
		return LineID;
	}

	public double getWorkHours() {
		return WorkHours;
	}

	public void setWorkHours(double workHours) {
		WorkHours = workHours;
	}

	public void setLineID(int lineID) {
		LineID = lineID;
	}

	public int getStepID() {
		return StepID;
	}

	public String getStepName() {
		return StepName;
	}

	public void setStepID(int stepID) {
		StepID = stepID;
	}

	public void setStepName(String stepName) {
		StepName = stepName;
	}

	public double getFQTY() {
		return FQTY;
	}

	public void setFQTY(double fQTY) {
		FQTY = fQTY;
	}

	public double getPeriod() {
		return Period;
	}

	public void setPeriod(double period) {
		Period = period;
	}

	public int getCreatorID() {
		return CreatorID;
	}

	public void setCreatorID(int creatorID) {
		CreatorID = creatorID;
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

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public String getLineName() {
		return LineName;
	}

	public void setLineName(String lineName) {
		LineName = lineName;
	}

	public APSManuCapacityStep() {

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

	public int getEditorID() {
		return EditorID;
	}

	public void setEditorID(int editorID) {
		EditorID = editorID;
	}

	public Calendar getAuditTime() {
		return AuditTime;
	}

	public void setAuditTime(Calendar auditTime) {
		AuditTime = auditTime;
	}

	public String getAuditor() {
		return Auditor;
	}

	public void setAuditor(String auditor) {
		Auditor = auditor;
	}

	public int getAuditorID() {
		return AuditorID;
	}

	public void setAuditorID(int auditorID) {
		AuditorID = auditorID;
	}

	public int getActive() {
		return Active;
	}

	public void setActive(int active) {
		Active = active;
	}

	public double getWorkHour() {
		return WorkHour;
	}

	public void setWorkHour(double workHour) {
		WorkHour = workHour;
	}
}
