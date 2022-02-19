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

import com.mes.loco.aps.server.service.mesenum.BFCMessageType;
import com.mes.loco.aps.server.service.mesenum.BPMEventModule;
import com.mes.loco.aps.server.service.mesenum.MESDBSource;
import com.mes.loco.aps.server.service.mesenum.MESException;
import com.mes.loco.aps.server.service.po.OutResult;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.aps.APSDayPlanAuditBPM;
import com.mes.loco.aps.server.service.po.aps.APSDayPlanAuditItemBPM;
import com.mes.loco.aps.server.service.po.bfc.BFCMessage;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.bpm.BPMTaskBase;
import com.mes.loco.aps.server.service.utils.CloneTool;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.CoreServiceImpl;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;
import com.mes.loco.aps.server.serviceimpl.dao.TaskBaseDAO;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSConstans;

public class APSDayPlanAuditBPMDAO extends BaseDAO implements TaskBaseDAO {

	private static Logger logger = LoggerFactory.getLogger(APSDayPlanAuditBPMDAO.class);

	private static APSDayPlanAuditBPMDAO Instance = null;

	private APSDayPlanAuditBPMDAO() {
		super();
	}

	public static APSDayPlanAuditBPMDAO getInstance() {
		if (Instance == null)
			Instance = new APSDayPlanAuditBPMDAO();
		return Instance;
	}

	/**
	 * 添加或修改
	 * 
	 * @param wAPSDayPlanAuditBPM
	 * @return
	 */
	public APSDayPlanAuditBPM Update(BMSEmployee wLoginUser, APSDayPlanAuditBPM wAPSDayPlanAuditBPM,
			OutResult<Integer> wErrorCode) {
		APSDayPlanAuditBPM wResult = new APSDayPlanAuditBPM();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.APS, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wAPSDayPlanAuditBPM == null)
				return wResult;

			String wSQL = "";
			if (wAPSDayPlanAuditBPM.getID() <= 0) {
				wSQL = StringUtils
						.Format("INSERT INTO {0}.aps_dayplanauditbpm(Code,FlowType,FlowID,UpFlowID,FollowerID,Status,"
								+ "StatusText,CreateTime,SubmitTime,AreaID,ShiftID,ShiftDate,"
								+ "AuditID,AuditTime,AuditItemBPMList) VALUES(:Code,:FlowType,:FlowID,:UpFlowID,"
								+ ":FollowerID,:Status,:StatusText,:CreateTime,:SubmitTime,:AreaID,:ShiftID,"
								+ ":ShiftDate,:AuditID,:AuditTime,:AuditItemBPMList);", wInstance.Result);
			} else {
				wSQL = StringUtils.Format(
						"UPDATE {0}.aps_dayplanauditbpm SET Code = :Code,FlowType = :FlowType,"
								+ "FlowID = :FlowID,UpFlowID = :UpFlowID,FollowerID = :FollowerID,Status = :Status,"
								+ "StatusText = :StatusText,CreateTime = :CreateTime,SubmitTime = :SubmitTime,"
								+ "AreaID = :AreaID,ShiftID = :ShiftID,ShiftDate = :ShiftDate,AuditID = :AuditID,"
								+ "AuditTime = :AuditTime,AuditItemBPMList = :AuditItemBPMList WHERE ID = :ID;",
						wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wAPSDayPlanAuditBPM.ID);
			wParamMap.put("Code", wAPSDayPlanAuditBPM.Code);
			wParamMap.put("FlowType", wAPSDayPlanAuditBPM.FlowType);
			wParamMap.put("FlowID", wAPSDayPlanAuditBPM.FlowID);
			wParamMap.put("UpFlowID", wAPSDayPlanAuditBPM.UpFlowID);
			wParamMap.put("FollowerID", StringUtils.Join(",", wAPSDayPlanAuditBPM.FollowerID));
			wParamMap.put("Status", wAPSDayPlanAuditBPM.Status);
			wParamMap.put("StatusText", wAPSDayPlanAuditBPM.StatusText);
			wParamMap.put("CreateTime", wAPSDayPlanAuditBPM.CreateTime);
			wParamMap.put("SubmitTime", wAPSDayPlanAuditBPM.SubmitTime);
			wParamMap.put("AreaID", wAPSDayPlanAuditBPM.AreaID);
			wParamMap.put("ShiftID", wAPSDayPlanAuditBPM.ShiftID);
			wParamMap.put("ShiftDate", wAPSDayPlanAuditBPM.ShiftDate);
			wParamMap.put("AuditID", wAPSDayPlanAuditBPM.AuditID);
			wParamMap.put("AuditTime", wAPSDayPlanAuditBPM.AuditTime);
			wParamMap.put("AuditItemBPMList",
					APSDayPlanAuditItemBPM.ListToString(wAPSDayPlanAuditBPM.AuditItemBPMList));

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wAPSDayPlanAuditBPM.getID() <= 0) {
				wAPSDayPlanAuditBPM.setID(keyHolder.getKey().intValue());
			}
			wResult = wAPSDayPlanAuditBPM;
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 条件查询集合
	 */
	public List<APSDayPlanAuditBPM> SelectList(BMSEmployee wLoginUser, int wID, String wCode, int wUpFlowID,
			String wFollowerID, int wAreaID, int wShiftID, List<Integer> wStateIDList, Calendar wStartTime,
			Calendar wEndTime, OutResult<Integer> wErrorCode) {
		List<APSDayPlanAuditBPM> wResultList = new ArrayList<APSDayPlanAuditBPM>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.APS, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			if (wStateIDList == null) {
				wStateIDList = new ArrayList<Integer>();
			}

			if (wFollowerID == null) {
				wFollowerID = "";
			}
			wFollowerID = wFollowerID.replace("0", "");

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

			String wSQL = StringUtils.Format("SELECT * FROM {0}.aps_dayplanauditbpm WHERE  1=1  "
					+ "and ( :wID <= 0 or :wID = ID ) " + "and ( :wCode is null or :wCode = '''' or :wCode = Code ) "
					+ "and ( :wUpFlowID <= 0 or :wUpFlowID = UpFlowID ) "
					+ "and ( :wFollowerID = '''' or  find_in_set( :wFollowerID,replace(FollowerID,'';'','','') ) ) "

					+ "and ( :wStartTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wStartTime <= SubmitTime) "
					+ "and ( :wEndTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wEndTime >= CreateTime) "

					+ "and ( :wAreaID <= 0 or :wAreaID = AreaID ) and ( :wShiftID <= 0 or :wShiftID = ShiftID ) "
					+ "and ( :wStatus is null or :wStatus = '''' or Status in ({1}));", wInstance.Result,
					wStateIDList.size() > 0 ? StringUtils.Join(",", wStateIDList) : "0");

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wCode", wCode);
			wParamMap.put("wUpFlowID", wUpFlowID);
			wParamMap.put("wFollowerID", wFollowerID);
			wParamMap.put("wAreaID", wAreaID);
			wParamMap.put("wShiftID", wShiftID);
			wParamMap.put("wStatus", StringUtils.Join(",", wStateIDList));
			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetValue(wResultList, wQueryResult);
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	public List<APSDayPlanAuditBPM> SelectList(BMSEmployee wLoginUser, List<Integer> wTaskIDList,
			OutResult<Integer> wErrorCode) {
		List<APSDayPlanAuditBPM> wResultList = new ArrayList<APSDayPlanAuditBPM>();
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
					"SELECT * FROM {0}.aps_dayplanauditbpm WHERE  1=1  "
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
	 * 数据赋值
	 */
	private void SetValue(List<APSDayPlanAuditBPM> wResultList, List<Map<String, Object>> wQueryResult) {
		try {
			for (Map<String, Object> wReader : wQueryResult) {
				APSDayPlanAuditBPM wItem = new APSDayPlanAuditBPM();

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
				wItem.AreaID = StringUtils.parseInt(wReader.get("AreaID"));
				wItem.ShiftID = StringUtils.parseInt(wReader.get("ShiftID"));
				wItem.ShiftDate = StringUtils.parseCalendar(wReader.get("ShiftDate"));
				wItem.AuditID = StringUtils.parseInt(wReader.get("AuditID"));
				wItem.AuditTime = StringUtils.parseCalendar(wReader.get("AuditTime"));
				wItem.AuditItemBPMList = APSDayPlanAuditItemBPM
						.StringToList(StringUtils.parseString(wReader.get("AuditItemBPMList")));

				wItem.AreaName = APSConstans.GetBMSDepartmentName(wItem.AreaID);
				wItem.Auditor = APSConstans.GetBMSEmployeeName(wItem.AuditID);
				wItem.UpFlowName = APSConstans.GetBMSEmployeeName(wItem.UpFlowID);
				wItem.FollowerName = this.GetNames(wItem.FollowerID);

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 获取最新的编码
	 */
	public String GetNewCode(BMSEmployee wLoginUser, OutResult<Integer> wErrorCode) {
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
					"select count(*)+1 as Number from {0}.aps_dayplanauditbpm where CreateTime > :wSTime and CreateTime < :wETime;",
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

			wResult = StringUtils.Format("DP{0}{1}{2}", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)),
					String.format("%02d", Calendar.getInstance().get(Calendar.MONTH) + 1),
					String.format("%04d", wNumber));
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
		List<APSDayPlanAuditBPM> wResult = new ArrayList<APSDayPlanAuditBPM>();
		try {
			// 获取所有任务消息
			List<BFCMessage> wMessageList = CoreServiceImpl
					.getInstance().BFC_GetMessageList(wLoginUser, wLoginUser.getID(),
							BPMEventModule.SCDayAudit.getValue(), -1, BFCMessageType.Task.getValue(), 0, -1, null, null)
					.List(BFCMessage.class);
			wMessageList.addAll(CoreServiceImpl
					.getInstance().BFC_GetMessageList(wLoginUser, wLoginUser.getID(),
							BPMEventModule.SCDayAudit.getValue(), -1, BFCMessageType.Task.getValue(), 1, -1, null, null)
					.List(BFCMessage.class));
			wMessageList.addAll(CoreServiceImpl
					.getInstance().BFC_GetMessageList(wLoginUser, wLoginUser.getID(),
							BPMEventModule.SCDayAudit.getValue(), -1, BFCMessageType.Task.getValue(), 2, -1, null, null)
					.List(BFCMessage.class));

			List<Integer> wTaskIDList = wMessageList.stream().map(p -> (int) p.MessageID).distinct()
					.collect(Collectors.toList());

			// 所有未完成的任务
			Map<Integer, APSDayPlanAuditBPM> wTaskMap = new HashMap<Integer, APSDayPlanAuditBPM>();
			if (wTaskIDList != null && wTaskIDList.size() > 0) {
				List<APSDayPlanAuditBPM> wMTCTaskListTemp = this.SelectList(wLoginUser, wTaskIDList, wErrorCode);

				wTaskMap = wMTCTaskListTemp.stream().collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));

			}
			APSDayPlanAuditBPM wTaskTemp = null;
			for (BFCMessage wBFCMessage : wMessageList) {
				if (!wTaskMap.containsKey((int) wBFCMessage.getMessageID()))
					continue;

				wTaskTemp = CloneTool.Clone(wTaskMap.get((int) wBFCMessage.getMessageID()), APSDayPlanAuditBPM.class);
				wTaskTemp.StepID = wBFCMessage.getStepID();
				wResult.add(wTaskTemp);
			}

			wResult.sort(Comparator.comparing(APSDayPlanAuditBPM::getSubmitTime).reversed());
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
		List<APSDayPlanAuditBPM> wResult = new ArrayList<APSDayPlanAuditBPM>();
		wErrorCode.set(0);
		try {
			List<APSDayPlanAuditBPM> wTaskList = new ArrayList<APSDayPlanAuditBPM>();
			// 获取所有任务消息
			List<BFCMessage> wMessageList = CoreServiceImpl.getInstance()
					.BFC_GetMessageList(wLoginUser, wLoginUser.getID(), BPMEventModule.SCDayAudit.getValue(), -1,
							BFCMessageType.Task.getValue(), 3, -1, wStartTime, wEndTime)
					.List(BFCMessage.class);
			wMessageList
					.addAll(CoreServiceImpl.getInstance()
							.BFC_GetMessageList(wLoginUser, wLoginUser.getID(), BPMEventModule.SCDayAudit.getValue(),
									-1, BFCMessageType.Task.getValue(), 4, -1, wStartTime, wEndTime)
							.List(BFCMessage.class));

			List<Integer> wTaskIDList = wMessageList.stream().map(p -> (int) p.MessageID).distinct()
					.collect(Collectors.toList());

			wTaskList = this.SelectList(wLoginUser, wTaskIDList, wErrorCode);

			wTaskList.sort(Comparator.comparing(APSDayPlanAuditBPM::getSubmitTime).reversed());

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
		List<APSDayPlanAuditBPM> wResult = new ArrayList<>();
		try {
			wResult = this.SelectList(wLoginUser, -1, "", wResponsorID, "", -1, -1, null, wStartTime, wEndTime,
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
			wResult = this.Update(wLoginUser, (APSDayPlanAuditBPM) wTask, wErrorCode);
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

			List<APSDayPlanAuditBPM> wList = SelectList(wLoginUser, wTaskID, wCode, -1, "", -1, -1, null, null, null,
					wErrorCode);
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
