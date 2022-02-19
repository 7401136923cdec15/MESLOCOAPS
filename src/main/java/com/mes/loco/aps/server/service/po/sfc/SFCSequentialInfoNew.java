package com.mes.loco.aps.server.service.po.sfc;

import com.mes.loco.aps.server.service.po.sfc.SFCSequentialInfoNew;
import java.io.Serializable;

/**
 * 机车电子履历
 */
public class SFCSequentialInfoNew implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 序号
	 */
	public int ID = 0;
	/**
	 * 操作类型
	 */
	public String TypeText = "";
	/**
	 * 操作人
	 */
	public String Operators = "";
	/**
	 * 操作时刻
	 */
	public String OperateTime = "";
	/**
	 * 工位
	 */
	public String StationName = "";
	/**
	 * 工序
	 */
	public String StepName = "";
	/**
	 * 开工打卡
	 */
	public String ClockInfo = "";
	/**
	 * 预检、自检、终检、出厂检
	 */
	public String CheckInfo = "";
	/**
	 * 互检
	 */
	public String MCheckInfo = "";
	/**
	 * 专检
	 */
	public String SCheckInfo = "";

	public SFCSequentialInfoNew() {
	}

	public SFCSequentialInfoNew(int iD, String typeText, String operators, String operateTime, String stationName,
			String stepName, String clockInfo, String checkInfo, String mCheckInfo, String sCheckInfo) {
		super();
		ID = iD;
		TypeText = typeText;
		Operators = operators;
		OperateTime = operateTime;
		StationName = stationName;
		StepName = stepName;
		ClockInfo = clockInfo;
		CheckInfo = checkInfo;
		MCheckInfo = mCheckInfo;
		SCheckInfo = sCheckInfo;
	}

	public int getID() {
		return ID;
	}

	public String getTypeText() {
		return TypeText;
	}

	public String getOperators() {
		return Operators;
	}

	public String getStationName() {
		return StationName;
	}

	public String getStepName() {
		return StepName;
	}

	public String getClockInfo() {
		return ClockInfo;
	}

	public String getCheckInfo() {
		return CheckInfo;
	}

	public String getMCheckInfo() {
		return MCheckInfo;
	}

	public String getSCheckInfo() {
		return SCheckInfo;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public void setTypeText(String typeText) {
		TypeText = typeText;
	}

	public void setOperators(String operators) {
		Operators = operators;
	}

	public String getOperateTime() {
		return OperateTime;
	}

	public void setOperateTime(String operateTime) {
		OperateTime = operateTime;
	}

	public void setStationName(String stationName) {
		StationName = stationName;
	}

	public void setStepName(String stepName) {
		StepName = stepName;
	}

	public void setClockInfo(String clockInfo) {
		ClockInfo = clockInfo;
	}

	public void setCheckInfo(String checkInfo) {
		CheckInfo = checkInfo;
	}

	public void setMCheckInfo(String mCheckInfo) {
		MCheckInfo = mCheckInfo;
	}

	public void setSCheckInfo(String sCheckInfo) {
		SCheckInfo = sCheckInfo;
	}
}
