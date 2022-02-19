package com.mes.loco.aps.server.service.po.fpc;

import java.io.Serializable;

public class StepPlanTableTemp implements Serializable {

	private static final long serialVersionUID = 1L;

	public int OrderID = 0;
	public int PartID = 0;
	public Object Data = new Object();

	public int getOrderID() {
		return OrderID;
	}

	public int getPartID() {
		return PartID;
	}

	public void setOrderID(int orderID) {
		OrderID = orderID;
	}

	public void setPartID(int partID) {
		PartID = partID;
	}

	public Object getData() {
		return Data;
	}

	public void setData(Object data) {
		Data = data;
	}
}
