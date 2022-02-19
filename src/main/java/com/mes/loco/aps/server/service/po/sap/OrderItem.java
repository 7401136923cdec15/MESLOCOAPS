package com.mes.loco.aps.server.service.po.sap;

import java.io.Serializable;

/**
 * SAP需求的订单输入结构
 * 
 * @author YouWang·Peng
 * @CreateTime 2021-1-4 14:18:00
 * @LastEditTime 2021-1-4 14:18:04
 *
 */
public class OrderItem implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * WBS元素
	 */
	public String PSPNR = "";
	/**
	 * 车型
	 */
	public String ZCHEX = "";
	/**
	 * 入厂时间
	 */
	public String ZRC_DATE = "";
	/**
	 * 完工日期
	 */
	public String ZWG_DATE = "";
	/**
	 * 台车号
	 */
	public String ZTCH = "";

	public OrderItem() {
	}

	public void setPSPNR(String pSPNR) {
		PSPNR = pSPNR;
	}

	public void setZCHEX(String zCHEX) {
		ZCHEX = zCHEX;
	}

	public void setZRC_DATE(String zRC_DATE) {
		ZRC_DATE = zRC_DATE;
	}

	public void setZWG_DATE(String zWG_DATE) {
		ZWG_DATE = zWG_DATE;
	}

	public void setZTCH(String zTCH) {
		ZTCH = zTCH;
	}
}
