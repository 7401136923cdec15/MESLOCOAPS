package com.mes.loco.aps.server.service.po.ipt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class IPTStandard implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	public long ID;

	public int CompanyID;
	/// <summary>
	/// 所属部门
	/// </summary>
	public int BusinessUnitID;
	/// <summary>
	/// 生产基地ID
	/// </summary>
	public int BaseID;
	/// <summary>
	/// 工厂ID
	/// </summary>
	public int FactoryID;
	public int WorkShopID;
	public int UserID;
	public int IPTMode;
	public int LineID;
	public int PartID;
	public int PartPointID;
	public int StationID;
	public int ProductID;
	public String ProductNo;
	public List<IPTItem> ItemList;
	public int IsCurrent;
	public Calendar TModify;
	public int IsEnd;
	public int IsUsed;
	public String Remark;
	/**
	 * 唯一版本号
	 */
	public String Version = "";

	/**
	 * 局段信息
	 */
	public int CustomID;
	public String CustomName = "";

	public String LineName = "";
	public String PartName = "";
	public String PartPointName = "";

	public IPTStandard() {
		ProductID = 0;
		ProductNo = "";
		Remark = "";
		Calendar wCalendar = Calendar.getInstance();
		wCalendar.set(2000, 1, 1);
		TModify = wCalendar;
		ItemList = new ArrayList<IPTItem>();
	}

	public long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}

	public int getCompanyID() {
		return CompanyID;
	}

	public void setCompanyID(int companyID) {
		CompanyID = companyID;
	}

	public int getBusinessUnitID() {
		return BusinessUnitID;
	}

	public void setBusinessUnitID(int businessUnitID) {
		BusinessUnitID = businessUnitID;
	}

	public int getBaseID() {
		return BaseID;
	}

	public void setBaseID(int baseID) {
		BaseID = baseID;
	}

	public int getFactoryID() {
		return FactoryID;
	}

	public void setFactoryID(int factoryID) {
		FactoryID = factoryID;
	}

	public int getWorkShopID() {
		return WorkShopID;
	}

	public void setWorkShopID(int workShopID) {
		WorkShopID = workShopID;
	}

	public int getUserID() {
		return UserID;
	}

	public void setUserID(int userID) {
		UserID = userID;
	}

	public int getIPTMode() {
		return IPTMode;
	}

	public void setIPTMode(int iPTMode) {
		IPTMode = iPTMode;
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

	public int getPartPointID() {
		return PartPointID;
	}

	public void setPartPointID(int partPointID) {
		PartPointID = partPointID;
	}

	public int getStationID() {
		return StationID;
	}

	public void setStationID(int stationID) {
		StationID = stationID;
	}

	public int getProductID() {
		return ProductID;
	}

	public void setProductID(int productID) {
		ProductID = productID;
	}

	public String getProductNo() {
		return ProductNo;
	}

	public void setProductNo(String productNo) {
		ProductNo = productNo;
	}

	public List<IPTItem> getItemList() {
		return ItemList;
	}

	public void setItemList(List<IPTItem> itemList) {
		ItemList = itemList;
	}

	public int getIsCurrent() {
		return IsCurrent;
	}

	public void setIsCurrent(int isCurrent) {
		IsCurrent = isCurrent;
	}

	public Calendar getTModify() {
		return TModify;
	}

	public void setTModify(Calendar tModify) {
		TModify = tModify;
	}

	public int getIsEnd() {
		return IsEnd;
	}

	public void setIsEnd(int isEnd) {
		IsEnd = isEnd;
	}

	public int getIsUsed() {
		return IsUsed;
	}

	public void setIsUsed(int isUsed) {
		IsUsed = isUsed;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	public int getCustomID() {
		return CustomID;
	}

	public void setCustomID(int customID) {
		CustomID = customID;
	}

	public String getCustomName() {
		return CustomName;
	}

	public void setCustomName(String customName) {
		CustomName = customName;
	}

	public String getVersion() {
		return Version;
	}

	public void setVersion(String version) {
		Version = version;
	}

	public String getLineName() {
		return LineName;
	}

	public void setLineName(String lineName) {
		LineName = lineName;
	}

	public String getPartName() {
		return PartName;
	}

	public void setPartName(String partName) {
		PartName = partName;
	}

	public String getPartPointName() {
		return PartPointName;
	}

	public void setPartPointName(String partPointName) {
		PartPointName = partPointName;
	}
}
