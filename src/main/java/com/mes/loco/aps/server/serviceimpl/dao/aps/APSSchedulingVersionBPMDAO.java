package com.mes.loco.aps.server.serviceimpl.dao.aps;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.mes.loco.aps.server.service.mesenum.APSShiftPeriod;
import com.mes.loco.aps.server.service.mesenum.BFCMessageType;
import com.mes.loco.aps.server.service.mesenum.BPMEventModule;
import com.mes.loco.aps.server.service.mesenum.MESDBSource;
import com.mes.loco.aps.server.service.mesenum.MESException;
import com.mes.loco.aps.server.service.po.OutResult;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.aps.APSSchedulingVersionBPM;
import com.mes.loco.aps.server.service.po.bfc.BFCMessage;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.bpm.BPMTaskBase;
import com.mes.loco.aps.server.service.utils.CloneTool;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.CoreServiceImpl;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;
import com.mes.loco.aps.server.serviceimpl.dao.TaskBaseDAO;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSConstans;

public class APSSchedulingVersionBPMDAO extends BaseDAO implements TaskBaseDAO {

	private static Logger logger = LoggerFactory.getLogger(APSSchedulingVersionBPMDAO.class);

	private static APSSchedulingVersionBPMDAO Instance = null;

	private APSSchedulingVersionBPMDAO() {
		super();
	}

	public static APSSchedulingVersionBPMDAO getInstance() {
		if (Instance == null)
			Instance = new APSSchedulingVersionBPMDAO();
		return Instance;
	}

	/**
	 * 添加或修改
	 * 
	 * @param wAPSSchedulingVersionBPM
	 * @return
	 */
	public BPMTaskBase Update(BMSEmployee wLoginUser, APSSchedulingVersionBPM wAPSSchedulingVersionBPM,
			OutResult<Integer> wErrorCode) {
		BPMTaskBase wResult = new BPMTaskBase();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.APS, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wAPSSchedulingVersionBPM == null)
				return wResult;

			if (wAPSSchedulingVersionBPM.FollowerID == null) {
				wAPSSchedulingVersionBPM.FollowerID = new ArrayList<Integer>();
			}

			String wSQL = "";
			if (wAPSSchedulingVersionBPM.getID() <= 0) {
				wSQL = StringUtils.Format(
						"INSERT INTO {0}.aps_schedulingversionbpm(Code,FlowType,FlowID,UpFlowID,FollowerID,"
								+ "Status,StatusText,CreateTime,SubmitTime,VersionNo,APSShiftPeriod,"
								+ "TaskPartIDList,StartTime,EndTime,AuditID,AuditTime) VALUES(:Code,:FlowType,"
								+ ":FlowID,:UpFlowID,:FollowerID,:Status,:StatusText,:CreateTime,:SubmitTime,"
								+ ":VersionNo,:APSShiftPeriod,:TaskPartIDList,:StartTime,:EndTime,:AuditID,:AuditTime);",
						wInstance.Result);
			} else {
				wSQL = StringUtils.Format("UPDATE {0}.aps_schedulingversionbpm SET Code = :Code,FlowType = :FlowType,"
						+ "FlowID = :FlowID,UpFlowID = :UpFlowID,FollowerID = :FollowerID,"
						+ "Status = :Status,StatusText = :StatusText,CreateTime = :CreateTime,"
						+ "SubmitTime = now(),VersionNo = :VersionNo,APSShiftPeriod = :APSShiftPeriod,"
						+ "TaskPartIDList = :TaskPartIDList,StartTime = :StartTime,EndTime = :EndTime,"
						+ "AuditID = :AuditID,AuditTime = :AuditTime WHERE ID = :ID;", wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wAPSSchedulingVersionBPM.ID);
			wParamMap.put("Code", wAPSSchedulingVersionBPM.Code);
			wParamMap.put("FlowType", wAPSSchedulingVersionBPM.FlowType);
			wParamMap.put("FlowID", wAPSSchedulingVersionBPM.FlowID);
			wParamMap.put("UpFlowID", wAPSSchedulingVersionBPM.UpFlowID);
			wParamMap.put("FollowerID", StringUtils.Join(",", wAPSSchedulingVersionBPM.FollowerID));
			wParamMap.put("Status", wAPSSchedulingVersionBPM.Status);
			wParamMap.put("StatusText", wAPSSchedulingVersionBPM.StatusText);
			wParamMap.put("CreateTime", wAPSSchedulingVersionBPM.CreateTime);
			wParamMap.put("SubmitTime", wAPSSchedulingVersionBPM.SubmitTime);
			wParamMap.put("VersionNo", wAPSSchedulingVersionBPM.VersionNo);
			wParamMap.put("APSShiftPeriod", wAPSSchedulingVersionBPM.APSShiftPeriod);
			wParamMap.put("TaskPartIDList", StringUtils.Join(",", wAPSSchedulingVersionBPM.TaskPartIDList));
			wParamMap.put("StartTime", wAPSSchedulingVersionBPM.StartTime);
			wParamMap.put("EndTime", wAPSSchedulingVersionBPM.EndTime);
			wParamMap.put("AuditID", wAPSSchedulingVersionBPM.AuditID);
			wParamMap.put("AuditTime", wAPSSchedulingVersionBPM.AuditTime);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wAPSSchedulingVersionBPM.getID() <= 0) {
				wAPSSchedulingVersionBPM.setID(keyHolder.getKey().intValue());
			}

			wResult = wAPSSchedulingVersionBPM;
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 条件查询集合
	 * 
	 * @return
	 */
	public List<APSSchedulingVersionBPM> SelectList(BMSEmployee wLoginUser, int wID, String wCode, int wFlowType,
			int wUpFlowID, String wFollowerID, String wVersionNo, int wAPSShiftPeriod, int wAuditID,
			List<Integer> wStateIDList, Calendar wStartTime, Calendar wEndTime, OutResult<Integer> wErrorCode) {
		List<APSSchedulingVersionBPM> wResultList = new ArrayList<APSSchedulingVersionBPM>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.APS, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			if (wFollowerID == null) {
				wFollowerID = "";
			}

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1);
			if (wStartTime == null) {
				wStartTime = wBaseTime;
			}
			if (wEndTime == null) {
				wEndTime = wBaseTime;
			}
			if (wStartTime.compareTo(wEndTime) > 0) {
				return wResultList;
			}

			if (wStateIDList == null) {
				wStateIDList = new ArrayList<Integer>();
			}

			String wSQL = StringUtils.Format("SELECT * FROM {0}.aps_schedulingversionbpm WHERE  1=1  "
					+ "and ( :wID <= 0 or :wID = ID ) " + "and ( :wCode is null or :wCode = '''' or :wCode = Code ) "
					+ "and ( :wFlowType <= 0 or :wFlowType = FlowType ) "
					+ "and ( :wUpFlowID <= 0 or :wUpFlowID = UpFlowID ) "
					+ "and ( :wFollowerID = '''' or  find_in_set( :wFollowerID,replace(FollowerID,'';'','','') ) ) "
					+ "and ( :wVersionNo is null or :wVersionNo = '''' or :wVersionNo = VersionNo ) "
					+ "and ( :wAPSShiftPeriod <= 0 or :wAPSShiftPeriod = APSShiftPeriod ) "

					+ "and ( :wStartTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wStartTime <= CreateTime) "
					+ "and ( :wEndTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wEndTime >= SubmitTime) "

					+ "and ( :wAuditID <= 0 or :wAuditID = AuditID ) "
					+ "and ( :wStatus is null or :wStatus = '''' or Status in ({1}));", wInstance.Result,
					wStateIDList.size() > 0 ? StringUtils.Join(",", wStateIDList) : "0");

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wCode", wCode);
			wParamMap.put("wFlowType", wFlowType);
			wParamMap.put("wUpFlowID", wUpFlowID);
			wParamMap.put("wFollowerID", wFollowerID);
			wParamMap.put("wVersionNo", wVersionNo);
			wParamMap.put("wAPSShiftPeriod", wAPSShiftPeriod);
			wParamMap.put("wAuditID", wAuditID);
			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);
			wParamMap.put("wStatus", StringUtils.Join(",", wStateIDList));

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetValue(wResultList, wQueryResult);
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	/**
	 * 赋值
	 */
	private void SetValue(List<APSSchedulingVersionBPM> wResultList, List<Map<String, Object>> wQueryResult) {
		for (Map<String, Object> wReader : wQueryResult) {
			try {
				APSSchedulingVersionBPM wItem = new APSSchedulingVersionBPM();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.Code = StringUtils.parseString(wReader.get("Code"));
				wItem.FlowType = StringUtils.parseInt(wReader.get("FlowType"));
				wItem.FlowID = StringUtils.parseInt(wReader.get("FlowID"));
				wItem.UpFlowID = StringUtils.parseInt(wReader.get("UpFlowID"));
				wItem.FollowerID = StringUtils
						.parseIntList(StringUtils.parseString(wReader.get("FollowerID")).split(",|;"));
				wItem.Status = StringUtils.parseInt(wReader.get("Status"));
				wItem.StatusText = StringUtils.parseString(wReader.get("StatusText"));
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				wItem.SubmitTime = StringUtils.parseCalendar(wReader.get("SubmitTime"));
				wItem.VersionNo = StringUtils.parseString(wReader.get("VersionNo"));
				wItem.APSShiftPeriod = StringUtils.parseInt(wReader.get("APSShiftPeriod"));
				wItem.TaskPartIDList = StringUtils
						.parseIntList(StringUtils.parseString(wReader.get("TaskPartIDList")).split(","));
				wItem.StartTime = StringUtils.parseCalendar(wReader.get("StartTime"));
				wItem.EndTime = StringUtils.parseCalendar(wReader.get("EndTime"));
				wItem.AuditID = StringUtils.parseInt(wReader.get("AuditID"));
				wItem.AuditTime = StringUtils.parseCalendar(wReader.get("AuditTime"));

				wItem.Auditor = APSConstans.GetBMSEmployeeName(wItem.AuditID);
				wItem.UpFlowName = APSConstans.GetBMSEmployeeName(wItem.UpFlowID);
				wItem.FollowerName = this.GetNames(wItem.FollowerID);

				wResultList.add(wItem);
			} catch (Exception ex) {
				logger.error(ex.toString());
			}
		}
	}

	public List<APSSchedulingVersionBPM> SelectList(BMSEmployee wLoginUser, List<Integer> wTaskIDList,
			OutResult<Integer> wErrorCode) {
		List<APSSchedulingVersionBPM> wResultList = new ArrayList<APSSchedulingVersionBPM>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.APS, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			if (wTaskIDList == null || wTaskIDList.size() <= 0) {
				return wResultList;
			}

			String wSQL = StringUtils.Format(
					"SELECT * FROM {0}.aps_schedulingversionbpm WHERE  1=1  "
							+ "and ( :wIDs is null or :wIDs = '''' or ID in ({1}));",
					wInstance.Result, wTaskIDList.size() > 0 ? StringUtils.Join(",", wTaskIDList) : "0");

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wIDs", StringUtils.Join(",", wTaskIDList));

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetValue(wResultList, wQueryResult);
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	/**
	 * 获取最新的编码
	 */
	public String GetNewCode(BMSEmployee wLoginUser, OutResult<Integer> wErrorCode, APSShiftPeriod wAPSShiftPeriod) {
		String wResult = "";
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.APS, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			// 本月时间
			int wYear = Calendar.getInstance().get(Calendar.YEAR);
			int wMonth = Calendar.getInstance().get(Calendar.MONTH);
			Calendar wSTime = Calendar.getInstance();
			wSTime.set(wYear, wMonth, 1, 0, 0, 0);
			Calendar wETime = Calendar.getInstance();
			wETime.set(wYear, wMonth + 1, 1, 23, 59, 59);
			wETime.add(Calendar.DATE, -1);

			String wSQL = StringUtils.Format(
					"select count(*)+1 as Number from {0}.aps_schedulingversionbpm where CreateTime > :wSTime and CreateTime < :wETime;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();
			wParamMap.put("wSTime", wSTime);
			wParamMap.put("wETime", wETime);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			int wNumber = 0;
			for (Map<String, Object> wReader : wQueryResult) {
				if (wReader.containsKey("Number")) {
					wNumber = StringUtils.parseInt(wReader.get("Number"));
					break;
				}
			}

			if (wAPSShiftPeriod == APSShiftPeriod.Week) {
				wResult = StringUtils.Format("WP{0}{1}{2}", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)),
						String.format("%02d", Calendar.getInstance().get(Calendar.MONTH) + 1),
						String.format("%04d", wNumber));
			} else if (wAPSShiftPeriod == APSShiftPeriod.Month) {
				wResult = StringUtils.Format("MP{0}{1}{2}", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)),
						String.format("%02d", Calendar.getInstance().get(Calendar.MONTH) + 1),
						String.format("%04d", wNumber));
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取处理人名称(多人)
	 */
	private String GetNames(List<Integer> wIDList) {
		String wResult = "";
		try {
			if (wIDList == null || wIDList.size() <= 0) {
				return wResult;
			}

			List<String> wNames = new ArrayList<String>();
			wIDList.forEach(p -> {
				if (StringUtils.isNotEmpty(APSConstans.GetBMSEmployeeName(p))) {
					wNames.add(APSConstans.GetBMSEmployeeName(p));
				}
			});

			if (wNames.size() > 0) {
				wResult = StringUtils.Join(",", wNames);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public List<BPMTaskBase> BPM_GetUndoTaskList(BMSEmployee wLoginUser, int wResponsorID,
			OutResult<Integer> wErrorCode) {
		List<APSSchedulingVersionBPM> wResult = new ArrayList<APSSchedulingVersionBPM>();
		try {
			// 获取所有任务消息
			List<BFCMessage> wMessageList = CoreServiceImpl.getInstance()
					.BFC_GetMessageList(wLoginUser, wLoginUser.getID(), BPMEventModule.SCMonthAudit.getValue(), -1,
							BFCMessageType.Task.getValue(), 0, -1, null, null)
					.List(BFCMessage.class);
			wMessageList.addAll(CoreServiceImpl.getInstance().BFC_GetMessageList(wLoginUser, wLoginUser.getID(),
					BPMEventModule.SCMonthAudit.getValue(), -1, BFCMessageType.Task.getValue(), 1, -1, null, null)
					.List(BFCMessage.class));
			wMessageList.addAll(CoreServiceImpl.getInstance().BFC_GetMessageList(wLoginUser, wLoginUser.getID(),
					BPMEventModule.SCMonthAudit.getValue(), -1, BFCMessageType.Task.getValue(), 2, -1, null, null)
					.List(BFCMessage.class));

			wMessageList.addAll(CoreServiceImpl.getInstance().BFC_GetMessageList(wLoginUser, wLoginUser.getID(),
					BPMEventModule.SCWeekAudit.getValue(), -1, BFCMessageType.Task.getValue(), 0, -1, null, null)
					.List(BFCMessage.class));
			wMessageList.addAll(CoreServiceImpl.getInstance().BFC_GetMessageList(wLoginUser, wLoginUser.getID(),
					BPMEventModule.SCWeekAudit.getValue(), -1, BFCMessageType.Task.getValue(), 1, -1, null, null)
					.List(BFCMessage.class));
			wMessageList.addAll(CoreServiceImpl.getInstance().BFC_GetMessageList(wLoginUser, wLoginUser.getID(),
					BPMEventModule.SCWeekAudit.getValue(), -1, BFCMessageType.Task.getValue(), 2, -1, null, null)
					.List(BFCMessage.class));

			List<Integer> wTaskIDList = wMessageList.stream().map(p -> (int) p.MessageID).distinct()
					.collect(Collectors.toList());

			// 所有未完成的任务
			Map<Integer, APSSchedulingVersionBPM> wTaskMap = new HashMap<Integer, APSSchedulingVersionBPM>();
			if (wTaskIDList != null && wTaskIDList.size() > 0) {
				List<APSSchedulingVersionBPM> wMTCTaskListTemp = this.SelectList(wLoginUser, wTaskIDList, wErrorCode);

				wTaskMap = wMTCTaskListTemp.stream().collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));

			}
			APSSchedulingVersionBPM wTaskTemp = null;
			for (BFCMessage wBFCMessage : wMessageList) {
				if (!wTaskMap.containsKey((int) wBFCMessage.getMessageID()))
					continue;

				wTaskTemp = CloneTool.Clone(wTaskMap.get((int) wBFCMessage.getMessageID()),
						APSSchedulingVersionBPM.class);
				wTaskTemp.StepID = wBFCMessage.getStepID();
				wResult.add(wTaskTemp);
			}

			wResult.sort(Comparator.comparing(APSSchedulingVersionBPM::getSubmitTime).reversed());
			// 剔除任务状态为0的任务（废弃任务）
			if (wResult != null && wResult.size() > 0) {
				wResult = wResult.stream().filter(p -> p.Status != 0).collect(Collectors.toList());
			}
		} catch (Exception e) {
			wErrorCode.set(MESException.Exception.getValue());
			logger.error(e.toString());
		}
		return new ArrayList<BPMTaskBase>(wResult);
	}

	@Override
	public List<BPMTaskBase> BPM_GetDoneTaskList(BMSEmployee wLoginUser, int wResponsorID, Calendar wStartTime,
			Calendar wEndTime, OutResult<Integer> wErrorCode) {
		List<APSSchedulingVersionBPM> wResult = new ArrayList<APSSchedulingVersionBPM>();
		wErrorCode.set(0);
		try {
			List<APSSchedulingVersionBPM> wTaskList = new ArrayList<APSSchedulingVersionBPM>();
			// 获取所有任务消息
			List<BFCMessage> wMessageList = CoreServiceImpl.getInstance()
					.BFC_GetMessageList(wLoginUser, wLoginUser.getID(), BPMEventModule.SCWeekAudit.getValue(), -1,
							BFCMessageType.Task.getValue(), 3, -1, wStartTime, wEndTime)
					.List(BFCMessage.class);
			wMessageList
					.addAll(CoreServiceImpl.getInstance()
							.BFC_GetMessageList(wLoginUser, wLoginUser.getID(), BPMEventModule.SCWeekAudit.getValue(),
									-1, BFCMessageType.Task.getValue(), 4, -1, wStartTime, wEndTime)
							.List(BFCMessage.class));

			wMessageList
					.addAll(CoreServiceImpl.getInstance()
							.BFC_GetMessageList(wLoginUser, wLoginUser.getID(), BPMEventModule.SCMonthAudit.getValue(),
									-1, BFCMessageType.Task.getValue(), 3, -1, wStartTime, wEndTime)
							.List(BFCMessage.class));

			wMessageList
					.addAll(CoreServiceImpl.getInstance()
							.BFC_GetMessageList(wLoginUser, wLoginUser.getID(), BPMEventModule.SCMonthAudit.getValue(),
									-1, BFCMessageType.Task.getValue(), 4, -1, wStartTime, wEndTime)
							.List(BFCMessage.class));

			List<Integer> wTaskIDList = wMessageList.stream().map(p -> (int) p.MessageID).distinct()
					.collect(Collectors.toList());

			wTaskList = this.SelectList(wLoginUser, wTaskIDList, wErrorCode);

			wTaskList.sort(Comparator.comparing(APSSchedulingVersionBPM::getSubmitTime).reversed());

			wResult = wTaskList;
			// 剔除任务状态为0的任务（废弃任务）
			if (wResult != null && wResult.size() > 0) {
				wResult = wResult.stream().filter(p -> p.Status != 0).collect(Collectors.toList());
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return new ArrayList<BPMTaskBase>(wResult);
	}

	@Override
	public List<BPMTaskBase> BPM_GetSendTaskList(BMSEmployee wLoginUser, int wResponsorID, Calendar wStartTime,
			Calendar wEndTime, OutResult<Integer> wErrorCode) {
		List<APSSchedulingVersionBPM> wResult = new ArrayList<>();
		try {
			wResult = this.SelectList(wLoginUser, -1, "", -1, wResponsorID, "", "", -1, -1, null, null, null,
					wErrorCode);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return new ArrayList<BPMTaskBase>(wResult);
	}

	@Override
	public BPMTaskBase BPM_UpdateTask(BMSEmployee wLoginUser, BPMTaskBase wTask, OutResult<Integer> wErrorCode) {
		BPMTaskBase wResult = new BPMTaskBase();
		try {
			wResult = this.Update(wLoginUser, (APSSchedulingVersionBPM) wTask, wErrorCode);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public BPMTaskBase BPM_GetTaskInfo(BMSEmployee wLoginUser, int wTaskID, String wCode,
			OutResult<Integer> wErrorCode) {
		BPMTaskBase wResult = new BPMTaskBase();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.APS, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<APSSchedulingVersionBPM> wList = this.SelectList(wLoginUser, wTaskID, wCode, -1, -1, "", "", -1, -1,
					null, null, null, wErrorCode);
			if (wList == null || wList.size() != 1)
				return wResult;
			wResult = wList.get(0);
		} catch (Exception e) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(e.toString());
		}
		return wResult;
	}
}
