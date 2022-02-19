package com.mes.loco.aps.server.serviceimpl.dao.aps;

import java.text.MessageFormat;
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
import com.mes.loco.aps.server.service.po.aps.APSBomBPM;
import com.mes.loco.aps.server.service.po.aps.APSBomBPMItem;
import com.mes.loco.aps.server.service.po.bfc.BFCMessage;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.bpm.BPMTaskBase;
import com.mes.loco.aps.server.service.utils.CloneTool;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.CoreServiceImpl;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;
import com.mes.loco.aps.server.serviceimpl.dao.TaskBaseDAO;
import com.mes.loco.aps.server.serviceimpl.dao.oms.OMSOrderDAO;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSConstans;

public class APSBomBPMDAO extends BaseDAO implements TaskBaseDAO {

	private static Logger logger = LoggerFactory.getLogger(APSBomBPM.class);

	private static APSBomBPMDAO Instance = null;

	/**
	 * 添加或修改
	 */
	public APSBomBPM Update(BMSEmployee wLoginUser, APSBomBPM wAPSBomBPM, OutResult<Integer> wErrorCode) {
		APSBomBPM wResult = wAPSBomBPM;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wAPSBomBPM == null)
				return wResult;

			String wSQL = "";
			if (wAPSBomBPM.getID() <= 0) {
				wSQL = MessageFormat.format(
						"INSERT INTO {0}.aps_bombpm(Code,FlowType,FlowID,UpFlowID,FollowerID,Status,StatusText,CreateTime,SubmitTime,Type,OrderID,LineID,ProductID,CustomerID,PartNo,DescribInfo) VALUES(:Code,:FlowType,:FlowID,:UpFlowID,:FollowerID,:Status,:StatusText,:CreateTime,:SubmitTime,:Type,:OrderID,:LineID,:ProductID,:CustomerID,:PartNo,:DescribInfo);",
						wInstance.Result);
				wAPSBomBPM.Code = GetNewCode(wLoginUser, wErrorCode);
			} else {
				wSQL = MessageFormat.format(
						"UPDATE {0}.aps_bombpm SET Code = :Code,FlowType = :FlowType,FlowID = :FlowID,UpFlowID = :UpFlowID,FollowerID = :FollowerID,Status = :Status,StatusText = :StatusText,CreateTime = :CreateTime,SubmitTime = now(),Type = :Type,OrderID = :OrderID,LineID = :LineID,ProductID = :ProductID,CustomerID = :CustomerID,PartNo = :PartNo,DescribInfo=:DescribInfo WHERE ID = :ID;",
						wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wAPSBomBPM.ID);
			wParamMap.put("Code", wAPSBomBPM.Code);
			wParamMap.put("FlowType", wAPSBomBPM.FlowType);
			wParamMap.put("FlowID", wAPSBomBPM.FlowID);
			wParamMap.put("UpFlowID", wAPSBomBPM.UpFlowID);
			wParamMap.put("FollowerID", StringUtils.Join(",", wAPSBomBPM.FollowerID));
			wParamMap.put("Status", wAPSBomBPM.Status);
			wParamMap.put("StatusText", wAPSBomBPM.StatusText);
			wParamMap.put("CreateTime", wAPSBomBPM.CreateTime);
			wParamMap.put("SubmitTime", wAPSBomBPM.SubmitTime);
			wParamMap.put("Type", wAPSBomBPM.Type);
			wParamMap.put("OrderID", wAPSBomBPM.OrderID);
			wParamMap.put("LineID", wAPSBomBPM.LineID);
			wParamMap.put("ProductID", wAPSBomBPM.ProductID);
			wParamMap.put("CustomerID", wAPSBomBPM.CustomerID);
			wParamMap.put("PartNo", wAPSBomBPM.PartNo);
			wParamMap.put("DescribInfo", wAPSBomBPM.DescribInfo);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wAPSBomBPM.getID() <= 0) {
				wResult.ID = keyHolder.getKey().intValue();
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 删除集合
	 * 
	 * @param wList
	 */
	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<APSBomBPM> wList,
			OutResult<Integer> wErrorCode) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wList == null || wList.size() <= 0)
				return wResult;

			List<String> wIDList = new ArrayList<String>();
			for (APSBomBPM wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = MessageFormat.format("delete from {1}.aps_bombpm WHERE ID IN({0}) ;",
					String.join(",", wIDList), wInstance.Result);
			this.ExecuteSqlTransaction(wSql);
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 查单条
	 * 
	 * @return
	 */
	public APSBomBPM SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		APSBomBPM wResult = new APSBomBPM();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<APSBomBPM> wList = this.SelectList(wLoginUser, wID, "", -1, -1, -1, -1, -1, -1, null, null, null,
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
	 * 条件查询集合
	 * 
	 * @return
	 */
	public List<APSBomBPM> SelectList(BMSEmployee wLoginUser, int wID, String wCode, int wUpFlowID, int wType,
			int wOrderID, int wLineID, int wProductID, int wCustomerID, Calendar wStartTime, Calendar wEndTime,
			List<Integer> wStateIDList, OutResult<Integer> wErrorCode) {
		List<APSBomBPM> wResultList = new ArrayList<APSBomBPM>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
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

			if (wStateIDList == null) {
				wStateIDList = new ArrayList<Integer>();
			}

			String wSQL = MessageFormat.format("SELECT * FROM {0}.aps_bombpm WHERE  1=1  "
					+ "and ( :wID <= 0 or :wID = ID ) " + "and ( :wCode is null or :wCode = '''' or :wCode = Code ) "
					+ "and ( :wUpFlowID <= 0 or :wUpFlowID = UpFlowID ) "
					+ "and ( :wStartTime <=str_to_date(''2010-01-01'', ''%Y-%m-%d'')  or :wStartTime <=  SubmitTime ) "
					+ "and ( :wEndTime <=str_to_date(''2010-01-01'', ''%Y-%m-%d'')  or :wEndTime >=  CreateTime ) "
					+ "and ( :wType <= 0 or :wType = Type ) and ( :wOrderID <= 0 or :wOrderID = OrderID ) "
					+ "and ( :wStatus is null or :wStatus = '''' or Status in ({1})) "
					+ "and ( :wLineID <= 0 or :wLineID = LineID ) "
					+ "and ( :wProductID <= 0 or :wProductID = ProductID ) "
					+ "and ( :wCustomerID <= 0 or :wCustomerID = CustomerID );", wInstance.Result,
					wStateIDList.size() > 0 ? StringUtils.Join(",", wStateIDList) : "0");

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wCode", wCode);
			wParamMap.put("wUpFlowID", wUpFlowID);
			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);
			wParamMap.put("wType", wType);
			wParamMap.put("wOrderID", wOrderID);
			wParamMap.put("wLineID", wLineID);
			wParamMap.put("wProductID", wProductID);
			wParamMap.put("wCustomerID", wCustomerID);
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

	private void SetValue(List<APSBomBPM> wResultList, List<Map<String, Object>> wQueryResult) {
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			for (Map<String, Object> wReader : wQueryResult) {
				APSBomBPM wItem = new APSBomBPM();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.Code = StringUtils.parseString(wReader.get("Code"));
				wItem.FlowType = StringUtils.parseInt(wReader.get("FlowType"));
				wItem.FlowID = StringUtils.parseInt(wReader.get("FlowID"));
				wItem.UpFlowID = StringUtils.parseInt(wReader.get("UpFlowID"));
				wItem.FollowerID = StringUtils
						.parseIntList(StringUtils.parseString(wReader.get("FollowerID")).split(","));
				wItem.Status = StringUtils.parseInt(wReader.get("Status"));
				wItem.StatusText = StringUtils.parseString(wReader.get("StatusText"));
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				wItem.SubmitTime = StringUtils.parseCalendar(wReader.get("SubmitTime"));
				wItem.Type = StringUtils.parseInt(wReader.get("Type"));
				wItem.OrderID = StringUtils.parseInt(wReader.get("OrderID"));
				wItem.LineID = StringUtils.parseInt(wReader.get("LineID"));
				wItem.ProductID = StringUtils.parseInt(wReader.get("ProductID"));
				wItem.CustomerID = StringUtils.parseInt(wReader.get("CustomerID"));
				wItem.PartNo = StringUtils.parseString(wReader.get("PartNo"));
				wItem.DescribInfo = StringUtils.parseString(wReader.get("DescribInfo"));

				wItem.UpFlowName = APSConstans.GetBMSEmployeeName(wItem.UpFlowID);
				wItem.LineName = APSConstans.GetFMCLineName(wItem.LineID);
				wItem.ProductNo = APSConstans.GetFPCProductName(wItem.ProductID);
				wItem.CustomerName = APSConstans.GetCRMCustomerName(wItem.CustomerID);
				wItem.CustomerCode = APSConstans.GetCRMCustomer(wItem.CustomerID).CustomerCode;
				wItem.WBSNo = OMSOrderDAO.getInstance().SelectByID(BaseDAO.SysAdmin, wItem.OrderID, wErrorCode).OrderNo;

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	private APSBomBPMDAO() {
		super();
	}

	public static APSBomBPMDAO getInstance() {
		if (Instance == null)
			Instance = new APSBomBPMDAO();
		return Instance;
	}

	public List<APSBomBPM> SelectList(BMSEmployee wLoginUser, List<Integer> wTaskIDList, Calendar wStartTime,
			Calendar wEndTime, OutResult<Integer> wErrorCode) {
		List<APSBomBPM> wResultList = new ArrayList<APSBomBPM>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.Basic, 0);
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

			String wSQL = StringUtils.Format("SELECT * FROM {0}.aps_bombpm WHERE  1=1  "
					+ "and ( :wStartTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wStartTime <= SubmitTime) "
					+ "and ( :wEndTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wEndTime >= CreateTime) "
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
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.Basic, 0);
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
					"select count(*)+1 as Number from {0}.aps_bombpm where CreateTime > :wSTime and CreateTime < :wETime;",
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

			wResult = StringUtils.Format("BOM{0}{1}{2}", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)),
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
		List<APSBomBPM> wResult = new ArrayList<APSBomBPM>();
		try {
			// 获取所有任务消息
			List<BFCMessage> wMessageList = CoreServiceImpl.getInstance()
					.BFC_GetMessageList(wLoginUser, wLoginUser.getID(), BPMEventModule.APMBomAudit.getValue(), -1,
							BFCMessageType.Task.getValue(), 0, -1, null, null)
					.List(BFCMessage.class);
			wMessageList.addAll(CoreServiceImpl.getInstance().BFC_GetMessageList(wLoginUser, wLoginUser.getID(),
					BPMEventModule.APMBomAudit.getValue(), -1, BFCMessageType.Task.getValue(), 1, -1, null, null)
					.List(BFCMessage.class));
			wMessageList.addAll(CoreServiceImpl.getInstance().BFC_GetMessageList(wLoginUser, wLoginUser.getID(),
					BPMEventModule.APMBomAudit.getValue(), -1, BFCMessageType.Task.getValue(), 2, -1, null, null)
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
			Map<Integer, APSBomBPM> wTaskMap = new HashMap<Integer, APSBomBPM>();
			if (wTaskIDList != null && wTaskIDList.size() > 0) {
				List<APSBomBPM> wMTCTaskListTemp = this.SelectList(wLoginUser, wTaskIDList, null, null, wErrorCode);

				wTaskMap = wMTCTaskListTemp.stream().collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));

			}
			APSBomBPM wTaskTemp = null;
			for (BFCMessage wBFCMessage : wMessageList) {
				if (!wTaskMap.containsKey((int) wBFCMessage.getMessageID()))
					continue;

				wTaskTemp = CloneTool.Clone(wTaskMap.get((int) wBFCMessage.getMessageID()), APSBomBPM.class);
				wTaskTemp.StepID = wBFCMessage.getStepID();
				wResult.add(wTaskTemp);
			}

			wResult.sort(Comparator.comparing(APSBomBPM::getSubmitTime).reversed());
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
		List<APSBomBPM> wResult = new ArrayList<APSBomBPM>();
		wErrorCode.set(0);
		try {
			List<APSBomBPM> wTaskList = new ArrayList<APSBomBPM>();
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
							.BFC_GetMessageList(wLoginUser, wLoginUser.getID(), BPMEventModule.APMBomAudit.getValue(),
									-1, BFCMessageType.Task.getValue(), 3, -1, wStartTime, wEndTime)
							.List(BFCMessage.class));

			wMessageList
					.addAll(CoreServiceImpl.getInstance()
							.BFC_GetMessageList(wLoginUser, wLoginUser.getID(), BPMEventModule.APMBomAudit.getValue(),
									-1, BFCMessageType.Task.getValue(), 4, -1, wStartTime, wEndTime)
							.List(BFCMessage.class));

			List<Integer> wTaskIDList = wMessageList.stream().map(p -> (int) p.MessageID).distinct()
					.collect(Collectors.toList());

			wTaskList = this.SelectList(wLoginUser, wTaskIDList, wStartTime, wEndTime, wErrorCode);

			wTaskList.sort(Comparator.comparing(APSBomBPM::getSubmitTime).reversed());

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
		List<APSBomBPM> wResult = new ArrayList<>();
		try {
			wResult = this.SelectList(wLoginUser, -1, "", wResponsorID, -1, -1, -1, -1, -1, wStartTime, wEndTime, null,
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
			wResult = this.Update(wLoginUser, (APSBomBPM) wTask, wErrorCode);

			// 更新子项
			for (APSBomBPMItem wAPSBomBPMItem : ((APSBomBPM) wTask).APSBomBPMItemList) {
				wAPSBomBPMItem.APSBomBPMID = wResult.ID;
				APSBomBPMItemDAO.getInstance().Update(wLoginUser, wAPSBomBPMItem, wErrorCode);
			}
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
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.Basic, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<APSBomBPM> wList = this.SelectList(wLoginUser, wTaskID, wCode, -1, -1, -1, -1, -1, -1, null, null,
					null, wErrorCode);
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
	 * 删除状态为0的单据
	 */

	public void DeleteNoUseTask(BMSEmployee wLoginUser, OutResult<Integer> wErrorCode) {
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return;
			}

			String wSql = MessageFormat.format("delete from {0}.aps_bombpm WHERE Status=0 and ID>0;", wInstance.Result);
			this.ExecuteSqlTransaction(wSql);
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
	}

	public List<Integer> GetUserByParts(BMSEmployee wLoginUser, String wPartIDs, OutResult<Integer> wErrorCode) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils
					.Format("SELECT t.ID FROM {0}.mbs_user t,{0}.bms_position t1 where t.DepartmentID in "
							+ "(SELECT ClassID FROM {0}.bms_workcharge where StationID in ({1})) "
							+ "and t.Position=t1.ID and t1.DutyID=1 ;", wInstance.Result, wPartIDs);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wID = StringUtils.parseInt(wReader.get("ID"));
				wResult.add(wID);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	public List<BFCMessage> SelectNoticeMessage(BMSEmployee wLoginUser, int iD, OutResult<Integer> wErrorCode) {
		List<BFCMessage> wResult = new ArrayList<BFCMessage>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"SELECT * FROM {0}.bfc_message where MessageID=:MessageID and ModuleID=8500 and type=1;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("MessageID", iD);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				BFCMessage wItem = new BFCMessage();

				wItem.ResponsorID = StringUtils.parseInt(wReader.get("ResponsorID"));
				wItem.Active = StringUtils.parseInt(wReader.get("Active"));
				wItem.EditTime = StringUtils.parseCalendar(wReader.get("EditTime"));

				wResult.add(wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}
}
