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
import com.mes.loco.aps.server.service.po.aps.APSPartsLog;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;

/**
 * 台车日志
 * 
 * @author YouWang·Peng
 * @CreateTime 2020-1-19 09:43:10
 * @LastEditTime 2020-4-17 15:17:43
 *
 */
public class APSPartsLogDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(APSPartsLogDAO.class);

	private static APSPartsLogDAO Instance = null;
	
	/**
	 * 权限码
	 */
	private static int AccessCode = 501200;

	/**
	 * 添加或修改
	 * 
	 * @param wAPSPartsLog
	 * @return
	 */
	public long Update(BMSEmployee wLoginUser, APSPartsLog wAPSPartsLog, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), AccessCode);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wAPSPartsLog == null)
				return 0;

			String wSQL = "";
			if (wAPSPartsLog.getID() <= 0) {
				wSQL = StringUtils.Format(
						"INSERT INTO {0}.aps_partslog(ProductID,PartNo,ArrivedTime,DepartureTime,CreatorID,CreateTime,EditorID,EditTime,Status) VALUES(:ProductID,:PartNo,:ArrivedTime,:DepartureTime,:CreatorID,:CreateTime,:EditorID,:EditTime,:Status);",
						wInstance.Result);
			} else {
				wSQL = StringUtils.Format(
						"UPDATE {0}.aps_partslog SET ProductID = :ProductID,PartNo = :PartNo,ArrivedTime = :ArrivedTime,DepartureTime = :DepartureTime,CreatorID = :CreatorID,CreateTime = :CreateTime,EditorID = :EditorID,EditTime = :EditTime,Status = :Status WHERE ID = :ID;",
						wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wAPSPartsLog.ID);
			wParamMap.put("ProductID", wAPSPartsLog.ProductID);
			wParamMap.put("PartNo", wAPSPartsLog.PartNo);
			wParamMap.put("ArrivedTime", wAPSPartsLog.ArrivedTime);
			wParamMap.put("DepartureTime", wAPSPartsLog.DepartureTime);
			wParamMap.put("CreatorID", wAPSPartsLog.CreatorID);
			wParamMap.put("CreateTime", wAPSPartsLog.CreateTime);
			wParamMap.put("EditorID", wAPSPartsLog.EditorID);
			wParamMap.put("EditTime", wAPSPartsLog.EditTime);
			wParamMap.put("Status", wAPSPartsLog.Status);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wAPSPartsLog.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wAPSPartsLog.setID(wResult);
			} else {
				wResult = wAPSPartsLog.getID();
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
	public void DeleteList(BMSEmployee wLoginUser, List<APSPartsLog> wList, OutResult<Integer> wErrorCode) {
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), AccessCode);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return;
			}

			if (wList == null || wList.size() <= 0)
				return;

			List<String> wIDList = new ArrayList<String>();
			for (APSPartsLog wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = StringUtils.Format("delete from {1}.aps_partslog WHERE ID IN({0}) ;",
					String.join(",", wIDList), wInstance.Result);
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
	public APSPartsLog SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		APSPartsLog wResult = new APSPartsLog();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}
			
			List<APSPartsLog> wList = SelectList(wLoginUser, wID, -1, "",wErrorCode);
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
	public List<APSPartsLog> SelectList(BMSEmployee wLoginUser, int wID, int wProductID, String wPartNo,
			OutResult<Integer> wErrorCode) {
		List<APSPartsLog> wResultList = new ArrayList<APSPartsLog>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = StringUtils.Format(
					"SELECT * FROM {0}.aps_partslog WHERE  1=1  and ( :wID <= 0 or :wID = ID ) and ( :wProductID <= 0 or :wProductID = ProductID ) and ( :wPartNo is null or :wPartNo = '''' or :wPartNo = PartNo );",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wProductID", wProductID);
			wParamMap.put("wPartNo", wPartNo);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				APSPartsLog wItem = new APSPartsLog();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.ProductID = StringUtils.parseInt(wReader.get("ProductID"));
				wItem.PartNo = StringUtils.parseString(wReader.get("PartNo"));
				wItem.ArrivedTime = StringUtils.parseCalendar(wReader.get("ArrivedTime"));
				wItem.DepartureTime = StringUtils.parseCalendar(wReader.get("DepartureTime"));
				wItem.CreatorID = StringUtils.parseInt(wReader.get("CreatorID"));
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				wItem.EditorID = StringUtils.parseInt(wReader.get("EditorID"));
				wItem.EditTime = StringUtils.parseCalendar(wReader.get("EditTime"));
				wItem.Status = StringUtils.parseInt(wReader.get("Status"));

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	private APSPartsLogDAO() {
		super();
	}

	public static APSPartsLogDAO getInstance() {
		if (Instance == null)
			Instance = new APSPartsLogDAO();
		return Instance;
	}
}
