package com.mes.loco.aps.server.service.po.wms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 产线领料需求
 * 
 * @author YouWang·Peng
 * @CreateTime 2021-6-10 10:17:32
 */
public class WMSPickDemand implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	public int ID = 0;
	/**
	 * 货主/工厂 默认1900
	 */
	public String Factory = "1900";
	/**
	 * 发运订单类型：产线领料订单100，换料领料订单101
	 */
	public String OrderType = "";
	/**
	 * 发运订单类型：产线领料订单100，换料领料订单101
	 */
	public String OrderTypeText = "";
	/**
	 * 工位物料需求单号
	 */
	public String DemandNo = "";
	/**
	 * 预期到货时间1
	 */
	public Calendar ExpectTime1 = Calendar.getInstance();
	/**
	 * 预期到货日期
	 */
	public String ExpectTime1Text = "";
	/**
	 * 预期到货时间2(最迟到货时间)
	 */
	public Calendar ExpectTime2 = Calendar.getInstance();
	/**
	 * 预期到货时间2(最迟到货时间)
	 */
	public String ExpectTime2Text = "";
	/**
	 * 工位班组长工号
	 */
	public String MonitorNo = "";
	/**
	 * 工位班组长名称
	 */
	public String Monitor = "";
	/**
	 * 车型ID
	 */
	public int ProductID = 0;
	/**
	 * 车型
	 */
	public String ProductNo = "";
	/**
	 * 修程ID
	 */
	public int LineID = 0;
	/**
	 * 修程
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
	 * 局段
	 */
	public String CustomerCode = "";
	/**
	 * 订单ID
	 */
	public int OrderID = 0;
	/**
	 * 车号
	 */
	public String PartNo = "";
	/**
	 * 工位ID
	 */
	public int PartID = 0;
	/**
	 * 工位名称
	 */
	public String PartName = "";
	/**
	 * 工位编码
	 */
	public String PartCode = "";
	/**
	 * 分拣员ID
	 */
	public int SorterID = 0;
	/**
	 * 分拣员名称
	 */
	public String Sorter = "";
	/**
	 * 分拣员工号
	 */
	public String SorterNo = "";
	/**
	 * 分拣时刻
	 */
	public Calendar SorterTime = Calendar.getInstance();
	/**
	 * 分拣时刻文本
	 */
	public String SorterTimeText = "";
	/**
	 * 配送员ID
	 */
	public int DeliveryID = 0;
	/**
	 * 配送员名称
	 */
	public String DeliveryMan = "";
	/**
	 * 配送员工号
	 */
	public String DeliveryNo = "";
	/**
	 * 配送时刻
	 */
	public Calendar DeliveryTime = Calendar.getInstance();
	/**
	 * 配送时刻
	 */
	public String DeliveryTimeText = "";
	/**
	 * 接收人ID
	 */
	public int ReceiveID = 0;
	/**
	 * 接收人名称
	 */
	public String ReceiveMan = "";
	/**
	 * 接收人工号
	 */
	public String ReceiveNo = "";
	/**
	 * 接收时刻
	 */
	public Calendar ReceiveTime = Calendar.getInstance();
	/**
	 * 接收时刻
	 */
	public String ReceiveTimeText = "";
	/**
	 * 领料需求单状态
	 */
	public int Status = 0;
	/**
	 * 状态
	 */
	public String StatusText = "";
	/**
	 * 推送状态
	 */
	public int SendStatus = 0;
	/**
	 * 推送状态
	 */
	public String SendStatusText = "";
	/**
	 * 推送描述
	 */
	public String SendDesc = "";
	/**
	 * 创建人
	 */
	public int CreateID = 0;
	/**
	 * 创建人名称
	 */
	public String Creator = "";
	/**
	 * 创建时间
	 */
	public Calendar CreateTime = Calendar.getInstance();
	/**
	 * 创建时刻
	 */
	public String CreateTimeText = "";
	/**
	 * 领料需求
	 */
	public List<WMSPickDemandItem> ItemList = new ArrayList<WMSPickDemandItem>();
	/**
	 * 项目号
	 */
	public String WBSNo = "";

	public WMSPickDemand() {
		super();
	}

	public WMSPickDemand(int iD, String factory, String orderType, String demandNo, Calendar expectTime1,
			Calendar expectTime2, String monitorNo, String monitor, int productID, String productNo, int lineID,
			String lineName, int customerID, String customerName, String customerCode, int orderID, String partNo,
			int partID, String partName, String partCode, String deliveryMan, String deliveryNo, Calendar deliveryTime,
			String receiveMan, String receiveNo, Calendar receiveTime, int status, int createID, String creator,
			Calendar createTime, String wBSNo) {
		super();
		ID = iD;
		Factory = factory;
		OrderType = orderType;
		DemandNo = demandNo;
		ExpectTime1 = expectTime1;
		ExpectTime2 = expectTime2;
		MonitorNo = monitorNo;
		Monitor = monitor;
		ProductID = productID;
		ProductNo = productNo;
		LineID = lineID;
		LineName = lineName;
		CustomerID = customerID;
		CustomerName = customerName;
		CustomerCode = customerCode;
		OrderID = orderID;
		PartNo = partNo;
		PartID = partID;
		PartName = partName;
		PartCode = partCode;
		DeliveryMan = deliveryMan;
		DeliveryNo = deliveryNo;
		DeliveryTime = deliveryTime;
		ReceiveMan = receiveMan;
		ReceiveNo = receiveNo;
		ReceiveTime = receiveTime;
		Status = status;
		CreateID = createID;
		Creator = creator;
		CreateTime = createTime;
		WBSNo = wBSNo;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getFactory() {
		return Factory;
	}

	public void setFactory(String factory) {
		Factory = factory;
	}

	public String getOrderType() {
		return OrderType;
	}

	public void setOrderType(String orderType) {
		OrderType = orderType;
	}

	public String getDemandNo() {
		return DemandNo;
	}

	public void setDemandNo(String demandNo) {
		DemandNo = demandNo;
	}

	public Calendar getExpectTime1() {
		return ExpectTime1;
	}

	public void setExpectTime1(Calendar expectTime1) {
		ExpectTime1 = expectTime1;
	}

	public Calendar getExpectTime2() {
		return ExpectTime2;
	}

	public void setExpectTime2(Calendar expectTime2) {
		ExpectTime2 = expectTime2;
	}

	public String getMonitorNo() {
		return MonitorNo;
	}

	public void setMonitorNo(String monitorNo) {
		MonitorNo = monitorNo;
	}

	public String getMonitor() {
		return Monitor;
	}

	public void setMonitor(String monitor) {
		Monitor = monitor;
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

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}

	public String getCustomerCode() {
		return CustomerCode;
	}

	public void setCustomerCode(String customerCode) {
		CustomerCode = customerCode;
	}

	public String getPartNo() {
		return PartNo;
	}

	public void setPartNo(String partNo) {
		PartNo = partNo;
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

	public String getPartCode() {
		return PartCode;
	}

	public void setPartCode(String partCode) {
		PartCode = partCode;
	}

	public String getDeliveryMan() {
		return DeliveryMan;
	}

	public void setDeliveryMan(String deliveryMan) {
		DeliveryMan = deliveryMan;
	}

	public String getDeliveryNo() {
		return DeliveryNo;
	}

	public void setDeliveryNo(String deliveryNo) {
		DeliveryNo = deliveryNo;
	}

	public Calendar getDeliveryTime() {
		return DeliveryTime;
	}

	public void setDeliveryTime(Calendar deliveryTime) {
		DeliveryTime = deliveryTime;
	}

	public String getReceiveMan() {
		return ReceiveMan;
	}

	public void setReceiveMan(String receiveMan) {
		ReceiveMan = receiveMan;
	}

	public String getReceiveNo() {
		return ReceiveNo;
	}

	public void setReceiveNo(String receiveNo) {
		ReceiveNo = receiveNo;
	}

	public Calendar getReceiveTime() {
		return ReceiveTime;
	}

	public void setReceiveTime(Calendar receiveTime) {
		ReceiveTime = receiveTime;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public int getCreateID() {
		return CreateID;
	}

	public void setCreateID(int createID) {
		CreateID = createID;
	}

	public String getCreator() {
		return Creator;
	}

	public void setCreator(String creator) {
		Creator = creator;
	}

	public Calendar getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(Calendar createTime) {
		CreateTime = createTime;
	}

	public List<WMSPickDemandItem> getItemList() {
		return ItemList;
	}

	public void setItemList(List<WMSPickDemandItem> itemList) {
		ItemList = itemList;
	}

	public int getOrderID() {
		return OrderID;
	}

	public void setOrderID(int orderID) {
		OrderID = orderID;
	}

	public String getOrderTypeText() {
		return OrderTypeText;
	}

	public void setOrderTypeText(String orderTypeText) {
		OrderTypeText = orderTypeText;
	}

	public String getExpectTime1Text() {
		return ExpectTime1Text;
	}

	public void setExpectTime1Text(String expectTime1Text) {
		ExpectTime1Text = expectTime1Text;
	}

	public String getExpectTime2Text() {
		return ExpectTime2Text;
	}

	public void setExpectTime2Text(String expectTime2Text) {
		ExpectTime2Text = expectTime2Text;
	}

	public String getDeliveryTimeText() {
		return DeliveryTimeText;
	}

	public void setDeliveryTimeText(String deliveryTimeText) {
		DeliveryTimeText = deliveryTimeText;
	}

	public String getReceiveTimeText() {
		return ReceiveTimeText;
	}

	public void setReceiveTimeText(String receiveTimeText) {
		ReceiveTimeText = receiveTimeText;
	}

	public String getStatusText() {
		return StatusText;
	}

	public void setStatusText(String statusText) {
		StatusText = statusText;
	}

	public String getCreateTimeText() {
		return CreateTimeText;
	}

	public void setCreateTimeText(String createTimeText) {
		CreateTimeText = createTimeText;
	}

	public int getSendStatus() {
		return SendStatus;
	}

	public void setSendStatus(int sendStatus) {
		SendStatus = sendStatus;
	}

	public String getSendStatusText() {
		return SendStatusText;
	}

	public void setSendStatusText(String sendStatusText) {
		SendStatusText = sendStatusText;
	}

	public String getSendDesc() {
		return SendDesc;
	}

	public void setSendDesc(String sendDesc) {
		SendDesc = sendDesc;
	}

	public String getWBSNo() {
		return WBSNo;
	}

	public void setWBSNo(String wBSNo) {
		WBSNo = wBSNo;
	}

	public int getSorterID() {
		return SorterID;
	}

	public void setSorterID(int sorterID) {
		SorterID = sorterID;
	}

	public String getSorter() {
		return Sorter;
	}

	public void setSorter(String sorter) {
		Sorter = sorter;
	}

	public String getSorterNo() {
		return SorterNo;
	}

	public void setSorterNo(String sorterNo) {
		SorterNo = sorterNo;
	}

	public Calendar getSorterTime() {
		return SorterTime;
	}

	public void setSorterTime(Calendar sorterTime) {
		SorterTime = sorterTime;
	}

	public int getDeliveryID() {
		return DeliveryID;
	}

	public void setDeliveryID(int deliveryID) {
		DeliveryID = deliveryID;
	}

	public int getReceiveID() {
		return ReceiveID;
	}

	public void setReceiveID(int receiveID) {
		ReceiveID = receiveID;
	}

	public String getSorterTimeText() {
		return SorterTimeText;
	}

	public void setSorterTimeText(String sorterTimeText) {
		SorterTimeText = sorterTimeText;
	}
}
