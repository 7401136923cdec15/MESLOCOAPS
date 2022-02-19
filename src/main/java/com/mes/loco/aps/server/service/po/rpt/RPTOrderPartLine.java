package com.mes.loco.aps.server.service.po.rpt;

import java.util.ArrayList;
import java.util.List;

public class RPTOrderPartLine {

	public int LineID;
	public String LineName = "";
	public String Month = "";
	public List<RPTOrderPartTJ> TreeList = new ArrayList<RPTOrderPartTJ>();

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

	public List<RPTOrderPartTJ> getTreeList() {
		return TreeList;
	}

	public void setTreeList(List<RPTOrderPartTJ> treeList) {
		TreeList = treeList;
	}
}
