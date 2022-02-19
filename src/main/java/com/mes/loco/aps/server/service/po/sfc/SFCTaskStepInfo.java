package com.mes.loco.aps.server.service.po.sfc;

import com.mes.loco.aps.server.service.po.sfc.SFCTaskStepInfo;
import java.io.Serializable;

/**
 * 工序任务详情
 * 
 * @author ShrisJava
 *
 */
public class SFCTaskStepInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 订单ID
	 */
	public int OrderID = 0;
	/**
	 * 订单号
	 */
	public String OrderNo = "";
	/**
	 * 车号
	 */
	public String PartNo = "";
	/**
	 * 工位ID
	 */
	public int StationID = 0;
	/**
	 * 工位名称
	 */
	public String StationName = "";
	/**
	 * 待派工数
	 */
	public int ToDispatch = 0;
	/**
	 * 已派工数
	 */
	public int Dispatched = 0;
	/**
	 * 1：已派工完成 0：未派工完成
	 */
	public int Status = 0;
	/**
	 * 顺序号
	 */
	public int OrderNum = 0;

	/**
	 * 工区ID
	 */
	public int AreaID = 0;
	/**
	 * 工区名称
	 */
	public String AreaName = "";

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

	public int getStationID() {
		return this.StationID;
	}

	public void setStationID(int stationID) {
		this.StationID = stationID;
	}

	public String getStationName() {
		return this.StationName;
	}

	public void setStationName(String stationName) {
		this.StationName = stationName;
	}

	public int getToDispatch() {
		return this.ToDispatch;
	}

	public void setToDispatch(int toDispatch) {
		this.ToDispatch = toDispatch;
	}

	public int getDispatched() {
		return this.Dispatched;
	}

	public void setDispatched(int dispatched) {
		this.Dispatched = dispatched;
	}

	public String getPartNo() {
		return PartNo;
	}

	public void setPartNo(String partNo) {
		PartNo = partNo;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public int getOrderNum() {
		return OrderNum;
	}

	public void setOrderNum(int orderNum) {
		OrderNum = orderNum;
	}

	public int getAreaID() {
		return AreaID;
	}

	public String getAreaName() {
		return AreaName;
	}

	public void setAreaID(int areaID) {
		AreaID = areaID;
	}

	public void setAreaName(String areaName) {
		AreaName = areaName;
	}
}
