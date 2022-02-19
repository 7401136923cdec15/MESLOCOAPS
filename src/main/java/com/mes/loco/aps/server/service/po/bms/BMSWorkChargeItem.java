package com.mes.loco.aps.server.service.po.bms;

import java.io.Serializable;

public class BMSWorkChargeItem implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	public int ID = 0;
	/**
	 * 班组工位外键
	 */
	public int WorkChargeID = 0;
	/**
	 * 车型ID
	 */
	public int ProductID = 0;
	/**
	 * 车型
	 */
	public String ProductNo = "";

	public BMSWorkChargeItem() {
		super();
	}

	public BMSWorkChargeItem(int iD, int workChargeID, int productID, String productNo) {
		super();
		ID = iD;
		WorkChargeID = workChargeID;
		ProductID = productID;
		ProductNo = productNo;
	}

	public int getID() {
		return ID;
	}

	public int getWorkChargeID() {
		return WorkChargeID;
	}

	public int getProductID() {
		return ProductID;
	}

	public String getProductNo() {
		return ProductNo;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public void setWorkChargeID(int workChargeID) {
		WorkChargeID = workChargeID;
	}

	public void setProductID(int productID) {
		ProductID = productID;
	}

	public void setProductNo(String productNo) {
		ProductNo = productNo;
	}
}
