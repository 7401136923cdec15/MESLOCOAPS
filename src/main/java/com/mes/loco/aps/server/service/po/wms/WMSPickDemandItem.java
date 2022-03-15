package com.mes.loco.aps.server.service.po.wms;

import java.io.Serializable;

/**
 * 产线领料需求明细
 * 
 * @author YouWang·Peng
 * @CreateTime 2022-1-6 09:27:05
 */
public class WMSPickDemandItem implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	public int ID = 0;
	/**
	 * 领料需求单ID
	 */
	public int DemandID = 0;
	/**
	 * 需求单号
	 */
	public String DemandNo = "";
	public String ProductNo = "";
	public String LineName = "";
	public String CustomerName = "";
	public String PartNo = "";
	public String PartName = "";
	/**
	 * 物料ID
	 */
	public int MaterialID = 0;
	/**
	 * 物料编码
	 */
	public String MaterialNo = "";
	/**
	 * 物料名称
	 */
	public String MaterialName = "";
	/**
	 * 数量
	 */
	public double FQTY = 0.0;
	/**
	 * WBS元素
	 */
	public String WBSNo = "";
	/**
	 * 工序ID
	 */
	public int PartPointID = 0;
	/**
	 * 工序编码
	 */
	public String PartPointCode = "";
	/**
	 * 工序名称
	 */
	public String PartPointName = "";
	/**
	 * 行号
	 */
	public String RowNo = "";
	/**
	 * 配套标记
	 */
	public String GroupFlag = "";
	/**
	 * 必换偶换
	 */
	public int ReplaceType = 0;
	/**
	 * 必换偶换
	 */
	public String ReplaceTypeText = "";
	/**
	 * 必修偶修
	 */
	public int OutSourceType = 0;
	/**
	 * 必修偶修
	 */
	public String OutSourceTypeText = "";
	/**
	 * 评估类型
	 */
	public String AssessmentType = "";
	/**
	 * 齐套标记
	 */
	public String KittingFlag = "";

	public WMSPickDemandItem() {
		super();
	}

	public WMSPickDemandItem(int iD, int demandID, int materialID, String materialNo, String materialName, double fQTY,
			String wBSNo, int partPointID, String partPointCode, String partPointName, String rowNo, String groupFlag,
			int replaceType, String replaceTypeText, int outSourceType, String outSourceTypeText, String assessmentType,
			String kittingFlag) {
		super();
		ID = iD;
		DemandID = demandID;
		MaterialID = materialID;
		MaterialNo = materialNo;
		MaterialName = materialName;
		FQTY = fQTY;
		WBSNo = wBSNo;
		PartPointID = partPointID;
		PartPointCode = partPointCode;
		PartPointName = partPointName;
		RowNo = rowNo;
		GroupFlag = groupFlag;
		ReplaceType = replaceType;
		ReplaceTypeText = replaceTypeText;
		OutSourceType = outSourceType;
		OutSourceTypeText = outSourceTypeText;
		AssessmentType = assessmentType;
		KittingFlag = kittingFlag;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getDemandID() {
		return DemandID;
	}

	public void setDemandID(int demandID) {
		DemandID = demandID;
	}

	public int getMaterialID() {
		return MaterialID;
	}

	public void setMaterialID(int materialID) {
		MaterialID = materialID;
	}

	public String getMaterialNo() {
		return MaterialNo;
	}

	public void setMaterialNo(String materialNo) {
		MaterialNo = materialNo;
	}

	public String getMaterialName() {
		return MaterialName;
	}

	public void setMaterialName(String materialName) {
		MaterialName = materialName;
	}

	public double getFQTY() {
		return FQTY;
	}

	public void setFQTY(double fQTY) {
		FQTY = fQTY;
	}

	public String getWBSNo() {
		return WBSNo;
	}

	public void setWBSNo(String wBSNo) {
		WBSNo = wBSNo;
	}

	public int getPartPointID() {
		return PartPointID;
	}

	public void setPartPointID(int partPointID) {
		PartPointID = partPointID;
	}

	public String getPartPointCode() {
		return PartPointCode;
	}

	public void setPartPointCode(String partPointCode) {
		PartPointCode = partPointCode;
	}

	public String getPartPointName() {
		return PartPointName;
	}

	public void setPartPointName(String partPointName) {
		PartPointName = partPointName;
	}

	public String getRowNo() {
		return RowNo;
	}

	public void setRowNo(String rowNo) {
		RowNo = rowNo;
	}

	public String getGroupFlag() {
		return GroupFlag;
	}

	public void setGroupFlag(String groupFlag) {
		GroupFlag = groupFlag;
	}

	public int getReplaceType() {
		return ReplaceType;
	}

	public void setReplaceType(int replaceType) {
		ReplaceType = replaceType;
	}

	public String getReplaceTypeText() {
		return ReplaceTypeText;
	}

	public void setReplaceTypeText(String replaceTypeText) {
		ReplaceTypeText = replaceTypeText;
	}

	public int getOutSourceType() {
		return OutSourceType;
	}

	public void setOutSourceType(int outSourceType) {
		OutSourceType = outSourceType;
	}

	public String getOutSourceTypeText() {
		return OutSourceTypeText;
	}

	public void setOutSourceTypeText(String outSourceTypeText) {
		OutSourceTypeText = outSourceTypeText;
	}

	public String getDemandNo() {
		return DemandNo;
	}

	public void setDemandNo(String demandNo) {
		DemandNo = demandNo;
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

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}

	public String getPartName() {
		return PartName;
	}

	public void setPartName(String partName) {
		PartName = partName;
	}

	public String getPartNo() {
		return PartNo;
	}

	public void setPartNo(String partNo) {
		PartNo = partNo;
	}

	public String getAssessmentType() {
		return AssessmentType;
	}

	public void setAssessmentType(String assessmentType) {
		AssessmentType = assessmentType;
	}

	public String getKittingFlag() {
		return KittingFlag;
	}

	public void setKittingFlag(String kittingFlag) {
		KittingFlag = kittingFlag;
	}
}
