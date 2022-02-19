package com.mes.loco.aps.server.service.po.sfc;

import java.io.Serializable;

import com.mes.loco.aps.server.service.po.oms.OMSOrder;

/**
 * 派工工位统计数据(供检察员查看)
 */
public class SFCTaskStepPart implements Serializable {

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
	 * 工位ID
	 */
	public int PartID = 0;
	/**
	 * 工位名称
	 */
	public String PartName = "";
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

	public SFCTaskStepPart() {
		super();
	}

	public SFCTaskStepPart(int orderID, OMSOrder order, String partNo, int partID, String partName, int stepSize,
			int toDispatch, int dispatched) {
		super();
		OrderID = orderID;
		Order = order;
		PartNo = partNo;
		PartID = partID;
		PartName = partName;
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

	public int getPartID() {
		return PartID;
	}

	public String getPartName() {
		return PartName;
	}

	public void setPartID(int partID) {
		PartID = partID;
	}

	public void setPartName(String partName) {
		PartName = partName;
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
