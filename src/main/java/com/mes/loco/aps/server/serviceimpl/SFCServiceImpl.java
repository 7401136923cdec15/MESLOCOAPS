package com.mes.loco.aps.server.serviceimpl;

import com.mes.loco.aps.server.service.SFCService;
import com.mes.loco.aps.server.service.mesenum.APSBOMSourceType;
import com.mes.loco.aps.server.service.mesenum.APSShiftPeriod;
import com.mes.loco.aps.server.service.mesenum.APSTaskStatus;
import com.mes.loco.aps.server.service.mesenum.BFCMessageStatus;
import com.mes.loco.aps.server.service.mesenum.BFCMessageType;
import com.mes.loco.aps.server.service.mesenum.BMSDepartmentType;
import com.mes.loco.aps.server.service.mesenum.BPMEventModule;
import com.mes.loco.aps.server.service.mesenum.FMCShiftLevel;
import com.mes.loco.aps.server.service.mesenum.FPCPartTypes;
import com.mes.loco.aps.server.service.mesenum.MESException;
import com.mes.loco.aps.server.service.mesenum.NCRLevel;
import com.mes.loco.aps.server.service.mesenum.OMSOrderStatus;
import com.mes.loco.aps.server.service.mesenum.SFCAttemptRunStatus;
import com.mes.loco.aps.server.service.mesenum.SFCBOMTaskStatus;
import com.mes.loco.aps.server.service.mesenum.SFCLoginType;
import com.mes.loco.aps.server.service.mesenum.SFCOrderFormType;
import com.mes.loco.aps.server.service.mesenum.SFCRepairItemResult;
import com.mes.loco.aps.server.service.mesenum.SFCSequentialInfoType;
import com.mes.loco.aps.server.service.mesenum.SFCTaskStepType;
import com.mes.loco.aps.server.service.mesenum.SFCTaskType;
import com.mes.loco.aps.server.service.mesenum.TagTypes;
import com.mes.loco.aps.server.service.mesenum.TaskQueryType;
import com.mes.loco.aps.server.service.po.APIResult;
import com.mes.loco.aps.server.service.po.OutResult;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.aps.APSBOMItem;
import com.mes.loco.aps.server.service.po.aps.APSTaskPart;
import com.mes.loco.aps.server.service.po.aps.APSTaskStep;
import com.mes.loco.aps.server.service.po.bfc.BFCMessage;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.bms.BMSRoleItem;
import com.mes.loco.aps.server.service.po.bms.BMSWorkCharge;
import com.mes.loco.aps.server.service.po.bpm.BPMActivitiHisTask;
import com.mes.loco.aps.server.service.po.bpm.BPMActivitiTask;
import com.mes.loco.aps.server.service.po.bpm.BPMTaskBase;
import com.mes.loco.aps.server.service.po.cfg.CFGItem;
import com.mes.loco.aps.server.service.po.excel.MyExcelSheet;
import com.mes.loco.aps.server.service.po.fmc.FMCWorkCharge;
import com.mes.loco.aps.server.service.po.fpc.FPCPart;
import com.mes.loco.aps.server.service.po.fpc.FPCRoutePart;
import com.mes.loco.aps.server.service.po.fpc.FPCRoutePartPoint;
import com.mes.loco.aps.server.service.po.ipt.IPTPreCheckProblem;
import com.mes.loco.aps.server.service.po.ipt.IPTValue;
import com.mes.loco.aps.server.service.po.lfs.LFSWorkAreaChecker;
import com.mes.loco.aps.server.service.po.lfs.LFSWorkAreaStation;
import com.mes.loco.aps.server.service.po.mss.MSSBOMItem;
import com.mes.loco.aps.server.service.po.mss.MSSBOMItemHistory;
import com.mes.loco.aps.server.service.po.mss.MSSMaterial;
import com.mes.loco.aps.server.service.po.oms.OMSOrder;
import com.mes.loco.aps.server.service.po.rro.RROItemTask;
//import com.mes.loco.aps.server.service.po.sch.SCHSecondment;
import com.mes.loco.aps.server.service.po.sch.SCHSecondmentApply;
import com.mes.loco.aps.server.service.po.sfc.SFCAttemptRun;
import com.mes.loco.aps.server.service.po.sfc.SFCBOMTask;
import com.mes.loco.aps.server.service.po.sfc.SFCBOMTaskItem;
import com.mes.loco.aps.server.service.po.sfc.SFCDefaultPerson;
import com.mes.loco.aps.server.service.po.sfc.SFCOrderForm;
import com.mes.loco.aps.server.service.po.sfc.SFCPartPerson;
import com.mes.loco.aps.server.service.po.sfc.SFCRepairItem;
import com.mes.loco.aps.server.service.po.sfc.SFCSequentialInfo;
import com.mes.loco.aps.server.service.po.sfc.SFCSequentialInfoNew;
import com.mes.loco.aps.server.service.po.sfc.SFCStationPerson;
import com.mes.loco.aps.server.service.po.sfc.SFCTaskIPT;
import com.mes.loco.aps.server.service.po.sfc.SFCTaskStep;
import com.mes.loco.aps.server.service.po.sfc.SFCTaskStepCar;
import com.mes.loco.aps.server.service.po.sfc.SFCTaskStepPart;
import com.mes.loco.aps.server.service.po.tcm.TCMMaterialChangeLog;
import com.mes.loco.aps.server.service.utils.CloneTool;
import com.mes.loco.aps.server.service.utils.DesUtil;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.CoreServiceImpl;
import com.mes.loco.aps.server.serviceimpl.FMCServiceImpl;
import com.mes.loco.aps.server.serviceimpl.MyHelperServiceImpl;
import com.mes.loco.aps.server.serviceimpl.QMSServiceImpl;
import com.mes.loco.aps.server.serviceimpl.SFCServiceImpl;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;
import com.mes.loco.aps.server.serviceimpl.dao.andon.AndonDAO;
import com.mes.loco.aps.server.serviceimpl.dao.aps.APSBOMItemDAO;
import com.mes.loco.aps.server.serviceimpl.dao.aps.APSTaskPartDAO;
import com.mes.loco.aps.server.serviceimpl.dao.aps.APSTaskStepDAO;
import com.mes.loco.aps.server.serviceimpl.dao.mss.MSSBOMItemDAO;
import com.mes.loco.aps.server.serviceimpl.dao.mss.MSSBOMItemHistoryDAO;
import com.mes.loco.aps.server.serviceimpl.dao.oms.OMSOrderDAO;
import com.mes.loco.aps.server.serviceimpl.dao.sch.SCHSecondmentApplyDAO;
import com.mes.loco.aps.server.serviceimpl.dao.sfc.SFCAttemptRunDAO;
import com.mes.loco.aps.server.serviceimpl.dao.sfc.SFCBOMTaskDAO;
import com.mes.loco.aps.server.serviceimpl.dao.sfc.SFCBOMTaskItemDAO;
import com.mes.loco.aps.server.serviceimpl.dao.sfc.SFCDefaultPersonDAO;
import com.mes.loco.aps.server.serviceimpl.dao.sfc.SFCOrderFormDAO;
import com.mes.loco.aps.server.serviceimpl.dao.sfc.SFCRepairItemDAO;
import com.mes.loco.aps.server.serviceimpl.dao.sfc.SFCTaskStepDAO;
import com.mes.loco.aps.server.serviceimpl.dao.tcm.TCMMaterialChangeLogDAO;
import com.mes.loco.aps.server.serviceimpl.utils.MESServer;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSConstans;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSUtils;
import com.mes.loco.aps.server.utils.RetCode;
import com.mes.loco.aps.server.utils.aps.ExcelUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SFCServiceImpl implements SFCService {
	private static Logger logger = LoggerFactory.getLogger(SFCServiceImpl.class);

	private static SFCService Instance;

	public static SFCService getInstance() {
		if (Instance == null)
			Instance = new SFCServiceImpl();
		return Instance;
	}

	public ServiceResult<SFCTaskStep> SFC_QueryTaskStep(BMSEmployee wLoginUser, int wID) {
		ServiceResult<SFCTaskStep> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = SFCTaskStepDAO.getInstance().SelectByID(wLoginUser, wID, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<List<SFCTaskStep>> SFC_QueryTaskStepList(BMSEmployee wLoginUser, int wID, int wTaskStepID,
			int wShiftID, int wOpeartorID, int wMonitorID) {
		ServiceResult<List<SFCTaskStep>> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = SFCTaskStepDAO.getInstance().SelectList(wLoginUser, wID, wTaskStepID, wShiftID,
					wOpeartorID, wMonitorID, null, -1, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<Integer> SFC_UpdateTaskStep(BMSEmployee wLoginUser, SFCTaskStep wSFCTaskStep) {
		ServiceResult<Integer> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = Integer.valueOf(SFCTaskStepDAO.getInstance().Update(wLoginUser, wSFCTaskStep, wErrorCode));
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<List<BMSEmployee>> SFC_QueryDispatchEmployeeList(BMSEmployee wLoginUser, boolean wIsRemove,
			List<Integer> wAPSTaskStepIDList) {
		ServiceResult<List<BMSEmployee>> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));

			wResult.Result = new ArrayList<>();

			List<BMSEmployee> wEmployeeList = CoreServiceImpl.getInstance().BMS_GetEmployeeAll(wLoginUser, 0, 0, 1)
					.List(BMSEmployee.class);
			if (wEmployeeList == null || wEmployeeList.size() <= 0) {
				return wResult;
			}
			if ((APSConstans.GetBMSDepartment(wLoginUser.DepartmentID)).Type != BMSDepartmentType.Class.getValue()
					|| (APSConstans.GetBMSPosition(wLoginUser.Position)).DutyID != 1) {
				return wResult;
			}

			wResult.Result = wEmployeeList.stream().filter(p -> (p.DepartmentID == wLoginUser.DepartmentID))
					.collect(Collectors.toList());

			List<SCHSecondmentApply> wSecondList = SCHSecondmentApplyDAO.getInstance().SelectListByClass(wLoginUser, -1,
					wLoginUser.DepartmentID, new ArrayList<Integer>(Arrays.asList(20)), wErrorCode);
			wSecondList = wSecondList.stream().filter(p -> Calendar.getInstance().compareTo(p.StartTime) >= 0
					&& Calendar.getInstance().compareTo(p.EndTime) <= 0).collect(Collectors.toList());

			if (wSecondList.size() > 0) {
				for (SCHSecondmentApply wItem : wSecondList) {

					String[] wIDs = wItem.PersonID.split(",");
					for (String wID : wIDs) {
						Integer wUserID = StringUtils.parseInt(wID);
						if (wUserID > 0) {
							if (wEmployeeList.stream().anyMatch(p -> p.ID == wUserID)) {
								BMSEmployee wPersonItem = wEmployeeList.stream().filter(p -> (p.ID == wUserID))
										.findFirst().get();
								wPersonItem.Name = StringUtils.Format("{0}({1})", wPersonItem.Name,
										APSConstans.GetBMSDepartmentName(wPersonItem.DepartmentID));
								wResult.Result.add(wPersonItem);
							}
						}
					}
				}
			}

			List<SCHSecondmentApply> wBeSecondList = SCHSecondmentApplyDAO.getInstance().SelectListByClass(wLoginUser,
					wLoginUser.DepartmentID, -1, new ArrayList<Integer>(Arrays.asList(20)), wErrorCode);
			wBeSecondList = wBeSecondList.stream().filter(p -> Calendar.getInstance().compareTo(p.StartTime) >= 0
					&& Calendar.getInstance().compareTo(p.EndTime) <= 0).collect(Collectors.toList());

			if (wBeSecondList.size() > 0) {
			}

			if (wIsRemove) {
				for (Integer wAPSTaskStepID : wAPSTaskStepIDList) {
					ServiceResult<List<SFCTaskStep>> wTaskList = this.SFC_QueryTaskStepList(wLoginUser, -1,
							wAPSTaskStepID, -1, -1, -1);
					// ?????????????????????????????????
					if (wResult != null && wResult.Result != null && wResult.Result.size() > 0 && wTaskList != null
							&& wTaskList.Result != null && wTaskList.Result.size() > 0) {
						wResult.Result.removeIf(p -> wTaskList.Result.stream().anyMatch(q -> q.OperatorID == p.ID));
					}
				}
			}

			// ????????????
			if (wResult.Result.size() > 0) {
				for (BMSEmployee wUser : wResult.Result) {
					if (StringUtils.isEmpty(wUser.LoginID) || wUser.LoginID.length() != 12) {
						continue;
					}
					wUser.Name = StringUtils.Format("{0}({1})", wUser.Name, wUser.LoginID.substring(7));
				}
			}

			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<List<SFCTaskStep>> SFC_QueryTaskStepListByEmployeeID(BMSEmployee wLoginUser,
			Calendar wStartTime, Calendar wEndTime, int wTagTypes) {
		ServiceResult<List<SFCTaskStep>> wResult = new ServiceResult<>();
		wResult.Result = new ArrayList<>();
		OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
		try {
			wResult.Result = SFCTaskStepDAO.getInstance().SelectListByTime(wLoginUser, wLoginUser.ID, wStartTime,
					wEndTime, wErrorCode);

			if (wResult.Result.size() <= 0) {
				return wResult;
			}

			List<SFCTaskStep> wAllSFCTaskStepList = new ArrayList<>();

			List<Integer> wStepTaskStepIDList = (List<Integer>) (wResult.Result).stream()
					.filter(p -> (p.Type == SFCTaskStepType.Step.getValue())).map(p -> Integer.valueOf(p.TaskStepID))
					.collect(Collectors.toList());
			if (wStepTaskStepIDList != null && wStepTaskStepIDList.size() > 0) {
				List<SFCTaskStep> wTempList = SFCTaskStepDAO.getInstance().SelectListByTaskStepIDList(wLoginUser,
						wStepTaskStepIDList, SFCTaskStepType.Step.getValue(), wErrorCode);
				if (wTempList != null && wTempList.size() > 0) {
					wAllSFCTaskStepList.addAll(wTempList);
				}
			}

			List<Integer> wQuestionTaskStepIDList = (List<Integer>) (wResult.Result).stream()
					.filter(p -> (p.Type == SFCTaskStepType.Question.getValue()))
					.map(p -> Integer.valueOf(p.TaskStepID)).collect(Collectors.toList());
			if (wQuestionTaskStepIDList != null && wQuestionTaskStepIDList.size() > 0) {
				List<SFCTaskStep> wTempList = SFCTaskStepDAO.getInstance().SelectListByTaskStepIDList(wLoginUser,
						wQuestionTaskStepIDList, SFCTaskStepType.Question.getValue(), wErrorCode);
				if (wTempList != null && wTempList.size() > 0) {
					wAllSFCTaskStepList.addAll(wTempList);
				}
			}

			List<Integer> wQualityTaskStepIDList = (List<Integer>) (wResult.Result).stream()
					.filter(p -> (p.Type == SFCTaskStepType.Quality.getValue())).map(p -> Integer.valueOf(p.TaskStepID))
					.collect(Collectors.toList());
			if (wQualityTaskStepIDList != null && wQualityTaskStepIDList.size() > 0) {
				List<SFCTaskStep> wTempList = SFCTaskStepDAO.getInstance().SelectListByTaskStepIDList(wLoginUser,
						wQualityTaskStepIDList, SFCTaskStepType.Quality.getValue(), wErrorCode);
				if (wTempList != null && wTempList.size() > 0) {
					wAllSFCTaskStepList.addAll(wTempList);
				}
			}

			for (SFCTaskStep wSFCTaskStep : wResult.Result) {
				List<SFCTaskStep> wTempList = (List<SFCTaskStep>) wAllSFCTaskStepList.stream()
						.filter(p -> (p.TaskStepID == wSFCTaskStep.TaskStepID && p.Type == wSFCTaskStep.Type))
						.collect(Collectors.toList());

				if (wTempList == null || wTempList.size() <= 0) {
					continue;
				}
				List<String> wOpeList = (List<String>) wTempList.stream()
						.map(p -> APSConstans.GetBMSEmployeeName(p.OperatorID)).collect(Collectors.toList());
				wSFCTaskStep.Operators = StringUtils.Join(",", wOpeList);
			}

			// ??????????????????
			if (wResult.Result.size() > 0) {
				// ??????????????? ????????????
				List<LFSWorkAreaStation> wLFSWorkAreaStationList = APSConstans.GetLFSWorkAreaStationList().values()
						.stream().collect(Collectors.toList());
				// ?????????????????????
				for (SFCTaskStep wSFCTaskStep : wResult.Result) {
					if (wLFSWorkAreaStationList.stream().anyMatch(p -> p.StationID == wSFCTaskStep.PartID)) {
						wSFCTaskStep.PartOrderID = wLFSWorkAreaStationList.stream()
								.filter(p -> p.StationID == wSFCTaskStep.PartID).findFirst().get().OrderNum;
					}
				}
				// ?????????????????????????????????????????????(?????????????????????????????????????????????)
				List<SFCTaskStep> wList1 = new ArrayList<SFCTaskStep>();
				wList1 = wResult.Result.stream().filter(p -> p.IsStartWork == SFCLoginType.Default.getValue())
						.collect(Collectors.toList());
				List<SFCTaskStep> wList2 = new ArrayList<SFCTaskStep>();
				wList2 = wResult.Result.stream().filter(p -> p.IsStartWork == SFCLoginType.StartWork.getValue())
						.collect(Collectors.toList());
				List<SFCTaskStep> wList3 = new ArrayList<SFCTaskStep>();
				wList3 = wResult.Result.stream().filter(p -> p.IsStartWork == SFCLoginType.AfterWork.getValue())
						.collect(Collectors.toList());
				List<SFCTaskStep> wList4 = new ArrayList<SFCTaskStep>();
				wList4 = wResult.Result.stream().filter(p -> p.IsStartWork == SFCLoginType.StopWork.getValue())
						.collect(Collectors.toList());
				// ????????????????????????????????????
				wList1.sort(Comparator.comparing(SFCTaskStep::getPartOrderID).thenComparing(SFCTaskStep::getPartNo));
				// ?????????????????????????????????
				wList4.sort(Comparator.comparing(SFCTaskStep::getPartOrderID).thenComparing(SFCTaskStep::getPartNo));
				// ????????????????????????????????????
				wList2.sort(Comparator.comparing(SFCTaskStep::getPartOrderID).thenComparing(SFCTaskStep::getPartNo));
				// ????????????????????????????????????
				wList3.sort(Comparator.comparing(SFCTaskStep::getPartOrderID).thenComparing(SFCTaskStep::getPartNo));
				// ??????????????????
				wResult.Result = new ArrayList<SFCTaskStep>();
				wResult.Result.addAll(wList1);
				wResult.Result.addAll(wList4);
				wResult.Result.addAll(wList2);
				wResult.Result.addAll(wList3);
			}

			// ??????????????????????????????????????????
			if (wResult.Result.size() > 0) {
				switch (SFCLoginType.getEnumType(wTagTypes)) {
				case StartWork:
					wResult.Result = wResult.Result.stream()
							.filter(p -> p.IsStartWork == SFCLoginType.Default.getValue()
									|| p.IsStartWork == SFCLoginType.StartWork.getValue()
									|| p.IsStartWork == SFCLoginType.StopWork.getValue())
							.collect(Collectors.toList());
					break;
				case AfterWork:
					wResult.Result = wResult.Result.stream()
							.filter(p -> p.IsStartWork == SFCLoginType.AfterWork.getValue()
									|| p.IsStartWork == SFCLoginType.StartWork.getValue())
							.collect(Collectors.toList());
					break;
				default:
					break;
				}

				List<Integer> wProblemIDList = wResult.Result.stream()
						.filter(p -> p.Type == SFCTaskStepType.Question.getValue()).map(p -> p.TaskStepID)
						.collect(Collectors.toList());
				if (wProblemIDList == null || wProblemIDList.size() <= 0) {
					return wResult;
				}

				for (SFCTaskStep wSFCTaskStep : wResult.Result) {
					if (wSFCTaskStep.Type != SFCTaskStepType.Question.getValue()) {
						continue;
					}

					IPTPreCheckProblem wProblem = QMSServiceImpl.getInstance()
							.IPT_QueryPreCheckProblemByID(wLoginUser, wSFCTaskStep.TaskStepID)
							.Info(IPTPreCheckProblem.class);
					if (wProblem == null || wProblem.ID <= 0) {
						continue;
					}

					wSFCTaskStep.PartPointName = wProblem.IPTItemName;
				}
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<List<SFCTaskStep>> SFC_QueryTaskStepListByMonitorID(BMSEmployee wLoginUser,
			Calendar wShiftDate) {
		ServiceResult<List<SFCTaskStep>> wResult = new ServiceResult<>();
		wResult.Result = new ArrayList<>();
		OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));

		try {
			int wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, Calendar.getInstance(), APSShiftPeriod.Day,
					FMCShiftLevel.Day, 0);

			wResult.Result = SFCTaskStepDAO.getInstance().SelectList(wLoginUser, -1, -1, wShiftID, -1, wLoginUser.ID,
					null, -1, wErrorCode);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<Integer> SFC_AdjustHour(BMSEmployee wLoginUser, List<SFCTaskStep> wSFCTaskStepList,
			Double wHour) {
		ServiceResult<Integer> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			for (SFCTaskStep wSFCTaskStep : wSFCTaskStepList) {
				wSFCTaskStep.WorkHour = wHour.doubleValue();
				SFCTaskStepDAO.getInstance().Update(wLoginUser, wSFCTaskStep, wErrorCode);
			}
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			wResult.FaultCode = String.valueOf(wResult.FaultCode) + e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<String> SFC_CheckPGPower(BMSEmployee wLoginUser, List<Integer> wAPSTaskStepIDList) {
		ServiceResult<String> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			StringBuffer wSB = new StringBuffer();
			for (Integer wTaskStepID : wAPSTaskStepIDList) {
				APSTaskStep wTaskStep = APSTaskStepDAO.getInstance().SelectByID(wLoginUser, wTaskStepID.intValue(),
						wErrorCode);
				if (wTaskStep == null || wTaskStep.ID <= 0) {
					wResult.Result = "????????????!";
					return wResult;
				}

				if (!((Boolean) (MyHelperServiceImpl.getInstance().SFC_JudgeTaskStepIsCanDo(wLoginUser,
						wTaskStep)).Result).booleanValue()) {
					wSB.append("???" + wTaskStep.StepName + "???");
				}
			}
			wResult.Result = wSB.toString();
			if (StringUtils.isNotEmpty((String) wResult.Result)) {
				wResult.Result = String.valueOf(wResult.Result) + "??????????????????????????????!";
			}
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			wResult.FaultCode = String.valueOf(wResult.FaultCode) + e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<Map<Integer, List<Integer>>> SFC_QueryStepPersonMap(BMSEmployee wLoginUser,
			List<APSTaskStep> wAPSTaskStepList) {
		ServiceResult<Map<Integer, List<Integer>>> wResult = new ServiceResult<>();
		wResult.Result = new HashMap<>();
		try {
			if (wAPSTaskStepList == null || wAPSTaskStepList.size() <= 0) {
				return wResult;
			}

			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));

			List<SFCDefaultPerson> wList = null;
			for (APSTaskStep wAPSTaskStep : wAPSTaskStepList) {
				wList = SFCDefaultPersonDAO.getInstance().SelectList(wLoginUser, -1,
						APSConstans.GetFPCProducID(wAPSTaskStep.ProductNo), wAPSTaskStep.LineID, wAPSTaskStep.PartID,
						wAPSTaskStep.StepID, wErrorCode);
				if (wList != null && wList.size() > 0) {
					(wResult.Result).put(Integer.valueOf(wAPSTaskStep.ID),
							((SFCDefaultPerson) wList.get(0)).PersonIDList);
				}
			}
		} catch (Exception e) {
			wResult.FaultCode = String.valueOf(wResult.FaultCode) + e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<List<BMSEmployee>> SFC_QueryPGEmployeeList(BMSEmployee wLoginUser, int wID) {
		ServiceResult<List<BMSEmployee>> wResult = new ServiceResult<>();
		try {
			wResult.Result = new ArrayList<>();
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			List<SFCTaskStep> wList = SFCTaskStepDAO.getInstance().SelectList(wLoginUser, -1, wID, -1, -1, -1, null,
					SFCTaskStepType.Step.getValue(), wErrorCode);
			for (SFCTaskStep wSFCTaskStep : wList) {
				((List<BMSEmployee>) wResult.Result).add(APSConstans.GetBMSEmployee(wSFCTaskStep.OperatorID));
			}
		} catch (Exception e) {
			wResult.FaultCode = String.valueOf(wResult.FaultCode) + e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	public synchronized ServiceResult<Integer> SFC_TaskStepSubmitAll(BMSEmployee wLoginUser,
			List<APSTaskStep> wAPSTaskStepList) {
		ServiceResult<Integer> wResult = new ServiceResult<>();
		OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
		try {
			if (wAPSTaskStepList == null || wAPSTaskStepList.size() <= 0) {
				return wResult;
			}

			if (wAPSTaskStepList.stream().anyMatch(p -> !(p.OperatorList != null && p.OperatorList.size() > 0))) {
				wResult.FaultCode = String.valueOf(wResult.FaultCode) + "?????????????????????????????????!";
				return wResult;
			}

			if (wAPSTaskStepList.stream().anyMatch(p -> p.Status != APSTaskStatus.Issued.getValue())) {
				wResult.FaultCode = String.valueOf(wResult.FaultCode) + "????????????????????????????????????????????????!";
				return wResult;
			}

			// ???????????????????????????
			wAPSTaskStepList
					.forEach(p -> p.OperatorList = p.OperatorList.stream().distinct().collect(Collectors.toList()));

			String wCheckResult = SFC_IsPresonCanUse(wLoginUser, wAPSTaskStepList);
			if (StringUtils.isNotEmpty(wCheckResult)) {
				wResult.FaultCode = String.valueOf(wResult.FaultCode) + wCheckResult;
				return wResult;
			}

			int wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, Calendar.getInstance(), APSShiftPeriod.Day,
					FMCShiftLevel.Day, 0);

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1);

			int wNewID = 0;
			List<BFCMessage> wMessageList = new ArrayList<>();

			List<SFCTaskStep> wAllSFCTaskStepList = SFCTaskStepDAO.getInstance().SelectListByTaskStepIDList(wLoginUser,
					wAPSTaskStepList.stream().map(p -> Integer.valueOf(p.ID)).collect(Collectors.toList()), -1,
					wErrorCode);

			for (APSTaskStep wAPSTaskStep : wAPSTaskStepList) {
				for (Iterator<Integer> iterator = wAPSTaskStep.OperatorList.iterator(); iterator.hasNext();) {
					int wOperatorID = ((Integer) iterator.next()).intValue();

					if (wAllSFCTaskStepList.stream()
							.anyMatch(p -> p.TaskStepID == wAPSTaskStep.ID && p.OperatorID == wOperatorID)) {
						continue;
					}

					SFCTaskStep wSFCTaskStep = new SFCTaskStep();
					wSFCTaskStep.TaskStepID = wAPSTaskStep.ID;
					wSFCTaskStep.CreateTime = Calendar.getInstance();
					wSFCTaskStep.EndTime = wBaseTime;
					wSFCTaskStep.ShiftID = wShiftID;
					wSFCTaskStep.WorkHour = 0.0D;
					wSFCTaskStep.OperatorID = wOperatorID;
					wSFCTaskStep.ReadyTime = Calendar.getInstance();
					wSFCTaskStep.MonitorID = wLoginUser.ID;
					wSFCTaskStep.Type = (wAPSTaskStep.TaskPartID > 0) ? SFCTaskStepType.Step.getValue()
							: SFCTaskStepType.Quality.getValue();
					wNewID = ((Integer) (SFC_UpdateTaskStep(wLoginUser, wSFCTaskStep)).Result).intValue();

					this.CreateMessage(wLoginUser, wShiftID, wSFCTaskStep, wNewID, wMessageList, wAPSTaskStep,
							wOperatorID);
				}

				// ????????????????????????
				UpdateDefaultPerson(wLoginUser, wErrorCode, wNewID, wAPSTaskStep);

				wAPSTaskStep.IsDispatched = true;
				APSTaskStepDAO.getInstance().Update(wLoginUser, wAPSTaskStep, wErrorCode);
			}

			// ???????????????????????????????????????
			CloseMessageList(wLoginUser, wAllSFCTaskStepList, wErrorCode);
			// ????????????
			CoreServiceImpl.getInstance().BFC_UpdateMessageList(wLoginUser, wMessageList);
		} catch (Exception e) {
			wResult.FaultCode = String.valueOf(wResult.FaultCode) + e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * ????????????
	 * 
	 * @param wLoginUser
	 * @param wAllSFCTaskStepList
	 * @param wErrorCode
	 */
	private void CloseMessageList(BMSEmployee wLoginUser, List<SFCTaskStep> wAllSFCTaskStepList,
			OutResult<Integer> wErrorCode) {
		try {
			if (wAllSFCTaskStepList == null || wAllSFCTaskStepList.size() <= 0) {
				return;
			}

			List<BFCMessage> wList = CoreServiceImpl.getInstance().BFC_GetMessageList(wLoginUser, -1,
					BPMEventModule.SCDispatching.getValue(), BFCMessageType.Task.getValue(), -1, -1, null, null)
					.List(BFCMessage.class);
			if (wList == null || wList.size() <= 0) {
				return;
			}

			wList = wList.stream().filter(p -> wAllSFCTaskStepList.stream().anyMatch(q -> q.ID == p.MessageID)
					&& (p.Active == 0 || p.Active == 1 || p.Active == 2)).collect(Collectors.toList());

			if (wList == null || wList.size() <= 0) {
				return;
			}

			wList.forEach(p -> {
				p.Active = BFCMessageStatus.Close.getValue();
			});

			CoreServiceImpl.getInstance().BFC_UpdateMessageList(wLoginUser, wList);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * ??????????????????
	 * 
	 * @param wLoginUser
	 * @param wErrorCode
	 * @param wNewID
	 * @param wAPSTaskStep
	 */
	private void UpdateDefaultPerson(BMSEmployee wLoginUser, OutResult<Integer> wErrorCode, int wNewID,
			APSTaskStep wAPSTaskStep) {
		try {
			// ??????????????????ID
			if (wAPSTaskStep.OperatorList != null && wAPSTaskStep.OperatorList.size() > 0) {
				wAPSTaskStep.OperatorList = wAPSTaskStep.OperatorList.stream().distinct().collect(Collectors.toList());
			}

			SFCTaskStep wNewItem = SFCTaskStepDAO.getInstance().SelectByID(wLoginUser, wNewID, wErrorCode);
			List<SFCDefaultPerson> wDefaultList = SFCDefaultPersonDAO.getInstance().SelectList(wLoginUser, -1,
					wNewItem.ProductID, wNewItem.LineID, wNewItem.PartID, wNewItem.StepID, wErrorCode);
			if (wDefaultList != null && wDefaultList.size() > 0) {
				((SFCDefaultPerson) wDefaultList.get(0)).PersonIDList = wAPSTaskStep.OperatorList;
				SFCDefaultPersonDAO.getInstance().Update(wLoginUser, wDefaultList.get(0), wErrorCode);
			} else {
				SFCDefaultPerson wSFCDefaultPerson = new SFCDefaultPerson();
				wSFCDefaultPerson.ID = 0;
				wSFCDefaultPerson.LineID = wNewItem.LineID;
				wSFCDefaultPerson.PartID = wNewItem.PartID;
				wSFCDefaultPerson.PartPointID = wNewItem.StepID;
				wSFCDefaultPerson.PersonIDList = wAPSTaskStep.OperatorList;
				wSFCDefaultPerson.ProductID = APSConstans.GetFPCProducID(wNewItem.ProductNo);
				SFCDefaultPersonDAO.getInstance().Update(wLoginUser, wSFCDefaultPerson, wErrorCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	private void CreateMessage(BMSEmployee wLoginUser, int wShiftID, SFCTaskStep wSFCTaskStep, int wNewID,
			List<BFCMessage> wMessageList, APSTaskStep wAPSTaskStep, int wOperatorID) {
		try {
			BFCMessage wMessage = new BFCMessage();
			wMessage.Active = 0;
			wMessage.CompanyID = 0;
			wMessage.CreateTime = Calendar.getInstance();
			wMessage.EditTime = Calendar.getInstance();
			wMessage.ID = 0L;
			wMessage.MessageID = wNewID;
			wMessage.Title = StringUtils.Format("?????? {0} {1}",
					new Object[] { wAPSTaskStep.StepName, wAPSTaskStep.PartNo });
			wMessage.MessageText = StringUtils.Format("???{0}??? {1}?????????{2}????????????????????????!",
					new Object[] {
							(wAPSTaskStep.TaskPartID > 0) ? BPMEventModule.SCDispatching.getLable()
									: BPMEventModule.SCDispatching.getLable(),
							wLoginUser.Name, wAPSTaskStep.StepName });
			wMessage.ModuleID = ((wAPSTaskStep.TaskPartID > 0) ? BPMEventModule.SCDispatching.getValue()
					: BPMEventModule.SCDispatching.getValue());
			wMessage.ResponsorID = wOperatorID;
			wMessage.ShiftID = wShiftID;
			wMessage.StationID = wSFCTaskStep.PartID;
			wMessage.Type = BFCMessageType.Task.getValue();
			wMessageList.add(wMessage);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	private String SFC_IsPresonCanUse(BMSEmployee wLoginUser, List<APSTaskStep> wAPSTaskStepList) {
		String wResult = "";
		OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
		try {
			List<Integer> wPersonIDList = new ArrayList<>();
			for (APSTaskStep wAPSTaskStep : wAPSTaskStepList) {
				wPersonIDList.addAll(wAPSTaskStep.OperatorList);
			}
			wPersonIDList = (List<Integer>) wPersonIDList.stream().distinct().collect(Collectors.toList());

			List<SCHSecondmentApply> wSCHSecondmentList = SCHSecondmentApplyDAO.getInstance()
					.SelectListByPersonIDList(wLoginUser, wPersonIDList, wErrorCode);
			wSCHSecondmentList.removeIf(p -> p.Status == 20 && (Calendar.getInstance().compareTo(p.StartTime) < 0
					|| Calendar.getInstance().compareTo(p.EndTime) > 0));

			Map<Integer, List<Integer>> wPersonClassListMap = new HashMap<>();
			for (Integer wPersonID : wPersonIDList) {
				List<SCHSecondmentApply> wTempList = (List<SCHSecondmentApply>) wSCHSecondmentList.stream()
						.filter(p -> (p.PersonID.contains(String.valueOf(wPersonID)))).collect(Collectors.toList());
				if (!wTempList.stream().anyMatch(p -> (p.PersonID.contains(String.valueOf(wPersonID))))) {
					int wDepartmetnID = APSConstans.GetBMSEmployee(wPersonID).DepartmentID;
					if (wDepartmetnID == wLoginUser.DepartmentID) {
						wPersonClassListMap.put(wPersonID, new ArrayList<>(Arrays.asList(new Integer[] {
								Integer.valueOf((APSConstans.GetBMSEmployee(wPersonID.intValue())).DepartmentID) })));
					} else {
						wResult = StringUtils.Format("????????????{0}???????????????{1}????????????????????????????????????????????????!",
								APSConstans.GetBMSEmployee(wPersonID).Name,
								APSConstans.GetBMSDepartmentName(wLoginUser.DepartmentID));
						return wResult;
					}
					continue;
				}
				if (wTempList.stream()
						.anyMatch(p -> (p.PersonID.contains(String.valueOf(wPersonID)) && p.Status != 20))) {
					wPersonClassListMap.put(wPersonID, new ArrayList<>());
					continue;
				}
				SCHSecondmentApply wTask = wTempList.stream()
						.filter(p -> (p.PersonID.contains(String.valueOf(wPersonID)) && p.Status == 20)).findFirst()
						.get();

				wPersonClassListMap.put(wPersonID, new ArrayList<>(Arrays.asList(
						new Integer[] { Integer.valueOf(wTask.NewClassID), Integer.valueOf(wTask.OldClassID) })));
			}

			Map<Integer, List<Integer>> wPersonStationListMap = new HashMap<>();
			for (Integer wPersonID : wPersonClassListMap.keySet()) {
				List<Integer> wClassIDList = wPersonClassListMap.get(wPersonID);
				if (wClassIDList.size() <= 0) {
					wPersonStationListMap.put(wPersonID, new ArrayList<>());
					continue;
				}
				List<Integer> wStationIDList = new ArrayList<>();
				for (Integer wClassID : wClassIDList) {
					List<FMCWorkCharge> wWorkChargeList = (List<FMCWorkCharge>) APSConstans.GetFMCWorkChargeList()
							.stream().filter(p -> (p.ClassID == wClassID.intValue())).collect(Collectors.toList());
					if (wWorkChargeList == null || wWorkChargeList.size() <= 0) {
						continue;
					}
					wStationIDList.addAll((Collection<? extends Integer>) wWorkChargeList.stream()
							.map(p -> Integer.valueOf(p.StationID)).collect(Collectors.toList()));
				}
				wPersonStationListMap.put(wPersonID, wStationIDList);
			}

			for (APSTaskStep wAPSTaskStep : wAPSTaskStepList) {
				for (Integer wPersonID : wAPSTaskStep.OperatorList) {
					if (!(wPersonStationListMap.get(wPersonID)).stream()
							.anyMatch(p -> (p.intValue() == wAPSTaskStep.PartID))) {
						wResult = StringUtils.Format("????????????{0}????????????????????????????????????????????????{1}???", new Object[] {
								APSConstans.GetBMSEmployeeName(wPersonID.intValue()), wAPSTaskStep.StepName });
						return wResult;
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	public ServiceResult<Integer> SFC_TaskStepSaveAll(BMSEmployee wLoginUser, List<APSTaskStep> wAPSTaskStepList) {
		ServiceResult<Integer> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));

			List<SFCTaskStep> wAllSFCTaskStepList = SFCTaskStepDAO.getInstance().SelectListByTaskStepIDList(wLoginUser,
					wAPSTaskStepList.stream().map(p -> Integer.valueOf(p.ID)).collect(Collectors.toList()), -1,
					wErrorCode);

			wAPSTaskStepList.forEach(p -> {
				p.IsDispatched = false;
				APSTaskStepDAO.getInstance().Update(wLoginUser, p, wErrorCode);
			});

			SFCTaskStepDAO.getInstance().DeleteList(wLoginUser, wAllSFCTaskStepList, wErrorCode);

			// ???????????????????????????????????????
			ExecutorService wES = Executors.newFixedThreadPool(1);
			wES.submit(() -> CloseMessageList(wLoginUser, wAllSFCTaskStepList, wErrorCode));
			wES.shutdown();

			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			wResult.FaultCode = String.valueOf(wResult.FaultCode) + e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<Integer> SFC_SubmitPGProblems(BMSEmployee wLoginUser, List<IPTPreCheckProblem> wDataList) {
		ServiceResult<Integer> wResult = new ServiceResult<>();
		OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
		try {
			List<SFCTaskStep> wTaskStepList = null;

			if (wDataList.stream().anyMatch(p -> p.DoPersonID <= 0)) {
				wResult.FaultCode += "?????????????????????????????????!";
				return wResult;
			}

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1);
			int wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, Calendar.getInstance(), APSShiftPeriod.Day,
					FMCShiftLevel.Day, 0);
			int wNewID = 0;
			List<BFCMessage> wMessageList = new ArrayList<>();

			Map<Integer, OMSOrder> wOrderMap = new HashMap<Integer, OMSOrder>();
			if (wDataList.size() > 0) {
				List<Integer> wOrderIDList = wDataList.stream().filter(p -> p.OrderID > 0).map(p -> p.OrderID)
						.distinct().collect(Collectors.toList());

				if (wOrderIDList.size() > 0) {
					wOrderMap = OMSOrderDAO.getInstance().SelectListByIDList(wLoginUser, wOrderIDList, wErrorCode)
							.stream().collect(Collectors.toMap(p -> p.ID, p -> p));
				}

			}

			OMSOrder wOMSOrder = null;
			for (IPTPreCheckProblem wIPTPreCheckProblem : wDataList) {
				wTaskStepList = SFCTaskStepDAO.getInstance().SelectListByType(wLoginUser,
						SFCTaskStepType.Question.getValue(), wIPTPreCheckProblem.ID, wErrorCode);
				if (wTaskStepList != null && wTaskStepList.size() > 0) {
					SFCTaskStepDAO.getInstance().DeleteList(wLoginUser, wTaskStepList, wErrorCode);
				}
				wOMSOrder = wOrderMap.containsKey(wIPTPreCheckProblem.OrderID)
						? wOrderMap.get(wIPTPreCheckProblem.OrderID)
						: new OMSOrder();

				SFCTaskStep wSFCTaskStep = new SFCTaskStep();
				wSFCTaskStep.TaskStepID = wIPTPreCheckProblem.ID;
				wSFCTaskStep.CreateTime = Calendar.getInstance();
				wSFCTaskStep.EndTime = wBaseTime;
				wSFCTaskStep.ShiftID = wShiftID;
				wSFCTaskStep.WorkHour = 0.0D;
				wSFCTaskStep.OperatorID = wIPTPreCheckProblem.DoPersonID;
				wSFCTaskStep.ReadyTime = Calendar.getInstance();
				wSFCTaskStep.MonitorID = wLoginUser.ID;
				wSFCTaskStep.Type = SFCTaskStepType.Question.getValue();
				wNewID = ((Integer) (SFC_UpdateTaskStep(wLoginUser, wSFCTaskStep)).Result).intValue();

				BFCMessage wMessage = new BFCMessage();
				wMessage.Active = 0;
				wMessage.CompanyID = 0;
				wMessage.CreateTime = Calendar.getInstance();
				wMessage.EditTime = Calendar.getInstance();
				wMessage.ID = 0L;
				wMessage.MessageID = wNewID;
				wMessage.Title = StringUtils.Format("??????????????? {0} {1}",
						new Object[] { wIPTPreCheckProblem.IPTItemName, wOMSOrder.PartNo });
				wMessage.MessageText = StringUtils.Format("????????????????????? {0}?????????{1},?????????????????????!",
						new Object[] { wLoginUser.Name, wIPTPreCheckProblem.IPTItemName });
				wMessage.ModuleID = BPMEventModule.SCDispatching.getValue();
				wMessage.ResponsorID = wIPTPreCheckProblem.DoPersonID;
				wMessage.ShiftID = wShiftID;
				wMessage.StationID = 0L;
				wMessage.StepID = 1;
				wMessage.Type = BFCMessageType.Task.getValue();
				wMessageList.add(wMessage);

				wIPTPreCheckProblem.IsDischarged = true;
				QMSServiceImpl.getInstance().IPT_UpdatePreCheckProblem(wLoginUser, wIPTPreCheckProblem);
			}

			CoreServiceImpl.getInstance().BFC_UpdateMessageList(wLoginUser, wMessageList);

			wResult.Result = Integer.valueOf(wNewID);
		} catch (Exception e) {
			wResult.FaultCode = String.valueOf(wResult.FaultCode) + e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<SFCOrderForm> SFC_QueryOrderForm(BMSEmployee wLoginUser, int wID) {
		ServiceResult<SFCOrderForm> wResult = new ServiceResult<>();
		wResult.Result = new SFCOrderForm();
		OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
		try {
			wResult.Result = SFCOrderFormDAO.getInstance().SelectByID(wLoginUser, wID, wErrorCode);
			if (wResult.Result != null && (wResult.Result).ID > 0) {
				OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, (wResult.Result).OrderID,
						wErrorCode);
				if (wOrder != null && wOrder.ID > 0) {
					(wResult.Result).LineName = wOrder.LineName;
					(wResult.Result).ProductNo = wOrder.ProductNo;
				}
			}
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			wResult.FaultCode = String.valueOf(wResult.FaultCode) + e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<List<SFCOrderForm>> SFC_QueryCompleteConfirmList(BMSEmployee wLoginUser) {
		ServiceResult<List<SFCOrderForm>> wResult = new ServiceResult<>();
		wResult.Result = new ArrayList<>();
		OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
		try {
			wResult.Result = SFCOrderFormDAO.getInstance().SelectList(wLoginUser, -1, -1, "", 1, null, wErrorCode);

			if (wResult.Result != null && (wResult.Result).size() > 0) {
				List<Integer> wRemovedList = new ArrayList<Integer>();
				for (SFCOrderForm wSFCOrderForm : wResult.Result) {
					OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wSFCOrderForm.OrderID,
							wErrorCode);
					if (wOrder != null && wOrder.ID > 0) {
						wSFCOrderForm.LineName = wOrder.LineName;
						wSFCOrderForm.ProductNo = wOrder.ProductNo;
						wSFCOrderForm.CustomerName = wOrder.BureauSection;
					}
					// ?????????????????????????????????
					if (wOrder.Status == OMSOrderStatus.CarSended.getValue()) {
						wRemovedList.add(wOrder.ID);
					}
				}
				for (Integer wOrderID : wRemovedList) {
					wResult.Result.removeIf(p -> p.OrderID == wOrderID);
				}
			}
		} catch (Exception e) {
			wResult.FaultCode = String.valueOf(wResult.FaultCode) + e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> SFC_CompleteConfirm(BMSEmployee wLoginUser, int wSFCOrderFormID,
			List<String> wImagePathList, String wRemark) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			SFCOrderForm wOrderForm = SFCOrderFormDAO.getInstance().SelectByID(wLoginUser, wSFCOrderFormID, wErrorCode);
			if (wOrderForm == null || wOrderForm.ID <= 0) {
				wResult.FaultCode += RetCode.SERVER_RST_ERROR_OUT;
				return wResult;
			}

			OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wOrderForm.OrderID, wErrorCode);
			if (wOrder == null || wOrder.ID <= 0) {
				wResult.FaultCode += RetCode.SERVER_RST_ERROR_OUT;
				return wResult;
			}

			if (wOrder.Status != OMSOrderStatus.Repairing.getValue()) {
				wResult.FaultCode += "???????????????????????????????????????????????????!";
				return wResult;
			}

//			List<FPCProductRoute> wFPCProductRouteList = FMCServiceImpl.getInstance()
//					.FPC_QueryProductRouteList(wLoginUser, 0, 0, 0).List(FPCProductRoute.class);
			List<FPCRoutePart> wFPCRoutePartList = FMCServiceImpl.getInstance()
					.FPC_QueryRoutePartListByRouteID(wLoginUser, 0).List(FPCRoutePart.class);
			List<FPCRoutePartPoint> wFPCRoutePartPointList = FMCServiceImpl.getInstance()
					.FPC_QueryRoutePartPointListByRouteID(wLoginUser, 0, 0).List(FPCRoutePartPoint.class);

			List<Integer> wPartIDList = APSUtils.getInstance().APS_GetPartIDListByOrder(wLoginUser, wOrder,
					wFPCRoutePartList);
			if (wPartIDList == null || wPartIDList.size() <= 0) {
				wResult.FaultCode += "?????????????????????????????????????????????!";
				return wResult;
			}

			// ??????????????????
			List<FPCPart> wQTFinals = FMCServiceImpl.getInstance()
					.FPC_QueryPartList(wLoginUser, -1, -1, -1, FPCPartTypes.QTFinally.getValue()).List(FPCPart.class);

			int wRouteID = APSConstans.GetFPCRoute(wOrder.ProductID, wOrder.LineID, wOrder.CustomerID).ID;

			for (int wPartID : wPartIDList) {
				// ??????????????????????????????????????????
				List<APSTaskPart> wTaskPartList = APSTaskPartDAO.getInstance().SelectList(wLoginUser, -1, wOrder.ID, -1,
						wOrder.LineID, wPartID, 1, APSShiftPeriod.Week.getValue(), null, -1, null, null, wErrorCode);
				if (wTaskPartList == null || wTaskPartList.size() <= 0) {
					wResult.FaultCode += StringUtils.Format("????????????{0}????????????????????????!", APSConstans.GetFPCPart(wPartID).Name);
					return wResult;
				}
				// ??????????????????????????????????????????ID??????
				List<Integer> wPartPointIDList = SFC_GetPartPointIDList(wFPCRoutePartList, wFPCRoutePartPointList,
						wPartID, wRouteID);
				if (wPartPointIDList == null || wPartPointIDList.size() <= 0) {
					wResult.FaultCode += StringUtils.Format("????????????{0}?????????????????????!", APSConstans.GetFPCPart(wPartID).Name);
					return wResult;
				}
				// ???????????????????????????????????????????????????
				List<APSTaskStep> wTaskStepList = APSTaskStepDAO.getInstance().SelectList(wLoginUser, -1, -1, -1,
						wTaskPartList.get(0).ID, -1, -1, -1, -1, -1, 1, null, null, null, null, wErrorCode);
				if (wTaskStepList == null || wTaskStepList.size() <= 0) {
					wResult.FaultCode += StringUtils.Format("????????????{0}???????????????????????????!", APSConstans.GetFPCPart(wPartID).Name);
					return wResult;
				}
				// ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
				if (!wPartPointIDList.stream().allMatch(p -> wTaskStepList.stream()
						.anyMatch(q -> p == q.StepID && q.Status == APSTaskStatus.Done.getValue()))) {
					Integer wStepID = wPartPointIDList.stream()
							.filter(p -> !(wTaskStepList.stream()
									.anyMatch(q -> p == q.StepID && q.Status == APSTaskStatus.Done.getValue())))
							.findFirst().get();
					wResult.FaultCode += StringUtils.Format("????????????{0}???????????????{1}??????????????????!",
							APSConstans.GetFPCPart(wPartID).Name, APSConstans.GetFPCPartPointName(wStepID));
					return wResult;
				}
				// ???????????????????????????????????????????????????????????????????????????????????????
				if (wQTFinals != null && wQTFinals.size() > 0
						&& wQTFinals.stream().anyMatch(p -> p.QTPartID == wPartID)) {
					List<FPCPart> wTempPartList = wQTFinals.stream().filter(p -> p.QTPartID == wPartID)
							.collect(Collectors.toList());
					for (FPCPart wItem : wTempPartList) {
						// ??????????????????????????????
						List<Integer> wQTStepIDList = APSUtils.getInstance().FMC_QueryStepIDList(wLoginUser,
								wOrder.LineID, wItem.ID, wOrder.ProductID);
						if (wQTStepIDList == null || wQTStepIDList.size() <= 0) {
							wResult.FaultCode += StringUtils.Format("????????????{0}?????????????????????!",
									APSConstans.GetFPCPartName(wItem.ID));
							return wResult;
						}
						for (int wStepID : wQTStepIDList) {
							List<APSTaskStep> wQTStepTaskList = APSTaskStepDAO.getInstance().SelectList(wLoginUser, -1,
									wOrder.ID, -1, -1, -1, wOrder.LineID, wItem.ID, wStepID, -1, 1, null, null, null,
									null, wErrorCode);
							if (wQTStepTaskList == null || wQTStepIDList.size() <= 0) {
								wResult.FaultCode += StringUtils.Format("????????????{0}???????????????{1}???????????????????????????!",
										APSConstans.GetFPCPartName(wItem.ID));
								return wResult;
							}
							if (wQTStepTaskList.stream().anyMatch(p -> p.Status != APSTaskStatus.Done.getValue())) {
								wResult.FaultCode += StringUtils.Format("????????????{0}???????????????{1}??????????????????!",
										APSConstans.GetFPCPartName(wItem.ID));
								return wResult;
							}
							// ???????????????????????????????????????????????????
							String wCheckResult = this.IsSendRepair(wLoginUser, wOrder, wItem.ID, wStepID);
							if (StringUtils.isNotEmpty(wCheckResult)) {
								wResult.FaultCode += wCheckResult;
								return wResult;
							}
						}
					}
				}
			}

			// ?????????????????????????????????????????????????????????
			int wNCRNumber = AndonDAO.getInstance().SelectNcrCountNF(wLoginUser, wOrder.ID, wErrorCode);
			if (wNCRNumber > 0) {
				wResult.FaultCode += "???????????????????????????????????????????????????!";
				return wResult;
			}
			// ????????????????????????????????????????????????
			int wRepairNumber = AndonDAO.getInstance().SelectRepairCountNF(wLoginUser, wOrder.ID, wErrorCode);
			if (wRepairNumber > 0) {
				wResult.FaultCode += "??????????????????????????????????????????!";
				return wResult;
			}

			// ????????????????????????????????????????????????????????????
			wOrderForm.ConfirmID = wLoginUser.ID;
			wOrderForm.ConfirmTime = Calendar.getInstance();
			wOrderForm.Status = 2;
			if (wImagePathList != null && wImagePathList.size() > 0) {
				wOrderForm.ImagePathList = wImagePathList;
			}
			if (StringUtils.isNotEmpty(wRemark)) {
				wOrderForm.Remark = wRemark;
			}
			SFCOrderFormDAO.getInstance().Update(wLoginUser, wOrderForm, wErrorCode);

			wOrder.Status = OMSOrderStatus.FinishedWork.getValue();
			wOrder.RealFinishDate = Calendar.getInstance();
			OMSOrderDAO.getInstance().Update(BaseDAO.SysAdmin, wOrder, wErrorCode);
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * ?????????????????????????????????????????????????????????????????????????????????
	 * 
	 * @param wLoginUser
	 * @param wOrder
	 * @param wPartID
	 * @param wStepID
	 * @return
	 */
	private String IsSendRepair(BMSEmployee wLoginUser, OMSOrder wOrder, int wPartID, int wStepID) {
		String wResult = "";
		try {
			List<SFCTaskIPT> wIPTList = QMSServiceImpl.getInstance().SFC_QueryTaskIPTList(wLoginUser, wOrder.ID)
					.List(SFCTaskIPT.class);
			if (wIPTList == null || wIPTList.size() <= 0) {
				return wResult;
			}
			List<SFCTaskIPT> wTaskIPTList = wIPTList.stream().filter(
					p -> p.PartID == wPartID && p.PartPointID == wStepID && p.TaskType == SFCTaskType.Final.getValue())
					.collect(Collectors.toList());
			if (wTaskIPTList == null || wTaskIPTList.size() <= 0) {
				return wResult;
			}

			for (SFCTaskIPT wSFCTaskIPT : wTaskIPTList) {
				List<IPTValue> wValueList = QMSServiceImpl.getInstance().IPT_QueryValueList(wLoginUser, wSFCTaskIPT.ID)
						.List(IPTValue.class);
				if (wValueList == null || wValueList.size() <= 0) {
					continue;
				}
				List<IPTValue> wNotQualityValues = wValueList.stream().filter(p -> p.Result == 2)
						.collect(Collectors.toList());
				if (wNotQualityValues == null || wNotQualityValues.size() <= 0) {
					continue;
				}

				for (IPTValue wIPTValue : wNotQualityValues) {
					if (!MyHelperServiceImpl.getInstance().WDW_IsSendRepair(wLoginUser, wSFCTaskIPT.ID,
							(int) wIPTValue.IPTItemID).Result) {
						wResult = StringUtils.Format("????????????{0}?????????????????????????????????????????????!", wSFCTaskIPT.PartPointName);
						return wResult;
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * ??????????????????????????????????????????ID??????
	 * 
	 * @param wFPCProductRouteList   ??????????????????
	 * @param wFPCRoutePartList      ??????????????????
	 * @param wFPCRoutePartPointList ??????????????????
	 * @param wPartID                ??????ID
	 * @param wLineName              ??????
	 * @param wProductNo             ????????????
	 * @return ??????ID??????
	 */
	private List<Integer> SFC_GetPartPointIDList(List<FPCRoutePart> wFPCRoutePartList,
			List<FPCRoutePartPoint> wFPCRoutePartPointList, int wPartID, int wRouteID) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
//			Optional<FPCProductRoute> wOption = wFPCProductRouteList.stream()
//					.filter(p -> p.Line.equals(wLineName) && p.ProductNo.equals(wProductNo)).findFirst();
//			if (!wOption.isPresent()) {
//				return wResult;
//			}
//
//			FPCProductRoute wFPCProductRoute = wOption.get();

			Optional<FPCRoutePart> wPartOption = wFPCRoutePartList.stream()
					.filter(p -> p.RouteID == wRouteID && p.PartID == wPartID).findFirst();
			if (!wPartOption.isPresent()) {
				return wResult;
			}

			FPCRoutePart wFPCRoutePart = wPartOption.get();

			List<FPCRoutePartPoint> wList = wFPCRoutePartPointList.stream()
					.filter(p -> p.PartID == wFPCRoutePart.PartID && p.RouteID == wRouteID)
					.collect(Collectors.toList());

			if (wList == null || wList.size() <= 0) {
				return wResult;
			}

			wResult = wList.stream().map(p -> p.PartPointID).collect(Collectors.toList());
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	public ServiceResult<SFCOrderForm> SFC_QueryOrderForm(BMSEmployee wLoginUser, int wOrderID, int wType) {
		ServiceResult<SFCOrderForm> wResult = new ServiceResult<>();
		wResult.Result = new SFCOrderForm();
		OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
		try {
			List<SFCOrderForm> wList = SFCOrderFormDAO.getInstance().SelectList(wLoginUser, -1, wOrderID, "", wType,
					null, wErrorCode);
			if (wList != null && wList.size() > 0) {
				for (SFCOrderForm wSFCOrderForm : wList) {
					OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wSFCOrderForm.OrderID,
							wErrorCode);
					if (wOrder == null || wOrder.ID <= 0) {
						continue;
					}
					wSFCOrderForm.LineName = wOrder.LineName;
					wSFCOrderForm.ProductNo = wOrder.ProductNo;
					wSFCOrderForm.CustomerName = wOrder.Customer;
				}

				wResult.Result = wList.get(0);
			} else {
				OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wOrderID, wErrorCode);
				if (wOrder == null || wOrder.ID <= 0) {
					return wResult;
				}

				wResult.Result = new SFCOrderForm();
				wResult.Result.ID = 0;
				wResult.Result.LineName = wOrder.LineName;
				wResult.Result.ProductNo = wOrder.ProductNo;
				wResult.Result.CustomerName = wOrder.Customer;
				wResult.Result.PartNo = wOrder.PartNo;
				wResult.Result.OrderNo = wOrder.OrderNo;
				wResult.Result.OrderID = wOrderID;
			}

			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			wResult.FaultCode = String.valueOf(wResult.FaultCode) + e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<List<SFCSequentialInfo>> SFC_QuerySequentialInfo(BMSEmployee wLoginUser, int wOrderID) {
		ServiceResult<List<SFCSequentialInfo>> wResult = new ServiceResult<>();
		wResult.Result = new ArrayList<>();
		OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
		try {
			OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wOrderID, wErrorCode);
			if (wOrder == null || wOrder.ID <= 0) {
				wResult.FaultCode = String.valueOf(wResult.FaultCode) + "??????????????????????????????!";
				return wResult;
			}
			SFCSequentialInfo wSFCSequentialInfo = null;

			List<FMCWorkCharge> wWorkChargeList = APSConstans.GetFMCWorkChargeList();
			List<FMCWorkCharge> wClassChargeList = new ArrayList<>((wWorkChargeList.stream()
					.collect(Collectors.toMap(FMCWorkCharge::getStationID, account -> account, (k1, k2) -> k2)))
							.values());
			Map<Integer, List<Integer>> wStationPersonMap = new HashMap<>();
			for (FMCWorkCharge wFMCWorkCharge : wClassChargeList) {
				List<FMCWorkCharge> wTempList = (List<FMCWorkCharge>) wWorkChargeList.stream()
						.filter(p -> (p.StationID == wFMCWorkCharge.StationID)).collect(Collectors.toList());
				List<Integer> wIntegerList = new ArrayList<>();
				for (FMCWorkCharge wItem : wTempList) {
					List<BMSEmployee> wEmployeeList = (List<BMSEmployee>) APSConstans.GetBMSEmployeeList().values()
							.stream()
							.filter(p -> (p.DepartmentID == wItem.ClassID
									&& (APSConstans.GetBMSPosition(p.Position)).DutyID == 1))
							.collect(Collectors.toList());
					if (wEmployeeList == null || wEmployeeList.size() <= 0) {
						continue;
					}
					wIntegerList.addAll((Collection<? extends Integer>) wEmployeeList.stream()
							.map(p -> Integer.valueOf(p.ID)).collect(Collectors.toList()));
				}
				wStationPersonMap.put(Integer.valueOf(wFMCWorkCharge.StationID), wIntegerList);
			}

			List<SFCOrderForm> wInPlantList = SFCOrderFormDAO.getInstance().SelectList(wLoginUser, -1, wOrderID, "",
					SFCOrderFormType.InConfirm.getValue(), new ArrayList<>(Arrays.asList(Integer.valueOf(2))),
					wErrorCode);
			if (wInPlantList != null && wInPlantList.size() > 0) {
				String wText = (wInPlantList.get(0)).Remark;
				wSFCSequentialInfo = new SFCSequentialInfo(0, wOrderID, wOrder.OrderNo, wOrder.PartNo,
						new ArrayList<>(Arrays.asList(wInPlantList.get(0).ConfirmID)), wInPlantList.get(0).ConfirmName,
						SFCSequentialInfoType.Inplant.getValue(), SFCSequentialInfoType.Inplant.getLable(), wText, 0,
						"", 0, "", wInPlantList.get(0).ConfirmTime, wInPlantList.get(0).ConfirmTime);
				((List<SFCSequentialInfo>) wResult.Result).add(wSFCSequentialInfo);
			}

			List<APSTaskPart> wTaskPartList = APSTaskPartDAO.getInstance().SelectList(wLoginUser, -1, wOrderID, -1, -1,
					-1, 1, APSShiftPeriod.Week.getValue(), null, -1, null, null, wErrorCode);
			for (APSTaskPart wAPSTaskPart : wTaskPartList) {
				List<Integer> wOperatorList = new ArrayList<>();
				List<String> wNames = new ArrayList<>();
				if (wStationPersonMap.containsKey(Integer.valueOf(wAPSTaskPart.PartID))) {
					wOperatorList = wStationPersonMap.get(Integer.valueOf(wAPSTaskPart.PartID));
					for (Integer wMonitorID : wOperatorList) {
						wNames.add(APSConstans.GetBMSEmployeeName(wMonitorID.intValue()));
					}
				}
				wSFCSequentialInfo = new SFCSequentialInfo(0, wOrderID, wOrder.OrderNo, wOrder.PartNo, wOperatorList,
						StringUtils.Join(",", wNames), SFCSequentialInfoType.StationTask.getValue(),
						SFCSequentialInfoType.StationTask.getLable(), "", wAPSTaskPart.PartID, wAPSTaskPart.PartName, 0,
						"", wAPSTaskPart.StartWorkTime, wAPSTaskPart.FinishWorkTime);
				((List<SFCSequentialInfo>) wResult.Result).add(wSFCSequentialInfo);
			}

			List<APSTaskStep> wAPSTaskStepList = APSTaskStepDAO.getInstance().SelectList(wLoginUser, -1, wOrderID, -1,
					-1, -1, -1, -1, -1, -1, 1, null, null, null, null, wErrorCode);
			for (APSTaskStep wAPSTaskStep : wAPSTaskStepList) {
				List<String> wStrList = new ArrayList<>();
				for (Integer wItem : wAPSTaskStep.OperatorList) {
					wStrList.add(APSConstans.GetBMSEmployeeName(wItem.intValue()));
				}
				wSFCSequentialInfo = new SFCSequentialInfo(0, wOrderID, wOrder.OrderNo, wOrder.PartNo,
						wAPSTaskStep.OperatorList, StringUtils.Join(",", wStrList),
						SFCSequentialInfoType.StepTask.getValue(), SFCSequentialInfoType.StepTask.getLable(), "",
						wAPSTaskStep.PartID, wAPSTaskStep.PartName, wAPSTaskStep.StepID, wAPSTaskStep.StepName,
						wAPSTaskStep.StartTime, wAPSTaskStep.EndTime);
				((List<SFCSequentialInfo>) wResult.Result).add(wSFCSequentialInfo);
			}

			List<SFCTaskIPT> wSFCTaskIPTList = QMSServiceImpl.getInstance().SFC_QueryTaskIPTList(wLoginUser, wOrderID)
					.List(SFCTaskIPT.class);
			if (wSFCTaskIPTList != null && wSFCTaskIPTList.size() > 0) {

				List<SFCTaskIPT> wIPTList = (List<SFCTaskIPT>) wSFCTaskIPTList.stream()
						.filter(p -> (p.TaskType == SFCTaskType.SelfCheck.getValue())).collect(Collectors.toList());
				if (wIPTList != null && wIPTList.size() > 0) {
					for (SFCTaskIPT wSFCTaskIPT : wIPTList) {
						List<Integer> wPIDList = wSFCTaskIPT.OperatorList;
						List<String> wPNames = new ArrayList<>();
						for (Integer wID : wPIDList) {
							wPNames.add(APSConstans.GetBMSEmployeeName(wID.intValue()));
						}
						wSFCSequentialInfo = new SFCSequentialInfo(0, wOrderID, wOrder.OrderNo, wOrder.PartNo, wPIDList,
								StringUtils.Join(",", wPNames), SFCSequentialInfoType.SelfCheck.getValue(),
								SFCSequentialInfoType.SelfCheck.getLable(), "", wSFCTaskIPT.StationID,
								wSFCTaskIPT.StationName, wSFCTaskIPT.PartPointID, wSFCTaskIPT.PartPointName,
								wSFCTaskIPT.StartTime, wSFCTaskIPT.EndTime);
						((List<SFCSequentialInfo>) wResult.Result).add(wSFCSequentialInfo);
					}
				}

				wIPTList = (List<SFCTaskIPT>) wSFCTaskIPTList.stream()
						.filter(p -> (p.TaskType == SFCTaskType.MutualCheck.getValue())).collect(Collectors.toList());
				if (wIPTList != null && wIPTList.size() > 0) {
					for (SFCTaskIPT wSFCTaskIPT : wIPTList) {
						List<Integer> wPIDList = wSFCTaskIPT.OperatorList;
						List<String> wPNames = new ArrayList<>();
						for (Integer wID : wPIDList) {
							wPNames.add(APSConstans.GetBMSEmployeeName(wID.intValue()));
						}
						wSFCSequentialInfo = new SFCSequentialInfo(0, wOrderID, wOrder.OrderNo, wOrder.PartNo, wPIDList,
								StringUtils.Join(",", wPNames), SFCSequentialInfoType.MutualCheck.getValue(),
								SFCSequentialInfoType.MutualCheck.getLable(), "", wSFCTaskIPT.StationID,
								wSFCTaskIPT.StationName, wSFCTaskIPT.PartPointID, wSFCTaskIPT.PartPointName,
								wSFCTaskIPT.StartTime, wSFCTaskIPT.EndTime);
						((List<SFCSequentialInfo>) wResult.Result).add(wSFCSequentialInfo);
					}
				}

				wIPTList = (List<SFCTaskIPT>) wSFCTaskIPTList.stream()
						.filter(p -> (p.TaskType == SFCTaskType.SpecialCheck.getValue())).collect(Collectors.toList());
				if (wIPTList != null && wIPTList.size() > 0) {
					for (SFCTaskIPT wSFCTaskIPT : wIPTList) {
						List<Integer> wPIDList = wSFCTaskIPT.OperatorList;
						List<String> wPNames = new ArrayList<>();
						for (Integer wID : wPIDList) {
							wPNames.add(APSConstans.GetBMSEmployeeName(wID.intValue()));
						}
						wSFCSequentialInfo = new SFCSequentialInfo(0, wOrderID, wOrder.OrderNo, wOrder.PartNo, wPIDList,
								StringUtils.Join(",", wPNames), SFCSequentialInfoType.SpecialCheck.getValue(),
								SFCSequentialInfoType.SpecialCheck.getLable(), "", wSFCTaskIPT.StationID,
								wSFCTaskIPT.StationName, wSFCTaskIPT.PartPointID, wSFCTaskIPT.PartPointName,
								wSFCTaskIPT.StartTime, wSFCTaskIPT.EndTime);
						((List<SFCSequentialInfo>) wResult.Result).add(wSFCSequentialInfo);
					}
				}

				wIPTList = (List<SFCTaskIPT>) wSFCTaskIPTList.stream()
						.filter(p -> (p.TaskType == SFCTaskType.Final.getValue())).collect(Collectors.toList());
				if (wIPTList != null && wIPTList.size() > 0) {
					for (SFCTaskIPT wSFCTaskIPT : wIPTList) {
						List<Integer> wPIDList = wSFCTaskIPT.OperatorList;
						List<String> wPNames = new ArrayList<>();
						for (Integer wID : wPIDList) {
							wPNames.add(APSConstans.GetBMSEmployeeName(wID.intValue()));
						}
						wSFCSequentialInfo = new SFCSequentialInfo(0, wOrderID, wOrder.OrderNo, wOrder.PartNo, wPIDList,
								StringUtils.Join(",", wPNames), SFCSequentialInfoType.FinalCheck.getValue(),
								SFCSequentialInfoType.FinalCheck.getLable(), "", wSFCTaskIPT.StationID,
								wSFCTaskIPT.StationName, wSFCTaskIPT.PartPointID, wSFCTaskIPT.PartPointName,
								wSFCTaskIPT.StartTime, wSFCTaskIPT.EndTime);
						((List<SFCSequentialInfo>) wResult.Result).add(wSFCSequentialInfo);
					}
				}

				wIPTList = (List<SFCTaskIPT>) wSFCTaskIPTList.stream()
						.filter(p -> (p.TaskType == SFCTaskType.OutPlant.getValue())).collect(Collectors.toList());
				if (wIPTList != null && wIPTList.size() > 0) {
					for (SFCTaskIPT wSFCTaskIPT : wIPTList) {
						List<Integer> wPIDList = wSFCTaskIPT.OperatorList;
						List<String> wPNames = new ArrayList<>();
						for (Integer wID : wPIDList) {
							wPNames.add(APSConstans.GetBMSEmployeeName(wID.intValue()));
						}
						wSFCSequentialInfo = new SFCSequentialInfo(0, wOrderID, wOrder.OrderNo, wOrder.PartNo, wPIDList,
								StringUtils.Join(",", wPNames), SFCSequentialInfoType.OutCheck.getValue(),
								SFCSequentialInfoType.OutCheck.getLable(), "", wSFCTaskIPT.StationID,
								wSFCTaskIPT.StationName, wSFCTaskIPT.PartPointID, wSFCTaskIPT.PartPointName,
								wSFCTaskIPT.StartTime, wSFCTaskIPT.EndTime);
						((List<SFCSequentialInfo>) wResult.Result).add(wSFCSequentialInfo);
					}
				}
			}

			List<SFCOrderForm> wCompleteList = SFCOrderFormDAO.getInstance().SelectList(wLoginUser, -1, wOrderID, "",
					SFCOrderFormType.CompleteConfirm.getValue(), new ArrayList<>(Arrays.asList(Integer.valueOf(2))),
					wErrorCode);
			if (wCompleteList != null && wCompleteList.size() > 0) {
				String wText = (wCompleteList.get(0)).Remark;
				wSFCSequentialInfo = new SFCSequentialInfo(0, wOrderID, wOrder.OrderNo, wOrder.PartNo,
						new ArrayList<>(Arrays.asList(wCompleteList.get(0).ConfirmID)),
						wCompleteList.get(0).ConfirmName, SFCSequentialInfoType.CompleteConfirm.getValue(),
						SFCSequentialInfoType.CompleteConfirm.getLable(), wText, 0, "", 0, "",
						wCompleteList.get(0).CreateTime, wCompleteList.get(0).ConfirmTime);
				((List<SFCSequentialInfo>) wResult.Result).add(wSFCSequentialInfo);
			}

			List<SFCOrderForm> wOutList = SFCOrderFormDAO.getInstance().SelectList(wLoginUser, -1, wOrderID, "",
					SFCOrderFormType.OutApply.getValue(), new ArrayList<>(Arrays.asList(Integer.valueOf(2))),
					wErrorCode);
			if (wOutList != null && wOutList.size() > 0) {
				String wText = (wOutList.get(0)).Remark;
				wSFCSequentialInfo = new SFCSequentialInfo(0, wOrderID, wOrder.OrderNo, wOrder.PartNo,
						new ArrayList<>(Arrays.asList(wOutList.get(0).ConfirmID)), wOutList.get(0).ConfirmName,
						SFCSequentialInfoType.OutPlant.getValue(), SFCSequentialInfoType.OutPlant.getLable(), wText, 0,
						"", 0, "", wOutList.get(0).CreateTime, wOutList.get(0).ConfirmTime);
				((List<SFCSequentialInfo>) wResult.Result).add(wSFCSequentialInfo);
			}

			if (wResult.Result != null && (wResult.Result).size() > 0) {
				(wResult.Result).removeIf(p -> (p == null));
				(wResult.Result).sort(Comparator.comparing(SFCSequentialInfo::getStartTime)
						.thenComparing(SFCSequentialInfo::getType));

				int wIndex = 1;
				for (SFCSequentialInfo wItem : wResult.Result) {
					wItem.ID = wIndex++;
				}
			}

			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			wResult.FaultCode = String.valueOf(wResult.FaultCode) + e.toString();
			e.printStackTrace();
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<List<SFCTaskStep>> SFC_QueryTaskStepList(BMSEmployee wLoginUser,
			List<Integer> wTaskStepIDList) {
		ServiceResult<List<SFCTaskStep>> wResult = new ServiceResult<>();
		wResult.Result = new ArrayList<>();
		OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
		try {
			wResult.Result = SFCTaskStepDAO.getInstance().SelectListByTaskStepIDList(wLoginUser, wTaskStepIDList, -1,
					wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			wResult.FaultCode = String.valueOf(wResult.FaultCode) + e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> SFC_DeleteList(BMSEmployee wLoginUser, List<SFCTaskStep> wList) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			wResult = SFCTaskStepDAO.getInstance().DeleteList(wLoginUser, wList, wErrorCode);
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<SFCBOMTask>> SFC_QueryBOMTaskHistory(BMSEmployee wLoginUser, int wID, String wCode,
			int wUpFlowID, int wFollowerID, int wOrderID, int wPartID, int wPartPointID, int wBOMID, int wLevel,
			Calendar wStartTime, Calendar wEndTime) {
		ServiceResult<List<SFCBOMTask>> wResult = new ServiceResult<List<SFCBOMTask>>();
		wResult.Result = new ArrayList<SFCBOMTask>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			wResult.Result = SFCBOMTaskDAO.getInstance().SelectList(wLoginUser, wID, wCode, wUpFlowID, "", wOrderID,
					wPartID, wPartPointID, wBOMID, wLevel, null, wStartTime, wEndTime, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<BPMTaskBase>> SFC_QueryBOMTaskEmployeeAll(BMSEmployee wLoginUser, int wTagTypes,
			Calendar wStartTime, Calendar wEndTime) {
		ServiceResult<List<BPMTaskBase>> wResult = new ServiceResult<List<BPMTaskBase>>();
		wResult.Result = new ArrayList<BPMTaskBase>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			switch (TagTypes.getEnumType(wTagTypes)) {
			case Applicant:// 2??????
				wResult.Result = SFCBOMTaskDAO.getInstance().BPM_GetSendTaskList(wLoginUser, wLoginUser.ID, wStartTime,
						wEndTime, wErrorCode);
				break;
			case Dispatcher:// 1??????
				wResult.Result = SFCBOMTaskDAO.getInstance().BPM_GetUndoTaskList(wLoginUser, wLoginUser.ID, wErrorCode);
				break;
			case Approver:// 4??????
				wResult.Result = SFCBOMTaskDAO.getInstance().BPM_GetDoneTaskList(wLoginUser, wLoginUser.ID, wStartTime,
						wEndTime, wErrorCode);
				break;
			default:
				break;
			}

			if (wResult.Result.size() > 0) {
				wResult.Result = wResult.Result.stream().filter(p -> p.Status != 0).collect(Collectors.toList());
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<SFCBOMTask> SFC_SubmitBOMTask(BMSEmployee wLoginUser, SFCBOMTask wTask) {
		ServiceResult<SFCBOMTask> wResult = new ServiceResult<SFCBOMTask>();
		wResult.Result = new SFCBOMTask();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			if (wTask == null || wTask.ID <= 0) {
				return wResult;
			}

			// ????????????????????????
			if (wTask.Status == 2) {
				ExecutorService wES = Executors.newFixedThreadPool(1);
				wES.submit(() -> SaveMaterialHistory(wLoginUser, wTask));
				wES.shutdown();
			}

			if (wTask.Status == SFCBOMTaskStatus.NomalClose.getValue()) {
				wTask.StatusText = SFCBOMTaskStatus.NomalClose.getLable();
			} else if (wTask.Status == SFCBOMTaskStatus.ExceptionClose.getValue()) {
				wTask.StatusText = SFCBOMTaskStatus.ExceptionClose.getLable();
			}

			SimpleDateFormat wSDF = new SimpleDateFormat("yyyyMMdd");
			String wShiftID = wSDF.format(Calendar.getInstance().getTime());

			if (wTask.Status == 66) {
				wTask.ConfirmedLevels += "C";
				wTask.SFCBOMTaskItemList.forEach(p -> {
					if (p.Level == 3 && p.Status != 2) {
						p.ShiftID = wShiftID;
					}
				});
			} else if (wTask.Status == 100) {
				wTask.ConfirmedLevels += "B";
				wTask.SFCBOMTaskItemList.forEach(p -> {
					if (p.Level == 2 && p.Status != 2) {
						p.ShiftID = wShiftID;
					}
				});
			} else if (wTask.Status == 155) {
				wTask.ConfirmedLevels += "A";
				wTask.SFCBOMTaskItemList.forEach(p -> {
					if (p.Level == 1 && p.Status != 2) {
						p.ShiftID = wShiftID;
					}
				});
			}

			// ?????????????????????????????????
			if (wTask.Status == 31) {
				wTask.SFCBOMTaskItemList.forEach(p -> {
					if (p.Level == 1) {
						p.Status = 2;
					}
				});
			} else if (wTask.Status == 32) {
				wTask.SFCBOMTaskItemList.forEach(p -> {
					if (p.Level == 2) {
						p.Status = 2;
					}
				});
			} else if (wTask.Status == 33) {
				wTask.SFCBOMTaskItemList.forEach(p -> {
					if (p.Level == 3) {
						p.Status = 2;
					}
				});
			}

			wResult.Result = (SFCBOMTask) SFCBOMTaskDAO.getInstance().BPM_UpdateTask(wLoginUser, wTask, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());

			// ??????????????????SAP
			if (wTask.Status == SFCBOMTaskStatus.NomalClose.getValue() && wTask.MaterialID > 0) {
				OMSOrder wOMSOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wTask.OrderID, wErrorCode);

				APSBOMItem wAPSBOMItem = CreateAPSBOMItem(wLoginUser, wTask, wErrorCode, wOMSOrder);

				long startTime = System.currentTimeMillis();
				logger.info("APS_UpdateBOMItem:");

				APSServiceImpl.getInstance().APS_UpdateBOMItem(wLoginUser, wAPSBOMItem);

				long endTime = System.currentTimeMillis();
				logger.info("????????????????????? " + (endTime - startTime) + "ms");
			}

			// ??????????????????SAP(??????????????????)
			if (wTask.Status == 66) {
				List<SFCBOMTaskItem> wItemList = wTask.SFCBOMTaskItemList.stream()
						.filter(p -> p.Level == 3 && p.Status != 2).collect(Collectors.toList());
				SynchronizedToSap(wLoginUser, wTask, wErrorCode, wItemList);
			} else if (wTask.Status == 100) {
				List<SFCBOMTaskItem> wItemList = wTask.SFCBOMTaskItemList.stream()
						.filter(p -> p.Level == 2 && p.Status != 2).collect(Collectors.toList());
				SynchronizedToSap(wLoginUser, wTask, wErrorCode, wItemList);
			} else if (wTask.Status == 155) {
				List<SFCBOMTaskItem> wItemList = wTask.SFCBOMTaskItemList.stream()
						.filter(p -> p.Level == 1 && p.Status != 2).collect(Collectors.toList());
				SynchronizedToSap(wLoginUser, wTask, wErrorCode, wItemList);
			}

			// ???????????????????????????
			if (wTask.Status == 3) {
				ExecutorService wES = Executors.newFixedThreadPool(1);
				wES.submit(() -> MaterialAutomaticRating(wTask));
				wES.shutdown();
			}

			// ?????????????????????
			if (wTask.Status == 4) {
				ExecutorService wES = Executors.newFixedThreadPool(1);
				wES.submit(() -> MaterialLevelBackWrite(wTask));
				wES.shutdown();
			}
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * ????????????????????????
	 */
	private void MaterialLevelBackWrite(SFCBOMTask wTask) {
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			// ????????????????????????????????????????????????
			for (SFCBOMTaskItem wSFCBOMTaskItem : wTask.SFCBOMTaskItemList) {
				int wLevel = SFCBOMTaskDAO.getInstance().SFC_QueryLevelByMaterialID(BaseDAO.SysAdmin,
						wSFCBOMTaskItem.MaterialID, wErrorCode);
				if (wLevel > 0) {
					continue;
				}

				SFCBOMTaskDAO.getInstance().SFC_UpdateMaterialLevel(BaseDAO.SysAdmin, wSFCBOMTaskItem.Level,
						wSFCBOMTaskItem.MaterialID, wErrorCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * ??????????????????
	 */
	private void MaterialAutomaticRating(SFCBOMTask wTask) {
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			// ????????????????????????????????????????????????
			for (SFCBOMTaskItem wSFCBOMTaskItem : wTask.SFCBOMTaskItemList) {
				int wLevel = SFCBOMTaskDAO.getInstance().SFC_QueryLevelByMaterialID(BaseDAO.SysAdmin,
						wSFCBOMTaskItem.MaterialID, wErrorCode);
				if (wLevel <= 0) {
					continue;
				}
				wSFCBOMTaskItem.Level = wLevel;
				SFCBOMTaskItemDAO.getInstance().Update(BaseDAO.SysAdmin, wSFCBOMTaskItem, wErrorCode);
			}
			// ????????????????????????????????????????????????????????????
			if (wTask.SFCBOMTaskItemList.stream().anyMatch(p -> p.Level <= 0)) {
				return;
			}
			// ???LevelA???LevelB???LevelC??????
			if (wTask.SFCBOMTaskItemList.stream().anyMatch(p -> p.Level == 1)) {
				wTask.LevelA = true;
			}
			if (wTask.SFCBOMTaskItemList.stream().anyMatch(p -> p.Level == 2)) {
				wTask.LevelB = true;
			}
			if (wTask.SFCBOMTaskItemList.stream().anyMatch(p -> p.Level == 3)) {
				wTask.LevelC = true;
			}
			// ?????????????????????
			wTask.Status = 4;

			List<BPMActivitiHisTask> wNewTaskList = BPMServiceImpl.getInstance()
					.BPM_GetHistoryInstanceByID(BaseDAO.SysAdmin, wTask.FlowID).List(BPMActivitiHisTask.class);
			wNewTaskList = wNewTaskList.stream().filter(p -> p.Status == 0).collect(Collectors.toList());

			CoreServiceImpl.getInstance().QMS_CompleteInstance(BaseDAO.SysAdmin, wTask, wNewTaskList.get(0).ID)
					.Info(SFCBOMTask.class);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * ????????????????????????
	 */
	private void SaveMaterialHistory(BMSEmployee wLoginUser, SFCBOMTask wTask) {
		try {
			if (wTask.SFCBOMTaskItemList == null || wTask.SFCBOMTaskItemList.size() <= 0
					|| wTask.SFCBOMTaskItemList.stream().allMatch(p -> p.BOMItemID > 0)) {
				return;
			}

			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			List<SFCBOMTaskItem> wList = wTask.SFCBOMTaskItemList.stream().filter(p -> p.BOMItemID <= 0)
					.collect(Collectors.toList());
			for (SFCBOMTaskItem wSFCBOMTaskItem : wList) {
				List<MSSBOMItemHistory> wSavedList = MSSBOMItemHistoryDAO.getInstance().SelectList(wLoginUser, -1,
						wTask.ProductID, wTask.LineID, wTask.CustomerID, wTask.PartID, wTask.PartPointID,
						wSFCBOMTaskItem.MaterialID, wErrorCode);
				if (wSavedList != null && wSavedList.size() > 0) {
					continue;
				}

				// ????????????
				MSSMaterial wMaterial = APSBOMItemDAO.getInstance().GetMaterialByID(wLoginUser,
						wSFCBOMTaskItem.MaterialID, wErrorCode);

				MSSBOMItemHistory wHistory = new MSSBOMItemHistory(0, wTask.ProductID, wTask.LineID, wTask.CustomerID,
						wTask.PartID, wTask.PartPointID, wSFCBOMTaskItem.MaterialID, wMaterial.MaterialNo,
						wMaterial.MaterialName, 0, wSFCBOMTaskItem.UnitID, wSFCBOMTaskItem.UnitText);
				MSSBOMItemHistoryDAO.getInstance().Update(wLoginUser, wHistory, wErrorCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	private void SynchronizedToSap(BMSEmployee wLoginUser, SFCBOMTask wTask, OutResult<Integer> wErrorCode,
			List<SFCBOMTaskItem> wItemList) {
		try {
			OMSOrder wOMSOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wTask.OrderID, wErrorCode);

			List<APSBOMItem> wList = CreateAPSBOMItemList(wLoginUser, wTask, wErrorCode, wOMSOrder, wItemList);

			if (wList.size() > 0) {
				ExecutorService wES = Executors.newFixedThreadPool(1);
				wES.submit(() -> APSServiceImpl.getInstance().APS_UpdateBOMItemList(wLoginUser, wList, wTask.ID));
				wES.shutdown();
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * ????????????bom??????
	 */
	private List<APSBOMItem> CreateAPSBOMItemList(BMSEmployee wLoginUser, SFCBOMTask wTask,
			OutResult<Integer> wErrorCode, OMSOrder wOMSOrder, List<SFCBOMTaskItem> wItemList) {
		List<APSBOMItem> wResult = new ArrayList<APSBOMItem>();
		try {
			if (wItemList == null || wItemList.size() <= 0) {
				return wResult;
			}

			for (SFCBOMTaskItem wSFCBOMTaskItem : wItemList) {
				APSBOMItem wAPSBOMItem = null;

				int wOutSourceType = 0;
				int wReplaceType = 0;
				if (wSFCBOMTaskItem.BOMItemID > 0) {
					// ?????????Core?????????
					MSSBOMItem wItem = WMSServiceImpl.getInstance()
							.MSS_QueryBOMItemByID(wLoginUser, wSFCBOMTaskItem.BOMItemID).Info(MSSBOMItem.class);
					wOutSourceType = wItem.OutsourceType;
					wReplaceType = wItem.ReplaceType;
				}
				wAPSBOMItem = new APSBOMItem();
				wAPSBOMItem.ID = 0;
				wAPSBOMItem.BOMType = 2;
				wAPSBOMItem.FactoryID = 1900;
				wAPSBOMItem.WBSNo = wOMSOrder.OrderNo;
				wAPSBOMItem.OrderID = wOMSOrder.ID;
				wAPSBOMItem.PartNo = wOMSOrder.PartNo;
				wAPSBOMItem.LineID = wOMSOrder.LineID;
				wAPSBOMItem.ProductID = wOMSOrder.ProductID;
				wAPSBOMItem.CustomerID = wOMSOrder.CustomerID;
				wAPSBOMItem.PartID = wTask.PartID;
				wAPSBOMItem.PartPointID = wTask.PartPointID;
				wAPSBOMItem.MaterialID = wSFCBOMTaskItem.MaterialID;
				wAPSBOMItem.MaterialNo = wSFCBOMTaskItem.MaterialNo;
				wAPSBOMItem.Number = wSFCBOMTaskItem.MaterialNumber;
				wAPSBOMItem.UnitID = APSBOMItemDAO.getInstance().GetUnitID(wLoginUser, wSFCBOMTaskItem.MaterialID,
						wErrorCode);
				wAPSBOMItem.ReplaceType = 2;
				if (wReplaceType > 0) {
					wAPSBOMItem.ReplaceType = wReplaceType;
				}
				wAPSBOMItem.OutsourceType = wOutSourceType;
				// ?????????????????????????????????????????????
				if (wOutSourceType == 2) {
					wAPSBOMItem.PartChange = 1;
				}
				wAPSBOMItem.StockID = 1200;
				wAPSBOMItem.Status = 1;

				if (wSFCBOMTaskItem.SapType == 1) {
					wAPSBOMItem.AssessmentType = "????????????";
				} else if (wSFCBOMTaskItem.SapType == 2) {
					wAPSBOMItem.AssessmentType = "????????????";
				} else if (wSFCBOMTaskItem.SapType == 3) {
					wAPSBOMItem.AssessmentType = "????????????";
				} else if (wSFCBOMTaskItem.SapType == 4) {
					wAPSBOMItem.AssessmentType = "???????????????";
				}
				wAPSBOMItem.SourceType = APSBOMSourceType.SFCBOMTask.getValue();
				wAPSBOMItem.SourceID = wSFCBOMTaskItem.ID;
				wAPSBOMItem.AuditTime = Calendar.getInstance();
				wAPSBOMItem.EditTime = Calendar.getInstance();

				// ?????????????????????
				if (StringUtils.isNotEmpty(wSFCBOMTaskItem.QualityLossBig)
						|| StringUtils.isNotEmpty(wSFCBOMTaskItem.QualityLossSmall)) {
					wAPSBOMItem.BOMType = 9;
					wAPSBOMItem.QTType = GetQTType(wSFCBOMTaskItem.QualityLossBig);
					wAPSBOMItem.QTItemType = GetQTItemType(wSFCBOMTaskItem.QualityLossSmall);
				}

				if (wAPSBOMItem != null)
					wResult.add(wAPSBOMItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * ????????????????????????
	 */
	private int GetQTItemType(String qualityLossSmall) {
		int wResult = 0;
		try {
			switch (qualityLossSmall) {
			case "01":
				wResult = 1;
				break;
			case "02":
				wResult = 2;
				break;
			case "03":
				wResult = 3;
				break;
			case "04":
				wResult = 4;
				break;
			case "05":
				wResult = 5;
				break;
			default:
				break;
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * ????????????????????????
	 */
	private int GetQTType(String qualityLossBig) {
		int wResult = 0;
		try {
			switch (qualityLossBig) {
			case "001":
				wResult = 1;
				break;
			case "002":
				wResult = 2;
				break;
			case "003":
				wResult = 3;
				break;
			case "004":
				wResult = 4;
				break;
			default:
				break;
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@SuppressWarnings("unused")
	private APSBOMItem CreateAPSBOMItem(BMSEmployee wLoginUser, SFCBOMTask wTask, OutResult<Integer> wErrorCode,
			OMSOrder wOMSOrder) {
		APSBOMItem wAPSBOMItem = null;
		try {
			if (wTask.BOMItemID > 0) {
				// ?????????Core?????????
				MSSBOMItem wItem = WMSServiceImpl.getInstance().MSS_QueryBOMItemByID(wLoginUser, wTask.BOMItemID)
						.Info(MSSBOMItem.class);
				wItem.MaterialNumber = wTask.MaterialNumber;
				wAPSBOMItem = new APSBOMItem(wItem, wOMSOrder.LineID, wOMSOrder.ProductID, wOMSOrder.BureauSectionID,
						wOMSOrder.ID, wOMSOrder.OrderNo, wOMSOrder.PartNo);
			} else {
				wAPSBOMItem = new APSBOMItem();
				wAPSBOMItem.ID = 0;
				wAPSBOMItem.BOMType = 2;
				wAPSBOMItem.FactoryID = 1900;
				wAPSBOMItem.WBSNo = wOMSOrder.OrderNo;
				wAPSBOMItem.OrderID = wOMSOrder.ID;
				wAPSBOMItem.PartNo = wOMSOrder.PartNo;
				wAPSBOMItem.LineID = wOMSOrder.LineID;
				wAPSBOMItem.ProductID = wOMSOrder.ProductID;
				wAPSBOMItem.CustomerID = wOMSOrder.CustomerID;
				wAPSBOMItem.PartID = wTask.PartID;
				wAPSBOMItem.PartPointID = wTask.PartPointID;
				wAPSBOMItem.MaterialID = wTask.MaterialID;
				wAPSBOMItem.MaterialNo = wTask.MaterialNo;
				wAPSBOMItem.Number = wTask.MaterialNumber;
				wAPSBOMItem.UnitID = APSBOMItemDAO.getInstance().GetUnitID(wLoginUser, wTask.MaterialID, wErrorCode);
				wAPSBOMItem.ReplaceType = 2;
				wAPSBOMItem.StockID = 1200;
				wAPSBOMItem.Status = 1;
			}

			if (wTask.SapType == 1) {
				wAPSBOMItem.AssessmentType = "????????????";
			} else if (wTask.SapType == 2) {
				wAPSBOMItem.AssessmentType = "????????????";
			}
			wAPSBOMItem.SourceType = APSBOMSourceType.SFCBOMTask.getValue();
			wAPSBOMItem.SourceID = wTask.ID;
			wAPSBOMItem.AuditTime = Calendar.getInstance();
			wAPSBOMItem.EditTime = Calendar.getInstance();
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wAPSBOMItem;
	}

	/**
	 * ???????????????????????????
	 */
	@SuppressWarnings("unused")
	private void LevelHandle(BMSEmployee wLoginUser, SFCBOMTask wTask, OutResult<Integer> wErrorCode) {
		try {
			if (wTask.Status != 50) {
				return;
			}

			if (wTask.SFCBOMTaskItemList == null || wTask.SFCBOMTaskItemList.size() <= 0) {
				return;
			}

			if (wTask.SFCBOMTaskItemList.stream().allMatch(p -> p.Level == 1)
					|| wTask.SFCBOMTaskItemList.stream().allMatch(p -> p.Level == 2)
					|| wTask.SFCBOMTaskItemList.stream().allMatch(p -> p.Level == 3)) {
				return;
			}

			// A??????
			List<SFCBOMTaskItem> wAList = wTask.SFCBOMTaskItemList.stream().filter(p -> p.Level == 1)
					.collect(Collectors.toList());
			// B??????
			List<SFCBOMTaskItem> wBList = wTask.SFCBOMTaskItemList.stream().filter(p -> p.Level == 2)
					.collect(Collectors.toList());
			// C??????
			List<SFCBOMTaskItem> wCList = wTask.SFCBOMTaskItemList.stream().filter(p -> p.Level == 3)
					.collect(Collectors.toList());

			// ???A???????????????
			if (wAList.size() > 0) {
				// ?????????????????????????????????
				SFCBOMTaskItemDAO.getInstance().DeleteList(wLoginUser, wTask.SFCBOMTaskItemList, wErrorCode);

				// ????????????
				for (SFCBOMTaskItem wItem : wAList) {
					wItem.ID = 0;
					wItem.SFCBOMTaskID = wTask.ID;
					SFCBOMTaskItemDAO.getInstance().Update(wLoginUser, wItem, wErrorCode);
				}

				wTask.SFCBOMTaskItemList = wAList;
				wTask.Level = 1;
				if (wBList.size() > 0) {
					CopyProcess(wLoginUser, wBList, wTask, 2, wErrorCode);
				}
				if (wCList.size() > 0) {
					CopyProcess(wLoginUser, wCList, wTask, 3, wErrorCode);
				}
			} else {
				// ?????????????????????????????????
				SFCBOMTaskItemDAO.getInstance().DeleteList(wLoginUser, wTask.SFCBOMTaskItemList, wErrorCode);
				// ???A???????????????
				wTask.SFCBOMTaskItemList = wBList;

				// ????????????
				for (SFCBOMTaskItem wItem : wBList) {
					wItem.ID = 0;
					wItem.SFCBOMTaskID = wTask.ID;
					SFCBOMTaskItemDAO.getInstance().Update(wLoginUser, wItem, wErrorCode);
				}

				wTask.Level = 2;
				if (wCList.size() > 0) {
					CopyProcess(wLoginUser, wCList, wTask, 3, wErrorCode);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * ??????????????????????????????
	 */
	private void CopyProcess(BMSEmployee wLoginUser, List<SFCBOMTaskItem> wList, SFCBOMTask wTask, int wLevel,
			OutResult<Integer> wErrorCode) {
		try {
			// ?????????????????????
			List<BPMActivitiHisTask> wHisList = BPMServiceImpl.getInstance()
					.BPM_GetHistoryInstanceByID(wLoginUser, wTask.FlowID).List(BPMActivitiHisTask.class);
			wHisList.removeIf(p -> p.Status == 0);
			// ?????????

			BMSEmployee wUser = CloneTool.Clone(APSConstans.GetBMSEmployee(wTask.UpFlowID), BMSEmployee.class);
			wUser.LoginName = DesUtil.encrypt(wUser.LoginName, BaseDAO.appSecret);
			wUser.Password = DesUtil.encrypt("123456", BaseDAO.appSecret);

			APIResult wAPiResult = CoreServiceImpl.getInstance().QMS_StartInstance(wUser, "8201");
			List<BPMActivitiTask> wBPMActivitiTask = wAPiResult.List(BPMActivitiTask.class);
			SFCBOMTask wData = wAPiResult.Custom("data", SFCBOMTask.class);

			SFCBOMTask wCloneItem = CloneTool.Clone(wTask, SFCBOMTask.class);

			wCloneItem.SFCBOMTaskItemList = wList;

			wCloneItem.ID = wData.ID;
			wCloneItem.Code = wData.Code;
			wCloneItem.Status = 14;
			wCloneItem.FlowID = wData.FlowID;
			wCloneItem.AreaCharge = wHisList.get(1).Assignee;
			wCloneItem.Level = wLevel;

			wCloneItem = CoreServiceImpl.getInstance()
					.QMS_CompleteInstance(wUser, wCloneItem, wBPMActivitiTask.get(0).ID).Info(SFCBOMTask.class);

			// ????????????
			for (SFCBOMTaskItem wItem : wList) {
				wItem.ID = 0;
				wItem.SFCBOMTaskID = wCloneItem.ID;
				SFCBOMTaskItemDAO.getInstance().Update(wLoginUser, wItem, wErrorCode);
			}

			// ?????????1

			wUser = CloneTool.Clone(APSConstans.GetBMSEmployee(Integer.parseInt(wHisList.get(1).Assignee)),
					BMSEmployee.class);
			wUser.LoginName = DesUtil.encrypt(wUser.LoginName, BaseDAO.appSecret);
			wUser.Password = DesUtil.encrypt("123456", BaseDAO.appSecret);

			wCloneItem.Status = 25;

			List<BPMActivitiHisTask> wNewTaskList = BPMServiceImpl.getInstance()
					.BPM_GetHistoryInstanceByID(wLoginUser, wCloneItem.FlowID).List(BPMActivitiHisTask.class);
			wNewTaskList = wNewTaskList.stream().filter(p -> p.Status == 0).collect(Collectors.toList());

			wCloneItem = CoreServiceImpl.getInstance().QMS_CompleteInstance(wUser, wCloneItem, wNewTaskList.get(0).ID)
					.Info(SFCBOMTask.class);

			// ?????????2
			wUser = CloneTool.Clone(APSConstans.GetBMSEmployee(Integer.parseInt(wHisList.get(2).Assignee)),
					BMSEmployee.class);
			wUser.LoginName = DesUtil.encrypt(wUser.LoginName, BaseDAO.appSecret);
			wUser.Password = DesUtil.encrypt("123456", BaseDAO.appSecret);

			wCloneItem.Status = 1;

			wNewTaskList = BPMServiceImpl.getInstance().BPM_GetHistoryInstanceByID(wLoginUser, wCloneItem.FlowID)
					.List(BPMActivitiHisTask.class);
			wNewTaskList = wNewTaskList.stream().filter(p -> p.Status == 0).collect(Collectors.toList());

			wCloneItem = CoreServiceImpl.getInstance().QMS_CompleteInstance(wUser, wCloneItem, wNewTaskList.get(0).ID)
					.Info(SFCBOMTask.class);

			// ?????????3
			wUser = CloneTool.Clone(APSConstans.GetBMSEmployee(Integer.parseInt(wHisList.get(3).Assignee)),
					BMSEmployee.class);
			wUser.LoginName = DesUtil.encrypt(wUser.LoginName, BaseDAO.appSecret);
			wUser.Password = DesUtil.encrypt("123456", BaseDAO.appSecret);

			wCloneItem.Status = 50;

			wNewTaskList = BPMServiceImpl.getInstance().BPM_GetHistoryInstanceByID(wLoginUser, wCloneItem.FlowID)
					.List(BPMActivitiHisTask.class);
			wNewTaskList = wNewTaskList.stream().filter(p -> p.Status == 0).collect(Collectors.toList());

			wCloneItem = CoreServiceImpl.getInstance().QMS_CompleteInstance(wUser, wCloneItem, wNewTaskList.get(0).ID)
					.Info(SFCBOMTask.class);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	@Override
	public ServiceResult<SFCBOMTask> SFC_QueryDefaultBOMTask(BMSEmployee wLoginUser, int wEventID) {
		ServiceResult<SFCBOMTask> wResult = new ServiceResult<SFCBOMTask>();
		wResult.Result = new SFCBOMTask();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			List<SFCBOMTask> wList = SFCBOMTaskDAO.getInstance().SelectList(wLoginUser, -1, "", wLoginUser.ID, "", -1,
					-1, -1, -1, -1, new ArrayList<Integer>(Arrays.asList(0)), null, null, wErrorCode);
			if (wList != null && wList.size() > 0) {
				wResult.Result = wList.get(0);
//				wResult.Result.Code = SFCBOMTaskDAO.getInstance().GetNewCode(wLoginUser, wErrorCode);
			}
			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public synchronized ServiceResult<SFCBOMTask> SFC_CreateBOMTask(BMSEmployee wLoginUser, BPMEventModule wEventID) {
		ServiceResult<SFCBOMTask> wResult = new ServiceResult<SFCBOMTask>();
		wResult.Result = new SFCBOMTask();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			SFCBOMTask wItem = new SFCBOMTask();
			wItem.ID = 0;
			wItem.Code = SFCBOMTaskDAO.getInstance().GetNewCode(wLoginUser, wErrorCode);
			wItem.UpFlowID = wLoginUser.ID;
			wItem.UpFlowName = wLoginUser.Name;
			wItem.FlowType = BPMEventModule.OccasionNCR.getValue();
			wItem.Status = 0;
			wItem.Level = NCRLevel.Default.getValue();
			wItem.CreateTime = Calendar.getInstance();
			wItem.SubmitTime = Calendar.getInstance();
			wItem.Responsibility = 0;
			wItem.ReviewComments = 0;
			wResult.Result = (SFCBOMTask) SFCBOMTaskDAO.getInstance().BPM_UpdateTask(wLoginUser, wItem, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<SFCBOMTask> SFC_GetBOMTask(BMSEmployee wLoginUser, int wID) {
		ServiceResult<SFCBOMTask> wResult = new ServiceResult<SFCBOMTask>();
		wResult.Result = new SFCBOMTask();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			wResult.Result = (SFCBOMTask) SFCBOMTaskDAO.getInstance().BPM_GetTaskInfo(wLoginUser, wID, "", wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<SFCStationPerson> SFC_QueryStationPerson(BMSEmployee wLoginUser, int wStationID) {
		ServiceResult<SFCStationPerson> wResult = new ServiceResult<SFCStationPerson>();
		wResult.Result = new SFCStationPerson();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			// ???????????????????????????
			List<LFSWorkAreaStation> wASList = APSConstans.GetLFSWorkAreaStationList().values().stream()
					.filter(p -> p.Active == 1 && p.StationID == wStationID).collect(Collectors.toList());
			// ???????????????????????????
			List<FMCWorkCharge> wWCList = APSConstans.GetFMCWorkChargeList().stream()
					.filter(p -> p.Active == 1 && p.StationID == wStationID).collect(Collectors.toList());
			// ?????????????????????
			List<LFSWorkAreaChecker> wWorkCheckerList = APSConstans.GetLFSWorkAreaCheckerList();
			if (wASList.size() > 0) {
				wResult.Result.AreaID = wASList.get(0).WorkAreaID;
				wResult.Result.AreaName = wASList.get(0).WorkArea;
				if (wWorkCheckerList.stream()
						.anyMatch(p -> p.WorkAreaID == wResult.Result.AreaID && p.WorkAreaID > 0)) {
					LFSWorkAreaChecker wChecker = wWorkCheckerList.stream()
							.filter(p -> p.WorkAreaID == wResult.Result.AreaID && p.WorkAreaID > 0).findFirst().get();
					wResult.Result.AuditorList = wChecker.LeaderIDList;
					if (wChecker.LeaderIDList != null && wChecker.LeaderIDList.size() > 0) {
						wResult.Result.Auditors = this.GetNames(wLoginUser, wChecker.LeaderIDList);
					}
				}
			}

			// ????????????(?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????)
			SetAuditors(wLoginUser, wResult, wErrorCode, wWCList, wWorkCheckerList);

			// ??????????????????
			if (wWCList.size() > 0) {
				List<Integer> wTechIDList = new ArrayList<Integer>();
				for (FMCWorkCharge wFMCWorkCharge : wWCList) {
					if (wFMCWorkCharge.TechnicianList == null || wFMCWorkCharge.TechnicianList.size() <= 0) {
						continue;
					}
					wTechIDList.addAll(wFMCWorkCharge.TechnicianList);
				}
				if (wTechIDList.size() > 0) {
					wTechIDList = wTechIDList.stream().distinct().collect(Collectors.toList());

					// ????????????????????????
					wTechIDList = wTechIDList.stream()
							.filter(p -> APSConstans.GetBMSEmployeeList().values().stream().anyMatch(q -> q.ID == p))
							.collect(Collectors.toList());

					wResult.Result.TechnicianList = wTechIDList;
					wResult.Result.Technicians = this.GetNames(wLoginUser, wTechIDList);
				}
			}
			// ????????????
			wResult.Result.StationID = wStationID;
			wResult.Result.StationName = APSConstans.GetFPCPartName(wStationID);

			// ????????????
			if (wResult.Result.AuditorList == null || wResult.Result.AuditorList.size() <= 0) {
				List<BMSRoleItem> wUList = CoreServiceImpl.getInstance().BMS_UserAllByRoleID(wLoginUser, 24)
						.List(BMSRoleItem.class);

				wResult.Result.AuditorList = wUList.stream().map(p -> p.FunctionID).distinct()
						.collect(Collectors.toList());

				wResult.Result.Auditors = GetNames(wLoginUser, wResult.Result.AuditorList);
			}
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * ??????????????????
	 */
	private void SetAuditors(BMSEmployee wLoginUser, ServiceResult<SFCStationPerson> wResult,
			OutResult<Integer> wErrorCode, List<FMCWorkCharge> wWCList, List<LFSWorkAreaChecker> wWorkCheckerList) {
		try {
			List<Integer> wAllAuditorList = new ArrayList<Integer>();
			for (FMCWorkCharge wFMCWorkCharge : wWCList) {
				// ????????????????????????
				int wAreaID = SFCBOMTaskDAO.getInstance().GetAreaIDByClassID(wLoginUser, wFMCWorkCharge.ClassID,
						wErrorCode);
				// ??????????????????????????????
				if (wAreaID > 0 && wWorkCheckerList.stream().anyMatch(p -> p.WorkAreaID == wAreaID)) {
					List<Integer> wLList = wWorkCheckerList.stream().filter(p -> p.WorkAreaID == wAreaID).findFirst()
							.get().LeaderIDList;
					wAllAuditorList.addAll(wLList);
				}
			}
			wAllAuditorList = wAllAuditorList.stream().distinct().collect(Collectors.toList());
			// ?????????????????????????????????
			int wLoginAreaID = SFCBOMTaskDAO.getInstance().GetAreaIDByClassID(wLoginUser, wLoginUser.DepartmentID,
					wErrorCode);
			if (wLoginAreaID > 0 && wWorkCheckerList.stream().anyMatch(p -> p.WorkAreaID == wLoginAreaID)) {
				List<Integer> wLList = wWorkCheckerList.stream().filter(p -> p.WorkAreaID == wLoginAreaID).findFirst()
						.get().LeaderIDList;
				List<Integer> wAllAuditorListClone = wAllAuditorList;
				if (wLList.stream().allMatch(p -> wAllAuditorListClone.stream().anyMatch(q -> q == p))) {
					wResult.Result.AuditorList = wLList;
					wResult.Result.Auditors = this.GetNames(wLoginUser, wLList);
				}
			} else {
				wResult.Result.AuditorList = wAllAuditorList;
				wResult.Result.Auditors = this.GetNames(wLoginUser, wResult.Result.AuditorList);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * ????????????ID??????????????????
	 */
	private String GetNames(BMSEmployee wLoginUser, List<Integer> wIDList) {
		String wResult = "";
		try {
			if (wIDList == null || wIDList.size() <= 0) {
				return wResult;
			}

			List<String> wNames = new ArrayList<String>();
			for (Integer wID : wIDList) {
				String wName = APSConstans.GetBMSEmployeeName(wID);
				if (StringUtils.isEmpty(wName)) {
					continue;
				}
				wNames.add(wName);
			}

			if (wNames.size() > 0) {
				wResult = StringUtils.Join(",", wNames);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public synchronized ServiceResult<Integer> SFC_AddPerson(BMSEmployee wLoginUser, APSTaskStep wData,
			List<Integer> wPersonIDList) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			if (wData == null || wData.ID <= 0 || wPersonIDList == null || wPersonIDList.size() <= 0) {
				wResult.FaultCode += "?????????????????????!";
			}

			if (wData.OperatorList.stream().anyMatch(p -> wPersonIDList.stream().anyMatch(q -> q == p))) {
				wResult.FaultCode += "??????????????????????????????????????????????????????????????????????????????";
				return wResult;
			}

			// ??????????????????
			List<SFCTaskStep> wSFCTaskStepList = SFCTaskStepDAO.getInstance().SelectList(wLoginUser, -1, wData.ID, -1,
					-1, -1, null, -1, wErrorCode);
			wPersonIDList.removeIf(p -> wSFCTaskStepList.stream().anyMatch(q -> q.OperatorID == p.intValue()));

			// APS
			int wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, Calendar.getInstance(), APSShiftPeriod.Day,
					FMCShiftLevel.Day, 0);
			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1);

			List<BFCMessage> wMessageList = new ArrayList<BFCMessage>();
			for (Integer wPersonID : wPersonIDList) {
				SFCTaskStep wSFCTaskStep = new SFCTaskStep();
				wSFCTaskStep.TaskStepID = wData.ID;
				wSFCTaskStep.CreateTime = Calendar.getInstance();
				wSFCTaskStep.EndTime = wBaseTime;
				wSFCTaskStep.ShiftID = wShiftID;
				wSFCTaskStep.WorkHour = 0.0D;
				wSFCTaskStep.OperatorID = wPersonID;
				wSFCTaskStep.ReadyTime = Calendar.getInstance();
				wSFCTaskStep.MonitorID = wLoginUser.ID;
				wSFCTaskStep.Type = (wData.TaskPartID > 0) ? SFCTaskStepType.Step.getValue()
						: SFCTaskStepType.Quality.getValue();
				int wNewID = ((Integer) (SFC_UpdateTaskStep(wLoginUser, wSFCTaskStep)).Result).intValue();

				this.CreateMessage(wLoginUser, wShiftID, wSFCTaskStep, wNewID, wMessageList, wData, wPersonID);
			}
			// ????????????
			CoreServiceImpl.getInstance().BFC_UpdateMessageList(wLoginUser, wMessageList);
			// ?????????????????????OperatorList
			if (wData.OperatorList == null) {
				wData.OperatorList = wPersonIDList;
			} else {
				wData.OperatorList.addAll(wPersonIDList);
			}
			APSTaskStepDAO.getInstance().Update(wLoginUser, wData, wErrorCode);

			// ??????????????????????????????
			ExecutorService wES = Executors.newFixedThreadPool(1);
			wES.submit(() -> GenerateMutualMessage(wLoginUser, wData, wErrorCode));
			wES.shutdown();
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * ????????????????????????????????????????????????????????????????????????
	 */
	private void GenerateMutualMessage(BMSEmployee wLoginUser, APSTaskStep wData, OutResult<Integer> wErrorCode) {
		try {
			// ??????????????????????????????
			int wMutualTaskID = SFCTaskStepDAO.getInstance().QueryMutualTaskID(wLoginUser, wData.ID, wErrorCode);
			if (wMutualTaskID <= 0) {
				return;
			}
			// ??????????????????????????????????????????
//			int wMessageCount = SFCTaskStepDAO.getInstance().QueryMutualMessageCount(wLoginUser, wMutualTaskID,
//					wErrorCode);
//			if (wMessageCount > 0) {
//				return;
//			}
			// ??????????????????????????????
			String wSelfOpes = SFCTaskStepDAO.getInstance().QuerySelfOpes(wLoginUser, wData.ID, wErrorCode);
			if (StringUtils.isEmpty(wSelfOpes)) {
				return;
			}
			// ??????????????????
			List<SFCTaskStep> wList = SFCTaskStepDAO.getInstance().SelectList(wLoginUser, -1, wData.ID, -1, -1, -1,
					null, -1, wErrorCode);
			wList.removeIf(p -> wSelfOpes.contains(String.valueOf(p.OperatorID)));
			if (wList.size() <= 0) {
				return;
			}
			// ???????????????????????????
			List<BFCMessage> wBFCMessageList = new ArrayList<BFCMessage>();
			BFCMessage wMessage = null;
			SimpleDateFormat wSDF = new SimpleDateFormat("yyyyMMdd");
			int wShiftID = Integer.parseInt(wSDF.format(Calendar.getInstance().getTime()));
			// ???????????????????????????????????????????????????????????????
			for (SFCTaskStep wSFCTaskStep : wList) {
				boolean wIsSend = SFCTaskStepDAO.getInstance().JudgeMessageIsSend(wLoginUser, wSFCTaskStep.OperatorID,
						wMutualTaskID, BPMEventModule.MutualCheck.getValue(), wErrorCode);
				if (wIsSend) {
					continue;
				}

				wMessage = new BFCMessage();
				wMessage.Active = 0;
				wMessage.CompanyID = 0;
				wMessage.CreateTime = Calendar.getInstance();
				wMessage.EditTime = Calendar.getInstance();
				wMessage.ID = 0;
				wMessage.MessageID = wMutualTaskID;
				wMessage.Title = StringUtils.Format("?????? {0} {1}", new Object[] { wData.StepName, wData.PartNo });
				wMessage.MessageText = StringUtils.Format("???{0}??? {1}????????????????????????{2}???",
						new Object[] { BPMEventModule.MutualCheck.getLable(), wLoginUser.Name, wData.StepName });
				wMessage.ModuleID = BPMEventModule.MutualCheck.getValue();
				wMessage.ResponsorID = wSFCTaskStep.OperatorID;
				wMessage.ShiftID = wShiftID;
				wMessage.StationID = wData.PartID;
				wMessage.Type = BFCMessageType.Task.getValue();
				wBFCMessageList.add(wMessage);
			}
			CoreServiceImpl.getInstance().BFC_UpdateMessageList(wLoginUser, wBFCMessageList);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	@Override
	public ServiceResult<SFCAttemptRun> SFC_QueryDefaultAttemptRun(BMSEmployee wLoginUser, int wEventID) {
		ServiceResult<SFCAttemptRun> wResult = new ServiceResult<SFCAttemptRun>();
		wResult.Result = new SFCAttemptRun();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			List<SFCAttemptRun> wList = SFCAttemptRunDAO.getInstance().SelectList(wLoginUser, -1, "", wLoginUser.ID, -1,
					-1, -1, new ArrayList<Integer>(Arrays.asList(0)), null, null, wErrorCode);
			if (wList.size() > 0) {
				wResult.Result = wList.get(0);
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public synchronized ServiceResult<SFCAttemptRun> SFC_CreateAttemptRun(BMSEmployee wLoginUser,
			BPMEventModule wEventID) {
		ServiceResult<SFCAttemptRun> wResult = new ServiceResult<SFCAttemptRun>();
		wResult.Result = new SFCAttemptRun();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			wResult.Result.Code = SFCAttemptRunDAO.getInstance().GetNewCode(wLoginUser, wErrorCode);
			wResult.Result.UpFlowID = wLoginUser.ID;
			wResult.Result.UpFlowName = wLoginUser.Name;
			wResult.Result.CreateTime = Calendar.getInstance();
			wResult.Result.SubmitTime = Calendar.getInstance();
			wResult.Result.ID = 0;
			wResult.Result.Status = SFCAttemptRunStatus.Default.getValue();
			wResult.Result.StatusText = "";
			wResult.Result.FlowType = wEventID.getValue();

			wResult.Result = (SFCAttemptRun) SFCAttemptRunDAO.getInstance().BPM_UpdateTask(wLoginUser, wResult.Result,
					wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<SFCAttemptRun> SFC_SubmitAttemptRun(BMSEmployee wLoginUser, SFCAttemptRun wData) {
		ServiceResult<SFCAttemptRun> wResult = new ServiceResult<SFCAttemptRun>();
		wResult.Result = new SFCAttemptRun();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			// ?????????????????????
			if (wData.ItemList != null && wData.ItemList.size() > 0) {
				for (SFCRepairItem wSFCRepairItem : wData.ItemList) {
					SFCRepairItemDAO.getInstance().Update(wLoginUser, wSFCRepairItem, wErrorCode);
				}
			}

			if (wData.OrderID > 0) {
				List<SFCRepairItem> wList = SFCRepairItemDAO.getInstance().SelectList(wLoginUser, -1, "", -1, -1,
						wData.ID, wErrorCode);
				if (wList == null || wList.size() <= 0) {
					// ???????????????????????????
					List<RROItemTask> wItemTaskList = WDWServiceImpl.getInstance()
							.RRO_QueryNotFinishItemList(wLoginUser, wData.OrderID).List(RROItemTask.class);
					if (wItemTaskList != null && wItemTaskList.size() > 0) {
						for (RROItemTask wRROItemTask : wItemTaskList) {
							SFCRepairItem wSFCRepairItem = new SFCRepairItem();
							wSFCRepairItem.Code = SFCRepairItemDAO.getInstance().GetNewCode(wLoginUser, wErrorCode);
							wSFCRepairItem.Detail = wRROItemTask.Remark;
							wSFCRepairItem.ID = 0;
							wSFCRepairItem.Name = wRROItemTask.Content;
							wSFCRepairItem.PartID = wRROItemTask.StationID;
							wSFCRepairItem.PartName = APSConstans.GetFPCPartName(wSFCRepairItem.PartID);
							wSFCRepairItem.Remark = "";
							wSFCRepairItem.Result = SFCRepairItemResult.NotFinish.getValue();
							wSFCRepairItem.TaskID = wData.ID;
							wSFCRepairItem.ItemTaskID = wRROItemTask.ID;
							wSFCRepairItem.TechnicianIDs = this.GetTechnicianIDs(wSFCRepairItem.PartID);
							wSFCRepairItem.TechnicianID = this.GetTechnicianID(wSFCRepairItem.PartID);
							wSFCRepairItem.TechnicianName = APSConstans.GetBMSEmployeeName(wSFCRepairItem.TechnicianID);

							SFCRepairItemDAO.getInstance().Update(wLoginUser, wSFCRepairItem, wErrorCode);
						}
					}
				}
			}

			if (wData.Status == SFCAttemptRunStatus.NomalClose.getValue()) {
				wData.StatusText = SFCAttemptRunStatus.NomalClose.getLable();
			}

			// ?????????????????????
			if (wData.Status == 3) {
				List<SFCRepairItem> wList = wData.ItemList.stream().filter(
						p -> p.TechnicianID == wLoginUser.ID && p.Result == SFCRepairItemResult.Submited.getValue())
						.collect(Collectors.toList());
				for (SFCRepairItem wSFCRepairItem : wList) {
					wSFCRepairItem.Result = SFCRepairItemResult.NotAgreen.getValue();
					SFCRepairItemDAO.getInstance().Update(wLoginUser, wSFCRepairItem, wErrorCode);
				}
			} else if (wData.Status == 4) {
				List<SFCRepairItem> wList = wData.ItemList.stream().filter(
						p -> p.TechnicianID == wLoginUser.ID && p.Result == SFCRepairItemResult.Submited.getValue())
						.collect(Collectors.toList());
				for (SFCRepairItem wSFCRepairItem : wList) {
					wSFCRepairItem.Result = SFCRepairItemResult.Agreen.getValue();
					SFCRepairItemDAO.getInstance().Update(wLoginUser, wSFCRepairItem, wErrorCode);
				}
			}

			wResult.Result = (SFCAttemptRun) SFCAttemptRunDAO.getInstance().BPM_UpdateTask(wLoginUser, wData,
					wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * ???????????????????????????????????????
	 */
	private String GetTechnicianIDs(int partID) {
		String wResult = "";
		try {
			FPCPart wPart = APSConstans.GetFPCPart(partID);

			if (wPart == null || wPart.ID <= 0 || wPart.TechnicianList == null || wPart.TechnicianList.size() <= 0) {
				return wResult;
			}

			wResult = wPart.TechnicianName;
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * ?????????????????????????????????
	 */
	private int GetTechnicianID(int partID) {
		int wResult = 0;
		try {
			FPCPart wPart = APSConstans.GetFPCPart(partID);

			if (wPart == null || wPart.ID <= 0 || wPart.TechnicianList == null || wPart.TechnicianList.size() <= 0) {
				return wResult;
			}

			wResult = wPart.TechnicianList.get(0);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<SFCAttemptRun> SFC_GetAttemptRun(BMSEmployee wLoginUser, int wID) {
		ServiceResult<SFCAttemptRun> wResult = new ServiceResult<SFCAttemptRun>();
		wResult.Result = new SFCAttemptRun();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			wResult.Result = (SFCAttemptRun) SFCAttemptRunDAO.getInstance().BPM_GetTaskInfo(wLoginUser, wID, "",
					wErrorCode);

			if (wResult.Result.Status == 2 || wResult.Result.Status == 3 || wResult.Result.Status == 4) {
				wResult.Result.ItemList.removeIf(p -> p.TechnicianID != wLoginUser.ID
						&& wResult.Result.ItemList.stream().anyMatch(q -> q.TechnicianID == wLoginUser.ID));
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<BPMTaskBase>> SFC_QueryAttemptRunEmployeeAll(BMSEmployee wLoginUser, int wTagTypes,
			Calendar wStartTime, Calendar wEndTime) {
		ServiceResult<List<BPMTaskBase>> wResult = new ServiceResult<List<BPMTaskBase>>();
		wResult.Result = new ArrayList<BPMTaskBase>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			switch (TagTypes.getEnumType(wTagTypes)) {
			case Applicant:// 2??????
				wResult.Result = SFCAttemptRunDAO.getInstance().BPM_GetSendTaskList(wLoginUser, wLoginUser.ID,
						wStartTime, wEndTime, wErrorCode);
				break;
			case Dispatcher:// 1??????
				wResult.Result = SFCAttemptRunDAO.getInstance().BPM_GetUndoTaskList(wLoginUser, wLoginUser.ID,
						wErrorCode);
				break;
			case Approver:// 4??????
				wResult.Result = SFCAttemptRunDAO.getInstance().BPM_GetDoneTaskList(wLoginUser, wLoginUser.ID,
						wStartTime, wEndTime, wErrorCode);
				break;
			default:
				break;
			}

			if (wResult.Result.size() > 0) {
				wResult.Result = wResult.Result.stream().filter(p -> p.Status != 0).collect(Collectors.toList());
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<SFCAttemptRun>> SFC_QueryAttemptRunHistory(BMSEmployee wLoginUser, int wID, String wCode,
			int wUpFlowID, int wFollowerID, int wOrderID, int wCheckerID, int wPartID, Calendar wStartTime,
			Calendar wEndTime) {
		ServiceResult<List<SFCAttemptRun>> wResult = new ServiceResult<List<SFCAttemptRun>>();
		wResult.Result = new ArrayList<SFCAttemptRun>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			wResult.Result = SFCAttemptRunDAO.getInstance().SelectList(wLoginUser, wUpFlowID, wCode, wUpFlowID,
					wOrderID, wCheckerID, wPartID, null, wStartTime, wEndTime, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> SFC_CloseMessage(BMSEmployee wLoginUser, int wID, BPMEventModule wEventID) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>();
		try {
			List<BFCMessage> wList = new ArrayList<BFCMessage>();

			List<BFCMessage> wList1 = CoreServiceImpl.getInstance()
					.BFC_GetMessageList(wLoginUser, -1, wEventID.getValue(), new ArrayList<Integer>(Arrays.asList(wID)),
							BFCMessageType.Task.getValue(), BFCMessageStatus.Default.getValue())
					.List(BFCMessage.class);

			if (wList1 != null && wList1.size() > 0) {
				wList.addAll(wList1);
			}

			List<BFCMessage> wList2 = CoreServiceImpl.getInstance()
					.BFC_GetMessageList(wLoginUser, -1, wEventID.getValue(), new ArrayList<Integer>(Arrays.asList(wID)),
							BFCMessageType.Task.getValue(), BFCMessageStatus.Sent.getValue())
					.List(BFCMessage.class);
			if (wList2 != null && wList2.size() > 0) {
				wList.addAll(wList2);
			}

			List<BFCMessage> wList3 = CoreServiceImpl.getInstance()
					.BFC_GetMessageList(wLoginUser, -1, wEventID.getValue(), new ArrayList<Integer>(Arrays.asList(wID)),
							BFCMessageType.Task.getValue(), BFCMessageStatus.Read.getValue())
					.List(BFCMessage.class);
			if (wList3 != null && wList3.size() > 0) {
				wList.addAll(wList3);
			}

			if (wList.size() > 0) {
				wList.forEach(p -> p.Active = BFCMessageStatus.Close.getValue());
				CoreServiceImpl.getInstance().BFC_UpdateMessageList(wLoginUser, wList);
			}
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Boolean> SFC_JugdeItem(BMSEmployee wLoginUser, int wOrderID, int wSFCAttemptRunID) {
		ServiceResult<Boolean> wResult = new ServiceResult<Boolean>();
		wResult.Result = true;
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			List<RROItemTask> wList = WDWServiceImpl.getInstance().RRO_QueryNotFinishItemList(wLoginUser, wOrderID)
					.List(RROItemTask.class);
			if (wList == null || wList.size() <= 0) {
				return wResult;
			}

			SFCAttemptRun wTask = (SFCAttemptRun) SFCAttemptRunDAO.getInstance().BPM_GetTaskInfo(wLoginUser,
					wSFCAttemptRunID, "", wErrorCode);
			if (wTask == null || wTask.ID <= 0 || wTask.ItemList == null || wTask.ItemList.size() <= 0) {
				wResult.Result = false;
				return wResult;
			}

			if (wTask.ItemList.stream().anyMatch(p -> wList.stream().anyMatch(q -> q.ID == p.ItemTaskID)
					&& p.Result != SFCRepairItemResult.Agreen.getValue())) {
				wResult.Result = false;
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public synchronized ServiceResult<Map<Integer, Integer>> SFC_JudgeAll(BMSEmployee wLoginUser, SFCAttemptRun wData) {
		ServiceResult<Map<Integer, Integer>> wResult = new ServiceResult<Map<Integer, Integer>>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		wResult.Result = new HashMap<Integer, Integer>();
		try {
			// ???1 ????????? ??????????????????????????????????????????????????????
			List<RROItemTask> wRROItemTaskList = new ArrayList<RROItemTask>();
			if (wData.OrderID > 0) {
				wRROItemTaskList = WDWServiceImpl.getInstance().RRO_QueryNotFinishItemList(wLoginUser, wData.OrderID)
						.List(RROItemTask.class);
			}

			List<SFCRepairItem> wRItemList = SFCRepairItemDAO.getInstance().SelectList(wLoginUser, -1, "", -1, -1,
					wData.ID, wErrorCode);

			// ???2 ????????? ????????????????????????????????? ?????????
			List<RROItemTask> wNewList = wRROItemTaskList.stream()
					.filter(p -> !wRItemList.stream().anyMatch(q -> q.ItemTaskID == p.ID)).collect(Collectors.toList());
			if (wNewList != null && wNewList.size() > 0) {
				for (RROItemTask wRROItemTask : wNewList) {
					SFCRepairItem wSFCRepairItem = new SFCRepairItem();
					wSFCRepairItem.Code = SFCRepairItemDAO.getInstance().GetNewCode(wLoginUser, wErrorCode);
					wSFCRepairItem.Detail = wRROItemTask.Remark;
					wSFCRepairItem.ID = 0;
					wSFCRepairItem.Name = wRROItemTask.Content;
					wSFCRepairItem.PartID = wRROItemTask.StationID;
					wSFCRepairItem.PartName = APSConstans.GetFPCPartName(wSFCRepairItem.PartID);
					wSFCRepairItem.Remark = "";
					wSFCRepairItem.Result = SFCRepairItemResult.NotFinish.getValue();
					wSFCRepairItem.TaskID = wData.ID;
					wSFCRepairItem.ItemTaskID = wRROItemTask.ID;
					wSFCRepairItem.TechnicianIDs = this.GetTechnicianIDs(wSFCRepairItem.PartID);
					wSFCRepairItem.TechnicianID = this.GetTechnicianID(wSFCRepairItem.PartID);
					wSFCRepairItem.TechnicianName = APSConstans.GetBMSEmployeeName(wSFCRepairItem.TechnicianID);

					wSFCRepairItem.ID = SFCRepairItemDAO.getInstance().Update(wLoginUser, wSFCRepairItem, wErrorCode);

					wRItemList.add(wSFCRepairItem);
				}
			}
			// ???3 ????????? ??????????????????????????????/?????????
			List<RROItemTask> wRROItemTaskList1 = wRROItemTaskList;
			List<SFCRepairItem> wFinishedList = wRItemList.stream()
					.filter(p -> !wRROItemTaskList1.stream().anyMatch(q -> q.ID == p.ItemTaskID))
					.collect(Collectors.toList());
			for (SFCRepairItem wSFCRepairItem : wFinishedList) {
				wSFCRepairItem.Result = SFCRepairItemResult.Finished.getValue();
				SFCRepairItemDAO.getInstance().Update(wLoginUser, wSFCRepairItem, wErrorCode);
			}
			// ???4 ????????????????????????
			List<SFCRepairItem> wList = wRItemList;
			List<SFCRepairItem> wNFList = wList.stream().filter(p -> p.Result != SFCRepairItemResult.Finished.getValue()
					&& p.Result != SFCRepairItemResult.Canceled.getValue()).collect(Collectors.toList());
			// ???5 ??????????????????????????????????????????
			if (wData.Status == 4 || wData.Status == 3) {
				// ???// 5 ??????????????????????????????????????????????????????
				wNFList.removeIf(
						p -> p.TechnicianID == wLoginUser.ID && p.Result == SFCRepairItemResult.Submited.getValue());
				// ???// 6 ?????????????????????????????????????????????
				if (wNFList.stream()
						.allMatch(p -> p.Result == SFCRepairItemResult.Agreen.getValue()
								|| p.Result == SFCRepairItemResult.Finished.getValue()
								|| p.Result == SFCRepairItemResult.Canceled.getValue())) {
					// ???// 8 ?????????????????? ????????? 3???2 ???4:1
					wResult.Result.put(3, 2);
					wResult.Result.put(4, 1);
				} else {
					// ???// 7 ???????????????????????? ??????????????? 3 ???2 ???4???2
					wResult.Result.put(3, 2);
					wResult.Result.put(4, 2);
				}
			} else if (wData.Status == 1) {
				if (wNFList == null || wNFList.size() <= 0) {
					wResult.Result.put(1, 1);
				} else {
					wResult.Result.put(1, 2);
				}
			} else {
				if (wNFList.stream().allMatch(p -> p.Result == SFCRepairItemResult.Agreen.getValue())) {
					wResult.Result.put(3, 2);
					wResult.Result.put(4, 1);
				} else {
					wResult.Result.put(3, 2);
					wResult.Result.put(4, 2);
				}
			}
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Boolean> SFC_JudgeIsSendApply(BMSEmployee wLoginUser, SFCAttemptRun wSFCAttemptRun) {
		ServiceResult<Boolean> wResult = new ServiceResult<Boolean>();
		wResult.Result = false;
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			if (wSFCAttemptRun.OrderID <= 0) {
				return wResult;
			}

			List<SFCAttemptRun> wList = SFCAttemptRunDAO.getInstance().SelectList(wLoginUser, -1, "", -1,
					wSFCAttemptRun.OrderID, -1, -1, null, null, null, wErrorCode);
			if (wList != null && wList.size() > 0 && wList.stream()
					.anyMatch(p -> p.ID != wSFCAttemptRun.ID && p.Status != SFCAttemptRunStatus.Canceled.getValue())) {
				wResult.Result = true;
			}
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> SFC_TaskStepSubmitAllNew(BMSEmployee wLoginUser, List<APSTaskStep> wAPSTaskStepList) {
		ServiceResult<Integer> wResult = new ServiceResult<>();
		OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
		try {
			if (wAPSTaskStepList == null || wAPSTaskStepList.size() <= 0) {
				return wResult;
			}

			if (wAPSTaskStepList.stream().anyMatch(p -> !(p.OperatorList != null && p.OperatorList.size() > 0))) {
				wResult.FaultCode = String.valueOf(wResult.FaultCode) + "?????????????????????????????????!";
				return wResult;
			}

			if (wAPSTaskStepList.stream().anyMatch(p -> p.Status != APSTaskStatus.Issued.getValue())) {
				wResult.FaultCode = String.valueOf(wResult.FaultCode) + "????????????????????????????????????????????????!";
				return wResult;
			}

			// ???????????????????????????
			wAPSTaskStepList
					.forEach(p -> p.OperatorList = p.OperatorList.stream().distinct().collect(Collectors.toList()));

			// ?????????????????????????????????
			for (APSTaskStep wAPSTaskStep : wAPSTaskStepList) {
				for (int wPersonID : wAPSTaskStep.OperatorList) {
					if (!APSTaskStepDAO.getInstance().JudgeCanDo(wLoginUser, wAPSTaskStep.ID, wPersonID, wErrorCode)) {
						wResult.FaultCode += StringUtils.Format("????????????{0}??????????????????{0}??????????????????????????????????????????????????????!",
								APSConstans.GetBMSEmployee(wPersonID), wAPSTaskStep.StepName);
						return wResult;
					}
				}
			}

			int wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, Calendar.getInstance(), APSShiftPeriod.Day,
					FMCShiftLevel.Day, 0);

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1);

			int wNewID = 0;
			List<BFCMessage> wMessageList = new ArrayList<>();

			for (APSTaskStep wAPSTaskStep : wAPSTaskStepList) {
				for (int wOperatorID : wAPSTaskStep.OperatorList) {
					if (SFCTaskStepDAO.getInstance().JudgeIsDispatched(wLoginUser, wAPSTaskStep.ID, wOperatorID,
							wErrorCode)) {
						continue;
					}

					SFCTaskStep wSFCTaskStep = new SFCTaskStep();
					wSFCTaskStep.TaskStepID = wAPSTaskStep.ID;
					wSFCTaskStep.CreateTime = Calendar.getInstance();
					wSFCTaskStep.EndTime = wBaseTime;
					wSFCTaskStep.ShiftID = wShiftID;
					wSFCTaskStep.WorkHour = 0.0D;
					wSFCTaskStep.OperatorID = wOperatorID;
					wSFCTaskStep.ReadyTime = Calendar.getInstance();
					wSFCTaskStep.MonitorID = wLoginUser.ID;
					wSFCTaskStep.Type = (wAPSTaskStep.TaskPartID > 0) ? SFCTaskStepType.Step.getValue()
							: SFCTaskStepType.Quality.getValue();
					wNewID = ((Integer) (SFC_UpdateTaskStep(wLoginUser, wSFCTaskStep)).Result).intValue();

					this.CreateMessage(wLoginUser, wShiftID, wSFCTaskStep, wNewID, wMessageList, wAPSTaskStep,
							wOperatorID);
				}

				// ????????????????????????
				int wCloneNewID = wNewID;
				UpdateDefaultPerson(wLoginUser, wErrorCode, wCloneNewID, wAPSTaskStep);

				wAPSTaskStep.IsDispatched = true;
				APSTaskStepDAO.getInstance().Update(wLoginUser, wAPSTaskStep, wErrorCode);
			}

			List<SFCTaskStep> wAllSFCTaskStepList = SFCTaskStepDAO.getInstance().SelectListByTaskStepIDList(wLoginUser,
					wAPSTaskStepList.stream().map(p -> Integer.valueOf(p.ID)).collect(Collectors.toList()), -1,
					wErrorCode);
			// ???????????????????????????????????????
			CloseMessageList(wLoginUser, wAllSFCTaskStepList, wErrorCode);
			// ????????????
			CoreServiceImpl.getInstance().BFC_UpdateMessageList(wLoginUser, wMessageList);
		} catch (Exception e) {
			wResult.FaultCode = String.valueOf(wResult.FaultCode) + e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<SFCSequentialInfo>> SFC_QuerySequentialInfoNew(BMSEmployee wLoginUser, int wOrderID,
			String wUUID) {
		ServiceResult<List<SFCSequentialInfo>> wResult = new ServiceResult<List<SFCSequentialInfo>>();
		wResult.Result = new ArrayList<SFCSequentialInfo>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			// ??????????????????
			wResult.Result.addAll(SFCOrderFormDAO.getInstance().SelectTelegraphInfo(wLoginUser, wOrderID, wErrorCode));
			// ??????????????????
			wResult.Result.addAll(SFCOrderFormDAO.getInstance().SelectInPlantInfo(wLoginUser, wOrderID, wErrorCode));

			List<SFCSequentialInfo> wWorkList = new ArrayList<SFCSequentialInfo>();

			// ??????????????????
//			wResult.Result.addAll(SFCOrderFormDAO.getInstance().SelectClockInInfo(wLoginUser, wOrderID, wErrorCode));
			wWorkList.addAll(SFCOrderFormDAO.getInstance().SelectClockInInfo(wLoginUser, wOrderID, wErrorCode));
			// ????????????
//			wResult.Result.addAll(SFCOrderFormDAO.getInstance().SelectPreCheckInfo(wLoginUser, wOrderID, wErrorCode));
			wWorkList.addAll(SFCOrderFormDAO.getInstance().SelectPreCheckInfo(wLoginUser, wOrderID, wErrorCode));
			// ????????????
//			wResult.Result.addAll(SFCOrderFormDAO.getInstance().SelectSelfCheckInfo(wLoginUser, wOrderID, wErrorCode));
			wWorkList.addAll(SFCOrderFormDAO.getInstance().SelectSelfCheckInfo(wLoginUser, wOrderID, wErrorCode));
			// ????????????
//			wResult.Result.addAll(SFCOrderFormDAO.getInstance().SelectMutalCheckInfo(wLoginUser, wOrderID, wErrorCode));
			wWorkList.addAll(SFCOrderFormDAO.getInstance().SelectMutalCheckInfo(wLoginUser, wOrderID, wErrorCode));
			// ????????????
//			wResult.Result
//					.addAll(SFCOrderFormDAO.getInstance().SelectSpecialCheckInfo(wLoginUser, wOrderID, wErrorCode));
			wWorkList.addAll(SFCOrderFormDAO.getInstance().SelectSpecialCheckInfo(wLoginUser, wOrderID, wErrorCode));
			// ????????????
//			wResult.Result.addAll(SFCOrderFormDAO.getInstance().SelectFinalCheckInfo(wLoginUser, wOrderID, wErrorCode));
			wWorkList.addAll(SFCOrderFormDAO.getInstance().SelectFinalCheckInfo(wLoginUser, wOrderID, wErrorCode));
			// ???????????????
//			wResult.Result.addAll(SFCOrderFormDAO.getInstance().SelectOutCheckInfo(wLoginUser, wOrderID, wErrorCode));
			wWorkList.addAll(SFCOrderFormDAO.getInstance().SelectOutCheckInfo(wLoginUser, wOrderID, wErrorCode));

			// ??????????????????????????????????????????????????????
			// ?????????
			wWorkList = wWorkList.stream().collect(Collectors.collectingAndThen(
					Collectors.toCollection(() -> new TreeSet<SFCSequentialInfo>(Comparator.comparing(
							o -> o.getStationID() + ";" + o.getStepID() + ";" + o.getType() + ";" + o.getOperators()))),
					ArrayList::new));

			// ???????????????????????????
			Map<Integer, Integer> wPartOrderMap = SFCOrderFormDAO.getInstance().SelectPartOrderMap(wLoginUser, wOrderID,
					wErrorCode);
			// ???????????????????????????
			for (SFCSequentialInfo wSFCSequentialInfo : wWorkList) {
				if (wPartOrderMap.containsKey(wSFCSequentialInfo.StationID)) {
					wSFCSequentialInfo.PartOrder = wPartOrderMap.get(wSFCSequentialInfo.StationID);
				} else if (wSFCSequentialInfo.StationName.contains("??????")) {
					wSFCSequentialInfo.PartOrder = 1000;
				} else if (wSFCSequentialInfo.StationName.contains("????????????")) {
					wSFCSequentialInfo.PartOrder = 1001;
				}
			}
			// ?????????????????????????????????????????????
			wWorkList.sort(
					Comparator.comparing(SFCSequentialInfo::getPartOrder).thenComparing(SFCSequentialInfo::getStationID)
							.thenComparing(SFCSequentialInfo::getStepID).thenComparing(SFCSequentialInfo::getType));
			// ?????????????????????
			wResult.Result.addAll(wWorkList);

			// ????????????
			wResult.Result.addAll(SFCOrderFormDAO.getInstance().SelectMoveCarInfo(wLoginUser, wOrderID, wErrorCode));
			// ?????????????????????
			wResult.Result.addAll(SFCOrderFormDAO.getInstance().SelectNCRInfo(wLoginUser, wOrderID, wErrorCode));
			// ????????????
			wResult.Result.addAll(SFCOrderFormDAO.getInstance().SelectRepairInfo(wLoginUser, wOrderID, wErrorCode));
			// ????????????
			wResult.Result.addAll(SFCOrderFormDAO.getInstance().SelectExceptionInfo(wLoginUser, wOrderID, wErrorCode));
			// ?????????????????????
			wResult.Result.addAll(SFCOrderFormDAO.getInstance().SelectProblemInfo(wLoginUser, wOrderID, wErrorCode));
			// ??????????????????
			wResult.Result
					.addAll(SFCOrderFormDAO.getInstance().SelectFinishConfirmInfo(wLoginUser, wOrderID, wErrorCode));
			// ??????????????????
			wResult.Result.addAll(SFCOrderFormDAO.getInstance().SelectOutApplyInfo(wLoginUser, wOrderID, wErrorCode));
			// ??????????????????
			wResult.Result.addAll(SFCOrderFormDAO.getInstance().SelectOutConfirmInfo(wLoginUser, wOrderID, wErrorCode));

			// ??????
//			if (wResult.Result.size() > 0) {
//				wResult.Result.sort(Comparator.comparing(SFCSequentialInfo::getStartTime));
//			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> SFC_DeleteRepeat(BMSEmployee wLoginUser) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			wResult.Result = SFCTaskStepDAO.getInstance().DeleteRepeat(wLoginUser, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> SFC_TaskStepSubmitAllNew_v2(BMSEmployee wLoginUser,
			List<APSTaskStep> wAPSTaskStepList) {
		ServiceResult<Integer> wResult = new ServiceResult<>();
		OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
		try {
			if (wAPSTaskStepList == null || wAPSTaskStepList.size() <= 0) {
				return wResult;
			}

			if (wAPSTaskStepList.stream().anyMatch(p -> !(p.OperatorList != null && p.OperatorList.size() > 0))) {
				wResult.FaultCode = String.valueOf(wResult.FaultCode) + "?????????????????????????????????!";
				return wResult;
			}

			if (wAPSTaskStepList.stream().anyMatch(p -> p.Status != APSTaskStatus.Issued.getValue())) {
				wResult.FaultCode = String.valueOf(wResult.FaultCode) + "????????????????????????????????????????????????!";
				return wResult;
			}

			// ???????????????????????????
			wAPSTaskStepList
					.forEach(p -> p.OperatorList = p.OperatorList.stream().distinct().collect(Collectors.toList()));

			/**
			 * ????????????ID????????????????????????
			 */
			List<SFCPartPerson> wSFCPartPersonList = SFCTaskStepDAO.getInstance().SelectPersonIDListByPart(wLoginUser,
					wAPSTaskStepList.get(0).PartID, wErrorCode);

			// ?????????????????????????????????
			for (APSTaskStep wAPSTaskStep : wAPSTaskStepList) {
				for (int wPersonID : wAPSTaskStep.OperatorList) {
					if (!wSFCPartPersonList.stream()
							.anyMatch(p -> p.PartID == wAPSTaskStep.PartID && p.PersonID == wPersonID)) {
						wResult.FaultCode += StringUtils.Format("????????????{0}??????????????????{1}??????????????????????????????????????????????????????!",
								APSConstans.GetBMSEmployee(wPersonID).Name, wAPSTaskStep.StepName);
						return wResult;
					}
				}
			}

			int wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, Calendar.getInstance(), APSShiftPeriod.Day,
					FMCShiftLevel.Day, 0);

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1);

			int wNewID = 0;
			List<BFCMessage> wMessageList = new ArrayList<>();

			List<SFCTaskStep> wSFCTaskList = SFCTaskStepDAO.getInstance().QueryTaskStepListByOrder(wLoginUser,
					wAPSTaskStepList.get(0).OrderID, wAPSTaskStepList.get(0).PartID, wErrorCode);

			for (APSTaskStep wAPSTaskStep : wAPSTaskStepList) {
				for (int wOperatorID : wAPSTaskStep.OperatorList) {

					if (wSFCTaskList.stream()
							.anyMatch(p -> p.TaskStepID == wAPSTaskStep.ID && p.OperatorID == wOperatorID)) {
						continue;
					}

					SFCTaskStep wSFCTaskStep = new SFCTaskStep();
					wSFCTaskStep.TaskStepID = wAPSTaskStep.ID;
					wSFCTaskStep.CreateTime = Calendar.getInstance();
					wSFCTaskStep.EndTime = wBaseTime;
					wSFCTaskStep.ShiftID = wShiftID;
					wSFCTaskStep.WorkHour = 0.0D;
					wSFCTaskStep.OperatorID = wOperatorID;
					wSFCTaskStep.ReadyTime = Calendar.getInstance();
					wSFCTaskStep.MonitorID = wLoginUser.ID;
					wSFCTaskStep.Type = (wAPSTaskStep.TaskPartID > 0) ? SFCTaskStepType.Step.getValue()
							: SFCTaskStepType.Quality.getValue();
					wNewID = ((Integer) (SFC_UpdateTaskStep(wLoginUser, wSFCTaskStep)).Result).intValue();

					this.CreateMessage(wLoginUser, wShiftID, wSFCTaskStep, wNewID, wMessageList, wAPSTaskStep,
							wOperatorID);
				}

				// ????????????????????????
				int wCloneNewID = wNewID;
				UpdateDefaultPerson(wLoginUser, wErrorCode, wCloneNewID, wAPSTaskStep);

				wAPSTaskStep.IsDispatched = true;

				// ???????????????
				if (APSConstans.GetFMCWorkChargeList().stream().anyMatch(p -> p.ClassID == wLoginUser.DepartmentID
						&& p.StationID == wAPSTaskStep.PartID && p.Active == 1)) {
					List<Integer> wCheckerList = APSConstans
							.GetFMCWorkChargeList().stream().filter(p -> p.ClassID == wLoginUser.DepartmentID
									&& p.StationID == wAPSTaskStep.PartID && p.Active == 1)
							.findFirst().get().CheckerList;
					if (wCheckerList != null && wCheckerList.size() > 0) {
						wAPSTaskStep.MaterialNo = StringUtils.Join(",", wCheckerList);
					}
				}

				APSTaskStepDAO.getInstance().Update(wLoginUser, wAPSTaskStep, wErrorCode);
			}

			// ????????????
			CoreServiceImpl.getInstance().BFC_UpdateMessageList(wLoginUser, wMessageList);
		} catch (Exception e) {
			wResult.FaultCode = String.valueOf(wResult.FaultCode) + e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<SFCBOMTask>> SFC_QueryBOMTaskEmployeeAllNew(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime, int wOrderID, int wPartID, int wLevel, int wStatus) {
		ServiceResult<List<SFCBOMTask>> wResult = new ServiceResult<List<SFCBOMTask>>();
		wResult.Result = new ArrayList<SFCBOMTask>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			List<SFCBOMTask> wSendList = new ArrayList<SFCBOMTask>();
			List<SFCBOMTask> wToDoList = new ArrayList<SFCBOMTask>();
			List<SFCBOMTask> wDoneList = new ArrayList<SFCBOMTask>();

			List<BPMTaskBase> wBaseList = SFCBOMTaskDAO.getInstance().BPM_GetSendTaskList(wLoginUser, wLoginUser.ID,
					wStartTime, wEndTime, wErrorCode);
			wSendList = CloneTool.CloneArray(wBaseList, SFCBOMTask.class);

			wBaseList = SFCBOMTaskDAO.getInstance().BPM_GetUndoTaskList(wLoginUser, wLoginUser.ID, wErrorCode);
			wToDoList = CloneTool.CloneArray(wBaseList, SFCBOMTask.class);

			wBaseList = SFCBOMTaskDAO.getInstance().BPM_GetDoneTaskList(wLoginUser, wLoginUser.ID, wStartTime, wEndTime,
					wErrorCode);
			wDoneList = CloneTool.CloneArray(wBaseList, SFCBOMTask.class);

			List<Integer> wIDList = new ArrayList<Integer>();

			for (SFCBOMTask wMTCTask : wToDoList) {
				if (wIDList.contains(wMTCTask.ID))
					continue;
				wMTCTask.TagTypes = TaskQueryType.ToHandle.getValue();
				wResult.Result.add(wMTCTask);
				wIDList.add(wMTCTask.ID);
			}

			for (SFCBOMTask wMTCTask : wDoneList) {
				if (wIDList.contains(wMTCTask.ID))
					continue;
				wMTCTask.TagTypes = TaskQueryType.Handled.getValue();
				wResult.Result.add(wMTCTask);
				wIDList.add(wMTCTask.ID);
			}

			for (SFCBOMTask wMTCTask : wSendList) {
				if (wIDList.contains(wMTCTask.ID))
					continue;
				wMTCTask.TagTypes = TaskQueryType.Sended.getValue();
				wResult.Result.add(wMTCTask);
				wIDList.add(wMTCTask.ID);
			}

			wResult.Result.removeIf(p -> p.Status == 0);

			// ??????
			if (wOrderID > 0) {
				wResult.Result = wResult.Result.stream().filter(p -> p.OrderID == wOrderID)
						.collect(Collectors.toList());
			}
			// ??????
			if (wPartID > 0) {
				wResult.Result = wResult.Result.stream().filter(p -> p.PartID == wPartID).collect(Collectors.toList());
			}
			// ??????
			if (wLevel > 0) {
				wResult.Result = wResult.Result.stream().filter(p -> p.Level == wLevel).collect(Collectors.toList());
			}
			// ??????
			if (wStatus >= 0) {
				if (wStatus == 0) {
					wResult.Result = wResult.Result.stream()
							.filter(p -> p.Status != 20 && p.Status != 21 && p.Status != 22)
							.collect(Collectors.toList());
				} else if (wStatus == 1) {
					wResult.Result = wResult.Result.stream()
							.filter(p -> p.Status == 20 || p.Status == 21 || p.Status == 22)
							.collect(Collectors.toList());
				}
			}

			wResult.Result.sort((o1, o2) -> o2.CreateTime.compareTo(o1.CreateTime));
			wResult.Result.sort((o1, o2) -> {
				if (o1.TagTypes == 1) {
					return -1;
				} else if (o2.TagTypes == 1) {
					return 1;
				}
				return 0;
			});

			// ??????????????????????????????
			List<SFCBOMTask> wFinishedList = wResult.Result.stream().filter(p -> p.Status == 20)
					.collect(Collectors.toList());
			wResult.Result.removeIf(p -> p.Status == 20);
			wResult.Result.addAll(wFinishedList);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<SFCBOMTask>> SFC_QueryBOMTaskList(BMSEmployee wLoginUser, int wOrderID, int wPartID,
			int wLevel, int wStatus, Calendar wStartTime, Calendar wEndTime, String wMaterialNo, String wMaterialName,
			int wIsLGL, int wIsQualityLoss) {
		ServiceResult<List<SFCBOMTask>> wResult = new ServiceResult<List<SFCBOMTask>>();
		wResult.Result = new ArrayList<SFCBOMTask>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {

			switch (wStatus) {
			case 1:
				wResult.Result.addAll(SFCBOMTaskDAO.getInstance().SelectList(wLoginUser, wOrderID, wPartID, -1, -1,
						wLevel, StringUtils.parseListArgs(SFCBOMTaskStatus.NomalClose.getValue()), null, wStartTime,
						wEndTime, wErrorCode));
				break;
			case 0:
				wResult.Result.addAll(
						SFCBOMTaskDAO.getInstance().SelectList(wLoginUser, wOrderID, wPartID, -1, -1, wLevel, null,
								StringUtils.parseListArgs(SFCBOMTaskStatus.NomalClose.getValue(),
										SFCBOMTaskStatus.Canceled.getValue(), SFCBOMTaskStatus.Default.getValue(), 25,
										SFCBOMTaskStatus.ExceptionClose.getValue()),
								wStartTime, wEndTime, wErrorCode));
				break;
			default:
				wResult.Result.addAll(SFCBOMTaskDAO.getInstance().SelectList(wLoginUser, wOrderID, wPartID, -1, -1,
						wLevel, null, null, wStartTime, wEndTime, wErrorCode));
				break;
			}
			wResult.Result.sort((o1, o2) -> o2.SubmitTime.compareTo(o1.SubmitTime));

			if (wIsLGL >= 0) {
				wResult.Result = wResult.Result.stream().filter(p -> p.IsLGL == wIsLGL).collect(Collectors.toList());
			}

			if (wIsQualityLoss >= 0) {
				wResult.Result = wResult.Result.stream().filter(p -> p.IsQualityLoss == wIsQualityLoss)
						.collect(Collectors.toList());
			}

			if (wResult.Result.size() <= 0 || wStatus == 1) {
				return wResult;
			}

			// ??????????????????????????????????????????????????????????????????
			if (StringUtils.isNotEmpty(wMaterialName) || StringUtils.isNotEmpty(wMaterialNo)) {
				List<Integer> wTaskIDList = SFCBOMTaskDAO.getInstance().SelectIDListByMaterial(wLoginUser,
						wMaterialName, wMaterialNo, wErrorCode);
				wResult.Result = wResult.Result.stream().filter(p -> wTaskIDList.stream().anyMatch(q -> q == p.ID))
						.collect(Collectors.toList());
			}

			List<BPMTaskBase> wBaseList = SFCBOMTaskDAO.getInstance().BPM_GetUndoTaskList(wLoginUser, wLoginUser.ID,
					wErrorCode);

			if (wBaseList == null || wBaseList.size() <= 0) {
				return wResult;
			}
			for (BPMTaskBase wTaskBase : wBaseList) {
				if (!(wTaskBase instanceof SFCBOMTask))
					continue;
				SFCBOMTask wSFCBOMTask = (SFCBOMTask) wTaskBase;
				wSFCBOMTask.TagTypes = TaskQueryType.ToHandle.getValue();
				for (int i = 0; i < wResult.Result.size(); i++) {
					if (wResult.Result.get(i).ID == wSFCBOMTask.ID)
						wResult.Result.set(i, wSFCBOMTask);
				}
			}
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<SFCAttemptRun>> SFC_QueryAttemptRunEmployeeAllNew(BMSEmployee wLoginUser,
			Calendar wStartTime, Calendar wEndTime, int wProductID, String wPartNo, int wStatus) {
		ServiceResult<List<SFCAttemptRun>> wResult = new ServiceResult<List<SFCAttemptRun>>();
		wResult.Result = new ArrayList<SFCAttemptRun>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			List<SFCAttemptRun> wSendList = new ArrayList<SFCAttemptRun>();
			List<SFCAttemptRun> wToDoList = new ArrayList<SFCAttemptRun>();
			List<SFCAttemptRun> wDoneList = new ArrayList<SFCAttemptRun>();

			List<BPMTaskBase> wBaseList = SFCAttemptRunDAO.getInstance().BPM_GetSendTaskList(wLoginUser,
					wLoginUser.getID(), wStartTime, wEndTime, wErrorCode);
			wSendList = CloneTool.CloneArray(wBaseList, SFCAttemptRun.class);

			wBaseList = SFCAttemptRunDAO.getInstance().BPM_GetUndoTaskList(wLoginUser, wLoginUser.getID(), wErrorCode);
			wToDoList = CloneTool.CloneArray(wBaseList, SFCAttemptRun.class);

			wBaseList = SFCAttemptRunDAO.getInstance().BPM_GetDoneTaskList(wLoginUser, wLoginUser.getID(), wStartTime,
					wEndTime, wErrorCode);
			wDoneList = CloneTool.CloneArray(wBaseList, SFCAttemptRun.class);
			List<Integer> wIDList = new ArrayList<Integer>();
			for (SFCAttemptRun wSFCAttemptRun : wToDoList) {
				if (wIDList.contains(wSFCAttemptRun.ID))
					continue;
				wSFCAttemptRun.TagTypes = TaskQueryType.ToHandle.getValue();
				wResult.Result.add(wSFCAttemptRun);
				wIDList.add(wSFCAttemptRun.ID);
			}

			for (SFCAttemptRun wSFCAttemptRun : wDoneList) {
				if (wIDList.contains(wSFCAttemptRun.ID))
					continue;
				wSFCAttemptRun.TagTypes = TaskQueryType.Handled.getValue();
				wResult.Result.add(wSFCAttemptRun);
				wIDList.add(wSFCAttemptRun.ID);
			}

			for (SFCAttemptRun wSFCAttemptRun : wSendList) {
				if (wIDList.contains(wSFCAttemptRun.ID))
					continue;
				wSFCAttemptRun.TagTypes = TaskQueryType.Sended.getValue();
				wResult.Result.add(wSFCAttemptRun);
				wIDList.add(wSFCAttemptRun.ID);
			}

			wResult.Result.removeIf(p -> p.Status == 0);

			// ??????
			if (wProductID > 0) {
				wResult.Result = wResult.Result.stream().filter(p -> p.ProductID == wProductID)
						.collect(Collectors.toList());
			}
			// ??????
			if (StringUtils.isNotEmpty(wPartNo)) {
				wResult.Result = wResult.Result.stream().filter(p -> p.PartNo.contains(wPartNo))
						.collect(Collectors.toList());
			}
			// ??????
			if (wStatus >= 0) {
				if (wStatus == 0) {
					wResult.Result = wResult.Result.stream()
							.filter(p -> p.Status != 20 && p.Status != 21 && p.Status != 22)
							.collect(Collectors.toList());
				} else if (wStatus == 1) {
					wResult.Result = wResult.Result.stream()
							.filter(p -> p.Status == 20 || p.Status == 21 || p.Status == 22)
							.collect(Collectors.toList());
				}
			}

			wResult.Result.sort((o1, o2) -> o2.CreateTime.compareTo(o1.CreateTime));
			wResult.Result.sort((o1, o2) -> {
				if (o1.TagTypes == 1) {
					return -1;
				} else if (o2.TagTypes == 1) {
					return 1;
				}
				return 0;
			});
			// ??????

		} catch (Exception e) {
			wResult.FaultCode += e.getMessage();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<SFCAttemptRun>> SFC_QueryAttemptRunList(BMSEmployee wLoginUser, int wOrderID, int wLineID,
			int wCustomerID, int wProductID, String wPartNo, int wStatus, Calendar wStartTime, Calendar wEndTime) {
		ServiceResult<List<SFCAttemptRun>> wResult = new ServiceResult<List<SFCAttemptRun>>();
		wResult.Result = new ArrayList<SFCAttemptRun>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {

			switch (wStatus) {
			case 1:
				wResult.Result.addAll(SFCAttemptRunDAO.getInstance().SelectList(wLoginUser, wOrderID, wLineID,
						wCustomerID, wProductID, wPartNo, -1, -1,
						StringUtils.parseListArgs(SFCAttemptRunStatus.NomalClose.getValue()), null, wStartTime,
						wEndTime, wErrorCode));
				break;
			case 0:
				wResult.Result.addAll(SFCAttemptRunDAO.getInstance().SelectList(wLoginUser, wOrderID, wLineID,
						wCustomerID, wProductID, wPartNo, -1, -1, null,
						StringUtils.parseListArgs(SFCAttemptRunStatus.NomalClose.getValue(),
								SFCAttemptRunStatus.Canceled.getValue(), SFCAttemptRunStatus.Default.getValue(),
								SFCAttemptRunStatus.ExceptionClose.getValue()),
						wStartTime, wEndTime, wErrorCode));

				break;
			default:
				wResult.Result.addAll(SFCAttemptRunDAO.getInstance().SelectList(wLoginUser, wOrderID, wLineID,
						wCustomerID, wProductID, wPartNo, -1, -1, null, null, wStartTime, wEndTime, wErrorCode));
				break;
			}
			wResult.Result.sort((o1, o2) -> o2.SubmitTime.compareTo(o1.SubmitTime));

			if (wResult.Result.size() <= 0 || wStatus == 1) {
				return wResult;
			}
			List<BPMTaskBase> wBaseList = SFCAttemptRunDAO.getInstance().BPM_GetUndoTaskList(wLoginUser, wLoginUser.ID,
					wErrorCode);

			if (wBaseList == null || wBaseList.size() <= 0) {
				return wResult;
			}
			for (BPMTaskBase wTaskBase : wBaseList) {
				if (!(wTaskBase instanceof SFCAttemptRun))
					continue;
				SFCAttemptRun wSFCAttemptRun = (SFCAttemptRun) wTaskBase;
				wSFCAttemptRun.TagTypes = TaskQueryType.ToHandle.getValue();
				for (int i = 0; i < wResult.Result.size(); i++) {
					if (wResult.Result.get(i).ID == wSFCAttemptRun.ID)
						wResult.Result.set(i, wSFCAttemptRun);
				}
			}

		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<FPCPart>> SFC_QueryLoginStationList(BMSEmployee wLoginUser) {
		ServiceResult<List<FPCPart>> wResult = new ServiceResult<List<FPCPart>>();
		wResult.Result = new ArrayList<FPCPart>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {

			List<BMSWorkCharge> wWorkChargeList = CoreServiceImpl.getInstance().FMC_QueryWorkChargeList(wLoginUser)
					.List(BMSWorkCharge.class);

			if (wLoginUser.DepartmentID != 61 && wLoginUser.DepartmentID != 68) {
				wWorkChargeList = wWorkChargeList.stream()
						.filter(p -> p.Active == 1 && p.ClassID == wLoginUser.DepartmentID)
						.collect(Collectors.toList());
			}

			if (wWorkChargeList.size() > 0) {
				for (BMSWorkCharge wBMSWorkCharge : wWorkChargeList) {
					FPCPart wFPCPart = APSConstans.GetFPCPart(wBMSWorkCharge.StationID);
					if (wFPCPart == null || wFPCPart.ID <= 0) {
						continue;
					}
					if (!wResult.Result.stream().anyMatch(p -> p.ID == wFPCPart.ID)) {
						wResult.Result.add(wFPCPart);
					}
				}
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<BMSEmployee>> SFC_QueryPartCheckerList(BMSEmployee wLoginUser, int wPartID) {
		ServiceResult<List<BMSEmployee>> wResult = new ServiceResult<List<BMSEmployee>>();
		wResult.Result = new ArrayList<BMSEmployee>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			List<FMCWorkCharge> wList = APSConstans.GetFMCWorkChargeList().stream()
					.filter(p -> p.StationID == wPartID && p.Active == 1).collect(Collectors.toList());
			List<Integer> wUserIDList = new ArrayList<Integer>();
			for (FMCWorkCharge wFMCWorkCharge : wList) {
				wUserIDList.addAll(wFMCWorkCharge.CheckerList);
			}

			wUserIDList = wUserIDList.stream().distinct().collect(Collectors.toList());

			for (Integer wUserID : wUserIDList) {
				BMSEmployee wUser = APSConstans.GetBMSEmployee(wUserID);
				if (wUser.ID > 0) {
					wResult.Result.add(wUser);
				}
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<BMSEmployee>> SFC_QueryTechnicianList(BMSEmployee wLoginUser) {
		ServiceResult<List<BMSEmployee>> wResult = new ServiceResult<List<BMSEmployee>>();
		wResult.Result = new ArrayList<BMSEmployee>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			List<FMCWorkCharge> wList = APSConstans.GetFMCWorkChargeList().stream().filter(p -> p.Active == 1)
					.collect(Collectors.toList());
			List<Integer> wUserIDList = new ArrayList<Integer>();
			for (FMCWorkCharge wFMCWorkCharge : wList) {
				wUserIDList.addAll(wFMCWorkCharge.TechnicianList);
			}

			wUserIDList = wUserIDList.stream().distinct().collect(Collectors.toList());

			for (Integer wUserID : wUserIDList) {
				BMSEmployee wUser = APSConstans.GetBMSEmployee(wUserID);
				if (wUser.ID > 0) {
					wResult.Result.add(wUser);
				}
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<CFGItem>> SFC_QueryModuleList(BMSEmployee wLoginUser) {
		ServiceResult<List<CFGItem>> wResult = new ServiceResult<List<CFGItem>>();
		wResult.Result = new ArrayList<CFGItem>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			wResult.Result = BPMEventModule.getEnumList();

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<String> SFC_ExportBOMTaskList(BMSEmployee wLoginUser, Calendar wDate) {
		ServiceResult<String> wResult = new ServiceResult<String>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			MyExcelSheet wMyExcelSheet = GetMyExcelSheet(wLoginUser, wDate);

			List<MyExcelSheet> wMyExcelSheetList = new ArrayList<MyExcelSheet>(Arrays.asList(wMyExcelSheet));

			wResult.Result = ExcelUtil.ExportData(wMyExcelSheetList, "?????????????????????");

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	private MyExcelSheet GetMyExcelSheet(BMSEmployee wLoginUser, Calendar wDate) {
		MyExcelSheet wResult = new MyExcelSheet();
		try {
			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2010, 0, 1, 0, 0, 0);

			Calendar wStartTime = Calendar.getInstance();
			Calendar wEndTime = Calendar.getInstance();
			if (wDate.compareTo(wBaseTime) <= 0) {
				wStartTime.set(Calendar.HOUR_OF_DAY, 0);
				wStartTime.set(Calendar.MINUTE, 0);
				wStartTime.set(Calendar.SECOND, 0);

				wEndTime.set(Calendar.HOUR_OF_DAY, 23);
				wEndTime.set(Calendar.MINUTE, 59);
				wEndTime.set(Calendar.SECOND, 59);
			} else {
				int wYear = wDate.get(Calendar.YEAR);
				int wMonth = wDate.get(Calendar.MONTH);
				int wDay = wDate.get(Calendar.DATE);

				wStartTime.set(wYear, wMonth, wDay, 0, 0, 0);
				wEndTime.set(wYear, wMonth, wDay, 23, 59, 59);
			}

			SimpleDateFormat wSDF = new SimpleDateFormat("yyyyMMdd");
			String wShiftID = wSDF.format(wDate.getTime());

			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			List<Integer> wIDList = SFCBOMTaskDAO.getInstance().GetCompletedListByTime(wLoginUser, wShiftID,
					wErrorCode);

			wResult.HeaderList = new ArrayList<String>(
					Arrays.asList("??????", "??????", "??????", "??????", "??????", "????????????", "????????????", "??????", "??????", "????????????", "??????", "??????", "?????????"));
			wResult.SheetName = "?????????????????????";
			wResult.TitleName = "????????????";
			wResult.DataList = new ArrayList<List<String>>();

			if (wIDList.size() <= 0) {
				return wResult;
			}

			List<SFCBOMTask> wList = SFCBOMTaskDAO.getInstance().SelectList(wLoginUser, wIDList, null, null,
					wErrorCode);
			int wIndex = 1;
			for (SFCBOMTask wSFCBOMTask : wList) {
				if (wSFCBOMTask.SFCBOMTaskItemList.size() <= 0) {
					List<String> wValueList = new ArrayList<String>(
							Arrays.asList(String.valueOf(wIndex++), wSFCBOMTask.Code, wSFCBOMTask.PartNo,
									wSFCBOMTask.PartName, wSFCBOMTask.PartPointName, wSFCBOMTask.MaterialNo,
									wSFCBOMTask.MaterialName, String.valueOf(wSFCBOMTask.MaterialNumber),
									wSFCBOMTask.Disposal, wSFCBOMTask.ReviewCommentsName,
									wSFCBOMTask.ResponsibilityName, wSFCBOMTask.LevelName, wSFCBOMTask.UpFlowName));
					wResult.DataList.add(wValueList);

					continue;
				}

				if (wSFCBOMTask.Status != 20) {
					if (wSFCBOMTask.ConfirmedLevels.contains("A")) {
						List<SFCBOMTaskItem> wTaskList = wSFCBOMTask.SFCBOMTaskItemList.stream()
								.filter(p -> p.Level == 1 && p.Status != 2 && p.ShiftID.equals(wShiftID))
								.collect(Collectors.toList());
						for (SFCBOMTaskItem wSFCBOMTaskItem : wTaskList) {
							List<String> wValueList = new ArrayList<String>(Arrays.asList(String.valueOf(wIndex++),
									wSFCBOMTask.Code, wSFCBOMTask.PartNo, wSFCBOMTask.PartName,
									wSFCBOMTask.PartPointName, wSFCBOMTaskItem.MaterialNo, wSFCBOMTaskItem.MaterialName,
									String.valueOf(wSFCBOMTaskItem.MaterialNumber), wSFCBOMTask.Disposal,
									wSFCBOMTaskItem.ReviewCommentsName, wSFCBOMTaskItem.ResponsibilityName,
									wSFCBOMTaskItem.LevelName, wSFCBOMTask.UpFlowName));

							wResult.DataList.add(wValueList);
						}
					}
					if (wSFCBOMTask.ConfirmedLevels.contains("B")) {
						List<SFCBOMTaskItem> wTaskList = wSFCBOMTask.SFCBOMTaskItemList.stream()
								.filter(p -> p.Level == 2 && p.Status != 2 && p.ShiftID.equals(wShiftID))
								.collect(Collectors.toList());
						for (SFCBOMTaskItem wSFCBOMTaskItem : wTaskList) {
							List<String> wValueList = new ArrayList<String>(Arrays.asList(String.valueOf(wIndex++),
									wSFCBOMTask.Code, wSFCBOMTask.PartNo, wSFCBOMTask.PartName,
									wSFCBOMTask.PartPointName, wSFCBOMTaskItem.MaterialNo, wSFCBOMTaskItem.MaterialName,
									String.valueOf(wSFCBOMTaskItem.MaterialNumber), wSFCBOMTask.Disposal,
									wSFCBOMTaskItem.ReviewCommentsName, wSFCBOMTaskItem.ResponsibilityName,
									wSFCBOMTaskItem.LevelName, wSFCBOMTask.UpFlowName));

							wResult.DataList.add(wValueList);
						}
					}
					if (wSFCBOMTask.ConfirmedLevels.contains("C")) {
						List<SFCBOMTaskItem> wTaskList = wSFCBOMTask.SFCBOMTaskItemList.stream()
								.filter(p -> p.Level == 3 && p.Status != 2 && p.ShiftID.equals(wShiftID))
								.collect(Collectors.toList());
						for (SFCBOMTaskItem wSFCBOMTaskItem : wTaskList) {
							List<String> wValueList = new ArrayList<String>(Arrays.asList(String.valueOf(wIndex++),
									wSFCBOMTask.Code, wSFCBOMTask.PartNo, wSFCBOMTask.PartName,
									wSFCBOMTask.PartPointName, wSFCBOMTaskItem.MaterialNo, wSFCBOMTaskItem.MaterialName,
									String.valueOf(wSFCBOMTaskItem.MaterialNumber), wSFCBOMTask.Disposal,
									wSFCBOMTaskItem.ReviewCommentsName, wSFCBOMTaskItem.ResponsibilityName,
									wSFCBOMTaskItem.LevelName, wSFCBOMTask.UpFlowName));

							wResult.DataList.add(wValueList);
						}
					}
				} else {
					wSFCBOMTask.SFCBOMTaskItemList = wSFCBOMTask.SFCBOMTaskItemList.stream()
							.filter(p -> p.Status != 2 && p.ShiftID.equals(wShiftID)).collect(Collectors.toList());
					for (SFCBOMTaskItem wSFCBOMTaskItem : wSFCBOMTask.SFCBOMTaskItemList) {
						List<String> wValueList = new ArrayList<String>(Arrays.asList(String.valueOf(wIndex++),
								wSFCBOMTask.Code, wSFCBOMTask.PartNo, wSFCBOMTask.PartName, wSFCBOMTask.PartPointName,
								wSFCBOMTaskItem.MaterialNo, wSFCBOMTaskItem.MaterialName,
								String.valueOf(wSFCBOMTaskItem.MaterialNumber), wSFCBOMTask.Disposal,
								wSFCBOMTaskItem.ReviewCommentsName, wSFCBOMTaskItem.ResponsibilityName,
								wSFCBOMTaskItem.LevelName, wSFCBOMTask.UpFlowName));

						wResult.DataList.add(wValueList);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<SFCSequentialInfoNew>> SFC_QuerySequentialInfoNew_V2(BMSEmployee wLoginUser,
			int wOrderID) {
		ServiceResult<List<SFCSequentialInfoNew>> wResult = new ServiceResult<List<SFCSequentialInfoNew>>();
		wResult.Result = new ArrayList<SFCSequentialInfoNew>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			List<SFCSequentialInfo> wList = new ArrayList<SFCSequentialInfo>();

			SimpleDateFormat wSdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

			// ??????????????????
			wList = SFCOrderFormDAO.getInstance().SelectTelegraphInfo(wLoginUser, wOrderID, wErrorCode);

			int wIndex = 1;
			for (SFCSequentialInfo wSFCSequentialInfo : wList) {
				SFCSequentialInfoNew wItem = new SFCSequentialInfoNew(wIndex++, "????????????", wSFCSequentialInfo.Operators,
						wSdf.format(wSFCSequentialInfo.EndTime.getTime()), "/", "/", "/", "/", "/", "/");
				wResult.Result.add(wItem);
			}

			// ??????????????????
			wList = SFCOrderFormDAO.getInstance().SelectInPlantInfo(wLoginUser, wOrderID, wErrorCode);
			for (SFCSequentialInfo wSFCSequentialInfo : wList) {
				SFCSequentialInfoNew wItem = new SFCSequentialInfoNew(wIndex++, "????????????", wSFCSequentialInfo.Operators,
						wSdf.format(wSFCSequentialInfo.EndTime.getTime()), "/", "/", "/", "/", "/", "/");
				wResult.Result.add(wItem);
			}

			List<SFCSequentialInfo> wWorkList = new ArrayList<SFCSequentialInfo>();

			// ??????????????????
			wWorkList.addAll(SFCOrderFormDAO.getInstance().SelectClockInInfo(wLoginUser, wOrderID, wErrorCode));
			// ????????????
			wWorkList.addAll(SFCOrderFormDAO.getInstance().SelectPreCheckInfo(wLoginUser, wOrderID, wErrorCode));
			// ????????????
			wWorkList.addAll(SFCOrderFormDAO.getInstance().SelectSelfCheckInfo(wLoginUser, wOrderID, wErrorCode));
			// ????????????
			wWorkList.addAll(SFCOrderFormDAO.getInstance().SelectMutalCheckInfo(wLoginUser, wOrderID, wErrorCode));
			// ????????????
			wWorkList.addAll(SFCOrderFormDAO.getInstance().SelectSpecialCheckInfo(wLoginUser, wOrderID, wErrorCode));
			// ????????????
			wWorkList.addAll(SFCOrderFormDAO.getInstance().SelectFinalCheckInfo(wLoginUser, wOrderID, wErrorCode));
			// ???????????????
			wWorkList.addAll(SFCOrderFormDAO.getInstance().SelectOutCheckInfo(wLoginUser, wOrderID, wErrorCode));

			// ?????????????????????
			// ?????????
			List<SFCSequentialInfo> wStepList = wWorkList.stream()
					.collect(Collectors.collectingAndThen(
							Collectors.toCollection(() -> new TreeSet<SFCSequentialInfo>(
									Comparator.comparing(o -> o.getStationID() + ";" + o.getStepID()))),
							ArrayList::new));

			// ???????????????????????????
			Map<Integer, Integer> wPartOrderMap = SFCOrderFormDAO.getInstance().SelectPartOrderMap(wLoginUser, wOrderID,
					wErrorCode);
			// ???????????????????????????
			for (SFCSequentialInfo wSFCSequentialInfo : wStepList) {
				if (wPartOrderMap.containsKey(wSFCSequentialInfo.StationID)) {
					wSFCSequentialInfo.PartOrder = wPartOrderMap.get(wSFCSequentialInfo.StationID);
				} else if (wSFCSequentialInfo.StationName.contains("??????")) {
					wSFCSequentialInfo.PartOrder = 1000;
				} else if (wSFCSequentialInfo.StationName.contains("????????????")) {
					wSFCSequentialInfo.PartOrder = 1001;
				}
			}
			// ?????????????????????????????????????????????
			wStepList.sort(
					Comparator.comparing(SFCSequentialInfo::getPartOrder).thenComparing(SFCSequentialInfo::getStationID)
							.thenComparing(SFCSequentialInfo::getStepID).thenComparing(SFCSequentialInfo::getType));

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1, 0, 0, 0);

			// ????????????
			for (SFCSequentialInfo wSFCSequentialInfo : wStepList) {
				String wTypeText = "";
				String checkInfo = "";
				String mCheckInfo = "/";
				String sCheckInfo = "/";

				if (wSFCSequentialInfo.StationName.contains("??????")) {
					wTypeText = "????????????";

					if (wWorkList.stream()
							.anyMatch(p -> p.StationID == wSFCSequentialInfo.StationID
									&& p.StepID == wSFCSequentialInfo.StepID
									&& p.Type == SFCSequentialInfoType.PreCheck.getValue())) {
						SFCSequentialInfo wInfo = wWorkList.stream()
								.filter(p -> p.StationID == wSFCSequentialInfo.StationID
										&& p.StepID == wSFCSequentialInfo.StepID
										&& p.Type == SFCSequentialInfoType.PreCheck.getValue())
								.findFirst().get();
						checkInfo = StringUtils.Format("{0}({1})", wInfo.Operators,
								wSdf.format(wInfo.EndTime.getTime()));
					}

				} else if (wSFCSequentialInfo.StationName.contains("??????")) {
					wTypeText = "????????????";

					if (wWorkList.stream()
							.anyMatch(p -> p.StationID == wSFCSequentialInfo.StationID
									&& p.StepID == wSFCSequentialInfo.StepID
									&& p.Type == SFCSequentialInfoType.FinalCheck.getValue())) {
						SFCSequentialInfo wInfo = wWorkList.stream()
								.filter(p -> p.StationID == wSFCSequentialInfo.StationID
										&& p.StepID == wSFCSequentialInfo.StepID
										&& p.Type == SFCSequentialInfoType.FinalCheck.getValue())
								.findFirst().get();
						checkInfo = StringUtils.Format("{0}({1})", wInfo.Operators,
								wSdf.format(wInfo.EndTime.getTime()));
					}
				} else if (wSFCSequentialInfo.StationName.contains("??????")) {
					wTypeText = "????????????";

					if (wWorkList.stream()
							.anyMatch(p -> p.StationID == wSFCSequentialInfo.StationID
									&& p.StepID == wSFCSequentialInfo.StepID
									&& p.Type == SFCSequentialInfoType.OutCheck.getValue())) {
						SFCSequentialInfo wInfo = wWorkList.stream()
								.filter(p -> p.StationID == wSFCSequentialInfo.StationID
										&& p.StepID == wSFCSequentialInfo.StepID
										&& p.Type == SFCSequentialInfoType.OutCheck.getValue())
								.findFirst().get();
						checkInfo = StringUtils.Format("{0}({1})", wInfo.Operators,
								wSdf.format(wInfo.EndTime.getTime()));
					}
				} else {
					wTypeText = "????????????";

					if (wWorkList.stream()
							.anyMatch(p -> p.StationID == wSFCSequentialInfo.StationID
									&& p.StepID == wSFCSequentialInfo.StepID
									&& p.Type == SFCSequentialInfoType.SelfCheck.getValue())) {
						SFCSequentialInfo wInfo = wWorkList.stream()
								.filter(p -> p.StationID == wSFCSequentialInfo.StationID
										&& p.StepID == wSFCSequentialInfo.StepID
										&& p.Type == SFCSequentialInfoType.SelfCheck.getValue())
								.findFirst().get();
						checkInfo = StringUtils.Format("{0}({1})", wInfo.Operators,
								wSdf.format(wInfo.EndTime.getTime()));
					}

					if (wWorkList.stream()
							.anyMatch(p -> p.StationID == wSFCSequentialInfo.StationID
									&& p.StepID == wSFCSequentialInfo.StepID
									&& p.Type == SFCSequentialInfoType.MutualCheck.getValue())) {
						SFCSequentialInfo wInfo = wWorkList.stream()
								.filter(p -> p.StationID == wSFCSequentialInfo.StationID
										&& p.StepID == wSFCSequentialInfo.StepID
										&& p.Type == SFCSequentialInfoType.MutualCheck.getValue())
								.findFirst().get();
						mCheckInfo = StringUtils.Format("{0}({1})", wInfo.Operators,
								wSdf.format(wInfo.EndTime.getTime()));
					}

					if (wWorkList.stream()
							.anyMatch(p -> p.StationID == wSFCSequentialInfo.StationID
									&& p.StepID == wSFCSequentialInfo.StepID
									&& p.Type == SFCSequentialInfoType.SpecialCheck.getValue())) {
						SFCSequentialInfo wInfo = wWorkList.stream()
								.filter(p -> p.StationID == wSFCSequentialInfo.StationID
										&& p.StepID == wSFCSequentialInfo.StepID
										&& p.Type == SFCSequentialInfoType.SpecialCheck.getValue())
								.findFirst().get();
						sCheckInfo = StringUtils.Format("{0}({1})", wInfo.Operators,
								wSdf.format(wInfo.EndTime.getTime()));
					}
				}

				String clockInfo = "";
				if (wWorkList.stream()
						.anyMatch(p -> p.StationID == wSFCSequentialInfo.StationID
								&& p.StepID == wSFCSequentialInfo.StepID
								&& p.Type == SFCSequentialInfoType.ClockIn.getValue())) {
					SFCSequentialInfo wInfo = wWorkList.stream()
							.filter(p -> p.StationID == wSFCSequentialInfo.StationID
									&& p.StepID == wSFCSequentialInfo.StepID
									&& p.Type == SFCSequentialInfoType.ClockIn.getValue())
							.findFirst().get();
					clockInfo = StringUtils.Format("{0}({1})", wInfo.Operators, wSdf.format(wInfo.EndTime.getTime()));
				}

				SFCSequentialInfoNew wItem = new SFCSequentialInfoNew(wIndex++, wTypeText, "/", "/",
						wSFCSequentialInfo.StationName, wSFCSequentialInfo.StepName, clockInfo, checkInfo, mCheckInfo,
						sCheckInfo);
				wResult.Result.add(wItem);
			}

			// ??????????????????
			wList = SFCOrderFormDAO.getInstance().SelectFinishConfirmInfo(wLoginUser, wOrderID, wErrorCode);
			for (SFCSequentialInfo wSFCSequentialInfo : wList) {
				SFCSequentialInfoNew wItem = new SFCSequentialInfoNew(wIndex++, "????????????", wSFCSequentialInfo.Operators,
						wSdf.format(wSFCSequentialInfo.EndTime.getTime()), "/", "/", "/", "/", "/", "/");
				wResult.Result.add(wItem);
			}
			// ??????????????????
			wList = SFCOrderFormDAO.getInstance().SelectOutApplyInfo(wLoginUser, wOrderID, wErrorCode);
			for (SFCSequentialInfo wSFCSequentialInfo : wList) {
				SFCSequentialInfoNew wItem = new SFCSequentialInfoNew(wIndex++, "????????????", wSFCSequentialInfo.Operators,
						wSdf.format(wSFCSequentialInfo.EndTime.getTime()), "/", "/", "/", "/", "/", "/");
				wResult.Result.add(wItem);
			}
			// ??????????????????
			wList = SFCOrderFormDAO.getInstance().SelectOutConfirmInfo(wLoginUser, wOrderID, wErrorCode);
			for (SFCSequentialInfo wSFCSequentialInfo : wList) {
				SFCSequentialInfoNew wItem = new SFCSequentialInfoNew(wIndex++, "????????????", wSFCSequentialInfo.Operators,
						wSdf.format(wSFCSequentialInfo.EndTime.getTime()), "/", "/", "/", "/", "/", "/");
				wResult.Result.add(wItem);
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<String> SFC_IsOverEqua(BMSEmployee wLoginUser, SFCBOMTask wTask) {
		ServiceResult<String> wResult = new ServiceResult<String>();
		wResult.Result = "";
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			for (SFCBOMTaskItem wItem : wTask.SFCBOMTaskItemList) {
				if (wItem.BOMItemID <= 0 || StringUtils.isNotEmpty(wItem.QualityLossBig)
						|| StringUtils.isNotEmpty(wItem.QualityLossSmall)) {
					continue;
				}

				String wMsg = APSBOMItemDAO.getInstance().IsOverEqua(wLoginUser, wTask, wItem, wErrorCode);
				if (StringUtils.isNotEmpty(wMsg)) {
					wResult.Result = wMsg;
					return wResult;
				}
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<SFCBOMTask> SFC_UpdateBOMTask(BMSEmployee wLoginUser, SFCBOMTask wData) {
		ServiceResult<SFCBOMTask> wResult = new ServiceResult<SFCBOMTask>();
		wResult.Result = new SFCBOMTask();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			wResult.Result = SFCBOMTaskDAO.getInstance().Update(wLoginUser, wData, wErrorCode);

			if (wData.SFCBOMTaskItemList != null && wData.SFCBOMTaskItemList.size() > 0) {
				for (SFCBOMTaskItem wSFCBOMTaskItem : wData.SFCBOMTaskItemList) {
					wSFCBOMTaskItem.SFCBOMTaskID = wResult.Result.ID;
					SFCBOMTaskItemDAO.getInstance().Update(wLoginUser, wSFCBOMTaskItem, wErrorCode);
				}
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> SFC_UpdateQuota(BMSEmployee wLoginUser) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			List<Integer> wItemIDList = SFCBOMTaskItemDAO.getInstance().GetQuotaZeroList(wLoginUser, wErrorCode);
			for (Integer wItemID : wItemIDList) {
				SFCBOMTaskItem wItem = SFCBOMTaskItemDAO.getInstance().SelectByID(wLoginUser, wItemID, wErrorCode);
				SFCBOMTask wTask = (SFCBOMTask) SFCBOMTaskDAO.getInstance().BPM_GetTaskInfo(wLoginUser,
						wItem.SFCBOMTaskID, "", wErrorCode);
				if (wTask == null || wTask.ID <= 0 || wTask.PartID <= 0 || wTask.PartPointID <= 0
						|| wItem.MaterialID <= 0) {
					continue;
				}
				double wQuota = SFCBOMTaskItemDAO.getInstance().GetQuota(wLoginUser, wTask.PartID, wTask.PartPointID,
						wItem.MaterialID, wErrorCode);
				if (wQuota <= 0) {
					continue;
				}
				wItem.Quota = wQuota;
				SFCBOMTaskItemDAO.getInstance().Update(wLoginUser, wItem, wErrorCode);
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<OMSOrder>> SFC_QueryOrderListByLogID(BMSEmployee wLoginUser, int wLogID) {
		ServiceResult<List<OMSOrder>> wResult = new ServiceResult<List<OMSOrder>>();
		wResult.Result = new ArrayList<OMSOrder>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			List<TCMMaterialChangeLog> wLogList = TCMMaterialChangeLogDAO.getInstance().SelectListWithoutSub(wLoginUser,
					wLogID, null, null, wErrorCode);
			if (wLogList == null || wLogList.size() <= 0) {
				wResult.CustomResult.put("Used", new ArrayList<OMSOrder>());
				wResult.CustomResult.put("Less", new ArrayList<OMSOrder>());
				return wResult;
			}

			if (StringUtils.isEmpty(wLogList.get(0).OrderIDList)) {
				wResult.CustomResult.put("Used", new ArrayList<OMSOrder>());
				wResult.CustomResult.put("Less", new ArrayList<OMSOrder>());
				return wResult;
			}

			List<Integer> wOrderIDList = StringUtils.parseIntList(wLogList.get(0).OrderIDList.split(","));

			List<OMSOrder> wOrderList = OMSOrderDAO.getInstance().SelectListByIDList(wLoginUser, wOrderIDList,
					wErrorCode);
			List<Integer> wUsedIDList = SFCBOMTaskDAO.getInstance().SelectedUsedOrderIDList(wLoginUser, wLogID,
					wErrorCode);
			List<OMSOrder> wUsedOrderList = wOrderList.stream()
					.filter(p -> wUsedIDList.stream().anyMatch(q -> q.intValue() == p.ID)).collect(Collectors.toList());
			List<OMSOrder> wLessOrderList = wOrderList.stream()
					.filter(p -> !wUsedIDList.stream().anyMatch(q -> q.intValue() == p.ID))
					.collect(Collectors.toList());
			wResult.CustomResult.put("Used", wUsedOrderList);
			wResult.CustomResult.put("Less", wLessOrderList);
			wResult.Result = wOrderList;

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> SFC_UpdateAssessType(BMSEmployee wLoginUser, List<SFCBOMTaskItem> wDataList) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			if (wDataList == null || wDataList.size() <= 0) {
				return wResult;
			}

			// ???????????????????????????????????????
			for (SFCBOMTaskItem wSFCBOMTaskItem : wDataList) {
				SFCBOMTaskItemDAO.getInstance().Update(wLoginUser, wSFCBOMTaskItem, wErrorCode);
			}
			// ????????????????????????ID????????????BOM
			for (SFCBOMTaskItem wSFCBOMTaskItem : wDataList) {
				int wApsBoMID = APSBOMItemDAO.getInstance().SelectIDListByBomTaskItemID(wLoginUser, wSFCBOMTaskItem.ID,
						wErrorCode);
				if (wApsBoMID <= 0) {
					continue;
				}
				// ????????????????????????????????????????????????????????????
				APSServiceImpl.getInstance().APS_BomItemPropertyChange(wLoginUser, wApsBoMID, "X", 0);
			}
			// ?????????????????????????????????
//			APSServiceImpl.getInstance().APS_BOMTaskToSAP(wLoginUser, wDataList.get(0).SFCBOMTaskID);

			// ?????????????????????????????????
			APSServiceImpl.getInstance().APS_BOMTaskToSAP(wLoginUser, wDataList);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<SFCTaskStepCar>> SFC_QueryDispatchCarList(BMSEmployee wLoginUser) {
		ServiceResult<List<SFCTaskStepCar>> wResult = new ServiceResult<List<SFCTaskStepCar>>();
		wResult.Result = new ArrayList<SFCTaskStepCar>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			// ????????????????????????????????????ID??????
			List<Integer> wOrderIDList = SFCTaskStepDAO.getInstance().SelectReparingOrderIDList(wLoginUser, wErrorCode);
			// ???????????????ID????????????????????????
			List<OMSOrder> wOrderList = OMSOrderDAO.getInstance().SelectListByIDList(wLoginUser, wOrderIDList,
					wErrorCode);
			// ?????????????????????
			for (OMSOrder wOMSOrder : wOrderList) {
				SFCTaskStepCar wTaskStepCar = SFCTaskStepDAO.getInstance().GetSFCTaskStepCar(wLoginUser, wOMSOrder,
						wErrorCode);
				// ?????????????????????
				wResult.Result.add(wTaskStepCar);
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<SFCTaskStepPart>> SFC_QueryDispatchPartList(BMSEmployee wLoginUser, int wOrderID) {
		ServiceResult<List<SFCTaskStepPart>> wResult = new ServiceResult<List<SFCTaskStepPart>>();
		wResult.Result = new ArrayList<SFCTaskStepPart>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			// ???????????????ID????????????ID??????
			List<Integer> wPartIDList = SFCTaskStepDAO.getInstance().SelectPartIDList(wLoginUser, wOrderID, wErrorCode);
			// ???????????????????????????
			List<LFSWorkAreaStation> wASList = APSConstans.GetLFSWorkAreaStationList().values().stream()
					.filter(p -> p.Active == 1).collect(Collectors.toList());
			// ?????????
			wASList.sort(Comparator.comparing(LFSWorkAreaStation::getOrderNum));
			// ???????????????
			OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wOrderID, wErrorCode);
			// ???????????????ID????????????
			for (LFSWorkAreaStation wLFSWorkAreaStation : wASList) {
				if (!wPartIDList.stream().anyMatch(p -> p.intValue() == wLFSWorkAreaStation.StationID)) {
					continue;
				}

				FPCPart wFPCPart = APSConstans.GetFPCPart(wLFSWorkAreaStation.StationID);
				if (wFPCPart == null || wFPCPart.ID <= 0) {
					continue;
				}

				SFCTaskStepPart wSFCTaskStepPart = SFCTaskStepDAO.getInstance().GetSFCTaskStepPart(wLoginUser, wFPCPart,
						wOrderID, wOrder, wErrorCode);
				// ?????????????????????
				wResult.Result.add(wSFCTaskStepPart);
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<String> SFC_ExportBOMTaskListByOrder(BMSEmployee wLoginUser, int wOrderID) {
		ServiceResult<String> wResult = new ServiceResult<String>();
		try {

			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			MyExcelSheet wMyExcelSheet = GetMyExcelSheet(wLoginUser, wOrderID);

			List<MyExcelSheet> wMyExcelSheetList = new ArrayList<MyExcelSheet>(Arrays.asList(wMyExcelSheet));

			wResult.Result = ExcelUtil.ExportData(wMyExcelSheetList, "?????????????????????");

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	private MyExcelSheet GetMyExcelSheet(BMSEmployee wLoginUser, int wOrderID) {
		MyExcelSheet wResult = new MyExcelSheet();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			wResult.HeaderList = new ArrayList<String>(Arrays.asList("??????", "??????", "??????", "??????", "??????", "????????????", "????????????", "??????",
					"??????", "????????????", "??????", "??????", "?????????", "????????????"));
			wResult.SheetName = "?????????????????????";
			wResult.TitleName = "????????????";
			wResult.DataList = new ArrayList<List<String>>();

			List<SFCBOMTask> wList = SFCBOMTaskDAO.getInstance().SelectList(wLoginUser, wOrderID, -1, -1, -1, -1, null,
					null, null, null, wErrorCode);

			List<Integer> wIDList = wList.stream().map(p -> p.ID).distinct().collect(Collectors.toList());

			wList = SFCBOMTaskDAO.getInstance().SelectList(wLoginUser, wIDList, null, null, wErrorCode);

			int wIndex = 1;
			for (SFCBOMTask wSFCBOMTask : wList) {
				if (wSFCBOMTask.SFCBOMTaskItemList.size() <= 0) {
					List<String> wValueList = new ArrayList<String>(Arrays.asList(String.valueOf(wIndex++),
							wSFCBOMTask.Code, wSFCBOMTask.PartNo, wSFCBOMTask.PartName, wSFCBOMTask.PartPointName,
							wSFCBOMTask.MaterialNo, wSFCBOMTask.MaterialName,
							String.valueOf(wSFCBOMTask.MaterialNumber), wSFCBOMTask.Disposal,
							wSFCBOMTask.ReviewCommentsName, wSFCBOMTask.ResponsibilityName, wSFCBOMTask.LevelName,
							wSFCBOMTask.UpFlowName, wSFCBOMTask.SAPStatusText));
					wResult.DataList.add(wValueList);

					continue;
				}

				for (SFCBOMTaskItem wSFCBOMTaskItem : wSFCBOMTask.SFCBOMTaskItemList) {
					List<String> wValueList = new ArrayList<String>(Arrays.asList(String.valueOf(wIndex++),
							wSFCBOMTask.Code, wSFCBOMTask.PartNo, wSFCBOMTask.PartName, wSFCBOMTask.PartPointName,
							wSFCBOMTaskItem.MaterialNo, wSFCBOMTaskItem.MaterialName,
							String.valueOf(wSFCBOMTaskItem.MaterialNumber), wSFCBOMTask.Disposal,
							wSFCBOMTaskItem.ReviewCommentsName, wSFCBOMTaskItem.ResponsibilityName,
							wSFCBOMTaskItem.LevelName, wSFCBOMTask.UpFlowName, wSFCBOMTask.SAPStatusText));

					wResult.DataList.add(wValueList);
				}
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<OMSOrder>> SFC_QueryOrderList(BMSEmployee wLoginUser) {
		ServiceResult<List<OMSOrder>> wResult = new ServiceResult<List<OMSOrder>>();
		wResult.Result = new ArrayList<OMSOrder>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			// ???????????????ID??????
			List<Integer> wOrderIDList = SFCBOMTaskDAO.getInstance().GetOrderIDList(wLoginUser, wErrorCode);
			// ???????????????ID????????????????????????
			wResult.Result = OMSOrderDAO.getInstance().SelectListByIDList(wLoginUser, wOrderIDList, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> SFC_BOMTaskCodeReset(BMSEmployee wLoginUser) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			int wYear = Calendar.getInstance().get(Calendar.YEAR);

			for (int i = 1; i <= 12; i++) {
				int wNumber = 1;
				// ????????????
				Calendar wSTime = Calendar.getInstance();
				wSTime.set(wYear, i - 1, 1, 0, 0, 0);
				// ????????????
				Calendar wETime = Calendar.getInstance();
				wETime.set(wYear, i, 1, 0, 0, 0);

				// ???????????????????????????????????????ID??????
				List<SFCBOMTask> wTaskList = SFCBOMTaskDAO.getInstance().SelectListByCreateTime(wLoginUser, null,
						wSTime, wETime, wErrorCode);
				for (SFCBOMTask wSFCBOMTask : wTaskList) {

					wSFCBOMTask.Code = StringUtils.Format("RP{0}{1}{2}", String.valueOf(wYear),
							String.format("%02d", i), String.format("%04d", wNumber));

					SFCBOMTaskDAO.getInstance().BPM_UpdateCode(wLoginUser, wSFCBOMTask, wErrorCode);

					wNumber++;
				}
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> SFC_UpdateBOMTaskSubmitTime(BMSEmployee wLoginUser) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			List<SFCBOMTask> wList = SFCBOMTaskDAO.getInstance().SelectList(wLoginUser, -1, -1, -1, -1, -1, null, null,
					null, null, wErrorCode);
			for (SFCBOMTask wSFCBOMTask : wList) {
				if (wSFCBOMTask.FlowID <= 0)
					continue;
				List<BPMActivitiHisTask> wHisList = BPMServiceImpl.getInstance()
						.BPM_GetActivitiHisTaskByPIId(wLoginUser, wSFCBOMTask.FlowID).List(BPMActivitiHisTask.class);
				if (wHisList == null || wHisList.size() <= 0) {
					continue;
				}
				Calendar wEndTime = wHisList.stream().max(Comparator.comparing(BPMActivitiHisTask::getEndTime))
						.get().EndTime;
				SFCBOMTaskDAO.getInstance().UpdateSubmitTime(wLoginUser, wSFCBOMTask, wEndTime, wErrorCode);
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> SFC_OutsourcingProcess(BMSEmployee wLoginUser, int wSFCBomTaskID) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			// ???????????????????????????
			SFCBOMTask wSFCBOMTask = (SFCBOMTask) SFCBOMTaskDAO.getInstance().BPM_GetTaskInfo(wLoginUser, wSFCBomTaskID,
					"", wErrorCode);
			if (wSFCBOMTask == null || wSFCBOMTask.ID <= 0) {
				return wResult;
			}
			// ?????????????????????????????????
			List<SFCBOMTaskItem> wItemList = SFCBOMTaskItemDAO.getInstance().SelectList(wLoginUser, -1, wSFCBomTaskID,
					wErrorCode);
			// ?????????SourceID???sourcetype????????????BOM
			for (SFCBOMTaskItem wSFCBOMTaskItem : wItemList) {
				if (wSFCBOMTaskItem.BOMItemID <= 0) {
					continue;
				}

				int wAPSBomItemID = APSBOMItemDAO.getInstance().SelectIDBySource(wLoginUser, wSFCBOMTaskItem.ID,
						wErrorCode);
				if (wAPSBomItemID <= 0) {
					continue;
				}

				// ???????????????????????????BOM
				APSServiceImpl.getInstance().APS_BomItemPropertyChange(wLoginUser, wAPSBomItemID, "X", 0);
				// ????????????outsourcetype??????2
				wSFCBOMTaskItem.OutsourceType = 2;
				SFCBOMTaskItemDAO.getInstance().Update(wLoginUser, wSFCBOMTaskItem, wErrorCode);
				// ????????????????????????????????????BOM?????????outsourcetype???2
				MSSBOMItemDAO.getInstance().UpdateOutsourceType(wLoginUser, wSFCBOMTaskItem.BOMItemID, wErrorCode);
			}

			// ?????????????????????????????????????????????
			APSServiceImpl.getInstance().APS_BOMTaskToSAP(wLoginUser, wSFCBomTaskID);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}
}

/*
 * Location: C:\Users\Shris\Desktop\???????????????
 * (5)\MESLOCOAPS.zip!\WEB-INF\classes\com\mes\loco\aps\server\serviceimpl\
 * SFCServiceImpl.class Java compiler version: 8 (52.0) JD-Core Version: 1.1.2
 */