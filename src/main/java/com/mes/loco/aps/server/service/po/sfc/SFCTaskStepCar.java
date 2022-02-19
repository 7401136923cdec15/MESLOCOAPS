package com.mes.loco.aps.server.service.po.sfc;

import java.io.Serializable;

import com.mes.loco.aps.server.service.po.oms.OMSOrder;

/**
 * 派工车辆统计数据(供检察员查看)
 */
public class SFCTaskStepCar implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 订单ID
	 */
	public int OrderID = 0;
	/**
	 * 订单详情
	 */
	public OMSOrder Order = new OMSOrder();
	/**
	 * 车号
	 */
	public String PartNo = "";
	/**
	 * 工序总数
	 */
	public int StepSize = 0;
	/**
	 * 待派工数
	 */
	public int ToDispatch = 0;
	/**
	 * 已派工数
	 */
	public int Dispatched = 0;

	public SFCTaskStepCar() {
		super();
	}

	public SFCTaskStepCar(int orderID, OMSOrder order, String partNo, int stepSize, int toDispatch, int dispatched) {
		super();
		OrderID = orderID;
		Order = order;
		PartNo = partNo;
		StepSize = stepSize;
		ToDispatch = toDispatch;
		Dispatched = dispatched;
	}

	public int getOrderID() {
		return OrderID;
	}

	public OMSOrder getOrder() {
		return Order;
	}

	public String getPartNo() {
		return PartNo;
	}

	public int getStepSize() {
		return StepSize;
	}

	public int getToDispatch() {
		return ToDispatch;
	}

	public int getDispatched() {
		return Dispatched;
	}

	public void setOrderID(int orderID) {
		OrderID = orderID;
	}

	public void setOrder(OMSOrder order) {
		Order = order;
	}

	public void setPartNo(String partNo) {
		PartNo = partNo;
	}

	public void setStepSize(int stepSize) {
		StepSize = stepSize;
	}

	public void setToDispatch(int toDispatch) {
		ToDispatch = toDispatch;
	}

	public void setDispatched(int dispatched) {
		Dispatched = dispatched;
	}
}
