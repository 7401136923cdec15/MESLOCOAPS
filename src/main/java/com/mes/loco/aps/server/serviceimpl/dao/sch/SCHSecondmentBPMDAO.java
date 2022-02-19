package com.mes.loco.aps.server.serviceimpl.dao.sch;

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
import com.mes.loco.aps.server.service.po.bfc.BFCMessage;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.bpm.BPMTaskBase;
import com.mes.loco.aps.server.service.po.sch.SCHSecondmentBPM;
import com.mes.loco.aps.server.service.utils.CloneTool;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.CoreServiceImpl;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;
import com.mes.loco.aps.server.serviceimpl.dao.TaskBaseDAO;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSConstans;

public class SCHSecondmentBPMDAO extends BaseDAO implements TaskBaseDAO {

	private static Logger logger = LoggerFactory.getLogger(SCHSecondmentBPMDAO.class);

	private static SCHSecondmentBPMDAO Instance = null;

	private SCHSecondmentBPMDAO() {
		super();
	}

	public static SCHSecondmentBPMDAO getInstance() {
		if (Instance == null)
			Instance = new SCHSecondmentBPMDAO();
		return Instance;
	}

	/**
	 * 添加或修改
	 * 
	 * @param wSCHSecondmentBPM
	 * @return
	 */
	public SCHSecondmentBPM Update(BMSEmployee wLoginUser, SCHSecondmentBPM wSCHSecondmentBPM,
			OutResult<Integer> wErrorCode) {
		SCHSecondmentBPM wResult = new SCHSecondmentBPM();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.APS, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wSCHSecondmentBPM == null)
				return wResult;

			String wSQL = "";
			if (wSCHSecondmentBPM.getID() <= 0) {
				wSQL = StringUtils.Format(
						"INSERT INTO {0}.sch_secondmentbpm(Code,FlowType,FlowID,UpFlowID,FollowerID,"
								+ "Status,StatusText,CreateTime,SubmitTime,Type,PersonID,OldClassID,"
								+ "NewClassID,OldPartList,NewPartList,OldPosition,NewPosition,StartTime,"
								+ "EndTime) VALUES(:Code,:FlowType,:FlowID,:UpFlowID,:FollowerID,"
								+ ":Status,:StatusText,:CreateTime,:SubmitTime,:Type,:PersonID,:OldClassID,:NewClassID,"
								+ ":OldPartList,:NewPartList,:OldPosition,:NewPosition,:StartTime,:EndTime);",
						wInstance.Result);
			} else {
				wSQL = StringUtils.Format("UPDATE {0}.sch_secondmentbpm SET Code = :Code,FlowType = :FlowType,"
						+ "FlowID = :FlowID,UpFlowID = :UpFlowID,FollowerID = :FollowerID,Status = :Status,"
						+ "StatusText = :StatusText,CreateTime = :CreateTime,SubmitTime = now(),"
						+ "Type = :Type,PersonID = :PersonID,OldClassID = :OldClassID,NewClassID = :NewClassID,"
						+ "OldPartList = :OldPartList,NewPartList = :NewPartList,OldPosition = :OldPosition,"
						+ "NewPosition = :NewPosition,StartTime = :StartTime,EndTime = :EndTime,SubmitTime=now() "
						+ "WHERE ID = :ID;", wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wSCHSecondmentBPM.ID);
			wParamMap.put("Code", wSCHSecondmentBPM.Code);
			wParamMap.put("FlowType", wSCHSecondmentBPM.FlowType);
			wParamMap.put("FlowID", wSCHSecondmentBPM.FlowID);
			wParamMap.put("UpFlowID", wSCHSecondmentBPM.UpFlowID);
			wParamMap.put("FollowerID", StringUtils.Join(",", wSCHSecondmentBPM.FollowerID));
			wParamMap.put("Status", wSCHSecondmentBPM.Status);
			wParamMap.put("StatusText", wSCHSecondmentBPM.StatusText);
			wParamMap.put("CreateTime", wSCHSecondmentBPM.CreateTime);
			wParamMap.put("SubmitTime", wSCHSecondmentBPM.SubmitTime);
			wParamMap.put("Type", wSCHSecondmentBPM.Type);
			wParamMap.put("PersonID", wSCHSecondmentBPM.PersonID);
			wParamMap.put("OldClassID", wSCHSecondmentBPM.OldClassID);
			wParamMap.put("NewClassID", wSCHSecondmentBPM.NewClassID);
			wParamMap.put("OldPartList", wSCHSecondmentBPM.OldPartList);
			wParamMap.put("NewPartList", wSCHSecondmentBPM.NewPartList);
			wParamMap.put("OldPosition", wSCHSecondmentBPM.OldPosition);
			wParamMap.put("NewPosition", wSCHSecondmentBPM.NewPosition);
			wParamMap.put("StartTime", wSCHSecondmentBPM.StartTime);
			wParamMap.put("EndTime", wSCHSecondmentBPM.EndTime);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wSCHSecondmentBPM.getID() <= 0) {
				wSCHSecondmentBPM.setID(keyHolder.getKey().intValue());
			}
			wResult = wSCHSecondmentBPM;
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResult;
	}

	public List<SCHSecondmentBPM> SelectList(BMSEmployee wLoginUser, int wID, String wCode, int wUpFlowID,
			String wFollowerID, int wType, int wPersonID, List<Integer> wStateIDList, Calendar wStartTime,
			Calendar wEndTime, OutResult<Integer> wErrorCode) {
		return SelectList(wLoginUser, wID, wCode, wUpFlowID, wFollowerID, wType, wPersonID, wStateIDList, null,
				wStartTime, wEndTime, wErrorCode);
	}

	public List<SCHSecondmentBPM> SelectList(BMSEmployee wLoginUser, int wType, int wPersonID,
			List<Integer> wStateIDList, List<Integer> wNoStateIDList, Calendar wStartTime, Calendar wEndTime,
			OutResult<Integer> wErrorCode) {
		return SelectList(wLoginUser, -1, "", -1, "", wType, wPersonID, wStateIDList, wNoStateIDList, wStartTime,
				wEndTime, wErrorCode);
	}

	/**
	 * 条件查询集合
	 * 
	 * @return
	 */
	private List<SCHSecondmentBPM> SelectList(BMSEmployee wLoginUser, int wID, String wCode, int wUpFlowID,
			String wFollowerID, int wType, int wPersonID, List<Integer> wStateIDList, List<Integer> wNoStateIDList,
			Calendar wStartTime, Calendar wEndTime, OutResult<Integer> wErrorCode) {
		List<SCHSecondmentBPM> wResultList = new ArrayList<SCHSecondmentBPM>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.APS, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			if (wStateIDList == null) {
				wStateIDList = new ArrayList<Integer>();
			}
			wStateIDList.removeIf(p -> p < 0);
			if (wNoStateIDList == null) {
				wNoStateIDList = new ArrayList<Integer>();
			}
			wNoStateIDList.removeIf(p -> p < 0);
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

			String wSQL = StringUtils.Format("SELECT * FROM {0}.sch_secondmentbpm WHERE  1=1  "
					+ " and ( :wID <= 0 or :wID = ID ) " + "and ( :wCode is null or :wCode = '''' or :wCode = Code ) "
					+ " and ( :wUpFlowID <= 0 or :wUpFlowID = UpFlowID ) "
					+ " and ( :wFollowerID is null or :wFollowerID = '''' or :wFollowerID = FollowerID ) "

					+ " and ( :wStartTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wStartTime <= SubmitTime) "
					+ " and ( :wEndTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wEndTime >= CreateTime) "

					+ " and ( :wType <= 0 or :wType = Type ) and ( :wPersonID <= 0 or :wPersonID = PersonID ) "
					+ " and ( :wStatus = '''' or Status in ({1})) and ( :wNoStatus = '''' or Status not in ({2}));",
					wInstance.Result, wStateIDList.size() > 0 ? StringUtils.Join(",", wStateIDList) : "0",
					wNoStateIDList.size() > 0 ? StringUtils.Join(",", wNoStateIDList) : "0");

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wCode", wCode);
			wParamMap.put("wUpFlowID", wUpFlowID);
			wParamMap.put("wFollowerID", wFollowerID);
			wParamMap.put("wType", wType);
			wParamMap.put("wPersonID", wPersonID);
			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);
			wParamMap.put("wStatus", StringUtils.Join(",", wStateIDList));
			wParamMap.put("wNoStatus", StringUtils.Join(",", wNoStateIDList));

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
	private void SetValue(List<SCHSecondmentBPM> wResultList, List<Map<String, Object>> wQueryResult) {
		try {
			for (Map<String, Object> wReader : wQueryResult) {
				SCHSecondmentBPM wItem = new SCHSecondmentBPM();

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
				wItem.Type = StringUtils.parseInt(wReader.get("Type"));
				wItem.PersonID = StringUtils.parseString(wReader.get("PersonID"));
				wItem.OldClassID = StringUtils.parseInt(wReader.get("OldClassID"));
				wItem.NewClassID = StringUtils.parseInt(wReader.get("NewClassID"));
				wItem.OldPartList = StringUtils.parseString(wReader.get("OldPartList"));
				wItem.NewPartList = StringUtils.parseString(wReader.get("NewPartList"));
				wItem.OldPosition = StringUtils.parseString(wReader.get("OldPosition"));
				wItem.NewPosition = StringUtils.parseString(wReader.get("NewPosition"));
				wItem.StartTime = StringUtils.parseCalendar(wReader.get("StartTime"));
				wItem.EndTime = StringUtils.parseCalendar(wReader.get("EndTime"));

				wItem.UpFlowName = APSConstans.GetBMSEmployeeName(wItem.UpFlowID);
				wItem.FollowerName = this.GetNames(wItem.FollowerID);
				wItem.PersonName = APSConstans.GetName(wItem.PersonID);
				wItem.OldClassName = APSConstans.GetBMSDepartmentName(wItem.OldClassID);
				wItem.NewClassName = APSConstans.GetBMSDepartmentName(wItem.NewClassID);
				wItem.OldPartNames = this.GetPartNames(StringUtils.parseIntList(wItem.OldPartList.split(",|;")));
				wItem.NewPartNames = this.GetPartNames(StringUtils.parseIntList(wItem.NewPartList.split(",|;")));

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 获取工位名称
	 */
	private String GetPartNames(List<Integer> wPartIDList) {
		String wResult = "";
		try {
			if (wPartIDList == null || wPartIDList.size() <= 0) {
				return wResult;
			}

			List<String> wNames = new ArrayList<String>();
			for (Integer wInteger : wPartIDList) {
				String wName = APSConstans.GetFPCPartName(wInteger);
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

	public List<SCHSecondmentBPM> SelectList(BMSEmployee wLoginUser, List<Integer> wTaskIDList, Calendar wStartTime,
			Calendar wEndTime, OutResult<Integer> wErrorCode) {
		List<SCHSecondmentBPM> wResultList = new ArrayList<SCHSecondmentBPM>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.APS, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			if (wTaskIDList == null || wTaskIDList.size() <= 0) {
				return wResultList;
			}

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1, 0, 0, 0);
			if (wStartTime == null) {
				wStartTime = wBaseTime;
			}
			if (wEndTime == null) {
				wEndTime = wBaseTime;
			}
			if (wStartTime.compareTo(wEndTime) > 0) {
				return wResultList;
			}

			String wSQL = StringUtils.Format("SELECT * FROM {0}.sch_secondmentbpm WHERE  1=1  "
					+ "and ( :wStartTime <=str_to_date(''2010-01-01'', ''%Y-%m-%d'')  or :wStartTime <=  SubmitTime ) "
					+ "and ( :wEndTime <=str_to_date(''2010-01-01'', ''%Y-%m-%d'')  or :wEndTime >=  CreateTime ) "
					+ "and ( :wIDs is null or :wIDs = '''' or ID in ({1}));", wInstance.Result,
					wTaskIDList.size() > 0 ? StringUtils.Join(",", wTaskIDList) : "0");

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wIDs", StringUtils.Join(",", wTaskIDList));
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
					"select count(*)+1 as Number from {0}.sch_secondmentbpm where CreateTime > :wSTime and CreateTime < :wETime;",
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

			wResult = StringUtils.Format("TP{0}{1}{2}", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)),
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
		List<SCHSecondmentBPM> wResult = new ArrayList<SCHSecondmentBPM>();
		try {
			// 获取所有任务消息
			List<BFCMessage> wMessageList = CoreServiceImpl.getInstance()
					.BFC_GetMessageList(wLoginUser, wLoginUser.getID(), BPMEventModule.ToLoan.getValue(), -1,
							BFCMessageType.Task.getValue(), 0, -1, null, null)
					.List(BFCMessage.class);
			wMessageList.addAll(CoreServiceImpl.getInstance().BFC_GetMessageList(wLoginUser, wLoginUser.getID(),
					BPMEventModule.ToLoan.getValue(), -1, BFCMessageType.Task.getValue(), 1, -1, null, null)
					.List(BFCMessage.class));
			wMessageList.addAll(CoreServiceImpl.getInstance().BFC_GetMessageList(wLoginUser, wLoginUser.getID(),
					BPMEventModule.ToLoan.getValue(), -1, BFCMessageType.Task.getValue(), 2, -1, null, null)
					.List(BFCMessage.class));

			List<Integer> wTaskIDList = wMessageList.stream().map(p -> (int) p.MessageID).distinct()
					.collect(Collectors.toList());

			// 所有未完成的任务
			Map<Integer, SCHSecondmentBPM> wTaskMap = new HashMap<Integer, SCHSecondmentBPM>();
			if (wTaskIDList != null && wTaskIDList.size() > 0) {
				List<SCHSecondmentBPM> wMTCTaskListTemp = this.SelectList(wLoginUser, wTaskIDList, null, null,
						wErrorCode);

				wTaskMap = wMTCTaskListTemp.stream().collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));

			}
			SCHSecondmentBPM wTaskTemp = null;
			for (BFCMessage wBFCMessage : wMessageList) {
				if (!wTaskMap.containsKey((int) wBFCMessage.getMessageID()))
					continue;

				wTaskTemp = CloneTool.Clone(wTaskMap.get((int) wBFCMessage.getMessageID()), SCHSecondmentBPM.class);
				wTaskTemp.StepID = wBFCMessage.getStepID();
				wResult.add(wTaskTemp);
			}

			wResult.sort(Comparator.comparing(SCHSecondmentBPM::getSubmitTime).reversed());
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
		List<SCHSecondmentBPM> wResult = new ArrayList<SCHSecondmentBPM>();
		wErrorCode.set(0);
		try {
			List<SCHSecondmentBPM> wTaskList = new ArrayList<SCHSecondmentBPM>();
			// 获取所有任务消息
			List<BFCMessage> wMessageList = CoreServiceImpl.getInstance()
					.BFC_GetMessageList(wLoginUser, wLoginUser.getID(), BPMEventModule.ToLoan.getValue(), -1,
							BFCMessageType.Task.getValue(), 3, -1, wStartTime, wEndTime)
					.List(BFCMessage.class);
			wMessageList
					.addAll(CoreServiceImpl.getInstance()
							.BFC_GetMessageList(wLoginUser, wLoginUser.getID(), BPMEventModule.ToLoan.getValue(), -1,
									BFCMessageType.Task.getValue(), 4, -1, wStartTime, wEndTime)
							.List(BFCMessage.class));

			List<Integer> wTaskIDList = wMessageList.stream().map(p -> (int) p.MessageID).distinct()
					.collect(Collectors.toList());

			wTaskList = this.SelectList(wLoginUser, wTaskIDList, wStartTime, wEndTime, wErrorCode);

			wTaskList.sort(Comparator.comparing(SCHSecondmentBPM::getSubmitTime).reversed());

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
		List<SCHSecondmentBPM> wResult = new ArrayList<>();
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
			wResult = this.Update(wLoginUser, (SCHSecondmentBPM) wTask, wErrorCode);
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

			List<SCHSecondmentBPM> wList = SelectList(wLoginUser, wTaskID, "", -1, "", -1, -1, null, null, null,
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
