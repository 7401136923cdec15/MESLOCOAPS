package com.mes.loco.aps.server.service.po.andon;

import java.io.Serializable;

/**
 * 年度任务完成情况
 * 
 * @author YouWang·Peng
 * @CreateTime 2021-8-31 09:21:57
 */
public class AndonYearFinishSituation implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 序号
	 */
	public int ID = 0;
	/**
	 * 修程ID
	 */
	public int LineID = 0;
	/**
	 * 修程名称
	 */
	public String LineName = "";
	/**
	 * 局段ID
	 */
	public int CustomerID = 0;
	/**
	 * 局段名称
	 */
	public String CustomerName = "";
	/**
	 * 车型
	 */
	public String ProductNo = "";
	/**
	 * 合计数
	 */
	public int Total = 0;
	/**
	 * 1月完成数
	 */
	public int Month1 = 0;
	/**
	 * 2月完成数
	 */
	public int Month2 = 0;
	/**
	 * 3月完成数
	 */
	public int Month3 = 0;
	/**
	 * 4月完成数
	 */
	public int Month4 = 0;
	/**
	 * 5月完成数
	 */
	public int Month5 = 0;
	/**
	 * 6月完成数
	 */
	public int Month6 = 0;
	/**
	 * 7月完成数
	 */
	public int Month7 = 0;
	/**
	 * 8月完成数
	 */
	public int Month8 = 0;
	/**
	 * 9月完成数
	 */
	public int Month9 = 0;
	/**
	 * 10月完成数
	 */
	public int Month10 = 0;
	/**
	 * 11月完成数
	 */
	public int Month11 = 0;
	/**
	 * 12月完成数
	 */
	public int Month12 = 0;

	public AndonYearFinishSituation() {
		super();
	}

	public AndonYearFinishSituation(int iD, int lineID, String lineName, int customerID, String customerName, int total,
			int month1, int month2, int month3, int month4, int month5, int month6, int month7, int month8, int month9,
			int month10, int month11, int month12) {
		super();
		ID = iD;
		LineID = lineID;
		LineName = lineName;
		CustomerID = customerID;
		CustomerName = customerName;
		Total = total;
		Month1 = month1;
		Month2 = month2;
		Month3 = month3;
		Month4 = month4;
		Month5 = month5;
		Month6 = month6;
		Month7 = month7;
		Month8 = month8;
		Month9 = month9;
		Month10 = month10;
		Month11 = month11;
		Month12 = month12;
	}

	public int getID() {
		return ID;
	}

	public int getLineID() {
		return LineID;
	}

	public String getLineName() {
		return LineName;
	}

	public int getCustomerID() {
		return CustomerID;
	}

	public String getCustomerName() {
		return CustomerName;
	}

	public int getTotal() {
		return Total;
	}

	public int getMonth1() {
		return Month1;
	}

	public int getMonth2() {
		return Month2;
	}

	public int getMonth3() {
		return Month3;
	}

	public int getMonth4() {
		return Month4;
	}

	public int getMonth5() {
		return Month5;
	}

	public int getMonth6() {
		return Month6;
	}

	public int getMonth7() {
		return Month7;
	}

	public int getMonth8() {
		return Month8;
	}

	public int getMonth9() {
		return Month9;
	}

	public int getMonth10() {
		return Month10;
	}

	public int getMonth11() {
		return Month11;
	}

	public int getMonth12() {
		return Month12;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public void setLineID(int lineID) {
		LineID = lineID;
	}

	public void setLineName(String lineName) {
		LineName = lineName;
	}

	public void setCustomerID(int customerID) {
		CustomerID = customerID;
	}

	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}

	public void setTotal(int total) {
		Total = total;
	}

	public void setMonth1(int month1) {
		Month1 = month1;
	}

	public void setMonth2(int month2) {
		Month2 = month2;
	}

	public void setMonth3(int month3) {
		Month3 = month3;
	}

	public void setMonth4(int month4) {
		Month4 = month4;
	}

	public void setMonth5(int month5) {
		Month5 = month5;
	}

	public void setMonth6(int month6) {
		Month6 = month6;
	}

	public void setMonth7(int month7) {
		Month7 = month7;
	}

	public void setMonth8(int month8) {
		Month8 = month8;
	}

	public void setMonth9(int month9) {
		Month9 = month9;
	}

	public void setMonth10(int month10) {
		Month10 = month10;
	}

	public void setMonth11(int month11) {
		Month11 = month11;
	}

	public void setMonth12(int month12) {
		Month12 = month12;
	}

	@Override
	public String toString() {
		return "AndonYearFinishSituation [ID=" + ID + ", LineID=" + LineID + ", LineName=" + LineName + ", CustomerID="
				+ CustomerID + ", CustomerName=" + CustomerName + ", Total=" + Total + ", Month1=" + Month1
				+ ", Month2=" + Month2 + ", Month3=" + Month3 + ", Month4=" + Month4 + ", Month5=" + Month5
				+ ", Month6=" + Month6 + ", Month7=" + Month7 + ", Month8=" + Month8 + ", Month9=" + Month9
				+ ", Month10=" + Month10 + ", Month11=" + Month11 + ", Month12=" + Month12 + "]";
	}
}
