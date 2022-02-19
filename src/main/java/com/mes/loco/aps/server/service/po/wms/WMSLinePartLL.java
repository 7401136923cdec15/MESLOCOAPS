package com.mes.loco.aps.server.service.po.wms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * WMS产线工位领料需求
 * 
 * @author YouWang·Peng
 * @CreateTime 2022-1-11 11:06:43
 */
public class WMSLinePartLL implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	public String warehouseId = "";
	public String customerId = "";
	public String orderType = "";
	public String docNo = "";
	public String soReferenceA = "";
	public String soReferenceB = "";
	public String soReferenceC = "";
	public String soReferenceD = "";
	public String priority = "";
	public String orderTime = "";
	public String expectedShipmentTime1 = "";
	public String requiredDeliveryTime = "";
	public String deliveryNo = "";
	public String consigneeId = "";
	public String consigneeName = "";
	public String consigneeContact = "";
	public String consigneeAddress1 = "";
	public String consigneeAddress2 = "";
	public String consigneeAddress3 = "";
	public String consigneeAddress4 = "";
	public String consigneeCountry = "";
	public String consigneeProvince = "";
	public String consigneeCity = "";
	public String consigneeDistrict = "";
	public String consigneeStreet = "";
	public String consigneeMail = "";
	public String consigneeTel1 = "";
	public String consigneeTel2 = "";
	public String consigneeZip = "";
	public String carrierId = "";
	public String carrierName = "";
	public String carrierFax = "";
	public String carrierMail = "";
	public String issuePartyId = "";
	public String issuePartyName = "";
	public String issuePartyAddress1 = "";
	public String issuePartyAddress2 = "";
	public String issuePartyAddress3 = "";
	public String issuePartyAddress4 = "";
	public String channel = "";
	public String shop = "";
	public String billingId = "";
	public String billingName = "";
	public String billingAddress1 = "";
	public String billingAddress2 = "";
	public String billingAddress3 = "";
	public String billingAddress4 = "";
	public String hedi01 = "";
	public String hedi02 = "";
	public String hedi03 = "";
	public String hedi04 = "";
	public String hedi05 = "";
	public String hedi06 = "";
	public String hedi07 = "";
	public String hedi08 = "";
	public String hedi09 = "";
	public String hedi10 = "";
	public String invoicePrintFlag = "";
	public String route = "";
	public String stop = "";
	public String userDefine1 = "";
	public String userDefine2 = "";
	public String userDefine3 = "";
	public String userDefine4 = "";
	public String userDefine5 = "";
	public String userDefine6 = "";
	public String userDefine7 = "";
	public String userDefine8 = "";
	public String userDefine9 = "";
	public String userDefine10 = "";
	public String notes = "";
	public String crossdockFlag = "";
	public List<WMSLLDetail> details = new ArrayList<WMSLLDetail>();

	public WMSLinePartLL() {
		super();
	}

	public WMSLinePartLL(String warehouseId, String customerId, String orderType, String docNo, String soReferenceA,
			String soReferenceB, String soReferenceC, String soReferenceD, String priority, String orderTime,
			String expectedShipmentTime1, String requiredDeliveryTime, String deliveryNo, String consigneeId,
			String consigneeName, String consigneeContact, String consigneeAddress1, String consigneeAddress2,
			String consigneeAddress3, String consigneeAddress4, String consigneeCountry, String consigneeProvince,
			String consigneeCity, String consigneeDistrict, String consigneeStreet, String consigneeMail,
			String consigneeTel1, String consigneeTel2, String consigneeZip, String carrierId, String carrierName,
			String carrierFax, String carrierMail, String issuePartyId, String issuePartyName,
			String issuePartyAddress1, String issuePartyAddress2, String issuePartyAddress3, String issuePartyAddress4,
			String channel, String shop, String billingId, String billingName, String billingAddress1,
			String billingAddress2, String billingAddress3, String billingAddress4, String hedi01, String hedi02,
			String hedi03, String hedi04, String hedi05, String hedi06, String hedi07, String hedi08, String hedi09,
			String hedi10, String invoicePrintFlag, String route, String stop, String userDefine1, String userDefine2,
			String userDefine3, String userDefine4, String userDefine5, String userDefine6, String userDefine7,
			String userDefine8, String userDefine9, String userDefine10, String notes, String crossdockFlag,
			List<WMSLLDetail> details) {
		super();
		this.warehouseId = warehouseId;
		this.customerId = customerId;
		this.orderType = orderType;
		this.docNo = docNo;
		this.soReferenceA = soReferenceA;
		this.soReferenceB = soReferenceB;
		this.soReferenceC = soReferenceC;
		this.soReferenceD = soReferenceD;
		this.priority = priority;
		this.orderTime = orderTime;
		this.expectedShipmentTime1 = expectedShipmentTime1;
		this.requiredDeliveryTime = requiredDeliveryTime;
		this.deliveryNo = deliveryNo;
		this.consigneeId = consigneeId;
		this.consigneeName = consigneeName;
		this.consigneeContact = consigneeContact;
		this.consigneeAddress1 = consigneeAddress1;
		this.consigneeAddress2 = consigneeAddress2;
		this.consigneeAddress3 = consigneeAddress3;
		this.consigneeAddress4 = consigneeAddress4;
		this.consigneeCountry = consigneeCountry;
		this.consigneeProvince = consigneeProvince;
		this.consigneeCity = consigneeCity;
		this.consigneeDistrict = consigneeDistrict;
		this.consigneeStreet = consigneeStreet;
		this.consigneeMail = consigneeMail;
		this.consigneeTel1 = consigneeTel1;
		this.consigneeTel2 = consigneeTel2;
		this.consigneeZip = consigneeZip;
		this.carrierId = carrierId;
		this.carrierName = carrierName;
		this.carrierFax = carrierFax;
		this.carrierMail = carrierMail;
		this.issuePartyId = issuePartyId;
		this.issuePartyName = issuePartyName;
		this.issuePartyAddress1 = issuePartyAddress1;
		this.issuePartyAddress2 = issuePartyAddress2;
		this.issuePartyAddress3 = issuePartyAddress3;
		this.issuePartyAddress4 = issuePartyAddress4;
		this.channel = channel;
		this.shop = shop;
		this.billingId = billingId;
		this.billingName = billingName;
		this.billingAddress1 = billingAddress1;
		this.billingAddress2 = billingAddress2;
		this.billingAddress3 = billingAddress3;
		this.billingAddress4 = billingAddress4;
		this.hedi01 = hedi01;
		this.hedi02 = hedi02;
		this.hedi03 = hedi03;
		this.hedi04 = hedi04;
		this.hedi05 = hedi05;
		this.hedi06 = hedi06;
		this.hedi07 = hedi07;
		this.hedi08 = hedi08;
		this.hedi09 = hedi09;
		this.hedi10 = hedi10;
		this.invoicePrintFlag = invoicePrintFlag;
		this.route = route;
		this.stop = stop;
		this.userDefine1 = userDefine1;
		this.userDefine2 = userDefine2;
		this.userDefine3 = userDefine3;
		this.userDefine4 = userDefine4;
		this.userDefine5 = userDefine5;
		this.userDefine6 = userDefine6;
		this.userDefine7 = userDefine7;
		this.userDefine8 = userDefine8;
		this.userDefine9 = userDefine9;
		this.userDefine10 = userDefine10;
		this.notes = notes;
		this.crossdockFlag = crossdockFlag;
		this.details = details;
	}

	public String getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getDocNo() {
		return docNo;
	}

	public void setDocNo(String docNo) {
		this.docNo = docNo;
	}

	public String getSoReferenceA() {
		return soReferenceA;
	}

	public void setSoReferenceA(String soReferenceA) {
		this.soReferenceA = soReferenceA;
	}

	public String getSoReferenceB() {
		return soReferenceB;
	}

	public void setSoReferenceB(String soReferenceB) {
		this.soReferenceB = soReferenceB;
	}

	public String getSoReferenceC() {
		return soReferenceC;
	}

	public void setSoReferenceC(String soReferenceC) {
		this.soReferenceC = soReferenceC;
	}

	public String getSoReferenceD() {
		return soReferenceD;
	}

	public void setSoReferenceD(String soReferenceD) {
		this.soReferenceD = soReferenceD;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getExpectedShipmentTime1() {
		return expectedShipmentTime1;
	}

	public void setExpectedShipmentTime1(String expectedShipmentTime1) {
		this.expectedShipmentTime1 = expectedShipmentTime1;
	}

	public String getRequiredDeliveryTime() {
		return requiredDeliveryTime;
	}

	public void setRequiredDeliveryTime(String requiredDeliveryTime) {
		this.requiredDeliveryTime = requiredDeliveryTime;
	}

	public String getDeliveryNo() {
		return deliveryNo;
	}

	public void setDeliveryNo(String deliveryNo) {
		this.deliveryNo = deliveryNo;
	}

	public String getConsigneeId() {
		return consigneeId;
	}

	public void setConsigneeId(String consigneeId) {
		this.consigneeId = consigneeId;
	}

	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

	public String getConsigneeContact() {
		return consigneeContact;
	}

	public void setConsigneeContact(String consigneeContact) {
		this.consigneeContact = consigneeContact;
	}

	public String getConsigneeAddress1() {
		return consigneeAddress1;
	}

	public void setConsigneeAddress1(String consigneeAddress1) {
		this.consigneeAddress1 = consigneeAddress1;
	}

	public String getConsigneeAddress2() {
		return consigneeAddress2;
	}

	public void setConsigneeAddress2(String consigneeAddress2) {
		this.consigneeAddress2 = consigneeAddress2;
	}

	public String getConsigneeAddress3() {
		return consigneeAddress3;
	}

	public void setConsigneeAddress3(String consigneeAddress3) {
		this.consigneeAddress3 = consigneeAddress3;
	}

	public String getConsigneeAddress4() {
		return consigneeAddress4;
	}

	public void setConsigneeAddress4(String consigneeAddress4) {
		this.consigneeAddress4 = consigneeAddress4;
	}

	public String getConsigneeCountry() {
		return consigneeCountry;
	}

	public void setConsigneeCountry(String consigneeCountry) {
		this.consigneeCountry = consigneeCountry;
	}

	public String getConsigneeProvince() {
		return consigneeProvince;
	}

	public void setConsigneeProvince(String consigneeProvince) {
		this.consigneeProvince = consigneeProvince;
	}

	public String getConsigneeCity() {
		return consigneeCity;
	}

	public void setConsigneeCity(String consigneeCity) {
		this.consigneeCity = consigneeCity;
	}

	public String getConsigneeDistrict() {
		return consigneeDistrict;
	}

	public void setConsigneeDistrict(String consigneeDistrict) {
		this.consigneeDistrict = consigneeDistrict;
	}

	public String getConsigneeStreet() {
		return consigneeStreet;
	}

	public void setConsigneeStreet(String consigneeStreet) {
		this.consigneeStreet = consigneeStreet;
	}

	public String getConsigneeMail() {
		return consigneeMail;
	}

	public void setConsigneeMail(String consigneeMail) {
		this.consigneeMail = consigneeMail;
	}

	public String getConsigneeTel1() {
		return consigneeTel1;
	}

	public void setConsigneeTel1(String consigneeTel1) {
		this.consigneeTel1 = consigneeTel1;
	}

	public String getConsigneeTel2() {
		return consigneeTel2;
	}

	public void setConsigneeTel2(String consigneeTel2) {
		this.consigneeTel2 = consigneeTel2;
	}

	public String getConsigneeZip() {
		return consigneeZip;
	}

	public void setConsigneeZip(String consigneeZip) {
		this.consigneeZip = consigneeZip;
	}

	public String getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(String carrierId) {
		this.carrierId = carrierId;
	}

	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}

	public String getCarrierFax() {
		return carrierFax;
	}

	public void setCarrierFax(String carrierFax) {
		this.carrierFax = carrierFax;
	}

	public String getCarrierMail() {
		return carrierMail;
	}

	public void setCarrierMail(String carrierMail) {
		this.carrierMail = carrierMail;
	}

	public String getIssuePartyId() {
		return issuePartyId;
	}

	public void setIssuePartyId(String issuePartyId) {
		this.issuePartyId = issuePartyId;
	}

	public String getIssuePartyName() {
		return issuePartyName;
	}

	public void setIssuePartyName(String issuePartyName) {
		this.issuePartyName = issuePartyName;
	}

	public String getIssuePartyAddress1() {
		return issuePartyAddress1;
	}

	public void setIssuePartyAddress1(String issuePartyAddress1) {
		this.issuePartyAddress1 = issuePartyAddress1;
	}

	public String getIssuePartyAddress2() {
		return issuePartyAddress2;
	}

	public void setIssuePartyAddress2(String issuePartyAddress2) {
		this.issuePartyAddress2 = issuePartyAddress2;
	}

	public String getIssuePartyAddress3() {
		return issuePartyAddress3;
	}

	public void setIssuePartyAddress3(String issuePartyAddress3) {
		this.issuePartyAddress3 = issuePartyAddress3;
	}

	public String getIssuePartyAddress4() {
		return issuePartyAddress4;
	}

	public void setIssuePartyAddress4(String issuePartyAddress4) {
		this.issuePartyAddress4 = issuePartyAddress4;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getShop() {
		return shop;
	}

	public void setShop(String shop) {
		this.shop = shop;
	}

	public String getBillingId() {
		return billingId;
	}

	public void setBillingId(String billingId) {
		this.billingId = billingId;
	}

	public String getBillingName() {
		return billingName;
	}

	public void setBillingName(String billingName) {
		this.billingName = billingName;
	}

	public String getBillingAddress1() {
		return billingAddress1;
	}

	public void setBillingAddress1(String billingAddress1) {
		this.billingAddress1 = billingAddress1;
	}

	public String getBillingAddress2() {
		return billingAddress2;
	}

	public void setBillingAddress2(String billingAddress2) {
		this.billingAddress2 = billingAddress2;
	}

	public String getBillingAddress3() {
		return billingAddress3;
	}

	public void setBillingAddress3(String billingAddress3) {
		this.billingAddress3 = billingAddress3;
	}

	public String getBillingAddress4() {
		return billingAddress4;
	}

	public void setBillingAddress4(String billingAddress4) {
		this.billingAddress4 = billingAddress4;
	}

	public String getHedi01() {
		return hedi01;
	}

	public void setHedi01(String hedi01) {
		this.hedi01 = hedi01;
	}

	public String getHedi02() {
		return hedi02;
	}

	public void setHedi02(String hedi02) {
		this.hedi02 = hedi02;
	}

	public String getHedi03() {
		return hedi03;
	}

	public void setHedi03(String hedi03) {
		this.hedi03 = hedi03;
	}

	public String getHedi04() {
		return hedi04;
	}

	public void setHedi04(String hedi04) {
		this.hedi04 = hedi04;
	}

	public String getHedi05() {
		return hedi05;
	}

	public void setHedi05(String hedi05) {
		this.hedi05 = hedi05;
	}

	public String getHedi06() {
		return hedi06;
	}

	public void setHedi06(String hedi06) {
		this.hedi06 = hedi06;
	}

	public String getHedi07() {
		return hedi07;
	}

	public void setHedi07(String hedi07) {
		this.hedi07 = hedi07;
	}

	public String getHedi08() {
		return hedi08;
	}

	public void setHedi08(String hedi08) {
		this.hedi08 = hedi08;
	}

	public String getHedi09() {
		return hedi09;
	}

	public void setHedi09(String hedi09) {
		this.hedi09 = hedi09;
	}

	public String getHedi10() {
		return hedi10;
	}

	public void setHedi10(String hedi10) {
		this.hedi10 = hedi10;
	}

	public String getInvoicePrintFlag() {
		return invoicePrintFlag;
	}

	public void setInvoicePrintFlag(String invoicePrintFlag) {
		this.invoicePrintFlag = invoicePrintFlag;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getStop() {
		return stop;
	}

	public void setStop(String stop) {
		this.stop = stop;
	}

	public String getUserDefine1() {
		return userDefine1;
	}

	public void setUserDefine1(String userDefine1) {
		this.userDefine1 = userDefine1;
	}

	public String getUserDefine2() {
		return userDefine2;
	}

	public void setUserDefine2(String userDefine2) {
		this.userDefine2 = userDefine2;
	}

	public String getUserDefine3() {
		return userDefine3;
	}

	public void setUserDefine3(String userDefine3) {
		this.userDefine3 = userDefine3;
	}

	public String getUserDefine4() {
		return userDefine4;
	}

	public void setUserDefine4(String userDefine4) {
		this.userDefine4 = userDefine4;
	}

	public String getUserDefine5() {
		return userDefine5;
	}

	public void setUserDefine5(String userDefine5) {
		this.userDefine5 = userDefine5;
	}

	public String getUserDefine6() {
		return userDefine6;
	}

	public void setUserDefine6(String userDefine6) {
		this.userDefine6 = userDefine6;
	}

	public String getUserDefine7() {
		return userDefine7;
	}

	public void setUserDefine7(String userDefine7) {
		this.userDefine7 = userDefine7;
	}

	public String getUserDefine8() {
		return userDefine8;
	}

	public void setUserDefine8(String userDefine8) {
		this.userDefine8 = userDefine8;
	}

	public String getUserDefine9() {
		return userDefine9;
	}

	public void setUserDefine9(String userDefine9) {
		this.userDefine9 = userDefine9;
	}

	public String getUserDefine10() {
		return userDefine10;
	}

	public void setUserDefine10(String userDefine10) {
		this.userDefine10 = userDefine10;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getCrossdockFlag() {
		return crossdockFlag;
	}

	public void setCrossdockFlag(String crossdockFlag) {
		this.crossdockFlag = crossdockFlag;
	}

	public List<WMSLLDetail> getDetails() {
		return details;
	}

	public void setDetails(List<WMSLLDetail> details) {
		this.details = details;
	}

}
