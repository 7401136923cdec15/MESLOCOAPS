package com.mes.loco.aps.server.controller.bpm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mes.loco.aps.server.controller.BaseController;
import com.mes.loco.aps.server.service.APSService;
import com.mes.loco.aps.server.service.BPMService;
import com.mes.loco.aps.server.service.CoreService;
import com.mes.loco.aps.server.service.LFSService;
import com.mes.loco.aps.server.service.SCHService;
import com.mes.loco.aps.server.service.SFCService;
import com.mes.loco.aps.server.service.mesenum.APSBomBPMStatus;
import com.mes.loco.aps.server.service.mesenum.APSTaskStepCancelLogBPMStatus;
import com.mes.loco.aps.server.service.mesenum.BPMEventModule;
import com.mes.loco.aps.server.service.mesenum.BPMHistoryTaskStatus;
import com.mes.loco.aps.server.service.mesenum.SCHSecondmentApplyStatus;
import com.mes.loco.aps.server.service.mesenum.SCHSecondmentBPMStatus;
import com.mes.loco.aps.server.service.mesenum.SFCAttemptRunStatus;
import com.mes.loco.aps.server.service.mesenum.SFCBOMTaskStatus;
import com.mes.loco.aps.server.service.po.APIResult;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.aps.APSBomBPM;
import com.mes.loco.aps.server.service.po.aps.APSDayPlanAuditBPM;
import com.mes.loco.aps.server.service.po.aps.APSSchedulingVersionBPM;
import com.mes.loco.aps.server.service.po.aps.APSTaskStepCancelLogBPM;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.bpm.BPMActivitiHisTask;
import com.mes.loco.aps.server.service.po.bpm.BPMActivitiProcessInstance;
import com.mes.loco.aps.server.service.po.bpm.BPMActivitiTask;
import com.mes.loco.aps.server.service.po.bpm.BPMTaskBase;
import com.mes.loco.aps.server.service.po.sch.SCHSecondmentApply;
import com.mes.loco.aps.server.service.po.sch.SCHSecondmentBPM;
import com.mes.loco.aps.server.service.po.sfc.SFCAttemptRun;
import com.mes.loco.aps.server.service.po.sfc.SFCBOMTask;
import com.mes.loco.aps.server.service.utils.CloneTool;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.utils.RetCode;

@RestController
@RequestMapping("/api/Runtime")
public class BPMRuntimeController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(BPMRuntimeController.class);

	@Autowired
	LFSService wLFSService;
	@Autowired
	APSService wAPSService;
	@Autowired
	BPMService wBPMService;
	@Autowired
	CoreService wCoreService;
	@Autowired
	SFCService wSFCService;
	@Autowired
	SCHService wSCHService;

	/**
	 * ??????????????????
	 * 
	 * @param request
	 * @param wParam
	 * @return
	 */
	@PostMapping("/startProcessByProcessDefinitionKey")
	public Object startProcessByProcessDefinitionKey(HttpServletRequest request,
			@RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}
			BMSEmployee wLoginUser = GetSession(request);

			if (!wParam.containsKey("processDefinitionKey")) {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);
				return wResult;
			}

			String wModuleIDString = StringUtils.parseString(wParam.get("processDefinitionKey"));
			if (wModuleIDString.startsWith("_")) {
				wModuleIDString = wModuleIDString.substring(1);
			}
			int wModuleID = StringUtils.parseInt(wModuleIDString);

			BPMEventModule wEventID = BPMEventModule.getEnumType(wModuleID);

			String wMsg = "";

			BPMTaskBase wData = null;
			@SuppressWarnings("rawtypes")
			ServiceResult wServiceResult = null;
			List<BPMActivitiTask> wBPMActivitiTask = new ArrayList<BPMActivitiTask>();
			switch (wEventID) {
			case SCDayAudit:
				// ????????????????????????????????????
				ServiceResult<Integer> wMsgRst = wAPSService.APS_CheckIsAuditor(wLoginUser);
				if (StringUtils.isNotEmpty(wMsgRst.FaultCode)) {
					return GetResult(RetCode.SERVER_CODE_ERR, wMsgRst.FaultCode);
				}

				// APSDayPlanAudit ???????????????0??????????????????????????????
				// ???????????????(???????????????????????????)
				wServiceResult = wAPSService.APS_QueryDefaultDayPlanAuditBPMTask(wLoginUser, wEventID.getValue());
				if (wServiceResult == null || wServiceResult.GetResult() == null
						|| ((BPMTaskBase) wServiceResult.GetResult()).ID <= 0
						|| ((BPMTaskBase) wServiceResult.GetResult()).FlowID <= 0)
					wServiceResult = wAPSService.APS_CreateDayPlanAuditBPMTask(wLoginUser, wEventID);
				if (StringUtils.isNotEmpty(wServiceResult.FaultCode)) {
					wMsg += wServiceResult.getFaultCode();
				}
				wData = (APSDayPlanAuditBPM) wServiceResult.GetResult();

				if (wParam.containsKey("data")) {
					wData = StringUtils.CombineData(wData, wParam.get("data"));
				}
				if (wData.ID > 0) {
					wData.CreateTime = Calendar.getInstance();
					if (wData.FlowID <= 0) {
						wData.FlowID = wBPMService.BPM_CreateProcess(wLoginUser, wEventID, wData.getID(), wData)
								.Info(Integer.class);
					}
					if (wData.FlowID <= 0) {
						wMsg += "?????????????????????";
					} else {
						wServiceResult = wAPSService.APS_SubmitDayPlanAuditBPMTask(wLoginUser,
								(APSDayPlanAuditBPM) wData);
						if (wServiceResult.ErrorCode != 0) {
							wMsg += wServiceResult.getFaultCode();
						}

						wBPMActivitiTask = wBPMService.BPM_GetTaskListByInstance(wLoginUser, wData.FlowID)
								.List(BPMActivitiTask.class);
					}
				}
				break;
			case OccasionNCR:
				// APSDayPlanAudit ???????????????0??????????????????????????????
				// ???????????????????????????
				if (!wCoreService.BMS_CheckPowerByAuthorityID(wLoginUser.CompanyID, wLoginUser.ID, 990000, 0, 0)
						.Info(Boolean.class)) {
					wResult = GetResult(RetCode.SERVER_CODE_SUC, "?????????", null, null);
					return wResult;
				}
				// ???????????????(???????????????????????????)
				wServiceResult = wSFCService.SFC_QueryDefaultBOMTask(wLoginUser, wEventID.getValue());
				if (wServiceResult == null || wServiceResult.GetResult() == null
						|| ((BPMTaskBase) wServiceResult.GetResult()).ID <= 0
						|| ((BPMTaskBase) wServiceResult.GetResult()).FlowID <= 0)
					wServiceResult = wSFCService.SFC_CreateBOMTask(wLoginUser, wEventID);
				if (StringUtils.isNotEmpty(wServiceResult.FaultCode)) {
					wMsg += wServiceResult.getFaultCode();
				}
				wData = (SFCBOMTask) wServiceResult.GetResult();

				if (wParam.containsKey("data")) {
					wData = StringUtils.CombineData(wData, wParam.get("data"));
				}
				if (wData.ID > 0) {
					wData.CreateTime = Calendar.getInstance();
					if (wData.FlowID <= 0) {
						wData.FlowID = wBPMService.BPM_CreateProcess(wLoginUser, wEventID, wData.getID(), wData)
								.Info(Integer.class);
					}
					if (wData.FlowID <= 0) {
						wMsg += "?????????????????????";
					} else {
						wServiceResult = wSFCService.SFC_SubmitBOMTask(wLoginUser, (SFCBOMTask) wData);
						if (wServiceResult.ErrorCode != 0) {
							wMsg += wServiceResult.getFaultCode();
						}

						wBPMActivitiTask = wBPMService.BPM_GetTaskListByInstance(wLoginUser, wData.FlowID)
								.List(BPMActivitiTask.class);
					}
				}
				break;
			case SCWeekAudit:
				// ???????????????????????????
				if (!wCoreService.BMS_CheckPowerByAuthorityID(wLoginUser.CompanyID, wLoginUser.ID, 81010001, 0, 0)
						.Info(Boolean.class)) {
					return GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_CODE_UNROLE);
				}

				// ???????????????0??????????????????????????????
				// ???????????????(???????????????????????????)
				wServiceResult = wAPSService.APS_QueryDefaultSchedulingVersionBPM(wLoginUser, wEventID.getValue());
				if (wServiceResult == null || wServiceResult.GetResult() == null
						|| ((BPMTaskBase) wServiceResult.GetResult()).ID <= 0
						|| ((BPMTaskBase) wServiceResult.GetResult()).FlowID <= 0)
					wServiceResult = wAPSService.APS_CreateSchedulingVersionBPM(wLoginUser, wEventID);
				if (StringUtils.isNotEmpty(wServiceResult.FaultCode)) {
					wMsg += wServiceResult.getFaultCode();
				}
				wData = (APSSchedulingVersionBPM) wServiceResult.GetResult();

				if (wParam.containsKey("data")) {
					wData = StringUtils.CombineData(wData, wParam.get("data"));
				}
				if (wData.ID > 0) {
					wData.CreateTime = Calendar.getInstance();
					if (wData.FlowID <= 0) {
						wData.FlowID = wBPMService.BPM_CreateProcess(wLoginUser, wEventID, wData.getID(), wData)
								.Info(Integer.class);
					}
					if (wData.FlowID <= 0) {
						wMsg += "?????????????????????";
					} else {
						wServiceResult = wAPSService.APS_SubmitSchedulingVersionBPM(wLoginUser,
								(APSSchedulingVersionBPM) wData);
						if (wServiceResult.ErrorCode != 0) {
							wMsg += wServiceResult.getFaultCode();
						}

						wBPMActivitiTask = wBPMService.BPM_GetTaskListByInstance(wLoginUser, wData.FlowID)
								.List(BPMActivitiTask.class);
					}
				}
				break;
			case SCMonthAudit:
				// ???????????????????????????
				if (!wCoreService.BMS_CheckPowerByAuthorityID(wLoginUser.CompanyID, wLoginUser.ID, 81010001, 0, 0)
						.Info(Boolean.class)) {
					return GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_CODE_UNROLE);
				}
				// ???????????????0??????????????????????????????
				// ???????????????(???????????????????????????)
				wServiceResult = wAPSService.APS_QueryDefaultSchedulingVersionBPM(wLoginUser, wEventID.getValue());
				if (wServiceResult == null || wServiceResult.GetResult() == null
						|| ((BPMTaskBase) wServiceResult.GetResult()).ID <= 0
						|| ((BPMTaskBase) wServiceResult.GetResult()).FlowID <= 0)
					wServiceResult = wAPSService.APS_CreateSchedulingVersionBPM(wLoginUser, wEventID);
				if (StringUtils.isNotEmpty(wServiceResult.FaultCode)) {
					wMsg += wServiceResult.getFaultCode();
				}
				wData = (APSSchedulingVersionBPM) wServiceResult.GetResult();

				if (wParam.containsKey("data")) {
					wData = StringUtils.CombineData(wData, wParam.get("data"));
				}
				if (wData.ID > 0) {
					wData.CreateTime = Calendar.getInstance();
					if (wData.FlowID <= 0) {
						wData.FlowID = wBPMService.BPM_CreateProcess(wLoginUser, wEventID, wData.getID(), wData)
								.Info(Integer.class);
					}
					if (wData.FlowID <= 0) {
						wMsg += "?????????????????????";
					} else {
						wServiceResult = wAPSService.APS_SubmitSchedulingVersionBPM(wLoginUser,
								(APSSchedulingVersionBPM) wData);
						if (wServiceResult.ErrorCode != 0) {
							wMsg += wServiceResult.getFaultCode();
						}

						wBPMActivitiTask = wBPMService.BPM_GetTaskListByInstance(wLoginUser, wData.FlowID)
								.List(BPMActivitiTask.class);
					}
				}
				break;
			case ToLoan:
				// ????????????????????????
				if (!wCoreService.BMS_CheckPowerByAuthorityID(wLoginUser.CompanyID, wLoginUser.ID, 502703, 0, 0)
						.Info(Boolean.class)) {
					wResult = GetResult(RetCode.SERVER_CODE_SUC, "?????????", null, null);
					return wResult;
				}
				// SCHDayPlanAudit ???????????????0??????????????????????????????
				// ???????????????(???????????????????????????)
				wServiceResult = wSCHService.SCH_QueryDefaultSecondementBPM(wLoginUser, wEventID.getValue());
				if (wServiceResult == null || wServiceResult.GetResult() == null
						|| ((BPMTaskBase) wServiceResult.GetResult()).ID <= 0
						|| ((BPMTaskBase) wServiceResult.GetResult()).FlowID <= 0)
					wServiceResult = wSCHService.SCH_CreateSecondementBPM(wLoginUser, wEventID);
				if (StringUtils.isNotEmpty(wServiceResult.FaultCode)) {
					wMsg += wServiceResult.getFaultCode();
				}
				wData = (SCHSecondmentBPM) wServiceResult.GetResult();

				if (wParam.containsKey("data")) {
					wData = StringUtils.CombineData(wData, wParam.get("data"));
				}
				if (wData.ID > 0) {
					wData.CreateTime = Calendar.getInstance();
					if (wData.FlowID <= 0) {
						wData.FlowID = wBPMService.BPM_CreateProcess(wLoginUser, wEventID, wData.getID(), wData)
								.Info(Integer.class);
					}
					if (wData.FlowID <= 0) {
						wMsg += "?????????????????????";
					} else {
						wServiceResult = wSCHService.SCH_SubmitSecondementBPM(wLoginUser, (SCHSecondmentBPM) wData);
						if (wServiceResult.ErrorCode != 0) {
							wMsg += wServiceResult.getFaultCode();
						}

						wBPMActivitiTask = wBPMService.BPM_GetTaskListByInstance(wLoginUser, wData.FlowID)
								.List(BPMActivitiTask.class);
					}
				}
				break;
			case AttemptRun:
				// ??????????????????????????????
				if (!wCoreService.BMS_CheckPowerByAuthorityID(wLoginUser.CompanyID, wLoginUser.ID, 990002, 0, 0)
						.Info(Boolean.class)) {
					wResult = GetResult(RetCode.SERVER_CODE_SUC, "?????????", null, null);
					return wResult;
				}
				// SFCDayPlanAudit ???????????????0??????????????????????????????
				// ???????????????(???????????????????????????)
				wServiceResult = wSFCService.SFC_QueryDefaultAttemptRun(wLoginUser, wEventID.getValue());
				if (wServiceResult == null || wServiceResult.GetResult() == null
						|| ((BPMTaskBase) wServiceResult.GetResult()).ID <= 0
						|| ((BPMTaskBase) wServiceResult.GetResult()).FlowID <= 0)
					wServiceResult = wSFCService.SFC_CreateAttemptRun(wLoginUser, wEventID);
				if (StringUtils.isNotEmpty(wServiceResult.FaultCode)) {
					wMsg += wServiceResult.getFaultCode();
				}
				wData = (SFCAttemptRun) wServiceResult.GetResult();

				if (wParam.containsKey("data")) {
					wData = StringUtils.CombineData(wData, wParam.get("data"));
				}
				if (wData.ID > 0) {
					wData.CreateTime = Calendar.getInstance();
					if (wData.FlowID <= 0) {
						wData.FlowID = wBPMService.BPM_CreateProcess(wLoginUser, wEventID, wData.getID(), wData)
								.Info(Integer.class);
					}
					if (wData.FlowID <= 0) {
						wMsg += "?????????????????????";
					} else {
						wServiceResult = wSFCService.SFC_SubmitAttemptRun(wLoginUser, (SFCAttemptRun) wData);
						if (wServiceResult.ErrorCode != 0) {
							wMsg += wServiceResult.getFaultCode();
						}

						wBPMActivitiTask = wBPMService.BPM_GetTaskListByInstance(wLoginUser, wData.FlowID)
								.List(BPMActivitiTask.class);
					}
				}
				break;
			case ToLoanApplyK:
			case ToLoanApply:
				// ????????????????????????
				if (!wCoreService.BMS_CheckPowerByAuthorityID(wLoginUser.CompanyID, wLoginUser.ID, 500703, 0, 0)
						.Info(Boolean.class)) {
					wResult = GetResult(RetCode.SERVER_CODE_SUC, "?????????", null, null);
					return wResult;
				}
				// SCHDayPlanAudit ???????????????0??????????????????????????????
				// ???????????????(???????????????????????????)
				wServiceResult = wSCHService.SCH_QueryDefaultSecondmentApply(wLoginUser, wEventID.getValue());
				if (wServiceResult == null || wServiceResult.GetResult() == null
						|| ((BPMTaskBase) wServiceResult.GetResult()).ID <= 0
						|| ((BPMTaskBase) wServiceResult.GetResult()).FlowID <= 0)
					wServiceResult = wSCHService.SCH_CreateSecondmentApply(wLoginUser, wEventID);
				if (StringUtils.isNotEmpty(wServiceResult.FaultCode)) {
					wMsg += wServiceResult.getFaultCode();
				}
				wData = (SCHSecondmentApply) wServiceResult.GetResult();

				if (wParam.containsKey("data")) {
					wData = StringUtils.CombineData(wData, wParam.get("data"));
				}
				if (wData.ID > 0) {
					wData.CreateTime = Calendar.getInstance();
					if (wData.FlowID <= 0) {
						wData.FlowID = wBPMService.BPM_CreateProcess(wLoginUser, wEventID, wData.getID(), wData)
								.Info(Integer.class);
					}
					if (wData.FlowID <= 0) {
						wMsg += "?????????????????????";
					} else {
						wServiceResult = wSCHService.SCH_SubmitSecondmentApply(wLoginUser, (SCHSecondmentApply) wData);
						if (wServiceResult.ErrorCode != 0) {
							wMsg += wServiceResult.getFaultCode();
						}

						wBPMActivitiTask = wBPMService.BPM_GetTaskListByInstance(wLoginUser, wData.FlowID)
								.List(BPMActivitiTask.class);
					}
				}
				break;
			case APMBomAudit:
				// ????????????????????????
				wServiceResult = wAPSService.APS_QueryDefaultBomBPM(wLoginUser, wEventID.getValue());
				if (wServiceResult == null || wServiceResult.GetResult() == null
						|| ((BPMTaskBase) wServiceResult.GetResult()).ID <= 0
						|| ((BPMTaskBase) wServiceResult.GetResult()).FlowID <= 0)
					wServiceResult = wAPSService.APS_CreateBomBPM(wLoginUser, wEventID);
				if (StringUtils.isNotEmpty(wServiceResult.FaultCode)) {
					wMsg += wServiceResult.getFaultCode();
				}
				wData = (APSBomBPM) wServiceResult.GetResult();

				if (wParam.containsKey("data")) {
					wData = StringUtils.CombineData(wData, wParam.get("data"));
				}
				if (wData.ID > 0) {
					if (wData.FlowID <= 0) {
						wData.FlowID = wBPMService.BPM_CreateProcess(wLoginUser, wEventID, wData.getID(), wData)
								.Info(Integer.class);
					}
					if (wData.FlowID <= 0) {
						wMsg += "?????????????????????";
					} else {
						wServiceResult = wAPSService.APS_SubmitBomBPM(wLoginUser, (APSBomBPM) wData);
						if (wServiceResult.ErrorCode != 0) {
							wMsg += wServiceResult.getFaultCode();
						}

						wBPMActivitiTask = wBPMService.BPM_GetTaskListByInstance(wLoginUser, wData.FlowID)
								.List(BPMActivitiTask.class);
					}
				}
				break;
			case TaskStepCancel:
				// ????????????????????????
				wServiceResult = wAPSService.APS_QueryDefaultTaskStepCancelLogBPM(wLoginUser, wEventID.getValue());
				if (wServiceResult == null || wServiceResult.GetResult() == null
						|| ((BPMTaskBase) wServiceResult.GetResult()).ID <= 0
						|| ((BPMTaskBase) wServiceResult.GetResult()).FlowID <= 0)
					wServiceResult = wAPSService.APS_CreateTaskStepCancelLogBPM(wLoginUser, wEventID);
				if (StringUtils.isNotEmpty(wServiceResult.FaultCode)) {
					wMsg += wServiceResult.getFaultCode();
				}
				wData = (APSTaskStepCancelLogBPM) wServiceResult.GetResult();

				if (wParam.containsKey("data")) {
					wData = StringUtils.CombineData(wData, wParam.get("data"));
				}
				if (wData.ID > 0) {
					if (wData.FlowID <= 0) {
						wData.FlowID = wBPMService.BPM_CreateProcess(wLoginUser, wEventID, wData.getID(), wData)
								.Info(Integer.class);
					}
					if (wData.FlowID <= 0) {
						wMsg += "?????????????????????";
					} else {
						wServiceResult = wAPSService.APS_SubmitTaskStepCancelLogBPM(wLoginUser,
								(APSTaskStepCancelLogBPM) wData);
						if (wServiceResult.ErrorCode != 0) {
							wMsg += wServiceResult.getFaultCode();
						}

						wBPMActivitiTask = wBPMService.BPM_GetTaskListByInstance(wLoginUser, wData.FlowID)
								.List(BPMActivitiTask.class);
					}
				}
				break;
			default:
				break;
			}
			if (wData == null) {
				wMsg += "?????????????????????";
			}
			if (StringUtils.isEmpty(wMsg)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wBPMActivitiTask, wData.FlowID);
				SetResult(wResult, "data", wData);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wMsg);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * ??????????????????
	 * 
	 * @param request
	 * @param wParam
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/CompleteMyPersonalTask")
	public Object CompleteMyPersonalTask(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}
			BMSEmployee wLoginUser = GetSession(request);

			if (!wParam.containsKey("TaskID") || !wParam.containsKey("data")) {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);
				return wResult;
			}

			int wTaskID = CloneTool.Clone(wParam.get("TaskID"), Integer.class);
			BPMTaskBase wBPMTaskBase = CloneTool.Clone(wParam.get("data"), BPMTaskBase.class);
			int wLocalScope = wParam.containsKey("localScope") ? StringUtils.parseInt(wParam.get("localScope")) : 0;
			if (wTaskID <= 0 || wBPMTaskBase == null || wBPMTaskBase.ID <= 0) {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);
				return wResult;
			}

			// ??????????????????????????????(????????????????????????)
			BPMActivitiHisTask wHisTask = wBPMService.BPM_GetTask(wLoginUser, wTaskID).Info(BPMActivitiHisTask.class);
			if (wHisTask == null || StringUtils.isEmpty(wHisTask.ID)) {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, "???????????????????????????!");
				return wResult;
			} else if (wHisTask.Status == BPMHistoryTaskStatus.NomalFinished.getValue()
					|| wHisTask.Status == BPMHistoryTaskStatus.Canceled.getValue()) {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, "???????????????????????????????????????!");
				return wResult;
			}

			int wModuleID = wBPMTaskBase.getFlowType();
			BPMEventModule wEventID = BPMEventModule.getEnumType(wModuleID);
			ServiceResult wServiceResult = null;
			BPMActivitiProcessInstance wBPMActivitiProcessInstance = null;

			ServiceResult<Boolean> wServiceResultBool = new ServiceResult<Boolean>(false);
			switch (wEventID) {
			case SCDayAudit: {
				// ??????????????????
				APSDayPlanAuditBPM wTask = CloneTool.Clone(wParam.get("data"), APSDayPlanAuditBPM.class);

				wServiceResultBool = this.wBPMService.BPM_MsgUpdate(wLoginUser, wTaskID, wLocalScope, wTask,
						wParam.get("data"));
				if (wServiceResultBool.getResult() || !StringUtils.isEmpty(wServiceResultBool.getFaultCode())) {
					wResult = GetResult(RetCode.SERVER_CODE_ERR, "????????????:" + wServiceResultBool.getFaultCode());
					return wResult;
				}
				wServiceResult = wAPSService.APS_SubmitDayPlanAuditBPMTask(wLoginUser, wTask);

				wBPMActivitiProcessInstance = wBPMService.BPM_GetInstanceByID(wLoginUser, wTask.FlowID)
						.Info(BPMActivitiProcessInstance.class);

				/**
				 * ??????????????????
				 */
				if (wBPMActivitiProcessInstance.DurationInMillis > 0
						&& StringUtils.isEmpty(wBPMActivitiProcessInstance.DeleteReason)
						&& (wTask.Status != 20 && wTask.Status != 22)) {
					// ??????????????????????????????????????????????????????????????????????????????
					wTask.Status = SFCBOMTaskStatus.NomalClose.getValue();
					wServiceResult = wAPSService.APS_SubmitDayPlanAuditBPMTask(wLoginUser, wTask);
				}
			}
				break;
			case OccasionNCR: {
				// ??????????????????
				SFCBOMTask wTask = CloneTool.Clone(wParam.get("data"), SFCBOMTask.class);

				wServiceResultBool = this.wBPMService.BPM_MsgUpdate(wLoginUser, wTaskID, wLocalScope, wTask,
						wParam.get("data"));
				if (wServiceResultBool.getResult() || !StringUtils.isEmpty(wServiceResultBool.getFaultCode())) {
					wResult = GetResult(RetCode.SERVER_CODE_ERR, "????????????:" + wServiceResultBool.getFaultCode());
					return wResult;
				}
				wServiceResult = wSFCService.SFC_SubmitBOMTask(wLoginUser, wTask);

				wBPMActivitiProcessInstance = wBPMService.BPM_GetInstanceByID(wLoginUser, wTask.FlowID)
						.Info(BPMActivitiProcessInstance.class);

				/**
				 * ??????????????????
				 */
				if (wBPMActivitiProcessInstance.DurationInMillis > 0
						&& StringUtils.isEmpty(wBPMActivitiProcessInstance.DeleteReason)
						&& (wTask.Status == 66 || wTask.Status == 100 || wTask.Status == 155)) {
					// ??????????????????????????????????????????????????????????????????????????????
					wTask.Status = SFCBOMTaskStatus.NomalClose.getValue();
					wServiceResult = wSFCService.SFC_SubmitBOMTask(wLoginUser, wTask);
				} else if (wBPMActivitiProcessInstance.DurationInMillis > 0
						&& StringUtils.isEmpty(wBPMActivitiProcessInstance.DeleteReason)
						&& (wTask.Status == 31 || wTask.Status == 32 || wTask.Status == 33)) {
					// ??????????????????????????????????????????????????????????????????????????????
					wTask.Status = 21;
					wTask.StatusText = "?????????";
					wServiceResult = wSFCService.SFC_SubmitBOMTask(wLoginUser, wTask);
				}
			}
				break;
			case SCWeekAudit:
			case SCMonthAudit: {
				// ??????????????????
				APSSchedulingVersionBPM wTask = CloneTool.Clone(wParam.get("data"), APSSchedulingVersionBPM.class);

				ServiceResult<String> wCheckResult = wAPSService.APS_CheckSchedulingVersionBPM(wLoginUser, wTask);
				if (StringUtils.isNotEmpty(wCheckResult.Result)) {
					wResult = GetResult(RetCode.SERVER_CODE_ERR, wCheckResult.Result);
					return wResult;
				}

				wServiceResultBool = this.wBPMService.BPM_MsgUpdate(wLoginUser, wTaskID, wLocalScope, wTask,
						wParam.get("data"));
				if (wServiceResultBool.getResult() || !StringUtils.isEmpty(wServiceResultBool.getFaultCode())) {
					wResult = GetResult(RetCode.SERVER_CODE_ERR, "????????????:" + wServiceResultBool.getFaultCode());
					return wResult;
				}
				wServiceResult = wAPSService.APS_SubmitSchedulingVersionBPM(wLoginUser, wTask);

				wBPMActivitiProcessInstance = wBPMService.BPM_GetInstanceByID(wLoginUser, wTask.FlowID)
						.Info(BPMActivitiProcessInstance.class);

				/**
				 * ??????????????????
				 */
				if (wBPMActivitiProcessInstance.DurationInMillis > 0
						&& StringUtils.isEmpty(wBPMActivitiProcessInstance.DeleteReason)
						&& (wTask.Status != 20 && wTask.Status != 21)) {
					// ??????????????????????????????????????????????????????????????????????????????
					wTask.Status = 20;
					wServiceResult = wAPSService.APS_SubmitSchedulingVersionBPM(wLoginUser, wTask);
				}
			}
				break;
			case ToLoan: {
				// ??????????????????
				SCHSecondmentBPM wTask = CloneTool.Clone(wParam.get("data"), SCHSecondmentBPM.class);

				wServiceResultBool = this.wBPMService.BPM_MsgUpdate(wLoginUser, wTaskID, wLocalScope, wTask,
						wParam.get("data"));
				if (wServiceResultBool.getResult() || !StringUtils.isEmpty(wServiceResultBool.getFaultCode())) {
					wResult = GetResult(RetCode.SERVER_CODE_ERR, "????????????:" + wServiceResultBool.getFaultCode());
					return wResult;
				}
				wServiceResult = wSCHService.SCH_SubmitSecondementBPM(wLoginUser, wTask);

				wBPMActivitiProcessInstance = wBPMService.BPM_GetInstanceByID(wLoginUser, wTask.FlowID)
						.Info(BPMActivitiProcessInstance.class);

				/**
				 * ??????????????????
				 */
				if (wBPMActivitiProcessInstance.DurationInMillis > 0
						&& StringUtils.isEmpty(wBPMActivitiProcessInstance.DeleteReason)
						&& (wTask.Status != SCHSecondmentBPMStatus.NomalClose.getValue()
								&& wTask.Status != SCHSecondmentBPMStatus.ExceptionClose.getValue())) {
					// ??????????????????????????????????????????????????????????????????????????????
					wTask.Status = SCHSecondmentBPMStatus.NomalClose.getValue();
					wServiceResult = wSCHService.SCH_SubmitSecondementBPM(wLoginUser, wTask);
				}
			}
				break;
			case AttemptRun: {
				// ??????????????????
				SFCAttemptRun wTask = CloneTool.Clone(wParam.get("data"), SFCAttemptRun.class);

				// ?????????????????????????????????????????????
				ServiceResult<Boolean> wSR = wSFCService.SFC_JudgeIsSendApply(wLoginUser, wTask);
				if (wSR.Result) {
					wResult = GetResult(RetCode.SERVER_CODE_ERR, "?????????????????????????????????????????????!");
					return wResult;
				}

				wServiceResultBool = this.wBPMService.BPM_MsgUpdate(wLoginUser, wTaskID, wLocalScope, wTask,
						wParam.get("data"));
				if (wServiceResultBool.getResult() || !StringUtils.isEmpty(wServiceResultBool.getFaultCode())) {
					wResult = GetResult(RetCode.SERVER_CODE_ERR, "????????????:" + wServiceResultBool.getFaultCode());
					return wResult;
				}
				wServiceResult = wSFCService.SFC_SubmitAttemptRun(wLoginUser, wTask);

				wBPMActivitiProcessInstance = wBPMService.BPM_GetInstanceByID(wLoginUser, wTask.FlowID)
						.Info(BPMActivitiProcessInstance.class);

				/**
				 * ??????????????????
				 */
				if (wBPMActivitiProcessInstance.DurationInMillis > 0
						&& StringUtils.isEmpty(wBPMActivitiProcessInstance.DeleteReason)
						&& (wTask.Status != SFCAttemptRunStatus.NomalClose.getValue()
								|| wTask.Status != SFCAttemptRunStatus.ExceptionClose.getValue())) {
					// ??????????????????????????????????????????????????????????????????????????????
					wTask.Status = SFCAttemptRunStatus.NomalClose.getValue();
					wServiceResult = wSFCService.SFC_SubmitAttemptRun(wLoginUser, wTask);
				}
			}
				break;
			case ToLoanApplyK:
			case ToLoanApply: {
				// ??????????????????
				SCHSecondmentApply wTask = CloneTool.Clone(wParam.get("data"), SCHSecondmentApply.class);

				wServiceResultBool = this.wBPMService.BPM_MsgUpdate(wLoginUser, wTaskID, wLocalScope, wTask,
						wParam.get("data"));
				if (wServiceResultBool.getResult() || !StringUtils.isEmpty(wServiceResultBool.getFaultCode())) {
					wResult = GetResult(RetCode.SERVER_CODE_ERR, "????????????:" + wServiceResultBool.getFaultCode());
					return wResult;
				}
				wServiceResult = wSCHService.SCH_SubmitSecondmentApply(wLoginUser, wTask);

				wBPMActivitiProcessInstance = wBPMService.BPM_GetInstanceByID(wLoginUser, wTask.FlowID)
						.Info(BPMActivitiProcessInstance.class);

				/**
				 * ??????????????????
				 */
				if (wBPMActivitiProcessInstance.DurationInMillis > 0
						&& StringUtils.isEmpty(wBPMActivitiProcessInstance.DeleteReason)
						&& (wTask.Status != SCHSecondmentApplyStatus.NomalClose.getValue()
								&& wTask.Status != SCHSecondmentApplyStatus.ExceptionClose.getValue())) {
					// ??????????????????????????????????????????????????????????????????????????????
					wTask.Status = SCHSecondmentApplyStatus.NomalClose.getValue();
					wServiceResult = wSCHService.SCH_SubmitSecondmentApply(wLoginUser, wTask);
				}
			}
				break;
			case APMBomAudit: {
				// ???????????????
				APSBomBPM wTask = CloneTool.Clone(wParam.get("data"), APSBomBPM.class);

				wServiceResultBool = this.wBPMService.BPM_MsgUpdate(wLoginUser, wTaskID, wLocalScope, wTask,
						wParam.get("data"));
				if (wServiceResultBool.getResult() || !StringUtils.isEmpty(wServiceResultBool.getFaultCode())) {
					wResult = GetResult(RetCode.SERVER_CODE_ERR, "????????????:" + wServiceResultBool.getFaultCode());
					return wResult;
				}
				wServiceResult = wAPSService.APS_SubmitBomBPM(wLoginUser, wTask);

				wBPMActivitiProcessInstance = wBPMService.BPM_GetInstanceByID(wLoginUser, wTask.FlowID)
						.Info(BPMActivitiProcessInstance.class);

				/**
				 * ??????????????????
				 */
				if (wBPMActivitiProcessInstance.DurationInMillis > 0
						&& StringUtils.isEmpty(wBPMActivitiProcessInstance.DeleteReason)
						&& (wTask.Status != APSBomBPMStatus.NomalClose.getValue()
								&& wTask.Status != APSBomBPMStatus.ExceptionClose.getValue())) {
					// ???????????????????????????????????????????????????????????????????????????
					wTask.Status = APSBomBPMStatus.NomalClose.getValue();
					wTask.StatusText = "?????????";
					wServiceResult = wAPSService.APS_SubmitBomBPM(wLoginUser, wTask);
				}
			}
				break;
			case TaskStepCancel: {
				// ???????????????
				APSTaskStepCancelLogBPM wTask = CloneTool.Clone(wParam.get("data"), APSTaskStepCancelLogBPM.class);

				wServiceResultBool = this.wBPMService.BPM_MsgUpdate(wLoginUser, wTaskID, wLocalScope, wTask,
						wParam.get("data"));
				if (wServiceResultBool.getResult() || !StringUtils.isEmpty(wServiceResultBool.getFaultCode())) {
					wResult = GetResult(RetCode.SERVER_CODE_ERR, "????????????:" + wServiceResultBool.getFaultCode());
					return wResult;
				}
				wServiceResult = wAPSService.APS_SubmitTaskStepCancelLogBPM(wLoginUser, wTask);

				wBPMActivitiProcessInstance = wBPMService.BPM_GetInstanceByID(wLoginUser, wTask.FlowID)
						.Info(BPMActivitiProcessInstance.class);

				/**
				 * ??????????????????
				 */
				if (wBPMActivitiProcessInstance.DurationInMillis > 0
						&& StringUtils.isEmpty(wBPMActivitiProcessInstance.DeleteReason)
						&& (wTask.Status != APSTaskStepCancelLogBPMStatus.NomalClose.getValue()
								&& wTask.Status != APSTaskStepCancelLogBPMStatus.ExceptionClose.getValue())) {
					// ???????????????????????????????????????????????????????????????????????????
					wTask.Status = APSTaskStepCancelLogBPMStatus.NomalClose.getValue();
					wServiceResult = wAPSService.APS_SubmitTaskStepCancelLogBPM(wLoginUser, wTask);
				}
			}
				break;
			default:
				break;
			}
			if (wServiceResult == null) {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, "???????????????!");
				return wResult;
			}
			List<BPMActivitiTask> wBPMActivitiTask = new ArrayList<BPMActivitiTask>();
			if (wServiceResult.Result != null && ((BPMTaskBase) wServiceResult.Result).FlowID > 0) {
				wBPMActivitiTask = wBPMService
						.BPM_GetTaskListByInstance(wLoginUser, ((BPMTaskBase) wServiceResult.Result).FlowID)
						.List(BPMActivitiTask.class);
				if (wBPMActivitiTask != null) {
					wBPMActivitiTask.removeIf(
							p -> !StringUtils.parseIntList(p.Assignee.split(",")).contains(wLoginUser.getID()));
				}
			}
			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wBPMActivitiTask, wServiceResult.Result);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.getFaultCode());
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * ??????????????????
	 * 
	 * @param request
	 * @param wParam
	 * @return
	 */
	@GetMapping("/deleteProcessInstance")
	public Object DelectInstance(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}
			BMSEmployee wLoginUser = GetSession(request);

			int wFlowID = StringUtils.parseInt(request.getParameter("processInstanceId"));
			int wID = StringUtils.parseInt(request.getParameter("ID"));
			int wFlowType = StringUtils.parseInt(request.getParameter("FlowType"));
			String wReason = StringUtils.parseString(request.getParameter("deleteReason"));

			if (StringUtils.isEmpty(wReason))
				wReason = "??????";

			if (wFlowID <= 0) {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);
				return wResult;
			}

			APIResult wAPIResult = wBPMService.BPM_DeleteInstanceByID(wLoginUser, wFlowID, wReason);
			if (wAPIResult.getResultCode() != RetCode.SERVER_CODE_SUC)
				return wAPIResult;

			if (wFlowType > 0 && wID > 0) {
				BPMEventModule wEventID = BPMEventModule.getEnumType(wFlowType);
				switch (wEventID) {
				case SCDayAudit: {
					// ??????????????????
					ServiceResult<APSDayPlanAuditBPM> wTaskResult = wAPSService.APS_GetDayPlanAuditBPMTask(wLoginUser,
							wID);
					if (wTaskResult.Result != null && wTaskResult.Result.ID > 0) {
						wTaskResult.Result.Status = 21;
						wTaskResult.Result.StatusText = "?????????";
						wTaskResult.Result.FollowerID = new ArrayList<Integer>();

						wAPSService.APS_SubmitDayPlanAuditBPMTask(wLoginUser, wTaskResult.Result);
					} else {
						wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);
						return wResult;
					}
				}
					break;
				case OccasionNCR: {
					// ??????????????????
					ServiceResult<SFCBOMTask> wTaskResult = wSFCService.SFC_GetBOMTask(wLoginUser, wID);
					if (wTaskResult.Result != null && wTaskResult.Result.ID > 0) {
						wTaskResult.Result.Status = SFCBOMTaskStatus.Canceled.getValue();
						wTaskResult.Result.StatusText = "?????????";
						wTaskResult.Result.FollowerID = new ArrayList<Integer>();

						wSFCService.SFC_SubmitBOMTask(wLoginUser, wTaskResult.Result);
					} else {
						wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);
						return wResult;
					}
				}
					break;
				case SCWeekAudit:
				case SCMonthAudit: {
					// ??????????????????
					ServiceResult<APSSchedulingVersionBPM> wTaskResult = wAPSService
							.APS_GetSchedulingVersionBPM(wLoginUser, wID);
					if (wTaskResult.Result != null && wTaskResult.Result.ID > 0) {
						wTaskResult.Result.Status = 22;
						wTaskResult.Result.StatusText = "?????????";
						wTaskResult.Result.FollowerID = new ArrayList<Integer>();

						wAPSService.APS_SubmitSchedulingVersionBPM(wLoginUser, wTaskResult.Result);
					} else {
						wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);
						return wResult;
					}
				}
					break;
				case ToLoan: {
					// ??????????????????
					ServiceResult<SCHSecondmentBPM> wTaskResult = wSCHService.SCH_GetSecondementBPM(wLoginUser, wID);
					if (wTaskResult.Result != null && wTaskResult.Result.ID > 0) {
						wTaskResult.Result.Status = 22;
						wTaskResult.Result.StatusText = "?????????";
						wTaskResult.Result.FollowerID = new ArrayList<Integer>();

						wSCHService.SCH_SubmitSecondementBPM(wLoginUser, wTaskResult.Result);
					} else {
						wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);
						return wResult;
					}
				}
					break;
				case AttemptRun: {
					// ??????????????????
					ServiceResult<SFCAttemptRun> wTaskResult = wSFCService.SFC_GetAttemptRun(wLoginUser, wID);
					if (wTaskResult.Result != null && wTaskResult.Result.ID > 0) {
						wTaskResult.Result.Status = SFCAttemptRunStatus.Canceled.getValue();
						wTaskResult.Result.StatusText = "?????????";
						wTaskResult.Result.FollowerID = new ArrayList<Integer>();

						wSFCService.SFC_SubmitAttemptRun(wLoginUser, wTaskResult.Result);
					} else {
						wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);
						return wResult;
					}
				}
					break;
				case ToLoanApplyK:
				case ToLoanApply: {
					// ??????????????????
					ServiceResult<SCHSecondmentApply> wTaskResult = wSCHService.SCH_GetSecondmentApply(wLoginUser, wID);
					if (wTaskResult.Result != null && wTaskResult.Result.ID > 0) {
						wTaskResult.Result.Status = SCHSecondmentApplyStatus.Canceled.getValue();
						wTaskResult.Result.StatusText = "?????????";
						wTaskResult.Result.FollowerID = new ArrayList<Integer>();

						wSCHService.SCH_SubmitSecondmentApply(wLoginUser, wTaskResult.Result);
					} else {
						wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);
						return wResult;
					}
				}
					break;
				case APMBomAudit: {
					// ???????????????
					ServiceResult<APSBomBPM> wTaskResult = wAPSService.APS_GetBomBPM(wLoginUser, wID);
					if (wTaskResult.Result != null && wTaskResult.Result.ID > 0) {
						wTaskResult.Result.Status = APSBomBPMStatus.Canceled.getValue();
						wTaskResult.Result.StatusText = "?????????";
						wTaskResult.Result.FollowerID = new ArrayList<Integer>();

						wAPSService.APS_SubmitBomBPM(wLoginUser, wTaskResult.Result);
					} else {
						wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);
						return wResult;
					}
				}
					break;
				case TaskStepCancel: {
					// ???????????????
					ServiceResult<APSTaskStepCancelLogBPM> wTaskResult = wAPSService
							.APS_GetTaskStepCancelLogBPM(wLoginUser, wID);
					if (wTaskResult.Result != null && wTaskResult.Result.ID > 0) {
						wTaskResult.Result.Status = APSTaskStepCancelLogBPMStatus.Canceled.getValue();
						wTaskResult.Result.StatusText = "?????????";
						wTaskResult.Result.FollowerID = new ArrayList<Integer>();

						wAPSService.APS_SubmitTaskStepCancelLogBPM(wLoginUser, wTaskResult.Result);
					} else {
						wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);
						return wResult;
					}
				}
					break;
				default:
					break;
				}
			}
			wResult = GetResult(RetCode.SERVER_CODE_SUC, "???????????????", null, null);
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}
}
