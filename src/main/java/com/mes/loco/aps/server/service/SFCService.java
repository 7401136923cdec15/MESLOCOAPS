package com.mes.loco.aps.server.service;

import com.mes.loco.aps.server.service.mesenum.BPMEventModule;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.aps.APSTaskStep;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.bpm.BPMTaskBase;
import com.mes.loco.aps.server.service.po.cfg.CFGItem;
import com.mes.loco.aps.server.service.po.fpc.FPCPart;
import com.mes.loco.aps.server.service.po.ipt.IPTPreCheckProblem;
import com.mes.loco.aps.server.service.po.oms.OMSOrder;
import com.mes.loco.aps.server.service.po.sfc.SFCAttemptRun;
import com.mes.loco.aps.server.service.po.sfc.SFCBOMTask;
import com.mes.loco.aps.server.service.po.sfc.SFCBOMTaskItem;
import com.mes.loco.aps.server.service.po.sfc.SFCOrderForm;
import com.mes.loco.aps.server.service.po.sfc.SFCSequentialInfo;
import com.mes.loco.aps.server.service.po.sfc.SFCSequentialInfoNew;
import com.mes.loco.aps.server.service.po.sfc.SFCStationPerson;
import com.mes.loco.aps.server.service.po.sfc.SFCTaskStep;
import com.mes.loco.aps.server.service.po.sfc.SFCTaskStepCar;
import com.mes.loco.aps.server.service.po.sfc.SFCTaskStepPart;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public interface SFCService {
	ServiceResult<SFCTaskStep> SFC_QueryTaskStep(BMSEmployee paramBMSEmployee, int paramInt);

	ServiceResult<List<SFCTaskStep>> SFC_QueryTaskStepList(BMSEmployee paramBMSEmployee, int paramInt1, int paramInt2,
			int paramInt3, int paramInt4, int paramInt5);

	ServiceResult<Integer> SFC_UpdateTaskStep(BMSEmployee paramBMSEmployee, SFCTaskStep paramSFCTaskStep);

	ServiceResult<List<BMSEmployee>> SFC_QueryDispatchEmployeeList(BMSEmployee paramBMSEmployee, boolean paramBoolean,
			List<Integer> paramList);

	ServiceResult<List<SFCTaskStep>> SFC_QueryTaskStepListByEmployeeID(BMSEmployee paramBMSEmployee,
			Calendar wStartTime, Calendar wEndTime, int wTagTypes);

	ServiceResult<List<SFCTaskStep>> SFC_QueryTaskStepListByMonitorID(BMSEmployee paramBMSEmployee,
			Calendar paramCalendar);

	ServiceResult<Integer> SFC_AdjustHour(BMSEmployee paramBMSEmployee, List<SFCTaskStep> paramList,
			Double paramDouble);

	ServiceResult<String> SFC_CheckPGPower(BMSEmployee paramBMSEmployee, List<Integer> paramList);

	ServiceResult<Map<Integer, List<Integer>>> SFC_QueryStepPersonMap(BMSEmployee paramBMSEmployee,
			List<APSTaskStep> paramList);

	ServiceResult<List<BMSEmployee>> SFC_QueryPGEmployeeList(BMSEmployee paramBMSEmployee, int paramInt);

	ServiceResult<Integer> SFC_TaskStepSubmitAll(BMSEmployee paramBMSEmployee, List<APSTaskStep> paramList);

	ServiceResult<Integer> SFC_TaskStepSaveAll(BMSEmployee paramBMSEmployee, List<APSTaskStep> paramList);

	ServiceResult<Integer> SFC_SubmitPGProblems(BMSEmployee paramBMSEmployee, List<IPTPreCheckProblem> paramList);

	ServiceResult<SFCOrderForm> SFC_QueryOrderForm(BMSEmployee paramBMSEmployee, int paramInt);

	ServiceResult<List<SFCOrderForm>> SFC_QueryCompleteConfirmList(BMSEmployee paramBMSEmployee);

	ServiceResult<Integer> SFC_CompleteConfirm(BMSEmployee paramBMSEmployee, int paramInt, List<String> paramList,
			String paramString);

	ServiceResult<SFCOrderForm> SFC_QueryOrderForm(BMSEmployee paramBMSEmployee, int paramInt1, int paramInt2);

	ServiceResult<List<SFCSequentialInfo>> SFC_QuerySequentialInfo(BMSEmployee paramBMSEmployee, int paramInt);

	ServiceResult<List<SFCTaskStep>> SFC_QueryTaskStepList(BMSEmployee paramBMSEmployee, List<Integer> paramList);

	ServiceResult<Integer> SFC_DeleteList(BMSEmployee wLoginUser, List<SFCTaskStep> wList);

	/**
	 * ????????????????????????????????????????????????
	 */
	ServiceResult<List<SFCBOMTask>> SFC_QueryBOMTaskHistory(BMSEmployee wLoginUser, int wID, String wCode,
			int wUpFlowID, int wFollowerID, int wOrderID, int wPartID, int wPartPointID, int wBOMID, int wLevel,
			Calendar wStartTime, Calendar wEndTime);

	/**
	 * ????????????????????????????????????
	 */
	ServiceResult<List<BPMTaskBase>> SFC_QueryBOMTaskEmployeeAll(BMSEmployee wLoginUser, int wTagTypes,
			Calendar wStartTime, Calendar wEndTime);

	/**
	 * ????????????????????????????????????
	 */
	ServiceResult<SFCBOMTask> SFC_SubmitBOMTask(BMSEmployee wLoginUser, SFCBOMTask wTask);

	/**
	 * ????????????????????????0?????????????????????????????????
	 */
	ServiceResult<SFCBOMTask> SFC_QueryDefaultBOMTask(BMSEmployee wLoginUser, int value);

	/**
	 * ?????????????????????????????????????????????
	 */
	ServiceResult<SFCBOMTask> SFC_CreateBOMTask(BMSEmployee wLoginUser, BPMEventModule wEventID);

	/**
	 * ???????????????????????????????????????
	 */
	ServiceResult<SFCBOMTask> SFC_GetBOMTask(BMSEmployee wLoginUser, int wID);

	/**
	 * ????????????ID??????????????????
	 */
	ServiceResult<SFCStationPerson> SFC_QueryStationPerson(BMSEmployee wLoginUser, int wStationID);

	/**
	 * ?????????????????????????????????
	 */
	ServiceResult<Integer> SFC_AddPerson(BMSEmployee wLoginUser, APSTaskStep wData, List<Integer> wPersonIDList);

	/**
	 * ???????????????????????????
	 */
	ServiceResult<SFCAttemptRun> SFC_QueryDefaultAttemptRun(BMSEmployee wLoginUser, int wEventID);

	/**
	 * ????????????
	 */
	ServiceResult<SFCAttemptRun> SFC_CreateAttemptRun(BMSEmployee wLoginUser, BPMEventModule wEventID);

	/**
	 * ????????????
	 */
	ServiceResult<SFCAttemptRun> SFC_SubmitAttemptRun(BMSEmployee wLoginUser, SFCAttemptRun wData);

	/**
	 * ??????????????????
	 */
	ServiceResult<SFCAttemptRun> SFC_GetAttemptRun(BMSEmployee wLoginUser, int wID);

	/**
	 * ??????????????????
	 */
	ServiceResult<List<BPMTaskBase>> SFC_QueryAttemptRunEmployeeAll(BMSEmployee wLoginUser, int wTagTypes,
			Calendar wStartTime, Calendar wEndTime);

	/**
	 * ??????????????????
	 */
	ServiceResult<List<SFCAttemptRun>> SFC_QueryAttemptRunHistory(BMSEmployee wLoginUser, int wID, String wCode,
			int wUpFlowID, int wFollowerID, int wOrderID, int wCheckerID, int wPartID, Calendar wStartTime,
			Calendar wEndTime);

	/**
	 * ????????????
	 */
	ServiceResult<Integer> SFC_CloseMessage(BMSEmployee wLoginUser, int wID, BPMEventModule wEventID);

	/**
	 * ?????????????????????????????????????????????????????????
	 */
	ServiceResult<Boolean> SFC_JugdeItem(BMSEmployee wLoginUser, int wOrderID, int wSFCAttemptRunID);

	/**
	 * ?????????????????????????????????
	 */
	ServiceResult<Map<Integer, Integer>> SFC_JudgeAll(BMSEmployee wLoginUser, SFCAttemptRun wData);

	/**
	 * ????????????????????????????????????????????????
	 */
	ServiceResult<Boolean> SFC_JudgeIsSendApply(BMSEmployee wLoginUser, SFCAttemptRun wSFCAttemptRun);

	/**
	 * ????????????????????????(?????????)
	 */
	ServiceResult<Integer> SFC_TaskStepSubmitAllNew(BMSEmployee wLoginUser, List<APSTaskStep> wAPSTaskStepList);

	/**
	 * ????????????????????????(?????????)
	 */
	ServiceResult<List<SFCSequentialInfo>> SFC_QuerySequentialInfoNew(BMSEmployee wLoginUser, int wOrderID,
			String wUUID);

	/**
	 * ???????????????????????????
	 */
	ServiceResult<Integer> SFC_DeleteRepeat(BMSEmployee wLoginUser);

	/**
	 * ????????????????????????
	 */
	ServiceResult<Integer> SFC_TaskStepSubmitAllNew_v2(BMSEmployee wLoginUser, List<APSTaskStep> wAPSTaskStepList);

	ServiceResult<List<SFCBOMTask>> SFC_QueryBOMTaskEmployeeAllNew(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime, int wOrderID, int wPartID, int wLevel, int wStatus);

	ServiceResult<List<SFCBOMTask>> SFC_QueryBOMTaskList(BMSEmployee wLoginUser, int wOrderID, int wPartID, int wLevel,
			int wStatus, Calendar wStartTime, Calendar wEndTime, String wMaterialNo, String wMaterialName, int wIsLGL,
			int wIsQualityLoss);

	ServiceResult<List<SFCAttemptRun>> SFC_QueryAttemptRunEmployeeAllNew(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime, int wProductID, String wPartNo, int wStatus);

	ServiceResult<List<SFCAttemptRun>> SFC_QueryAttemptRunList(BMSEmployee wLoginUser, int wOrderID, int wLineID,
			int wCustomerID, int wProductID, String wPartNo, int wStatus, Calendar wStartTime, Calendar wEndTime);

	/**
	 * ?????????????????????????????????
	 */
	ServiceResult<List<FPCPart>> SFC_QueryLoginStationList(BMSEmployee wLoginUser);

	ServiceResult<List<BMSEmployee>> SFC_QueryPartCheckerList(BMSEmployee wLoginUser, int wPartID);

	ServiceResult<List<BMSEmployee>> SFC_QueryTechnicianList(BMSEmployee wLoginUser);

	ServiceResult<List<CFGItem>> SFC_QueryModuleList(BMSEmployee wLoginUser);

	ServiceResult<String> SFC_ExportBOMTaskList(BMSEmployee wLoginUser, Calendar wDate);

	ServiceResult<List<SFCSequentialInfoNew>> SFC_QuerySequentialInfoNew_V2(BMSEmployee wLoginUser, int wOrderID);

	ServiceResult<String> SFC_IsOverEqua(BMSEmployee wLoginUser, SFCBOMTask wTask);

	ServiceResult<SFCBOMTask> SFC_UpdateBOMTask(BMSEmployee wLoginUser, SFCBOMTask wData);

	ServiceResult<Integer> SFC_UpdateQuota(BMSEmployee wLoginUser);

	ServiceResult<List<OMSOrder>> SFC_QueryOrderListByLogID(BMSEmployee wLoginUser, int wLogID);

	/**
	 * ??????????????????
	 */
	ServiceResult<Integer> SFC_UpdateAssessType(BMSEmployee wLoginUser, List<SFCBOMTaskItem> wDataList);

	ServiceResult<List<SFCTaskStepCar>> SFC_QueryDispatchCarList(BMSEmployee wLoginUser);

	ServiceResult<List<SFCTaskStepPart>> SFC_QueryDispatchPartList(BMSEmployee wLoginUser, int wOrderID);

	/**
	 * ????????????????????????
	 */
	ServiceResult<String> SFC_ExportBOMTaskListByOrder(BMSEmployee wLoginUser, int wOrderID);

	/**
	 * ???????????????????????????
	 */
	ServiceResult<List<OMSOrder>> SFC_QueryOrderList(BMSEmployee wLoginUser);

	/**
	 * ????????????????????????
	 */
	ServiceResult<Integer> SFC_BOMTaskCodeReset(BMSEmployee wLoginUser);

	ServiceResult<Integer> SFC_UpdateBOMTaskSubmitTime(BMSEmployee wLoginUser);

	ServiceResult<Integer> SFC_OutsourcingProcess(BMSEmployee wLoginUser, int wSFCBomTaskID);
}
