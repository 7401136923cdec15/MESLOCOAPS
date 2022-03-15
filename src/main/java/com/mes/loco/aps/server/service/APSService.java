package com.mes.loco.aps.server.service;

import com.mes.loco.aps.server.service.mesenum.APSShiftPeriod;
import com.mes.loco.aps.server.service.mesenum.BPMEventModule;
import com.mes.loco.aps.server.service.mesenum.OMSOrderPriority;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.andon.AndonTime;
import com.mes.loco.aps.server.service.po.aps.APSAuditConfig;
import com.mes.loco.aps.server.service.po.aps.APSBOMItem;
import com.mes.loco.aps.server.service.po.aps.APSBOMLog;
import com.mes.loco.aps.server.service.po.aps.APSBomBPM;
import com.mes.loco.aps.server.service.po.aps.APSCondition;
import com.mes.loco.aps.server.service.po.aps.APSDayPlanAudit;
import com.mes.loco.aps.server.service.po.aps.APSDayPlanAuditBPM;
import com.mes.loco.aps.server.service.po.aps.APSDismantling;
import com.mes.loco.aps.server.service.po.aps.APSInstallation;
import com.mes.loco.aps.server.service.po.aps.APSManuCapacity;
import com.mes.loco.aps.server.service.po.aps.APSManuCapacityStep;
import com.mes.loco.aps.server.service.po.aps.APSMessage;
import com.mes.loco.aps.server.service.po.aps.APSPartNoDetails;
import com.mes.loco.aps.server.service.po.aps.APSPartsLog;
import com.mes.loco.aps.server.service.po.aps.APSSchedulingVersion;
import com.mes.loco.aps.server.service.po.aps.APSSchedulingVersionBPM;
import com.mes.loco.aps.server.service.po.aps.APSStepPlanParam;
import com.mes.loco.aps.server.service.po.aps.APSTaskLine;
import com.mes.loco.aps.server.service.po.aps.APSTaskPart;
import com.mes.loco.aps.server.service.po.aps.APSTaskStep;
import com.mes.loco.aps.server.service.po.aps.APSTaskStepCancelLog;
import com.mes.loco.aps.server.service.po.aps.APSTaskStepCancelLogBPM;
import com.mes.loco.aps.server.service.po.aps.APSTaskStepDetails;
import com.mes.loco.aps.server.service.po.aps.APSTaskStepInfo;
import com.mes.loco.aps.server.service.po.aps.APSTaskStepPlan;
import com.mes.loco.aps.server.service.po.aps.APSTriggerPart;
import com.mes.loco.aps.server.service.po.aps.APSWorkAreaDetails;
import com.mes.loco.aps.server.service.po.aps.APSWorkHour;
import com.mes.loco.aps.server.service.po.bms.BMSDepartment;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.bms.BMSWorkChargeItem;
import com.mes.loco.aps.server.service.po.bpm.BPMTaskBase;
import com.mes.loco.aps.server.service.po.excel.ExcelData;
import com.mes.loco.aps.server.service.po.fmc.FMCWorkspace;
import com.mes.loco.aps.server.service.po.fpc.FPCPart;
import com.mes.loco.aps.server.service.po.fpc.FPCProduct;
import com.mes.loco.aps.server.service.po.fpc.FPCRoute;
import com.mes.loco.aps.server.service.po.fpc.FPCRoutePart;
import com.mes.loco.aps.server.service.po.lfs.LFSWorkAreaStation;
import com.mes.loco.aps.server.service.po.mcs.MCSLogInfo;
import com.mes.loco.aps.server.service.po.mrp.MRPMaterialPlan;
import com.mes.loco.aps.server.service.po.mss.MSSBOMItem;
import com.mes.loco.aps.server.service.po.oms.OMSOrder;
import com.mes.loco.aps.server.service.po.oms.OMSOutsourceOrder;
import com.mes.loco.aps.server.service.po.sfc.SFCBOMTaskItem;
import com.mes.loco.aps.server.service.po.sfc.SFCTaskStepInfo;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public interface APSService {
	ServiceResult<APSManuCapacity> APS_QueryManuCapacity(BMSEmployee wLoginUser, int wID);

	ServiceResult<List<APSManuCapacity>> APS_QueryManuCapacityList(BMSEmployee wLoginUser, int wID, int wLineID,
			int wPartID, int wActive);

	ServiceResult<Long> APS_UpdateManuCapacity(BMSEmployee wLoginUser, APSManuCapacity wAPSManuCapacity);

	ServiceResult<Integer> APS_ActiveManuCapacityList(BMSEmployee wLoginUser, List<Integer> wIDList, int wActive);

	ServiceResult<APSDismantling> APS_QueryDismantling(BMSEmployee wLoginUser, int wID);

	ServiceResult<List<APSDismantling>> APS_QueryDismantlingList(BMSEmployee wLoginUser, int wID, int wLineID,
			int wPartID, int wProductID, int wMaterialID);

	ServiceResult<Long> APS_UpdateDismantling(BMSEmployee wLoginUser, APSDismantling wAPSDismantling);

	ServiceResult<APSInstallation> APS_QueryInstallation(BMSEmployee wLoginUser, int wID);

	ServiceResult<List<APSInstallation>> APS_QueryInstallationList(BMSEmployee wLoginUser, int wID, int wLineID,
			int wPartID, int wMaterialID, int wProductID);

	ServiceResult<Long> APS_UpdateInstallation(BMSEmployee wLoginUser, APSInstallation wAPSInstallation);

	ServiceResult<APSPartsLog> APS_QueryPartsLog(BMSEmployee wLoginUser, int wID);

	ServiceResult<List<APSPartsLog>> APS_QueryPartsLogList(BMSEmployee wLoginUser, int wID, int wProductID,
			String wPartNo);

	ServiceResult<Long> APS_UpdatePartsLog(BMSEmployee wLoginUser, APSPartsLog wAPSPartsLog);

	ServiceResult<APSTaskLine> APS_QueryTaskLine(BMSEmployee wLoginUser, int wID);

	ServiceResult<List<APSTaskLine>> APS_QueryTaskLineList(BMSEmployee wLoginUser, int wID, int wOrderID,
			int wWorkShopID, int wLineID, int wActive);

	ServiceResult<Long> APS_UpdateTaskLine(BMSEmployee wLoginUser, APSTaskLine wAPSTaskLine);

	ServiceResult<Integer> APS_ActiveTaskLineList(BMSEmployee wLoginUser, List<Integer> wIDList, int wActive);

	ServiceResult<APSTaskPart> APS_QueryTaskPart(BMSEmployee wLoginUser, int wID);

	ServiceResult<List<APSTaskPart>> APS_QueryTaskPartList(BMSEmployee wLoginUser, int wID, int wOrderID,
			int wTaskLineID, int wLineID, int wPartID, int wActive);

	ServiceResult<List<APSTaskPart>> APS_QueryTaskPartList(BMSEmployee wLoginUser, int wShiftPeriod,
			List<Integer> wStatusIDList, List<Integer> wOrderIDList);

	ServiceResult<Long> APS_UpdateTaskPart(BMSEmployee wLoginUser, APSTaskPart wAPSTaskPart);

	ServiceResult<Integer> APS_ActiveTaskPartList(BMSEmployee wLoginUser, List<Integer> wIDList, int wActive);

	ServiceResult<List<OMSOrder>> APS_SetOrderPriority(BMSEmployee wLoginUser, List<OMSOrder> wOrderList,
			List<OMSOrderPriority> wOrderPriorityList, List<Integer> wLineOrders, List<Integer> wCustomerOrders,
			List<Integer> wProductIDs);

	ServiceResult<List<APSTaskPart>> APS_AutoTaskPart(BMSEmployee wLoginUser, List<OMSOrder> wOrderList,
			APSShiftPeriod wShiftPeriod, List<APSTaskPart> wOrderPartIssuedList,
			List<OMSOutsourceOrder> wOutsourceOrderList, Calendar wStartTime, Calendar wEndTime,
			Map<Integer, List<FPCRoutePart>> wRoutePartList, List<APSManuCapacity> wManuCapacityList,
			List<APSInstallation> wAPSInstallationList, List<APSDismantling> wAPSDismantlingList,
			List<APSMessage> wMessageList, int wWorkDay, int wLimitMinutes, List<APSTaskPart> wMutualTaskList);

	ServiceResult<List<APSMessage>> APS_CheckTaskPart(BMSEmployee wLoginUser, List<OMSOrder> wOrderList,
			List<APSTaskPart> wCheckTaskList, APSShiftPeriod wShiftPeriod, List<APSTaskPart> wOrderPartIssuedList,
			List<OMSOutsourceOrder> wOutsourceOrderList, Calendar wStartTime, Calendar wEndTime,
			Map<Integer, List<FPCRoutePart>> wRoutePartList, List<APSManuCapacity> wManuCapacityList,
			List<APSInstallation> wAPSInstallationList, List<APSDismantling> wAPSDismantlingList, int wWorkDay);

	ServiceResult<APSMessage> APS_QueryMessage(BMSEmployee wLoginUser, int wID);

	ServiceResult<List<APSMessage>> APS_QueryMessageList(BMSEmployee wLoginUser, int wID, int wOrderID);

	ServiceResult<Long> APS_UpdateMessage(BMSEmployee wLoginUser, APSMessage wAPSMessage);

	ServiceResult<List<APSTaskStep>> APS_QueryTaskStepByShiftDate(BMSEmployee wLoginUser, Calendar wShiftDate,
			int wWork);

	ServiceResult<List<APSTaskStep>> APS_CreateTaskStepByShiftDate(BMSEmployee wLoginUser, Calendar wShiftDate);

	ServiceResult<List<Map<String, Object>>> APS_QueryTableInfoList(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime, int wAPSShiftPeriod);

	ServiceResult<APSTaskStep> APS_QueryTaskStep(BMSEmployee wLoginUser, int wID);

	ServiceResult<List<APSTaskStep>> APS_QueryTaskStepList(BMSEmployee wLoginUser, int wID, int wOrderID,
			int wTaskLineID, int wTaskPartID, int wWorkShopID, int wLineID, int wPartID, int wStepID, int wShiftID,
			int wActive, List<Integer> wStateIDList);

	ServiceResult<Integer> APS_UpdateTaskStep(BMSEmployee wLoginUser, APSTaskStep wAPSTaskStep);

	ServiceResult<Integer> APS_ActiveTaskStepList(BMSEmployee wLoginUser, List<Integer> wIDList, int wActive);

	ServiceResult<List<APSTaskStep>> APS_QueryTaskStepListByMonitorID(BMSEmployee wLoginUser, Calendar wTaskDate,
			int wMonitorID);

	ServiceResult<List<Map<String, Object>>> ChangeToTable(List<OMSOrder> wOrderList,
			List<APSTaskPart> wAPSTaskPartList, List<LFSWorkAreaStation> wStationList);

	ServiceResult<List<Map<String, Object>>> ChangeToTableByEndTime(List<OMSOrder> wOrderList,
			List<APSTaskPart> wAPSTaskPartList, List<LFSWorkAreaStation> wStationList);

	ServiceResult<List<APSTaskPart>> APS_QueryHistoryTaskPartList(BMSEmployee wLoginUser, int wAPSShiftPeriod,
			Calendar wStartTime, Calendar wEndTime);

	ServiceResult<List<APSMessage>> APS_QueryMessageListBySubmitTime(BMSEmployee wLoginUser, Calendar wSubmitTime);

	ServiceResult<APSCondition> APS_QueryCondition(BMSEmployee wLoginUser, int wID);

	ServiceResult<List<APSCondition>> APS_QueryConditionList(BMSEmployee wLoginUser, int wID, int wShiftPeriod);

	ServiceResult<Integer> APS_UpdateCondition(BMSEmployee wLoginUser, APSCondition wAPSCondition);

	ServiceResult<List<APSSchedulingVersion>> APS_QueryAuditTaskList(BMSEmployee wLoginUser, int wAPSShiftPeriod,
			Calendar wShiftDate, int wTagType);

	ServiceResult<Integer> APS_AuditOperate(BMSEmployee wLoginUser, APSSchedulingVersion wAPSSchedulingVersion,
			int wOperateType, int wAPSShiftPeriod);

	ServiceResult<APSAuditConfig> APS_QueryAuditConfig(BMSEmployee wLoginUser, int wID);

	ServiceResult<List<APSAuditConfig>> APS_QueryAuditConfigList(BMSEmployee wLoginUser, int wID, int wAPSShiftPeriod);

	ServiceResult<Integer> APS_UpdateAuditConfig(BMSEmployee wLoginUser, APSAuditConfig wAPSAuditConfig);

	ServiceResult<Integer> APS_DeleteAuditConfigList(BMSEmployee wLoginUser, List<APSAuditConfig> wAPSAuditConfigList);

	ServiceResult<List<FPCProduct>> APS_PlanProductList(BMSEmployee wLoginUser, int wShiftPeriod);

	ServiceResult<Integer> APS_AdjustHour(BMSEmployee wLoginUser, List<APSTaskStep> wAPSTaskStepList, double wHour);

	ServiceResult<List<APSTaskStep>> APS_QueryAuditStepTaskList(BMSEmployee wLoginUser, int wTagType,
			Calendar wShiftDate);

	ServiceResult<Integer> APS_AuditStepOperate(BMSEmployee wLoginUser, List<APSTaskStep> wTaskList, int wOperateType);

	ServiceResult<List<APSTaskPart>> APS_QueryTaskPartList(BMSEmployee wLoginUser, int wShiftID, int wShiftPeriod);

	ServiceResult<Integer> APS_SaveTaskPartList(BMSEmployee wLoginUser, Calendar wStartTime, Calendar wEndTime,
			List<APSTaskPart> wTaskPartList, List<APSMessage> wMessageList);

	ServiceResult<List<APSTaskStep>> APS_QueryLeaderTask(BMSEmployee wLoginUser, Calendar wShiftDate);

	ServiceResult<List<APSTaskPart>> APS_QueryRFTaskPartList(BMSEmployee wLoginUser, int wOrderID);

	ServiceResult<APSWorkHour> APS_QueryWorkHour(BMSEmployee wLoginUser);

	ServiceResult<Integer> APS_SaveWorkHour(BMSEmployee wLoginUser, int wMinWorkHour, int wMiddleWorkHour,
			int wMaxWorkHour);

	ServiceResult<Boolean> APS_CheckIsExist(BMSEmployee wLoginUser, Calendar wStartTime, APSShiftPeriod wShiftPeriod);

	ServiceResult<Integer> APS_QueryTodayToDoTaskPartList(BMSEmployee wLoginUser);

	ServiceResult<List<APSSchedulingVersion>> APS_QueryHistoryVersionList(BMSEmployee wLoginUser, int wAPSShiftPeriod,
			Calendar wStartTime, Calendar wEndTime);

	ServiceResult<List<FMCWorkspace>> APS_QueryPartNoStatus(BMSEmployee wLoginUser, List<FMCWorkspace> wList);

//	ServiceResult<List<APSTaskStep>> APS_QueryTaskStepByTime(BMSEmployee wLoginUser, Calendar wStartTime,
//			Calendar wEndTime, boolean wIsAudit);

	ServiceResult<List<OMSOrder>> APS_QueryCompleteOrderList(BMSEmployee wLoginUser);

	ServiceResult<Integer> APS_CompleteConfirm(BMSEmployee wLoginUser, int wOrderID, List<String> wImagePathList,
			String wRemark);

	ServiceResult<List<OMSOrder>> APS_QueryOutApplyOrderList(BMSEmployee wLoginUser);

	ServiceResult<Integer> APS_OutApply(BMSEmployee wLoginUser, int wOrderID, List<String> wImagePathList,
			String wRemark);

	ServiceResult<Integer> APS_OutConfirm(BMSEmployee wLoginUser, int wOrderID);

	ServiceResult<Integer> APS_AutoSendMessageToApplyOutPlant(BMSEmployee wLoginUser);

	ServiceResult<List<OMSOrder>> OMS_QueryInPlantList(BMSEmployee wLoginUser, int wType);

	ServiceResult<Integer> OMS_InPlantConfirm(BMSEmployee wLoginUser, int wOrderID, List<String> wImagePathList,
			String wRemark);

	ServiceResult<Integer> APS_StationRepairFrequencyTip(BMSEmployee wLoginUser);

	ServiceResult<List<APSTaskPart>> APS_QueryCompareList(BMSEmployee wLoginUser, List<Integer> wOrderIDList);

	/**
	 * 校验(月计划未排，无法排程周计划)
	 * 
	 * @param wLoginUser
	 * @param wOrderList
	 * @return
	 */
	ServiceResult<String> APS_CheckIsMakedMonthPlan(BMSEmployee wLoginUser, List<OMSOrder> wOrderList);

	/**
	 * 检查登录者是否是工区主管，若不是，提示返回
	 * 
	 * @param wLoginUser
	 * @return
	 */
	ServiceResult<String> APS_CheckIsAreaLeader(BMSEmployee wLoginUser);

	/**
	 * 查询派工记录(工序任务)
	 * 
	 * @param wLoginUser
	 * @param wStartTime
	 * @param wEndTime
	 * @return
	 */
	ServiceResult<List<APSTaskStep>> APS_QueryTaskStepListByTime(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime);

	/**
	 * 日计划查询 通过时间 工区 修程 工位查询日计划
	 */
	ServiceResult<List<APSTaskStep>> APS_QueryTaskStepByConditions(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime, int wAreaID, int wLineID, int wPartID);

	/**
	 * 获取派工工序任务工位统计数据
	 */
	ServiceResult<List<SFCTaskStepInfo>> APS_QuerySFCTaskStepInfoList(BMSEmployee wLoginUser, List<APSTaskStep> wList);

	/**
	 * 提交日计划审批任务
	 */
	ServiceResult<APSDayPlanAuditBPM> APS_SubmitDayPlanAuditBPMTask(BMSEmployee wLoginUser, APSDayPlanAuditBPM wData);

	/**
	 * 查询默认日计划审批单据
	 */
	ServiceResult<APSDayPlanAuditBPM> APS_QueryDefaultDayPlanAuditBPMTask(BMSEmployee wLoginUser, int wEventID);

	/**
	 * 创建默认日计划审批单
	 */
	ServiceResult<APSDayPlanAuditBPM> APS_CreateDayPlanAuditBPMTask(BMSEmployee wLoginUser, BPMEventModule wEventID);

	/**
	 * 根据主键获取日计划审批单据
	 */
	ServiceResult<APSDayPlanAuditBPM> APS_GetDayPlanAuditBPMTask(BMSEmployee wLoginUser, int wID);

	/**
	 * 查询日计划审批历史记录
	 */
	ServiceResult<List<APSDayPlanAuditBPM>> APS_QueryDayPlanAuditBPMHistory(BMSEmployee wLoginUser, int wID,
			String wCode, int wUpFlowID, int wFollowerID, int wAreaID, int wShiftID, Calendar wStartTime,
			Calendar wEndTime);

	/**
	 * 查询日计划审批人员任务
	 */
	ServiceResult<List<BPMTaskBase>> APS_QueryDayPlanAuditBPMEmployeeAll(BMSEmployee wLoginUser, int wTagTypes,
			Calendar wStartTime, Calendar wEndTime);

	/**
	 * 获取日计划审批单子项详情
	 */
	ServiceResult<List<APSTaskStep>> APS_QueryDayPlanAuditItemDetails(BMSEmployee wLoginUser,
			int wAPSDayPlanAuditBPMID);

	/**
	 * 根据工艺路线ID集合获取工艺路线
	 */
	ServiceResult<List<FPCRoute>> ASP_QueryRouteList(BMSEmployee wLoginUser, List<Integer> wRouteIDList,
			Map<Integer, List<FPCRoutePart>> wRouteMap);

	/**
	 * 导出计划
	 */
	ServiceResult<String> Export(BMSEmployee wLoginUser, String wVersionNo, String wSuffix, int wTaskPartID,
			int wAPSShiftPeriod);

	/**
	 * 查询周计划或月计划记录
	 */
	ServiceResult<List<APSTaskPart>> APS_QueryTaskPartList(BMSEmployee wLoginUser, int wOrderID, Calendar wStartTime,
			Calendar wEndTime, int wAPSShiftPeriod);

	/**
	 * 获取周/月计划审批默认单据
	 */
	ServiceResult<APSSchedulingVersionBPM> APS_QueryDefaultSchedulingVersionBPM(BMSEmployee wLoginUser, int wEventID);

	/**
	 * 创建周/月计划审批单
	 */
	ServiceResult<APSSchedulingVersionBPM> APS_CreateSchedulingVersionBPM(BMSEmployee wLoginUser,
			BPMEventModule wEventID);

	/**
	 * 提交月/周计划审批单
	 */
	ServiceResult<APSSchedulingVersionBPM> APS_SubmitSchedulingVersionBPM(BMSEmployee wLoginUser,
			APSSchedulingVersionBPM wData);

	/**
	 * 根据主键获取周/月计划审批单
	 */
	ServiceResult<APSSchedulingVersionBPM> APS_GetSchedulingVersionBPM(BMSEmployee wLoginUser, int wID);

	/**
	 * 查询历史记录周/月计划审批
	 */
	ServiceResult<List<APSSchedulingVersionBPM>> APS_QuerySchedulingVersionBPMHistory(BMSEmployee wLoginUser, int wID,
			String wCode, int wUpFlowID, int wFollowerID, int wAPSShiftPeriod, Calendar wStartTime, Calendar wEndTime);

	/**
	 * 人员查询周/月计划审批
	 */
	ServiceResult<List<BPMTaskBase>> APS_QuerySchedulingVersionBPMEmployeeAll(BMSEmployee wLoginUser,
			int wAPSShiftPeriod, int wTagTypes, Calendar wStartTime, Calendar wEndTime);

	/**
	 * 查看审批单中计划详情
	 */
	ServiceResult<List<APSTaskPart>> APS_QuerySchedulingVersionItemDetails(BMSEmployee wLoginUser,
			int wAPSSchedulingVersionBPMID);

	/**
	 * 查询单条日计划审批单
	 */
	ServiceResult<APSDayPlanAudit> APS_QueryDayPlanAudit(BMSEmployee wLoginUser, int wID);

//	/**
//	 * 车号分类日计划查询
//	 */
//	ServiceResult<List<APSPartNoDetails>> APS_QueryPartNoDetails(BMSEmployee wLoginUser, Calendar wStartTime,
//			Calendar wEndTime);

	/**
	 * 工区分类日计划查询
	 */
//	ServiceResult<List<APSWorkAreaDetails>> APS_QueryAreaDetails(BMSEmployee wLoginUser, Calendar wStartTime,
//			Calendar wEndTime, int wOrderID);

	/**
	 * 判断登陆者是否是工区主管
	 * 
	 * @param wLoginUser
	 * @return
	 */
	ServiceResult<Integer> APS_CheckIsAuditor(BMSEmployee wLoginUser);

	/**
	 * 重置工序任务到派工
	 */
	ServiceResult<Integer> APS_ReSetTaskStep(BMSEmployee wLoginUser, int wAPSTaskStepID);

	/**
	 * 批量进厂确认
	 */
	ServiceResult<Integer> OMS_BatchInPlantConfirm(BMSEmployee wLoginUser, List<OMSOrder> wDataList);

	/**
	 * 工时调整工位任务数据源
	 */
	ServiceResult<List<APSTaskPart>> APS_QueryWorkHourList(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime, int wOrderID);

	/**
	 * 删除重复的日计划
	 */
	ServiceResult<Integer> APS_Delete(BMSEmployee wLoginUser, int wOrderID, int wPartID);

	/**
	 * 获取派工工位信息
	 */
	ServiceResult<List<SFCTaskStepInfo>> APS_GetCarStationList(BMSEmployee wLoginUser, Calendar wTaskDate,
			int wWorkAreaID);

	/**
	 * 根据订单、工位、车号获取工序任务集合
	 */
	ServiceResult<List<APSTaskStep>> APS_QueryTaskStepListByOrder(BMSEmployee wLoginUser, Calendar wTaskDate,
			int wOrderID, int wPartID, String wPartNo);

	/**
	 * 多辆车查询周计划或月计划记录
	 */
	ServiceResult<List<APSTaskPart>> APS_QueryMoreTaskPartList(BMSEmployee wLoginUser, List<Integer> wOrderIDList,
			Calendar wStartTime, Calendar wEndTime, int wAPSShiftPeriod);

	/**
	 * 获取工序日计划信息
	 */
	ServiceResult<List<APSTaskStepInfo>> APS_QueryTaskStepInfo(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime, List<Integer> wClassIDList, List<Integer> wPartIDList, List<Integer> wStatusList,
			int wIsDispathed);

	/**
	 * 获取班组列表
	 */
	ServiceResult<List<BMSDepartment>> APS_QueryClassList(BMSEmployee wLoginUser);

	/**
	 * 获取工位列表
	 */
	ServiceResult<List<FPCPart>> APS_QueryStationList(BMSEmployee wLoginUser);

	/**
	 * 获取工序任务详情(三检进度)
	 */
	ServiceResult<List<APSTaskStepDetails>> APS_QueryTransInfo(BMSEmployee wLoginUser, int wAPSTaskPartID);

	/**
	 * 生成日计划
	 */
	ServiceResult<List<APSTaskStep>> APS_CreateTaskStepByShiftDateNew(BMSEmployee wLoginUser, Calendar wShiftDate);

	/**
	 * 获取车号排序的日计划
	 */
	ServiceResult<List<APSPartNoDetails>> APS_QueryPartNoDetailsNew(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime, int wWorkAreaID);

	/**
	 * 获取工区分类的日计划
	 */
	ServiceResult<List<APSWorkAreaDetails>> APS_QueryAreaDetailsNew(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime, int wOrderID, int wWorkAreaID);

	/**
	 * 日计划的详情
	 * 
	 * @param wOrderID
	 * @param wAreaID
	 */
	ServiceResult<List<APSTaskStep>> APS_QueryTaskStepByTimeNew(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime, int wAreaID, int wOrderID);

	/**
	 * 获取台位车辆状态
	 */
	ServiceResult<List<FMCWorkspace>> APS_QueryPartNoStatusNew(BMSEmployee wLoginUser, List<FMCWorkspace> wList);

	/**
	 * 触发日计划
	 */
	void APS_TriggerDayPlans(BMSEmployee adminUser);

	/**
	 * 根据订单和工位获取工序任务集合
	 */
	ServiceResult<List<APSTaskStep>> APS_QueryOrderTimeInfoList(BMSEmployee wLoginUser, int wOrderID,
			String wStationName);

	void APS_ReMakingDayPlan(BMSEmployee adminUser);

	ServiceResult<List<APSBOMItem>> APS_QueryBOMItemList(BMSEmployee wLoginUser, int wOrderID, String wWBSNo,
			String wPartNo, int wLineID, int wProductID, int wCustomerID, int wPartID, int wPartPointID,
			int wMaterialID, String wMaterialNo, int wBOMType, int wReplaceType, int wOutsourceType,
			List<Integer> wStatus, int wDifferenceItem, int wOverQuota, int wSourceType);

	ServiceResult<APSBOMItem> APS_QueryBOMItemByID(BMSEmployee wLoginUser, int wID);

	ServiceResult<Integer> APS_UpdateBOMItem(BMSEmployee wLoginUser, APSBOMItem wBOMItem);

	ServiceResult<List<APSBOMItem>> APS_CreateBOMItem(BMSEmployee wLoginUser, int wRouteID,
			List<APSBOMItem> wBOMItemList);

	ServiceResult<List<APSBOMItem>> APS_CreateBOMItem(BMSEmployee wLoginUser, int wRouteID, int wLineID,

			int wProductID, int wCustomerID, int wOrderID, String wWBSNo, String wPartNo);

	ServiceResult<Integer> APS_DeleteBOMItem(BMSEmployee wLoginUser, APSBOMItem wBOMItem);

	/**
	 * 根据订单创建台位Bom，并推送到SAP
	 */
	ServiceResult<Integer> APS_CreateBomItemByOrder(BMSEmployee wLoginUser, int wOrderID);

	/**
	 * 获取订单(提示有无台车BOM)
	 */
	ServiceResult<List<OMSOrder>> APS_QueryOrderBOMList(BMSEmployee wLoginUser, List<Integer> wCustomerList,
			List<Integer> wLineList, List<Integer> wStatusList, List<Integer> wProductList, String wPartNo,
			List<Integer> wActiveList, Calendar wStartTime, Calendar wEndTime);

//	/**
//	 * 自动创建台车BOM
//	 */
//	void APS_AutoCreateBOM(BMSEmployee adminUser);

	/**
	 * 自动更新台车BOM(推送SAP)
	 */
	ServiceResult<Integer> APS_UpdateBomItemByBOMItem(BMSEmployee wLoginUser, int wOrderID);

//	/**
//	 * 自动更新台车BOM(推送SAP)
//	 */
//	void APS_AutoUpdateBOM(BMSEmployee adminUser);
//
//	/**
//	 * 自动更新台车订单(推送SAP)
//	 */
//	void APS_AutoUpdateOrder(BMSEmployee adminUser);

	/**
	 * 自动生成竣工确认单
	 */
	void APS_AutoGenerateConfirmForm(BMSEmployee adminUser);

	/**
	 * 自动完成工位任务
	 */
	void APS_AutoFinishTaskPart(BMSEmployee adminUser);

	/**
	 * 获取订单时间
	 */
	List<AndonTime> ASP_QueryAndonTimeList(BMSEmployee wLoginUser, List<Integer> wOrderIDList);

	/**
	 * 查询需要删除的台车BOM集合
	 */
	ServiceResult<List<APSBOMItem>> APS_QueryQueryDeleteBomItemList(BMSEmployee wLoginUser, int wOrderID, int wPartID,
			String wMaterialNos);

	/**
	 * 手动推送偶换件的台车bom到mes和SAP
	 */
	ServiceResult<Integer> APS_BOMTaskToSAP(BMSEmployee wLoginUser, int wBOMTaskID);

	/**
	 * 日计划查询
	 * 
	 * @param wAreaID
	 */
	ServiceResult<List<APSDayPlanAuditBPM>> APS_QueryDayPlanAuditBPMEmployeeAllWeb(BMSEmployee wLoginUser,
			Calendar wStartTime, Calendar wEndTime, int wStatus, int wAreaID);

	/**
	 * 手动推送标准BOM(新增的)，新增台车BOM到SAP
	 */
	ServiceResult<String> APS_SendMoreToSAP(BMSEmployee wLoginUser, List<MSSBOMItem> wDataList);

	ServiceResult<String> IPT_ImportAPSBOM(BMSEmployee wLoginUser, ExcelData result, String wOriginalFileName,
			int wOrderID);

	/**
	 * 属性变更
	 */
	ServiceResult<Integer> APS_BOMItemUpdateProperty(BMSEmployee wBMSEmployee, APSBOMItem wBOMItem);

	/**
	 * 竣工确认
	 */
	ServiceResult<Integer> APS_CompleteConfirm_V2(BMSEmployee wLoginUser, int wOrderID, List<String> wImagePathList,
			String wRemark);

	/**
	 * 批量保存台车bom到系统和erp
	 */
	ServiceResult<Integer> APS_UpdateBOMItemList(BMSEmployee wLoginUser, List<APSBOMItem> wDataList, int wSFCBOMTaskID);

	/**
	 * 通过年、月查询月计划
	 */
	ServiceResult<List<APSTaskPart>> APS_QueryMonthPlanByMonth(BMSEmployee wLoginUser, int wYear, int wMonth);

	ServiceResult<List<APSTaskPart>> APS_QueryWeekPlanByWeek(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime);

	ServiceResult<Integer> APS_SendToSAPPro(BMSEmployee wLoginUser, int wOrderID, int wSFCBOMTaskID);

	ServiceResult<Integer> APS_ChangeLogAddToSAP(BMSEmployee wLoginUser, int wLogID);

	ServiceResult<List<MCSLogInfo>> MCS_QueryLogList(BMSEmployee wLoginUser, Calendar wStartTime, Calendar wEndTime);

	ServiceResult<List<APSSchedulingVersionBPM>> APS_QuerySchedulingVersionBPMEmployeeAllWeb(BMSEmployee wLoginUser,
			int wAPSShiftPeriod, int wStatus, Calendar wStartTime, Calendar wEndTime);

	ServiceResult<String> APS_CheckSchedulingVersionBPM(BMSEmployee wLoginUser, APSSchedulingVersionBPM wTask);

	ServiceResult<List<APSBOMLog>> APS_QueryBOMLogList(BMSEmployee wLoginUser, int wOrderID, int wLineID,
			int wProductID, int wCustomerID, int wStatus, Calendar wStartTime, Calendar wEndTime);

	ServiceResult<APSBOMLog> APS_QueryBOMLogByID(BMSEmployee wBMSEmployee, int wID);

	ServiceResult<APSManuCapacityStep> APS_QueryManuCapacityStep(BMSEmployee wLoginUser, int wID);

	ServiceResult<List<APSManuCapacityStep>> APS_QueryManuCapacityStepList(BMSEmployee wLoginUser, int wLineID,
			int wPartID, int wStatus, int wActive);

	ServiceResult<Integer> APS_UpdateManuCapacityStep(BMSEmployee wLoginUser, APSManuCapacityStep wCapacity);

	void APS_ActiveManuCapacityStepList(BMSEmployee wLoginUser, List<Integer> wIDList, int wActive);

	ServiceResult<List<APSTaskPart>> APS_AutoSchedulingStep(BMSEmployee wLoginUser, int wOrderID,
			APSShiftPeriod wShiftPeriod, Calendar wStartTime, Calendar wEndTime, int wWorkDay, int wLimitMinutes,
			List<APSTaskPart> wMutualTaskList, int wPartID);

	ServiceResult<Integer> APS_BomItemPropertyChange(BMSEmployee wLoginUser, int wID, String wDeleteID, double wNumber);

	ServiceResult<Integer> APS_AssessTypeChange(BMSEmployee wLoginUser);

	ServiceResult<List<APSTaskStepPlan>> APS_QueryStepPlanList(BMSEmployee wLoginUser, String wOrderIDs,
			String wPartIDs, int wAPSShiftPeriod);

	ServiceResult<Integer> APS_AddAPSBOMToSAP(BMSEmployee wLoginUser, int wBOMID, int wOrderID);

	ServiceResult<Integer> APS_SaveStepPlan(BMSEmployee wLoginUser, List<APSTaskPart> wStepPlanList);

	ServiceResult<List<FPCRoute>> ASP_QueryRouteList(BMSEmployee wLoginUser, List<Integer> wRouteIDList, int wPartID);

	void APS_CancelDeleteX(BMSEmployee wLoginUser, int i);

	void SendSingleMaterial(BMSEmployee wLoginUser, int wBOMID, List<Integer> wBOMItemIDList,
			List<Integer> wOrderIDList);

	ServiceResult<String> APS_DeleteListByImport(BMSEmployee wLoginUser, ExcelData result, int wOrderID);

	void APS_ChangeNumber(BMSEmployee wLoginUser, int wOrderID, int wAPSBOMID);

	ServiceResult<List<APSTaskPart>> APS_StepPlanMore(BMSEmployee wLoginUser, List<APSStepPlanParam> wList);

	ServiceResult<Integer> APS_QueryBOMID(BMSEmployee wLoginUser, int wSourceID, int wType, String wCustomerCode);

	ServiceResult<List<APSTaskStepPlan>> APS_QueryStepPlanCompare(BMSEmployee wLoginUser, String wOrderIDs,
			String wPartIDs, int wAPSShiftPeriod);

	ServiceResult<Integer> APS_SendSingleMaterial(BMSEmployee wLoginUser, int wBOMID, String wOrders, String wItemIDs);

	ServiceResult<Integer> MCS_QueryIDByBOMID(BMSEmployee wLoginUser, int wBOMID);

	ServiceResult<Integer> MCS_QueryIDByBOPID(BMSEmployee wLoginUser, int wBOPID);

	ServiceResult<MCSLogInfo> MCS_QueryLogInfo(BMSEmployee wLoginUser, int wID);

	ServiceResult<Integer> APS_ReSendBOMByIDList(BMSEmployee wLoginUser, List<Integer> wIDList);

	ServiceResult<Integer> OMS_SynchronizePartNo(BMSEmployee wLoginUser, int wOrderID);

	/**
	 * 同步工序计划
	 */
	ServiceResult<Integer> APS_SynchronizedStepPlan(BMSEmployee wLoginUser, int wPartID, String wOrders);

	ServiceResult<List<BMSWorkChargeItem>> BMS_QueryWorkChargeItemList(BMSEmployee wLoginUser, int wWorkChargeID);

	ServiceResult<Integer> BMS_DeleteWorkChargeItemList(BMSEmployee wLoginUser, List<BMSWorkChargeItem> wList);

	ServiceResult<Integer> BMS_SaveWorkChargeItemList(BMSEmployee wLoginUser, List<BMSWorkChargeItem> wList);

	ServiceResult<List<APSTaskStepCancelLog>> APS_QueryTaskStepCancelLogList(BMSEmployee wLoginUser, int wOrderID,
			int wPartID, Calendar wStartTime, Calendar wEndTime);

	ServiceResult<Integer> APS_SaveStepCancelLog(BMSEmployee wLoginUser, List<APSTaskStep> wList, int wCancelType,
			String wCancelTypeName, String wRemark);

	ServiceResult<APSTaskStepCancelLog> APS_QueryTaskStepCancelLogInfo(BMSEmployee wLoginUser, int wID);

	ServiceResult<Integer> APS_CancelTaskStepPlan(BMSEmployee wLoginUser, List<APSTaskStep> wDataList);

	ServiceResult<Integer> APS_ActiveTaskPart(BMSEmployee wLoginUser, int wAPSSchedulingVersionBPMID);

	/**
	 * 查询默认状态的单据
	 */
	ServiceResult<APSBomBPM> APS_QueryDefaultBomBPM(BMSEmployee wLoginUser, int wEventID);

	/**
	 * 创建单据
	 */
	ServiceResult<APSBomBPM> APS_CreateBomBPM(BMSEmployee wLoginUser, BPMEventModule wEventID);

	/**
	 * 提交单据
	 */
	ServiceResult<APSBomBPM> APS_SubmitBomBPM(BMSEmployee wLoginUser, APSBomBPM wData);

	/**
	 * 查询单条单据
	 */
	ServiceResult<APSBomBPM> APS_GetBomBPM(BMSEmployee wLoginUser, int wID);

	/**
	 * 用人员拿任务
	 */
	ServiceResult<List<BPMTaskBase>> APS_QueryBomBPMEmployeeAll(BMSEmployee wLoginUser, int wTagTypes,
			Calendar wStartTime, Calendar wEndTime);

	/**
	 * 查询单据历史
	 */
	ServiceResult<List<APSBomBPM>> APS_QueryBomBPMHistory(BMSEmployee wLoginUser, int wID, String wCode, int wUpFlowID,
			int wOrderID, int wLineID, int wProductID, int wCustomerID, int wType, Calendar wStartTime,
			Calendar wEndTime);

	ServiceResult<List<APSBomBPM>> APS_QueryBomBPMEmployeeAllWeb(BMSEmployee wLoginUser, String wCode, int wUpFlowID,
			int wOrderID, int wLineID, int wProductID, int wCustomerID, int wType, int wStatus, Calendar wStartTime,
			Calendar wEndTime, String wMaterialName, String wMaterialNo);

	/**
	 * 自动同步MES的台车bom必换件、委外必修件到ERP
	 */
	ServiceResult<Integer> APS_AutoSynchronizedBOM(BMSEmployee wLoginUser);

	/**
	 * 晚上2点定时处理台车BOM
	 */
	void APS_HandleBOMOnTime();

	/**
	 * 查询默认状态的单据
	 */
	ServiceResult<APSTaskStepCancelLogBPM> APS_QueryDefaultTaskStepCancelLogBPM(BMSEmployee wLoginUser, int wEventID);

	/**
	 * 创建单据
	 */
	ServiceResult<APSTaskStepCancelLogBPM> APS_CreateTaskStepCancelLogBPM(BMSEmployee wLoginUser,
			BPMEventModule wEventID);

	/**
	 * 提交单据
	 */
	ServiceResult<APSTaskStepCancelLogBPM> APS_SubmitTaskStepCancelLogBPM(BMSEmployee wLoginUser,
			APSTaskStepCancelLogBPM wData);

	/**
	 * 查询单条单据
	 */
	ServiceResult<APSTaskStepCancelLogBPM> APS_GetTaskStepCancelLogBPM(BMSEmployee wLoginUser, int wID);

	/**
	 * 用人员拿任务
	 */
	ServiceResult<List<BPMTaskBase>> APS_QueryTaskStepCancelLogBPMEmployeeAll(BMSEmployee wLoginUser, int wTagTypes,
			Calendar wStartTime, Calendar wEndTime);

	/**
	 * 查询单据历史
	 */
	ServiceResult<List<APSTaskStepCancelLogBPM>> APS_QueryTaskStepCancelLogBPMHistory(BMSEmployee wLoginUser, int wID,
			String wCode, int wUpFlowID, int wOrderID, int wPartID, Calendar wStartTime, Calendar wEndTime);

	ServiceResult<List<APSTaskStepCancelLogBPM>> APS_QueryTaskStepCancelLogBPMEmployeeAllWeb(BMSEmployee wLoginUser,
			String wCode, int wUpFlowID, int wOrderID, int wPartID, int wStatus, Calendar wStartTime, Calendar wEndTime,
			int wCancelType);

	void APS_BOMTaskToSAP(BMSEmployee wLoginUser, List<SFCBOMTaskItem> wDataList);

	/**
	 * 根据ID查询
	 * 
	 * @return
	 */
	ServiceResult<APSTriggerPart> APS_QueryTriggerPart(BMSEmployee wLoginUser, int wID);

	/**
	 * 条件查询
	 * 
	 * @return
	 */
	ServiceResult<List<APSTriggerPart>> APS_QueryTriggerPartList(BMSEmployee wLoginUser, int wID, int wProductID,
			int wLineID, int wCustomerID, int wActive);

	/**
	 * 新增或修改
	 * 
	 * @return
	 */
	ServiceResult<Integer> APS_UpdateTriggerPart(BMSEmployee wLoginUser, APSTriggerPart wAPSTriggerPart);

	/**
	 * 激活或禁用
	 * 
	 * @return
	 */
	ServiceResult<Integer> APS_ActiveTriggerPartList(BMSEmployee wLoginUser, List<Integer> wIDList, int wActive);

	/**
	 * 触发日计划
	 */
	void APS_TriggerDayPlans(BMSEmployee wAdminUser, APSSchedulingVersion wAPSSchedulingVersion);

	/**
	 * 工位找班组长
	 */
	ServiceResult<List<BMSEmployee>> APS_QueryUserByParts(BMSEmployee wLoginUser, String wPartIDs);

	/**
	 * 查询配料需求计划
	 */
	ServiceResult<List<MRPMaterialPlan>> MRP_QueryMaterialPlanList(BMSEmployee wLoginUser, int wProductID, int wLineID,
			int wCustomerID, int wOrderID, int wPartID, int wStepID, String wMaterial, int wMaterialType,
			int wReplaceType, int wOutSourceType, Calendar wStartTime, Calendar wEndTime);

	/**
	 * 手动触发物料配送单
	 */
	ServiceResult<Integer> MRP_HandTrigger(BMSEmployee wLoginUser, int wOrderID, int wPartID);

	/**
	 * 自动将物料需求计划生成物料配送单
	 */
	void APS_AutoCreateDeliveryOrder(BMSEmployee adminUser);

	/**
	 * 自动推送物料配送单至WMS
	 */
	void APS_AutoSendDemandList(BMSEmployee adminUser);
}
