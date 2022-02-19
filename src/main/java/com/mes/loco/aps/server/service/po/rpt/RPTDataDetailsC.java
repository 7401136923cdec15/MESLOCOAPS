package com.mes.loco.aps.server.service.po.rpt;

/**
 * 统计数据详情C5、C6
 * 
 * @author PengYouWang
 * @CreateTime 2020-6-17 17:05:34
 * @LastEditTime 2020-6-17 17:08:17
 *
 */
public class RPTDataDetailsC {

	/**
	 * 局段
	 */
	public int CustomerID = 0;
	/**
	 * 车型
	 */
	public int ProductID = 0;

	/**
	 * C5完成数
	 */
	public int C5Finish = 0;
	/**
	 * C6完成数
	 */
	public int C6Finish = 0;
	// 辅助属性
	/**
	 * 局段
	 */
	public String Customer = "";
	/**
	 * 车型
	 */
	public String ProductNo = "";

	public RPTDataDetailsC() {
	}

	public RPTDataDetailsC(int customerID, int productID, int c5Finish, int c6Finish, String customer,
			String productNo) {
		super();
		CustomerID = customerID;
		ProductID = productID;
		C5Finish = c5Finish;
		C6Finish = c6Finish;
		Customer = customer;
		ProductNo = productNo;
	}

	public int getC5Finish() {
		return C5Finish;
	}

	public void setC5Finish(int c5Finish) {
		C5Finish = c5Finish;
	}

	public int getC6Finish() {
		return C6Finish;
	}

	public void setC6Finish(int c6Finish) {
		C6Finish = c6Finish;
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
}
