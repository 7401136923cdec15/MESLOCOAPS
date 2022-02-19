package com.mes.loco.aps.server.service.po.sfc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mes.loco.aps.server.service.po.bpm.BPMTaskBase;
import com.mes.loco.aps.server.service.po.rro.RROItemTask;

/**
 * 试运申请
 */
public class SFCAttemptRun extends BPMTaskBase implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	public int OrderID = 0;

	public String WBSNo = "";

	public int ProductID = 0;

	public String ProductNo = "";

	public String PartNo = "";

	public int CheckerID = 0;

	public String CheckerName = "";

	public int PartID; // 台车当前工位

	public String PartName = ""; // Name+(No)

	/**
	 * 未完成的返修项
	 */
	public List<SFCRepairItem> ItemList = new ArrayList<SFCRepairItem>();

	/**
	 * 未完成的返修项
	 */
	public List<RROItemTask> ItemTaskList = new ArrayList<RROItemTask>();

	/**
	 * 返修项是否全部完成
	 */
	public boolean IsFinish = false;

	public int TagTypes = 0;

	public int LineID = 0;
	public String LineName = "";
	public int CustomerID = 0;
	public String Customer = "";
	public int ItemDone = 0;
	public int LoginID = 0;

	public int getOrderID() {
		return OrderID;
	}

	public void setOrderID(int orderID) {
		OrderID = orderID;
	}

	public int getTagTypes() {
		return TagTypes;
	}

	public void setTagTypes(int tagTypes) {
		TagTypes = tagTypes;
	}

	public String getWBSNo() {
		return WBSNo;
	}

	public void setWBSNo(String wBSNo) {
		WBSNo = wBSNo;
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

	public String getPartNo() {
		return PartNo;
	}

	public void setPartNo(String partNo) {
		PartNo = partNo;
	}

	public int getCheckerID() {
		return CheckerID;
	}

	public void setCheckerID(int checkerID) {
		CheckerID = checkerID;
	}

	public String getCheckerName() {
		return CheckerName;
	}

	public void setCheckerName(String checkerName) {
		CheckerName = checkerName;
	}

	public int getPartID() {
		return PartID;
	}

	public void setPartID(int partID) {
		PartID = partID;
	}

	public String getPartName() {
		return PartName;
	}

	public void setPartName(String partName) {
		PartName = partName;
	}

	public List<SFCRepairItem> getItemList() {
		return ItemList;
	}

	public void setItemList(List<SFCRepairItem> itemList) {
		ItemList = itemList;
	}

	public boolean isIsFinish() {
		return IsFinish;
	}

	public void setIsFinish(boolean isFinish) {
		IsFinish = isFinish;
	}

	public int getLineID() {
		return LineID;
	}

	public void setLineID(int lineID) {
		LineID = lineID;
	}

	public String getLineName() {
		return LineName;
	}

	public void setLineName(String lineName) {
		LineName = lineName;
	}

	public int getCustomerID() {
		return CustomerID;
	}

	public void setCustomerID(int customerID) {
		CustomerID = customerID;
	}

	public String getCustomer() {
		return Customer;
	}

	public void setCustomer(String customer) {
		Customer = customer;
	}

	public List<RROItemTask> getItemTaskList() {
		return ItemTaskList;
	}

	public void setItemTaskList(List<RROItemTask> itemTaskList) {
		ItemTaskList = itemTaskList;
	}

	public int getItemDone() {
		return ItemDone;
	}

	public void setItemDone(int itemDone) {
		ItemDone = itemDone;
	}

	public int getLoginID() {
		return LoginID;
	}

	public void setLoginID(int loginID) {
		LoginID = loginID;
	}
}
