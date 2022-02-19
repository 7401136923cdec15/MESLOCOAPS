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
import com.mes.loco.aps.server.service.po.aps.APSManuCapacity;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;

/**
 * 工位工时
 * 
 * @author YouWang·Peng
 * @CreateTime 2020-1-19 09:43:10
 * @LastEditTime 2020年4月17日15:46:13
 *
 */
public class APSManuCapacityDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(APSManuCapacityDAO.class);

	private static APSManuCapacityDAO Instance = null;

	/**
	 * 权限码
	 */
	private static int AccessCode = 501800;

	/**
	 * 添加或修改
	 * 
	 * @param wAPSManuCapacity
	 * @return
	 */
	public long Update(BMSEmployee wLoginUser, APSManuCapacity wAPSManuCapacity, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), AccessCode);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wAPSManuCapacity == null)
				return 0;

			String wSQL = "";
			if (wAPSManuCapacity.getID() <= 0) {
				wSQL = StringUtils.Format("INSERT INTO {0}.aps_manucapacity(LineID,PartID,FQTY,"
						+ "Period,WorkHour,WorkHours,CreatorID,CreateTime,"
						+ "EditTime,EditorID,AuditTime,AuditorID,Status,Active) "
						+ "VALUES(:LineID,:PartID,:FQTY,:Period,:WorkHour,:WorkHours,"
						+ ":CreatorID,:CreateTime,:EditTime,:EditorID,:AuditTime," + ":AuditorID,:Status,:Active);",
						wInstance.Result);
			} else {
				wSQL = StringUtils.Format("UPDATE {0}.aps_manucapacity SET LineID = :LineID,"
						+ "PartID = :PartID,FQTY = :FQTY,Period = :Period,"
						+ "WorkHour = :WorkHour,WorkHours = :WorkHours,"
						+ "CreatorID = :CreatorID,CreateTime = :CreateTime,"
						+ "EditTime = :EditTime,EditorID = :EditorID,"
						+ "AuditTime = :AuditTime,AuditorID = :AuditorID,"
						+ "Status = :Status,Active = :Active WHERE ID = :ID;", wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wAPSManuCapacity.ID);
			wParamMap.put("LineID", wAPSManuCapacity.LineID);
			wParamMap.put("PartID", wAPSManuCapacity.PartID);
			wParamMap.put("FQTY", wAPSManuCapacity.FQTY);
			wParamMap.put("Period", wAPSManuCapacity.Period);
			wParamMap.put("WorkHour", wAPSManuCapacity.WorkHour);
			wParamMap.put("WorkHours", wAPSManuCapacity.WorkHours);
			wParamMap.put("CreatorID", wAPSManuCapacity.CreatorID);
			wParamMap.put("CreateTime", wAPSManuCapacity.CreateTime);
			wParamMap.put("EditTime", wAPSManuCapacity.EditTime);
			wParamMap.put("EditorID", wAPSManuCapacity.EditorID);
			wParamMap.put("AuditTime", wAPSManuCapacity.AuditTime);
			wParamMap.put("AuditorID", wAPSManuCapacity.AuditorID);
			wParamMap.put("Status", wAPSManuCapacity.Status);
			wParamMap.put("Active", wAPSManuCapacity.Active);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wAPSManuCapacity.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wAPSManuCapacity.setID(wResult);
			} else {
				wResult = wAPSManuCapacity.getID();
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
	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<APSManuCapacity> wList,
			OutResult<Integer> wErrorCode) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), AccessCode);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wList == null || wList.size() <= 0)
				return wResult;

			List<String> wIDList = new ArrayList<String>();
			for (APSManuCapacity wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = StringUtils.Format("delete from {1}.aps_manucapacity WHERE ID IN({0}) ;",
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
	public APSManuCapacity SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		APSManuCapacity wResult = new APSManuCapacity();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<APSManuCapacity> wList = SelectList(wLoginUser, wID, -1, -1, -1, wErrorCode);
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
	public List<APSManuCapacity> SelectList(BMSEmployee wLoginUser, int wID, int wLineID, int wPartID, int wActive,
			OutResult<Integer> wErrorCode) {
		List<APSManuCapacity> wResultList = new ArrayList<APSManuCapacity>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = StringUtils.Format("SELECT * FROM {0}.aps_manucapacity WHERE  1=1  "
					+ "and ( :wID <= 0 or :wID = ID ) " + "and ( :wLineID <= 0 or :wLineID = LineID ) "
					+ "and ( :wPartID <= 0 or :wPartID = PartID ) " + "and ( :wActive <= 0 or :wActive = Active );",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wLineID", wLineID);
			wParamMap.put("wPartID", wPartID);
			wParamMap.put("wActive", wActive);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				APSManuCapacity wItem = new APSManuCapacity();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.LineID = StringUtils.parseInt(wReader.get("LineID"));
				wItem.PartID = StringUtils.parseInt(wReader.get("PartID"));
				wItem.FQTY = StringUtils.parseInt(wReader.get("FQTY"));
				wItem.Period = StringUtils.parseDouble(wReader.get("Period"));
				wItem.WorkHour = StringUtils.parseDouble(wReader.get("WorkHour"));
				wItem.WorkHours = StringUtils.parseDouble(wReader.get("WorkHours"));
				wItem.CreatorID = StringUtils.parseInt(wReader.get("CreatorID"));
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				wItem.EditTime = StringUtils.parseCalendar(wReader.get("EditTime"));
				wItem.EditorID = StringUtils.parseInt(wReader.get("EditorID"));
				wItem.AuditTime = StringUtils.parseCalendar(wReader.get("AuditTime"));
				wItem.AuditorID = StringUtils.parseInt(wReader.get("AuditorID"));
				wItem.Status = StringUtils.parseInt(wReader.get("Status"));
				wItem.Active = StringUtils.parseInt(wReader.get("Active"));

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
					wLoginUser.getID(), AccessCode);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wIDList == null || wIDList.size() <= 0)
				return wResult;
			for (Integer wItem : wIDList) {
				APSManuCapacity wAPSManuCapacity = SelectByID(wLoginUser, wItem, wErrorCode);
				if (wAPSManuCapacity == null || wAPSManuCapacity.ID <= 0)
					continue;
				// 只有激活的才能禁用
				if (wActive == 2 && wAPSManuCapacity.Active != 1) {
					wErrorCode.set(MESException.Logic.getValue());
					return wResult;
				}
				wAPSManuCapacity.Active = wActive;
				long wID = Update(wLoginUser, wAPSManuCapacity, wErrorCode);
				if (wID <= 0)
					break;
			}
		} catch (Exception e) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(e.toString());
		}
		return wResult;
	}

	private APSManuCapacityDAO() {
		super();
	}

	public static APSManuCapacityDAO getInstance() {
		if (Instance == null)
			Instance = new APSManuCapacityDAO();
		return Instance;
	}
}
