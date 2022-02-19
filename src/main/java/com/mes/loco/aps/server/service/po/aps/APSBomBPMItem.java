package com.mes.loco.aps.server.service.po.aps;

import java.io.Serializable;
import java.util.Calendar;

/**
 * 台车BOM审批流子项
 * 
 * 2021-11-12 10:23:44
 *
 */
public class APSBomBPMItem implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	public int ID = 0;
	/**
	 * 台车BOM审批主表ID
	 */
	public int APSBomBPMID = 0;
	/**
	 * 原台车BOMID
	 */
	public int APSBomItemID = 0;
	/**
	 * BOM类型 检修/新造
	 */
	public int BOMType = 0;
	/**
	 * 工厂
	 */
	public int FactoryID = 1900;
	/**
	 * WBS编号
	 */
	public String WBSNo = "";
	/**
	 * 订单ID
	 */
	public int OrderID = 0;
	/**
	 * 车号
	 */
	public String PartNo = "";
	/**
	 * 修程ID
	 */
	public int LineID = 0;
	/**
	 * 修程名称
	 */
	public String LineName = "";
	/**
	 * 车型ID
	 */
	public int ProductID = 0;
	/**
	 * 车型编号
	 */
	public String ProductNo = "";
	/**
	 * 局段ID
	 */
	public int CustomerID = 0;
	/**
	 * 局段编码
	 */
	public String CustomerCode = "";
	/**
	 * 局段名称
	 */
	public String CustomerName = "";
	/**
	 * 工位ID
	 */
	public int PartID = 0;
	/**
	 * 工位名称
	 */
	public String PartName = "";
	/**
	 * 工序ID
	 */
	public int PartPointID = 0;
	/**
	 * 工序名称/工序描述
	 */
	public String PartPointName = "";
	/**
	 * 物料ID
	 */
	public int MaterialID = 0;
	/**
	 * 物料编号
	 */
	public String MaterialNo = "";
	/**
	 * 物料名称
	 */
	public String MaterialName = "";
	/**
	 * 用量
	 */
	public Double Number = 0.0;
	/**
	 * 单位ID
	 */
	public int UnitID = 0;
	/**
	 * 单位名称
	 */
	public String UnitText = "PC";
	/**
	 * 必换1/偶换2/计划外 9
	 */
	public int ReplaceType = 0;
	/**
	 * 委外必修件1/委外偶修件2/自修必修3/自修偶修4/ 其他0
	 */
	public int OutsourceType = 0;
	/**
	 * 原拆原装要求 X/空
	 */
	public int OriginalType = 0;
	/**
	 * 是否拆解下车 X/空
	 */
	public int DisassyType = 0;
	/**
	 * 是否超修程 X/空
	 */
	public int OverLine = 0;
	/**
	 * 是否互唤件 X/空
	 */
	public int PartChange = 0;
	/**
	 * 领料部门
	 */
	public String ReceiveDepart = "0001";
	/**
	 * 仓库号 1100新造 1200检修
	 */
	public int StockID = 0;
	/**
	 * 质量损失大类 报废001 返工返修002 停产003 内部质量收入004
	 */
	public int QTType = 0;
	/**
	 * 质量损失小类 设计差错01 工艺差错02 制造差错03 供方原因04 其他原因05
	 */
	public int QTItemType = 0;
	/**
	 * 客户供料 X/空
	 */
	public int CustomerMaterial = 0;
	/**
	 * 审批人ID
	 */
	public int AuditorID = 0;
	/**
	 * 审批人名称
	 */
	public String Auditor = "";
	/**
	 * 审批时刻
	 */
	public Calendar AuditTime = Calendar.getInstance();
	/**
	 * 编辑人ID
	 */
	public int EditorID = 0;
	/**
	 * 编辑人名称
	 */
	public String Editor = "";
	/**
	 * 编辑时刻
	 */
	public Calendar EditTime = Calendar.getInstance();
	/**
	 * 状态
	 */
	public int Status = 0;

	// 2020-12-29 14:03:43
	/**
	 * 相关需求编号(主台车BOM)
	 */
	public String RelaDemandNo = "";
	/**
	 * 标文本码
	 */
	public String TextCode = "";
	/**
	 * 工作中心
	 */
	public String WorkCenter = "";
	/**
	 * 删除标识
	 */
	public String DeleteID = "";
	/**
	 * 相关需求的项目编号
	 */
	public String SubRelaDemandNo = "";
	/**
	 * 评估类型
	 */
	public String AssessmentType = "常规新件";
	/**
	 * 附件标志
	 */
	public String AccessoryLogo = "";
	/**
	 * 检修件分类
	 */
	public String RepairPartClass = "";
	/**
	 * 备注
	 */
	public String Remark = "";
	/**
	 * 定容号组
	 */
	public String DingrongGroup = "";
	/**
	 * 修复旧件标识
	 */
	public String RepairCoreIdentification = "";
	/**
	 * 领料数量
	 */
	public int PickingQuantity = 0;
	/**
	 * 偶换率
	 */
	public double EvenExchangeRate = 0.0;
	/**
	 * 委托单位
	 */
	public String Client = "";
	/**
	 * 顺序号
	 */
	public int OrderNum = 0;

	// 2021-3-17 14:30:06新增
	/**
	 * 来源类型
	 */
	public int SourceType = 0;
	/**
	 * 来源ID
	 */
	public int SourceID = 0;
	/**
	 * 是否为差异项
	 */
	public int DifferenceItem = 0;
	/**
	 * 是否超定额
	 */
	public int OverQuota = 0;

	/**
	 * SAP推送状态 0,1成功，2失败
	 */
	public int SAPStatus = 0;
	/**
	 * SAP消息文本
	 */
	public String SAPMsg = "";
	/**
	 * 修改后的用量
	 */
	public double NewNumber = 0.0;
	/**
	 * 修改后的必换偶换属性
	 */
	public int NewReplaceType = 0;
	/**
	 * 修改后的委外修属性
	 */
	public int NewOutsourceType = 0;
	/**
	 * 修改后的是否互换件属性
	 */
	public int NewPartChange = 0;
	/**
	 * 修改后的质量损失大类属性
	 */
	public int NewQTType = 0;
	/**
	 * 修改后的质量损失小类属性
	 */
	public int NewQTItemType = 0;
	/**
	 * 修改后的评估类型属性
	 */
	public String NewAssessmentType = "";
	/**
	 * 修改后的备注属性
	 */
	public String NewRemark = "";

	/**
	 * 操作类型
	 */
	public int Type = 0;

	public APSBomBPMItem(int iD, int aPSBomBPMID, int aPSBomItemID, int bOMType, int factoryID, String wBSNo,
			int orderID, String partNo, int lineID, String lineName, int productID, String productNo, int customerID,
			String customerCode, int partID, String partName, int partPointID, String partPointName, int materialID,
			String materialNo, String materialName, Double number, int unitID, String unitText, int replaceType,
			int outsourceType, int originalType, int disassyType, int overLine, int partChange, String receiveDepart,
			int stockID, int qTType, int qTItemType, int customerMaterial, int auditorID, String auditor,
			Calendar auditTime, int editorID, String editor, Calendar editTime, int status, String relaDemandNo,
			String textCode, String workCenter, String deleteID, String subRelaDemandNo, String assessmentType,
			String accessoryLogo, String repairPartClass, String remark, String dingrongGroup,
			String repairCoreIdentification, int pickingQuantity, double evenExchangeRate, String client, int orderNum,
			int sourceType, int sourceID, int differenceItem, int overQuota, int sAPStatus, String sAPMsg,
			double newNumber, int newReplaceType, int newOutsourceType, int newPartChange, int newQTType,
			int newQTItemType, String newAssessmentType, String newRemark) {
		super();
		ID = iD;
		APSBomBPMID = aPSBomBPMID;
		APSBomItemID = aPSBomItemID;
		BOMType = bOMType;
		FactoryID = factoryID;
		WBSNo = wBSNo;
		OrderID = orderID;
		PartNo = partNo;
		LineID = lineID;
		LineName = lineName;
		ProductID = productID;
		ProductNo = productNo;
		CustomerID = customerID;
		CustomerCode = customerCode;
		PartID = partID;
		PartName = partName;
		PartPointID = partPointID;
		PartPointName = partPointName;
		MaterialID = materialID;
		MaterialNo = materialNo;
		MaterialName = materialName;
		Number = number;
		UnitID = unitID;
		UnitText = unitText;
		ReplaceType = replaceType;
		OutsourceType = outsourceType;
		OriginalType = originalType;
		DisassyType = disassyType;
		OverLine = overLine;
		PartChange = partChange;
		ReceiveDepart = receiveDepart;
		StockID = stockID;
		QTType = qTType;
		QTItemType = qTItemType;
		CustomerMaterial = customerMaterial;
		AuditorID = auditorID;
		Auditor = auditor;
		AuditTime = auditTime;
		EditorID = editorID;
		Editor = editor;
		EditTime = editTime;
		Status = status;
		RelaDemandNo = relaDemandNo;
		TextCode = textCode;
		WorkCenter = workCenter;
		DeleteID = deleteID;
		SubRelaDemandNo = subRelaDemandNo;
		AssessmentType = assessmentType;
		AccessoryLogo = accessoryLogo;
		RepairPartClass = repairPartClass;
		Remark = remark;
		DingrongGroup = dingrongGroup;
		RepairCoreIdentification = repairCoreIdentification;
		PickingQuantity = pickingQuantity;
		EvenExchangeRate = evenExchangeRate;
		Client = client;
		OrderNum = orderNum;
		SourceType = sourceType;
		SourceID = sourceID;
		DifferenceItem = differenceItem;
		OverQuota = overQuota;
		SAPStatus = sAPStatus;
		SAPMsg = sAPMsg;
		NewNumber = newNumber;
		NewReplaceType = newReplaceType;
		NewOutsourceType = newOutsourceType;
		NewPartChange = newPartChange;
		NewQTType = newQTType;
		NewQTItemType = newQTItemType;
		NewAssessmentType = newAssessmentType;
		NewRemark = newRemark;
	}

	public APSBomBPMItem() {
	}

	public int getAPSBomBPMID() {
		return APSBomBPMID;
	}

	public void setAPSBomBPMID(int aPSBomBPMID) {
		APSBomBPMID = aPSBomBPMID;
	}

	public int getAPSBomItemID() {
		return APSBomItemID;
	}

	public void setAPSBomItemID(int aPSBomItemID) {
		APSBomItemID = aPSBomItemID;
	}

	public double getNewNumber() {
		return NewNumber;
	}

	public void setNewNumber(double newNumber) {
		NewNumber = newNumber;
	}

	public int getNewReplaceType() {
		return NewReplaceType;
	}

	public void setNewReplaceType(int newReplaceType) {
		NewReplaceType = newReplaceType;
	}

	public int getNewOutsourceType() {
		return NewOutsourceType;
	}

	public void setNewOutsourceType(int newOutsourceType) {
		NewOutsourceType = newOutsourceType;
	}

	public int getNewPartChange() {
		return NewPartChange;
	}

	public void setNewPartChange(int newPartChange) {
		NewPartChange = newPartChange;
	}

	public int getNewQTType() {
		return NewQTType;
	}

	public void setNewQTType(int newQTType) {
		NewQTType = newQTType;
	}

	public int getNewQTItemType() {
		return NewQTItemType;
	}

	public void setNewQTItemType(int newQTItemType) {
		NewQTItemType = newQTItemType;
	}

	public String getNewAssessmentType() {
		return NewAssessmentType;
	}

	public void setNewAssessmentType(String newAssessmentType) {
		NewAssessmentType = newAssessmentType;
	}

	public String getNewRemark() {
		return NewRemark;
	}

	public void setNewRemark(String newRemark) {
		NewRemark = newRemark;
	}

	public int getStockIDByBOMType(int wBOMType) {
		switch (wBOMType) {
		case 1:
			return 1100;
		case 2:
			return 1200;
		default:
			break;
		}
		return 0;
	}

	public int getBOMType() {
		return BOMType;
	}

	public String getRelaDemandNo() {
		return RelaDemandNo;
	}

	public void setRelaDemandNo(String relaDemandNo) {
		RelaDemandNo = relaDemandNo;
	}

	public String getTextCode() {
		return TextCode;
	}

	public void setTextCode(String textCode) {
		TextCode = textCode;
	}

	public int getDifferenceItem() {
		return DifferenceItem;
	}

	public int getOverQuota() {
		return OverQuota;
	}

	public void setDifferenceItem(int differenceItem) {
		DifferenceItem = differenceItem;
	}

	public void setOverQuota(int overQuota) {
		OverQuota = overQuota;
	}

	public String getWorkCenter() {
		return WorkCenter;
	}

	public void setWorkCenter(String workCenter) {
		WorkCenter = workCenter;
	}

	public String getDeleteID() {
		return DeleteID;
	}

	public void setDeleteID(String deleteID) {
		DeleteID = deleteID;
	}

	public String getSubRelaDemandNo() {
		return SubRelaDemandNo;
	}

	public void setSubRelaDemandNo(String subRelaDemandNo) {
		SubRelaDemandNo = subRelaDemandNo;
	}

	public String getAssessmentType() {
		return AssessmentType;
	}

	public void setAssessmentType(String assessmentType) {
		AssessmentType = assessmentType;
	}

	public String getAccessoryLogo() {
		return AccessoryLogo;
	}

	public void setAccessoryLogo(String accessoryLogo) {
		AccessoryLogo = accessoryLogo;
	}

	public String getRepairPartClass() {
		return RepairPartClass;
	}

	public void setRepairPartClass(String repairPartClass) {
		RepairPartClass = repairPartClass;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	public String getDingrongGroup() {
		return DingrongGroup;
	}

	public void setDingrongGroup(String dingrongGroup) {
		DingrongGroup = dingrongGroup;
	}

	public String getRepairCoreIdentification() {
		return RepairCoreIdentification;
	}

	public void setRepairCoreIdentification(String repairCoreIdentification) {
		RepairCoreIdentification = repairCoreIdentification;
	}

	public int getPickingQuantity() {
		return PickingQuantity;
	}

	public void setPickingQuantity(int pickingQuantity) {
		PickingQuantity = pickingQuantity;
	}

	public double getEvenExchangeRate() {
		return EvenExchangeRate;
	}

	public void setEvenExchangeRate(double evenExchangeRate) {
		EvenExchangeRate = evenExchangeRate;
	}

	public String getClient() {
		return Client;
	}

	public void setClient(String client) {
		Client = client;
	}

	public void setBOMType(int bOMType) {
		BOMType = bOMType;
	}

	public int getFactoryID() {
		return FactoryID;
	}

	public void setFactoryID(int factoryID) {
		FactoryID = factoryID;
	}

	public String getWBSNo() {
		return WBSNo;
	}

	public void setWBSNo(String wBSNo) {
		WBSNo = wBSNo;
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

	public String getLineName() {
		return LineName;
	}

	public void setLineName(String lineName) {
		LineName = lineName;
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

	public int getPartID() {
		return PartID;
	}

	public void setPartID(int partID) {
		PartID = partID;
	}

	public String getPartNo() {
		return PartNo;
	}

	public void setPartNo(String partNo) {
		PartNo = partNo;
	}

	public int getPartPointID() {
		return PartPointID;
	}

	public void setPartPointID(int partPointID) {
		PartPointID = partPointID;
	}

	public String getPartPointName() {
		return PartPointName;
	}

	public void setPartPointName(String partPointName) {
		PartPointName = partPointName;
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

	public Double getNumber() {
		return Number;
	}

	public void setNumber(Double number) {
		Number = number;
	}

	public int getUnitID() {
		return UnitID;
	}

	public void setUnitID(int unitID) {
		UnitID = unitID;
	}

	public String getUnitText() {
		return UnitText;
	}

	public void setUnitText(String unitText) {
		UnitText = unitText;
	}

	public int getReplaceType() {
		return ReplaceType;
	}

	public void setReplaceType(int replaceType) {
		ReplaceType = replaceType;
	}

	public int getOutsourceType() {
		return OutsourceType;
	}

	public void setOutsourceType(int outsourceType) {
		OutsourceType = outsourceType;
	}

	public int getOriginalType() {
		return OriginalType;
	}

	public void setOriginalType(int originalType) {
		OriginalType = originalType;
	}

	public int getDisassyType() {
		return DisassyType;
	}

	public void setDisassyType(int disassyType) {
		DisassyType = disassyType;
	}

	public int getOverLine() {
		return OverLine;
	}

	public void setOverLine(int overLine) {
		OverLine = overLine;
	}

	public int getPartChange() {
		return PartChange;
	}

	public void setPartChange(int partChange) {
		PartChange = partChange;
	}

	public String getReceiveDepart() {
		return ReceiveDepart;
	}

	public void setReceiveDepart(String receiveDepart) {
		ReceiveDepart = receiveDepart;
	}

	public int getStockID() {
		return StockID;
	}

	public void setStockID(int stockID) {
		StockID = stockID;
	}

	public int getQTType() {
		return QTType;
	}

	public void setQTType(int qTType) {
		QTType = qTType;
	}

	public int getQTItemType() {
		return QTItemType;
	}

	public void setQTItemType(int qTItemType) {
		QTItemType = qTItemType;
	}

	public int getCustomerMaterial() {
		return CustomerMaterial;
	}

	public void setCustomerMaterial(int customerMaterial) {
		CustomerMaterial = customerMaterial;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getCustomerID() {
		return CustomerID;
	}

	public void setCustomerID(int customerID) {
		CustomerID = customerID;
	}

	public int getEditorID() {
		return EditorID;
	}

	public void setEditorID(int editorID) {
		EditorID = editorID;
	}

	public String getEditor() {
		return Editor;
	}

	public void setEditor(String editor) {
		Editor = editor;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public Calendar getEditTime() {
		return EditTime;
	}

	public void setEditTime(Calendar editTime) {
		EditTime = editTime;
	}

	public int getAuditorID() {
		return AuditorID;
	}

	public void setAuditorID(int auditorID) {
		AuditorID = auditorID;
	}

	public String getAuditor() {
		return Auditor;
	}

	public void setAuditor(String auditor) {
		Auditor = auditor;
	}

	public Calendar getAuditTime() {
		return AuditTime;
	}

	public void setAuditTime(Calendar auditTime) {
		AuditTime = auditTime;
	}

	public String getPartName() {
		return PartName;
	}

	public void setPartName(String partName) {
		PartName = partName;
	}

	public String getCustomerCode() {
		return CustomerCode;
	}

	public void setCustomerCode(String customerCode) {
		CustomerCode = customerCode;
	}

	public int getOrderNum() {
		return OrderNum;
	}

	public int getSourceType() {
		return SourceType;
	}

	public int getSourceID() {
		return SourceID;
	}

	public void setOrderNum(int orderNum) {
		OrderNum = orderNum;
	}

	public void setSourceType(int sourceType) {
		SourceType = sourceType;
	}

	public void setSourceID(int sourceID) {
		SourceID = sourceID;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getSAPStatus() {
		return SAPStatus;
	}

	public String getSAPMsg() {
		return SAPMsg;
	}

	public void setSAPStatus(int sAPStatus) {
		SAPStatus = sAPStatus;
	}

	public void setSAPMsg(String sAPMsg) {
		SAPMsg = sAPMsg;
	}

	public String getCustomerName() {
		return CustomerName;
	}

	public int getType() {
		return Type;
	}

	public void setType(int type) {
		Type = type;
	}

	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}
}
