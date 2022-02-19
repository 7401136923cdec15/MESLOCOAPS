package com.mes.loco.aps.server.service.po.aps;

import java.io.Serializable;
import java.util.Calendar;

/**
 * 车辆进厂触发任务的工位
 * 
 * @author YouWang·Peng
 * @CreateTime 2021-11-30 14:19:31
 */
public class APSTriggerPart implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	public int ID = 0;
	public int ProductID = 0;
	public int LineID = 0;
	public int CustomerID = 0;
	public int PartID = 0;
	public int CreateID = 0;
	public Calendar CreateTime = Calendar.getInstance();
	public int Active = 0;

	// 辅助属性
	public String ProductNo = "";
	public String LineName = "";
	public String CustomerName = "";
	public String PartName = "";
	public String CreateName = "";

	public APSTriggerPart() {
		super();
	}

	public APSTriggerPart(int iD, int productID, int lineID, int customerID, int partID, int createID,
			Calendar createTime, int active, String productNo, String lineName, String customerName, String partName,
			String createName) {
		super();
		ID = iD;
		ProductID = productID;
		LineID = lineID;
		CustomerID = customerID;
		PartID = partID;
		CreateID = createID;
		CreateTime = createTime;
		Active = active;
		ProductNo = productNo;
		LineName = lineName;
		CustomerName = customerName;
		PartName = partName;
		CreateName = createName;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getCreateID() {
		return CreateID;
	}

	public void setCreateID(int createID) {
		CreateID = createID;
	}

	public Calendar getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(Calendar createTime) {
		CreateTime = createTime;
	}

	public int getActive() {
		return Active;
	}

	public void setActive(int active) {
		Active = active;
	}

	public String getCreateName() {
		return CreateName;
	}

	public void setCreateName(String createName) {
		CreateName = createName;
	}

	public int getProductID() {
		return ProductID;
	}

	public void setProductID(int productID) {
		ProductID = productID;
	}

	public int getLineID() {
		return LineID;
	}

	public void setLineID(int lineID) {
		LineID = lineID;
	}

	public int getCustomerID() {
		return CustomerID;
	}

	public void setCustomerID(int customerID) {
		CustomerID = customerID;
	}

	public int getPartID() {
		return PartID;
	}

	public void setPartID(int partID) {
		PartID = partID;
	}

	public String getProductNo() {
		return ProductNo;
	}

	public void setProductNo(String productNo) {
		ProductNo = productNo;
	}

	public String getLineName() {
		return LineName;
	}

	public void setLineName(String lineName) {
		LineName = lineName;
	}

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}

	public String getPartName() {
		return PartName;
	}

	public void setPartName(String partName) {
		PartName = partName;
	}
}
