package com.mes.loco.aps.server.service.po.aps;

import java.io.Serializable;
import java.util.Calendar;

import com.mes.loco.aps.server.service.po.bpm.BPMTaskBase;

/**
 * 日计划取消日志
 */
public class APSTaskStepCancelLogBPM extends BPMTaskBase implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 订单ID
	 */
	public int OrderID = 0;
	/**
	 * 车号
	 */
	public String PartNo = "";
	/**
	 * 工位
	 */
	public int PartID = 0;
	/**
	 * 工位
	 */
	public String PartName = "";
	/**
	 * 工序
	 */
	public String StepIDs = "";
	/**
	 * 工序
	 */
	public String StepNames = "";
	/**
	 * 取消类型
	 */
	public int CancelType = 0;
	/**
	 * 取消类型
	 */
	public String CancelTypeName = "";
	/**
	 * 描述信息
	 */
	public String DescribeInfo = "";

	// 辅助属性
	public int LineID = 0;
	public String LineName = "";
	public int ProductID = 0;
	public String ProductNo = "";
	public int CustomerID = 0;
	public String Customer = "";

	public APSTaskStepCancelLogBPM() {
		super();
	}

	public int getOrderID() {
		return OrderID;
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

	public String getPartNo() {
		return PartNo;
	}

	public int getPartID() {
		return PartID;
	}

	public String getPartName() {
		return PartName;
	}

	public String getStepIDs() {
		return StepIDs;
	}

	public String getStepNames() {
		return StepNames;
	}

	public int getCancelType() {
		return CancelType;
	}

	public String getCancelTypeName() {
		return CancelTypeName;
	}

	public Calendar getCreateTime() {
		return CreateTime;
	}

	public void setOrderID(int orderID) {
		OrderID = orderID;
	}

	public void setPartNo(String partNo) {
		PartNo = partNo;
	}

	public void setPartID(int partID) {
		PartID = partID;
	}

	public void setPartName(String partName) {
		PartName = partName;
	}

	public void setStepIDs(String stepIDs) {
		StepIDs = stepIDs;
	}

	public void setStepNames(String stepNames) {
		StepNames = stepNames;
	}

	public void setCancelType(int cancelType) {
		CancelType = cancelType;
	}

	public void setCancelTypeName(String cancelTypeName) {
		CancelTypeName = cancelTypeName;
	}

	public String getDescribeInfo() {
		return DescribeInfo;
	}

	public void setDescribeInfo(String describeInfo) {
		DescribeInfo = describeInfo;
	}

	public void setCreateTime(Calendar createTime) {
		CreateTime = createTime;
	}

	public int getProductID() {
		return ProductID;
	}

	public void setProductID(int productID) {
		ProductID = productID;
	}

	public String getProductNo() {
		return ProductNo;
	}

	public void setProductNo(String productNo) {
		ProductNo = productNo;
	}

	public int getCustomerID() {
		return CustomerID;
	}

	public void setCustomerID(int customerID) {
		CustomerID = customerID;
	}

	public String getCustomer() {
		return Customer;
	}

	public void setCustomer(String customer) {
		Customer = customer;
	}
}
