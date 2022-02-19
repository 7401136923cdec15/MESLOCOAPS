package com.mes.loco.aps.server.service.po.aps;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class APSStepPlanParam implements Serializable {
	private static final long serialVersionUID = 1L;

	public int OrderID = 0;
	public int PartID = 0;
	public int APSShiftPeriod = 0;
	public Calendar StartTime = Calendar.getInstance();
	public Calendar EndTime = Calendar.getInstance();
	public int WorkDay = 0;
	public int LimitMinutes = 0;
	public List<APSTaskPart> MutualTaskList = new ArrayList<APSTaskPart>();

	public APSStepPlanParam() {
	}

	public int getOrderID() {
		return OrderID;
	}

	public int getPartID() {
		return PartID;
	}

	public int getAPSShiftPeriod() {
		return APSShiftPeriod;
	}

	public Calendar getStartTime() {
		return StartTime;
	}

	public Calendar getEndTime() {
		return EndTime;
	}

	public int getWorkDay() {
		return WorkDay;
	}

	public int getLimitMinutes() {
		return LimitMinutes;
	}

	public List<APSTaskPart> getMutualTaskList() {
		return MutualTaskList;
	}

	public void setOrderID(int orderID) {
		OrderID = orderID;
	}

	public void setPartID(int partID) {
		PartID = partID;
	}

	public void setAPSShiftPeriod(int aPSShiftPeriod) {
		APSShiftPeriod = aPSShiftPeriod;
	}

	public void setStartTime(Calendar startTime) {
		StartTime = startTime;
	}

	public void setEndTime(Calendar endTime) {
		EndTime = endTime;
	}

	public void setWorkDay(int workDay) {
		WorkDay = workDay;
	}

	public void setLimitMinutes(int limitMinutes) {
		LimitMinutes = limitMinutes;
	}

	public void setMutualTaskList(List<APSTaskPart> mutualTaskList) {
		MutualTaskList = mutualTaskList;
	}
}
