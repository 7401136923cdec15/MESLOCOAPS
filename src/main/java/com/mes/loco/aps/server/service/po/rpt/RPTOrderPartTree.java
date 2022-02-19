package com.mes.loco.aps.server.service.po.rpt;

import java.util.ArrayList;
import java.util.List;

public class RPTOrderPartTree {
	public int LineID = 0;
	public String LineName = "";
	public List<RPTOrderPart> TreeList = new ArrayList<RPTOrderPart>();

	public RPTOrderPartTree() {
		super();
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

	public List<RPTOrderPart> getRPTOrderPartList() {
		return TreeList;
	}

	public void setRPTOrderPartList(List<RPTOrderPart> rPTOrderPartList) {
		TreeList = rPTOrderPartList;
	}

}
