package com.mes.loco.aps.server.service.po.aps;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 台车BOM创建日志
 */
public class APSBOMLog implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	public int ID = 0;
	/**
	 * 车型ID
	 */
	public int ProductID = 0;
	/**
	 * 车型
	 */
	public String ProductNo = "";
	/**
	 * 修程ID
	 */
	public int LineID = 0;
	/**
	 * 修程
	 */
	public String LineName = "";
	/**
	 * 局段ID
	 */
	public int CustomerID = 0;
	/**
	 * 局段
	 */
	public String CustomerName = "";
	/**
	 * 订单号
	 */
	public int OrderID = 0;
	/**
	 * 车号
	 */
	public String PartNo = "";
	/**
	 * WBS元素
	 */
	public String WBSNo = "";
	/**
	 * 1成功 2失败
	 */
	public int Status = 0;
	/**
	 * 提示信息
	 */
	public String Msg = "";
	/**
	 * 创建时间
	 */
	public Calendar CreateTime = Calendar.getInstance();
	/**
	 * 错误列表
	 */
	public List<String> ErrorList = new ArrayList<String>();

	public APSBOMLog() {
	}

	public APSBOMLog(int iD, int productID, String productNo, int lineID, String lineName, int customerID,
			String customerName, int orderID, String partNo, String wBSNo, int status, String msg,
			Calendar createTime) {
		super();
		ID = iD;
		ProductID = productID;
		ProductNo = productNo;
		LineID = lineID;
		LineName = lineName;
		CustomerID = customerID;
		CustomerName = customerName;
		OrderID = orderID;
		PartNo = partNo;
		WBSNo = wBSNo;
		Status = status;
		Msg = msg;
		CreateTime = createTime;
	}

	public int getID() {
		return ID;
	}

	public int getProductID() {
		return ProductID;
	}

	public String getProductNo() {
		return ProductNo;
	}

	public int getLineID() {
		return LineID;
	}

	public String getLineName() {
		return LineName;
	}

	public int getCustomerID() {
		return CustomerID;
	}

	public String getCustomerName() {
		return CustomerName;
	}

	public int getOrderID() {
		return OrderID;
	}

	public String getPartNo() {
		return PartNo;
	}

	public String getWBSNo() {
		return WBSNo;
	}

	public int getStatus() {
		return Status;
	}

	public String getMsg() {
		return Msg;
	}

	public Calendar getCreateTime() {
		return CreateTime;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public void setProductID(int productID) {
		ProductID = productID;
	}

	public void setProductNo(String productNo) {
		ProductNo = productNo;
	}

	public void setLineID(int lineID) {
		LineID = lineID;
	}

	public void setLineName(String lineName) {
		LineName = lineName;
	}

	public void setCustomerID(int customerID) {
		CustomerID = customerID;
	}

	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}

	public void setOrderID(int orderID) {
		OrderID = orderID;
	}

	public void setPartNo(String partNo) {
		PartNo = partNo;
	}

	public void setWBSNo(String wBSNo) {
		WBSNo = wBSNo;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public void setMsg(String msg) {
		Msg = msg;
	}

	public void setCreateTime(Calendar createTime) {
		CreateTime = createTime;
	}

	public List<String> getErrorList() {
		return ErrorList;
	}

	public void setErrorList(List<String> errorList) {
		ErrorList = errorList;
	}
}
