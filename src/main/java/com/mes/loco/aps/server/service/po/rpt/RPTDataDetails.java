package com.mes.loco.aps.server.service.po.rpt;

/**
 * 统计数据详情
 * 
 * @author PengYouWang
 * @CreateTime 2020-6-17 17:05:34
 * @LastEditTime 2020-6-17 17:08:17
 *
 */
public class RPTDataDetails {

	/**
	 * 局段
	 */
	public int CustomerID = 0;
	/**
	 * 车型
	 */
	public int ProductID = 0;
	/**
	 * 修程
	 */
	public int LineID = 0;
	/**
	 * 完成数
	 */
	public int Finish = 0;
	// 辅助属性
	/**
	 * 局段
	 */
	public String Customer = "";
	/**
	 * 车型
	 */
	public String ProductNo = "";
	/**
	 * 修程
	 */
	public String LineName = "";

	public RPTDataDetails() {
	}

	public RPTDataDetails(int customerID, int productID, int lineID, int finish, String customer, String productNo,
			String lineName) {
		super();
		CustomerID = customerID;
		ProductID = productID;
		LineID = lineID;
		Finish = finish;
		Customer = customer;
		ProductNo = productNo;
		LineName = lineName;
	}

	public int getCustomerID() {
		return CustomerID;
	}

	public void setCustomerID(int customerID) {
		CustomerID = customerID;
	}

	public int getProductID() {
		return ProductID;
	}

	public void setProductID(int productID) {
		ProductID = productID;
	}

	public int getLineID() {
		return LineID;
	}

	public void setLineID(int lineID) {
		LineID = lineID;
	}

	public int getFinish() {
		return Finish;
	}

	public void setFinish(int finish) {
		Finish = finish;
	}

	public String getCustomer() {
		return Customer;
	}

	public void setCustomer(String customer) {
		Customer = customer;
	}

	public String getProductNo() {
		return ProductNo;
	}

	public void setProductNo(String productNo) {
		ProductNo = productNo;
	}

	public String getLineName() {
		return LineName;
	}

	public void setLineName(String lineName) {
		LineName = lineName;
	}
}
