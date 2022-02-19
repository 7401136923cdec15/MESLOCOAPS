package com.mes.loco.aps.server.serviceimpl.dao.aps;

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
import com.mes.loco.aps.server.service.po.aps.APSTaskLine;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;

public class APSTaskLineDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(APSTaskLineDAO.class);

	private static APSTaskLineDAO Instance = null;

	/**
	 * 添加或修改
	 * 
	 * @param wAPSTaskLine
	 * @return
	 */
	public long Update(BMSEmployee wLoginUser, APSTaskLine wAPSTaskLine, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wAPSTaskLine == null)
				return 0;

			String wSQL = "";
			if (wAPSTaskLine.getID() <= 0) {
				wSQL = StringUtils.Format(
						"INSERT INTO {0}.aps_taskline(OrderID,PartNo,WorkShopID,LineID,PartHours,CraftMinutes,ShiftID,PlanerID,SubmitTime,SessionTime,AuditorID,AuditTime,ProductNo,MaterialNo,BOMNo,Priority,Active,TaskText,WorkHour,Status,StartTime,EndTime,ShiftPeriod) VALUES(:OrderID,:PartNo,:WorkShopID,:LineID,:PartHours,:CraftMinutes,:ShiftID,:PlanerID,:SubmitTime,:SessionTime,:AuditorID,:AuditTime,:ProductNo,:MaterialNo,:BOMNo,:Priority,:Active,:TaskText,:WorkHour,:Status,:StartTime,:EndTime,:ShiftPeriod);",
						wInstance.Result);
			} else {
				wSQL = StringUtils.Format(
						"UPDATE {0}.aps_taskline SET OrderID = :OrderID,PartNo = :PartNo,WorkShopID = :WorkShopID,LineID = :LineID,PartHours = :PartHours,CraftMinutes = :CraftMinutes,ShiftID = :ShiftID,PlanerID = :PlanerID,SubmitTime = :SubmitTime,SessionTime = :SessionTime,AuditorID = :AuditorID,AuditTime = :AuditTime,ProductNo = :ProductNo,MaterialNo = :MaterialNo,BOMNo = :BOMNo,Priority = :Priority,Active = :Active,TaskText = :TaskText,WorkHour = :WorkHour,Status = :Status,StartTime = :StartTime,EndTime = :EndTime,ShiftPeriod = :ShiftPeriod WHERE ID = :ID;",
						wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wAPSTaskLine.ID);
			wParamMap.put("OrderID", wAPSTaskLine.OrderID);
			wParamMap.put("PartNo", wAPSTaskLine.PartNo);
			wParamMap.put("WorkShopID", wAPSTaskLine.WorkShopID);
			wParamMap.put("LineID", wAPSTaskLine.LineID);
			wParamMap.put("PartHours", wAPSTaskLine.PartHours);
			wParamMap.put("CraftMinutes", wAPSTaskLine.CraftMinutes);
			wParamMap.put("ShiftID", wAPSTaskLine.ShiftID);
			wParamMap.put("PlanerID", wAPSTaskLine.PlanerID);
			wParamMap.put("SubmitTime", wAPSTaskLine.SubmitTime);
			wParamMap.put("SessionTime", wAPSTaskLine.SessionTime);
			wParamMap.put("AuditorID", wAPSTaskLine.AuditorID);
			wParamMap.put("AuditTime", wAPSTaskLine.AuditTime);
			wParamMap.put("ProductNo", wAPSTaskLine.ProductNo);
			wParamMap.put("MaterialNo", wAPSTaskLine.MaterialNo);
			wParamMap.put("BOMNo", wAPSTaskLine.BOMNo);
			wParamMap.put("Priority", wAPSTaskLine.Priority);
			wParamMap.put("Active", wAPSTaskLine.Active);
			wParamMap.put("TaskText", wAPSTaskLine.TaskText);
			wParamMap.put("WorkHour", wAPSTaskLine.WorkHour);
			wParamMap.put("Status", wAPSTaskLine.Status);
			wParamMap.put("StartTime", wAPSTaskLine.StartTime);
			wParamMap.put("EndTime", wAPSTaskLine.EndTime);
			wParamMap.put("ShiftPeriod", wAPSTaskLine.ShiftPeriod);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wAPSTaskLine.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wAPSTaskLine.setID(wResult);
			} else {
				wResult = wAPSTaskLine.getID();
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
	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<APSTaskLine> wList,
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
			for (APSTaskLine wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = StringUtils.Format("delete from {1}.aps_taskline WHERE ID IN({0}) ;",
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
	public APSTaskLine SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		APSTaskLine wResult = new APSTaskLine();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<APSTaskLine> wList = SelectList(wLoginUser, wID, -1, -1, -1, -1, wErrorCode);
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
	public List<APSTaskLine> SelectList(BMSEmployee wLoginUser, int wID, int wOrderID, int wWorkShopID, int wLineID,
			int wActive, OutResult<Integer> wErrorCode) {
		List<APSTaskLine> wResultList = new ArrayList<APSTaskLine>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = StringUtils.Format(
					"SELECT * FROM {0}.aps_taskline WHERE  1=1  and ( :wID <= 0 or :wID = ID ) and ( :wOrderID <= 0 or :wOrderID = OrderID ) and ( :wWorkShopID <= 0 or :wWorkShopID = WorkShopID ) and ( :wLineID <= 0 or :wLineID = LineID ) and ( :wActive <= 0 or :wActive = Active );",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wOrderID", wOrderID);
			wParamMap.put("wWorkShopID", wWorkShopID);
			wParamMap.put("wLineID", wLineID);
			wParamMap.put("wActive", wActive);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				APSTaskLine wItem = new APSTaskLine();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.OrderID = StringUtils.parseInt(wReader.get("OrderID"));
				wItem.PartNo = StringUtils.parseString(wReader.get("PartNo"));
				wItem.WorkShopID = StringUtils.parseInt(wReader.get("WorkShopID"));
				wItem.LineID = StringUtils.parseInt(wReader.get("LineID"));
				wItem.PartHours = StringUtils.parseInt(wReader.get("PartHours"));
				wItem.CraftMinutes = StringUtils.parseInt(wReader.get("CraftMinutes"));
				wItem.ShiftID = StringUtils.parseInt(wReader.get("ShiftID"));
				wItem.PlanerID = StringUtils.parseInt(wReader.get("PlanerID"));
				wItem.SubmitTime = StringUtils.parseCalendar(wReader.get("SubmitTime"));
				wItem.SessionTime = StringUtils.parseCalendar(wReader.get("SessionTime"));
				wItem.AuditorID = StringUtils.parseInt(wReader.get("AuditorID"));
				wItem.AuditTime = StringUtils.parseCalendar(wReader.get("AuditTime"));
				wItem.ProductNo = StringUtils.parseString(wReader.get("ProductNo"));
				wItem.MaterialNo = StringUtils.parseString(wReader.get("MaterialNo"));
				wItem.BOMNo = StringUtils.parseString(wReader.get("BOMNo"));
				wItem.Priority = StringUtils.parseInt(wReader.get("Priority"));
				wItem.Active = StringUtils.parseInt(wReader.get("Active"));
				wItem.TaskText = StringUtils.parseString(wReader.get("TaskText"));
				wItem.WorkHour = StringUtils.parseInt(wReader.get("WorkHour"));
				wItem.Status = StringUtils.parseInt(wReader.get("Status"));
				wItem.StartTime = StringUtils.parseCalendar(wReader.get("StartTime"));
				wItem.EndTime = StringUtils.parseCalendar(wReader.get("EndTime"));
				wItem.ShiftPeriod = StringUtils.parseInt(wReader.get("ShiftPeriod"));

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
				APSTaskLine wAPSTaskLine = SelectByID(wLoginUser, wItem, wErrorCode);
				if (wAPSTaskLine == null || wAPSTaskLine.ID <= 0)
					continue;
				// 只有激活的才能禁用
				if (wActive == 2 && wAPSTaskLine.Active != 1) {
					wErrorCode.set(MESException.Logic.getValue());
					return wResult;
				}
				wAPSTaskLine.Active = wActive;
				long wID = Update(wLoginUser, wAPSTaskLine, wErrorCode);
				if (wID <= 0)
					break;
			}
		} catch (Exception e) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(e.toString());
		}
		return wResult;
	}

	private APSTaskLineDAO() {
		super();
	}

	public static APSTaskLineDAO getInstance() {
		if (Instance == null)
			Instance = new APSTaskLineDAO();
		return Instance;
	}
}
