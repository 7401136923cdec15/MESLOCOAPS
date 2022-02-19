package com.mes.loco.aps.server.service.po.andon;

import java.io.Serializable;

/**
 * 停时统计
 * 
 * @author YouWang·Peng
 * @CreateTime 2021-8-31 09:45:57
 */
public class AndonStopStatistics implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	public int LineID = 0;
	public String LineName = "";

	public int LJDC = 0;
	public int BYJG = 0;
	public int LJJG = 0;
	public double DBJXTS = 0;
	public double SJJXTS = 0;
	public double ZCTS = 0;
	public double ZTTS = 0;
	public double YHTS = 0;

	public AndonStopStatistics() {
		super();
	}

	public AndonStopStatistics(int lineID, String lineName, int lJDC, int bYJG, int lJJG, double dBJXTS, double sJJXTS,
			double zCTS, double zTTS, double yHTS) {
		super();
		LineID = lineID;
		LineName = lineName;
		LJDC = lJDC;
		BYJG = bYJG;
		LJJG = lJJG;
		DBJXTS = dBJXTS;
		SJJXTS = sJJXTS;
		ZCTS = zCTS;
		ZTTS = zTTS;
		YHTS = yHTS;
	}

	public int getLineID() {
		return LineID;
	}

	public String getLineName() {
		return LineName;
	}

	public int getLJDC() {
		return LJDC;
	}

	public int getBYJG() {
		return BYJG;
	}

	public int getLJJG() {
		return LJJG;
	}

	public double getDBJXTS() {
		return DBJXTS;
	}

	public double getSJJXTS() {
		return SJJXTS;
	}

	public double getZCTS() {
		return ZCTS;
	}

	public double getZTTS() {
		return ZTTS;
	}

	public double getYHTS() {
		return YHTS;
	}

	public void setLineID(int lineID) {
		LineID = lineID;
	}

	public void setLineName(String lineName) {
		LineName = lineName;
	}

	public void setLJDC(int lJDC) {
		LJDC = lJDC;
	}

	public void setBYJG(int bYJG) {
		BYJG = bYJG;
	}

	public void setLJJG(int lJJG) {
		LJJG = lJJG;
	}

	public void setDBJXTS(double dBJXTS) {
		DBJXTS = dBJXTS;
	}

	public void setSJJXTS(double sJJXTS) {
		SJJXTS = sJJXTS;
	}

	public void setZCTS(double zCTS) {
		ZCTS = zCTS;
	}

	public void setZTTS(double zTTS) {
		ZTTS = zTTS;
	}

	public void setYHTS(double yHTS) {
		YHTS = yHTS;
	}

	@Override
	public String toString() {
		return "AndonStopStatistics [LineID=" + LineID + ", LineName=" + LineName + ", LJDC=" + LJDC + ", BYJG=" + BYJG
				+ ", LJJG=" + LJJG + ", DBJXTS=" + DBJXTS + ", SJJXTS=" + SJJXTS + ", ZCTS=" + ZCTS + ", ZTTS=" + ZTTS
				+ ", YHTS=" + YHTS + "]";
	}

}
