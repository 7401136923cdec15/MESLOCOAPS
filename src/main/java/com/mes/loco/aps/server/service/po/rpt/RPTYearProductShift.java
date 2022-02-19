package com.mes.loco.aps.server.service.po.rpt;

import java.util.ArrayList;
import java.util.List;

/**
 * 年修程数据统计
 * 
 * @author PengYouWang
 * @CreateTime 2020-6-17 17:05:34
 * @LastEditTime 2020-6-17 17:08:17
 *
 */
public class RPTYearProductShift {
	/**
	 * 年份
	 */
	public int Year = 0;
	/**
	 * 修程统计数据列表
	 */
	public List<RPTProductShift> RPTCustomerShiftList = new ArrayList<>();

	public RPTYearProductShift() {
	}

	public int getYear() {
		return Year;
	}

	public void setYear(int year) {
		Year = year;
	}

	public List<RPTProductShift> getRPTCustomerShiftList() {
		return RPTCustomerShiftList;
	}

	public void setRPTCustomerShiftList(List<RPTProductShift> rPTCustomerShiftList) {
		RPTCustomerShiftList = rPTCustomerShiftList;
	}
}
