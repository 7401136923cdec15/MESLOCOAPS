package com.mes.loco.aps.server.serviceimpl.dao.sfc;

import com.mes.loco.aps.server.service.mesenum.MESDBSource;
import com.mes.loco.aps.server.service.mesenum.MESException;
import com.mes.loco.aps.server.service.mesenum.SFCTaskStepType;
import com.mes.loco.aps.server.service.po.OutResult;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.aps.APSTaskStep;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.fmc.FMCWorkCharge;
import com.mes.loco.aps.server.service.po.fpc.FPCPart;
import com.mes.loco.aps.server.service.po.ipt.IPTPreCheckProblem;
import com.mes.loco.aps.server.service.po.oms.OMSOrder;
import com.mes.loco.aps.server.service.po.sch.SCHSecondmentApply;
import com.mes.loco.aps.server.service.po.sfc.SFCPartPerson;
import com.mes.loco.aps.server.service.po.sfc.SFCTaskIPT;
import com.mes.loco.aps.server.service.po.sfc.SFCTaskStep;
import com.mes.loco.aps.server.service.po.sfc.SFCTaskStepCar;
import com.mes.loco.aps.server.service.po.sfc.SFCTaskStepPart;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.QMSServiceImpl;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;
import com.mes.loco.aps.server.serviceimpl.dao.aps.APSTaskStepDAO;
import com.mes.loco.aps.server.serviceimpl.dao.oms.OMSOrderDAO;
import com.mes.loco.aps.server.serviceimpl.dao.sch.SCHSecondmentApplyDAO;
import com.mes.loco.aps.server.serviceimpl.dao.sfc.SFCTaskStepDAO;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSConstans;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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

public class SFCTaskStepDAO extends BaseDAO {
	private static Logger logger = LoggerFactory.getLogger(SFCTaskStepDAO.class);

	private static SFCTaskStepDAO Instance = null;

	public int Update(BMSEmployee wLoginUser, SFCTaskStep wSFCTaskStep, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result) != 0) {
				return wResult;
			}

			if (wSFCTaskStep == null) {
				return 0;
			}
			String wSQL = "";
			if (wSFCTaskStep.getID() <= 0) {
				wSQL = StringUtils.Format(
						"INSERT INTO {0}.sfc_taskstep(TaskStepID,ShiftID,MonitorID,WorkHour,OperatorID,"
								+ "CreateTime,ReadyTime,IsStartWork,Type,EditTime,RealHour,Active) "
								+ "VALUES(:TaskStepID,:ShiftID,:MonitorID,:WorkHour,:OperatorID,"
								+ ":CreateTime,:ReadyTime,:IsStartWork,:Type,:EditTime,:RealHour,:Active);",
						new Object[] { wInstance.Result });
			} else {
				wSQL = StringUtils.Format(
						"UPDATE {0}.sfc_taskstep SET TaskStepID = :TaskStepID,ShiftID = :ShiftID,"
								+ "MonitorID = :MonitorID,WorkHour = :WorkHour,OperatorID = :OperatorID,"
								+ "CreateTime = :CreateTime,ReadyTime = :ReadyTime,IsStartWork=:IsStartWork,"
								+ "Type=:Type,EditTime=:EditTime,RealHour=:RealHour,Active=:Active WHERE ID = :ID;",
						new Object[] { wInstance.Result });
			}
			wSQL = DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("ID", Integer.valueOf(wSFCTaskStep.ID));
			wParamMap.put("TaskStepID", Integer.valueOf(wSFCTaskStep.TaskStepID));
			wParamMap.put("ShiftID", Integer.valueOf(wSFCTaskStep.ShiftID));
			wParamMap.put("WorkHour", Double.valueOf(wSFCTaskStep.WorkHour));
			wParamMap.put("MonitorID", Integer.valueOf(wSFCTaskStep.MonitorID));
			wParamMap.put("OperatorID", Integer.valueOf(wSFCTaskStep.OperatorID));
			wParamMap.put("CreateTime", wSFCTaskStep.CreateTime);
			wParamMap.put("ReadyTime", wSFCTaskStep.ReadyTime);
			wParamMap.put("IsStartWork", Integer.valueOf(wSFCTaskStep.IsStartWork));
			wParamMap.put("Type", Integer.valueOf(wSFCTaskStep.Type));
			wParamMap.put("EditTime", wSFCTaskStep.EditTime);
			wParamMap.put("RealHour", wSFCTaskStep.RealHour);
			wParamMap.put("Active", wSFCTaskStep.Active);

			GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
			MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource(wParamMap);

			this.nameJdbcTemplate.update(wSQL, (SqlParameterSource) mapSqlParameterSource,
					(KeyHolder) generatedKeyHolder);

			if (wSFCTaskStep.getID() <= 0) {
				wResult = generatedKeyHolder.getKey().intValue();
				wSFCTaskStep.setID(wResult);
			} else {
				wResult = wSFCTaskStep.getID();
			}
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResult;
	}

	public SFCTaskStep SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		SFCTaskStep wResult = new SFCTaskStep();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result) != 0) {
				return wResult;
			}

			List<SFCTaskStep> wList = SelectList(wLoginUser, wID, -1, -1, -1, -1, null, -1, wErrorCode);
			if (wList == null || wList.size() != 1)
				return wResult;
			wResult = wList.get(0);
		} catch (Exception e) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<SFCTaskStep> wList,
			OutResult<Integer> wErrorCode) {
		ServiceResult<Integer> wResult = new ServiceResult<>(Integer.valueOf(0));
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result) != 0) {
				return wResult;
			}

			if (wList == null || wList.size() <= 0) {
				return wResult;
			}
			List<String> wIDList = new ArrayList<>();
			for (SFCTaskStep wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = StringUtils.Format("delete from {1}.sfc_taskstep WHERE ID IN({0}) ;",
					String.join(",", wIDList), wInstance.Result);
			ExecuteSqlTransaction(wSql);
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResult;
	}

	public List<SFCTaskStep> SelectList(BMSEmployee wLoginUser, int wID, int wTaskStepID, int wShiftID, int wOperatorID,
			int wMonitorID, List<Integer> wStatusList, int wType, OutResult<Integer> wErrorCode) {
		List<SFCTaskStep> wResultList = new ArrayList<>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result) != 0) {
				return wResultList;
			}

			if (wStatusList == null) {
				wStatusList = new ArrayList<>();
			}

			String wSQL = StringUtils.Format("SELECT * FROM {0}.sfc_taskstep WHERE 1=1 "
					+ "and ( :wID <= 0 or :wID = ID ) " + "and ( :wTaskStepID <= 0 or :wTaskStepID = TaskStepID ) "
					+ "and ( :wOperatorID <= 0 or :wOperatorID = OperatorID ) "
					+ "and ( :wType <= 0 or :wType = Type ) " + "and ( :wMonitorID <= 0 or :wMonitorID = MonitorID ) "
					+ "and ( :wShiftID <= 0 or :wShiftID = ShiftID );", new Object[] { wInstance.Result });
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wID", Integer.valueOf(wID));
			wParamMap.put("wTaskStepID", Integer.valueOf(wTaskStepID));
			wParamMap.put("wOperatorID", Integer.valueOf(wOperatorID));
			wParamMap.put("wMonitorID", Integer.valueOf(wMonitorID));
			wParamMap.put("wShiftID", Integer.valueOf(wShiftID));
			wParamMap.put("wType", Integer.valueOf(wType));

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetValue(wLoginUser, wErrorCode, wResultList, wQueryResult);

			if (wStatusList != null && wStatusList.size() > 0 && wResultList != null && wResultList.size() > 0) {
				List<Integer> wStateIDList = wStatusList;

				wResultList.removeIf(p -> p.Type == SFCTaskStepType.Step.getValue()
						&& !wStateIDList.stream().anyMatch(q -> q == p.Status));
			}
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResultList;
	}

	/**
	 * 通过时间查询未完成的和时间段内完成的任务
	 * 
	 * @return
	 */
	public List<SFCTaskStep> SelectListByTime(BMSEmployee wLoginUser, int wOperatorID, Calendar wStartTime,
			Calendar wEndTime, OutResult<Integer> wErrorCode) {
		List<SFCTaskStep> wResultList = new ArrayList<>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result) != 0) {
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

			String wSQL = StringUtils.Format(
					"select t1.* from {0}.sfc_taskstep t1 left join {0}.aps_taskstep t2 on t1.TaskStepID=t2.ID where 1=1 "
							+ "and ( :wOperatorID <= 0 or :wOperatorID = OperatorID ) "
							+ "and ((:wStartTime <=  t1.EditTime and  :wEndTime >=  t1.CreateTime and t2.Status in (5)) or (t2.Status in (2,3,4)));",
					new Object[] { wInstance.Result });
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wOperatorID", wOperatorID);
			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetValue(wLoginUser, wErrorCode, wResultList, wQueryResult);
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResultList;
	}

	public List<SFCTaskStep> SelectListByTaskStepIDList(BMSEmployee wLoginUser, List<Integer> wTaskStepIDList,
			int wType, OutResult<Integer> wErrorCode) {
		List<SFCTaskStep> wResultList = new ArrayList<>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result) != 0) {
				return wResultList;
			}

			if (wTaskStepIDList == null) {
				wTaskStepIDList = new ArrayList<>();
			}

			String wSQL = StringUtils.Format(
					"SELECT * FROM {0}.sfc_taskstep WHERE 1=1 " + "and ( :wType <= 0 or :wType = Type ) "
							+ "and ( :wTaskStepID is null or :wTaskStepID = '''' or TaskStepID in ({1}));",
					new Object[] { wInstance.Result,
							(wTaskStepIDList.size() > 0) ? StringUtils.Join(",", wTaskStepIDList) : "0" });
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wType", Integer.valueOf(wType));
			wParamMap.put("wTaskStepID", StringUtils.Join(",", wTaskStepIDList));

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetValue(wLoginUser, wErrorCode, wResultList, wQueryResult);
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResultList;
	}

	private void SetValue(BMSEmployee wLoginUser, OutResult<Integer> wErrorCode, List<SFCTaskStep> wResultList,
			List<Map<String, Object>> wQueryResult) {
		try {

			Map<Integer, OMSOrder> wOrderMap = new HashMap<Integer, OMSOrder>();
			if (wQueryResult.size() > 0) {
				List<Integer> wOrderIDList = wQueryResult.stream().filter(p -> p.containsKey("OrderID"))
						.map(p -> StringUtils.parseInt(p.get("OrderID"))).distinct().collect(Collectors.toList());

				if (wOrderIDList.size() > 0) {
					wOrderMap = OMSOrderDAO.getInstance().SelectListByIDList(wLoginUser, wOrderIDList, wErrorCode)
							.stream().collect(Collectors.toMap(p -> p.ID, p -> p));
				}

			}
			OMSOrder wOMSOrder = null;

			for (Map<String, Object> wReader : wQueryResult) {
				IPTPreCheckProblem wIPTPreCheckProblem;
				APSTaskStep wAPSTaskStep;
				SFCTaskStep wItem = new SFCTaskStep();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.TaskStepID = StringUtils.parseInt(wReader.get("TaskStepID"));
				wItem.ShiftID = StringUtils.parseInt(wReader.get("ShiftID"));
				wItem.WorkHour = StringUtils.parseDouble(wReader.get("WorkHour"));
				wItem.MonitorID = StringUtils.parseInt(wReader.get("MonitorID"));
				wItem.OperatorID = StringUtils.parseInt(wReader.get("OperatorID"));
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				wItem.EditTime = StringUtils.parseCalendar(wReader.get("EditTime"));
				wItem.ReadyTime = StringUtils.parseCalendar(wReader.get("ReadyTime"));
				wItem.IsStartWork = StringUtils.parseInt(wReader.get("IsStartWork"));
				wItem.Type = StringUtils.parseInt(wReader.get("Type"));
				wItem.Active = StringUtils.parseInt(wReader.get("Active"));
				wItem.RealHour = StringUtils.parseDouble(wReader.get("RealHour"));

				switch (SFCTaskStepType.getEnumType(wItem.Type)) {
				case Step:
				case Quality:
					wAPSTaskStep = APSTaskStepDAO.getInstance().SelectByID(wLoginUser, wItem.TaskStepID, wErrorCode);
					if (wAPSTaskStep != null && wAPSTaskStep.ID > 0) {
						wItem.OrderID = wAPSTaskStep.OrderID;
						wItem.PartNo = wAPSTaskStep.PartNo;
						wItem.TaskLineID = wAPSTaskStep.TaskLineID;
						wItem.TaskLineID = wAPSTaskStep.TaskLineID;
						wItem.TaskPartID = wAPSTaskStep.TaskPartID;
						wItem.WorkShopID = wAPSTaskStep.WorkShopID;
						wItem.LineID = wAPSTaskStep.LineID;
						wItem.PartID = wAPSTaskStep.PartID;
						wItem.StepID = wAPSTaskStep.StepID;
						wItem.ProductID = APSConstans.GetFPCProducID(wAPSTaskStep.ProductNo);
						wItem.ProductNo = wAPSTaskStep.ProductNo;
						wItem.StartTime = wAPSTaskStep.StartTime;
						wItem.EndTime = wAPSTaskStep.EndTime;
						wItem.Status = wAPSTaskStep.Status;
						wItem.OrderNo = wAPSTaskStep.OrderNo;
						wItem.PlanerID = wAPSTaskStep.PlanerID;
						wItem.TaskText = wAPSTaskStep.TaskText;
					}
					break;
				case Question:
					wIPTPreCheckProblem = (IPTPreCheckProblem) QMSServiceImpl.getInstance()
							.IPT_QueryPreCheckProblemByID(wLoginUser, wItem.TaskStepID).Info(IPTPreCheckProblem.class);
					if (wIPTPreCheckProblem != null && wIPTPreCheckProblem.ID > 0) {
						wItem.OrderID = wIPTPreCheckProblem.OrderID;
						wItem.PartNo = wIPTPreCheckProblem.CarNumber;
						wItem.TaskLineID = 0;
						wItem.TaskPartID = 0;
						wItem.WorkShopID = 0;
						wItem.LineID = wIPTPreCheckProblem.LineID;
						wItem.PartID = wIPTPreCheckProblem.DoStationID;
						wItem.StepID = 0;
						wItem.ProductID = wIPTPreCheckProblem.ProductID;
						wItem.ProductNo = wIPTPreCheckProblem.ProductNo;
						wItem.Status = wIPTPreCheckProblem.Status;
						wItem.OrderNo = wIPTPreCheckProblem.OrderNo;
						wItem.PlanerID = 0;
						wItem.TaskText = "";
					}
					break;
				default:
					break;
				}

				wItem.Monitor = APSConstans.GetBMSEmployeeName(wItem.MonitorID);
				wItem.Operator = APSConstans.GetBMSEmployeeName(wItem.OperatorID);
				wItem.PlanerName = APSConstans.GetBMSEmployeeName(wItem.PlanerID);

				wItem.PartName = APSConstans.GetFPCPartName(wItem.PartID);
				wItem.MaterialNo = "";
				wItem.MaterialName = "";
				wItem.PartPointName = APSConstans.GetFPCPartPointName(wItem.StepID);
				wItem.LineName = APSConstans.GetFMCLineName(wItem.LineID);

				wOMSOrder = wOrderMap.containsKey(wItem.OrderID) ? wOrderMap.get(wItem.OrderID) : new OMSOrder();

				wItem.CustomerID = wOMSOrder.CustomerID;
				wItem.CustomerName = wOMSOrder.Customer;

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	public static SFCTaskStepDAO getInstance() {
		if (Instance == null)
			Instance = new SFCTaskStepDAO();
		return Instance;
	}

	public List<SFCTaskStep> SelectListByType(BMSEmployee wLoginUser, int wType, int wTaskID,
			OutResult<Integer> wErrorCode) {
		List<SFCTaskStep> wResultList = new ArrayList<>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result) != 0) {
				return wResultList;
			}

			String wSQL = StringUtils.Format("SELECT * FROM {0}.sfc_taskstep WHERE 1=1 "
					+ "and ( :wType <= 0 or :wType = Type ) " + "and ( :wTaskID <= 0 or :wTaskID = TaskStepID );",
					new Object[] { wInstance.Result });
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wType", Integer.valueOf(wType));
			wParamMap.put("wTaskID", Integer.valueOf(wTaskID));

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				SFCTaskStep wItem = new SFCTaskStep();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.TaskStepID = StringUtils.parseInt(wReader.get("TaskStepID"));
				wItem.ShiftID = StringUtils.parseInt(wReader.get("ShiftID"));
				wItem.WorkHour = StringUtils.parseDouble(wReader.get("WorkHour"));
				wItem.MonitorID = StringUtils.parseInt(wReader.get("MonitorID"));
				wItem.OperatorID = StringUtils.parseInt(wReader.get("OperatorID"));
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				wItem.ReadyTime = StringUtils.parseCalendar(wReader.get("ReadyTime"));
				wItem.IsStartWork = StringUtils.parseInt(wReader.get("IsStartWork"));
				wItem.Type = StringUtils.parseInt(wReader.get("Type"));
				wItem.Active = StringUtils.parseInt(wReader.get("Active"));
				wItem.RealHour = StringUtils.parseDouble(wReader.get("RealHour"));

				wItem.Monitor = APSConstans.GetBMSEmployeeName(wItem.MonitorID);
				wItem.Operator = APSConstans.GetBMSEmployeeName(wItem.OperatorID);

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResultList;
	}

	/**
	 * 判断某人是否派工了某工序
	 */
	public synchronized boolean JudgeIsDispatched(BMSEmployee wLoginUser, int wAPSTaskStepID, int wOperateID,
			OutResult<Integer> wErrorCode) {
		boolean wResult = false;
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result) != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"SELECT count(*) as Number FROM {0}.sfc_taskstep "
							+ "where TaskStepID=:TaskStepID and OperatorID=:OperatorID;",
					new Object[] { wInstance.Result });
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("TaskStepID", wAPSTaskStepID);
			wParamMap.put("OperatorID", wOperateID);

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wNumber = StringUtils.parseInt(wReader.get("Number"));
				if (wNumber > 0) {
					wResult = true;
				} else {
					wResult = false;
				}
				return wResult;
			}
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 判断某人是否派工了某工序
	 */
	public List<SFCTaskStep> QueryTaskStepListByOrder(BMSEmployee wLoginUser, int wOrderID, int wPartID,
			OutResult<Integer> wErrorCode) {
		List<SFCTaskStep> wResult = new ArrayList<SFCTaskStep>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result) != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"SELECT t1.TaskStepID,t1.OperatorID FROM {0}.sfc_taskstep t1,{0}.aps_TaskStep t2 "
							+ "where t1.TaskStepID=t2.ID and t2.OrderID=:OrderID and t2.PartID=:PartID;",
					new Object[] { wInstance.Result });
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("OrderID", wOrderID);
			wParamMap.put("PartID", wPartID);

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SFCTaskStep wSFCTaskStep = null;
			for (Map<String, Object> wReader : wQueryResult) {
				wSFCTaskStep = new SFCTaskStep();
				wSFCTaskStep.TaskStepID = StringUtils.parseInt(wReader.get("TaskStepID"));
				wSFCTaskStep.OperatorID = StringUtils.parseInt(wReader.get("OperatorID"));
				wResult.add(wSFCTaskStep);
			}
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 删除重复的派工记录
	 */
	public Integer DeleteRepeat(BMSEmployee wLoginUser, OutResult<Integer> wErrorCode) {
		Integer wResult = 0;
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result) != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"SELECT t1.ID,t1.TaskStepID,t1.OperatorID FROM {0}.sfc_taskstep t1,"
							+ "{0}.sfc_taskstep t2  where t1.TaskStepID=t2.TaskStepID "
							+ "and t1.OperatorID=t2.OperatorID and t1.ID!=t2.ID group by t1.ID; ",
					new Object[] { wInstance.Result });
			Map<String, Object> wParamMap = new HashMap<>();

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			List<SFCTaskStep> wReList = new ArrayList<SFCTaskStep>();
			for (Map<String, Object> wReader : wQueryResult) {
				int wID = StringUtils.parseInt(wReader.get("ID"));
				int wTaskStepID = StringUtils.parseInt(wReader.get("TaskStepID"));
				int wOperatorID = StringUtils.parseInt(wReader.get("OperatorID"));
				if (!wReList.stream().anyMatch(p -> p.TaskStepID == wTaskStepID && p.OperatorID == wOperatorID)) {
					SFCTaskStep wSFCTaskStep = new SFCTaskStep();
					wSFCTaskStep.ID = wID;
					wSFCTaskStep.TaskStepID = wTaskStepID;
					wSFCTaskStep.OperatorID = wOperatorID;
					wReList.add(wSFCTaskStep);
				}
			}
			if (wReList.size() > 0) {
				DeleteList(wLoginUser, wReList, wErrorCode);
			}
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据工位获取人员列表
	 */
	public List<SFCPartPerson> SelectPersonIDListByPart(BMSEmployee wLoginUser, int wPartID,
			OutResult<Integer> wErrorCode) {
		List<SFCPartPerson> wResult = new ArrayList<SFCPartPerson>();
		try {
			// 获取班组工位信息
			List<FMCWorkCharge> wList = APSConstans.GetFMCWorkChargeList();
			wList = wList.stream().filter(p -> p.StationID == wPartID).collect(Collectors.toList());
			// 获取班组人员信息
			List<BMSEmployee> wEmployeeList = APSConstans.GetBMSEmployeeList().values().stream()
					.collect(Collectors.toList());
			// 获取工位人员
			List<FMCWorkCharge> wNewList = wList;
			List<BMSEmployee> wPList = wEmployeeList.stream()
					.filter(p -> wNewList.stream().anyMatch(q -> p.DepartmentID == q.ClassID))
					.collect(Collectors.toList());
			for (BMSEmployee wBMSEmployee : wPList) {
				SFCPartPerson wSFCPartPerson = new SFCPartPerson();
				wSFCPartPerson.PartID = wPartID;
				wSFCPartPerson.PersonID = wBMSEmployee.ID;
				wResult.add(wSFCPartPerson);
			}
			// 获取借调信息
			List<SCHSecondmentApply> wApplyList = SCHSecondmentApplyDAO.getInstance().SelectListByClass(wLoginUser, -1,
					wLoginUser.DepartmentID, new ArrayList<Integer>(Arrays.asList(20)), wErrorCode);
			wApplyList.stream().filter(p -> Calendar.getInstance().compareTo(p.StartTime) > 0
					&& Calendar.getInstance().compareTo(p.EndTime) < 0).collect(Collectors.toList());
			for (SCHSecondmentApply wSCHSecondmentApply : wApplyList) {
				if (!wResult.stream()
						.anyMatch(p -> wSCHSecondmentApply.PersonID.contains(String.valueOf(p.PersonID)))) {

					String[] wIDs = wSCHSecondmentApply.PersonID.split(",");
					for (String wID : wIDs) {
						Integer wUserID = StringUtils.parseInt(wID);

						SFCPartPerson wSFCPartPerson = new SFCPartPerson();
						wSFCPartPerson.PartID = wPartID;
						wSFCPartPerson.PersonID = wUserID;
						wResult.add(wSFCPartPerson);
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 删除派工消息
	 */
	public void DeleteMessage(BMSEmployee wAdminUser, List<SFCTaskStep> wSFCTaskStepList,
			OutResult<Integer> wErrorCode) {
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wAdminUser.getCompanyID(), MESDBSource.APS,
					wAdminUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return;
			}

			if (wSFCTaskStepList == null || wSFCTaskStepList.size() <= 0) {
				return;
			}

			String wSQL = StringUtils.Format(
					"delete FROM {0}.bfc_message " + "where ModuleID=8103 and MessageID in({1}) and ID>0;",
					wInstance.Result, StringUtils.Join(",",
							wSFCTaskStepList.stream().map(p -> p.ID).distinct().collect(Collectors.toList())));

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			nameJdbcTemplate.update(wSQL, wParamMap);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	public List<Integer> SelectReparingOrderIDList(BMSEmployee wLoginUser, OutResult<Integer> wErrorCode) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("SELECT distinct t1.OrderID FROM {0}.aps_taskpart t1,{0}.oms_order t2 "
					+ "where t1.OrderID=t2.ID and t2.Status=4 and t2.Active=1 and t1.Active=1 "
					+ "and t1.ShiftPeriod=5 and t1.Status !=5;", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wOrderID = StringUtils.parseInt(wReader.get("OrderID"));
				if (wOrderID > 0) {
					wResult.add(wOrderID);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 派工车辆统计信息
	 */
	public SFCTaskStepCar GetSFCTaskStepCar(BMSEmployee wLoginUser, OMSOrder wOMSOrder, OutResult<Integer> wErrorCode) {
		SFCTaskStepCar wResult = new SFCTaskStepCar();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"Select (select count(*) from {0}.aps_taskstep where OrderID=:OrderID and Active=1) StepSize,"
							+ "		(select count(*) from {0}.aps_taskstep where OrderID=:OrderID and Active=1 and IsDispatched=0) ToDispatch;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("OrderID", wOMSOrder.ID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wStepSize = StringUtils.parseInt(wReader.get("StepSize"));
				int wToDispatch = StringUtils.parseInt(wReader.get("ToDispatch"));

				wResult = new SFCTaskStepCar(wOMSOrder.ID, wOMSOrder, wOMSOrder.PartNo, wStepSize, wToDispatch,
						wStepSize - wToDispatch);
				return wResult;
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取工位ID集合
	 */
	public List<Integer> SelectPartIDList(BMSEmployee wLoginUser, int wOrderID, OutResult<Integer> wErrorCode) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"select distinct PartID from {0}.aps_taskstep where OrderID=:OrderID and Active=1;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("OrderID", wOrderID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wPartID = StringUtils.parseInt(wReader.get("PartID"));
				wResult.add(wPartID);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取派工工位统计数据
	 */
	public SFCTaskStepPart GetSFCTaskStepPart(BMSEmployee wLoginUser, FPCPart wFPCPart, int wOrderID, OMSOrder wOrder,
			OutResult<Integer> wErrorCode) {
		SFCTaskStepPart wResult = new SFCTaskStepPart();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"Select (select count(*) from {0}.aps_taskstep where OrderID=:OrderID and Active=1 and PartID=:PartID) StepSize,"
							+ "		(select count(*) from {0}.aps_taskstep where OrderID=:OrderID and Active=1 and IsDispatched=0 and PartID=:PartID) ToDispatch;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("OrderID", wOrderID);
			wParamMap.put("PartID", wFPCPart.ID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {

				int wStepSize = StringUtils.parseInt(wReader.get("StepSize"));
				int wToDispatch = StringUtils.parseInt(wReader.get("ToDispatch"));

				wResult = new SFCTaskStepPart(wOrderID, wOrder, wOrder.PartNo, wFPCPart.ID, wFPCPart.Name, wStepSize,
						wToDispatch, wStepSize - wToDispatch);
				return wResult;
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 查询检验单据状态
	 */
	public List<SFCTaskIPT> GetCheckTaskStatusList(BMSEmployee wLoginUser, List<Integer> wAPSTaskstepIDList,
			OutResult<Integer> wErrorCode) {
		List<SFCTaskIPT> wResult = new ArrayList<SFCTaskIPT>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"SELECT TaskStepID,TaskType,Status FROM {0}.sfc_taskipt "
							+ "where TaskStepID in ({1}) and TaskType in (6,12,13) and Active=1;",
					wInstance.Result, StringUtils.Join(",", wAPSTaskstepIDList));

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {

				SFCTaskIPT wItem = new SFCTaskIPT();

				wItem.TaskStepID = StringUtils.parseInt(wReader.get("TaskStepID"));
				wItem.TaskType = StringUtils.parseInt(wReader.get("TaskType"));
				wItem.Status = StringUtils.parseInt(wReader.get("Status"));

				wResult.add(wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	public void DisableDispatchMessage(BMSEmployee wLoginUser, int wMessageID, OutResult<Integer> wErrorCode) {
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return;
			}

			String wSQL = StringUtils.Format(
					"update {0}.bfc_message set Active=4 where MessageID=:MessageID and ModuleID=8103 and ID>0;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("MessageID", wMessageID);

			wSQL = this.DMLChange(wSQL);

			nameJdbcTemplate.update(wSQL, wParamMap);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 查询未完成的互检单ID
	 */

	public int QueryMutualTaskID(BMSEmployee wLoginUser, int wTaskStepID, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"select ID from {0}.sfc_taskipt where taskstepID=:taskstepID and TaskType=12 and Status=1 and Active=1;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("taskstepID", wTaskStepID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				wResult = StringUtils.parseInt(wReader.get("ID"));
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 查询互检待办消息数
	 */
	public int QueryMutualMessageCount(BMSEmployee wLoginUser, int wMutualTaskID, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"select count(*) Number from {0}.bfc_message where ModuleID=8112 and MessageID=:MessageID and Type=2;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("MessageID", wMutualTaskID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				wResult = StringUtils.parseInt(wReader.get("Number"));
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 查询自检单操作人
	 */
	public String QuerySelfOpes(BMSEmployee wLoginUser, int wTaskStepID, OutResult<Integer> wErrorCode) {
		String wResult = "";
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"select OperatorList from {0}.sfc_taskipt where taskstepID=:taskstepID and TaskType=6 and Active=1;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("taskstepID", wTaskStepID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				wResult = StringUtils.parseString(wReader.get("OperatorList"));
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 判断消息是否存在
	 */
	public boolean JudgeMessageIsSend(BMSEmployee wLoginUser, int operatorID, int wMutualTaskID, int wModuleID,
			OutResult<Integer> wErrorCode) {
		boolean wResult = false;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils
					.Format("SELECT count(*) Number FROM {0}.bfc_message where ResponsorID=:ResponsorID "
							+ "and Type=2 and ModuleID=:ModuleID and MessageID=:MessageID;", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ResponsorID", operatorID);
			wParamMap.put("ModuleID", wModuleID);
			wParamMap.put("MessageID", wMutualTaskID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wNumber = StringUtils.parseInt(wReader.get("Number"));
				if (wNumber > 0)
					return true;
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}
}

/*
 * Location: C:\Users\Shris\Desktop\新建文件夹
 * (5)\MESLOCOAPS.zip!\WEB-INF\classes\com\mes\loco\aps\server\serviceimpl\dao\
 * sfc\SFCTaskStepDAO.class Java compiler version: 8 (52.0) JD-Core Version:
 * 1.1.2
 */