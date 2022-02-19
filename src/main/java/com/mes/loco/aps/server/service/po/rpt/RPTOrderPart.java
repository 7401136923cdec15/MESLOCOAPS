package com.mes.loco.aps.server.service.po.rpt;

import java.util.Calendar;

import com.mes.loco.aps.server.service.po.oms.OMSOrder;

public class RPTOrderPart {
	public int OrderID = 0;
	/**
	 * 修程
	 */
	public int LineID;

	public String Line = "";

	public int CustomerID = 0;

	public String CustomerName = "";

	public String PartNo = "";

	public int PartID = 0;

	public String PartName = "";

	/**
	 * 月计划完工日期
	 */
	public Calendar PlantDate = Calendar.getInstance();

	/**
	 * 实际完工日期
	 */
	public Calendar RealDate = Calendar.getInstance();

	/**
	 * 延迟天数
	 */
	public int LaterDay = 0;

	// 辅助信息
	public OMSOrder Order = new OMSOrder();

	public int getOrderID() {
		return OrderID;
	}

	public void setOrderID(int orderID) {
		OrderID = orderID;
	}

	public int getLineID() {
		return LineID;
	}

	public void setLineID(int lineID) {
		LineID = lineID;
	}

	public String getLine() {
		return Line;
	}

	public void setLine(String line) {
		Line = line;
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

	public String getPartNo() {
		return PartNo;
	}

	public void setPartNo(String partNo) {
		PartNo = partNo;
	}

	public int getPartID() {
		return PartID;
	}

	public void setPartID(int partID) {
		PartID = partID;
	}

	public String getPartName() {
		return PartName;
	}

	public void setPartName(String partName) {
		PartName = partName;
	}

	public Calendar getPlantDate() {
		return PlantDate;
	}

	public void setPlantDate(Calendar plantDate) {
		PlantDate = plantDate;
	}

	public Calendar getRealDate() {
		return RealDate;
	}

	public void setRealDate(Calendar realDate) {
		RealDate = realDate;
	}

	public int getLaterDay() {
		return LaterDay;
	}

	public void setLaterDay(int laterDay) {
		LaterDay = laterDay;
	}

	public OMSOrder getOrder() {
		return Order;
	}

	public void setOrder(OMSOrder order) {
		Order = order;
	}
}
