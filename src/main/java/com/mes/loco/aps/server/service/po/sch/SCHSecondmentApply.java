package com.mes.loco.aps.server.service.po.sch;

import com.mes.loco.aps.server.service.po.bpm.BPMTaskBase;
import com.mes.loco.aps.server.service.po.sch.SCHSecondmentApply;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 借调申请单(流程引擎版)
 */
public class SCHSecondmentApply extends BPMTaskBase implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 流程类型(1、跨工区跨班组借调 2、本工区跨班组借调 )
	 */
	public int Type = 0;
	/**
	 * 调入人员ID （Step1，Type1,2,3）
	 */
	public String PersonID = "";
	/**
	 * 原属班组（Step1，Type1,2）
	 */
	public int OldClassID = 0;
	/**
	 * 调入班组（Step1，Type1,2）
	 */
	public int NewClassID = 0;
	/**
	 * 原属工位 （Step1，Type3）
	 */
	public String OldPartList = "";
	/**
	 * 调入工位 （Step1，Type3）
	 */
	public String NewPartList = "";
	/**
	 * 原属岗位（Step1，Type1,2,3）
	 */
	public String OldPosition = "";
	/**
	 * 调入岗位（Step1，Type1,2,3）
	 */
	public String NewPosition = "";
	/**
	 * 调动开始时间（Step1，Type1,2,3）
	 */
	public Calendar StartTime = Calendar.getInstance();
	/**
	 * 调动 截止时间（Step1，Type1,2,3）
	 */
	public Calendar EndTime = Calendar.getInstance();

	/**
	 * 工区ID
	 */
	public int AreaID = 0;
	/**
	 * 工区名称
	 */
	public String AreaName = "";
	/**
	 * 调入工区ID
	 */
	public int NewAreaID = 0;
	/**
	 * 调入工区名称
	 */
	public String NewAreaName = "";

	// 辅助属性
	public String PersonName = "";
	public String OldClassName = "";
	public String NewClassName = "";
	public String TypeText = "";
	public String OldPartNames = "";
	public String NewPartNames = "";

	public SCHSecondmentApply() {
		CreateTime.set(2000, 0, 1, 0, 0, 0);
		SubmitTime.set(2000, 0, 1, 0, 0, 0);
		StartTime.set(2000, 0, 1, 0, 0, 0);
		EndTime.set(2000, 0, 1, 0, 0, 0);
	}

	public SCHSecondmentApply(SCHSecondmentBPM wSCHSecondmentBPM) {

		this.Code = wSCHSecondmentBPM.Code;
		this.CreateTime = wSCHSecondmentBPM.CreateTime;
		this.EndTime = wSCHSecondmentBPM.EndTime;
		this.FlowID = wSCHSecondmentBPM.FlowID;
		this.FlowType = wSCHSecondmentBPM.FlowType;
		this.FollowerID = wSCHSecondmentBPM.FollowerID;
		this.FollowerName = wSCHSecondmentBPM.FollowerName;
		this.ID = wSCHSecondmentBPM.ID;
		this.NewClassID = wSCHSecondmentBPM.NewClassID;
		this.NewClassName = wSCHSecondmentBPM.NewClassName;
		this.NewPartList = wSCHSecondmentBPM.NewPartList;
		this.NewPartNames = wSCHSecondmentBPM.NewPartNames;
		this.OldPosition = wSCHSecondmentBPM.OldPosition;
		this.PersonID = wSCHSecondmentBPM.PersonID;
		this.PersonName = wSCHSecondmentBPM.PersonName;
		this.StartTime = wSCHSecondmentBPM.StartTime;
		this.Status = wSCHSecondmentBPM.Status;
		this.StatusText = wSCHSecondmentBPM.StatusText;
		this.OldClassID = wSCHSecondmentBPM.OldClassID;
		this.OldClassName = wSCHSecondmentBPM.OldClassName;
		this.OldPartNames = wSCHSecondmentBPM.OldPartNames;
		this.NewPosition = wSCHSecondmentBPM.NewPosition;
		this.StepID = wSCHSecondmentBPM.StepID;
		this.SubmitTime = wSCHSecondmentBPM.SubmitTime;
		this.Type = wSCHSecondmentBPM.Type;
		this.UpFlowID = wSCHSecondmentBPM.UpFlowID;
		this.UpFlowName = wSCHSecondmentBPM.UpFlowName;
		this.TagTypes = wSCHSecondmentBPM.TagTypes;
	}

	public static List<SCHSecondmentApply> BPMListToApplyList(List<SCHSecondmentBPM> wSCHSecondmentBPMList) {
		List<SCHSecondmentApply> wResult = new ArrayList<SCHSecondmentApply>();
		for (SCHSecondmentBPM schSecondmentBPM : wSCHSecondmentBPMList) {
			wResult.add(new SCHSecondmentApply(schSecondmentBPM));
		}
		return wResult;
	}

	public int getType() {
		return Type;
	}

	public void setType(int type) {
		Type = type;
	}

	public String getPersonID() {
		return PersonID;
	}

	public void setPersonID(String personID) {
		PersonID = personID;
	}

	public int getOldClassID() {
		return OldClassID;
	}

	public void setOldClassID(int oldClassID) {
		OldClassID = oldClassID;
	}

	public int getNewClassID() {
		return NewClassID;
	}

	public void setNewClassID(int newClassID) {
		NewClassID = newClassID;
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

	public String getPersonName() {
		return PersonName;
	}

	public void setPersonName(String personName) {
		PersonName = personName;
	}

	public String getOldClassName() {
		return OldClassName;
	}

	public void setOldClassName(String oldClassName) {
		OldClassName = oldClassName;
	}

	public String getNewClassName() {
		return NewClassName;
	}

	public void setNewClassName(String newClassName) {
		NewClassName = newClassName;
	}

	public String getTypeText() {
		return TypeText;
	}

	public void setTypeText(String typeText) {
		TypeText = typeText;
	}

	public String getOldPartList() {
		return OldPartList;
	}

	public void setOldPartList(String oldPartList) {
		OldPartList = oldPartList;
	}

	public String getNewPartList() {
		return NewPartList;
	}

	public void setNewPartList(String newPartList) {
		NewPartList = newPartList;
	}

	public String getOldPosition() {
		return OldPosition;
	}

	public void setOldPosition(String oldPosition) {
		OldPosition = oldPosition;
	}

	public String getNewPosition() {
		return NewPosition;
	}

	public void setNewPosition(String newPosition) {
		NewPosition = newPosition;
	}

	public String getOldPartNames() {
		return OldPartNames;
	}

	public void setOldPartNames(String oldPartNames) {
		OldPartNames = oldPartNames;
	}

	public String getNewPartNames() {
		return NewPartNames;
	}

	public void setNewPartNames(String newPartNames) {
		NewPartNames = newPartNames;
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

	public int getNewAreaID() {
		return NewAreaID;
	}

	public void setNewAreaID(int newAreaID) {
		NewAreaID = newAreaID;
	}

	public String getNewAreaName() {
		return NewAreaName;
	}

	public void setNewAreaName(String newAreaName) {
		NewAreaName = newAreaName;
	}
}
