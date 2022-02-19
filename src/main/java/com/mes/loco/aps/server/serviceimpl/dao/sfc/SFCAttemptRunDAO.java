package com.mes.loco.aps.server.serviceimpl.dao.sfc;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.mes.loco.aps.server.service.mesenum.SFCAttemptRunStatus;
import com.mes.loco.aps.server.service.po.OutResult;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.bfc.BFCMessage;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.bpm.BPMOperationStep;
import com.mes.loco.aps.server.service.po.bpm.BPMTaskBase;
import com.mes.loco.aps.server.service.po.rro.RROItemTask;
import com.mes.loco.aps.server.service.po.sfc.SFCAttemptRun;
import com.mes.loco.aps.server.service.po.sfc.SFCRepairItem;
import com.mes.loco.aps.server.service.utils.CloneTool;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.BPMServiceImpl;
import com.mes.loco.aps.server.serviceimpl.CoreServiceImpl;
import com.mes.loco.aps.server.serviceimpl.SFCServiceImpl;
import com.mes.loco.aps.server.serviceimpl.WDWServiceImpl;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;
import com.mes.loco.aps.server.serviceimpl.dao.TaskBaseDAO;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSConstans;

/**
 * 偶换件不合格评审
 * 
 * @author ShrisJava
 *
 */
public class SFCAttemptRunDAO extends BaseDAO implements TaskBaseDAO {

	private static Logger logger = LoggerFactory.getLogger(SFCAttemptRunDAO.class);

	private static SFCAttemptRunDAO Instance = null;

	private SFCAttemptRunDAO() {
		super();
	}

	public static SFCAttemptRunDAO getInstance() {
		if (Instance == null)
			Instance = new SFCAttemptRunDAO();
		return Instance;
	}

	/**
	 * 添加或修改
	 * 
	 * @param wSFCAttemptRun
	 * @return
	 */
	public SFCAttemptRun Update(BMSEmployee wLoginUser, SFCAttemptRun wSFCAttemptRun, OutResult<Integer> wErrorCode) {
		SFCAttemptRun wResult = new SFCAttemptRun();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.APS, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wSFCAttemptRun == null)
				return wResult;

			if (wSFCAttemptRun.FollowerID == null) {
				wSFCAttemptRun.FollowerID = new ArrayList<Integer>();
			}

			String wSQL = "";
			if (wSFCAttemptRun.getID() <= 0) {
				wSQL = StringUtils.Format(
						"INSERT INTO {0}.sfc_attemptrun(Code,FlowType,FlowID,UpFlowID,"
								+ "FollowerID,Status,StatusText,CreateTime,SubmitTime,OrderID,CheckerID,"
								+ "PartID,ItemList) VALUES(:Code,:FlowType,:FlowID,:UpFlowID,:FollowerID,"
								+ ":Status,:StatusText,:CreateTime,:SubmitTime,:OrderID,:CheckerID,:PartID,:ItemList);",
						wInstance.Result);
			} else {
				wSQL = StringUtils.Format("UPDATE {0}.sfc_attemptrun SET Code = :Code,FlowType = :FlowType,"
						+ "FlowID = :FlowID,UpFlowID = :UpFlowID,FollowerID = :FollowerID,"
						+ "Status = :Status,StatusText = :StatusText,CreateTime = :CreateTime,"
						+ "SubmitTime = now(),OrderID = :OrderID,CheckerID = :CheckerID,PartID = :PartID,"
						+ "ItemList = :ItemList WHERE ID = :ID;", wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			String wItemList = "";
			if (wSFCAttemptRun.ItemList != null && wSFCAttemptRun.ItemList.size() > 0) {
				for (SFCRepairItem wSFCRepairItem : wSFCAttemptRun.ItemList) {
					wSFCRepairItem.ID = SFCRepairItemDAO.getInstance().Update(wLoginUser, wSFCRepairItem, wErrorCode);
				}
				wItemList = StringUtils.Join(";",
						wSFCAttemptRun.ItemList.stream().map(p -> p.ID).collect(Collectors.toList()));
			}

			wParamMap.put("ID", wSFCAttemptRun.ID);
			wParamMap.put("Code", wSFCAttemptRun.Code);
			wParamMap.put("FlowType", wSFCAttemptRun.FlowType);
			wParamMap.put("FlowID", wSFCAttemptRun.FlowID);
			wParamMap.put("UpFlowID", wSFCAttemptRun.UpFlowID);
			wParamMap.put("FollowerID", StringUtils.Join(",", wSFCAttemptRun.FollowerID));
			wParamMap.put("Status", wSFCAttemptRun.Status);
			wParamMap.put("StatusText", wSFCAttemptRun.StatusText);
			wParamMap.put("CreateTime", wSFCAttemptRun.CreateTime);
			wParamMap.put("SubmitTime", wSFCAttemptRun.SubmitTime);
			wParamMap.put("OrderID", wSFCAttemptRun.OrderID);
			wParamMap.put("CheckerID", wSFCAttemptRun.CheckerID);
			wParamMap.put("PartID", wSFCAttemptRun.PartID);
			wParamMap.put("ItemList", wItemList);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wSFCAttemptRun.getID() <= 0) {
				wSFCAttemptRun.setID(keyHolder.getKey().intValue());
			}
			wResult = wSFCAttemptRun;
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResult;
	}

	public List<SFCAttemptRun> SelectList(BMSEmployee wLoginUser, int wID, String wCode, int wUpFlowID, int wOrderID,
			int wCheckerID, int wPartID, List<Integer> wStateIDList, Calendar wStartTime, Calendar wEndTime,
			OutResult<Integer> wErrorCode) {
		return this.SelectList(wLoginUser, wID, wCode, wUpFlowID, wOrderID, -1, -1, -1, "", wCheckerID, wPartID,
				wStateIDList, null, wStartTime, wEndTime, wErrorCode);
	}

	public List<SFCAttemptRun> SelectList(BMSEmployee wLoginUser, int wOrderID, int wLineID, int wCustomerID,
			int wProductID, String wPartNo, int wCheckerID, int wPartID, List<Integer> wStateIDList,
			List<Integer> wNoStateIDList, Calendar wStartTime, Calendar wEndTime, OutResult<Integer> wErrorCode) {
		return this.SelectList(wLoginUser, -1, "", -1, wOrderID, wLineID, wCustomerID, wProductID, wPartNo, wCheckerID,
				wPartID, wStateIDList, wNoStateIDList, wStartTime, wEndTime, wErrorCode);
	}

	/**
	 * 条件查询集合
	 * 
	 * @return
	 */
	private List<SFCAttemptRun> SelectList(BMSEmployee wLoginUser, int wID, String wCode, int wUpFlowID, int wOrderID,
			int wLineID, int wCustomerID, int wProductID, String wPartNo, int wCheckerID, int wPartID,
			List<Integer> wStateIDList, List<Integer> wNoStateIDList, Calendar wStartTime, Calendar wEndTime,
			OutResult<Integer> wErrorCode) {
		List<SFCAttemptRun> wResultList = new ArrayList<SFCAttemptRun>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.APS, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
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
			if (wPartNo == null)
				wPartNo = "";

			if (wStateIDList == null) {
				wStateIDList = new ArrayList<Integer>();
			}

			String wSQL = StringUtils.Format(
					"SELECT t.*,t1.LineID,t1.ProductID,t2.CustomerID,t2.WBSNo,t1.PartNo FROM {0}.sfc_attemptrun t "
							+ " inner join {0}.oms_order t1 on t.OrderID=t1.ID "
							+ " left join {0}.oms_command t2 on t1.CommandID=t2.ID WHERE  1=1  "
							+ " and ( :wID <= 0 or :wID = t.ID ) "
							+ " and ( :wCode is null or :wCode = '''' or :wCode = t.Code ) "
							+ " and ( :wUpFlowID <= 0 or :wUpFlowID = t.UpFlowID ) "
							+ " and ( :wOrderID <= 0 or :wOrderID = t.OrderID ) "
							+ " and ( :wLineID <= 0 or :wLineID = t1.LineID ) "
							+ " and ( :wCustomerID <= 0 or :wCustomerID = t2.CustomerID ) "
							+ " and ( :wProductID <= 0 or :wProductID = t1.ProductID ) "
							+ " and ( :wPartNo ='''' or :wPartNo = t1.PartNo ) "
							+ " and ( :wStartTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wStartTime <= t.SubmitTime) "
							+ " and ( :wEndTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wEndTime >= t.CreateTime) "

							+ " and ( :wCheckerID <= 0 or :wCheckerID = t.CheckerID ) "
							+ " and ( :wPartID <= 0 or :wPartID = t.PartID ) "
							+ " and ( :wStatus = '''' or t.Status in ({1}));",
					wInstance.Result, wStateIDList.size() > 0 ? StringUtils.Join(",", wStateIDList) : "0");

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wCode", wCode);
			wParamMap.put("wUpFlowID", wUpFlowID);
			wParamMap.put("wOrderID", wOrderID);
			wParamMap.put("wLineID", wLineID);
			wParamMap.put("wCustomerID", wCustomerID);
			wParamMap.put("wProductID", wProductID);
			wParamMap.put("wPartNo", wPartNo);
			wParamMap.put("wCheckerID", wCheckerID);
			wParamMap.put("wPartID", wPartID);
			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);
			wParamMap.put("wStatus", StringUtils.Join(",", wStateIDList));

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetValue(wLoginUser, wErrorCode, wResultList, wQueryResult);
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	/**
	 * 赋值
	 */
	private void SetValue(BMSEmployee wLoginUser, OutResult<Integer> wErrorCode, List<SFCAttemptRun> wResultList,
			List<Map<String, Object>> wQueryResult) {
		try {

			for (Map<String, Object> wReader : wQueryResult) {
				SFCAttemptRun wItem = new SFCAttemptRun();

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
				wItem.OrderID = StringUtils.parseInt(wReader.get("OrderID"));
				wItem.CheckerID = StringUtils.parseInt(wReader.get("CheckerID"));
				wItem.PartID = StringUtils.parseInt(wReader.get("PartID"));
				wItem.ItemList = SFCRepairItemDAO.getInstance().SelectList(wLoginUser, -1, "", -1, -1, wItem.ID,
						wErrorCode);

				// 返修项列表
				if (wItem.ItemList != null && wItem.ItemList.size() > 0) {
					List<Integer> wIDList = wItem.ItemList.stream().map(p -> p.ItemTaskID).distinct().filter(p -> p > 0)
							.collect(Collectors.toList());
					if (wIDList.size() > 0) {
						List<RROItemTask> wItemTaskList = WDWServiceImpl.getInstance()
								.RRO_QueryItemTaskList(wLoginUser, wIDList).List(RROItemTask.class);
						if (wItemTaskList != null && wItemTaskList.size() > 0) {
							wItem.ItemTaskList = wItemTaskList;
						}
					}
				}

				wItem.UpFlowName = APSConstans.GetBMSEmployeeName(wItem.UpFlowID);

				wItem.WBSNo = StringUtils.parseString(wReader.get("WBSNo"));
				wItem.ProductID = StringUtils.parseInt(wReader.get("ProductID"));
				wItem.ProductNo = APSConstans.GetFPCProduct(wItem.ProductID).ProductNo;
				wItem.PartNo = StringUtils.parseString(wReader.get("PartNo"));
				wItem.CheckerName = APSConstans.GetBMSEmployeeName(wItem.CheckerID);
				wItem.PartName = APSConstans.GetFPCPartName(wItem.PartID);

				wItem.LineID = StringUtils.parseInt(wReader.get("LineID"));
				wItem.LineName = APSConstans.GetFMCLineName(wItem.LineID);
				wItem.CustomerID = StringUtils.parseInt(wReader.get("CustomerID"));
				wItem.Customer = APSConstans.GetCRMCustomerName(wItem.CustomerID);

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * ID集合获取任务集合
	 */
	private List<SFCAttemptRun> SelectList(BMSEmployee wLoginUser, List<Integer> wTaskIDList, Calendar wStartTime,
			Calendar wEndTime, OutResult<Integer> wErrorCode) {
		List<SFCAttemptRun> wResultList = new ArrayList<SFCAttemptRun>();
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

			String wSQL = StringUtils.Format(
					"SELECT t.*,t1.LineID,t1.ProductID,t2.CustomerID,t2.WBSNo,t1.PartNo FROM {0}.sfc_attemptrun t "
							+ " inner join {0}.oms_order t1 on t.OrderID=t1.ID "
							+ " left join {0}.oms_command t2 on t1.CommandID=t2.ID WHERE  1=1  "
							+ "and ( :wStartTime <=str_to_date(''2010-01-01'', ''%Y-%m-%d'')  or :wStartTime <=  t.SubmitTime ) "
							+ "and ( :wEndTime <=str_to_date(''2010-01-01'', ''%Y-%m-%d'')  or :wEndTime >=  t.CreateTime ) "
							+ "and ( :wIDs = '''' or t.ID in ({1}));",
					wInstance.Result, wTaskIDList.size() > 0 ? StringUtils.Join(",", wTaskIDList) : "0");

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wIDs", StringUtils.Join(",", wTaskIDList));
			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetValue(wLoginUser, wErrorCode, wResultList, wQueryResult);
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
					"select count(*)+1 as Number from {0}.sfc_attemptrun where CreateTime > :wSTime and CreateTime < :wETime;",
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

			wResult = StringUtils.Format("TR{0}{1}{2}", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)),
					String.format("%02d", Calendar.getInstance().get(Calendar.MONTH) + 1),
					String.format("%04d", wNumber));
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public List<BPMTaskBase> BPM_GetUndoTaskList(BMSEmployee wLoginUser, int wResponsorID,
			OutResult<Integer> wErrorCode) {
		List<SFCAttemptRun> wResult = new ArrayList<SFCAttemptRun>();
		try {
			// 获取所有任务消息
			List<BFCMessage> wMessageList = CoreServiceImpl
					.getInstance().BFC_GetMessageList(wLoginUser, wLoginUser.getID(),
							BPMEventModule.AttemptRun.getValue(), -1, BFCMessageType.Task.getValue(), 0, -1, null, null)
					.List(BFCMessage.class);
			wMessageList.addAll(CoreServiceImpl
					.getInstance().BFC_GetMessageList(wLoginUser, wLoginUser.getID(),
							BPMEventModule.AttemptRun.getValue(), -1, BFCMessageType.Task.getValue(), 1, -1, null, null)
					.List(BFCMessage.class));
			wMessageList.addAll(CoreServiceImpl
					.getInstance().BFC_GetMessageList(wLoginUser, wLoginUser.getID(),
							BPMEventModule.AttemptRun.getValue(), -1, BFCMessageType.Task.getValue(), 2, -1, null, null)
					.List(BFCMessage.class));

			List<Integer> wTaskIDList = wMessageList.stream().map(p -> (int) p.MessageID).distinct()
					.collect(Collectors.toList());

			// 所有未完成的任务
			Map<Integer, SFCAttemptRun> wTaskMap = new HashMap<Integer, SFCAttemptRun>();
			if (wTaskIDList != null && wTaskIDList.size() > 0) {
				List<SFCAttemptRun> wMTCTaskListTemp = this.SelectList(wLoginUser, wTaskIDList, null, null, wErrorCode);

				wTaskMap = wMTCTaskListTemp.stream().collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));

			}
			SFCAttemptRun wTaskTemp = null;
			for (BFCMessage wBFCMessage : wMessageList) {
				if (!wTaskMap.containsKey((int) wBFCMessage.getMessageID()))
					continue;

				wTaskTemp = CloneTool.Clone(wTaskMap.get((int) wBFCMessage.getMessageID()), SFCAttemptRun.class);
				wTaskTemp.StepID = wBFCMessage.getStepID();
				wResult.add(wTaskTemp);
			}

			wResult.sort(Comparator.comparing(SFCAttemptRun::getSubmitTime).reversed());
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
		List<SFCAttemptRun> wResult = new ArrayList<SFCAttemptRun>();
		wErrorCode.set(0);
		try {
			List<SFCAttemptRun> wTaskList = new ArrayList<SFCAttemptRun>();
			// 获取所有任务消息
			List<BFCMessage> wMessageList = CoreServiceImpl.getInstance()
					.BFC_GetMessageList(wLoginUser, wLoginUser.getID(), BPMEventModule.AttemptRun.getValue(), -1,
							BFCMessageType.Task.getValue(), 3, -1, wStartTime, wEndTime)
					.List(BFCMessage.class);
			wMessageList
					.addAll(CoreServiceImpl.getInstance()
							.BFC_GetMessageList(wLoginUser, wLoginUser.getID(), BPMEventModule.AttemptRun.getValue(),
									-1, BFCMessageType.Task.getValue(), 4, -1, wStartTime, wEndTime)
							.List(BFCMessage.class));

			List<Integer> wTaskIDList = wMessageList.stream().map(p -> (int) p.MessageID).distinct()
					.collect(Collectors.toList());

			wTaskList = this.SelectList(wLoginUser, wTaskIDList, wStartTime, wEndTime, wErrorCode);

			wTaskList.sort(Comparator.comparing(SFCAttemptRun::getSubmitTime).reversed());

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
		List<SFCAttemptRun> wResult = new ArrayList<SFCAttemptRun>();
		try {
			wResult = this.SelectList(wLoginUser, -1, "", wResponsorID, -1, -1, -1, null, wStartTime, wEndTime,
					wErrorCode);

			List<SFCAttemptRun> wNFList = this.SelectList(wLoginUser, -1, "", wResponsorID, -1, -1, -1,
					new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4)), null, null, wErrorCode);
			if (wNFList.size() > 0) {
				wResult.addAll(wNFList);

				wResult = new ArrayList<SFCAttemptRun>(wResult.stream()
						.collect(Collectors.toMap(SFCAttemptRun::getID, account -> account, (k1, k2) -> k2)).values());
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return new ArrayList<BPMTaskBase>(wResult);
	}

	@Override
	public BPMTaskBase BPM_UpdateTask(BMSEmployee wLoginUser, BPMTaskBase wTask, OutResult<Integer> wErrorCode) {
		BPMTaskBase wResult = new BPMTaskBase();
		try {
			wResult = this.Update(wLoginUser, (SFCAttemptRun) wTask, wErrorCode);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public BPMTaskBase BPM_GetTaskInfo(BMSEmployee wLoginUser, int wTaskID, String wCode,
			OutResult<Integer> wErrorCode) {
		SFCAttemptRun wResult = new SFCAttemptRun();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.APS, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<SFCAttemptRun> wList = SelectList(wLoginUser, wTaskID, wCode, -1, -1, -1, -1, null, null, null,
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

	/**
	 * 自动判断返修是否完成
	 */
	public boolean AutoJudge(BMSEmployee wLoginUser, SFCAttemptRun wSFCAttemptRun, OutResult<Integer> wErrorCode) {
		boolean wResult = false;
		try {
			if (wSFCAttemptRun == null || wSFCAttemptRun.ID <= 0 || wSFCAttemptRun.StepID <= 0
					|| wSFCAttemptRun.Status == SFCAttemptRunStatus.NomalClose.getValue()) {
				return wResult;
			}

			// 是否满足条件 否 return false;
			List<BPMOperationStep> wBPMOperationStepList = BPMServiceImpl.getInstance()
					.BPM_GetOperationByTaskID(wLoginUser, wSFCAttemptRun.StepID).List(BPMOperationStep.class);

			if (wBPMOperationStepList == null || wBPMOperationStepList.size() <= 0) {
				wResult = false;
				return wResult;
			}

			// 判断所有返修项是否全部完成
			List<RROItemTask> wItemTaskList = WDWServiceImpl.getInstance()
					.RRO_QueryNotFinishItemList(wLoginUser, wSFCAttemptRun.OrderID).List(RROItemTask.class);
			if (wItemTaskList != null && wItemTaskList.size() > 0) {
				wResult = false;
				return wResult;
			}

			BPMOperationStep wBPMOperationStep = wBPMOperationStepList.get(0);

			Field[] fields = wSFCAttemptRun.getClass().getFields();
			for (Field wField : fields) {
				if (!wField.getName().equals(wBPMOperationStep.Name))
					continue;

				wField.set(wSFCAttemptRun, CloneTool.Clone(wBPMOperationStep.Value, wField.getType()));
			}

			// 获取表单属性
			wSFCAttemptRun.ItemDone = 1;
			wSFCAttemptRun.StatusText = "";

			ServiceResult<Boolean> bpm_MsgUpdate = BPMServiceImpl.getInstance().BPM_MsgUpdate(wLoginUser,
					wSFCAttemptRun.StepID, 0, wSFCAttemptRun, wSFCAttemptRun);

			if (!bpm_MsgUpdate.getResult() || StringUtils.isNotEmpty(bpm_MsgUpdate.getFaultCode()))
				SFCServiceImpl.getInstance().SFC_SubmitAttemptRun(wLoginUser, wSFCAttemptRun);

			wResult = true;
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}
}
