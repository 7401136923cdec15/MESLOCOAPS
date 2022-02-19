package com.mes.loco.aps.server.service.po.sch;

import com.mes.loco.aps.server.service.po.bpm.BPMTaskBase;
import com.mes.loco.aps.server.service.po.sch.SCHSecondmentBPM;
import java.io.Serializable;
import java.util.Calendar;

/**
 * 借调(流程引擎版)
 */
public class SCHSecondmentBPM extends BPMTaskBase implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 流程类型(1、跨工区跨班组借调 2、本工区跨班组借调 3、班组内员工调动)
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

	// 辅助属性
	/**
	 * 借调人员名称
	 */
	public String PersonName = "";
	public String OldClassName = "";
	public String NewClassName = "";
	public String OldPartNames = "";
	public String NewPartNames = "";

	public SCHSecondmentBPM() {
		StartTime.set(2000, 0, 1, 0, 0, 0);
		EndTime.set(2000, 0, 1, 0, 0, 0);
		SubmitTime.set(2000, 0, 1, 0, 0, 0);
	}

	public int getType() {
		return Type;
	}

	public void setType(int type) {
		Type = type;
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

	public String getPersonID() {
		return PersonID;
	}

	public String getOldPosition() {
		return OldPosition;
	}

	public String getNewPosition() {
		return NewPosition;
	}

	public void setPersonID(String personID) {
		PersonID = personID;
	}

	public void setOldPosition(String oldPosition) {
		OldPosition = oldPosition;
	}

	public void setNewPosition(String newPosition) {
		NewPosition = newPosition;
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
}
