package com.mes.loco.aps.server.serviceimpl.dao.aps;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.mes.loco.aps.server.service.po.aps.APSTaskStepCancelLog;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;

public class APSTaskStepCancelLogDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(APSTaskStepCancelLogDAO.class);

	private static APSTaskStepCancelLogDAO Instance = null;

	/**
	 * 添加或修改
	 * 
	 * @param wAPSTaskStepCancelLog
	 * @return
	 */
	public int Update(BMSEmployee wLoginUser, APSTaskStepCancelLog wAPSTaskStepCancelLog,
			OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wAPSTaskStepCancelLog == null)
				return 0;

			String wSQL = "";
			if (wAPSTaskStepCancelLog.getID() <= 0) {
				wSQL = MessageFormat.format(
						"INSERT INTO {0}.aps_taskstepcancellog(OrderID,PartNo,PartID,PartName,StepIDs,StepNames,CancelType,CancelTypeName,Remark,CreateID,CreatorName,CreateTime) VALUES(:OrderID,:PartNo,:PartID,:PartName,:StepIDs,:StepNames,:CancelType,:CancelTypeName,:Remark,:CreateID,:CreatorName,:CreateTime);",
						wInstance.Result);
			} else {
				wSQL = MessageFormat.format(
						"UPDATE {0}.aps_taskstepcancellog SET OrderID = :OrderID,PartNo = :PartNo,PartID = :PartID,PartName = :PartName,StepIDs = :StepIDs,StepNames = :StepNames,CancelType = :CancelType,CancelTypeName = :CancelTypeName,Remark = :Remark,CreateID = :CreateID,CreatorName = :CreatorName,CreateTime = :CreateTime WHERE ID = :ID;",
						wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wAPSTaskStepCancelLog.ID);
			wParamMap.put("OrderID", wAPSTaskStepCancelLog.OrderID);
			wParamMap.put("PartNo", wAPSTaskStepCancelLog.PartNo);
			wParamMap.put("PartID", wAPSTaskStepCancelLog.PartID);
			wParamMap.put("PartName", wAPSTaskStepCancelLog.PartName);
			wParamMap.put("StepIDs", wAPSTaskStepCancelLog.StepIDs);
			wParamMap.put("StepNames", wAPSTaskStepCancelLog.StepNames);
			wParamMap.put("CancelType", wAPSTaskStepCancelLog.CancelType);
			wParamMap.put("CancelTypeName", wAPSTaskStepCancelLog.CancelTypeName);
			wParamMap.put("Remark", wAPSTaskStepCancelLog.Remark);
			wParamMap.put("CreateID", wAPSTaskStepCancelLog.CreateID);
			wParamMap.put("CreatorName", wAPSTaskStepCancelLog.CreatorName);
			wParamMap.put("CreateTime", wAPSTaskStepCancelLog.CreateTime);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wAPSTaskStepCancelLog.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wAPSTaskStepCancelLog.setID(wResult);
			} else {
				wResult = wAPSTaskStepCancelLog.getID();
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
	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<APSTaskStepCancelLog> wList,
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
			for (APSTaskStepCancelLog wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = MessageFormat.format("delete from {1}.aps_taskstepcancellog WHERE ID IN({0}) ;",
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
	public APSTaskStepCancelLog SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		APSTaskStepCancelLog wResult = new APSTaskStepCancelLog();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<APSTaskStepCancelLog> wList = SelectList(wLoginUser, wID, -1, -1, -1, -1, null, null, wErrorCode);
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
	public List<APSTaskStepCancelLog> SelectList(BMSEmployee wLoginUser, int wID, int wOrderID, int wPartID,
			int wCancelType, int wCreateID, Calendar wStartTime, Calendar wEndTime, OutResult<Integer> wErrorCode) {
		List<APSTaskStepCancelLog> wResultList = new ArrayList<APSTaskStepCancelLog>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
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

			String wSQL = MessageFormat.format("SELECT * FROM {0}.aps_taskstepcancellog WHERE  1=1  "
					+ "and ( :wID <= 0 or :wID = ID ) " + "and ( :wOrderID <= 0 or :wOrderID = OrderID ) "
					+ "and ( :wPartID <= 0 or :wPartID = PartID ) "
					+ "and ( :wCancelType <= 0 or :wCancelType = CancelType ) "
					+ "and ( :wStartTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or CreateTime >= :wStartTime) "
					+ "and ( :wEndTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or CreateTime <= :wEndTime) "
					+ "and ( :wCreateID <= 0 or :wCreateID = CreateID );", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wOrderID", wOrderID);
			wParamMap.put("wPartID", wPartID);
			wParamMap.put("wCancelType", wCancelType);
			wParamMap.put("wCreateID", wCreateID);
			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				APSTaskStepCancelLog wItem = new APSTaskStepCancelLog();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.OrderID = StringUtils.parseInt(wReader.get("OrderID"));
				wItem.PartNo = StringUtils.parseString(wReader.get("PartNo"));
				wItem.PartID = StringUtils.parseInt(wReader.get("PartID"));
				wItem.PartName = StringUtils.parseString(wReader.get("PartName"));
				wItem.StepIDs = StringUtils.parseString(wReader.get("StepIDs"));
				wItem.StepNames = StringUtils.parseString(wReader.get("StepNames"));
				wItem.CancelType = StringUtils.parseInt(wReader.get("CancelType"));
				wItem.CancelTypeName = StringUtils.parseString(wReader.get("CancelTypeName"));
				wItem.Remark = StringUtils.parseString(wReader.get("Remark"));
				wItem.CreateID = StringUtils.parseInt(wReader.get("CreateID"));
				wItem.CreatorName = StringUtils.parseString(wReader.get("CreatorName"));
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	private APSTaskStepCancelLogDAO() {
		super();
	}

	public static APSTaskStepCancelLogDAO getInstance() {
		if (Instance == null)
			Instance = new APSTaskStepCancelLogDAO();
		return Instance;
	}
}
