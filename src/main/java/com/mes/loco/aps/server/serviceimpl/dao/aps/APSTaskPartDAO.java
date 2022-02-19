package com.mes.loco.aps.server.serviceimpl.dao.aps;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.mes.loco.aps.server.service.mesenum.APSShiftPeriod;
import com.mes.loco.aps.server.service.mesenum.APSTaskStatus;
import com.mes.loco.aps.server.service.mesenum.MESDBSource;
import com.mes.loco.aps.server.service.mesenum.MESException;
import com.mes.loco.aps.server.service.mesenum.OMSOrderStatus;
import com.mes.loco.aps.server.service.po.OutResult;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.andon.AndonTime;
import com.mes.loco.aps.server.service.po.aps.APSTaskPart;
import com.mes.loco.aps.server.service.po.aps.APSTaskPartDetails;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.fpc.FPCPart;
import com.mes.loco.aps.server.service.po.sfc.SFCTaskStepInfo;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSConstans;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSUtils;

public class APSTaskPartDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(APSTaskPartDAO.class);

	private static APSTaskPartDAO Instance = null;

	private APSTaskPartDAO() {
		super();
	}

	public static APSTaskPartDAO getInstance() {
		if (Instance == null)
			Instance = new APSTaskPartDAO();
		return Instance;
	}

	/**
	 * 添加或修改
	 * 
	 * @param wAPSTaskPart
	 * @return
	 */
	public long Update(BMSEmployee wLoginUser, APSTaskPart wAPSTaskPart, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wAPSTaskPart == null)
				return 0;

			String wSQL = "";
			if (wAPSTaskPart.getID() <= 0) {
				wSQL = StringUtils.Format("INSERT INTO {0}.aps_taskpart(OrderID,PartNo,TaskLineID,WorkShopID,"
						+ "LineID,PartID,WorkHour,CraftMinutes,ShiftID,PlanerID,ShiftPeriod,"
						+ "SubmitTime,StartTime,EndTime,ProductNo,MaterialNo,BOMNo,Priority,Active,"
						+ "TaskText,DelayHours,Status,StartWorkTime,FinishWorkTime,RouteID) VALUES(:OrderID,:PartNo,:TaskLineID,"
						+ ":WorkShopID,:LineID,:PartID,:WorkHour,:CraftMinutes,:ShiftID,"
						+ ":PlanerID,:ShiftPeriod,:SubmitTime,:StartTime,:EndTime,:ProductNo,:MaterialNo,"
						+ ":BOMNo,:Priority,:Active,:TaskText,:DelayHours,:Status,:StartWorkTime,:FinishWorkTime,:RouteID);",
						wInstance.Result);
			} else {
				wSQL = StringUtils.Format("UPDATE {0}.aps_taskpart SET OrderID = :OrderID,PartNo = :PartNo,"
						+ "TaskLineID = :TaskLineID,WorkShopID = :WorkShopID,LineID = :LineID,"
						+ "PartID = :PartID,WorkHour = :WorkHour,CraftMinutes = :CraftMinutes,"
						+ "ShiftID = :ShiftID,PlanerID = :PlanerID,ShiftPeriod = :ShiftPeriod,"
						+ "SubmitTime = :SubmitTime,StartTime=:StartTime,EndTime = :EndTime,ProductNo = :ProductNo,"
						+ "MaterialNo = :MaterialNo,BOMNo = :BOMNo,Priority = :Priority,"
						+ "Active = :Active,TaskText = :TaskText,DelayHours = :DelayHours,"
						+ "Status = :Status,StartWorkTime=:StartWorkTime,FinishWorkTime=:FinishWorkTime,"
						+ "RouteID=:RouteID WHERE ID = :ID;", wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wAPSTaskPart.ID);
			wParamMap.put("OrderID", wAPSTaskPart.OrderID);
			wParamMap.put("PartNo", wAPSTaskPart.PartNo);
			wParamMap.put("TaskLineID", wAPSTaskPart.TaskLineID);
			wParamMap.put("WorkShopID", wAPSTaskPart.WorkShopID);
			wParamMap.put("LineID", wAPSTaskPart.LineID);
			wParamMap.put("PartID", wAPSTaskPart.PartID);
			wParamMap.put("WorkHour", wAPSTaskPart.WorkHour);
			wParamMap.put("CraftMinutes", wAPSTaskPart.CraftMinutes);
			wParamMap.put("ShiftID", wAPSTaskPart.ShiftID);
			wParamMap.put("PlanerID", wAPSTaskPart.PlanerID);
			wParamMap.put("ShiftPeriod", wAPSTaskPart.ShiftPeriod);
			wParamMap.put("SubmitTime", wAPSTaskPart.SubmitTime);
			wParamMap.put("StartTime", wAPSTaskPart.StartTime);
			wParamMap.put("EndTime", wAPSTaskPart.EndTime);
			wParamMap.put("ProductNo", wAPSTaskPart.ProductNo);
			wParamMap.put("MaterialNo", wAPSTaskPart.MaterialNo);
			wParamMap.put("BOMNo", wAPSTaskPart.BOMNo);
			wParamMap.put("Priority", wAPSTaskPart.Priority);
			wParamMap.put("Active", wAPSTaskPart.Active);
			wParamMap.put("TaskText", wAPSTaskPart.TaskText);
			wParamMap.put("DelayHours", wAPSTaskPart.DelayHours);
			wParamMap.put("Status", wAPSTaskPart.Status);
			wParamMap.put("StartWorkTime", wAPSTaskPart.StartWorkTime);
			wParamMap.put("FinishWorkTime", wAPSTaskPart.FinishWorkTime);
			wParamMap.put("RouteID", wAPSTaskPart.RouteID);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wAPSTaskPart.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wAPSTaskPart.setID(wResult);
			} else {
				wResult = wAPSTaskPart.getID();
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
	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<APSTaskPart> wList,
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
			for (APSTaskPart wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = StringUtils.Format("delete from {1}.aps_taskpart WHERE ID IN({0}) ;",
					String.join(",", wIDList), wInstance.Result);
			this.ExecuteSqlTransaction(wSql);
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResult;
	}

	public void DeleteIDList(BMSEmployee wLoginUser, List<Integer> wIDList, OutResult<Integer> wErrorCode) {
		try {
			if (wIDList == null || wIDList.size() <= 0) {
				return;
			}

			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return;
			}

			String wSql = StringUtils.Format("update {1}.aps_taskpart set Active=2 WHERE ID IN({0}) ;",
					StringUtils.Join(",", wIDList), wInstance.Result);
			this.ExecuteSqlTransaction(wSql);
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
	}

	/**
	 * 查单条
	 * 
	 * @return
	 */
	public APSTaskPart SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		APSTaskPart wResult = new APSTaskPart();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<APSTaskPart> wList = SelectList(wLoginUser, wID, -1, -1, -1, -1, -1, -1, null, -1, null, null,
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

	public List<APSTaskPart> SelectListByOrderIDList(BMSEmployee wLoginUser, int wShiftPeriod,
			List<Integer> wStatusIDList, List<Integer> wOrderIDList, OutResult<Integer> wErrorCode) {
		List<APSTaskPart> wResult = new ArrayList<APSTaskPart>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wStatusIDList == null) {
				wStatusIDList = new ArrayList<Integer>();
			}
			if (wOrderIDList == null) {
				wOrderIDList = new ArrayList<Integer>();
			}
			wStatusIDList.removeIf(p -> p < 0);
			wOrderIDList.removeIf(p -> p <= 0);

			String wSQL = StringUtils.Format(
					"SELECT t.*,o.OrderNo,o.BureauSectionID FROM {0}.aps_taskpart t"
							+ " left join {0}.oms_order o on t.OrderID=o.ID   WHERE t.Active=1   "
							+ " and ( :wShiftPeriod <=0  or t.ShiftPeriod=:wShiftPeriod)"
							+ " and ( :wStatus = '''' or t.Status in ({1}))"
							+ " and ( :wOrderID = '''' or t.OrderID in ({2}));",
					wInstance.Result, wStatusIDList.size() > 0 ? StringUtils.Join(",", wStatusIDList) : "0",
					wOrderIDList.size() > 0 ? StringUtils.Join(",", wOrderIDList) : "0");

			Map<String, Object> wParamMap = new HashMap<String, Object>();
			wParamMap.put("wShiftPeriod", wShiftPeriod);
			wParamMap.put("wStatus", StringUtils.Join(",", wStatusIDList));
			wParamMap.put("wOrderID", StringUtils.Join(",", wOrderIDList));

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetValue(wResult, wQueryResult);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	public List<APSTaskPart> SelectListByIDList(BMSEmployee wLoginUser, List<Integer> wIDList,
			List<Integer> wStateIDList, OutResult<Integer> wErrorCode) {
		List<APSTaskPart> wResult = new ArrayList<APSTaskPart>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wStateIDList == null) {
				wStateIDList = new ArrayList<Integer>();
			}

			if (wIDList == null) {
				wIDList = new ArrayList<Integer>();
			}

			String wSQL = StringUtils.Format(
					"SELECT t.*,o.OrderNo,o.BureauSectionID FROM {0}.aps_taskpart t left join {0}.oms_order o on t.OrderID=o.ID  WHERE  1=1  "
							+ "and ( :wID = '''' or t.ID in ({1})) " + "and ( :wStatus = '''' or t.Status in ({2}));",
					wInstance.Result, wIDList.size() > 0 ? StringUtils.Join(",", wIDList) : "0",
					wStateIDList.size() > 0 ? StringUtils.Join(",", wStateIDList) : "0");

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", StringUtils.Join(",", wIDList));
			wParamMap.put("wStatus", StringUtils.Join(",", wStateIDList));

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetValue(wResult, wQueryResult);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 条件查询集合
	 */
	public List<APSTaskPart> SelectList(BMSEmployee wLoginUser, int wID, int wOrderID, int wTaskLineID, int wLineID,
			int wPartID, int wActive, int wShiftPeriod, List<Integer> wStateIDList, int wShiftID, Calendar wStartTime,
			Calendar wEndTime, OutResult<Integer> wErrorCode) {
		List<APSTaskPart> wResultList = new ArrayList<APSTaskPart>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			if (wStateIDList == null)
				wStateIDList = new ArrayList<Integer>();

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1);
			if (wStartTime == null || wStartTime.compareTo(wBaseTime) < 0)
				wStartTime = wBaseTime;
			if (wEndTime == null || wEndTime.compareTo(wBaseTime) < 0)
				wEndTime = wBaseTime;
			if (wStartTime.compareTo(wEndTime) > 0)
				return wResultList;

			String wSQL = StringUtils.Format(
					"SELECT t.*,o.OrderNo,o.BureauSectionID FROM {0}.aps_taskpart t  left join {0}.oms_order o on t.OrderID=o.ID WHERE  1=1  "
							+ "and ( :wID <= 0 or :wID = t.ID ) " + "and ( :wOrderID <= 0 or :wOrderID = t.OrderID ) "
							+ "and ( :wTaskLineID <= 0 or :wTaskLineID = t.TaskLineID ) "
							+ "and ( :wLineID <= 0 or :wLineID = t.LineID ) "
							+ "and ( :wPartID <= 0 or :wPartID = t.PartID ) "
							+ "and ( :wActive < 0 or :wActive = t.Active ) "
							+ "and ( :wShiftID <= 0 or :wShiftID = t.ShiftID ) "
							+ "and ( :wShiftPeriod <= 0 or :wShiftPeriod = t.ShiftPeriod ) "
							+ "and ( :wStartTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wStartTime <= t.EndTime) "
							+ "and ( :wEndTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wEndTime >= t.StartTime) "
							+ "and ( :wStatus = '''' or t.Status in ({1}));",
					wInstance.Result, wStateIDList.size() > 0 ? StringUtils.Join(",", wStateIDList) : "0");

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wOrderID", wOrderID);
			wParamMap.put("wTaskLineID", wTaskLineID);
			wParamMap.put("wLineID", wLineID);
			wParamMap.put("wPartID", wPartID);
			wParamMap.put("wActive", wActive);
			wParamMap.put("wShiftID", wShiftID);
			wParamMap.put("wShiftPeriod", wShiftPeriod);
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
	 * 根据订单和工位列表查询工位任务集合(转向架互换)
	 */
	public List<APSTaskPart> SelectList(BMSEmployee wLoginUser, int wID, int wOrderID, int wTaskLineID, int wLineID,
			int wPartID, int wActive, int wShiftPeriod, List<Integer> wStateIDList, int wShiftID, Calendar wStartTime,
			Calendar wEndTime, List<Integer> wPartIDList, OutResult<Integer> wErrorCode) {
		List<APSTaskPart> wResultList = new ArrayList<APSTaskPart>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			if (wStateIDList == null)
				wStateIDList = new ArrayList<Integer>();
			if (wPartIDList == null)
				wPartIDList = new ArrayList<Integer>();

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1);
			if (wStartTime == null || wStartTime.compareTo(wBaseTime) < 0)
				wStartTime = wBaseTime;
			if (wEndTime == null || wEndTime.compareTo(wBaseTime) < 0)
				wEndTime = wBaseTime;
			if (wStartTime.compareTo(wEndTime) > 0)
				return wResultList;

			String wSQL = StringUtils.Format(
					"SELECT t.*,o.OrderNo,o.BureauSectionID FROM {0}.aps_taskpart t  left join {0}.oms_order o on t.OrderID=o.ID WHERE  1=1  "
							+ "and ( :wID <= 0 or :wID = t.ID ) " + "and ( :wOrderID <= 0 or :wOrderID = t.OrderID ) "
							+ "and ( :wTaskLineID <= 0 or :wTaskLineID = t.TaskLineID ) "
							+ "and ( :wLineID <= 0 or :wLineID = t.LineID ) "
							+ "and ( :wPartID <= 0 or :wPartID = t.PartID ) "
							+ "and ( :wActive < 0 or :wActive = t.Active ) "
							+ "and ( :wShiftID <= 0 or :wShiftID = t.ShiftID ) "
							+ "and ( :wShiftPeriod <= 0 or :wShiftPeriod = t.ShiftPeriod ) "
							+ "and ( :wStartTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wStartTime <= t.EndTime) "
							+ "and ( :wEndTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wEndTime >= t.StartTime) "
							+ "and ( :wPart = '''' or t.PartID in ({2})) "
							+ "and ( :wStatus = '''' or t.Status in ({1}));",
					wInstance.Result, wStateIDList.size() > 0 ? StringUtils.Join(",", wStateIDList) : "0",
					wPartIDList.size() > 0 ? StringUtils.Join(",", wPartIDList) : "0");

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wOrderID", wOrderID);
			wParamMap.put("wTaskLineID", wTaskLineID);
			wParamMap.put("wLineID", wLineID);
			wParamMap.put("wPartID", wPartID);
			wParamMap.put("wActive", wActive);
			wParamMap.put("wShiftID", wShiftID);
			wParamMap.put("wShiftPeriod", wShiftPeriod);
			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);
			wParamMap.put("wStatus", StringUtils.Join(",", wStateIDList));
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

	public List<APSTaskPart> SelectListByIDList(BMSEmployee wLoginUser, List<Integer> wIDList,
			OutResult<Integer> wErrorCode) {
		List<APSTaskPart> wResult = new ArrayList<APSTaskPart>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wIDList == null) {
				wIDList = new ArrayList<Integer>();
			}

			String wSQL = StringUtils.Format(
					"SELECT t.*,o.OrderNo,o.BureauSectionID FROM {0}.aps_taskpart t left join {0}.oms_order o on t.OrderID=o.ID  WHERE  1=1  "
							+ "and ( :wID = '''' or t.ID in ({1}));",
					wInstance.Result, wIDList.size() > 0 ? StringUtils.Join(",", wIDList) : "0");

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", StringUtils.Join(",", wIDList));

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetValue(wResult, wQueryResult);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	private void SetValue(List<APSTaskPart> wResult, List<Map<String, Object>> wQueryResult) {
		try {
			for (Map<String, Object> wReader : wQueryResult) {
				APSTaskPart wItem = new APSTaskPart();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.OrderID = StringUtils.parseInt(wReader.get("OrderID"));
				wItem.PartNo = StringUtils.parseString(wReader.get("PartNo"));
				wItem.TaskLineID = StringUtils.parseInt(wReader.get("TaskLineID"));
				wItem.WorkShopID = StringUtils.parseInt(wReader.get("WorkShopID"));
				wItem.LineID = StringUtils.parseInt(wReader.get("LineID"));
				wItem.PartID = StringUtils.parseInt(wReader.get("PartID"));
				wItem.WorkHour = StringUtils.parseDouble(wReader.get("WorkHour"));
				wItem.CraftMinutes = StringUtils.parseInt(wReader.get("CraftMinutes"));
				wItem.ShiftID = StringUtils.parseInt(wReader.get("ShiftID"));
				wItem.PlanerID = StringUtils.parseInt(wReader.get("PlanerID"));
				wItem.ShiftPeriod = StringUtils.parseInt(wReader.get("ShiftPeriod"));
				wItem.SubmitTime = StringUtils.parseCalendar(wReader.get("SubmitTime"));
				wItem.StartTime = StringUtils.parseCalendar(wReader.get("StartTime"));
				wItem.EndTime = StringUtils.parseCalendar(wReader.get("EndTime"));
				wItem.StartWorkTime = StringUtils.parseCalendar(wReader.get("StartWorkTime"));
				wItem.FinishWorkTime = StringUtils.parseCalendar(wReader.get("FinishWorkTime"));
				wItem.ProductNo = StringUtils.parseString(wReader.get("ProductNo"));
				wItem.MaterialNo = StringUtils.parseString(wReader.get("MaterialNo"));
				wItem.BOMNo = StringUtils.parseString(wReader.get("BOMNo"));
				wItem.Priority = StringUtils.parseInt(wReader.get("Priority"));
				wItem.Active = StringUtils.parseInt(wReader.get("Active"));
				wItem.TaskText = StringUtils.parseString(wReader.get("TaskText"));
				wItem.DelayHours = StringUtils.parseDouble(wReader.get("DelayHours"));
				wItem.Status = StringUtils.parseInt(wReader.get("Status"));
				wItem.RouteID = StringUtils.parseInt(wReader.get("RouteID"));

				if (wReader.containsKey("BureauSectionID")) {
					wItem.CustomerID = StringUtils.parseInt(wReader.get("BureauSectionID"));
					wItem.CustomerName = APSConstans.GetCRMCustomerName(wItem.CustomerID);
				}

				wItem.LineName = APSConstans.GetFMCLineName(wItem.LineID);
				wItem.PartName = APSConstans.GetFPCPartName(wItem.PartID);
				wItem.PlanerName = APSConstans.GetBMSEmployeeName(wItem.PlanerID);
				wItem.StatusText = APSTaskStatus.getEnumType(wItem.Status).getLable();
				if (wReader.containsKey("OrderNo"))
					wItem.OrderNo = StringUtils.parseString(wReader.get("OrderNo"));

				wResult.add(wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 条件查询完工的工位计划集合
	 * 
	 * @return
	 */
	public List<APSTaskPart> SelectFinishedList(BMSEmployee wLoginUser, OutResult<Integer> wErrorCode) {
		List<APSTaskPart> wResultList = new ArrayList<APSTaskPart>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = StringUtils.Format(
					"SELECT t1.*,t2.OrderNo FROM {0}.aps_taskpart t1,{0}.oms_order t2 "
							+ "where t1.OrderID=t2.ID t1.Status={1} and t2.Status ={2} and t1.ShiftPeriod={3};",
					wInstance.Result, APSTaskStatus.Done.getValue(), OMSOrderStatus.Repairing.getValue(),
					APSShiftPeriod.Week.getValue());

			Map<String, Object> wParamMap = new HashMap<String, Object>();
			wSQL = this.DMLChange(wSQL);
			wResultList = this.QueryForList(wSQL, wParamMap, APSTaskPart.class);

			SetText(wResultList);
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	private void SetText(List<APSTaskPart> wResultList) {
		try {
			if (wResultList == null || wResultList.size() <= 0) {
				return;
			}

			for (APSTaskPart wAPSTaskPart : wResultList) {
				wAPSTaskPart.LineName = APSConstans.GetFMCLineName(wAPSTaskPart.LineID);
				wAPSTaskPart.PartName = APSConstans.GetFPCPartName(wAPSTaskPart.PartID);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 根据ShiftID 查询工位任务
	 * 
	 * @return
	 */
	public List<APSTaskPart> SelectListByShiftID(BMSEmployee wLoginUser, int wShiftID, OutResult<Integer> wErrorCode) {
		List<APSTaskPart> wResultList = new ArrayList<APSTaskPart>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = StringUtils.Format("SELECT * FROM {0}.aps_taskpart WHERE  ShiftID=:wShiftID;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wShiftID", wShiftID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				APSTaskPart wItem = new APSTaskPart();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.OrderID = StringUtils.parseInt(wReader.get("OrderID"));
				wItem.PartNo = StringUtils.parseString(wReader.get("PartNo"));
				wItem.TaskLineID = StringUtils.parseInt(wReader.get("TaskLineID"));
				wItem.WorkShopID = StringUtils.parseInt(wReader.get("WorkShopID"));
				wItem.LineID = StringUtils.parseInt(wReader.get("LineID"));
				wItem.PartID = StringUtils.parseInt(wReader.get("PartID"));
				wItem.WorkHour = StringUtils.parseDouble(wReader.get("WorkHour"));
				wItem.CraftMinutes = StringUtils.parseInt(wReader.get("CraftMinutes"));
				wItem.ShiftID = StringUtils.parseInt(wReader.get("ShiftID"));
				wItem.PlanerID = StringUtils.parseInt(wReader.get("PlanerID"));
				wItem.ShiftPeriod = StringUtils.parseInt(wReader.get("ShiftPeriod"));
				wItem.SubmitTime = StringUtils.parseCalendar(wReader.get("SubmitTime"));
				wItem.StartTime = StringUtils.parseCalendar(wReader.get("StartTime"));
				wItem.EndTime = StringUtils.parseCalendar(wReader.get("EndTime"));
				wItem.StartWorkTime = StringUtils.parseCalendar(wReader.get("StartWorkTime"));
				wItem.FinishWorkTime = StringUtils.parseCalendar(wReader.get("FinishWorkTime"));
				wItem.ProductNo = StringUtils.parseString(wReader.get("ProductNo"));
				wItem.MaterialNo = StringUtils.parseString(wReader.get("MaterialNo"));
				wItem.BOMNo = StringUtils.parseString(wReader.get("BOMNo"));
				wItem.Priority = StringUtils.parseInt(wReader.get("Priority"));
				wItem.Active = StringUtils.parseInt(wReader.get("Active"));
				wItem.TaskText = StringUtils.parseString(wReader.get("TaskText"));
				wItem.DelayHours = StringUtils.parseDouble(wReader.get("DelayHours"));
				wItem.Status = StringUtils.parseInt(wReader.get("Status"));
				wItem.RouteID = StringUtils.parseInt(wReader.get("RouteID"));

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
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
				APSTaskPart wAPSTaskPart = SelectByID(wLoginUser, wItem, wErrorCode);
				if (wAPSTaskPart == null || wAPSTaskPart.ID <= 0)
					continue;
				// 只有激活的才能禁用
				if (wActive == 2 && wAPSTaskPart.Active != 1) {
					wErrorCode.set(MESException.Logic.getValue());
					return wResult;
				}
				wAPSTaskPart.Active = wActive;
				if (wActive == 0) {
					wAPSTaskPart.Status = APSTaskStatus.Canceled.getValue();
				}
				long wID = Update(wLoginUser, wAPSTaskPart, wErrorCode);
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
	 * 批量修改状态
	 * 
	 */
	public void Update(BMSEmployee wLoginUser, List<Integer> wTaskPartIDList, int wStatus,
			OutResult<Integer> wErrorCode) {
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.APS, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return;
			}

			if (wTaskPartIDList == null) {
				wTaskPartIDList = new ArrayList<Integer>();
			}

			String wSQL = StringUtils.Format("update {0}.aps_taskpart set Status={1} where ID in({2});",
					wInstance.Result, wStatus,
					wTaskPartIDList.size() > 0 ? StringUtils.Join(",", wTaskPartIDList) : "0");

			wSQL = this.DMLChange(wSQL);

			ExecuteSqlTransaction(wSQL);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 通过时间段查询工位任务
	 * 
	 * @return
	 */
	public List<APSTaskPart> SelectListByTime(BMSEmployee wLoginUser, List<Integer> wPartIDList, Calendar wStartTime,
			Calendar wEndTime, OutResult<Integer> wErrorCode) {
		List<APSTaskPart> wResult = new ArrayList<APSTaskPart>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wPartIDList == null || wPartIDList.size() <= 0) {
				wPartIDList = new ArrayList<Integer>();
			}

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1);
			if (wStartTime == null || wStartTime.compareTo(wBaseTime) < 0)
				wStartTime = wBaseTime;
			if (wEndTime == null || wEndTime.compareTo(wBaseTime) < 0)
				wEndTime = wBaseTime;
			if (wStartTime.compareTo(wEndTime) > 0)
				return wResult;

			String wSQL = StringUtils.Format(
					"SELECT t.*,o.OrderNo,o.BureauSectionID FROM {0}.aps_taskpart  t left join {0}.oms_order o on t.OrderID=o.ID WHERE 1=1  "
							+ "and (( t.Status in (5) AND :wStartTime < t.FinishWorkTime AND :wEndTime > t.SubmitTime ) "
							+ "OR ( t.Status IN (2,4) )) " + "and ( :wPartID = '''' OR t.PartID IN ({1}));",
					wInstance.Result, wPartIDList.size() > 0 ? StringUtils.Join(",", wPartIDList) : "0");

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);
			wParamMap.put("wPartID", StringUtils.Join(",", wPartIDList));

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetValue(wResult, wQueryResult);
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 查询今日待完工工位任务
	 * 
	 * @param wLoginUser
	 * @return
	 */
	public int SelectTodayToDoList(BMSEmployee wLoginUser, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			// 今日时间
			Calendar wTodayETime = Calendar.getInstance();
			wTodayETime.set(Calendar.HOUR_OF_DAY, 23);
			wTodayETime.set(Calendar.MINUTE, 59);
			wTodayETime.set(Calendar.SECOND, 59);

			String wSQL = StringUtils.Format(
					"SELECT Count(*) as Number FROM {0}.aps_taskpart WHERE StartTime<=:wToday AND Status in(2,3,4) AND ShiftPeriod={1} and Active=1;",
					wInstance.Result, APSShiftPeriod.Week.getValue());

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wToday", wTodayETime);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wMap : wQueryResult) {
				wResult = StringUtils.parseInt(wMap.get("Number"));
				return wResult;
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 判断订单列表是否排程了月计划
	 * 
	 * @param wLoginUser
	 * @param wOrderList
	 * @param wErrorCode
	 * @return
	 */
	public String APS_CheckIsMakedMonthPlan(BMSEmployee wLoginUser, List<Integer> wOrderList,
			OutResult<Integer> wErrorCode) {
		String wResult = "";
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.APS, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wOrderList == null) {
				wOrderList = new ArrayList<Integer>();
			}

			String wSQL = StringUtils.Format(
					"SELECT t1.PartNo,t2.ID as TaskPartID " + "FROM {0}.oms_order t1 LEFT JOIN {0}.aps_taskpart t2 "
							+ "ON t1.ID=t2.OrderID AND t2.ShiftPeriod=:MonthPeriod and t2.Status=2 "
							+ "WHERE t1.ID in({1});",
					wInstance.Result, wOrderList.size() > 0 ? StringUtils.Join(",", wOrderList) : "0");

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("MonthPeriod", APSShiftPeriod.Month.getValue());

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			String wPartNo = "";
			for (Map<String, Object> wReader : wQueryResult) {
				String wNo = StringUtils.parseString(wReader.get("PartNo"));
				int wTaskPartID = StringUtils.parseInt(wReader.get("TaskPartID"));
				if (wTaskPartID <= 0) {
					wPartNo = wNo;
					break;
				}
			}
			if (StringUtils.isNotEmpty(wPartNo)) {
				wResult = StringUtils.Format("提示：【{0}】该台车月计划未排或未下达，无法排程周计划!", wPartNo);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据订单找到所有的排了日计划的周计划
	 */
	public List<APSTaskPart> SelectWeekPlanByOrderID(BMSEmployee wLoginUser, int wOrderID,
			OutResult<Integer> wErrorCode) {
		List<APSTaskPart> wResultList = new ArrayList<APSTaskPart>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = StringUtils.Format(
					"SELECT t.*,o.OrderNo,o.BureauSectionID FROM {0}.aps_taskpart  t left join {0}.oms_order o on t.OrderID=o.ID where t.OrderID=:wOrderID "
							+ "and t.ID in (Select distinct TaskPartID from {0}.aps_taskstep where t.OrderID=:wOrderID);",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wOrderID", wOrderID);

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
	 * 根据工位ID集合和订单ID获取所有工位计划列表
	 */
	public List<APSTaskPart> SelectListByPartIDList(BMSEmployee wLoginUser, int wOrderID, List<Integer> wPartIDList,
			OutResult<Integer> wErrorCode) {
		List<APSTaskPart> wResultList = new ArrayList<APSTaskPart>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			if (wPartIDList == null) {
				wPartIDList = new ArrayList<Integer>();
			}

			String wSQL = StringUtils.Format(
					"SELECT t.*,o.OrderNo,o.BureauSectionID FROM {0}.aps_taskpart  t left join {0}.oms_order o on t.OrderID=o.ID where 1=1 "
							+ "and ( :wOrderID <= 0 or :wOrderID =  t.OrderID ) "
							+ "and ( :wPartID = '''' or t.PartID in ({1}));",
					wInstance.Result, wPartIDList.size() > 0 ? StringUtils.Join(",", wPartIDList) : "0");

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wOrderID", wOrderID);
			wParamMap.put("wPartID", StringUtils.Join(",", wPartIDList));

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
	 * 获取派工的工位列表
	 */
	public List<APSTaskPart> SelectDispatchList(BMSEmployee wLoginUser, Calendar wStartTime, Calendar wEndTime,
			boolean wIsAll, OutResult<Integer> wErrorCode) {
		List<APSTaskPart> wResultList = new ArrayList<APSTaskPart>();
		try {
			ServiceResult<String> wAPSInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wAPSInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = StringUtils.Format(
					"SELECT t1.*,o.OrderNo,o.BureauSectionID FROM {0}.aps_taskpart t1 "
							+ " inner join  {1}.bms_workcharge t2 on t2.StationID=t1.PartID and t2.Active=1 "
							+ " inner join  {1}.mbs_user t3 on t3.DepartmentID=t2.ClassID and t3.ID=:LoginID "
							+ " inner join  {0}.aps_taskstep t4  on  t4.TaskPartID=t1.ID and t4.Status in(2,4) "
							+ " left join {0}.oms_order o on t1.OrderID=o.ID  where  t1.ShiftPeriod=5 and t1.Active=1 "
							+ " and (  (:StartTime<t1.FinishWorkTime and :EndTime>t1.StartWorkTime "
							+ " and t1.Status in (5)) or (t1.Status in(2,3,4)) ) group by t1.ID "
							+ " order by t1.Status asc,t1.PartNo asc,t1.PartID asc;",
					wAPSInstance.Result, wInstance.Result);
			if (wIsAll) {
				wSQL = StringUtils.Format("SELECT t1.*,o.OrderNo,o.BureauSectionID  FROM {0}.aps_taskpart t1"
						+ " inner join {0}.aps_taskstep t4 on t4.TaskPartID=t1.ID and t4.Status in(2,4)"
						+ " left  join {0}.oms_order o on t1.OrderID=o.ID  "
						+ " where t1.ShiftPeriod=5 and t1.Active=1 "
						+ " and ( (:StartTime<t1.FinishWorkTime  and :EndTime>t1.StartWorkTime "
						+ " and t1.Status in (5)) or (t1.Status in(2,3,4)) ) group by t1.ID "
						+ " order by t1.Status asc,t1.PartNo asc,t1.PartID asc;", wAPSInstance.Result);
			}

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
	 * 根据工位任务ID获取待做和已做数量
	 */
	public SFCTaskStepInfo SelectToDoAndDone(BMSEmployee wLoginUser, int wTaskPartID, OutResult<Integer> wErrorCode) {
		SFCTaskStepInfo wResult = new SFCTaskStepInfo();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"select (SELECT count(*)  from {0}.aps_taskstep where TaskPartID=:TaskPartID and IsDispatched=0 and Active=1) as ToDo,"
							+ "(SELECT count(*)  from {0}.aps_taskstep where TaskPartID=:TaskPartID and IsDispatched=1 and Active=1) as Done;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("TaskPartID", wTaskPartID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wMap : wQueryResult) {
				int wToDo = StringUtils.parseInt(wMap.get("ToDo"));
				int wDone = StringUtils.parseInt(wMap.get("Done"));
				wResult.ToDispatch = wToDo;
				wResult.Dispatched = wDone;
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 判断该工位任务的日计划是否已经下达了
	 */
	public boolean IsIssueDayPlan(BMSEmployee wLoginUser, int wTaskPartID, OutResult<Integer> wErrorCode) {
		boolean wResult = false;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"SELECT count(*) as Number FROM {0}.aps_taskstep where TaskPartID=:TaskPartID and Status in(2,4,5);",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("TaskPartID", wTaskPartID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wMap : wQueryResult) {
				int wNumber = StringUtils.parseInt(wMap.get("Number"));
				if (wNumber == 0) {
					return false;
				} else {
					return true;
				}
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据订单集合和时间查询月计划或周计划记录
	 */
	public List<APSTaskPart> SelectListByOrderListAndTime(BMSEmployee wLoginUser, List<Integer> wOrderIDList,
			Calendar wStartTime, Calendar wEndTime, int wAPSShiftPeriod, OutResult<Integer> wErrorCode) {
		List<APSTaskPart> wResult = new ArrayList<APSTaskPart>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wOrderIDList == null) {
				wOrderIDList = new ArrayList<Integer>();
			}

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1);
			if (wStartTime == null || wStartTime.compareTo(wBaseTime) < 0)
				wStartTime = wBaseTime;
			if (wEndTime == null || wEndTime.compareTo(wBaseTime) < 0)
				wEndTime = wBaseTime;
			if (wStartTime.compareTo(wEndTime) > 0)
				return wResult;

			String wSQL = StringUtils.Format(
					"SELECT t.*,o.OrderNo,o.BureauSectionID FROM {0}.aps_taskpart t left join {0}.oms_order o on t.OrderID=o.ID WHERE t.Active=1  "
							+ "and ( :wShiftPeriod <= 0 or :wShiftPeriod = t.ShiftPeriod )  "
							+ "and ( :wStartTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wStartTime <= t.EndTime) "
							+ "and ( :wEndTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wEndTime >= t.StartTime) "
							+ "and ( :wOrderID = '''' or t.OrderID in ({1}));",
					wInstance.Result, wOrderIDList.size() > 0 ? StringUtils.Join(",", wOrderIDList) : "0");

			Map<String, Object> wParamMap = new HashMap<String, Object>();
			wParamMap.put("wShiftPeriod", wAPSShiftPeriod);
			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);
			wParamMap.put("wOrderID", StringUtils.Join(",", wOrderIDList));

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetValue(wResult, wQueryResult);
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取工位任务详情
	 */
	public List<APSTaskPartDetails> SelectAPSTaskPartDetailsList(BMSEmployee wLoginUser, Calendar wShiftEnd,
			OutResult<Integer> wErrorCode) {
		List<APSTaskPartDetails> wResult = new ArrayList<APSTaskPartDetails>();
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
					"SELECT (SELECT count(*)  FROM {0}.aps_taskstep where TaskPartID=t1.ID) StepSize,"
							+ "(SELECT count(*)  FROM {0}.aps_taskstep where TaskPartID=t1.ID "
							+ "and Status=5) StepFinish,"
							+ "(SELECT count(*)  FROM {0}.aps_taskstep where TaskPartID=t1.ID "
							+ "and Status in(2,4,5)) StepSchedule,"
							+ "(SELECT count(*)  FROM {0}.aps_taskstep where TaskPartID=1 and "
							+ "Status in(8,9,10)) StepMaking,"
							+ "t2.WorkAreaID AreaID,t3.Name AreaName,t1.PartNo,t1.OrderID,t1.PartID,t2.OrderNum,"
							+ "t1.ID TaskPartID FROM {0}.aps_taskpart t1,{2}.lfs_workareastation t2,"
							+ "{1}.bms_department t3 where t3.ID=t2.WorkAreaID and t1.PartID=t2.StationID "
							+ "and t2.Active=1 and t1.ID in (SELECT distinct(TaskPartID) FROM {0}.aps_taskstep "
							+ "where Status=1 and :EndTime>PlanStartTime and TaskPartID in(SELECT distinct(ID) as TaskPartID FROM "
							+ "{0}.aps_taskpart where Status in(2,3,4)  "
							+ "and ShiftPeriod=5 and Active=1 and PartID in (SELECT distinct(StationID) FROM "
							+ "{2}.lfs_workareastation where WorkAreaID in(SELECT ID FROM {1}.bms_department "
							+ "where ID in(SELECT DepartmentID FROM {1}.mbs_user where ID=:UserID) and Type=2 "
							+ "and 1 in(SELECT count(*) FROM {1}.bms_position where ID in(SELECT Position "
							+ "FROM {1}.mbs_user where ID=:UserID) and DutyID=2)) and Active=1) and Active=1));",
					wInstance.Result, wInstance1.Result, wInstance2.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();
			wParamMap.put("UserID", wLoginUser.ID);
			wParamMap.put("EndTime", wShiftEnd);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wMap : wQueryResult) {
				APSTaskPartDetails wAPSTaskPartDetails = new APSTaskPartDetails();

				wAPSTaskPartDetails.APSTaskPart = new APSTaskPart();
				wAPSTaskPartDetails.APSTaskPart.ID = StringUtils.parseInt(wMap.get("TaskPartID"));
				wAPSTaskPartDetails.AreaID = StringUtils.parseInt(wMap.get("AreaID"));
				wAPSTaskPartDetails.AreaName = StringUtils.parseString(wMap.get("AreaName"));
				wAPSTaskPartDetails.OrderID = StringUtils.parseInt(wMap.get("OrderID"));
				wAPSTaskPartDetails.OrderNum = StringUtils.parseInt(wMap.get("OrderNum"));
				wAPSTaskPartDetails.PartID = StringUtils.parseInt(wMap.get("PartID"));
				wAPSTaskPartDetails.PartNo = StringUtils.parseString(wMap.get("PartNo"));
				wAPSTaskPartDetails.StepFinish = StringUtils.parseInt(wMap.get("StepFinish"));
				wAPSTaskPartDetails.StepMaking = StringUtils.parseInt(wMap.get("StepMaking"));
				wAPSTaskPartDetails.StepSchedule = StringUtils.parseInt(wMap.get("StepSchedule"));
				wAPSTaskPartDetails.StepSize = StringUtils.parseInt(wMap.get("StepSize"));

				wResult.add(wAPSTaskPartDetails);
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 获取工序任务已完成但工位任务状态未完成的工位任务
	 */
	public List<APSTaskPart> APS_QueryFinishedTaskPartList(BMSEmployee wLoginUser, OutResult<Integer> wErrorCode) {
		List<APSTaskPart> wResult = new ArrayList<APSTaskPart>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("select t1.*,o.OrderNo,o.BureauSectionID from {0}.aps_taskpart t1"
					+ " left join {0}.oms_order o on t1.OrderID=o.ID where 1=1 "
					+ " and (select count(*) from {0}.aps_taskstep where TaskPartID=t1.ID) >0"
					+ " and (select count(*) from {0}.aps_taskstep where TaskPartID=t1.ID)"
					+ "   = (select count(*) from {0}.aps_taskstep p1 where p1.TaskPartID=t1.ID and p1.Status =5)"
					+ " and t1.Status !=5;", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetValue(wResult, wQueryResult);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 查询订单实际时间
	 */
	public List<AndonTime> APS_QueryAndonTime(BMSEmployee wLoginUser, List<Integer> wOrderIDList,
			OutResult<Integer> wErrorCode) {
		List<AndonTime> wResult = new ArrayList<AndonTime>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wOrderIDList == null)
				wOrderIDList = new ArrayList<Integer>();

			String wSQL = StringUtils.Format(
					"SELECT t1.OrderID,t1.FinishWorkTime,t2.RealReceiveDate "
							+ "FROM {0}.aps_taskpart t1,{0}.oms_order t2 " + "where t1.OrderID=t2.ID and t1.PartID=103 "
							+ "and t1.ShiftPeriod=5 and t1.Active=1 and t1.OrderID in ({1});",
					wInstance.Result, wOrderIDList.size() > 0 ? StringUtils.Join(",", wOrderIDList) : "0");

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2010, 0, 1, 0, 0, 0);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);
			for (Map<String, Object> wReader : wQueryResult) {
				AndonTime wItem = new AndonTime();
				wItem.OrderID = StringUtils.parseInt(wReader.get("OrderID"));
				wItem.RealReceiveDate = StringUtils.parseCalendar(wReader.get("RealReceiveDate"));
				wItem.FinishWorkTime = StringUtils.parseCalendar(wReader.get("FinishWorkTime"));
				if (wItem.FinishWorkTime.compareTo(wBaseTime) > 0) {
					wItem.StopTime = APSUtils.getInstance().CalIntervalDays(wItem.RealReceiveDate,
							wItem.FinishWorkTime);
				} else {
					wItem.StopTime = 0;
				}

				wResult.add(wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 工位工序进度
	 */
	public List<APSTaskPart> SelectListProgress(BMSEmployee wLoginUser, List<Integer> wTaskLineIDList,
			OutResult<Integer> wErrorCode) {
		List<APSTaskPart> wResultList = new ArrayList<APSTaskPart>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = StringUtils.Format("SELECT ID , "
					+ "(SELECT count(*)  FROM {0}.aps_taskstep  where Active =1 and TaskPartID=t.ID  ) as PointSize, "
					+ "(SELECT count(*)  FROM {0}.aps_taskstep  where Active =1 and Status =5 and TaskPartID=t.ID   )as PointFinishSize "
					+ " FROM {0}.aps_taskpart  t where t.ID in({1})", wInstance.Result,
					StringUtils.Join(",", wTaskLineIDList));

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				APSTaskPart wItem = new APSTaskPart();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.PointSize = StringUtils.parseInt(wReader.get("PointSize"));
				wItem.PointFinishSize = StringUtils.parseInt(wReader.get("PointFinishSize"));

				wResultList.add(wItem);
			}

		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	/**
	 * 通过时间段查询月计划
	 */
	public List<APSTaskPart> APS_QueryMonthPlanByMonth(BMSEmployee wLoginUser, Calendar wStartTime, Calendar wEndTime,
			OutResult<Integer> wErrorCode) {
		List<APSTaskPart> wResult = new ArrayList<APSTaskPart>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1);
			if (wStartTime == null || wStartTime.compareTo(wBaseTime) < 0)
				wStartTime = wBaseTime;
			if (wEndTime == null || wEndTime.compareTo(wBaseTime) < 0)
				wEndTime = wBaseTime;
			if (wStartTime.compareTo(wEndTime) > 0)
				return wResult;

			String wSQL = StringUtils.Format(
					"select t.*,o.OrderNo,o.BureauSectionID from {0}.aps_taskpart t  left join {0}.oms_order o "
							+ "on t.OrderID=o.ID" + " where t.ShiftPeriod=6 "
							+ "and t.Active=1 and t.StartTime > :wStartTime and t.StartTime < :wEndTime;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetValue(wResult, wQueryResult);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取周计划
	 */
	public List<APSTaskPart> APS_QueryWeekPlanByWeek(BMSEmployee wLoginUser, Calendar wStartTime, Calendar wEndTime,
			OutResult<Integer> wErrorCode) {
		List<APSTaskPart> wResult = new ArrayList<APSTaskPart>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1);
			if (wStartTime == null || wStartTime.compareTo(wBaseTime) < 0)
				wStartTime = wBaseTime;
			if (wEndTime == null || wEndTime.compareTo(wBaseTime) < 0)
				wEndTime = wBaseTime;
			if (wStartTime.compareTo(wEndTime) > 0)
				return wResult;

			String wSQL = StringUtils.Format(
					"select t.*,o.OrderNo,o.BureauSectionID from {0}.aps_taskpart t  left join {0}.oms_order o "
							+ "on t.OrderID=o.ID" + " where t.ShiftPeriod=5 "
							+ "and t.Active=1 and t.EndTime > :wStartTime and t.StartTime < :wEndTime;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetValue(wResult, wQueryResult);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	public Map<Integer, Integer> SelectPartOrderMap(BMSEmployee wLoginUser, int wOrderID) {
		Map<Integer, Integer> wResult = new HashMap<Integer, Integer>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			ServiceResult<String> wInstance1 = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance1.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"SELECT PartID,OrderID FROM {0}.fpc_routepart where RouteID  "
							+ "in (select RouteID from {1}.oms_order where ID=:OrderID);",
					wInstance.Result, wInstance1.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("OrderID", wOrderID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wPartID = StringUtils.parseInt(wReader.get("PartID"));
				int wOrder = StringUtils.parseInt(wReader.get("OrderID"));
				wResult.put(wPartID, wOrder);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据订单、工位获取工序集合(虚拟工位)
	 */
	public List<FPCPart> SelectStepPart(BMSEmployee wLoginUser, int wOrderID, int wPartID,
			OutResult<Integer> wErrorCode) {
		List<FPCPart> wResult = new ArrayList<FPCPart>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
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
					"SELECT t1.PartPointID,t2.Name,t1.OrderID FROM {0}.fpc_routepartpoint t1,{0}.fpc_partpoint t2 "
							+ "where t1.PartPointID=t2.ID " + "and t1.RouteID in (SELECT RouteID FROM {1}.oms_order "
							+ "where ID=:wOrderID) and t1.PartID=:wPartID;",
					wInstance.Result, wInstance1.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wOrderID", wOrderID);
			wParamMap.put("wPartID", wPartID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				FPCPart wItem = new FPCPart();

				wItem.ID = StringUtils.parseInt(wReader.get("PartPointID"));
				wItem.Name = StringUtils.parseString(wReader.get("Name"));
				wItem.FactoryID = StringUtils.parseInt(wReader.get("OrderID"));

				wResult.add(wItem);
			}

			// 查询是否设置了手动排序，若设置了，修改FactoryID
			List<FPCPart> wOList = APSManuCapacityStepDAO.getInstance().SelectListByOrder(wLoginUser, wOrderID, wPartID,
					wErrorCode);
			if (wOList.stream().anyMatch(p -> p.FactoryID > 0)) {
				for (FPCPart fpcPart : wResult) {
					if (!wOList.stream().anyMatch(p -> p.ID == fpcPart.ID)) {
						continue;
					}
					fpcPart.FactoryID = wOList.stream().filter(p -> p.ID == fpcPart.ID).findFirst().get().FactoryID;
				}
			}
			// 排序
			wResult.sort(Comparator.comparing(FPCPart::getFactoryID));
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 判断此订单，此工位是否是不做的工位
	 */
	public boolean CheckIsNotDo(BMSEmployee wLoginUser, int orderID, int partID, OutResult<Integer> wErrorCode) {
		boolean wResult = false;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("select ChangeControl from {0}.fpc_routepart "
					+ "where RouteID in (select RouteID from {0}.oms_order where ID=:wOrderID) and PartID=:wPartID;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wOrderID", orderID);
			wParamMap.put("wPartID", partID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wChangeControl = StringUtils.parseInt(wReader.get("ChangeControl"));
				if (wChangeControl == 3) {
					wResult = true;
					return wResult;
				}

			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 时间段内获取计划
	 */
	public List<Integer> SelectAndonList(BMSEmployee wLoginUser, int wYear, int wMonth, OutResult<Integer> wErrorCode) {
		List<Integer> wResultList = new ArrayList<Integer>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			// 当月时间
			Calendar wMonthStart = Calendar.getInstance();
			wMonthStart.set(wYear, wMonth - 1, 1, 0, 0, 0);
			Calendar wMonthEnd = Calendar.getInstance();
			wMonthEnd.set(wYear, wMonth, 1, 0, 0, 0);

			String wSQL = StringUtils
					.Format("SELECT distinct OrderID FROM {0}.aps_taskpart where ShiftPeriod=5 and Active=1  "
							+ "and ( ( EndTime > :wMonthStart and EndTime < :wMonthEnd ) "
							+ "or (EndTime < :wMonthStart and Status in (2,4)) or ( FinishWorkTime > :wMonthStart "
							+ "and FinishWorkTime < :wMonthEnd   ) );", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wMonthStart", wMonthStart);
			wParamMap.put("wMonthEnd", wMonthEnd);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wOrderID = StringUtils.parseInt(wReader.get("OrderID"));
				wResultList.add(wOrderID);
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	/**
	 * 根据订单和工位查询周计划
	 */
	public List<APSTaskPart> SelectListByOrdersAndPartIDs(BMSEmployee wLoginUser, String wOrderIDs, String wPartIDs,
			OutResult<Integer> wErrorCode) {
		List<APSTaskPart> wResult = new ArrayList<APSTaskPart>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (StringUtils.isEmpty(wOrderIDs) || StringUtils.isEmpty(wPartIDs)) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"SELECT * FROM {0}.aps_taskpart where "
							+ "OrderID in ({1}) and PartID in ({2}) and ShiftPeriod=5 and Active=1 and Status>=2;",
					wInstance.Result, wOrderIDs, wPartIDs);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetValue(wResult, wQueryResult);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}
}
