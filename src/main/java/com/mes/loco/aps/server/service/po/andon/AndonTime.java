package com.mes.loco.aps.server.service.po.andon;

import java.io.Serializable;
import java.util.Calendar;

/**
 * 订单到场完工信息
 */

public class AndonTime implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 订单ID
	 */
	public int OrderID = 0;
	/**
	 * 进厂时间
	 */
	public Calendar RealReceiveDate = Calendar.getInstance();
	/**
	 * 完工时间
	 */
	public Calendar FinishWorkTime = Calendar.getInstance();

	/**
	 * 停时
	 */
	public int StopTime = 0;

	public int getOrderID() {
		return OrderID;
	}

	public Calendar getRealReceiveDate() {
		return RealReceiveDate;
	}

	public Calendar getFinishWorkTime() {
		return FinishWorkTime;
	}

	public int getStopTime() {
		return StopTime;
	}

	public void setOrderID(int orderID) {
		OrderID = orderID;
	}

	public void setRealReceiveDate(Calendar realReceiveDate) {
		RealReceiveDate = realReceiveDate;
	}

	public void setFinishWorkTime(Calendar finishWorkTime) {
		FinishWorkTime = finishWorkTime;
	}

	public void setStopTime(int stopTime) {
		StopTime = stopTime;
	}
}
