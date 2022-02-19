package com.mes.loco.aps.server.service.po.andon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 生产日报车辆部分
 */
public class AndonDayReportCar implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 车型
	 */
	public String ProductNo = "";
	/**
	 * 车号
	 */
	public String PartNo = "";
	/**
	 * 修程
	 */
	public String LineName = "";
	/**
	 * 局段
	 */
	public String CustomerName = "";
	/**
	 * 计划日期
	 */
	public List<AndonDayReportDate> PlanList = new ArrayList<AndonDayReportDate>();
	/**
	 * 实际日期
	 */
	public List<AndonDayReportDate> RealList = new ArrayList<AndonDayReportDate>();
	/**
	 * 检修停时
	 */
	public int StopTime = 0;
	/**
	 * 备注
	 */
	public String Remark = "";

	public AndonDayReportCar() {
		super();
	}

	public AndonDayReportCar(String productNo, String partNo, String lineName, String customerName,
			List<AndonDayReportDate> planList, List<AndonDayReportDate> realList, int stopTime, String remark) {
		super();
		ProductNo = productNo;
		PartNo = partNo;
		LineName = lineName;
		CustomerName = customerName;
		PlanList = planList;
		RealList = realList;
		StopTime = stopTime;
		Remark = remark;
	}

	public String getProductNo() {
		return ProductNo;
	}

	public String getPartNo() {
		return PartNo;
	}

	public String getLineName() {
		return LineName;
	}

	public String getCustomerName() {
		return CustomerName;
	}

	public List<AndonDayReportDate> getPlanList() {
		return PlanList;
	}

	public List<AndonDayReportDate> getRealList() {
		return RealList;
	}

	public int getStopTime() {
		return StopTime;
	}

	public String getRemark() {
		return Remark;
	}

	public void setProductNo(String productNo) {
		ProductNo = productNo;
	}

	public void setPartNo(String partNo) {
		PartNo = partNo;
	}

	public void setLineName(String lineName) {
		LineName = lineName;
	}

	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}

	public void setPlanList(List<AndonDayReportDate> planList) {
		PlanList = planList;
	}

	public void setRealList(List<AndonDayReportDate> realList) {
		RealList = realList;
	}

	public void setStopTime(int stopTime) {
		StopTime = stopTime;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

}
