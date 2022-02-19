package com.mes.loco.aps.server.service.po.sfc;

import com.mes.loco.aps.server.service.po.sfc.SFCSequentialInfo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 机车电子履历
 */
public class SFCSequentialInfo implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 电子履历行号
	 */
	public int ID = 0;
	/**
	 * 订单行号
	 */
	public int OrderID = 0;
	/**
	 * 订单编号
	 */
	public String OrderNo = "";
	/**
	 * 完整车号
	 */
	public String PartNo = "";
	/**
	 * 操作员ID集合
	 */
	public List<Integer> OperateIDList = new ArrayList<>();
	/**
	 * 操作员名称(多个逗号分隔)
	 */
	public String Operators = "";
	/**
	 * 履历类型ID
	 */
	public int Type = 0;
	/**
	 * 履历类型名称
	 */
	public String TypeText = "";
	/**
	 * 描述
	 */
	public String Text = "";
	/**
	 * 工位ID
	 */
	public int StationID = 0;
	/**
	 * 工位名称
	 */
	public String StationName = "";
	/**
	 * 工序ID
	 */
	public int StepID = 0;
	/**
	 * 工序名称
	 */
	public String StepName = "";
	/**
	 * 开始时刻
	 */
	public Calendar StartTime = Calendar.getInstance();
	/**
	 * 结束时刻
	 */
	public Calendar EndTime = Calendar.getInstance();

	/**
	 * 工位顺序
	 */
	public int PartOrder = 0;

	public SFCSequentialInfo() {
	}

	public SFCSequentialInfo(int iD, int orderID, String orderNo, String partNo, List<Integer> operateID,
			String operators, int type, String typeText, String text, int stationID, String stationName, int stepID,
			String stepName, Calendar startTime, Calendar endTime) {
		this.ID = iD;
		this.OrderID = orderID;
		this.OrderNo = orderNo;
		this.PartNo = partNo;
		this.OperateIDList = operateID;
		this.Operators = operators;
		this.Type = type;
		this.TypeText = typeText;
		this.Text = text;
		this.StationID = stationID;
		this.StationName = stationName;
		this.StepID = stepID;
		this.StepName = stepName;
		this.StartTime = startTime;
		this.EndTime = endTime;
	}

	public int getStationID() {
		return this.StationID;
	}

	public void setStationID(int stationID) {
		this.StationID = stationID;
	}

	public int getPartOrder() {
		return PartOrder;
	}

	public void setPartOrder(int partOrder) {
		PartOrder = partOrder;
	}

	public String getStationName() {
		return this.StationName;
	}

	public void setStationName(String stationName) {
		this.StationName = stationName;
	}

	public int getStepID() {
		return this.StepID;
	}

	public void setStepID(int stepID) {
		this.StepID = stepID;
	}

	public String getStepName() {
		return this.StepName;
	}

	public void setStepName(String stepName) {
		this.StepName = stepName;
	}

	public Calendar getStartTime() {
		return this.StartTime;
	}

	public void setStartTime(Calendar startTime) {
		this.StartTime = startTime;
	}

	public Calendar getEndTime() {
		return this.EndTime;
	}

	public void setEndTime(Calendar endTime) {
		this.EndTime = endTime;
	}

	public int getID() {
		return this.ID;
	}

	public void setID(int iD) {
		this.ID = iD;
	}

	public int getOrderID() {
		return this.OrderID;
	}

	public void setOrderID(int orderID) {
		this.OrderID = orderID;
	}

	public String getOrderNo() {
		return this.OrderNo;
	}

	public void setOrderNo(String orderNo) {
		this.OrderNo = orderNo;
	}

	public String getPartNo() {
		return this.PartNo;
	}

	public void setPartNo(String partNo) {
		this.PartNo = partNo;
	}

	public List<Integer> getOperateIDList() {
		return this.OperateIDList;
	}

	public void setOperateIDList(List<Integer> operateIDList) {
		this.OperateIDList = operateIDList;
	}

	public String getOperators() {
		return this.Operators;
	}

	public void setOperators(String operators) {
		this.Operators = operators;
	}

	public int getType() {
		return this.Type;
	}

	public void setType(int type) {
		this.Type = type;
	}

	public String getTypeText() {
		return this.TypeText;
	}

	public void setTypeText(String typeText) {
		this.TypeText = typeText;
	}

	public String getText() {
		return this.Text;
	}

	public void setText(String text) {
		this.Text = text;
	}
}
