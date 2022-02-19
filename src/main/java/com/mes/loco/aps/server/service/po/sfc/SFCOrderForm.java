package com.mes.loco.aps.server.service.po.sfc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 竣工确认单或出厂申请单
 * 
 * @author ShrisJava
 *
 */
public class SFCOrderForm implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	public int ID = 0;
	/**
	 * 订单ID
	 */
	public int OrderID = 0;
	/**
	 * 订单号
	 */
	public String OrderNo = "";
	/**
	 * 车号
	 */
	public String PartNo = "";
	/**
	 * 创建人ID
	 */
	public int CreateID = 0;
	/**
	 * 创建人名称
	 */
	public String Creator = "";
	/**
	 * 创建时刻
	 */
	public Calendar CreateTime = Calendar.getInstance();
	/**
	 * 确认人ID
	 */
	public int ConfirmID = 0;
	/**
	 * 确认人名称
	 */
	public String ConfirmName = "";
	/**
	 * 确认时刻
	 */
	public Calendar ConfirmTime = Calendar.getInstance();
	/**
	 * 状态 1申请中 2已确认
	 */
	public int Status = 0;
	/**
	 * 1 竣工确认单 2出厂申请单 3进厂确认单
	 */
	public int Type = 0;
	/**
	 * 图片路径
	 */
	public List<String> ImagePathList = new ArrayList<String>();
	/**
	 * 备注
	 */
	public String Remark = "";

	// 辅助属性
	public String LineName = "";
	public String ProductNo = "";
	public String CustomerName = "";

	public SFCOrderForm() {
		Calendar wBaseTime = Calendar.getInstance();
		wBaseTime.set(2000, 0, 1);
		CreateTime = wBaseTime;
		ConfirmTime = wBaseTime;
	}

	public SFCOrderForm(int iD, int orderID, String orderNo, String partNo, int createID, String creator,
			Calendar createTime, int confirmID, String confirmName, Calendar confirmTime, int status, int type,
			String lineName, String productNo, String customerName) {
		super();
		ID = iD;
		OrderID = orderID;
		OrderNo = orderNo;
		PartNo = partNo;
		CreateID = createID;
		Creator = creator;
		CreateTime = createTime;
		ConfirmID = confirmID;
		ConfirmName = confirmName;
		ConfirmTime = confirmTime;
		Status = status;
		Type = type;
		LineName = lineName;
		ProductNo = productNo;
		CustomerName = customerName;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getLineName() {
		return LineName;
	}

	public void setLineName(String lineName) {
		LineName = lineName;
	}

	public List<String> getImagePathList() {
		return ImagePathList;
	}

	public void setImagePathList(List<String> imagePathList) {
		ImagePathList = imagePathList;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}

	public String getProductNo() {
		return ProductNo;
	}

	public void setProductNo(String productNo) {
		ProductNo = productNo;
	}

	public int getOrderID() {
		return OrderID;
	}

	public void setOrderID(int orderID) {
		OrderID = orderID;
	}

	public String getOrderNo() {
		return OrderNo;
	}

	public void setOrderNo(String orderNo) {
		OrderNo = orderNo;
	}

	public String getPartNo() {
		return PartNo;
	}

	public void setPartNo(String partNo) {
		PartNo = partNo;
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

	public int getConfirmID() {
		return ConfirmID;
	}

	public void setConfirmID(int confirmID) {
		ConfirmID = confirmID;
	}

	public String getConfirmName() {
		return ConfirmName;
	}

	public void setConfirmName(String confirmName) {
		ConfirmName = confirmName;
	}

	public Calendar getConfirmTime() {
		return ConfirmTime;
	}

	public void setConfirmTime(Calendar confirmTime) {
		ConfirmTime = confirmTime;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public int getType() {
		return Type;
	}

	public void setType(int type) {
		Type = type;
	}
}
