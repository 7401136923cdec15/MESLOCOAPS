package com.mes.loco.aps.server.service.po.bms;

import java.io.Serializable;

/**
 * 车型、班组、工位数据集合
 * 
 * @author ShrisJava
 *
 */
public class BMSProductClassPart implements Serializable {

	private static final long serialVersionUID = 1L;

	public int ProductID = 0;
	public String ProductNo = "";
	public int ClassID = 0;
	public int PartID = 0;

	public BMSProductClassPart(int productID, String productNo, int classID, int partID) {
		super();
		ProductID = productID;
		ProductNo = productNo;
		ClassID = classID;
		PartID = partID;
	}

	public BMSProductClassPart() {
		super();
	}

	public int getProductID() {
		return ProductID;
	}

	public String getProductNo() {
		return ProductNo;
	}

	public void setProductNo(String productNo) {
		ProductNo = productNo;
	}

	public int getClassID() {
		return ClassID;
	}

	public int getPartID() {
		return PartID;
	}

	public void setProductID(int productID) {
		ProductID = productID;
	}

	public void setClassID(int classID) {
		ClassID = classID;
	}

	public void setPartID(int partID) {
		PartID = partID;
	}

}
