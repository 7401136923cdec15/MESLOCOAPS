package com.mes.loco.aps.server.service.po.andon;

import java.io.Serializable;

/**
 * 生产日报第一页
 */
public class AndonDayReportTJ implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 全年累计修竣
	 */
	public int LJXJ = 0;
	/**
	 * 累计修竣C5
	 */
	public int LJXJ_C5 = 0;
	/**
	 * C5平均检修停时
	 */
	public double JXTS_C5 = 0.0;
	/**
	 * 累计修竣C6
	 */
	public int LJXJ_C6 = 0;
	/**
	 * C6平均检修停时
	 */
	public double JXTS_C6 = 0.0;
	/**
	 * 本月竣工
	 */
	public int BYJG = 0;
	/**
	 * 在厂机车
	 */
	public int ZCJC = 0;
	/**
	 * 在修机车
	 */
	public int ZXJC = 0;

	public AndonDayReportTJ() {
		super();
	}

	public AndonDayReportTJ(int lJXJ, int lJXJ_C5, double jXTS_C5, int lJXJ_C6, double jXTS_C6, int bYJG, int zCJC,
			int zXJC) {
		super();
		LJXJ = lJXJ;
		LJXJ_C5 = lJXJ_C5;
		JXTS_C5 = jXTS_C5;
		LJXJ_C6 = lJXJ_C6;
		JXTS_C6 = jXTS_C6;
		BYJG = bYJG;
		ZCJC = zCJC;
		ZXJC = zXJC;
	}

	public int getLJXJ() {
		return LJXJ;
	}

	public int getLJXJ_C5() {
		return LJXJ_C5;
	}

	public double getJXTS_C5() {
		return JXTS_C5;
	}

	public int getLJXJ_C6() {
		return LJXJ_C6;
	}

	public double getJXTS_C6() {
		return JXTS_C6;
	}

	public int getBYJG() {
		return BYJG;
	}

	public int getZCJC() {
		return ZCJC;
	}

	public int getZXJC() {
		return ZXJC;
	}

	public void setLJXJ(int lJXJ) {
		LJXJ = lJXJ;
	}

	public void setLJXJ_C5(int lJXJ_C5) {
		LJXJ_C5 = lJXJ_C5;
	}

	public void setJXTS_C5(double jXTS_C5) {
		JXTS_C5 = jXTS_C5;
	}

	public void setLJXJ_C6(int lJXJ_C6) {
		LJXJ_C6 = lJXJ_C6;
	}

	public void setJXTS_C6(double jXTS_C6) {
		JXTS_C6 = jXTS_C6;
	}

	public void setBYJG(int bYJG) {
		BYJG = bYJG;
	}

	public void setZCJC(int zCJC) {
		ZCJC = zCJC;
	}

	public void setZXJC(int zXJC) {
		ZXJC = zXJC;
	}
}
