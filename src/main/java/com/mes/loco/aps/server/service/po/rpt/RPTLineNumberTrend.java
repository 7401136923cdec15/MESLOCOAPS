package com.mes.loco.aps.server.service.po.rpt;

/**
 * 机车进出厂修程数量统计
 * 
 * @author PengYouWang
 * @CreateTime 2020-7-13 23:41:44
 * @LastEditTime 2020-7-13 23:41:48
 *
 */
public class RPTLineNumberTrend {
	/**
	 * 进厂台车数
	 */
	public int EnterPlant = 0;
	/**
	 * 出厂台车数
	 */
	public int OutPlant = 0;
	/**
	 * 在厂台车数
	 */
	public int InPlant = 0;
	/**
	 * 修程ID
	 */
	public int LineID = 0;
	/**
	 * 修程名称
	 */
	public String LineName = "";

	public RPTLineNumberTrend() {
	}

	public int getEnterPlant() {
		return EnterPlant;
	}

	public void setEnterPlant(int enterPlant) {
		EnterPlant = enterPlant;
	}

	public int getOutPlant() {
		return OutPlant;
	}

	public void setOutPlant(int outPlant) {
		OutPlant = outPlant;
	}

	public int getInPlant() {
		return InPlant;
	}

	public void setInPlant(int inPlant) {
		InPlant = inPlant;
	}

	public int getLineID() {
		return LineID;
	}

	public void setLineID(int lineID) {
		LineID = lineID;
	}

	public String getLineName() {
		return LineName;
	}

	public void setLineName(String lineName) {
		LineName = lineName;
	}
}
