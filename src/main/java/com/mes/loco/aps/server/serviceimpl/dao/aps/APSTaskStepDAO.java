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
import com.mes.loco.aps.server.service.mesenum.BPMEventModule;
import com.mes.loco.aps.server.service.mesenum.FMCShiftLevel;
import com.mes.loco.aps.server.service.mesenum.MESDBSource;
import com.mes.loco.aps.server.service.mesenum.MESException;
import com.mes.loco.aps.server.service.po.OutResult;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.aps.APSPartNoDetails;
import com.mes.loco.aps.server.service.po.aps.APSTaskRemark;
import com.mes.loco.aps.server.service.po.aps.APSTaskStep;
import com.mes.loco.aps.server.service.po.aps.APSTaskStepDetails;
import com.mes.loco.aps.server.service.po.aps.APSTaskStepInfo;
import com.mes.loco.aps.server.service.po.aps.APSWorkAreaDetails;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.oms.OMSOrder;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;
import com.mes.loco.aps.server.serviceimpl.dao.oms.OMSOrderDAO;
import com.mes.loco.aps.server.serviceimpl.utils.MESServer;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSConstans;

public class APSTaskStepDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(APSTaskStepDAO.class);

	private static APSTaskStepDAO Instance = null;

	private APSTaskStepDAO() {
		super();
	}

	public static APSTaskStepDAO getInstance() {
		if (Instance == null)
			Instance = new APSTaskStepDAO();
		return Instance;
	}

	/**
	 * 添加或修改
	 * 
	 * @param wAPSTaskStep
	 * @return
	 */
	public int Update(BMSEmployee wLoginUser, APSTaskStep wAPSTaskStep, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wAPSTaskStep == null)
				return 0;

			String wSQL = "";
			if (wAPSTaskStep.getID() <= 0) {
				wSQL = StringUtils.Format("INSERT INTO {0}.aps_taskstep(OrderID,PartNo,TaskLineID,"
						+ "TaskPartID,WorkShopID,LineID,PartID,StepID,ShiftID,CreateTime,"
						+ "StartTime,EndTime,ReadyTime,Status,Active,ProductNo,MaterialNo,"
						+ "PlannerID,TaskText,WorkHour,OperatorList,IsDispatched,RemarkList,Remark,RealHour,PlanStartTime,PlanEndTime) "
						+ "VALUES(:OrderID,:PartNo," + ":TaskLineID,:TaskPartID,:WorkShopID,:LineID,:PartID,"
						+ ":StepID,:ShiftID,now(),:StartTime,:EndTime,:ReadyTime,:Status,:Active,"
						+ ":ProductNo,:MaterialNo,:PlannerID,:TaskText,:WorkHour,:OperatorList,"
						+ ":IsDispatched,:RemarkList,:Remark,:RealHour,:PlanStartTime,:PlanEndTime);",
						wInstance.Result);
			} else {
				wSQL = StringUtils.Format("UPDATE {0}.aps_taskstep SET OrderID = :OrderID,"
						+ "PartNo = :PartNo,TaskLineID = :TaskLineID,"
						+ "TaskPartID = :TaskPartID,WorkShopID = :WorkShopID,"
						+ "LineID = :LineID,PartID = :PartID,StepID = :StepID,"
						+ "ShiftID = :ShiftID,StartTime = :StartTime,EndTime = :EndTime,ReadyTime=:ReadyTime,"
						+ "Status = :Status,Active = :Active,ProductNo = :ProductNo,"
						+ "MaterialNo = :MaterialNo,PlannerID = :PlannerID,TaskText = :TaskText,"
						+ "WorkHour = :WorkHour,OperatorList=:OperatorList,"
						+ "IsDispatched=:IsDispatched,RemarkList=:RemarkList,"
						+ "Remark=:Remark,RealHour=:RealHour,PlanStartTime=:PlanStartTime,PlanEndTime=:PlanEndTime WHERE ID = :ID;",
						wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wAPSTaskStep.ID);
			wParamMap.put("OrderID", wAPSTaskStep.OrderID);
			wParamMap.put("PartNo", wAPSTaskStep.PartNo);
			wParamMap.put("TaskLineID", wAPSTaskStep.TaskLineID);
			wParamMap.put("TaskPartID", wAPSTaskStep.TaskPartID);
			wParamMap.put("WorkShopID", wAPSTaskStep.WorkShopID);
			wParamMap.put("LineID", wAPSTaskStep.LineID);
			wParamMap.put("PartID", wAPSTaskStep.PartID);
			wParamMap.put("StepID", wAPSTaskStep.StepID);
			wParamMap.put("ShiftID", wAPSTaskStep.ShiftID);
			wParamMap.put("StartTime", wAPSTaskStep.StartTime);
			wParamMap.put("EndTime", wAPSTaskStep.EndTime);
			wParamMap.put("ReadyTime", wAPSTaskStep.ReadyTime);
			wParamMap.put("Status", wAPSTaskStep.Status);
			wParamMap.put("Active", wAPSTaskStep.Active);
			wParamMap.put("ProductNo", wAPSTaskStep.ProductNo);
			wParamMap.put("MaterialNo", wAPSTaskStep.MaterialNo);
			wParamMap.put("PlannerID", wAPSTaskStep.PlanerID);
			wParamMap.put("TaskText", wAPSTaskStep.TaskText);
			wParamMap.put("WorkHour", wAPSTaskStep.WorkHour);
			wParamMap.put("IsDispatched", wAPSTaskStep.IsDispatched ? 1 : 0);
			wParamMap.put("OperatorList", StringUtils.Join(",", wAPSTaskStep.OperatorList));
			wParamMap.put("RemarkList", APSTaskRemark.ListToString(wAPSTaskStep.RemarkList));
			wParamMap.put("Remark", wAPSTaskStep.Remark.toString());
			wParamMap.put("RealHour", wAPSTaskStep.RealHour);
			wParamMap.put("PlanStartTime", wAPSTaskStep.PlanStartTime);
			wParamMap.put("PlanEndTime", wAPSTaskStep.PlanEndTime);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wAPSTaskStep.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wAPSTaskStep.setID(wResult);
			} else {
				wResult = wAPSTaskStep.getID();
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
	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<APSTaskStep> wList,
			OutResult<Integer> wErrorCode) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wList == null || wList.size() <= 0)
				return wResult;

			List<String> wIDList = new ArrayList<String>();
			for (APSTaskStep wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = StringUtils.Format("delete from {1}.aps_taskstep WHERE ID IN({0}) ;",
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
	public APSTaskStep SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		APSTaskStep wResult = new APSTaskStep();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<APSTaskStep> wList = SelectList(wLoginUser, wID, -1, -1, -1, -1, -1, -1, -1, -1, -1, null, null, null,
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
	 * 条件查询集合
	 * 
	 * @return
	 */
	public List<APSTaskStep> SelectList(BMSEmployee wLoginUser, int wID, int wOrderID, int wTaskLineID, int wTaskPartID,
			int wWorkShopID, int wLineID, int wPartID, int wStepID, int wShiftID, int wActive,
			List<Integer> wStateIDList, Calendar wStartTime, Calendar wEndTime, List<Integer> wPartIDList,
			OutResult<Integer> wErrorCode) {
		List<APSTaskStep> wResultList = new ArrayList<APSTaskStep>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			if (wStateIDList == null)
				wStateIDList = new ArrayList<Integer>();

			if (wPartIDList == null) {
				wPartIDList = new ArrayList<Integer>();
			}

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1);
			if (wStartTime == null) {
				wStartTime = wBaseTime;
			}
			if (wEndTime == null) {
				wEndTime = wBaseTime;
			}

			String wSQL = StringUtils.Format("SELECT t1.*,t2.OrderNo "
					+ "FROM {0}.aps_taskstep t1,{0}.oms_order t2  WHERE t1.OrderID=t2.ID "
					+ "and ( :wID <= 0 or :wID = t1.ID ) " + "and ( :wOrderID <= 0 or :wOrderID = t1.OrderID ) "
					+ "and ( :wTaskLineID <= 0 or :wTaskLineID = t1.TaskLineID ) "
					+ "and ( :wTaskPartID <= 0 or :wTaskPartID = t1.TaskPartID ) "
					+ "and ( :wWorkShopID <= 0 or :wWorkShopID = t1.WorkShopID ) "
					+ "and ( :wLineID <= 0 or :wLineID = t1.LineID ) "
					+ "and ( :wPartID <= 0 or :wPartID = t1.PartID ) "
					+ "and ( :wStepID <= 0 or :wStepID = t1.StepID ) "
					+ "and ( :wShiftID <= 0 or :wShiftID = t1.ShiftID ) "
					+ "and ( :wActive <= 0 or :wActive = t1.Active ) "
					+ "and ( :wStartTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wStartTime <= t1.CreateTime) "
					+ "and ( :wEndTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wEndTime >= t1.CreateTime) "
					+ "and ( :wPart = '''' or t1.PartID in ({2})) "
					+ "and ( :wStatus is null or :wStatus = '''' or t1.Status in ({1}));", wInstance.Result,
					wStateIDList.size() > 0 ? StringUtils.Join(",", wStateIDList) : "0",
					wPartIDList.size() > 0 ? StringUtils.Join(",", wPartIDList) : "0");

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wOrderID", wOrderID);
			wParamMap.put("wTaskLineID", wTaskLineID);
			wParamMap.put("wTaskPartID", wTaskPartID);
			wParamMap.put("wWorkShopID", wWorkShopID);
			wParamMap.put("wLineID", wLineID);
			wParamMap.put("wPartID", wPartID);
			wParamMap.put("wStepID", wStepID);
			wParamMap.put("wShiftID", wShiftID);
			wParamMap.put("wActive", wActive);
			wParamMap.put("wStatus", StringUtils.Join(",", wStateIDList));
			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);
			wParamMap.put("wPart", StringUtils.Join(",", wPartIDList));

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
	 * 条件查询集合
	 */
	public List<APSTaskStep> SelectQTList(BMSEmployee wLoginUser, Calendar wStartTime, Calendar wEndTime,
			List<Integer> wStateIDList, List<Integer> wPartIDList, OutResult<Integer> wErrorCode) {
		List<APSTaskStep> wResultList = new ArrayList<APSTaskStep>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
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

			if (wStateIDList == null) {
				wStateIDList = new ArrayList<Integer>();
			}

			if (wPartIDList == null) {
				wPartIDList = new ArrayList<Integer>();
			}

			String wSQL = StringUtils.Format("SELECT t1.*,t2.OrderNo "
					+ "FROM {0}.aps_taskstep t1,{0}.oms_order t2  WHERE t1.OrderID=t2.ID "
					+ "and ( :wStartTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wStartTime <= t1.EndTime) "
					+ "and ( :wEndTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wEndTime >= t1.StartTime) "
					+ "and ( :wPart is null or :wPart = '''' or t1.PartID in ({2})) "
					+ "and ( :wStatus is null or :wStatus = '''' or t1.Status in ({1}));", wInstance.Result,
					wStateIDList.size() > 0 ? StringUtils.Join(",", wStateIDList) : "0",
					wPartIDList.size() > 0 ? StringUtils.Join(",", wPartIDList) : "0");

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wStatus", StringUtils.Join(",", wStateIDList));
			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);
			wParamMap.put("wPart", StringUtils.Join(",", wPartIDList));

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
	 * 条件查询集合(时间必给)
	 * 
	 * @return
	 */
	public List<APSTaskStep> SelectListByTime(BMSEmployee wLoginUser, Calendar wStartTime, Calendar wEndTime,
			OutResult<Integer> wErrorCode) {
		List<APSTaskStep> wResultList = new ArrayList<APSTaskStep>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
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

			String wSQL = StringUtils.Format("SELECT t1.*,t2.OrderNo FROM {0}.aps_taskstep t1,{0}.oms_order t2  "
					+ "WHERE t1.OrderID=t2.ID and ((:wStartTime <= t1.CreateTime and t1.CreateTime <= :wEndTime) "
					+ "or (:wStartTime <= t1.EndTime  and  :wEndTime >= t1.StartTime) );", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

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
	 * 条件查询审批过的任务
	 * 
	 * @param wLoginUser  登录信息
	 * @param wTodaySTime 开始时间
	 * @param wTodayETime 结束时间
	 * @param iD          人员ID
	 * @return 工序任务集合
	 */
	public List<APSTaskStep> SelectAuditList(BMSEmployee wLoginUser, Calendar wStartTime, Calendar wEndTime,
			int wAuditID, OutResult<Integer> wErrorCode) {
		List<APSTaskStep> wResultList = new ArrayList<APSTaskStep>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			ServiceResult<String> wInstance1 = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance1.ErrorCode);
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

			String wSQL = StringUtils.Format(
					"SELECT t1.*,t2.OrderNo FROM {0}.aps_taskstep t1,{0}.oms_order t2 , {2}.bfc_auditaction t3 "
							+ "where t1.OrderID=t2.ID and t3.TaskID=t1.ID and t3.EventModule={1} "
							+ "and :wStartTime<= t3.AuditorTime " + "and t3.AuditorTime<=:wEndTime "
							+ "and t3.AuditorID=:wAuditID;",
					wInstance.Result, BPMEventModule.SCDayAudit.getValue(), wInstance1.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);
			wParamMap.put("wAuditID", wAuditID);

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
	 * 检查当日是否可制定日计划
	 */
	public String CheckShiftDate(BMSEmployee wLoginUser, Calendar wShiftDate, OutResult<Integer> wErrorCode) {
		String wResult = "";
		try {
			if (wShiftDate == null) {
				wResult = "提示：参数错误!";
				return wResult;
			}

			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			int wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, wShiftDate, APSShiftPeriod.Day,
					FMCShiftLevel.Day, 0);

			int wTShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, Calendar.getInstance(), APSShiftPeriod.Day,
					FMCShiftLevel.Day, 0);

			if (wShiftID < wTShiftID) {
				wResult = "提示：生成日期有误";
				return wResult;
			}

			String wSQL = StringUtils.Format("SELECT COUNT(*) as Number FROM {0}.aps_taskstep where ShiftID > {1};",
					wInstance.Result, wShiftID);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			int wNumber = 0;
			for (Map<String, Object> wReader : wQueryResult) {
				wNumber = StringUtils.parseInt(wReader.get("Number"));
				break;
			}

			if (wNumber > 0) {
				wResult = StringUtils.Format("提示：未来日计划已制定，无法制定【{0}】日计划!", wShiftID);
				return wResult;
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 条件查询集合
	 * 
	 * @return
	 */
	public List<APSTaskStep> SelectListByTaskPartIDList(BMSEmployee wLoginUser, List<Integer> wTaskPartIDList,
			OutResult<Integer> wErrorCode) {
		List<APSTaskStep> wResultList = new ArrayList<APSTaskStep>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			if (wTaskPartIDList == null) {
				wTaskPartIDList = new ArrayList<Integer>();
			}

			String wSQL = StringUtils.Format(
					"SELECT t1.*,t2.OrderNo " + "FROM {0}.aps_taskstep t1,{0}.oms_order t2  WHERE t1.OrderID=t2.ID "
							+ "and t1.Active=1 "
							+ "and ( :wTaskPartIDList is null or :wTaskPartIDList = '''' or t1.TaskPartID in ({1}));",
					wInstance.Result, wTaskPartIDList.size() > 0 ? StringUtils.Join(",", wTaskPartIDList) : "0");

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wTaskPartIDList", StringUtils.Join(",", wTaskPartIDList));

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetValue(wResultList, wQueryResult);
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	private void SetValue(List<APSTaskStep> wResultList, List<Map<String, Object>> wQueryResult) {
		try {
			for (Map<String, Object> wReader : wQueryResult) {
				APSTaskStep wItem = new APSTaskStep();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.OrderID = StringUtils.parseInt(wReader.get("OrderID"));
				wItem.PartNo = StringUtils.parseString(wReader.get("PartNo"));
				wItem.TaskLineID = StringUtils.parseInt(wReader.get("TaskLineID"));
				wItem.TaskPartID = StringUtils.parseInt(wReader.get("TaskPartID"));
				wItem.WorkShopID = StringUtils.parseInt(wReader.get("WorkShopID"));
				wItem.LineID = StringUtils.parseInt(wReader.get("LineID"));
				wItem.PartID = StringUtils.parseInt(wReader.get("PartID"));
				wItem.StepID = StringUtils.parseInt(wReader.get("StepID"));
				wItem.ShiftID = StringUtils.parseInt(wReader.get("ShiftID"));
				wItem.StartTime = StringUtils.parseCalendar(wReader.get("StartTime"));
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				wItem.EndTime = StringUtils.parseCalendar(wReader.get("EndTime"));
				wItem.ReadyTime = StringUtils.parseCalendar(wReader.get("ReadyTime"));
				wItem.PlanStartTime = StringUtils.parseCalendar(wReader.get("PlanStartTime"));
				wItem.PlanEndTime = StringUtils.parseCalendar(wReader.get("PlanEndTime"));
				wItem.Status = StringUtils.parseInt(wReader.get("Status"));
				wItem.Active = StringUtils.parseInt(wReader.get("Active"));
				wItem.ProductNo = StringUtils.parseString(wReader.get("ProductNo"));
				wItem.MaterialNo = StringUtils.parseString(wReader.get("MaterialNo"));
				wItem.PlanerID = StringUtils.parseInt(wReader.get("PlannerID"));
				wItem.TaskText = StringUtils.parseString(wReader.get("TaskText"));
				wItem.WorkHour = StringUtils.parseDouble(wReader.get("WorkHour"));
				wItem.RealHour = StringUtils.parseDouble(wReader.get("RealHour"));
				wItem.OperatorList = StringUtils
						.parseIntList(StringUtils.parseString(wReader.get("OperatorList")).split(",|;"));
				wItem.IsDispatched = StringUtils.parseInt(wReader.get("IsDispatched")) == 1 ? true : false;
				wItem.RemarkList = APSTaskRemark.StringToList(StringUtils.parseString(wReader.get("RemarkList")));
				wItem.Remark = new APSTaskRemark(StringUtils.parseString(wReader.get("Remark")));

				wItem.LineName = APSConstans.GetFMCLineName(wItem.LineID);
				wItem.PartName = APSConstans.GetFPCPartName(wItem.PartID);
				wItem.StepName = APSConstans.GetFPCPartPointName(wItem.StepID);

				wItem.OrderNo = StringUtils.parseString(wReader.get("OrderNo"));

				wItem.Operators = APSConstans.GetBMSEmployeeName(wItem.OperatorList);

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 批量激活或禁用
	 */
	public ServiceResult<Integer> Active(BMSEmployee wLoginUser, List<Integer> wIDList, int wActive,
			OutResult<Integer> wErrorCode) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wIDList == null || wIDList.size() <= 0)
				return wResult;
			for (Integer wItem : wIDList) {
				APSTaskStep wAPSTaskStep = SelectByID(wLoginUser, wItem, wErrorCode);
				if (wAPSTaskStep == null || wAPSTaskStep.ID <= 0)
					continue;
				// 只有激活的才能禁用
				if (wActive == 2 && wAPSTaskStep.Active != 1) {
					wErrorCode.set(MESException.Logic.getValue());
					return wResult;
				}
				wAPSTaskStep.Active = wActive;
				long wID = Update(wLoginUser, wAPSTaskStep, wErrorCode);
				if (wID <= 0)
					break;
			}
		} catch (Exception e) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 通过ID集合获取集合
	 */
	public List<APSTaskStep> SelectListByIDList(BMSEmployee wLoginUser, List<Integer> wIDList,
			OutResult<Integer> wErrorCode) {
		List<APSTaskStep> wResultList = new ArrayList<APSTaskStep>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			if (wIDList == null) {
				wIDList = new ArrayList<Integer>();
			}

			String wSQL = StringUtils.Format(
					"SELECT t1.*,t2.OrderNo " + "FROM {0}.aps_taskstep t1,{0}.oms_order t2  WHERE t1.OrderID=t2.ID "
							+ "and ( :wIDs is null or :wIDs = '''' or t1.ID in ({1}));",
					wInstance.Result, wIDList.size() > 0 ? StringUtils.Join(",", wIDList) : "0");

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wIDs", StringUtils.Join(",", wIDList));

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
	 * 重置工序任务到派工
	 */
	public Integer ReSet(BMSEmployee wLoginUser, int wAPSTaskStepID, OutResult<Integer> wErrorCode) {
		Integer wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			ServiceResult<String> wInstance1 = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance1.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			ServiceResult<String> wInstance2 = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.EXC,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance2.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSql = StringUtils.Format(
					"update {1}.aps_taskstep set IsDispatched=0,OperatorList='''' where ID={0}; "
							+ "delete from {2}.sfc_loginstation where SFCTaskStepID in( "
							+ "	select ID from {1}.sfc_taskstep where TaskStepID= {0} " + ") and ID>0; "
							+ "delete from {1}.sfc_taskstep where TaskStepID= {0} and ID>0; "
							+ "delete from {3}.ipt_value where TaskID in( "
							+ "	select ID from {2}.sfc_taskipt where TaskStepID= {0} " + ") and ID>0; "
							+ "delete FROM {2}.sfc_taskipt where TaskStepID= {0} and ID>0;",
					String.valueOf(wAPSTaskStepID), wInstance.Result, wInstance1.Result, wInstance2.Result);
			this.ExecuteSqlTransaction(wSql);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 通过登录人、时间段查询终检和出厂检工序任务
	 */
	public List<APSTaskStep> SelectFinalAndOutList(BMSEmployee wLoginUser, Calendar wStartTime, Calendar wEndTime,
			OutResult<Integer> wErrorCode) {
		List<APSTaskStep> wResultList = new ArrayList<APSTaskStep>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			ServiceResult<String> wInstance1 = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance1.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = StringUtils.Format(
					"SELECT t1.* FROM {0}.aps_taskstep t1,{1}.fpc_part t2," + "{1}.bms_workcharge t3,{1}.mbs_user t4 "
							+ "where t4.ID=:LoginID and t4.DepartmentID=t3.ClassID and t3.StationID=t2.ID "
							+ "and t2.PartType in(3,4) and t1.PartID=t2.ID "
							+ "and ((:StartTime<t1.EndTime and :EndTime>t1.StartTime "
							+ "and t1.Status in(5)) or (t1.Status in(2,4)));",
					wInstance.Result, wInstance1.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("LoginID", wLoginUser.ID);
			wParamMap.put("StartTime", wStartTime);
			wParamMap.put("EndTime", wEndTime);

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
	 * 根据订单、工位、车号查询工序任务
	 */
	public List<APSTaskStep> SelectListByOrder(BMSEmployee wLoginUser, int wOrderID, int wPartID, String wPartNo,
			OutResult<Integer> wErrorCode) {
		List<APSTaskStep> wResultList = new ArrayList<APSTaskStep>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = StringUtils.Format(
					"SELECT t1.*,t2.OrderNo " + "FROM {0}.aps_taskstep t1,{0}.oms_order t2  WHERE t1.OrderID=t2.ID "
							+ "and ( :wOrderID <= 0 or :wOrderID = t1.OrderID ) "
							+ "and ( :wPartID <= 0 or :wPartID = t1.PartID ) "
							+ "and ( :wPartNo is null or :wPartNo = '''' or t1.PartNo =:wPartNo);",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wOrderID", wOrderID);
			wParamMap.put("wPartID", wPartID);
			wParamMap.put("wPartNo", wPartNo);

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
	 * 根据人员ID判断此人是否能做此工序任务
	 */
	public boolean JudgeCanDo(BMSEmployee wLoginUser, int wAPSTaskStepID, int wPersonID,
			OutResult<Integer> wErrorCode) {
		boolean wResult = false;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			ServiceResult<String> wInstance1 = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance1.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"select (SELECT count(*) FROM {0}.sch_secondmentapply "
							+ "where PersonID=:PersonID and Status=:Status and StartTime<now() and now()< EndTime "
							+ "and OldClassID in((SELECT ClassID FROM {1}.bms_workcharge t1,{0}.aps_taskstep t2 "
							+ "where t1.StationID=t2.PartID and t2.ID=:APSTaskStepID and t1.Active=1))) as t1,"
							+ "(SELECT count(*) FROM {0}.sch_secondmentapply where PersonID=:PersonID "
							+ "and Status=:Status and StartTime<now() and now()< EndTime and NewClassID in"
							+ "((SELECT ClassID FROM {1}.bms_workcharge t1,{0}.aps_taskstep t2 "
							+ "where t1.StationID=t2.PartID and t2.ID=:APSTaskStepID and t1.Active=1))) as t2,"
							+ "(SELECT count(*) FROM {1}.mbs_user where ID=:PersonID and DepartmentID in "
							+ "(SELECT ClassID FROM {1}.bms_workcharge t1,{0}.aps_taskstep t2 "
							+ "where t1.StationID=t2.PartID and t2.ID=:APSTaskStepID and t1.Active=1)) as t3;",
					wInstance.Result, wInstance1.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("Status", 20);
			wParamMap.put("APSTaskStepID", wAPSTaskStepID);
			wParamMap.put("PersonID", wPersonID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wMap : wQueryResult) {
				int wT1 = StringUtils.parseInt(wMap.get("t1"));
				int wT2 = StringUtils.parseInt(wMap.get("t2"));
				int wT3 = StringUtils.parseInt(wMap.get("t3"));
				if (wT1 > 0 || wT2 > 0 || wT3 > 0) {
					wResult = true;
				} else {
					wResult = false;
				}
				return wResult;
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 工序任务ID获取派工人和执行班组
	 */
	public APSTaskStepInfo QueryDispatchInfo(BMSEmployee wLoginUser, int wAPSTaskStepID,
			OutResult<Integer> wErrorCode) {
		APSTaskStepInfo wResult = new APSTaskStepInfo();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			ServiceResult<String> wInstance1 = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance1.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"SELECT t2.Name as Dispacher,t3.Name as ClassName FROM {0}.sfc_taskstep t1,"
							+ "{1}.mbs_user t2,{1}.bms_department t3 where t1.MonitorID=t2.ID "
							+ "and t2.DepartmentID=t3.ID and t1.TaskStepID=:TaskStepID group by t2.Name;",
					wInstance.Result, wInstance1.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("TaskStepID", wAPSTaskStepID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wMap : wQueryResult) {
				String wClassName = StringUtils.parseString(wMap.get("ClassName"));
				String wDispacher = StringUtils.parseString(wMap.get("Dispacher"));
				wResult.ClassName = wClassName;
				wResult.Dispather = wDispacher;
				return wResult;
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据时间段和班次获取工序详情
	 */
	public List<APSTaskStepInfo> QueryAPSTaskStepInfoList(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime, List<Integer> wClassIDList, List<Integer> wPartIDList, List<Integer> wStatusList,
			int wIsDispatched, OutResult<Integer> wErrorCode) {
		List<APSTaskStepInfo> wResult = new ArrayList<APSTaskStepInfo>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			ServiceResult<String> wInstance1 = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance1.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
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
				return wResult;
			}

			if (wClassIDList == null) {
				wClassIDList = new ArrayList<Integer>();
			}
			if (wPartIDList == null) {
				wPartIDList = new ArrayList<Integer>();
			}
			if (wStatusList == null) {
				wStatusList = new ArrayList<Integer>();
			}

			boolean wIsHistory = true;
			if (wEndTime.compareTo(Calendar.getInstance()) > 0) {
				wIsHistory = false;
			}

			int wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, wEndTime, APSShiftPeriod.Day,
					FMCShiftLevel.Day, 0);

			String wSQL = "";
			if (!wIsHistory) {
				wSQL = StringUtils.Format(
						"SELECT t1.IsDispatched,t1.ID,t2.OrderNo,t5.Name as LineName,t6.CustomerName,t2.PartNo,"
								+ "t1.ReadyTime,t1.StartTime,t1.EndTime,t3.Name as PartName,t3.ID as PartID,t4.Name as "
								+ "StepName,t1.Status,t1.OperatorList FROM {0}.aps_taskstep t1,"
								+ "{0}.oms_order t2,{3}.fpc_part t3,{3}.fpc_partpoint t4,"
								+ "{3}.fmc_line t5,{3}.crm_customer t6 where t2.BureauSectionID=t6.ID "
								+ "and t1.LineID=t5.ID and t1.StepID=t4.ID and t1.PartID=t3.ID and t1.OrderID=t2.ID "
								+ "and ( :wPartID is null or :wPartID = '''' or t1.PartID in ({1})) "
								+ "and ( :wStatus is null or :wStatus = '''' or t1.Status in ({2})) "
								+ "and ( :wIsDispatched < 0 or :wIsDispatched = t1.IsDispatched ) "
								+ "and  ((t1.ShiftID<=:wShiftID  and t1.Status !=5) or "
								+ "(:StartTime<t1.EndTime and :EndTime>t1.StartTime and t1.Status=5) ) group by t1.ID;",
						wInstance.Result, wPartIDList.size() > 0 ? StringUtils.Join(",", wPartIDList) : "0",
						wStatusList.size() > 0 ? StringUtils.Join(",", wStatusList) : "0", wInstance1.Result);
			} else {
				wSQL = StringUtils.Format(
						"SELECT t1.IsDispatched,t1.ID,t2.OrderNo,t5.Name as LineName,t6.CustomerName,t2.PartNo,t1.ReadyTime,t1.StartTime,"
								+ "t1.EndTime,t3.Name as PartName,t3.ID as PartID,t4.Name as StepName,t1.Status,t1.OperatorList "
								+ "FROM {0}.aps_taskstep t1,{0}.oms_order t2,{3}.fpc_part t3,"
								+ "{3}.fpc_partpoint t4,{3}.fmc_line t5,{3}.crm_customer t6 where "
								+ "t2.BureauSectionID=t6.ID and t1.LineID=t5.ID and t1.StepID=t4.ID and t1.PartID=t3.ID "
								+ "and ( :wPartID is null or :wPartID = '''' or t1.PartID in ({1})) "
								+ "and ( :wStatus is null or :wStatus = '''' or t1.Status in ({2})) "
								+ "and ( :wIsDispatched < 0 or :wIsDispatched = t1.IsDispatched ) "
								+ "and t1.OrderID=t2.ID and  ((t1.ShiftID<=:wShiftID  and t1.Status !=5) "
								+ "or (:StartTime<t1.EndTime and :EndTime>t1.StartTime and t1.Status=5) ) "
								+ "and t1.Status in(2,4,5) group by t1.ID;",
						wInstance.Result, wPartIDList.size() > 0 ? StringUtils.Join(",", wPartIDList) : "0",
						wStatusList.size() > 0 ? StringUtils.Join(",", wStatusList) : "0", wInstance1.Result);
			}

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("StartTime", wStartTime);
			wParamMap.put("EndTime", wEndTime);
			wParamMap.put("wShiftID", wShiftID);
			wParamMap.put("wPartID", StringUtils.Join(",", wPartIDList));
			wParamMap.put("wStatus", StringUtils.Join(",", wStatusList));
			wParamMap.put("wIsDispatched", wIsDispatched);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wMap : wQueryResult) {
				APSTaskStepInfo wItem = new APSTaskStepInfo();

				wItem.ID = StringUtils.parseInt(wMap.get("ID"));
				APSTaskStepInfo wInfo = this.QueryDispatchInfo(wLoginUser, wItem.ID, wErrorCode);
				wItem.ClassName = wInfo.ClassName;
				wItem.Dispather = wInfo.Dispather;
				wItem.CustomerName = StringUtils.parseString(wMap.get("CustomerName"));
				wItem.EndTime = StringUtils.parseCalendar(wMap.get("EndTime"));
				wItem.LineName = StringUtils.parseString(wMap.get("LineName"));
				wItem.OrderNo = StringUtils.parseString(wMap.get("OrderNo"));
				wItem.PartName = StringUtils.parseString(wMap.get("PartName"));
				wItem.PartID = StringUtils.parseInt(wMap.get("PartID"));
				wItem.PartNo = StringUtils.parseString(wMap.get("PartNo"));
				wItem.ReadyTime = StringUtils.parseCalendar(wMap.get("ReadyTime"));
				wItem.StartTime = StringUtils.parseCalendar(wMap.get("StartTime"));
				wItem.Status = StringUtils.parseInt(wMap.get("Status"));
				wItem.IsDispatched = StringUtils.parseInt(wMap.get("IsDispatched"));
				wItem.StepName = StringUtils.parseString(wMap.get("StepName"));
				wItem.Operators = this.GetOperators(StringUtils.parseString(wMap.get("OperatorList")));

				wResult.add(wItem);
			}

			if (wResult.size() > 0 && wClassIDList.size() > 0) {
				List<Integer> wNewClassList = wClassIDList;
				wResult = wResult.stream()
						.filter(p -> wNewClassList.stream()
								.anyMatch(q -> APSConstans.GetBMSDepartmentName(q).equals(p.ClassName)))
						.collect(Collectors.toList());
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据人员ID字符串获取人员列表
	 */
	private String GetOperators(String wOperatorList) {
		String wResult = "";
		try {
			if (StringUtils.isEmpty(wOperatorList)) {
				return wResult;
			}

			String[] wStrs = wOperatorList.split(",|;");
			List<String> wNames = new ArrayList<String>();
			for (String wStr : wStrs) {
				String wName = APSConstans.GetBMSEmployeeName(Integer.parseInt(wStr));
				if (StringUtils.isNotEmpty(wName)) {
					wNames.add(wName);
				}
			}
			wResult = StringUtils.Join(",", wNames);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取工序任务详情(质量三检进度)
	 */
	public List<APSTaskStepDetails> SelectAPSTaskStepDetails(BMSEmployee wLoginUser, int wAPSTaskPartID,
			OutResult<Integer> wErrorCode) {
		List<APSTaskStepDetails> wResult = new ArrayList<APSTaskStepDetails>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			ServiceResult<String> wInstance1 = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance1.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			ServiceResult<String> wInstance2 = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.EXC,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance2.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"SELECT   (select count(*) as TotalSize from {2}.ipt_itemrecord where ItemType!=4 and vid in(  SELECT ModuleVersionID FROM {1}.sfc_taskipt where TaskStepID=t1.ID and TaskType=6)) as TSize,"
							+ "(select count(distinct(IPTItemID)) as FinishSize from {2}.ipt_value where TaskID in( SELECT ID FROM {1}.sfc_taskipt where TaskStepID=t1.ID and TaskType=6) and Status=2) as SelfFSize,"
							+ "(select count(distinct(IPTItemID)) as FinishSize from {2}.ipt_value where TaskID in( SELECT ID FROM {1}.sfc_taskipt where TaskStepID=t1.ID and TaskType=12) and Status=2) as MFSize,"
							+ "(select count(distinct(IPTItemID)) as FinishSize from {2}.ipt_value where TaskID in( SELECT ID FROM {1}.sfc_taskipt where TaskStepID=t1.ID and TaskType=13) and Status=2) as SFSize,"
							+ "(select count(*) as TotalSize from {2}.ipt_itemrecord where ItemType!=4 and vid in(  SELECT ModuleVersionID FROM {1}.sfc_taskipt where TaskStepID=t1.ID and TaskType=14)) as PTSize,"
							+ "(select count(distinct(IPTItemID)) as FinishSize from {2}.ipt_value where TaskID in( SELECT ID FROM {1}.sfc_taskipt where TaskStepID=t1.ID and TaskType=14) and Status=2) as PFSize,"
							+ "t1.OrderID,t1.ID, t2.PartType as Type,t2.Name as PartName,t2.ID as PartID,"
							+ "t3.Name as StepName,t4.Name as LineName,t1.PartNo,t1.Status "
							+ "FROM {0}.aps_taskstep t1,{1}.fpc_part t2,"
							+ "{1}.fpc_partpoint t3,{1}.fmc_line t4 where t1.PartID=t2.ID "
							+ "and t1.StepID=t3.ID and t1.LineID=t4.ID and TaskPartID=:TaskPartID and t1.Active=1;",
					wInstance.Result, wInstance1.Result, wInstance2.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("TaskPartID", wAPSTaskPartID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wMap : wQueryResult) {
				APSTaskStepDetails wAPSTaskStepDetails = new APSTaskStepDetails();

				wAPSTaskStepDetails.ID = StringUtils.parseInt(wMap.get("ID"));
				wAPSTaskStepDetails.LineName = StringUtils.parseString(wMap.get("LineName"));
				wAPSTaskStepDetails.MFSize = StringUtils.parseInt(wMap.get("MFSize"));
				wAPSTaskStepDetails.PartID = StringUtils.parseInt(wMap.get("PartID"));
				wAPSTaskStepDetails.PartName = StringUtils.parseString(wMap.get("PartName"));
				wAPSTaskStepDetails.PFSize = StringUtils.parseInt(wMap.get("PFSize"));
				wAPSTaskStepDetails.PartNo = StringUtils.parseString(wMap.get("PartNo"));
				wAPSTaskStepDetails.PTSize = StringUtils.parseInt(wMap.get("PTSize"));
				wAPSTaskStepDetails.SelfFSize = StringUtils.parseInt(wMap.get("SelfFSize"));
				wAPSTaskStepDetails.SFSize = StringUtils.parseInt(wMap.get("SFSize"));
				wAPSTaskStepDetails.Status = StringUtils.parseInt(wMap.get("Status"));
				wAPSTaskStepDetails.StepName = StringUtils.parseString(wMap.get("StepName"));
				wAPSTaskStepDetails.TSize = StringUtils.parseInt(wMap.get("TSize"));
				wAPSTaskStepDetails.Type = StringUtils.parseInt(wMap.get("Type"));
				wAPSTaskStepDetails.OrderID = StringUtils.parseInt(wMap.get("OrderID"));

				wResult.add(wAPSTaskStepDetails);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取工序任务集合
	 */
	public List<APSTaskStep> SelectAPSTaskStepList(BMSEmployee wLoginUser, Calendar wShiftEnd,
			OutResult<Integer> wErrorCode) {
		List<APSTaskStep> wResult = new ArrayList<APSTaskStep>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			ServiceResult<String> wInstance1 = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance1.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			ServiceResult<String> wInstance2 = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.EXC,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance2.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"select distinct(ID) TaskStepID from {0}.aps_taskstep where :EndTime>PlanStartTime and "
							+ "TaskPartID in(SELECT distinct(TaskPartID) FROM {0}.aps_taskstep "
							+ "where Status=1 and TaskPartID in(SELECT distinct(ID) as TaskPartID FROM "
							+ "{0}.aps_taskpart where Status in(2,3,4)  "
							+ "and ShiftPeriod=5 and Active=1 and PartID in (SELECT distinct(StationID) FROM "
							+ "{2}.lfs_workareastation where WorkAreaID in(SELECT ID FROM {1}.bms_department "
							+ "where ID in(SELECT DepartmentID FROM {1}.mbs_user where ID=:UserID) and Type=2 "
							+ "and 1 in(SELECT count(*) FROM {1}.bms_position where ID in(SELECT Position "
							+ "FROM {1}.mbs_user where ID=:UserID) and DutyID=2)) and Active=1) and Active=1))",
					wInstance.Result, wInstance1.Result, wInstance2.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("EndTime", wShiftEnd);
			wParamMap.put("UserID", wLoginUser.ID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			List<Integer> wTaskStepIDList = new ArrayList<Integer>();
			for (Map<String, Object> wMap : wQueryResult) {
				int wTaskStepID = StringUtils.parseInt(wMap.get("TaskStepID"));
				if (wTaskStepID > 0) {
					wTaskStepIDList.add(wTaskStepID);
				}
			}
			if (wTaskStepIDList.size() > 0) {
				wResult = APSTaskStepDAO.getInstance().SelectListByIDList(wLoginUser, wTaskStepIDList, wErrorCode);
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 获取车号排序的日计划
	 * 
	 * @param wErrorCode
	 */
	public List<APSPartNoDetails> SelectAPSPartNoDetailsList(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime, int wWorkAreaID, OutResult<Integer> wErrorCode) {
		List<APSPartNoDetails> wResult = new ArrayList<APSPartNoDetails>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			ServiceResult<String> wInstance1 = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.EXC,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance1.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"select t1.* from  {0}.aps_taskstep t1, {0}.aps_taskpart t2," + "{1}.lfs_workareastation t3  "
							+ "where t1.Active=1 and t1.TaskPartID=t2.ID and t2.PartID=t3.StationID "
							+ "and ( :WorkAreaID < 0 or :WorkAreaID = t3.WorkAreaID ) " + "and t1.TaskPartID in  "
							+ "(SELECT distinct(TaskPartID) FROM  {0}.aps_taskstep t1, {0}.aps_taskpart t2 "
							+ "where t1.Active=1 and t1.TaskPartID=t2.ID and t2.Active=1  "
							+ "and (t1.Status != 5 or (t1.Status=5 and :StartTime < t1.EndTime "
							+ "and :EndTime)>t1.StartTime ) and t1.TaskPartID>0);",
					wInstance.Result, wInstance1.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("StartTime", wStartTime);
			wParamMap.put("EndTime", wEndTime);
			wParamMap.put("WorkAreaID", wWorkAreaID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			List<APSTaskStep> wList = new ArrayList<APSTaskStep>();
			for (Map<String, Object> wMap : wQueryResult) {
				APSTaskStep wAPSTaskStep = new APSTaskStep();
				wAPSTaskStep.PartNo = StringUtils.parseString(wMap.get("PartNo"));
				wAPSTaskStep.OrderID = StringUtils.parseInt(wMap.get("OrderID"));
				wAPSTaskStep.Status = StringUtils.parseInt(wMap.get("Status"));
				wList.add(wAPSTaskStep);
			}

			List<Integer> wOrderIDList = wList.stream().map(p -> p.OrderID).distinct().collect(Collectors.toList());

			List<OMSOrder> wOrderList = OMSOrderDAO.getInstance().SelectListByIDList(wLoginUser, wOrderIDList,
					wErrorCode);
			// 订单排序
			wOrderList.sort(Comparator.comparing(OMSOrder::getERPID).thenComparing(OMSOrder::getRealReceiveDate));
//			wOrderIDList = wOrderList.stream().map(p -> p.ID).distinct().collect(Collectors.toList());

			for (OMSOrder wOMSOrder : wOrderList) {
				APSPartNoDetails wAPSPartNoDetails = new APSPartNoDetails();
				wAPSPartNoDetails.OrderID = wOMSOrder.ID;
				wAPSPartNoDetails.PartNo = wList.stream().filter(p -> p.OrderID == wOMSOrder.ID).findFirst()
						.get().PartNo;
				wAPSPartNoDetails.StepSize = (int) wList.stream().filter(p -> p.OrderID == wOMSOrder.ID).count();
				wAPSPartNoDetails.StepFinish = (int) wList.stream()
						.filter(p -> p.OrderID == wOMSOrder.ID && p.Status == 5).count();
				wAPSPartNoDetails.StepSchedule = (int) wList.stream()
						.filter(p -> p.OrderID == wOMSOrder.ID && (p.Status == 2 || p.Status == 4 || p.Status == 5))
						.count();
				wAPSPartNoDetails.StepMaking = (int) wList.stream()
						.filter(p -> p.OrderID == wOMSOrder.ID && (p.Status == 8 || p.Status == 9 || p.Status == 10))
						.count();
				wResult.add(wAPSPartNoDetails);
			}

			// 排序
//			if (wResult.size() > 0) {
//				wResult.sort(Comparator.comparing(APSPartNoDetails::getPartNo));
//			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取工区分类的日计划
	 */

	public List<APSWorkAreaDetails> SelectAPSWorkAreaDetailsList(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime, int wOrderID, OutResult<Integer> wErrorCode) {
		List<APSWorkAreaDetails> wResult = new ArrayList<APSWorkAreaDetails>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			ServiceResult<String> wInstance1 = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance1.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			ServiceResult<String> wInstance2 = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.EXC,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance2.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("select t2.WorkAreaID,t3.Name as WorkArea,t1.* from {0}.aps_taskstep t1,"
					+ "{2}.lfs_workareastation t2,{1}.bms_department t3 "
					+ "where t1.Active=1 and t2.WorkAreaID=t3.ID and t1.PartID =t2.StationID and OrderID=:OrderID "
					+ "and TaskPartID in (SELECT distinct(TaskPartID) FROM {0}.aps_taskstep t1,"
					+ "{0}.aps_taskpart t2 where  t1.TaskPartID=t2.ID and t2.Active=1 "
					+ "and (t1.Status != 5 or (t1.Status=5 and :StartTime < t1.EndTime "
					+ "and :EndTime)>t1.StartTime ) and t1.TaskPartID>0);", wInstance.Result, wInstance1.Result,
					wInstance2.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("StartTime", wStartTime);
			wParamMap.put("EndTime", wEndTime);
			wParamMap.put("OrderID", wOrderID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			List<APSTaskStep> wList = new ArrayList<APSTaskStep>();
			for (Map<String, Object> wMap : wQueryResult) {
				APSTaskStep wAPSTaskStep = new APSTaskStep();
				wAPSTaskStep.PartNo = StringUtils.parseString(wMap.get("PartNo"));
				wAPSTaskStep.OrderID = StringUtils.parseInt(wMap.get("OrderID"));
				wAPSTaskStep.Status = StringUtils.parseInt(wMap.get("Status"));
				wAPSTaskStep.AreaID = StringUtils.parseInt(wMap.get("WorkAreaID"));
				wAPSTaskStep.AreaName = StringUtils.parseString(wMap.get("WorkArea"));
				wList.add(wAPSTaskStep);
			}

			List<Integer> wAreaIDList = wList.stream().map(p -> p.AreaID).distinct().collect(Collectors.toList());
			for (Integer wAraID : wAreaIDList) {
				APSWorkAreaDetails wAPSWorkAreaDetails = new APSWorkAreaDetails();
				wAPSWorkAreaDetails.AreaID = wAraID;
				wAPSWorkAreaDetails.AreaName = wList.stream().filter(p -> p.AreaID == wAraID).findFirst()
						.get().AreaName;
				wAPSWorkAreaDetails.PartNo = wList.stream().filter(p -> p.AreaID == wAraID).findFirst().get().PartNo;
				wAPSWorkAreaDetails.OrderID = wOrderID;
				wAPSWorkAreaDetails.StepSize = (int) wList.stream().filter(p -> p.AreaID == wAraID).count();
				wAPSWorkAreaDetails.StepFinish = (int) wList.stream().filter(p -> p.AreaID == wAraID && p.Status == 5)
						.count();
				wAPSWorkAreaDetails.StepSchedule = (int) wList.stream()
						.filter(p -> p.AreaID == wAraID && (p.Status == 2 || p.Status == 4 || p.Status == 5)).count();
				wAPSWorkAreaDetails.StepMaking = (int) wList.stream()
						.filter(p -> p.AreaID == wAraID && (p.Status == 8 || p.Status == 9 || p.Status == 10)).count();
				wResult.add(wAPSWorkAreaDetails);
			}

			// 排序
			if (wResult.size() > 0) {
				wResult.sort(Comparator.comparing(APSWorkAreaDetails::getAreaID));
			}

		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取工序列表
	 * 
	 * @param wErrorCode
	 */
	public List<APSTaskStep> SelectQueryTaskStep(BMSEmployee wLoginUser, Calendar wStartTime, Calendar wEndTime,
			int wAreaID, int wOrderID, OutResult<Integer> wErrorCode) {
		List<APSTaskStep> wResult = new ArrayList<APSTaskStep>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			ServiceResult<String> wInstance1 = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance1.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			ServiceResult<String> wInstance2 = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.EXC,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance2.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"select t2.WorkAreaID,t2.OrderNum,t3.Name as WorkArea,t1.* from {0}.aps_taskstep t1,"
							+ "{2}.lfs_workareastation t2,{1}.bms_department t3 "
							+ "where t1.Active=1 and t2.WorkAreaID=t3.ID and t1.PartID =t2.StationID  "
							+ "and ( :wAreaID <= 0 or :wAreaID = t2.WorkAreaID ) "
							+ "and ( :wOrderID <= 0 or :wOrderID = t1.OrderID ) " + "and TaskPartID "
							+ "in (SELECT distinct(TaskPartID) FROM {0}.aps_taskstep t1,"
							+ "{0}.aps_taskpart t2 where  t1.TaskPartID=t2.ID and t2.Active=1 "
							+ "and (t1.Status != 5 or (t1.Status=5 and :StartTime < t1.EndTime "
							+ "and :EndTime>t1.StartTime )) and t1.TaskPartID>0);",
					wInstance.Result, wInstance1.Result, wInstance2.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("StartTime", wEndTime);
			wParamMap.put("EndTime", wEndTime);
			wParamMap.put("wAreaID", wAreaID);
			wParamMap.put("wOrderID", wOrderID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				APSTaskStep wItem = new APSTaskStep();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.OrderNum = StringUtils.parseInt(wReader.get("OrderNum"));
				wItem.AreaID = StringUtils.parseInt(wReader.get("WorkAreaID"));
				wItem.AreaName = StringUtils.parseString(wReader.get("WorkArea"));
				wItem.OrderID = StringUtils.parseInt(wReader.get("OrderID"));
				wItem.PartNo = StringUtils.parseString(wReader.get("PartNo"));
				wItem.TaskLineID = StringUtils.parseInt(wReader.get("TaskLineID"));
				wItem.TaskPartID = StringUtils.parseInt(wReader.get("TaskPartID"));
				wItem.WorkShopID = StringUtils.parseInt(wReader.get("WorkShopID"));
				wItem.LineID = StringUtils.parseInt(wReader.get("LineID"));
				wItem.PartID = StringUtils.parseInt(wReader.get("PartID"));
				wItem.StepID = StringUtils.parseInt(wReader.get("StepID"));
				wItem.ShiftID = StringUtils.parseInt(wReader.get("ShiftID"));
				wItem.StartTime = StringUtils.parseCalendar(wReader.get("StartTime"));
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				wItem.EndTime = StringUtils.parseCalendar(wReader.get("EndTime"));
				wItem.ReadyTime = StringUtils.parseCalendar(wReader.get("ReadyTime"));
				wItem.PlanStartTime = StringUtils.parseCalendar(wReader.get("PlanStartTime"));
				wItem.Status = StringUtils.parseInt(wReader.get("Status"));
				wItem.Active = StringUtils.parseInt(wReader.get("Active"));
				wItem.ProductNo = StringUtils.parseString(wReader.get("ProductNo"));
				wItem.MaterialNo = StringUtils.parseString(wReader.get("MaterialNo"));
				wItem.PlanerID = StringUtils.parseInt(wReader.get("PlannerID"));
				wItem.TaskText = StringUtils.parseString(wReader.get("TaskText"));
				wItem.WorkHour = StringUtils.parseDouble(wReader.get("WorkHour"));
				wItem.RealHour = StringUtils.parseDouble(wReader.get("RealHour"));
				wItem.OperatorList = StringUtils
						.parseIntList(StringUtils.parseString(wReader.get("OperatorList")).split(",|;"));
				wItem.IsDispatched = StringUtils.parseInt(wReader.get("IsDispatched")) == 1 ? true : false;
				wItem.RemarkList = APSTaskRemark.StringToList(StringUtils.parseString(wReader.get("RemarkList")));
				wItem.Remark = new APSTaskRemark(StringUtils.parseString(wReader.get("Remark")));

				wItem.LineName = APSConstans.GetFMCLineName(wItem.LineID);
				wItem.PartName = APSConstans.GetFPCPartName(wItem.PartID);
				wItem.StepName = APSConstans.GetFPCPartPointName(wItem.StepID);

				wItem.OrderNo = StringUtils.parseString(wReader.get("OrderNo"));

				wResult.add(wItem);
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 自动完成工序任务
	 * 
	 * @param wErrorCode
	 */
	public void AutoFinishTask(BMSEmployee wLoginUser, OutResult<Integer> wErrorCode) {
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return;
			}

			ServiceResult<String> wInstance1 = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance1.ErrorCode);
			if (wErrorCode.Result != 0) {
				return;
			}

			String wSQL = StringUtils.Format("SELECT t1.* FROM {0}.aps_taskstep t1,{1}.sfc_taskipt t2,"
					+ "{1}.sfc_taskipt t3 where t1.ID=t2.TaskStepID and t2.Status=2 "
					+ "and t2.TaskType=6 and t3.TaskStepID=t1.ID and t3.Status=2 "
					+ "and t3.TaskType=12 and t1.Status=4;", wInstance.Result, wInstance1.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			List<APSTaskStep> wList = new ArrayList<APSTaskStep>();
			for (Map<String, Object> wReader : wQueryResult) {
				APSTaskStep wItem = new APSTaskStep();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.OrderID = StringUtils.parseInt(wReader.get("OrderID"));
				wItem.PartNo = StringUtils.parseString(wReader.get("PartNo"));
				wItem.TaskLineID = StringUtils.parseInt(wReader.get("TaskLineID"));
				wItem.TaskPartID = StringUtils.parseInt(wReader.get("TaskPartID"));
				wItem.WorkShopID = StringUtils.parseInt(wReader.get("WorkShopID"));
				wItem.LineID = StringUtils.parseInt(wReader.get("LineID"));
				wItem.PartID = StringUtils.parseInt(wReader.get("PartID"));
				wItem.StepID = StringUtils.parseInt(wReader.get("StepID"));
				wItem.ShiftID = StringUtils.parseInt(wReader.get("ShiftID"));
				wItem.StartTime = StringUtils.parseCalendar(wReader.get("StartTime"));
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				wItem.EndTime = StringUtils.parseCalendar(wReader.get("EndTime"));
				wItem.ReadyTime = StringUtils.parseCalendar(wReader.get("ReadyTime"));
				wItem.PlanStartTime = StringUtils.parseCalendar(wReader.get("PlanStartTime"));
				wItem.Status = StringUtils.parseInt(wReader.get("Status"));
				wItem.Active = StringUtils.parseInt(wReader.get("Active"));
				wItem.ProductNo = StringUtils.parseString(wReader.get("ProductNo"));
				wItem.MaterialNo = StringUtils.parseString(wReader.get("MaterialNo"));
				wItem.PlanerID = StringUtils.parseInt(wReader.get("PlannerID"));
				wItem.TaskText = StringUtils.parseString(wReader.get("TaskText"));
				wItem.WorkHour = StringUtils.parseDouble(wReader.get("WorkHour"));
				wItem.RealHour = StringUtils.parseDouble(wReader.get("RealHour"));
				wItem.OperatorList = StringUtils
						.parseIntList(StringUtils.parseString(wReader.get("OperatorList")).split(",|;"));
				wItem.IsDispatched = StringUtils.parseInt(wReader.get("IsDispatched")) == 1 ? true : false;
				wItem.RemarkList = APSTaskRemark.StringToList(StringUtils.parseString(wReader.get("RemarkList")));
				wItem.Remark = new APSTaskRemark(StringUtils.parseString(wReader.get("Remark")));
				wItem.OrderNo = StringUtils.parseString(wReader.get("OrderNo"));

				wList.add(wItem);
			}

			if (wList.size() > 0) {
				for (APSTaskStep wAPSTaskStep : wList) {
					wAPSTaskStep.Status = 5;
					wAPSTaskStep.EndTime = Calendar.getInstance();
					APSTaskStepDAO.getInstance().Update(wLoginUser, wAPSTaskStep, wErrorCode);
				}
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
	}

	/**
	 * 根据订单和工位获取工序任务集合
	 */
	public List<APSTaskStep> APS_QueryOrderTimeInfoList(BMSEmployee wLoginUser, int wOrderID, String wStationName,
			OutResult<Integer> wErrorCode) {
		List<APSTaskStep> wResult = new ArrayList<APSTaskStep>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			ServiceResult<String> wInstance1 = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance1.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("SELECT ID FROM {0}.aps_taskstep where "
					+ "TaskPartID in (SELECT t1.ID FROM {0}.aps_taskpart t1,{1}.fpc_part t2 "
					+ "where t1.PartID=t2.ID and t1.OrderID=:OrderID and t2.Name=:StationName and t1.ShiftPeriod=5 and t1.Active=1);",
					wInstance.Result, wInstance1.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("OrderID", wOrderID);
			wParamMap.put("StationName", wStationName);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			List<Integer> wTaskStepIDList = new ArrayList<Integer>();
			for (Map<String, Object> wMap : wQueryResult) {
				int wTaskStepID = StringUtils.parseInt(wMap.get("ID"));
				if (wTaskStepID > 0) {
					wTaskStepIDList.add(wTaskStepID);
				}
			}
			if (wTaskStepIDList.size() > 0) {
				wResult = APSTaskStepDAO.getInstance().SelectListByIDList(wLoginUser, wTaskStepIDList, wErrorCode);
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 查询shiftID小于指定ShiftID的日计划(状态为下达)
	 * 
	 * @param wErrorCode
	 */
	public List<APSTaskStep> SelectListByShiftID(BMSEmployee wAdminUser, int wShiftID, OutResult<Integer> wErrorCode) {
		List<APSTaskStep> wResult = new ArrayList<APSTaskStep>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wAdminUser.getCompanyID(), MESDBSource.APS,
					wAdminUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"SELECT * FROM {0}.aps_taskstep " + "where Status=2 and ShiftID < :ShiftID;", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ShiftID", wShiftID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetValue(wResult, wQueryResult);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取标准工时
	 */
	public double GetActualPeriod(BMSEmployee wLoginUser, int routeID, int partID, int stepID,
			OutResult<Integer> wErrorCode) {
		double wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"SELECT ActualPeriod FROM {0}.fpc_routepartpoint "
							+ "where RouteID=:RouteID and PartID=:PartID and PartPointID=:PartPointID;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("RouteID", routeID);
			wParamMap.put("PartID", partID);
			wParamMap.put("PartPointID", stepID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				wResult = StringUtils.parseDouble(wReader.get("ActualPeriod"));
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 通过订单ID集合和工位ID集合获取数据集合
	 */
	public List<APSTaskStep> SelectList(BMSEmployee wLoginUser, String wOrderIDs, String wPartIDs,
			OutResult<Integer> wErrorCode) {
		List<APSTaskStep> wResultList = new ArrayList<APSTaskStep>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = StringUtils.Format(
					"SELECT t1.*,t2.OrderNo " + "FROM {0}.aps_taskstep t1,{0}.oms_order t2  WHERE t1.OrderID=t2.ID "
							+ "and ( t1.OrderID in ({1}) ) " + "and ( t1.PartID in ({2}) ) ;",
					wInstance.Result, wOrderIDs, wPartIDs);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetValue(wResultList, wQueryResult);
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	public List<Integer> SelectNotRealFinishedTaskStepIDList(BMSEmployee wLoginUser, OutResult<Integer> wErrorCode) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"select t1.TaskStepID from {0}.sfc_taskipt t1,{0}.aps_taskstep t2 where "
							+ "t1.TaskType=13 and t1.status = 1 "
							+ "and t1.TaskStepID=t2.ID and t2.Status=5 and t1.SubmitTime>''2021-9-1 00:00:00'';",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wTaskStepID = StringUtils.parseInt(wReader.get("TaskStepID"));
				wResult.add(wTaskStepID);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}
}
