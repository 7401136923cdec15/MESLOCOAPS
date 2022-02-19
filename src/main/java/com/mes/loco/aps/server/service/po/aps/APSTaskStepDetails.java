package com.mes.loco.aps.server.service.po.aps;

import java.io.Serializable;

/**
 * 工序任务详情
 */
public class APSTaskStepDetails implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 工序任务ID
	 */
	public int ID = 0;
	/**
	 * 工位类型
	 */
	public int Type = 0;
	/**
	 * 工位名称
	 */
	public String PartName = "";
	/**
	 * 工位ID
	 */
	public int PartID = 0;
	/**
	 * 工序名称
	 */
	public String StepName = "";
	/**
	 * 修程
	 */
	public String LineName = "";
	/**
	 * 车号
	 */
	public String PartNo = "";
	/**
	 * 状态
	 */
	public int Status = 0;
	/**
	 * 自检总数
	 */
	public int TSize = 0;
	/**
	 * 自检完成数
	 */
	public int SelfFSize = 0;
	/**
	 * 互检完成数
	 */
	public int MFSize = 0;
	/**
	 * 专检完成数
	 */
	public int SFSize = 0;
	/**
	 * 预检总数
	 */
	public int PTSize = 0;
	/**
	 * 预检完成数
	 */
	public int PFSize = 0;
	/**
	 * 订单ID
	 */
	public int OrderID = 0;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getType() {
		return Type;
	}

	public void setType(int type) {
		Type = type;
	}

	public String getPartName() {
		return PartName;
	}

	public void setPartName(String partName) {
		PartName = partName;
	}

	public int getPartID() {
		return PartID;
	}

	public void setPartID(int partID) {
		PartID = partID;
	}

	public String getStepName() {
		return StepName;
	}

	public void setStepName(String stepName) {
		StepName = stepName;
	}

	public String getLineName() {
		return LineName;
	}

	public void setLineName(String lineName) {
		LineName = lineName;
	}

	public String getPartNo() {
		return PartNo;
	}

	public void setPartNo(String partNo) {
		PartNo = partNo;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public int getTSize() {
		return TSize;
	}

	public void setTSize(int tSize) {
		TSize = tSize;
	}

	public int getSelfFSize() {
		return SelfFSize;
	}

	public void setSelfFSize(int selfFSize) {
		SelfFSize = selfFSize;
	}

	public int getMFSize() {
		return MFSize;
	}

	public void setMFSize(int mFSize) {
		MFSize = mFSize;
	}

	public int getSFSize() {
		return SFSize;
	}

	public void setSFSize(int sFSize) {
		SFSize = sFSize;
	}

	public int getPTSize() {
		return PTSize;
	}

	public void setPTSize(int pTSize) {
		PTSize = pTSize;
	}

	public int getPFSize() {
		return PFSize;
	}

	public void setPFSize(int pFSize) {
		PFSize = pFSize;
	}

	public int getOrdreID() {
		return OrderID;
	}

	public void setOrdreID(int ordreID) {
		OrderID = ordreID;
	}
}
