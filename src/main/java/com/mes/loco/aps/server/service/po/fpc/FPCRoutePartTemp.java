package com.mes.loco.aps.server.service.po.fpc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.*;

public class FPCRoutePartTemp implements Serializable {

	private static final long serialVersionUID = 1L;

	public int OrderID = 0;
	public int RouteID = 0;
	public int PartID = 0;
	public List<FPCRoutePartPoint> List = new ArrayList<FPCRoutePartPoint>();

	public int getOrderID() {
		return OrderID;
	}

	public int getPartID() {
		return PartID;
	}

	public List<FPCRoutePartPoint> getList() {
		return List;
	}

	public int getRouteID() {
		return RouteID;
	}

	public void setRouteID(int routeID) {
		RouteID = routeID;
	}

	public void setOrderID(int orderID) {
		OrderID = orderID;
	}

	public void setPartID(int partID) {
		PartID = partID;
	}

	public void setList(List<FPCRoutePartPoint> list) {
		List = list;
	}

}
