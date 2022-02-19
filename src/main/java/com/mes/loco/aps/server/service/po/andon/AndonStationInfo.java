package com.mes.loco.aps.server.service.po.andon;

import java.io.Serializable;

/**
 * 工位情况
 * 
 * @author YouWang·Peng
 * @CreateTime 2020-7-29 11:48:03
 * @LastEditTime 2020-7-29 11:48:08
 *
 */
public class AndonStationInfo implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 完成进度
	 */
	public int FinishProgress = 0;
	/**
	 * 剩余数
	 */
	public int FQTYLess = 0;
	/**
	 * 完成数
	 */
	public int FQTYFinish = 0;
	/**
	 * 工位ID
	 */
	public int StationID = 0;
	/**
	 * 工位名称
	 */
	public String StationName = "";
	/**
	 * 工位状态
	 */
	public String Status = "";

	public AndonStationInfo() {
	}

	public int getFinishProgress() {
		return FinishProgress;
	}

	public void setFinishProgress(int finishProgress) {
		FinishProgress = finishProgress;
	}

	public int getFQTYLess() {
		return FQTYLess;
	}

	public void setFQTYLess(int fQTYLess) {
		FQTYLess = fQTYLess;
	}

	public int getFQTYFinish() {
		return FQTYFinish;
	}

	public void setFQTYFinish(int fQTYFinish) {
		FQTYFinish = fQTYFinish;
	}

	public String getStationName() {
		return StationName;
	}

	public void setStationName(String stationName) {
		StationName = stationName;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public int getStationID() {
		return StationID;
	}

	public void setStationID(int stationID) {
		StationID = stationID;
	}
}
