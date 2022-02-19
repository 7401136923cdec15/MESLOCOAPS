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
import com.mes.loco.aps.server.service.po.aps.APSAuditConfig;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;

public class APSAuditConfigDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(APSAuditConfigDAO.class);

	private static APSAuditConfigDAO Instance = null;

	/**
	 * 添加或修改
	 * 
	 * @param wAPSAuditConfig
	 * @return
	 */
	public int Update(BMSEmployee wLoginUser, APSAuditConfig wAPSAuditConfig, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wAPSAuditConfig == null)
				return 0;

			String wSQL = "";
			if (wAPSAuditConfig.getID() <= 0) {
				wSQL = StringUtils.Format(
						"INSERT INTO {0}.aps_auditconfig(APSShiftPeriod," + "AuditPositionID,AuditLevel,CreateID,"
								+ "CreateTime,EditID,EditTime) " + "VALUES(:APSShiftPeriod,:AuditPositionID,"
								+ ":AuditLevel,:CreateID,:CreateTime," + ":EditID,:EditTime);",
						wInstance.Result);
			} else {
				wSQL = StringUtils.Format("UPDATE {0}.aps_auditconfig SET " + "APSShiftPeriod = :APSShiftPeriod,"
						+ "AuditPositionID = :AuditPositionID," + "AuditLevel = :AuditLevel," + "CreateID = :CreateID,"
						+ "CreateTime = :CreateTime," + "EditID = :EditID," + "EditTime = :EditTime "
						+ "WHERE ID = :ID;", wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wAPSAuditConfig.ID);
			wParamMap.put("APSShiftPeriod", wAPSAuditConfig.APSShiftPeriod);
			wParamMap.put("AuditPositionID", wAPSAuditConfig.AuditPositionID);
			wParamMap.put("AuditLevel", wAPSAuditConfig.AuditLevel);
			wParamMap.put("CreateID", wAPSAuditConfig.CreateID);
			wParamMap.put("CreateTime", wAPSAuditConfig.CreateTime);
			wParamMap.put("EditID", wAPSAuditConfig.EditID);
			wParamMap.put("EditTime", wAPSAuditConfig.EditTime);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wAPSAuditConfig.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wAPSAuditConfig.setID(wResult);
			} else {
				wResult = wAPSAuditConfig.getID();
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
	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<APSAuditConfig> wList,
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
			for (APSAuditConfig wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = StringUtils.Format("delete from {1}.aps_auditconfig WHERE ID IN({0}) ;",
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
	public APSAuditConfig SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		APSAuditConfig wResult = new APSAuditConfig();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<APSAuditConfig> wList = SelectList(wLoginUser, wID, -1, wErrorCode);
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
	public List<APSAuditConfig> SelectList(BMSEmployee wLoginUser, int wID, int wAPSShiftPeriod,
			OutResult<Integer> wErrorCode) {
		List<APSAuditConfig> wResultList = new ArrayList<APSAuditConfig>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = MessageFormat
					.format("SELECT * FROM {0}.aps_auditconfig WHERE  1=1  " + "and ( :wID <= 0 or :wID = ID ) "
							+ "and ( :wAPSShiftPeriod <= 0 or :wAPSShiftPeriod = APSShiftPeriod );", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wAPSShiftPeriod", wAPSShiftPeriod);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				APSAuditConfig wItem = new APSAuditConfig();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.APSShiftPeriod = StringUtils.parseInt(wReader.get("APSShiftPeriod"));
				wItem.AuditPositionID = StringUtils.parseInt(wReader.get("AuditPositionID"));
				wItem.AuditLevel = StringUtils.parseInt(wReader.get("AuditLevel"));
				wItem.CreateID = StringUtils.parseInt(wReader.get("CreateID"));
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				wItem.EditID = StringUtils.parseInt(wReader.get("EditID"));
				wItem.EditTime = StringUtils.parseCalendar(wReader.get("EditTime"));

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	private APSAuditConfigDAO() {
		super();
	}

	public static APSAuditConfigDAO getInstance() {
		if (Instance == null)
			Instance = new APSAuditConfigDAO();
		return Instance;
	}
}
