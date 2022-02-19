package com.mes.loco.aps.server.service.po.fpc;

import java.io.Serializable;
import java.util.ArrayList;

import com.mes.loco.aps.server.service.po.aps.APSManuCapacityStep;

import java.util.*;

public class FPCCapacityStepTemp implements Serializable {

	private static final long serialVersionUID = 1L;

	public int OrderID = 0;
	public int RouteID = 0;
	public int PartID = 0;
	public List<APSManuCapacityStep> List = new ArrayList<APSManuCapacityStep>();

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

	public int getRouteID() {
		return RouteID;
	}

	public void setRouteID(int routeID) {
		RouteID = routeID;
	}

	public List<APSManuCapacityStep> getList() {
		return List;
	}

	public void setList(List<APSManuCapacityStep> list) {
		List = list;
	}

}
