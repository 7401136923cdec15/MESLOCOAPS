package com.mes.loco.aps.server.service.po.aps;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mes.loco.aps.server.service.po.bpm.BPMTaskBase;

/**
 * 台车BOM审批流
 * 
 * @author YouWang·Peng
 * @CreateTime 2021-11-12 10:14:51
 */
public class APSBomBPM extends BPMTaskBase implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 操作类型 1新增 2修改 3删除
	 */
	public int Type = 0;
	/**
	 * 订单ID
	 */
	public int OrderID = 0;
	/**
	 * 修程ID
	 */
	public int LineID = 0;
	/**
	 * 车型ID
	 */
	public int ProductID = 0;
	/**
	 * 局段ID
	 */
	public int CustomerID = 0;
	/**
	 * 车号
	 */
	public String PartNo = "";

	// 辅助属性
	/**
	 * 修程名称
	 */
	public String LineName = "";
	/**
	 * 车型名称
	 */
	public String ProductNo = "";
	/**
	 * 局段名称
	 */
	public String CustomerName = "";
	public String CustomerCode = "";
	public String WBSNo = "";

	/**
	 * 子项列表
	 */
	public List<APSBomBPMItem> APSBomBPMItemList = new ArrayList<APSBomBPMItem>();

	/**
	 * 描述信息
	 */
	public String DescribInfo = "";

	public APSBomBPM() {
	}

	public APSBomBPM(int type, int orderID, int lineID, int productID, int customerID, String partNo, String lineName,
			String productNo, String customerName) {
		super();
		Type = type;
		OrderID = orderID;
		LineID = lineID;
		ProductID = productID;
		CustomerID = customerID;
		PartNo = partNo;
		LineName = lineName;
		ProductNo = productNo;
		CustomerName = customerName;
	}

	public int getType() {
		return Type;
	}

	public void setType(int type) {
		Type = type;
	}

	public int getOrderID() {
		return OrderID;
	}

	public void setOrderID(int orderID) {
		OrderID = orderID;
	}

	public int getLineID() {
		return LineID;
	}

	public void setLineID(int lineID) {
		LineID = lineID;
	}

	public int getProductID() {
		return ProductID;
	}

	public void setProductID(int productID) {
		ProductID = productID;
	}

	public int getCustomerID() {
		return CustomerID;
	}

	public void setCustomerID(int customerID) {
		CustomerID = customerID;
	}

	public String getPartNo() {
		return PartNo;
	}

	public void setPartNo(String partNo) {
		PartNo = partNo;
	}

	public String getLineName() {
		return LineName;
	}

	public void setLineName(String lineName) {
		LineName = lineName;
	}

	public String getProductNo() {
		return ProductNo;
	}

	public void setProductNo(String productNo) {
		ProductNo = productNo;
	}

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}

	public List<APSBomBPMItem> getAPSBomBPMItemList() {
		return APSBomBPMItemList;
	}

	public void setAPSBomBPMItemList(List<APSBomBPMItem> aPSBomBPMItemList) {
		APSBomBPMItemList = aPSBomBPMItemList;
	}

	public String getCustomerCode() {
		return CustomerCode;
	}

	public void setCustomerCode(String customerCode) {
		CustomerCode = customerCode;
	}

	public String getWBSNo() {
		return WBSNo;
	}

	public void setWBSNo(String wBSNo) {
		WBSNo = wBSNo;
	}

	public String getDescribInfo() {
		return DescribInfo;
	}

	public void setDescribInfo(String describInfo) {
		DescribInfo = describInfo;
	}
}
