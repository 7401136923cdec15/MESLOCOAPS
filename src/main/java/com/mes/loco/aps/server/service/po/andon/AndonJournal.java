package com.mes.loco.aps.server.service.po.andon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 报表日志
 */
public class AndonJournal implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	public int ID = 0;
	/**
	 * 创建人
	 */
	public int CreateID = 0;
	/**
	 * 创建人
	 */
	public String CreateName = "";
	/**
	 * 创建时间
	 */
	public Calendar CreateTime = Calendar.getInstance();
	/**
	 * C5修年度累计竣工台数
	 */
	public int FQTYYearComplete_C5 = 0;
	/**
	 * C5修本月竣工台数
	 */
	public int FQTYMonthComplete_C5 = 0;
	/**
	 * C5修在厂机车台数
	 */
	public int FQTYPlant_C5 = 0;
	/**
	 * C5修在修机车台数
	 */
	public int FQTYRepair_C5 = 0;
	/**
	 * C6修年度累计竣工台数
	 */
	public int FQTYYearComplete_C6 = 0;
	/**
	 * C6修本月竣工台数
	 */
	public int FQTYMonthComplete_C6 = 0;
	/**
	 * C6修在厂机车台数
	 */
	public int FQTYPlant_C6 = 0;
	/**
	 * C6修在修机车台数
	 */
	public int FQTYRepair_C6 = 0;
	/**
	 * 重大影响
	 */
	public String SignificantImpact = "";
	/**
	 * C5修详情
	 */
	public List<AndonJournalItem> ItemList_C5 = new ArrayList<AndonJournalItem>();
	/**
	 * C6修详情
	 */
	public List<AndonJournalItem> ItemList_C6 = new ArrayList<AndonJournalItem>();

	public AndonJournal() {
		super();
	}

	public AndonJournal(int iD, int createID, String createName, Calendar createTime, int fQTYYearComplete_C5,
			int fQTYMonthComplete_C5, int fQTYPlant_C5, int fQTYRepair_C5, int fQTYYearComplete_C6,
			int fQTYMonthComplete_C6, int fQTYPlant_C6, int fQTYRepair_C6, String significantImpact,
			List<AndonJournalItem> itemList_C5, List<AndonJournalItem> itemList_C6) {
		super();
		ID = iD;
		CreateID = createID;
		CreateName = createName;
		CreateTime = createTime;
		FQTYYearComplete_C5 = fQTYYearComplete_C5;
		FQTYMonthComplete_C5 = fQTYMonthComplete_C5;
		FQTYPlant_C5 = fQTYPlant_C5;
		FQTYRepair_C5 = fQTYRepair_C5;
		FQTYYearComplete_C6 = fQTYYearComplete_C6;
		FQTYMonthComplete_C6 = fQTYMonthComplete_C6;
		FQTYPlant_C6 = fQTYPlant_C6;
		FQTYRepair_C6 = fQTYRepair_C6;
		SignificantImpact = significantImpact;
		ItemList_C5 = itemList_C5;
		ItemList_C6 = itemList_C6;
	}

	public int getID() {
		return ID;
	}

	public int getCreateID() {
		return CreateID;
	}

	public String getCreateName() {
		return CreateName;
	}

	public Calendar getCreateTime() {
		return CreateTime;
	}

	public int getFQTYYearComplete_C5() {
		return FQTYYearComplete_C5;
	}

	public int getFQTYMonthComplete_C5() {
		return FQTYMonthComplete_C5;
	}

	public int getFQTYPlant_C5() {
		return FQTYPlant_C5;
	}

	public int getFQTYRepair_C5() {
		return FQTYRepair_C5;
	}

	public int getFQTYYearComplete_C6() {
		return FQTYYearComplete_C6;
	}

	public int getFQTYMonthComplete_C6() {
		return FQTYMonthComplete_C6;
	}

	public int getFQTYPlant_C6() {
		return FQTYPlant_C6;
	}

	public int getFQTYRepair_C6() {
		return FQTYRepair_C6;
	}

	public String getSignificantImpact() {
		return SignificantImpact;
	}

	public List<AndonJournalItem> getItemList_C5() {
		return ItemList_C5;
	}

	public List<AndonJournalItem> getItemList_C6() {
		return ItemList_C6;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public void setCreateID(int createID) {
		CreateID = createID;
	}

	public void setCreateName(String createName) {
		CreateName = createName;
	}

	public void setCreateTime(Calendar createTime) {
		CreateTime = createTime;
	}

	public void setFQTYYearComplete_C5(int fQTYYearComplete_C5) {
		FQTYYearComplete_C5 = fQTYYearComplete_C5;
	}

	public void setFQTYMonthComplete_C5(int fQTYMonthComplete_C5) {
		FQTYMonthComplete_C5 = fQTYMonthComplete_C5;
	}

	public void setFQTYPlant_C5(int fQTYPlant_C5) {
		FQTYPlant_C5 = fQTYPlant_C5;
	}

	public void setFQTYRepair_C5(int fQTYRepair_C5) {
		FQTYRepair_C5 = fQTYRepair_C5;
	}

	public void setFQTYYearComplete_C6(int fQTYYearComplete_C6) {
		FQTYYearComplete_C6 = fQTYYearComplete_C6;
	}

	public void setFQTYMonthComplete_C6(int fQTYMonthComplete_C6) {
		FQTYMonthComplete_C6 = fQTYMonthComplete_C6;
	}

	public void setFQTYPlant_C6(int fQTYPlant_C6) {
		FQTYPlant_C6 = fQTYPlant_C6;
	}

	public void setFQTYRepair_C6(int fQTYRepair_C6) {
		FQTYRepair_C6 = fQTYRepair_C6;
	}

	public void setSignificantImpact(String significantImpact) {
		SignificantImpact = significantImpact;
	}

	public void setItemList_C5(List<AndonJournalItem> itemList_C5) {
		ItemList_C5 = itemList_C5;
	}

	public void setItemList_C6(List<AndonJournalItem> itemList_C6) {
		ItemList_C6 = itemList_C6;
	}
}
