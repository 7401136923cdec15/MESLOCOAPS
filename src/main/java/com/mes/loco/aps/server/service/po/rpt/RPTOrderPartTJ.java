package com.mes.loco.aps.server.service.po.rpt;

public class RPTOrderPartTJ {

	/**
	 * 月shiftID
	 */
	public int ShiftID = 0;
	public int LineID;
	public String LineName = "";
	public int PartID = 0;
	public int PlantCount = 0;
	public int FinshCount = 0;
	public int LaterCount = 0;

	public int getShiftID() {
		return ShiftID;
	}

	public void setShiftID(int shiftID) {
		ShiftID = shiftID;
	}

	public int getLineID() {
		return LineID;
	}

	public void setLineID(int lineID) {
		LineID = lineID;
	}

	public int getPartID() {
		return PartID;
	}

	public void setPartID(int partID) {
		PartID = partID;
	}

	public int getPlantCount() {
		return PlantCount;
	}

	public String getLineName() {
		return LineName;
	}

	public void setLineName(String lineName) {
		LineName = lineName;
	}

	public void setPlantCount(int plantCount) {
		PlantCount = plantCount;
	}

	public int getFinshCount() {
		return FinshCount;
	}

	public void setFinshCount(int finshCount) {
		FinshCount = finshCount;
	}

	public int getLaterCount() {
		return LaterCount;
	}

	public void setLaterCount(int laterCount) {
		LaterCount = laterCount;
	}
}
