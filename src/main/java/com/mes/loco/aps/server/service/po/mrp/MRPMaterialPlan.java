package com.mes.loco.aps.server.service.po.mrp;

import java.io.Serializable;
import java.util.Calendar;

/**
 * 物料需求计划
 * 
 * @author YouWang·Peng
 * @CreateTime 2022-3-8 11:02:07
 */
public class MRPMaterialPlan implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	public int ID = 0;
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
	 * 局段
	 */
	public String CustomerName = "";
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
	 * 工序ID
	 */
	public int StepID = 0;
	/**
	 * 工序名称
	 */
	public String StepName = "";
	/**
	 * 物料ID
	 */
	public int MaterialID = 0;
	/**
	 * 物料名称
	 */
	public String MaterialName = "";
	/**
	 * 物料编号
	 */
	public String MaterialNo = "";
	/**
	 * 物料类型ID
	 */
	public int MaterialType = 0;
	/**
	 * 物料类型名称
	 */
	public String MaterialTypeText = "";
	/**
	 * 需求数量
	 */
	public double FQTY = 0.0;
	/**
	 * 需求日期
	 */
	public Calendar DemandDate = Calendar.getInstance();
	/**
	 * 需求日期
	 */
	public String DemandDateText = "";
	/**
	 * 周计划序列号
	 */
	public String WeekPlanSerialNo = "";
	/**
	 * 激活状态
	 */
	public int Active = 0;
	/**
	 * 激活文本
	 */
	public String ActiveText = "";
	/**
	 * 创建时刻
	 */
	public Calendar CreateTime = Calendar.getInstance();
	/**
	 * 创建时刻
	 */
	public String CreateTimeText = "";
	/**
	 * 创建人ID
	 */
	public int CreateID = 0;
	/**
	 * 创建人
	 */
	public String Creator = "";
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
	 * 状态
	 */
	public int Status = 0;
	/**
	 * 状态文本
	 */
	public String StatusText = "";
	/**
	 * 编辑人ID
	 */
	public int EditID = 0;
	/**
	 * 编辑人
	 */
	public String Editor = "";
	/**
	 * 编辑时刻
	 */
	public Calendar EditTime = Calendar.getInstance();
	/**
	 * 编辑时刻文本
	 */
	public String EditTimeText = "";
	/**
	 * 领料需求单ID
	 */
	public int WMSPickDemandID = 0;
	/**
	 * 项目号
	 */
	public String WBSNo = "";
	/**
	 * 评估类型
	 */
	public String AssessmentType = "";

	public MRPMaterialPlan() {
		super();
	}

	public MRPMaterialPlan(int iD, int productID, int lineID, int customerID, int orderID, String partNo, int partID,
			int stepID, int materialID, String materialName, String materialNo, int materialType, double fQTY,
			Calendar demandDate, String weekPlanSerialNo, int active, Calendar createTime, int createID,
			int replaceType, int outSourceType, int status, String wBSNo, String wAssessmentType) {
		super();
		ID = iD;
		ProductID = productID;
		LineID = lineID;
		CustomerID = customerID;
		OrderID = orderID;
		PartNo = partNo;
		PartID = partID;
		StepID = stepID;
		MaterialID = materialID;
		MaterialName = materialName;
		MaterialNo = materialNo;
		MaterialType = materialType;
		FQTY = fQTY;
		DemandDate = demandDate;
		WeekPlanSerialNo = weekPlanSerialNo;
		Active = active;
		CreateTime = createTime;
		CreateID = createID;
		ReplaceType = replaceType;
		OutSourceType = outSourceType;
		Status = status;
		WBSNo = wBSNo;
		AssessmentType = wAssessmentType;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
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

	public int getOrderID() {
		return OrderID;
	}

	public void setOrderID(int orderID) {
		OrderID = orderID;
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

	public int getStepID() {
		return StepID;
	}

	public void setStepID(int stepID) {
		StepID = stepID;
	}

	public String getStepName() {
		return StepName;
	}

	public void setStepName(String stepName) {
		StepName = stepName;
	}

	public int getMaterialID() {
		return MaterialID;
	}

	public void setMaterialID(int materialID) {
		MaterialID = materialID;
	}

	public String getMaterialName() {
		return MaterialName;
	}

	public void setMaterialName(String materialName) {
		MaterialName = materialName;
	}

	public String getMaterialNo() {
		return MaterialNo;
	}

	public void setMaterialNo(String materialNo) {
		MaterialNo = materialNo;
	}

	public int getMaterialType() {
		return MaterialType;
	}

	public void setMaterialType(int materialType) {
		MaterialType = materialType;
	}

	public String getMaterialTypeText() {
		return MaterialTypeText;
	}

	public void setMaterialTypeText(String materialTypeText) {
		MaterialTypeText = materialTypeText;
	}

	public double getFQTY() {
		return FQTY;
	}

	public void setFQTY(double fQTY) {
		FQTY = fQTY;
	}

	public Calendar getDemandDate() {
		return DemandDate;
	}

	public void setDemandDate(Calendar demandDate) {
		DemandDate = demandDate;
	}

	public String getWeekPlanSerialNo() {
		return WeekPlanSerialNo;
	}

	public void setWeekPlanSerialNo(String weekPlanSerialNo) {
		WeekPlanSerialNo = weekPlanSerialNo;
	}

	public int getActive() {
		return Active;
	}

	public void setActive(int active) {
		Active = active;
	}

	public String getActiveText() {
		return ActiveText;
	}

	public void setActiveText(String activeText) {
		ActiveText = activeText;
	}

	public Calendar getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(Calendar createTime) {
		CreateTime = createTime;
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

	public String getDemandDateText() {
		return DemandDateText;
	}

	public void setDemandDateText(String demandDateText) {
		DemandDateText = demandDateText;
	}

	public String getCreateTimeText() {
		return CreateTimeText;
	}

	public void setCreateTimeText(String createTimeText) {
		CreateTimeText = createTimeText;
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

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public String getStatusText() {
		return StatusText;
	}

	public void setStatusText(String statusText) {
		StatusText = statusText;
	}

	public int getEditID() {
		return EditID;
	}

	public void setEditID(int editID) {
		EditID = editID;
	}

	public String getEditor() {
		return Editor;
	}

	public void setEditor(String editor) {
		Editor = editor;
	}

	public int getWMSPickDemandID() {
		return WMSPickDemandID;
	}

	public void setWMSPickDemandID(int wMSPickDemandID) {
		WMSPickDemandID = wMSPickDemandID;
	}

	public Calendar getEditTime() {
		return EditTime;
	}

	public void setEditTime(Calendar editTime) {
		EditTime = editTime;
	}

	public String getEditTimeText() {
		return EditTimeText;
	}

	public void setEditTimeText(String editTimeText) {
		EditTimeText = editTimeText;
	}

	public String getWBSNo() {
		return WBSNo;
	}

	public void setWBSNo(String wBSNo) {
		WBSNo = wBSNo;
	}

	public String getAssessmentType() {
		return AssessmentType;
	}

	public void setAssessmentType(String assessmentType) {
		AssessmentType = assessmentType;
	}
}
