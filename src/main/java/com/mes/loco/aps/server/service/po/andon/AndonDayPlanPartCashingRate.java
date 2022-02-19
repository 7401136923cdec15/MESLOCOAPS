package com.mes.loco.aps.server.service.po.andon;

import java.io.Serializable;

/**
 * 工位日计划兑现率
 */

public class AndonDayPlanPartCashingRate implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 工位ID
	 */
	public int PartID = 0;
	/**
	 * 工位名称
	 */
	public String PartName = "";
	/**
	 * 工位日计划完成数
	 */
	public int FinishNumber = 0;
	/**
	 * 工位日计划数
	 */
	public int TotalNumber = 0;
	/**
	 * 兑现率
	 */
	public double Rate = 0.0;
	/**
	 * 班次
	 */
	public int ShfitID = 0;

	public AndonDayPlanPartCashingRate() {
		super();
	}

	public AndonDayPlanPartCashingRate(int partID, String partName, int finishNumber, int totalNumber, double rate) {
		super();
		PartID = partID;
		PartName = partName;
		FinishNumber = finishNumber;
		TotalNumber = totalNumber;
		Rate = rate;
	}

	@Override
	public String toString() {
		return "AndonDayPlanPartCashingRate [PartID=" + PartID + ", PartName=" + PartName + ", FinishNumber="
				+ FinishNumber + ", TotalNumber=" + TotalNumber + ", Rate=" + Rate + "]";
	}

	public int getPartID() {
		return PartID;
	}

	public String getPartName() {
		return PartName;
	}

	public int getShfitID() {
		return ShfitID;
	}

	public void setShfitID(int shfitID) {
		ShfitID = shfitID;
	}

	public int getFinishNumber() {
		return FinishNumber;
	}

	public int getTotalNumber() {
		return TotalNumber;
	}

	public double getRate() {
		return Rate;
	}

	public void setPartID(int partID) {
		PartID = partID;
	}

	public void setPartName(String partName) {
		PartName = partName;
	}

	public void setFinishNumber(int finishNumber) {
		FinishNumber = finishNumber;
	}

	public void setTotalNumber(int totalNumber) {
		TotalNumber = totalNumber;
	}

	public void setRate(double rate) {
		Rate = rate;
	}
}
