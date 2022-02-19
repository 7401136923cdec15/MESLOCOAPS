package com.mes.loco.aps.server.service.po.ncr;

import java.io.Serializable;

import com.mes.loco.aps.server.service.po.bpm.BPMTaskBase;

/**
 * NCR申请单
 * 
 * @author ShrisJava
 *
 */
public class SendNCRTask extends BPMTaskBase implements Serializable {
	/**
	 * 序列化ID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 订单ID
	 */
	public int OrderID = 0;

	/**
	 * 客户ID (局段)
	 */
	public int CustomerID = 0;
	/**
	 * 客户名称（局段名称）
	 */
	public String CustomerName = "";
	/**
	 * 修程ID
	 */
	public int LineID = 0;
	/**
	 * 修程
	 */
	public String LineName = "";
	/**
	 * 工位ID
	 */
	public int StationID = 0;
	/**
	 * 工位
	 */
	public String StationName = "";
	/**
	 * 车型ID
	 */
	public int CarTypeID;
	/**
	 * 车型名称
	 */
	public String CarType = "";
	/**
	 * 车号
	 */
	public String CarNumber = "";
	/**
	 * 不合格品数量
	 */
	public int Number;
	/**
	 * 发生部门ID
	 */
	public int DepartmentID;
	/**
	 * 发生部门名称
	 */
	public String Department = "";
	/**
	 * 产品不合格描述
	 */
	public String DescribeInfo = "";
	
	/**
	 * 关联NCRID
	 */
	public int NCRID;
	
	/**
	 * 关联NCR单据编号
	 */
	public String NCRCode;
	
	/**
	 * 图片
	 */
	public String ImageList="";
	

	public SendNCRTask() {
		super();
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getCode() {
		return Code;
	}

	public void setCode(String code) {
		Code = code;
	}

	public int getCarTypeID() {
		return CarTypeID;
	}

	public void setCarTypeID(int carTypeID) {
		CarTypeID = carTypeID;
	}

	public String getCarType() {
		return CarType;
	}

	public void setCarType(String carType) {
		CarType = carType;
	}

	public String getCarNumber() {
		return CarNumber;
	}

	public void setCarNumber(String carNumber) {
		CarNumber = carNumber;
	}

	public int getNumber() {
		return Number;
	}

	public void setNumber(int number) {
		Number = number;
	}

	public int getDepartmentID() {
		return DepartmentID;
	}

	public void setDepartmentID(int departmentID) {
		DepartmentID = departmentID;
	}

	public String getDepartment() {
		return Department;
	}

	public void setDepartment(String department) {
		Department = department;
	}

	public String getDescribeInfo() {
		return DescribeInfo;
	}

	public void setDescribeInfo(String describeInfo) {
		DescribeInfo = describeInfo;
	}

	public int getOrderID() {
		return OrderID;
	}

	public void setOrderID(int orderID) {
		OrderID = orderID;
	}

	public int getCustomerID() {
		return CustomerID;
	}

	public void setCustomerID(int customerID) {
		CustomerID = customerID;
	}

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String customerName) {
		CustomerName = customerName;
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

	public int getStationID() {
		return StationID;
	}

	public void setStationID(int stationID) {
		StationID = stationID;
	}

	public String getStationName() {
		return StationName;
	}

	public void setStationName(String stationName) {
		StationName = stationName;
	}

	public int getNCRID() {
		return NCRID;
	}

	public void setNCRID(int nCRID) {
		NCRID = nCRID;
	}

	public String getNCRCode() {
		return NCRCode;
	}

	public void setNCRCode(String nCRCode) {
		NCRCode = nCRCode;
	}

	public String getImageList() {
		return ImageList;
	}

	public void setImageList(String imageList) {
		ImageList = imageList;
	}
}
