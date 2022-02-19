package com.mes.loco.aps.server.serviceimpl;

import com.mes.loco.aps.server.service.SCHService;
import com.mes.loco.aps.server.service.mesenum.APSShiftPeriod;
import com.mes.loco.aps.server.service.mesenum.BFCMessageStatus;
import com.mes.loco.aps.server.service.mesenum.BFCMessageType;
import com.mes.loco.aps.server.service.mesenum.BPMEventModule;
import com.mes.loco.aps.server.service.mesenum.FMCShiftLevel;
import com.mes.loco.aps.server.service.mesenum.MESException;
import com.mes.loco.aps.server.service.mesenum.SCHSecondStatus;
import com.mes.loco.aps.server.service.mesenum.SCHSecondmentApplyStatus;
import com.mes.loco.aps.server.service.mesenum.SCHSecondmentApplyType;
import com.mes.loco.aps.server.service.mesenum.SCHSecondmentBPMStatus;
import com.mes.loco.aps.server.service.mesenum.TagTypes;
import com.mes.loco.aps.server.service.mesenum.TaskQueryType;
import com.mes.loco.aps.server.service.po.OutResult;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.bfc.BFCMessage;
import com.mes.loco.aps.server.service.po.bms.BMSDepartment;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.bms.BMSPosition;
import com.mes.loco.aps.server.service.po.bpm.BPMTaskBase;
import com.mes.loco.aps.server.service.po.fmc.FMCWorkCharge;
import com.mes.loco.aps.server.service.po.fpc.FPCPart;
import com.mes.loco.aps.server.service.po.lfs.LFSWorkAreaChecker;
import com.mes.loco.aps.server.service.po.sch.SCHSecondment;
import com.mes.loco.aps.server.service.po.sch.SCHSecondmentApply;
import com.mes.loco.aps.server.service.po.sch.SCHSecondmentBPM;
import com.mes.loco.aps.server.service.utils.CalendarUtil;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.CoreServiceImpl;
import com.mes.loco.aps.server.serviceimpl.LFSServiceImpl;
import com.mes.loco.aps.server.serviceimpl.SCHServiceImpl;
import com.mes.loco.aps.server.serviceimpl.dao.sch.SCHSecondmentApplyDAO;
import com.mes.loco.aps.server.serviceimpl.dao.sch.SCHSecondmentBPMDAO;
import com.mes.loco.aps.server.serviceimpl.dao.sch.SCHSecondmentDAO;
import com.mes.loco.aps.server.serviceimpl.utils.MESServer;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSConstans;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SCHServiceImpl implements SCHService {
	private static Logger logger = LoggerFactory.getLogger(SCHServiceImpl.class);

	private static SCHService Instance;

	public static SCHService getInstance() {
		if (Instance == null)
			Instance = new SCHServiceImpl();
		return Instance;
	}

	public ServiceResult<SCHSecondment> SCH_QuerySecondment(BMSEmployee wLoginUser, int wID) {
		ServiceResult<SCHSecondment> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = SCHSecondmentDAO.getInstance().SelectByID(wLoginUser, wID, wErrorCode);

			if (wResult.Result != null && ((SCHSecondment) wResult.Result).ID > 0) {

				((SCHSecondment) wResult.Result).SendName = APSConstans
						.GetBMSEmployeeName(((SCHSecondment) wResult.Result).SendID);

				((SCHSecondment) wResult.Result).SecondDepartment = APSConstans
						.GetBMSDepartmentName(((SCHSecondment) wResult.Result).SecondDepartmentID);

				((SCHSecondment) wResult.Result).BeSecondDepartment = APSConstans
						.GetBMSDepartmentName(((SCHSecondment) wResult.Result).BeSecondDepartmentID);

				((SCHSecondment) wResult.Result).SecondAuditor = APSConstans
						.GetBMSEmployeeName(((SCHSecondment) wResult.Result).SecondAuditID);

				((SCHSecondment) wResult.Result).BeSecondAuditor = APSConstans
						.GetBMSEmployeeName(((SCHSecondment) wResult.Result).BeSecondAuditID);

				((SCHSecondment) wResult.Result).SecondPerson = APSConstans
						.GetBMSEmployeeName(((SCHSecondment) wResult.Result).SecondPersonID);

				((SCHSecondment) wResult.Result).AreaName = APSConstans
						.GetBMSDepartmentName(((SCHSecondment) wResult.Result).AreaID);
			}

			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<List<SCHSecondment>> SCH_QuerySecondmentList(BMSEmployee wLoginUser, int wID, int wSendID,
			int wSecondDepartmentID, int wSecondAuditID, int wBeSecondAuditID, int wSecondPersonID,
			List<Integer> wStateIDList, Calendar wStartTime, Calendar wEndTime) {
		ServiceResult<List<SCHSecondment>> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = SCHSecondmentDAO.getInstance().SelectList(wLoginUser, wID, wSendID, wSecondDepartmentID,
					wSecondAuditID, wBeSecondAuditID, wSecondPersonID, -1, wStateIDList, wStartTime, wEndTime,
					wErrorCode);
			if ((wResult.Result).size() > 0) {
				SetText((List<SCHSecondment>) wResult.Result);
			}
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<Integer> SCH_UpdateSecondment(BMSEmployee wLoginUser, SCHSecondment wSCHSecondment) {
		ServiceResult<Integer> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = Integer
					.valueOf(SCHSecondmentDAO.getInstance().Update(wLoginUser, wSCHSecondment, wErrorCode));

			int wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, Calendar.getInstance(), APSShiftPeriod.Day,
					FMCShiftLevel.Day, 0);

			if (wSCHSecondment != null && wSCHSecondment.Status == SCHSecondStatus.ToOtherSecond.getValue()
					&& wSCHSecondment.IsOverArea) {

				List<BMSEmployee> wList = APSUtils.getInstance().APS_GetDirectorByAreaID(wLoginUser,
						wSCHSecondment.AreaID);
				List<BFCMessage> wBFCMessageList = new ArrayList<>();

				for (BMSEmployee wBMSEmployee : wList) {

					BFCMessage wMessage = new BFCMessage();
					wMessage.Active = 0;
					wMessage.CompanyID = 0;
					wMessage.CreateTime = Calendar.getInstance();
					wMessage.EditTime = Calendar.getInstance();
					wMessage.ID = 0L;
					wMessage.MessageID = ((Integer) wResult.Result).intValue();
					wMessage.MessageText = StringUtils.Format("{0} {1}发起了跨区借调申请",
							new Object[] { BPMEventModule.ToLoan.getLable(), wLoginUser.Name });
					wMessage.ModuleID = BPMEventModule.ToLoan.getValue();
					wMessage.ResponsorID = wBMSEmployee.ID;
					wMessage.ShiftID = wShiftID;
					wMessage.StationID = 0L;
					wMessage.Title = StringUtils.Format("{0} {1}发起了跨区借调申请",
							new Object[] { BPMEventModule.ToLoan.getLable(), wLoginUser.Name });
					wMessage.Type = BFCMessageType.Task.getValue();
					wBFCMessageList.add(wMessage);
				}
				CoreServiceImpl.getInstance().BFC_UpdateMessageList(wLoginUser, wBFCMessageList);
			}

			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<List<BMSEmployee>> SCH_QueryCanSelectList(BMSEmployee wLoginUser, int wAreaDepartmentID) {
		ServiceResult<List<BMSEmployee>> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			List<BMSEmployee> wEmployeeList = CoreServiceImpl.getInstance().BMS_GetEmployeeAll(wLoginUser, 0, 0, 1)
					.List(BMSEmployee.class);
			if (wEmployeeList == null || wEmployeeList.size() <= 0) {
				return wResult;
			}
			wEmployeeList = (List<BMSEmployee>) wEmployeeList.stream()
					.filter(p -> (p.DepartmentID == wAreaDepartmentID)).collect(Collectors.toList());

			if (wEmployeeList != null && wEmployeeList.size() > 0) {
				wEmployeeList.removeIf(p -> {
					List<SCHSecondment> wTempList = SCHSecondmentDAO.getInstance().SelectList(wLoginUser, -1, -1, -1,
							-1, -1, p.ID, -1, new ArrayList<>(Arrays.asList(SCHSecondStatus.Seconded.getValue())), null,
							null, wErrorCode);

					return (wTempList != null && wTempList.size() > 0);
				});
			}

			wResult.Result = wEmployeeList;
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 通过标签查询任务集合
	 */
	@Override
	public ServiceResult<List<SCHSecondment>> SCH_QuerySecondmentListByTagTypes(BMSEmployee wLoginUser, int wTagTypes) {
		ServiceResult<List<SCHSecondment>> wResult = new ServiceResult<List<SCHSecondment>>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			// 初始化
			wResult.Result = new ArrayList<SCHSecondment>();
			// 待完成状态集合
			List<Integer> wToDoList = new ArrayList<Integer>(
					Arrays.asList(SCHSecondStatus.Apply.getValue(), SCHSecondStatus.ToOtherSecond.getValue()));
			// 七天前、今日23点59分59秒
			Calendar wBeforeDay = CalendarUtil.GetDate(Calendar.getInstance());
			wBeforeDay.add(Calendar.DATE, -7);
			Calendar wToEndDay = Calendar.getInstance();
			wToEndDay.set(Calendar.HOUR_OF_DAY, 23);
			wToEndDay.set(Calendar.MINUTE, 59);
			wToEndDay.set(Calendar.SECOND, 59);
			// 查询所有未完成的任务和七天之内未完成的任务
			wResult.Result.addAll(SCHSecondmentDAO.getInstance().SelectList(wLoginUser, -1, -1, -1, -1, -1, -1, -1,
					wToDoList, null, null, wErrorCode));
			wResult.Result.addAll(SCHSecondmentDAO.getInstance().SelectList(wLoginUser, -1, -1, -1, -1, -1, -1, -1,
					null, wBeforeDay, wToEndDay, wErrorCode));
			// 去重
			wResult.Result = new ArrayList<SCHSecondment>(wResult.Result.stream()
					.collect(Collectors.toMap(SCHSecondment::getID, account -> account, (k1, k2) -> k2)).values());
			// 通过标签筛选任务
			switch (TagTypes.getEnumType(wTagTypes)) {
			case Default:// 默认
			case Approver:// 审批
			case Confirmer:// 确认
				break;
			case Applicant:// 发起
				wResult.Result = wResult.Result.stream().filter(p -> p.SendID == wLoginUser.ID)
						.collect(Collectors.toList());
				break;
			case Dispatcher:// 接收
				// 工区人员列表
				List<LFSWorkAreaChecker> wWorkAreaCheckerList = LFSServiceImpl.getInstance()
						.LFS_QueryWorkAreaCheckerList(wLoginUser).List(LFSWorkAreaChecker.class);
				if (wWorkAreaCheckerList == null || wWorkAreaCheckerList.size() <= 0) {
					return wResult;
				}

				// 若登陆者是工区主管
				if (wWorkAreaCheckerList.stream()
						.anyMatch(p -> p.LeaderIDList.stream().anyMatch(q -> q == wLoginUser.ID))) {
					int wAreaID = wWorkAreaCheckerList.stream()
							.filter(p -> p.LeaderIDList.stream().anyMatch(q -> q == wLoginUser.ID)).findFirst()
							.get().WorkAreaID;
					wResult.Result = wResult.Result.stream().filter(p -> p.IsOverArea == true && p.AreaID == wAreaID)
							.collect(Collectors.toList());
				}
				break;
			default:
				break;
			}
			// 翻译中文
			if (wResult.Result.size() > 0) {
				this.SetText(wResult.Result);
			}
			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 翻译中文
	 * 
	 * @param wList
	 */
	private void SetText(List<SCHSecondment> wList) {
		try {
			for (SCHSecondment wSCHSecondment : wList) {

				wSCHSecondment.SendName = APSConstans.GetBMSEmployeeName(wSCHSecondment.SendID);

				wSCHSecondment.SecondDepartment = APSConstans.GetBMSDepartmentName(wSCHSecondment.SecondDepartmentID);

				wSCHSecondment.BeSecondDepartment = APSConstans
						.GetBMSDepartmentName(wSCHSecondment.BeSecondDepartmentID);

				wSCHSecondment.SecondAuditor = APSConstans.GetBMSEmployeeName(wSCHSecondment.SecondAuditID);

				wSCHSecondment.BeSecondAuditor = APSConstans.GetBMSEmployeeName(wSCHSecondment.BeSecondAuditID);

				wSCHSecondment.SecondPerson = APSConstans.GetBMSEmployeeName(wSCHSecondment.SecondPersonID);

				wSCHSecondment.AreaName = APSConstans.GetBMSDepartmentName(wSCHSecondment.AreaID);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	public ServiceResult<List<BMSEmployee>> SCH_QueryEmoloyeeListByClassList(BMSEmployee wLoginUser,
			List<BMSDepartment> wList) {
		ServiceResult<List<BMSEmployee>> wResult = new ServiceResult<>();
		try {
			Map<Integer, BMSEmployee> wMap = APSConstans.GetBMSEmployeeList();

			wResult.Result = new ArrayList<>();
			BMSEmployee wPerson = null;
			for (BMSDepartment wBMSDepartment : wList) {

				Iterator<Integer> iterator = (wMap.values().stream().filter(p -> (p.DepartmentID == wBMSDepartment.ID))
						.map(p -> Integer.valueOf(p.ID)).collect(Collectors.toList())).iterator();
				while (iterator.hasNext()) {
					int wPersonID = ((Integer) iterator.next()).intValue();
					ServiceResult<List<SCHSecondment>> wJDList = SCH_QuerySecondmentList(wLoginUser, -1, -1, -1, -1, -1,
							wPersonID, null, null, null);
					if (wJDList.Result != null && (wJDList.Result).size() > 0
							&& (wJDList.Result).stream().anyMatch(p -> !(p.Status == SCHSecondStatus.Seconded.getValue()
									&& p.ValidDate.compareTo(Calendar.getInstance()) <= 0))) {
						continue;
					}
					wPerson = APSConstans.GetBMSEmployee(wPersonID);
					wPerson.DepartmentID = wBMSDepartment.ID;
					((List<BMSEmployee>) wResult.Result).add(wPerson);
				}

			}
		} catch (Exception e) {
			wResult.FaultCode = String.valueOf(wResult.FaultCode) + e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<List<BFCMessage>> SFC_GetMessaegList(BMSEmployee wLoginUser, SCHSecondment wSCHSecondment) {
		ServiceResult<List<BFCMessage>> wResult = new ServiceResult<>();
		wResult.Result = new ArrayList<>();
		try {
			int wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, Calendar.getInstance(), APSShiftPeriod.Day,
					FMCShiftLevel.Day, 0);

			BMSEmployee wMonitor = APSUtils.getInstance().APS_GetMonitorByDepartmentID(wLoginUser,
					wSCHSecondment.SecondDepartmentID);

			if (wMonitor != null && wMonitor.ID > 0) {

				BFCMessage wMessage = new BFCMessage();
				wMessage.Active = 0;
				wMessage.CompanyID = wLoginUser.CompanyID;
				wMessage.CreateTime = Calendar.getInstance();
				wMessage.EditTime = Calendar.getInstance();
				wMessage.ID = 0L;
				wMessage.MessageID = wSCHSecondment.ID;
				wMessage.MessageText = StringUtils.Format("{0} {1}借调了{2}",
						new Object[] { BPMEventModule.ToLoan.getLable(), wLoginUser.Name,
								APSConstans.GetBMSEmployeeName(wSCHSecondment.SecondPersonID) });
				wMessage.ModuleID = BPMEventModule.ToLoan.getValue();
				wMessage.ResponsorID = wMonitor.ID;
				wMessage.ShiftID = wShiftID;
				wMessage.StationID = 0L;
				wMessage.Title = StringUtils.Format("{0} {1}借调了{2}", new Object[] { BPMEventModule.ToLoan.getLable(),
						wLoginUser.Name, APSConstans.GetBMSEmployeeName(wSCHSecondment.SecondPersonID) });
				wMessage.Type = BFCMessageType.Notify.getValue();
				((List<BFCMessage>) wResult.Result).add(wMessage);
			}

			wMonitor = APSUtils.getInstance().APS_GetMonitorByDepartmentID(wLoginUser,
					wSCHSecondment.BeSecondDepartmentID);
			if (wMonitor != null && wMonitor.ID > 0) {

				BFCMessage wMessage = new BFCMessage();
				wMessage.Active = 0;
				wMessage.CompanyID = wLoginUser.CompanyID;
				wMessage.CreateTime = Calendar.getInstance();
				wMessage.EditTime = Calendar.getInstance();
				wMessage.ID = 0L;
				wMessage.MessageID = wSCHSecondment.ID;
				wMessage.MessageText = StringUtils.Format("{0} {1}借调了{2}",
						new Object[] { BPMEventModule.ToLoan.getLable(), wLoginUser.Name,
								APSConstans.GetBMSEmployeeName(wSCHSecondment.SecondPersonID) });
				wMessage.ModuleID = BPMEventModule.ToLoan.getValue();
				wMessage.ResponsorID = wMonitor.ID;
				wMessage.ShiftID = wShiftID;
				wMessage.StationID = 0L;
				wMessage.Title = StringUtils.Format("{0} {1}借调了{2}", new Object[] { BPMEventModule.ToLoan.getLable(),
						wLoginUser.Name, APSConstans.GetBMSEmployeeName(wSCHSecondment.SecondPersonID) });
				wMessage.Type = BFCMessageType.Notify.getValue();
				((List<BFCMessage>) wResult.Result).add(wMessage);
			}

			BFCMessage wMessage = new BFCMessage();
			wMessage.Active = 0;
			wMessage.CompanyID = wLoginUser.CompanyID;
			wMessage.CreateTime = Calendar.getInstance();
			wMessage.EditTime = Calendar.getInstance();
			wMessage.ID = 0L;
			wMessage.MessageID = wSCHSecondment.ID;
			wMessage.MessageText = StringUtils.Format("{0} {1}借调了{2}", new Object[] { BPMEventModule.ToLoan.getLable(),
					wLoginUser.Name, APSConstans.GetBMSEmployeeName(wSCHSecondment.SecondPersonID) });
			wMessage.ModuleID = BPMEventModule.ToLoan.getValue();
			wMessage.ResponsorID = wSCHSecondment.SecondPersonID;
			wMessage.ShiftID = wShiftID;
			wMessage.StationID = 0L;
			wMessage.Title = StringUtils.Format("{0} {1}借调了{2}", new Object[] { BPMEventModule.ToLoan.getLable(),
					wLoginUser.Name, APSConstans.GetBMSEmployeeName(wSCHSecondment.SecondPersonID) });
			wMessage.Type = BFCMessageType.Notify.getValue();
			((List<BFCMessage>) wResult.Result).add(wMessage);
		} catch (Exception e) {
			wResult.FaultCode = String.valueOf(wResult.FaultCode) + e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<Integer> SCH_SendMessageToRelaPersons(BMSEmployee wLoginUser,
			List<SCHSecondment> wSCHSecondmentList) {
		ServiceResult<Integer> wResult = new ServiceResult<>();
		try {
			if (wSCHSecondmentList == null || wSCHSecondmentList.size() <= 0) {
				return wResult;
			}

			int wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, Calendar.getInstance(), APSShiftPeriod.Day,
					FMCShiftLevel.Day, 0);

			List<BFCMessage> wList = CoreServiceImpl.getInstance()
					.BFC_GetMessageList(wLoginUser, -1, BPMEventModule.ToLoan.getValue(),
							BFCMessageType.Task.getValue(), BFCMessageStatus.Default.getValue(), -1, null, null)
					.List(BFCMessage.class);
			wList = (List<BFCMessage>) wList.stream()
					.filter(p -> (p.MessageID == ((SCHSecondment) wSCHSecondmentList.get(0)).ID))
					.collect(Collectors.toList());

			List<BFCMessage> wDoneList = (List<BFCMessage>) wList.stream().filter(p -> (p.ResponsorID == wLoginUser.ID))
					.collect(Collectors.toList());
			List<BFCMessage> wCloseList = (List<BFCMessage>) wList.stream()
					.filter(p -> (p.ResponsorID != wLoginUser.ID)).collect(Collectors.toList());
			wDoneList.forEach(p -> {
			});
			wCloseList.forEach(p -> {

			});
			List<BFCMessage> wHandleList = new ArrayList<>();
			wHandleList.addAll(wDoneList);
			wHandleList.addAll(wCloseList);

			List<BFCMessage> wCallList = new ArrayList<>();

			for (SCHSecondment wSCHSecondment : wSCHSecondmentList) {

				BFCMessage wMessage = new BFCMessage();
				wMessage.Active = 0;
				wMessage.CompanyID = 0;
				wMessage.CreateTime = Calendar.getInstance();
				wMessage.EditTime = Calendar.getInstance();
				wMessage.ID = 0L;
				wMessage.MessageID = ((Integer) wResult.Result).intValue();
				wMessage.MessageText = StringUtils.Format("{0} {1}处理了跨区借调申请，借调了{2}",
						new Object[] { BPMEventModule.ToLoan.getLable(), wLoginUser.Name,
								APSConstans.GetBMSEmployeeName(wSCHSecondment.SecondPersonID) });
				wMessage.ModuleID = BPMEventModule.ToLoan.getValue();
				wMessage.ResponsorID = (APSUtils.getInstance().APS_GetMonitorByDepartmentID(wLoginUser,
						wSCHSecondment.SecondDepartmentID)).ID;
				wMessage.ShiftID = wShiftID;
				wMessage.StationID = 0L;
				wMessage.Title = StringUtils.Format("{0} {1}处理了跨区借调申请，借调了{2}",
						new Object[] { BPMEventModule.ToLoan.getLable(), wLoginUser.Name,
								APSConstans.GetBMSEmployeeName(wSCHSecondment.SecondPersonID) });
				wMessage.Type = BFCMessageType.Notify.getValue();
				wCallList.add(wMessage);

				wMessage = new BFCMessage();
				wMessage.Active = 0;
				wMessage.CompanyID = 0;
				wMessage.CreateTime = Calendar.getInstance();
				wMessage.EditTime = Calendar.getInstance();
				wMessage.ID = 0L;
				wMessage.MessageID = ((Integer) wResult.Result).intValue();
				wMessage.MessageText = StringUtils.Format("{0} {1}处理了跨区借调申请，借调了{2}",
						new Object[] { BPMEventModule.ToLoan.getLable(), wLoginUser.Name,
								APSConstans.GetBMSEmployeeName(wSCHSecondment.SecondPersonID) });
				wMessage.ModuleID = BPMEventModule.ToLoan.getValue();
				wMessage.ResponsorID = (APSUtils.getInstance().APS_GetMonitorByDepartmentID(wLoginUser,
						wSCHSecondment.BeSecondDepartmentID)).ID;
				wMessage.ShiftID = wShiftID;
				wMessage.StationID = 0L;
				wMessage.Title = StringUtils.Format("{0} {1}处理了跨区借调申请，借调了{2}",
						new Object[] { BPMEventModule.ToLoan.getLable(), wLoginUser.Name,
								APSConstans.GetBMSEmployeeName(wSCHSecondment.SecondPersonID) });
				wMessage.Type = BFCMessageType.Notify.getValue();
				wCallList.add(wMessage);

				wMessage = new BFCMessage();
				wMessage.Active = 0;
				wMessage.CompanyID = 0;
				wMessage.CreateTime = Calendar.getInstance();
				wMessage.EditTime = Calendar.getInstance();
				wMessage.ID = 0L;
				wMessage.MessageID = ((Integer) wResult.Result).intValue();
				wMessage.MessageText = StringUtils.Format("{0} {1}处理了跨区借调申请，借调了{2}",
						new Object[] { BPMEventModule.ToLoan.getLable(), wLoginUser.Name,
								APSConstans.GetBMSEmployeeName(wSCHSecondment.SecondPersonID) });
				wMessage.ModuleID = BPMEventModule.ToLoan.getValue();
				wMessage.ResponsorID = wSCHSecondment.SecondPersonID;
				wMessage.ShiftID = wShiftID;
				wMessage.StationID = 0L;
				wMessage.Title = StringUtils.Format("{0} {1}处理了跨区借调申请，借调了{2}",
						new Object[] { BPMEventModule.ToLoan.getLable(), wLoginUser.Name,
								APSConstans.GetBMSEmployeeName(wSCHSecondment.SecondPersonID) });
				wMessage.Type = BFCMessageType.Notify.getValue();
				wCallList.add(wMessage);

				wMessage = new BFCMessage();
				wMessage.Active = 0;
				wMessage.CompanyID = 0;
				wMessage.CreateTime = Calendar.getInstance();
				wMessage.EditTime = Calendar.getInstance();
				wMessage.ID = 0L;
				wMessage.MessageID = ((Integer) wResult.Result).intValue();
				wMessage.MessageText = StringUtils.Format("{0} {1}处理了跨区借调申请，借调了{2}",
						new Object[] { BPMEventModule.ToLoan.getLable(), wLoginUser.Name,
								APSConstans.GetBMSEmployeeName(wSCHSecondment.SecondPersonID) });
				wMessage.ModuleID = BPMEventModule.ToLoan.getValue();
				wMessage.ResponsorID = wSCHSecondment.SecondAuditID;
				wMessage.ShiftID = wShiftID;
				wMessage.StationID = 0L;
				wMessage.Title = StringUtils.Format("{0} {1}处理了跨区借调申请，借调了{2}",
						new Object[] { BPMEventModule.ToLoan.getLable(), wLoginUser.Name,
								APSConstans.GetBMSEmployeeName(wSCHSecondment.SecondPersonID) });
				wMessage.Type = BFCMessageType.Notify.getValue();
				wCallList.add(wMessage);
			}

			List<BFCMessage> wAllList = new ArrayList<>();
			wAllList.addAll(wHandleList);
			wAllList.addAll(wCallList);
			CoreServiceImpl.getInstance().BFC_UpdateMessageList(wLoginUser, wAllList);
		} catch (Exception e) {
			wResult.FaultCode = String.valueOf(wResult.FaultCode) + e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<Integer> SFH_DeadlineSecondment(BMSEmployee wLoginUser, int wSCHSecondmentID) {
		ServiceResult<Integer> wResult = new ServiceResult<>(Integer.valueOf(0));
		OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
		try {
			SCHSecondment wTask = SCHSecondmentDAO.getInstance().SelectByID(wLoginUser, wSCHSecondmentID, wErrorCode);
			if (wTask == null || wTask.ID <= 0) {
				wResult.FaultCode = String.valueOf(wResult.FaultCode) + "提示：参数错误!";
				return wResult;
			}

			if (wTask.Status != SCHSecondStatus.Seconded.getValue()) {
				wResult.FaultCode = String.valueOf(wResult.FaultCode) + "提示：请选择状态为“已借调”的单据!";
				return wResult;
			}

			if (wTask.ValidDate.compareTo(Calendar.getInstance()) < 0) {
				wResult.FaultCode = String.valueOf(wResult.FaultCode) + "提示：该借调单的失效时间已到，无法执行此操作!";
				return wResult;
			}

			wTask.ValidDate = Calendar.getInstance();
			wResult.Result = Integer.valueOf(SCHSecondmentDAO.getInstance().Update(wLoginUser, wTask, wErrorCode));

			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			wResult.FaultCode = String.valueOf(wResult.FaultCode) + e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<SCHSecondment>> SCH_QueryAll(BMSEmployee wLoginUser, int wTagType, Calendar wStartTime,
			Calendar wEndTime, int wClassID) {
		ServiceResult<List<SCHSecondment>> wResult = new ServiceResult<List<SCHSecondment>>();
		wResult.Result = new ArrayList<SCHSecondment>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			// 今日时间
			Calendar wTodaySTime = Calendar.getInstance();
			wTodaySTime.set(Calendar.HOUR_OF_DAY, 0);
			wTodaySTime.set(Calendar.MINUTE, 0);
			wTodaySTime.set(Calendar.SECOND, 0);
			Calendar wTodayETime = Calendar.getInstance();
			wTodayETime.set(Calendar.HOUR_OF_DAY, 23);
			wTodayETime.set(Calendar.MINUTE, 59);
			wTodayETime.set(Calendar.SECOND, 59);
			// 基础时间
			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2010, 0, 1);

			int wType = 0;
			if (wStartTime == null || wStartTime.compareTo(wBaseTime) < 0) {
				wType = 1;// 默认
			} else {
				wType = 2;// 时间查询
			}

			switch (TagTypes.getEnumType(wTagType)) {
			case Applicant:// 发起
				if (wType == 1) {
					// ①我发起的有效期内的
					List<SCHSecondment> wList1 = SCHSecondmentDAO.getInstance().SelectList(wLoginUser, -1,
							wLoginUser.ID, -1, -1, -1, -1, -1, null, null, null, wErrorCode);
					wList1 = wList1.stream().filter(p -> p.ValidDate.compareTo(Calendar.getInstance()) > 0)
							.collect(Collectors.toList());
					if (wList1.size() > 0) {
						wResult.Result.addAll(wList1);
					}
					// ②今日发起的
					List<SCHSecondment> wList2 = SCHSecondmentDAO.getInstance().SelectList(wLoginUser, -1,
							wLoginUser.ID, -1, -1, -1, -1, -1, null, wTodaySTime, wTodayETime, wErrorCode);
					if (wList2.size() > 0) {
						wResult.Result.addAll(wList2);
					}
					// ③我发起的，未结束的
					List<SCHSecondment> wList3 = SCHSecondmentDAO.getInstance().SelectList(wLoginUser, -1,
							wLoginUser.ID, -1, -1, -1, -1, -1,
							new ArrayList<Integer>(Arrays.asList(SCHSecondStatus.Default.getValue(),
									SCHSecondStatus.Apply.getValue(), SCHSecondStatus.ToOtherSecond.getValue())),
							null, null, wErrorCode);
					if (wList3.size() > 0) {
						wResult.Result.addAll(wList3);
					}
				} else {
					// ①时间段内我发起的
					List<SCHSecondment> wList2 = SCHSecondmentDAO.getInstance().SelectList(wLoginUser, -1,
							wLoginUser.ID, -1, -1, -1, -1, -1, null, wStartTime, wEndTime, wErrorCode);
					if (wClassID > 0) {
						wList2 = wList2.stream()
								.filter(p -> p.BeSecondDepartmentID == wClassID || p.SecondDepartmentID == wClassID)
								.collect(Collectors.toList());
					}
					if (wList2.size() > 0) {
						wResult.Result.addAll(wList2);
					}
				}
				break;
			case Approver:// 审批
				if (wType == 1) {
					// ①我审批的有效期内的
					List<SCHSecondment> wList1 = SCHSecondmentDAO.getInstance().SelectList(wLoginUser, -1, -1, -1, -1,
							wLoginUser.ID, -1, -1, null, null, null, wErrorCode);
					wList1 = wList1.stream().filter(p -> p.ValidDate.compareTo(Calendar.getInstance()) > 0)
							.collect(Collectors.toList());
					if (wList1.size() > 0) {
						wResult.Result.addAll(wList1);
					}
					// ②今日审批的
					List<SCHSecondment> wList2 = SCHSecondmentDAO.getInstance().SelectList(wLoginUser, -1, -1, -1, -1,
							wLoginUser.ID, -1, -1, null, wTodaySTime, wTodayETime, wErrorCode);
					if (wList2.size() > 0) {
						wResult.Result.addAll(wList2);
					}
					// ③待我审批的
					List<SCHSecondment> wList3 = SCHSecondmentDAO.getInstance().SelectList(wLoginUser, -1, -1, -1, -1,
							-1, -1, -1, new ArrayList<Integer>(Arrays.asList(SCHSecondStatus.ToOtherSecond.getValue())),
							null, null, wErrorCode);
					for (SCHSecondment wSCHSecondment : wList3) {
						List<LFSWorkAreaChecker> wChecker = APSConstans.GetLFSWorkAreaCheckerList().stream()
								.filter(p -> p.WorkAreaID == wSCHSecondment.AreaID).collect(Collectors.toList());
						if (wChecker == null || wChecker.size() <= 0) {
							continue;
						}

						if (wChecker.get(0).LeaderIDList == null || wChecker.get(0).LeaderIDList.size() <= 0) {
							continue;
						}

						if (!wChecker.get(0).LeaderIDList.stream().anyMatch(p -> p == wLoginUser.ID)) {
							continue;
						}

						wResult.Result.add(wSCHSecondment);
					}
				} else {
					// ①时间段内我审批的
					List<SCHSecondment> wList2 = SCHSecondmentDAO.getInstance().SelectList(wLoginUser, -1, -1, -1, -1,
							wLoginUser.ID, -1, -1, null, wStartTime, wEndTime, wErrorCode);
					if (wList2.size() > 0) {
						wResult.Result.addAll(wList2);
					}
					// ②时间段内待我审批的
					List<SCHSecondment> wList3 = SCHSecondmentDAO.getInstance().SelectList(wLoginUser, -1, -1, -1, -1,
							-1, -1, -1, new ArrayList<Integer>(Arrays.asList(SCHSecondStatus.ToOtherSecond.getValue())),
							wStartTime, wEndTime, wErrorCode);
					for (SCHSecondment wSCHSecondment : wList3) {
						List<LFSWorkAreaChecker> wChecker = APSConstans.GetLFSWorkAreaCheckerList().stream()
								.filter(p -> p.WorkAreaID == wSCHSecondment.AreaID).collect(Collectors.toList());
						if (wChecker == null || wChecker.size() <= 0) {
							continue;
						}

						if (wChecker.get(0).LeaderIDList == null || wChecker.get(0).LeaderIDList.size() <= 0) {
							continue;
						}

						if (!wChecker.get(0).LeaderIDList.stream().anyMatch(p -> p == wLoginUser.ID)) {
							continue;
						}

						wResult.Result.add(wSCHSecondment);
					}
				}
				break;
			default:
				break;
			}

			if (wResult.Result.size() > 0) {
				// 去重
				wResult.Result = new ArrayList<SCHSecondment>(wResult.Result.stream()
						.collect(Collectors.toMap(SCHSecondment::getID, account -> account, (k1, k2) -> k2)).values());
				// 根据发起时间逆序排序
				wResult.Result.sort(Comparator.comparing(SCHSecondment::getSendTime, Comparator.reverseOrder()));
				// 翻译
				this.SetText(wResult.Result);
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<SCHSecondmentBPM> SCH_QueryDefaultSecondementBPM(BMSEmployee wLoginUser, int wEventID) {
		ServiceResult<SCHSecondmentBPM> wResult = new ServiceResult<SCHSecondmentBPM>();
		wResult.Result = new SCHSecondmentBPM();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			List<SCHSecondmentBPM> wList = SCHSecondmentBPMDAO.getInstance().SelectList(wLoginUser, -1, "",
					wLoginUser.ID, "", -1, -1, new ArrayList<Integer>(Arrays.asList(0)), null, null, wErrorCode);
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
	public synchronized ServiceResult<SCHSecondmentBPM> SCH_CreateSecondementBPM(BMSEmployee wLoginUser,
			BPMEventModule wEventID) {
		ServiceResult<SCHSecondmentBPM> wResult = new ServiceResult<SCHSecondmentBPM>();
		wResult.Result = new SCHSecondmentBPM();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			wResult.Result.Code = SCHSecondmentBPMDAO.getInstance().GetNewCode(wLoginUser, wErrorCode);
			wResult.Result.UpFlowID = wLoginUser.ID;
			wResult.Result.UpFlowName = wLoginUser.Name;
			wResult.Result.CreateTime = Calendar.getInstance();
			wResult.Result.SubmitTime = Calendar.getInstance();
			wResult.Result.ID = 0;
			wResult.Result.Status = SCHSecondmentBPMStatus.Default.getValue();
			wResult.Result.Type = SCHSecondmentApplyType.ClassLoan.getValue();
			wResult.Result.FlowType = wEventID.getValue();

			wResult.Result = (SCHSecondmentBPM) SCHSecondmentBPMDAO.getInstance().BPM_UpdateTask(wLoginUser,
					wResult.Result, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<SCHSecondmentBPM> SCH_SubmitSecondementBPM(BMSEmployee wLoginUser, SCHSecondmentBPM wData) {
		ServiceResult<SCHSecondmentBPM> wResult = new ServiceResult<SCHSecondmentBPM>();
		wResult.Result = new SCHSecondmentBPM();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			if (wData.Status == SCHSecondmentApplyStatus.NomalClose.getValue()) {
				wData.StatusText = SCHSecondmentApplyStatus.NomalClose.getLable();
			}

			wResult.Result = (SCHSecondmentBPM) SCHSecondmentBPMDAO.getInstance().BPM_UpdateTask(wLoginUser, wData,
					wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<SCHSecondmentBPM> SCH_GetSecondementBPM(BMSEmployee wLoginUser, int wID) {
		ServiceResult<SCHSecondmentBPM> wResult = new ServiceResult<SCHSecondmentBPM>();
		wResult.Result = new SCHSecondmentBPM();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			wResult.Result = (SCHSecondmentBPM) SCHSecondmentBPMDAO.getInstance().BPM_GetTaskInfo(wLoginUser, wID, "",
					wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<SCHSecondmentBPM>> SFC_QuerySecondementBPMEmployeeAll(BMSEmployee wLoginUser,
			int wTagTypes, Calendar wStartTime, Calendar wEndTime, int wType) {
		ServiceResult<List<BPMTaskBase>> wResult = new ServiceResult<List<BPMTaskBase>>();
		wResult.Result = new ArrayList<BPMTaskBase>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		ServiceResult<List<SCHSecondmentBPM>> wList = new ServiceResult<List<SCHSecondmentBPM>>();
		wList.Result = new ArrayList<SCHSecondmentBPM>();
		try {
			switch (TagTypes.getEnumType(wTagTypes)) {
			case Applicant:// 2发起
				wResult.Result = SCHSecondmentBPMDAO.getInstance().BPM_GetSendTaskList(wLoginUser, wLoginUser.ID,
						wStartTime, wEndTime, wErrorCode);
				break;
			case Dispatcher:// 1待做
				wResult.Result = SCHSecondmentBPMDAO.getInstance().BPM_GetUndoTaskList(wLoginUser, wLoginUser.ID,
						wErrorCode);
				break;
			case Approver:// 4已做
				wResult.Result = SCHSecondmentBPMDAO.getInstance().BPM_GetDoneTaskList(wLoginUser, wLoginUser.ID,
						wStartTime, wEndTime, wErrorCode);
				break;
			default:
				break;
			}

			if (wResult.Result.size() > 0) {
				wResult.Result = wResult.Result.stream().filter(p -> p.Status != 0).collect(Collectors.toList());

				for (BPMTaskBase wBPMTaskBase : wResult.Result) {
					SCHSecondmentBPM wItem = (SCHSecondmentBPM) wBPMTaskBase;
					if (wItem.Type != wType) {
						continue;
					}

					wList.Result.add(wItem);
				}
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wList;
	}

	@Override
	public ServiceResult<List<SCHSecondmentBPM>> SCH_QuerySecondmentBPMHistory(BMSEmployee wLoginUser, int wID,
			String wCode, int wUpFlowID, int wFollowerID, int wPersonID, int wType, Calendar wStartTime,
			Calendar wEndTime) {
		ServiceResult<List<SCHSecondmentBPM>> wResult = new ServiceResult<List<SCHSecondmentBPM>>();
		wResult.Result = new ArrayList<SCHSecondmentBPM>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			wResult.Result = SCHSecondmentBPMDAO.getInstance().SelectList(wLoginUser, wID, wCode, wUpFlowID,
					String.valueOf(wFollowerID), wType, wPersonID, null, wStartTime, wEndTime, wErrorCode);

			if (wResult.Result.size() > 0) {
				wResult.Result.removeIf(p -> p.Status == 0);
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<SCHSecondmentApply> SCH_QueryDefaultSecondmentApply(BMSEmployee wLoginUser, int wEventID) {
		ServiceResult<SCHSecondmentApply> wResult = new ServiceResult<SCHSecondmentApply>();
		wResult.Result = new SCHSecondmentApply();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

//			int wType = 0;
//			switch (BPMEventModule.getEnumType(wEventID)) {
//			case ToLoanApply:
//				wType = SCHSecondmentApplyType.TAreaLoan.getValue();
//				break;
//			case ToLoanApplyK:
//				wType = SCHSecondmentApplyType.KAreaLoan.getValue();
//				break;
//			default:
//				break;
//			}

			List<SCHSecondmentApply> wList = SCHSecondmentApplyDAO.getInstance().SelectList(wLoginUser, -1, "", -1, -1,
					-1, null, null, new ArrayList<Integer>(Arrays.asList(0)), wErrorCode);
			if (wList.size() > 0) {
				SCHSecondmentApplyDAO.getInstance().DeleteList(wLoginUser, wList, wErrorCode);
//				wResult.Result = wList.get(0);
//
//				wResult.Result.CreateTime = Calendar.getInstance();
//				String[] wStrs = wResult.Result.Code.split("-");
//				int wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, Calendar.getInstance(),
//						APSShiftPeriod.Day, FMCShiftLevel.Day, 0);
//				wResult.Result.Code = SCHSecondmentApplyDAO.getInstance().GetNewCode(wLoginUser, wErrorCode);
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public synchronized ServiceResult<SCHSecondmentApply> SCH_CreateSecondmentApply(BMSEmployee wLoginUser,
			BPMEventModule wEventID) {
		ServiceResult<SCHSecondmentApply> wResult = new ServiceResult<SCHSecondmentApply>();
		wResult.Result = new SCHSecondmentApply();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			wResult.Result.Code = SCHSecondmentApplyDAO.getInstance().GetNewCode(wLoginUser, wErrorCode);
			wResult.Result.UpFlowID = wLoginUser.ID;
			wResult.Result.UpFlowName = wLoginUser.Name;
			wResult.Result.CreateTime = Calendar.getInstance();
			wResult.Result.SubmitTime = Calendar.getInstance();
			wResult.Result.ID = 0;
			wResult.Result.Status = SCHSecondmentApplyStatus.Default.getValue();
			wResult.Result.StatusText = "";
			wResult.Result.AreaID = this.GetAreaID(wLoginUser);
			wResult.Result.AreaName = APSConstans.GetBMSDepartmentName(wResult.Result.AreaID);
			switch (wEventID) {
			case ToLoanApply:
				wResult.Result.Type = SCHSecondmentApplyType.TAreaLoan.getValue();
				break;
			case ToLoanApplyK:
				wResult.Result.Type = SCHSecondmentApplyType.KAreaLoan.getValue();
				break;
			default:
				break;
			}
			wResult.Result.FlowType = wEventID.getValue();

			wResult.Result = (SCHSecondmentApply) SCHSecondmentApplyDAO.getInstance().BPM_UpdateTask(wLoginUser,
					wResult.Result, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 根据登录人获取工区ID
	 */
	private int GetAreaID(BMSEmployee wLoginUser) {
		int wResult = 0;
		try {
			if (APSConstans.GetLFSWorkAreaCheckerList().stream()
					.anyMatch(p -> p.LeaderIDList != null && p.LeaderIDList.size() > 0 && p.Active == 1
							&& p.LeaderIDList.stream().anyMatch(q -> q == wLoginUser.ID))) {
				wResult = APSConstans.GetLFSWorkAreaCheckerList().stream()
						.filter(p -> p.LeaderIDList != null && p.LeaderIDList.size() > 0 && p.Active == 1
								&& p.LeaderIDList.stream().anyMatch(q -> q == wLoginUser.ID))
						.findFirst().get().WorkAreaID;
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<SCHSecondmentApply> SCH_SubmitSecondmentApply(BMSEmployee wLoginUser,
			SCHSecondmentApply wData) {
		ServiceResult<SCHSecondmentApply> wResult = new ServiceResult<SCHSecondmentApply>();
		wResult.Result = new SCHSecondmentApply();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			if (wData.Status == SCHSecondmentApplyStatus.NomalClose.getValue()) {
				wData.StatusText = SCHSecondmentApplyStatus.NomalClose.getLable();
			} else if (wData.Status == SCHSecondmentApplyStatus.ExceptionClose.getValue()) {
				wData.StatusText = SCHSecondmentApplyStatus.ExceptionClose.getLable();
			}

			if ((wData.Type == 2 && wData.Status == 6 && wData.FlowType == BPMEventModule.ToLoanApply.getValue())
					|| (wData.Type == 1 && wData.Status == 8
							&& wData.FlowType == BPMEventModule.ToLoanApplyK.getValue())) {
				wData.Status = 20;
				wData.StatusText = "已确认";
			}

			wResult.Result = (SCHSecondmentApply) SCHSecondmentApplyDAO.getInstance().BPM_UpdateTask(wLoginUser, wData,
					wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<SCHSecondmentApply> SCH_GetSecondmentApply(BMSEmployee wLoginUser, int wID) {
		ServiceResult<SCHSecondmentApply> wResult = new ServiceResult<SCHSecondmentApply>();
		wResult.Result = new SCHSecondmentApply();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			wResult.Result = (SCHSecondmentApply) SCHSecondmentApplyDAO.getInstance().BPM_GetTaskInfo(wLoginUser, wID,
					"", wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<SCHSecondmentApply>> SCH_QuerySecondmentApplyEmployeeAll(BMSEmployee wLoginUser,
			int wTagTypes, Calendar wStartTime, Calendar wEndTime, int wType) {
		ServiceResult<List<BPMTaskBase>> wResult = new ServiceResult<List<BPMTaskBase>>();
		wResult.Result = new ArrayList<BPMTaskBase>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		ServiceResult<List<SCHSecondmentApply>> wList = new ServiceResult<List<SCHSecondmentApply>>();
		wList.Result = new ArrayList<SCHSecondmentApply>();
		try {
			switch (TagTypes.getEnumType(wTagTypes)) {
			case Applicant:// 2发起
				wResult.Result = SCHSecondmentApplyDAO.getInstance().BPM_GetSendTaskList(wLoginUser, wLoginUser.ID,
						wStartTime, wEndTime, wErrorCode);
				break;
			case Dispatcher:// 1待做
				wResult.Result = SCHSecondmentApplyDAO.getInstance().BPM_GetUndoTaskList(wLoginUser, wLoginUser.ID,
						wErrorCode);
				break;
			case Approver:// 4已做
				wResult.Result = SCHSecondmentApplyDAO.getInstance().BPM_GetDoneTaskList(wLoginUser, wLoginUser.ID,
						wStartTime, wEndTime, wErrorCode);
				break;
			default:
				break;
			}

			if (wResult.Result.size() > 0) {
				wResult.Result = wResult.Result.stream().filter(p -> p.Status != 0).collect(Collectors.toList());

				for (BPMTaskBase wBPMTaskBase : wResult.Result) {
					SCHSecondmentApply wItem = (SCHSecondmentApply) wBPMTaskBase;
					if (wItem.Type != wType)
						continue;

					wList.Result.add(wItem);
				}
			}

			// 按照申请时刻降序排列
			if (wList.Result.size() > 0) {
				wList.Result = new ArrayList<SCHSecondmentApply>(wList.Result.stream()
						.collect(Collectors.toMap(SCHSecondmentApply::getID, account -> account, (k1, k2) -> k2))
						.values());

				wList.Result.sort(Comparator.comparing(SCHSecondmentApply::getCreateTime, Comparator.reverseOrder()));
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wList;
	}

	@Override
	public ServiceResult<List<SCHSecondmentApply>> SCH_QuerySecondmentApplyHistory(BMSEmployee wLoginUser, int wID,
			String wCode, int wUpFlowID, int wPersonID, int wType, Calendar wStartTime, Calendar wEndTime) {
		ServiceResult<List<SCHSecondmentApply>> wResult = new ServiceResult<List<SCHSecondmentApply>>();
		wResult.Result = new ArrayList<SCHSecondmentApply>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			wResult.Result = SCHSecondmentApplyDAO.getInstance().SelectList(wLoginUser, wID, wCode, wUpFlowID, wType,
					wPersonID, wStartTime, wEndTime, null, wErrorCode);

			if (wResult.Result.size() > 0) {
				wResult.Result.removeIf(p -> p.Status == 0);
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> SCH_QueryPersonInfo(BMSEmployee wLoginUser) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			BMSDepartment wOldClass = new BMSDepartment();
			List<FPCPart> wOldPartList = new ArrayList<FPCPart>();
			List<BMSPosition> wOldPositionList = new ArrayList<BMSPosition>();
			List<BMSEmployee> wEmployeeList = new ArrayList<BMSEmployee>();

			wOldClass = APSConstans.GetBMSDepartment(wLoginUser.DepartmentID);

			List<FMCWorkCharge> wChargeList = APSConstans.GetFMCWorkChargeList().stream()
					.filter(p -> p.Active == 1 && p.ClassID == wLoginUser.DepartmentID).collect(Collectors.toList());
			if (wChargeList != null && wChargeList.size() > 0) {
				wOldPartList = wChargeList.stream().map(p -> APSConstans.GetFPCPart(p.StationID))
						.collect(Collectors.toList());
				wOldPartList.removeIf(p -> p.ID <= 0 || p.Active != 1);
			}

			wOldPositionList = APSConstans.GetBMSPositionList().values().stream()
					.filter(p -> p.Active == 1 && p.DepartmentID == wLoginUser.DepartmentID)
					.collect(Collectors.toList());

			wEmployeeList = APSConstans.GetBMSEmployeeList().values().stream()
					.filter(p -> p.Active == 1 && p.DepartmentID == wLoginUser.DepartmentID)
					.collect(Collectors.toList());

			// ①班组内借调人员剔除
			List<SCHSecondmentBPM> wList1 = SCHSecondmentBPMDAO.getInstance().SelectList(wLoginUser, -1, "", -1, "", 3,
					-1, new ArrayList<Integer>(Arrays.asList(1)), null, null, wErrorCode);
			if (wList1 != null && wList1.size() > 0) {
				List<Integer> wPersonIDList1 = new ArrayList<Integer>();
				SetPersonList(wList1, wPersonIDList1);
				wEmployeeList.removeIf(p -> wPersonIDList1.stream().anyMatch(q -> q == p.ID));
			}
			List<SCHSecondmentBPM> wList2 = SCHSecondmentBPMDAO.getInstance().SelectList(wLoginUser, -1, "", -1, "", 3,
					-1, new ArrayList<Integer>(Arrays.asList(20)), null, null, wErrorCode);
			if (wList2 != null && wList2.size() > 0) {
				wList2 = wList2.stream().filter(p -> Calendar.getInstance().compareTo(p.StartTime) >= 0
						&& Calendar.getInstance().compareTo(p.EndTime) <= 0).collect(Collectors.toList());
				List<Integer> wPersonIDList2 = new ArrayList<Integer>();
				SetPersonList(wList2, wPersonIDList2);
				if (wPersonIDList2.size() > 0) {
					wEmployeeList.removeIf(p -> wPersonIDList2.stream().anyMatch(q -> q == p.ID));
				}
			}
			// 剔除处于借调流程中的未结束的人员
			List<SCHSecondmentApply> wList = SCHSecondmentApplyDAO.getInstance().SelectList(wLoginUser, -1, "", -1, -1,
					-1, null, null, new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6)), wErrorCode);
			wEmployeeList.removeIf(p -> wList.stream().anyMatch(q -> q.PersonID.contains(String.valueOf(p.ID))));
			// 剔除借调确认的，在有效期内的人
			List<SCHSecondmentApply> wValidList = SCHSecondmentApplyDAO.getInstance().SelectList(wLoginUser, -1, "", -1,
					-1, -1, null, null, new ArrayList<Integer>(Arrays.asList(20)), wErrorCode);
			List<SCHSecondmentApply> wValidList1 = wValidList.stream()
					.filter(p -> p.EndTime.compareTo(Calendar.getInstance()) > 0).collect(Collectors.toList());
			wEmployeeList.removeIf(p -> wValidList1.stream().anyMatch(q -> q.PersonID.contains(String.valueOf(p.ID))));

			wResult.CustomResult.put("OldClass", wOldClass);
			wResult.CustomResult.put("OldPartList", wOldPartList);
			wResult.CustomResult.put("OldPositionList", wOldPositionList);
			wResult.CustomResult.put("EmployeeList", wEmployeeList);
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	private void SetPersonList(List<SCHSecondmentBPM> wList1, List<Integer> wPersonIDList1) {
		try {
			List<String> wStrList = wList1.stream().map(p -> p.PersonID).distinct().collect(Collectors.toList());
			for (String wStr : wStrList) {
				String[] wIDS = wStr.split(",");
				for (String wID : wIDS) {
					Integer wIntID = StringUtils.parseInt(wID);
					if (wIntID > 0) {
						wPersonIDList1.add(wIntID);
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	@Override
	public ServiceResult<Integer> SCH_QueryPersonInfo(BMSEmployee wLoginUser, int wPerson) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>();
		try {
			BMSDepartment wOldClass = new BMSDepartment();
			List<FPCPart> wOldPartList = new ArrayList<FPCPart>();
			List<BMSPosition> wOldPositionList = new ArrayList<BMSPosition>();
			List<BMSEmployee> wEmployeeList = new ArrayList<BMSEmployee>();

			BMSEmployee wPeople = APSConstans.GetBMSEmployee(wPerson);

			wOldClass = APSConstans.GetBMSDepartment(wPeople.DepartmentID);

			List<FMCWorkCharge> wChargeList = APSConstans.GetFMCWorkChargeList().stream()
					.filter(p -> p.Active == 1 && p.ClassID == wPeople.DepartmentID).collect(Collectors.toList());
			if (wChargeList != null && wChargeList.size() > 0) {
				wOldPartList = wChargeList.stream().map(p -> APSConstans.GetFPCPart(p.StationID))
						.collect(Collectors.toList());
				wOldPartList.removeIf(p -> p.ID <= 0 || p.Active != 1);
			}

			wOldPositionList = APSConstans.GetBMSPositionList().values().stream()
					.filter(p -> p.Active == 1 && p.DepartmentID == wPeople.DepartmentID).collect(Collectors.toList());

			wEmployeeList = APSConstans.GetBMSEmployeeList().values().stream()
					.filter(p -> p.Active == 1 && p.DepartmentID == wPeople.DepartmentID).collect(Collectors.toList());

			wResult.CustomResult.put("OldClass", wOldClass);
			wResult.CustomResult.put("OldPartList", wOldPartList);
			wResult.CustomResult.put("OldPositionList", wOldPositionList);
			wResult.CustomResult.put("EmployeeList", wEmployeeList);
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<BMSDepartment>> SCH_QueryAreaClassList(BMSEmployee wLoginUser, int wAreaID) {
		ServiceResult<List<BMSDepartment>> wResult = new ServiceResult<List<BMSDepartment>>();
		wResult.Result = new ArrayList<BMSDepartment>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			if (wAreaID <= 0) {
				return wResult;
			}

			wResult.Result = APSConstans.GetBMSDepartmentList().values().stream()
					.filter(p -> p.Active == 1 && p.ParentID == wAreaID).collect(Collectors.toList());

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<BMSEmployee>> SFC_QueryEmployeePartList(BMSEmployee wLoginUser, int wClassID) {
		ServiceResult<List<BMSEmployee>> wResult = new ServiceResult<List<BMSEmployee>>();
		wResult.Result = new ArrayList<BMSEmployee>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			if (wClassID <= 0) {
				return wResult;
			}

			wResult.Result = APSConstans.GetBMSEmployeeList().values().stream()
					.filter(p -> p.Active == 1 && p.DepartmentID == wClassID).collect(Collectors.toList());

			// 剔除处于借调流程中的未结束的人员
			List<SCHSecondmentApply> wList = SCHSecondmentApplyDAO.getInstance().SelectList(wLoginUser, -1, "", -1, -1,
					-1, null, null, new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6)), wErrorCode);
			wResult.Result.removeIf(p -> wList.stream().anyMatch(q -> q.PersonID.contains(String.valueOf(p.ID))));
			// 剔除借调确认的，在有效期内的人
//			List<SCHSecondmentApply> wValidList = SCHSecondmentApplyDAO.getInstance().SelectList(wLoginUser, -1, "", -1,
//					-1, -1, null, null, new ArrayList<Integer>(Arrays.asList(20)), wErrorCode);
//			List<SCHSecondmentApply> wValidList1 = wValidList.stream()
//					.filter(p -> p.EndTime.compareTo(Calendar.getInstance()) > 0).collect(Collectors.toList());
//			wResult.Result.removeIf(p -> wValidList1.stream().anyMatch(q -> q.PersonID.contains(String.valueOf(p.ID))));

			List<FMCWorkCharge> wChargeList = APSConstans.GetFMCWorkChargeList().stream()
					.filter(p -> p.ClassID == wClassID && p.Active == 1).collect(Collectors.toList());
			List<FPCPart> wPartList = new ArrayList<FPCPart>();
			for (FMCWorkCharge wFMCWorkCharge : wChargeList) {
				if (wFMCWorkCharge.StationID <= 0) {
					continue;
				}

				FPCPart wPart = APSConstans.GetFPCPart(wFMCWorkCharge.StationID);
				if (wPart == null || wPart.ID <= 0) {
					continue;
				}

				wPartList.add(wPart);
			}

			wResult.CustomResult.put("PartList", wPartList);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> SCH_DeadLine(BMSEmployee wLoginUser, SCHSecondmentApply wData) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			if (wData.Status != 20) {
				wResult.FaultCode += "提示：该借调单未审批完，无法执行此操作!";
				return wResult;
			}

			if (wData.EndTime.compareTo(Calendar.getInstance()) < 0) {
				wResult.FaultCode += "提示：该借调单已过期，无法执行此操作!";
				return wResult;
			}

			wData.EndTime = Calendar.getInstance();
			SCHSecondmentApplyDAO.getInstance().BPM_UpdateTask(wLoginUser, wData, wErrorCode);
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<SCHSecondmentApply>> SCH_QuerySecondmentApplyHistoryAll(BMSEmployee wLoginUser, int wID,
			String wCode, int wUpFlowID, int wPersonID, int wType, Calendar wStartTime, Calendar wEndTime) {
		ServiceResult<List<SCHSecondmentApply>> wResult = new ServiceResult<List<SCHSecondmentApply>>();
		wResult.Result = new ArrayList<SCHSecondmentApply>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			switch (SCHSecondmentApplyType.getEnumType(wType)) {
			case ClassLoan:
				List<SCHSecondmentBPM> wList = SCHSecondmentBPMDAO.getInstance().SelectList(wLoginUser, wID, wCode,
						wUpFlowID, "", wType, wPersonID, null, wStartTime, wEndTime, wErrorCode);
				for (SCHSecondmentBPM wSCHSecondmentBPM : wList) {
					SCHSecondmentApply wSCHSecondmentApply = new SCHSecondmentApply();

					wSCHSecondmentApply.Code = wSCHSecondmentBPM.Code;
					wSCHSecondmentApply.CreateTime = wSCHSecondmentBPM.CreateTime;
					wSCHSecondmentApply.EndTime = wSCHSecondmentBPM.EndTime;
					wSCHSecondmentApply.FlowID = wSCHSecondmentBPM.FlowID;
					wSCHSecondmentApply.FlowType = wSCHSecondmentBPM.FlowType;
					wSCHSecondmentApply.FollowerID = wSCHSecondmentBPM.FollowerID;
					wSCHSecondmentApply.FollowerName = wSCHSecondmentBPM.FollowerName;
					wSCHSecondmentApply.ID = wSCHSecondmentBPM.ID;
					wSCHSecondmentApply.OldClassID = wSCHSecondmentBPM.OldClassID;
					wSCHSecondmentApply.OldClassName = wSCHSecondmentBPM.OldClassName;
					wSCHSecondmentApply.OldPartList = wSCHSecondmentBPM.OldPartList;
					wSCHSecondmentApply.OldPartNames = wSCHSecondmentBPM.OldPartNames;
					wSCHSecondmentApply.NewClassID = wSCHSecondmentBPM.NewClassID;
					wSCHSecondmentApply.NewClassName = wSCHSecondmentBPM.NewClassName;
					wSCHSecondmentApply.NewPartList = wSCHSecondmentBPM.NewPartList;
					wSCHSecondmentApply.NewPartNames = wSCHSecondmentBPM.NewPartNames;
					wSCHSecondmentApply.OldPosition = wSCHSecondmentBPM.OldPosition;
					wSCHSecondmentApply.NewPosition = wSCHSecondmentBPM.NewPosition;
					wSCHSecondmentApply.PersonID = wSCHSecondmentBPM.PersonID;
					wSCHSecondmentApply.PersonName = wSCHSecondmentBPM.PersonName;
					wSCHSecondmentApply.StartTime = wSCHSecondmentBPM.StartTime;
					wSCHSecondmentApply.Status = wSCHSecondmentBPM.Status;
					wSCHSecondmentApply.StatusText = wSCHSecondmentBPM.StatusText;
					wSCHSecondmentApply.StepID = wSCHSecondmentBPM.StepID;
					wSCHSecondmentApply.SubmitTime = wSCHSecondmentBPM.SubmitTime;
					wSCHSecondmentApply.Type = wSCHSecondmentBPM.Type;
					wSCHSecondmentApply.UpFlowID = wSCHSecondmentBPM.UpFlowID;
					wSCHSecondmentApply.UpFlowName = wSCHSecondmentBPM.UpFlowName;

					wResult.Result.add(wSCHSecondmentApply);
				}
				break;
			case KAreaLoan:
			case TAreaLoan:
				wResult.Result = SCHSecondmentApplyDAO.getInstance().SelectList(wLoginUser, wID, wCode, wUpFlowID,
						wType, wPersonID, wStartTime, wEndTime, null, wErrorCode);
				break;
			case Default:
				wList = SCHSecondmentBPMDAO.getInstance().SelectList(wLoginUser, wID, wCode, wUpFlowID, "", wType,
						wPersonID, null, wStartTime, wEndTime, wErrorCode);
				for (SCHSecondmentBPM wSCHSecondmentBPM : wList) {
					SCHSecondmentApply wSCHSecondmentApply = new SCHSecondmentApply();

					wSCHSecondmentApply.Code = wSCHSecondmentBPM.Code;
					wSCHSecondmentApply.CreateTime = wSCHSecondmentBPM.CreateTime;
					wSCHSecondmentApply.EndTime = wSCHSecondmentBPM.EndTime;
					wSCHSecondmentApply.FlowID = wSCHSecondmentBPM.FlowID;
					wSCHSecondmentApply.FlowType = wSCHSecondmentBPM.FlowType;
					wSCHSecondmentApply.FollowerID = wSCHSecondmentBPM.FollowerID;
					wSCHSecondmentApply.FollowerName = wSCHSecondmentBPM.FollowerName;
					wSCHSecondmentApply.ID = wSCHSecondmentBPM.ID;
					wSCHSecondmentApply.OldClassID = wSCHSecondmentBPM.OldClassID;
					wSCHSecondmentApply.OldClassName = wSCHSecondmentBPM.OldClassName;
					wSCHSecondmentApply.OldPartList = wSCHSecondmentBPM.OldPartList;
					wSCHSecondmentApply.OldPartNames = wSCHSecondmentBPM.OldPartNames;
					wSCHSecondmentApply.NewClassID = wSCHSecondmentBPM.NewClassID;
					wSCHSecondmentApply.NewClassName = wSCHSecondmentBPM.NewClassName;
					wSCHSecondmentApply.NewPartList = wSCHSecondmentBPM.NewPartList;
					wSCHSecondmentApply.NewPartNames = wSCHSecondmentBPM.NewPartNames;
					wSCHSecondmentApply.OldPosition = wSCHSecondmentBPM.OldPosition;
					wSCHSecondmentApply.NewPosition = wSCHSecondmentBPM.NewPosition;
					wSCHSecondmentApply.PersonID = wSCHSecondmentBPM.PersonID;
					wSCHSecondmentApply.PersonName = wSCHSecondmentBPM.PersonName;
					wSCHSecondmentApply.StartTime = wSCHSecondmentBPM.StartTime;
					wSCHSecondmentApply.Status = wSCHSecondmentBPM.Status;
					wSCHSecondmentApply.StatusText = wSCHSecondmentBPM.StatusText;
					wSCHSecondmentApply.StepID = wSCHSecondmentBPM.StepID;
					wSCHSecondmentApply.SubmitTime = wSCHSecondmentBPM.SubmitTime;
					wSCHSecondmentApply.Type = wSCHSecondmentBPM.Type;
					wSCHSecondmentApply.UpFlowID = wSCHSecondmentBPM.UpFlowID;
					wSCHSecondmentApply.UpFlowName = wSCHSecondmentBPM.UpFlowName;

					wResult.Result.add(wSCHSecondmentApply);
				}
				wResult.Result.addAll(SCHSecondmentApplyDAO.getInstance().SelectList(wLoginUser, wID, wCode, wUpFlowID,
						wType, wPersonID, wStartTime, wEndTime, null, wErrorCode));
				break;
			default:
				break;
			}

			if (wResult.Result.size() > 0) {
				wResult.Result.removeIf(p -> p.Status == 0);
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<SCHSecondmentApply>> SCH_QuerySecondmentApplyEmployeeAllPro(BMSEmployee wLoginUser,
			int wTagTypes, Calendar wStartTime, Calendar wEndTime, int wType) {
		ServiceResult<List<BPMTaskBase>> wResult = new ServiceResult<List<BPMTaskBase>>();
		wResult.Result = new ArrayList<BPMTaskBase>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		ServiceResult<List<SCHSecondmentApply>> wList = new ServiceResult<List<SCHSecondmentApply>>();
		wList.Result = new ArrayList<SCHSecondmentApply>();
		try {
			switch (SCHSecondmentApplyType.getEnumType(wType)) {
			case ClassLoan:
				switch (TagTypes.getEnumType(wTagTypes)) {
				case Applicant:// 2发起
					wResult.Result = SCHSecondmentBPMDAO.getInstance().BPM_GetSendTaskList(wLoginUser, wLoginUser.ID,
							wStartTime, wEndTime, wErrorCode);
					break;
				case Dispatcher:// 1待做
					wResult.Result = SCHSecondmentBPMDAO.getInstance().BPM_GetUndoTaskList(wLoginUser, wLoginUser.ID,
							wErrorCode);
					break;
				case Approver:// 4已做
					wResult.Result = SCHSecondmentBPMDAO.getInstance().BPM_GetDoneTaskList(wLoginUser, wLoginUser.ID,
							wStartTime, wEndTime, wErrorCode);
					break;
				default:
					break;
				}
				break;
			case KAreaLoan:
			case TAreaLoan:
				switch (TagTypes.getEnumType(wTagTypes)) {
				case Applicant:// 2发起
					wResult.Result = SCHSecondmentApplyDAO.getInstance().BPM_GetSendTaskList(wLoginUser, wLoginUser.ID,
							wStartTime, wEndTime, wErrorCode);
					break;
				case Dispatcher:// 1待做
					wResult.Result = SCHSecondmentApplyDAO.getInstance().BPM_GetUndoTaskList(wLoginUser, wLoginUser.ID,
							wErrorCode);
					break;
				case Approver:// 4已做
					wResult.Result = SCHSecondmentApplyDAO.getInstance().BPM_GetDoneTaskList(wLoginUser, wLoginUser.ID,
							wStartTime, wEndTime, wErrorCode);
					break;
				default:
					break;
				}
				break;
			case Default:
				switch (TagTypes.getEnumType(wTagTypes)) {
				case Applicant:// 2发起
					wResult.Result = SCHSecondmentBPMDAO.getInstance().BPM_GetSendTaskList(wLoginUser, wLoginUser.ID,
							wStartTime, wEndTime, wErrorCode);
					wResult.Result.addAll(SCHSecondmentApplyDAO.getInstance().BPM_GetSendTaskList(wLoginUser,
							wLoginUser.ID, wStartTime, wEndTime, wErrorCode));
					break;
				case Dispatcher:// 1待做
					wResult.Result = SCHSecondmentBPMDAO.getInstance().BPM_GetUndoTaskList(wLoginUser, wLoginUser.ID,
							wErrorCode);
					wResult.Result.addAll(SCHSecondmentApplyDAO.getInstance().BPM_GetUndoTaskList(wLoginUser,
							wLoginUser.ID, wErrorCode));
					break;
				case Approver:// 4已做
					wResult.Result = SCHSecondmentBPMDAO.getInstance().BPM_GetDoneTaskList(wLoginUser, wLoginUser.ID,
							wStartTime, wEndTime, wErrorCode);
					wResult.Result.addAll(SCHSecondmentApplyDAO.getInstance().BPM_GetDoneTaskList(wLoginUser,
							wLoginUser.ID, wStartTime, wEndTime, wErrorCode));
					break;
				default:
					break;
				}
				break;
			default:
				break;
			}

			if (wResult.Result.size() > 0) {
				wResult.Result = wResult.Result.stream().filter(p -> p.Status != 0).collect(Collectors.toList());

				for (BPMTaskBase wBPMTaskBase : wResult.Result) {
					if (wBPMTaskBase.getClass() == SCHSecondmentBPM.class) {
						SCHSecondmentBPM wSCHSecondmentBPM = (SCHSecondmentBPM) wBPMTaskBase;

						SCHSecondmentApply wSCHSecondmentApply = new SCHSecondmentApply();

						wSCHSecondmentApply.Code = wSCHSecondmentBPM.Code;
						wSCHSecondmentApply.CreateTime = wSCHSecondmentBPM.CreateTime;
						wSCHSecondmentApply.EndTime = wSCHSecondmentBPM.EndTime;
						wSCHSecondmentApply.FlowID = wSCHSecondmentBPM.FlowID;
						wSCHSecondmentApply.FlowType = wSCHSecondmentBPM.FlowType;
						wSCHSecondmentApply.FollowerID = wSCHSecondmentBPM.FollowerID;
						wSCHSecondmentApply.FollowerName = wSCHSecondmentBPM.FollowerName;
						wSCHSecondmentApply.ID = wSCHSecondmentBPM.ID;
						wSCHSecondmentApply.NewClassID = wSCHSecondmentBPM.NewClassID;
						wSCHSecondmentApply.NewClassName = wSCHSecondmentBPM.NewClassName;
						wSCHSecondmentApply.NewPartList = wSCHSecondmentBPM.NewPartList;
						wSCHSecondmentApply.NewPartNames = wSCHSecondmentBPM.NewPartNames;
						wSCHSecondmentApply.OldPosition = wSCHSecondmentBPM.OldPosition;
						wSCHSecondmentApply.PersonID = wSCHSecondmentBPM.PersonID;
						wSCHSecondmentApply.PersonName = wSCHSecondmentBPM.PersonName;
						wSCHSecondmentApply.StartTime = wSCHSecondmentBPM.StartTime;
						wSCHSecondmentApply.Status = wSCHSecondmentBPM.Status;
						wSCHSecondmentApply.StatusText = wSCHSecondmentBPM.StatusText;
						wSCHSecondmentApply.OldClassID = wSCHSecondmentBPM.OldClassID;
						wSCHSecondmentApply.OldClassName = wSCHSecondmentBPM.OldClassName;
						wSCHSecondmentApply.OldPartNames = wSCHSecondmentBPM.OldPartNames;
						wSCHSecondmentApply.NewPosition = wSCHSecondmentBPM.NewPosition;
						wSCHSecondmentApply.StepID = wSCHSecondmentBPM.StepID;
						wSCHSecondmentApply.SubmitTime = wSCHSecondmentBPM.SubmitTime;
						wSCHSecondmentApply.Type = wSCHSecondmentBPM.Type;
						wSCHSecondmentApply.UpFlowID = wSCHSecondmentBPM.UpFlowID;
						wSCHSecondmentApply.UpFlowName = wSCHSecondmentBPM.UpFlowName;

						wList.Result.add(wSCHSecondmentApply);
					} else {
						SCHSecondmentApply wItem = (SCHSecondmentApply) wBPMTaskBase;
						wList.Result.add(wItem);
					}
				}
			}

			// 按照申请时刻降序排列
			if (wList.Result.size() > 0) {
				wList.Result = new ArrayList<SCHSecondmentApply>(wList.Result.stream()
						.collect(Collectors.toMap(SCHSecondmentApply::getID, account -> account, (k1, k2) -> k2))
						.values());

				wList.Result.sort(Comparator.comparing(SCHSecondmentApply::getCreateTime, Comparator.reverseOrder()));
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wList;
	}

	@Override
	public ServiceResult<List<SCHSecondmentApply>> SCH_QuerySecondmentApplyEmployeeAllNew(BMSEmployee wLoginUser,
			Calendar wStartTime, Calendar wEndTime, int wType, int wStatus) {
		ServiceResult<List<SCHSecondmentApply>> wList = new ServiceResult<List<SCHSecondmentApply>>();
		wList.Result = new ArrayList<SCHSecondmentApply>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

		try {
			List<BPMTaskBase> wResult = new ArrayList<BPMTaskBase>();
			List<BPMTaskBase> wSendBaseList = new ArrayList<BPMTaskBase>();
			List<BPMTaskBase> wToDoBaseList = new ArrayList<BPMTaskBase>();
			List<BPMTaskBase> wDoneBaseList = new ArrayList<BPMTaskBase>();

			switch (SCHSecondmentApplyType.getEnumType(wType)) {
			case ClassLoan:
				wSendBaseList = SCHSecondmentBPMDAO.getInstance().BPM_GetSendTaskList(wLoginUser, wLoginUser.ID,
						wStartTime, wEndTime, wErrorCode);
				wToDoBaseList = SCHSecondmentBPMDAO.getInstance().BPM_GetUndoTaskList(wLoginUser, wLoginUser.ID,
						wErrorCode);
				wDoneBaseList = SCHSecondmentBPMDAO.getInstance().BPM_GetDoneTaskList(wLoginUser, wLoginUser.ID,
						wStartTime, wEndTime, wErrorCode);
				break;
			case KAreaLoan:
				wSendBaseList = SCHSecondmentApplyDAO.getInstance().BPM_GetSendTaskList(wLoginUser, wLoginUser.ID,
						wStartTime, wEndTime, wErrorCode);
				wSendBaseList = wSendBaseList.stream().filter(p -> p.FlowType == BPMEventModule.ToLoanApplyK.getValue())
						.collect(Collectors.toList());
				wToDoBaseList = SCHSecondmentApplyDAO.getInstance().BPM_GetUndoTaskList(wLoginUser, wLoginUser.ID,
						wErrorCode);
				wToDoBaseList = wToDoBaseList.stream().filter(p -> p.FlowType == BPMEventModule.ToLoanApplyK.getValue())
						.collect(Collectors.toList());
				wDoneBaseList = SCHSecondmentApplyDAO.getInstance().BPM_GetDoneTaskList(wLoginUser, wLoginUser.ID,
						wStartTime, wEndTime, wErrorCode);
				wDoneBaseList = wDoneBaseList.stream().filter(p -> p.FlowType == BPMEventModule.ToLoanApplyK.getValue())
						.collect(Collectors.toList());
				break;
			case TAreaLoan:
				wSendBaseList = SCHSecondmentApplyDAO.getInstance().BPM_GetSendTaskList(wLoginUser, wLoginUser.ID,
						wStartTime, wEndTime, wErrorCode);
				wSendBaseList = wSendBaseList.stream().filter(p -> p.FlowType == BPMEventModule.ToLoanApply.getValue())
						.collect(Collectors.toList());
				wToDoBaseList = SCHSecondmentApplyDAO.getInstance().BPM_GetUndoTaskList(wLoginUser, wLoginUser.ID,
						wErrorCode);
				wToDoBaseList = wToDoBaseList.stream().filter(p -> p.FlowType == BPMEventModule.ToLoanApply.getValue())
						.collect(Collectors.toList());
				wDoneBaseList = SCHSecondmentApplyDAO.getInstance().BPM_GetDoneTaskList(wLoginUser, wLoginUser.ID,
						wStartTime, wEndTime, wErrorCode);
				wDoneBaseList = wDoneBaseList.stream().filter(p -> p.FlowType == BPMEventModule.ToLoanApply.getValue())
						.collect(Collectors.toList());
				break;
			case Default:
				// 班组内
				wSendBaseList.addAll(SCHSecondmentBPMDAO.getInstance().BPM_GetSendTaskList(wLoginUser, wLoginUser.ID,
						wStartTime, wEndTime, wErrorCode));
				wToDoBaseList.addAll(
						SCHSecondmentBPMDAO.getInstance().BPM_GetUndoTaskList(wLoginUser, wLoginUser.ID, wErrorCode));
				wDoneBaseList.addAll(SCHSecondmentBPMDAO.getInstance().BPM_GetDoneTaskList(wLoginUser, wLoginUser.ID,
						wStartTime, wEndTime, wErrorCode));
				// 本工区、跨工区
				wSendBaseList.addAll(SCHSecondmentApplyDAO.getInstance().BPM_GetSendTaskList(wLoginUser, wLoginUser.ID,
						wStartTime, wEndTime, wErrorCode));
				wToDoBaseList.addAll(
						SCHSecondmentApplyDAO.getInstance().BPM_GetUndoTaskList(wLoginUser, wLoginUser.ID, wErrorCode));
				wDoneBaseList.addAll(SCHSecondmentApplyDAO.getInstance().BPM_GetDoneTaskList(wLoginUser, wLoginUser.ID,
						wStartTime, wEndTime, wErrorCode));
				break;
			default:
				break;
			}

			List<Integer> wIDList1 = new ArrayList<Integer>();
			List<Integer> wIDList2 = new ArrayList<Integer>();

			for (BPMTaskBase wBPMTaskBase : wToDoBaseList) {
				if (wBPMTaskBase.FlowType == BPMEventModule.ToLoan.getValue()) {
					if (wIDList1.contains(wBPMTaskBase.ID))
						continue;
					wIDList1.add(wBPMTaskBase.ID);
				} else {
					if (wIDList2.contains(wBPMTaskBase.ID))
						continue;
					wIDList2.add(wBPMTaskBase.ID);
				}

				wBPMTaskBase.TagTypes = TaskQueryType.ToHandle.getValue();
				wResult.add(wBPMTaskBase);
			}

			for (BPMTaskBase wBPMTaskBase : wDoneBaseList) {
				if (wBPMTaskBase.FlowType == BPMEventModule.ToLoan.getValue()) {
					if (wIDList1.contains(wBPMTaskBase.ID))
						continue;
					wIDList1.add(wBPMTaskBase.ID);
				} else {
					if (wIDList2.contains(wBPMTaskBase.ID))
						continue;
					wIDList2.add(wBPMTaskBase.ID);
				}
				wBPMTaskBase.TagTypes = TaskQueryType.Handled.getValue();
				wResult.add(wBPMTaskBase);
			}
			for (BPMTaskBase wBPMTaskBase : wSendBaseList) {
				if (wBPMTaskBase.FlowType == BPMEventModule.ToLoan.getValue()) {
					if (wIDList1.contains(wBPMTaskBase.ID))
						continue;
					wIDList1.add(wBPMTaskBase.ID);
				} else {
					if (wIDList2.contains(wBPMTaskBase.ID))
						continue;
					wIDList2.add(wBPMTaskBase.ID);
				}
				wBPMTaskBase.TagTypes = TaskQueryType.Sended.getValue();
				wResult.add(wBPMTaskBase);

			}

			wResult.removeIf(p -> p.Status == 0);

			for (BPMTaskBase wBPMTaskBase : wResult) {
				if (wBPMTaskBase instanceof SCHSecondmentBPM) {
					SCHSecondmentBPM wSCHSecondmentBPM = (SCHSecondmentBPM) wBPMTaskBase;

					SCHSecondmentApply wSCHSecondmentApply = new SCHSecondmentApply();

					wSCHSecondmentApply.Code = wSCHSecondmentBPM.Code;
					wSCHSecondmentApply.CreateTime = wSCHSecondmentBPM.CreateTime;
					wSCHSecondmentApply.EndTime = wSCHSecondmentBPM.EndTime;
					wSCHSecondmentApply.FlowID = wSCHSecondmentBPM.FlowID;
					wSCHSecondmentApply.FlowType = wSCHSecondmentBPM.FlowType;
					wSCHSecondmentApply.FollowerID = wSCHSecondmentBPM.FollowerID;
					wSCHSecondmentApply.FollowerName = wSCHSecondmentBPM.FollowerName;
					wSCHSecondmentApply.ID = wSCHSecondmentBPM.ID;
					wSCHSecondmentApply.NewClassID = wSCHSecondmentBPM.NewClassID;
					wSCHSecondmentApply.NewClassName = wSCHSecondmentBPM.NewClassName;
					wSCHSecondmentApply.NewPartList = wSCHSecondmentBPM.NewPartList;
					wSCHSecondmentApply.NewPartNames = wSCHSecondmentBPM.NewPartNames;
					wSCHSecondmentApply.OldPosition = wSCHSecondmentBPM.OldPosition;
					wSCHSecondmentApply.PersonID = wSCHSecondmentBPM.PersonID;
					wSCHSecondmentApply.PersonName = wSCHSecondmentBPM.PersonName;
					wSCHSecondmentApply.StartTime = wSCHSecondmentBPM.StartTime;
					wSCHSecondmentApply.Status = wSCHSecondmentBPM.Status;
					wSCHSecondmentApply.StatusText = wSCHSecondmentBPM.StatusText;
					wSCHSecondmentApply.OldClassID = wSCHSecondmentBPM.OldClassID;
					wSCHSecondmentApply.OldClassName = wSCHSecondmentBPM.OldClassName;
					wSCHSecondmentApply.OldPartNames = wSCHSecondmentBPM.OldPartNames;
					wSCHSecondmentApply.NewPosition = wSCHSecondmentBPM.NewPosition;
					wSCHSecondmentApply.StepID = wSCHSecondmentBPM.StepID;
					wSCHSecondmentApply.SubmitTime = wSCHSecondmentBPM.SubmitTime;
					wSCHSecondmentApply.Type = wSCHSecondmentBPM.Type;
					wSCHSecondmentApply.UpFlowID = wSCHSecondmentBPM.UpFlowID;
					wSCHSecondmentApply.UpFlowName = wSCHSecondmentBPM.UpFlowName;
					wSCHSecondmentApply.TagTypes = wSCHSecondmentBPM.TagTypes;

					wList.Result.add(wSCHSecondmentApply);
				} else if (wBPMTaskBase instanceof SCHSecondmentApply) {
					SCHSecondmentApply wItem = (SCHSecondmentApply) wBPMTaskBase;
					wList.Result.add(wItem);
				}
			}

			// 状态
			if (wStatus >= 0) {
				if (wStatus == 0) {
					wList.Result = wList.Result.stream().filter(p -> p.Status != 20 && p.Status != 21 && p.Status != 22)
							.collect(Collectors.toList());
				} else if (wStatus == 1) {
					wList.Result = wList.Result.stream().filter(p -> p.Status == 20 || p.Status == 21 || p.Status == 22)
							.collect(Collectors.toList());
				}
			}

			wList.Result.sort((o1, o2) -> o2.CreateTime.compareTo(o1.CreateTime));

			wList.Result.sort((o1, o2) -> {
				if (o1.TagTypes == 1) {
					return -1;
				} else if (o2.TagTypes == 1) {
					return 1;
				}
				return 0;
			});

			wList.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wList.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wList;
	}

	@Override
	public ServiceResult<List<SCHSecondmentApply>> SCH_QuerySecondmentApplyList(BMSEmployee wLoginUser, int wType,
			int wStatus, Calendar wStartTime, Calendar wEndTime) {
		ServiceResult<List<SCHSecondmentApply>> wResult = new ServiceResult<List<SCHSecondmentApply>>();
		wResult.Result = new ArrayList<SCHSecondmentApply>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			switch (wStatus) {
			case 1:

				switch (SCHSecondmentApplyType.getEnumType(wType)) {
				case ClassLoan:
					wResult.Result.addAll(SCHSecondmentApply
							.BPMListToApplyList(SCHSecondmentBPMDAO.getInstance().SelectList(wLoginUser, wType, -1,
									StringUtils.parseListArgs(SCHSecondmentBPMStatus.NomalClose.getValue()), null,
									wStartTime, wEndTime, wErrorCode)));
					break;
				case KAreaLoan:
				case TAreaLoan:

					wResult.Result.addAll(SCHSecondmentApplyDAO.getInstance().SelectList(wLoginUser, wType, -1,
							StringUtils.parseListArgs(SCHSecondmentApplyStatus.NomalClose.getValue()), null, wStartTime,
							wEndTime, wErrorCode));
					break;
				case Default:
					wResult.Result.addAll(SCHSecondmentApply
							.BPMListToApplyList(SCHSecondmentBPMDAO.getInstance().SelectList(wLoginUser, wType, -1,
									StringUtils.parseListArgs(SCHSecondmentBPMStatus.NomalClose.getValue()), null,
									wStartTime, wEndTime, wErrorCode)));
					wResult.Result.addAll(SCHSecondmentApplyDAO.getInstance().SelectList(wLoginUser, wType, -1,
							StringUtils.parseListArgs(SCHSecondmentApplyStatus.NomalClose.getValue()), null, wStartTime,
							wEndTime, wErrorCode));
					break;
				default:
					break;
				}

				break;
			case 0:
				switch (SCHSecondmentApplyType.getEnumType(wType)) {
				case ClassLoan:
					wResult.Result.addAll(SCHSecondmentApply.BPMListToApplyList(
							SCHSecondmentBPMDAO.getInstance().SelectList(wLoginUser, wType, -1, null,
									StringUtils.parseListArgs(SCHSecondmentBPMStatus.NomalClose.getValue(),
											SCHSecondmentBPMStatus.ExceptionClose.getValue(),
											SCHSecondmentBPMStatus.Default.getValue()),
									wStartTime, wEndTime, wErrorCode)));
					break;
				case KAreaLoan:
				case TAreaLoan:

					wResult.Result.addAll(SCHSecondmentApplyDAO.getInstance().SelectList(wLoginUser, wType, -1, null,
							StringUtils.parseListArgs(SCHSecondmentApplyStatus.NomalClose.getValue(),
									SCHSecondmentApplyStatus.Canceled.getValue(),
									SCHSecondmentApplyStatus.ExceptionClose.getValue(),
									SCHSecondmentApplyStatus.Default.getValue()),
							wStartTime, wEndTime, wErrorCode));
					break;
				case Default:
					wResult.Result.addAll(SCHSecondmentApply.BPMListToApplyList(
							SCHSecondmentBPMDAO.getInstance().SelectList(wLoginUser, wType, -1, null,
									StringUtils.parseListArgs(SCHSecondmentBPMStatus.NomalClose.getValue(),
											SCHSecondmentBPMStatus.ExceptionClose.getValue(),
											SCHSecondmentBPMStatus.Default.getValue()),
									wStartTime, wEndTime, wErrorCode)));
					wResult.Result.addAll(SCHSecondmentApplyDAO.getInstance().SelectList(wLoginUser, wType, -1, null,
							StringUtils.parseListArgs(SCHSecondmentApplyStatus.NomalClose.getValue(),
									SCHSecondmentApplyStatus.Canceled.getValue(),
									SCHSecondmentApplyStatus.ExceptionClose.getValue(),
									SCHSecondmentApplyStatus.Default.getValue()),
							wStartTime, wEndTime, wErrorCode));
					break;
				default:
					break;
				}

				break;
			default:
				switch (SCHSecondmentApplyType.getEnumType(wType)) {
				case ClassLoan:
					wResult.Result.addAll(SCHSecondmentApply.BPMListToApplyList(SCHSecondmentBPMDAO.getInstance()
							.SelectList(wLoginUser, wType, -1, null, null, wStartTime, wEndTime, wErrorCode)));
					break;
				case KAreaLoan:
				case TAreaLoan:

					wResult.Result.addAll(SCHSecondmentApplyDAO.getInstance().SelectList(wLoginUser, wType, -1, null,
							null, wStartTime, wEndTime, wErrorCode));
					break;
				case Default:
					wResult.Result.addAll(SCHSecondmentApply.BPMListToApplyList(SCHSecondmentBPMDAO.getInstance()
							.SelectList(wLoginUser, wType, -1, null, null, wStartTime, wEndTime, wErrorCode)));
					wResult.Result.addAll(SCHSecondmentApplyDAO.getInstance().SelectList(wLoginUser, wType, -1, null,
							null, wStartTime, wEndTime, wErrorCode));
					break;
				default:
					break;
				}
				break;
			}
			wResult.Result.sort((o1, o2) -> o2.SubmitTime.compareTo(o1.SubmitTime));

			if (wResult.Result.size() <= 0 || wStatus == 1) {
				return wResult;
			}
			List<BPMTaskBase> wBaseList = new ArrayList<BPMTaskBase>();
			switch (SCHSecondmentApplyType.getEnumType(wType)) {
			case ClassLoan:
				wBaseList.addAll(
						SCHSecondmentBPMDAO.getInstance().BPM_GetUndoTaskList(wLoginUser, wLoginUser.ID, wErrorCode));
				break;
			case KAreaLoan:
				wBaseList.addAll(
						SCHSecondmentApplyDAO.getInstance().BPM_GetUndoTaskList(wLoginUser, wLoginUser.ID, wErrorCode));
				wBaseList.removeIf(p -> p.FlowType != BPMEventModule.ToLoanApplyK.getValue());
				break;
			case TAreaLoan:

				wBaseList.addAll(
						SCHSecondmentApplyDAO.getInstance().BPM_GetUndoTaskList(wLoginUser, wLoginUser.ID, wErrorCode));
				wBaseList.removeIf(p -> p.FlowType != BPMEventModule.ToLoanApply.getValue());
				break;
			case Default:
				wBaseList.addAll(
						SCHSecondmentBPMDAO.getInstance().BPM_GetUndoTaskList(wLoginUser, wLoginUser.ID, wErrorCode));
				wBaseList.addAll(
						SCHSecondmentApplyDAO.getInstance().BPM_GetUndoTaskList(wLoginUser, wLoginUser.ID, wErrorCode));
				break;
			default:
				break;
			}

			if (wBaseList == null || wBaseList.size() <= 0) {
				return wResult;
			}
			for (BPMTaskBase wTaskBase : wBaseList) {

				if (wTaskBase instanceof SCHSecondmentApply) {
					SCHSecondmentApply wSFCBOMTask = (SCHSecondmentApply) wTaskBase;
					wSFCBOMTask.TagTypes = TaskQueryType.ToHandle.getValue();
					for (int i = 0; i < wResult.Result.size(); i++) {
						if (wResult.Result.get(i).ID == wSFCBOMTask.ID)
							wResult.Result.set(i, wSFCBOMTask);
					}
				}
				if (wTaskBase instanceof SCHSecondmentBPM) {
					SCHSecondmentApply wSFCBOMTask = new SCHSecondmentApply((SCHSecondmentBPM) wTaskBase);
					wSFCBOMTask.TagTypes = TaskQueryType.ToHandle.getValue();
					for (int i = 0; i < wResult.Result.size(); i++) {
						if (wResult.Result.get(i).ID == wSFCBOMTask.ID)
							wResult.Result.set(i, wSFCBOMTask);
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
}

/*
 * Location: C:\Users\Shris\Desktop\新建文件夹
 * (5)\MESLOCOAPS.zip!\WEB-INF\classes\com\mes\loco\aps\server\serviceimpl\
 * SCHServiceImpl.class Java compiler version: 8 (52.0) JD-Core Version: 1.1.2
 */