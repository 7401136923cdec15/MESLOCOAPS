package com.mes.loco.aps.server.service.po.fpc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.*;

public class FPCPartTemp implements Serializable {

	private static final long serialVersionUID = 1L;

	public int RouteID = 0;
	public int PartID = 0;
	public List<FPCPart> List = new ArrayList<FPCPart>();

	public int getPartID() {
		return PartID;
	}

	public int getRouteID() {
		return RouteID;
	}

	public void setRouteID(int routeID) {
		RouteID = routeID;
	}

	public void setPartID(int partID) {
		PartID = partID;
	}

	public List<FPCPart> getList() {
		return List;
	}

	public void setList(List<FPCPart> list) {
		List = list;
	}

}
