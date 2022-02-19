package com.mes.loco.aps.server.service.po.andon;

import java.io.Serializable;
import java.util.Calendar;

/**
 * 台车订单实时列表
 * 
 * @author YouWang·Peng
 * @CreateTime 2020-5-22 23:04:50
 * @LastEditTime 2020-1-19 09:43:18
 *
 */
public class OrderTimeInfo implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 订单ID
	 */
	public int OrderID = 0;
	/**
	 * 台车编号
	 */
	public String PartNo = "";
	/**
	 * 配属局段
	 */
	public String CustomerName = "";
	/**
	 * 修程
	 */
	public String LineName = "";
	/**
	 * 进厂时刻(排序用)
	 */
	public Calendar InPlantTime = Calendar.getInstance();
	/**
	 * 进厂日期
	 */
	public String RealReceiveDate = "";
	/**
	 * 停时
	 */
	public int StopTime = 0;
	/**
	 * 库位
	 */
	public String StockName = "";
	/**
	 * 台位
	 */
	public String PlaceName = "";
	/**
	 * 工位
	 */
	public String StationName = "";
	/**
	 * 完成数
	 */
	public int FinishNum = 0;
	/**
	 * 剩余数
	 */
	public int SurplusNum = 0;
	/**
	 * 完成进度
	 */
	public int Progress = 0;
	/**
	 * 返修数
	 */
	public int ReworkNum = 0;
	/**
	 * 不合格评审数
	 */
	public int NcrNum = 0;
	/**
	 * 异常数
	 */
	public int ExcNum = 0;
	/**
	 * 状态
	 */
	public String Status = "";
	/**
	 * 工位总数
	 */
	public int STTotal = 0;
	/**
	 * 工位完成数
	 */
	public int STFinish = 0;

	/**
	 * 提前/落后情况
	 */
	public int PlanSituation = 0;
	/**
	 * 工位计划开工时间
	 */
	public Calendar PlanStartTime = Calendar.getInstance();
	/**
	 * 工位计划完工时间
	 */
	public Calendar PlanEndTime = Calendar.getInstance();

	/**
	 * 1蓝色 2红色 3正常颜色 4绿色
	 */
	public int StationColor = 0;

	public OrderTimeInfo() {
	}

	public String getPartNo() {
		return PartNo;
	}

	public void setPartNo(String partNo) {
		PartNo = partNo;
	}

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}

	public String getInPlantDate() {
		return RealReceiveDate;
	}

	public void setInPlantDate(String inPlantDate) {
		RealReceiveDate = inPlantDate;
	}

	public int getStopHour() {
		return StopTime;
	}

	public void setStopHour(int stopHour) {
		StopTime = stopHour;
	}

	public Calendar getInPlantTime() {
		return InPlantTime;
	}

	public void setInPlantTime(Calendar inPlantTime) {
		InPlantTime = inPlantTime;
	}

	public String getStockName() {
		return StockName;
	}

	public String getLineName() {
		return LineName;
	}

	public void setLineName(String lineName) {
		LineName = lineName;
	}

	public String getRealReceiveDate() {
		return RealReceiveDate;
	}

	public void setRealReceiveDate(String realReceiveDate) {
		RealReceiveDate = realReceiveDate;
	}

	public int getStopTime() {
		return StopTime;
	}

	public void setStopTime(int stopTime) {
		StopTime = stopTime;
	}

	public int getFinishNum() {
		return FinishNum;
	}

	public void setFinishNum(int finishNum) {
		FinishNum = finishNum;
	}

	public int getSurplusNum() {
		return SurplusNum;
	}

	public void setSurplusNum(int surplusNum) {
		SurplusNum = surplusNum;
	}

	public int getReworkNum() {
		return ReworkNum;
	}

	public void setReworkNum(int reworkNum) {
		ReworkNum = reworkNum;
	}

	public int getNcrNum() {
		return NcrNum;
	}

	public void setNcrNum(int ncrNum) {
		NcrNum = ncrNum;
	}

	public int getExcNum() {
		return ExcNum;
	}

	public void setExcNum(int excNum) {
		ExcNum = excNum;
	}

	public void setStockName(String stockName) {
		StockName = stockName;
	}

	public String getPlaceName() {
		return PlaceName;
	}

	public void setPlaceName(String placeName) {
		PlaceName = placeName;
	}

	public String getStationName() {
		return StationName;
	}

	public void setStationName(String stationName) {
		StationName = stationName;
	}

	public int getFQTYFinish() {
		return FinishNum;
	}

	public void setFQTYFinish(int fQTYFinish) {
		FinishNum = fQTYFinish;
	}

	public int getFQTYLess() {
		return SurplusNum;
	}

	public void setFQTYLess(int fQTYLess) {
		SurplusNum = fQTYLess;
	}

	public int getProgress() {
		return Progress;
	}

	public void setProgress(int progress) {
		Progress = progress;
	}

	public int getFQTYRepair() {
		return ReworkNum;
	}

	public void setFQTYRepair(int fQTYRepair) {
		ReworkNum = fQTYRepair;
	}

	public int getFQTYNcr() {
		return NcrNum;
	}

	public void setFQTYNcr(int fQTYNcr) {
		NcrNum = fQTYNcr;
	}

	public int getFQTYException() {
		return ExcNum;
	}

	public void setFQTYException(int fQTYException) {
		ExcNum = fQTYException;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public int getOrderID() {
		return OrderID;
	}

	public void setOrderID(int orderID) {
		OrderID = orderID;
	}

	public int getSTTotal() {
		return STTotal;
	}

	public void setSTTotal(int sTTotal) {
		STTotal = sTTotal;
	}

	public int getSTFinish() {
		return STFinish;
	}

	public void setSTFinish(int sTFinish) {
		STFinish = sTFinish;
	}

	public int getPlanSituation() {
		return PlanSituation;
	}

	public void setPlanSituation(int planSituation) {
		PlanSituation = planSituation;
	}

	public Calendar getPlanStartTime() {
		return PlanStartTime;
	}

	public Calendar getPlanEndTime() {
		return PlanEndTime;
	}

	public void setPlanStartTime(Calendar planStartTime) {
		PlanStartTime = planStartTime;
	}

	public void setPlanEndTime(Calendar planEndTime) {
		PlanEndTime = planEndTime;
	}

	public int getStationColor() {
		return StationColor;
	}

	public void setStationColor(int stationColor) {
		StationColor = stationColor;
	}
}
