package com.mes.loco.aps.server.serviceimpl.dao.aps;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.mes.loco.aps.server.service.mesenum.MESDBSource;
import com.mes.loco.aps.server.service.mesenum.MESException;
import com.mes.loco.aps.server.service.po.OutResult;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.aps.APSTaskStepPlan;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSConstans;

public class APSTaskStepPlanDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(APSTaskStepPlanDAO.class);

	private static APSTaskStepPlanDAO Instance = null;

	/**
	 * 添加或修改
	 * 
	 * @param wAPSTaskStepPlan
	 * @return
	 */
	public int Update(BMSEmployee wLoginUser, APSTaskStepPlan wAPSTaskStepPlan, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wAPSTaskStepPlan == null)
				return 0;

			String wSQL = "";
			if (wAPSTaskStepPlan.getID() <= 0) {
				wSQL = MessageFormat.format(
						"INSERT INTO {0}.aps_taskstepplan(OrderID,PartNo,TaskPartID,LineID,PartID,StepID,ShiftID,CreateTime,StartTime,EndTime,ReadyTime,Status,Active,ProductNo,PlannerID,TaskText,Remark,APSShiftPeriod) VALUES(:OrderID,:PartNo,:TaskPartID,:LineID,:PartID,:StepID,:ShiftID,:CreateTime,:StartTime,:EndTime,:ReadyTime,:Status,:Active,:ProductNo,:PlannerID,:TaskText,:Remark,:APSShiftPeriod);",
						wInstance.Result);
			} else {
				wSQL = MessageFormat.format(
						"UPDATE {0}.aps_taskstepplan SET OrderID = :OrderID,PartNo = :PartNo,TaskPartID = :TaskPartID,LineID = :LineID,PartID = :PartID,StepID = :StepID,ShiftID = :ShiftID,CreateTime = :CreateTime,StartTime = :StartTime,EndTime = :EndTime,ReadyTime = :ReadyTime,Status = :Status,Active = :Active,ProductNo = :ProductNo,PlannerID = :PlannerID,TaskText = :TaskText,Remark = :Remark,APSShiftPeriod=:APSShiftPeriod WHERE ID = :ID;",
						wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wAPSTaskStepPlan.ID);
			wParamMap.put("OrderID", wAPSTaskStepPlan.OrderID);
			wParamMap.put("PartNo", wAPSTaskStepPlan.PartNo);
			wParamMap.put("TaskPartID", wAPSTaskStepPlan.TaskPartID);
			wParamMap.put("LineID", wAPSTaskStepPlan.LineID);
			wParamMap.put("PartID", wAPSTaskStepPlan.PartID);
			wParamMap.put("StepID", wAPSTaskStepPlan.StepID);
			wParamMap.put("ShiftID", wAPSTaskStepPlan.ShiftID);
			wParamMap.put("CreateTime", wAPSTaskStepPlan.CreateTime);
			wParamMap.put("StartTime", wAPSTaskStepPlan.StartTime);
			wParamMap.put("EndTime", wAPSTaskStepPlan.EndTime);
			wParamMap.put("ReadyTime", wAPSTaskStepPlan.ReadyTime);
			wParamMap.put("Status", wAPSTaskStepPlan.Status);
			wParamMap.put("Active", wAPSTaskStepPlan.Active);
			wParamMap.put("ProductNo", wAPSTaskStepPlan.ProductNo);
			wParamMap.put("PlannerID", wAPSTaskStepPlan.PlannerID);
			wParamMap.put("TaskText", wAPSTaskStepPlan.TaskText);
			wParamMap.put("Remark", wAPSTaskStepPlan.Remark);
			wParamMap.put("APSShiftPeriod", wAPSTaskStepPlan.APSShiftPeriod);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wAPSTaskStepPlan.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wAPSTaskStepPlan.setID(wResult);
			} else {
				wResult = wAPSTaskStepPlan.getID();
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
	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<APSTaskStepPlan> wList,
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
			for (APSTaskStepPlan wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = MessageFormat.format("delete from {1}.aps_taskstepplan WHERE ID IN({0}) ;",
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
	public APSTaskStepPlan SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		APSTaskStepPlan wResult = new APSTaskStepPlan();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<APSTaskStepPlan> wList = SelectList(wLoginUser, wID, -1, -1, -1, -1, -1, -1, -1, -1, -1, wErrorCode);
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
	public List<APSTaskStepPlan> SelectList(BMSEmployee wLoginUser, int wID, int wOrderID, int wTaskPartID, int wLineID,
			int wPartID, int wStepID, int wShiftID, int wStatus, int wActive, int wAPSShiftPeriod,
			OutResult<Integer> wErrorCode) {
		List<APSTaskStepPlan> wResultList = new ArrayList<APSTaskStepPlan>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = MessageFormat.format("SELECT t.*,o.OrderNo FROM {0}.aps_taskstepplan t,{0}.oms_order o "
					+ "WHERE  1=1 and t.OrderID=o.ID " + "and ( :wID <= 0 or :wID = t.ID ) "
					+ "and ( :wOrderID <= 0 or :wOrderID = t.OrderID ) "
					+ "and ( :wTaskPartID <= 0 or :wTaskPartID = t.TaskPartID ) "
					+ "and ( :wLineID <= 0 or :wLineID = t.LineID ) " + "and ( :wPartID <= 0 or :wPartID = t.PartID ) "
					+ "and ( :wStepID <= 0 or :wStepID = t.StepID ) "
					+ "and ( :wShiftID <= 0 or :wShiftID = t.ShiftID ) "
					+ "and ( :wStatus <= 0 or :wStatus = t.Status ) "
					+ "and ( :wAPSShiftPeriod <= 0 or :wAPSShiftPeriod = t.APSShiftPeriod ) "
					+ "and ( :wActive <= 0 or :wActive = t.Active );", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wOrderID", wOrderID);
			wParamMap.put("wTaskPartID", wTaskPartID);
			wParamMap.put("wLineID", wLineID);
			wParamMap.put("wPartID", wPartID);
			wParamMap.put("wStepID", wStepID);
			wParamMap.put("wShiftID", wShiftID);
			wParamMap.put("wStatus", wStatus);
			wParamMap.put("wActive", wActive);
			wParamMap.put("wAPSShiftPeriod", wAPSShiftPeriod);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetValue(wResultList, wQueryResult);
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	private void SetValue(List<APSTaskStepPlan> wResultList, List<Map<String, Object>> wQueryResult) {
		try {
			for (Map<String, Object> wReader : wQueryResult) {
				APSTaskStepPlan wItem = new APSTaskStepPlan();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.OrderID = StringUtils.parseInt(wReader.get("OrderID"));
				wItem.PartNo = StringUtils.parseString(wReader.get("PartNo"));
				wItem.TaskPartID = StringUtils.parseInt(wReader.get("TaskPartID"));
				wItem.LineID = StringUtils.parseInt(wReader.get("LineID"));
				wItem.PartID = StringUtils.parseInt(wReader.get("PartID"));
				wItem.StepID = StringUtils.parseInt(wReader.get("StepID"));
				wItem.ShiftID = StringUtils.parseInt(wReader.get("ShiftID"));
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				wItem.StartTime = StringUtils.parseCalendar(wReader.get("StartTime"));
				wItem.EndTime = StringUtils.parseCalendar(wReader.get("EndTime"));
				wItem.ReadyTime = StringUtils.parseCalendar(wReader.get("ReadyTime"));
				wItem.Status = StringUtils.parseInt(wReader.get("Status"));
				wItem.Active = StringUtils.parseInt(wReader.get("Active"));
				wItem.ProductNo = StringUtils.parseString(wReader.get("ProductNo"));
				wItem.PlannerID = StringUtils.parseInt(wReader.get("PlannerID"));
				wItem.TaskText = StringUtils.parseString(wReader.get("TaskText"));
				wItem.Remark = StringUtils.parseString(wReader.get("Remark"));
				wItem.APSShiftPeriod = StringUtils.parseInt(wReader.get("APSShiftPeriod"));
				/**
				 * 订单号
				 */
				wItem.OrderNo = StringUtils.parseString(wReader.get("OrderNo"));
				/**
				 * 修程名称
				 */
				wItem.LineName = APSConstans.GetFMCLineName(wItem.LineID);
				/**
				 * 工位名称
				 */
				wItem.PartName = APSConstans.GetFPCPartName(wItem.PartID);
				/**
				 * 工序名称
				 */
				wItem.StepName = APSConstans.GetFPCPartPointName(wItem.StepID);
				/**
				 * 计划员名称
				 */
				wItem.PlanerName = APSConstans.GetBMSEmployeeName(wItem.PlannerID);

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
			if (wActive != 0 && wActive != 1)
				return wResult;
			for (Integer wItem : wIDList) {
				APSTaskStepPlan wAPSTaskStepPlan = SelectByID(wLoginUser, wItem, wErrorCode);
				if (wAPSTaskStepPlan == null || wAPSTaskStepPlan.ID <= 0)
					continue;
				wAPSTaskStepPlan.Active = wActive;
				long wID = Update(wLoginUser, wAPSTaskStepPlan, wErrorCode);
				if (wID <= 0)
					break;
			}
		} catch (Exception e) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(e.toString());
		}
		return wResult;
	}

	private APSTaskStepPlanDAO() {
		super();
	}

	public static APSTaskStepPlanDAO getInstance() {
		if (Instance == null)
			Instance = new APSTaskStepPlanDAO();
		return Instance;
	}

	/**
	 * 根据工位计划ID删除工序排程计划
	 */
	public void DeleteByTaskPartID(BMSEmployee wLoginUser, int wTaskPartID, OutResult<Integer> wErrorCode) {
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return;
			}

			if (wTaskPartID <= 0)
				return;

			String wSql = MessageFormat.format("DELETE FROM {0}.aps_taskstepplan WHERE TaskPartID ={1} and ID>0;",
					wInstance.Result, String.valueOf(wTaskPartID));

			this.ExecuteSqlTransaction(wSql);
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
	}

	/**
	 * 
	 */
	public List<APSTaskStepPlan> SelectList(BMSEmployee wLoginUser, int orderID, int wPartID, List<Integer> wStepIDList,
			int wAPSShiftPeriod, OutResult<Integer> wErrorCode) {
		List<APSTaskStepPlan> wResultList = new ArrayList<APSTaskStepPlan>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = MessageFormat.format(
					"SELECT t.*,o.OrderNo FROM {0}.aps_taskstepplan t,{0}.oms_order o "
							+ "WHERE  1=1 and t.OrderID=o.ID " + "and ( :wOrderID <= 0 or :wOrderID = t.OrderID ) "
							+ "and ( :wPartID <= 0 or :wPartID = t.PartID ) "
							+ "and ( :wAPSShiftPeriod <= 0 or :wAPSShiftPeriod = t.APSShiftPeriod ) "
							+ "and ( :wStepID is null or :wStepID = '''' or t.StepID in ({1})) ;",
					wInstance.Result, wStepIDList.size() > 0 ? StringUtils.Join(",", wStepIDList) : "0");

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wOrderID", orderID);
			wParamMap.put("wPartID", wPartID);
			wParamMap.put("wAPSShiftPeriod", wAPSShiftPeriod);
			wParamMap.put("wStepID", StringUtils.Join(",", wStepIDList));

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
	 * 根据订单ID集合获取工序计划集合
	 */
	public List<APSTaskStepPlan> SelectList(BMSEmployee wLoginUser, List<Integer> wOrderIDList,
			OutResult<Integer> wErrorCode) {
		List<APSTaskStepPlan> wResultList = new ArrayList<APSTaskStepPlan>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = MessageFormat.format(
					"SELECT t.*,o.OrderNo FROM {0}.aps_taskstepplan t,{0}.oms_order o "
							+ "WHERE  1=1 and t.OrderID=o.ID  and t.APSShiftPeriod=5 "
							+ "and ( :wOrderID is null or :wOrderID = '''' or t.OrderID in ({1})) ;",
					wInstance.Result, wOrderIDList.size() > 0 ? StringUtils.Join(",", wOrderIDList) : "0");

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wOrderID", StringUtils.Join(",", wOrderIDList));

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
	 * 通过订单ID集合和工位ID集合获取工序计划集合
	 */
	public List<APSTaskStepPlan> SelectList(BMSEmployee wLoginUser, String wOrderIDs, String wPartIDs,
			int wAPSShfitPeriod, OutResult<Integer> wErrorCode) {
		List<APSTaskStepPlan> wResultList = new ArrayList<APSTaskStepPlan>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = MessageFormat.format("SELECT t.*,o.OrderNo FROM {0}.aps_taskstepplan t,{0}.oms_order o "
					+ "WHERE  1=1 and t.OrderID=o.ID " + "and (  t.OrderID in ({1}) ) " + "and (  t.PartID in ({2}) ) "
					+ "and ( :wAPSShiftPeriod <= 0 or :wAPSShiftPeriod = t.APSShiftPeriod ) ;", wInstance.Result,
					wOrderIDs, wPartIDs);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wAPSShiftPeriod", wAPSShfitPeriod);

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
	 * 根据订单，工序ID集合获取排好序的工序集合
	 */
	public List<Integer> SelectOrderMap(BMSEmployee wLoginUser, int wOrderID, String wSteps,
			OutResult<Integer> wErrorCode) {
		List<Integer> wResult = new ArrayList<Integer>();
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
					"SELECT PartPointID,OrderID FROM {2}.fpc_routepartpoint where RouteID in "
							+ "(SELECT RouteID FROM {0}.oms_order "
							+ "where ID=:OrderID) and PartPointID in ({1}) order by OrderID asc;",
					wInstance.Result, wSteps, wInstance1.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("OrderID", wOrderID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int PartPointID = StringUtils.parseInt(wReader.get("PartPointID"));
				wResult.add(PartPointID);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}
}
