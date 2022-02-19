package com.mes.loco.aps.server.service.po.rpt;

import java.util.ArrayList;
import java.util.List;

public class RPTOrderPartMonth {

	public int MonthID = 0;
	public String Month = "";
	public List<RPTOrderPartLine> TreeList = new ArrayList<RPTOrderPartLine>();

	public String getMonth() {
		return Month;
	}

	public void setMonth(String month) {
		Month = month;
	}

	public int getMonthID() {
		return MonthID;
	}

	public void setMonthID(int monthID) {
		MonthID = monthID;
	}

	public List<RPTOrderPartLine> getTreeList() {
		return TreeList;
	}

	public void setTreeList(List<RPTOrderPartLine> treeList) {
		TreeList = treeList;
	}
}
