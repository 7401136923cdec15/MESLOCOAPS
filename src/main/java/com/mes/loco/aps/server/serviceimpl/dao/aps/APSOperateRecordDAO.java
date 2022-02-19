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
import com.mes.loco.aps.server.service.po.aps.APSOperateRecord;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;

public class APSOperateRecordDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(APSOperateRecordDAO.class);

	private static APSOperateRecordDAO Instance = null;

	/**
	 * 添加或修改
	 * 
	 * @param wAPSOperateRecord
	 * @return
	 */
	public int Update(BMSEmployee wLoginUser, APSOperateRecord wAPSOperateRecord, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wAPSOperateRecord == null)
				return 0;

			String wSQL = "";
			if (wAPSOperateRecord.getID() <= 0) {
				wSQL = StringUtils.Format("INSERT INTO {0}.aps_operaterecord(APSTaskPartID,"
						+ "OperateID,OperateType,OperateTime,Remark,OperateLevel) "
						+ "VALUES(:APSTaskPartID,:OperateID,:OperateType," + ":OperateTime,:Remark,:OperateLevel);",
						wInstance.Result);
			} else {
				wSQL = StringUtils.Format("UPDATE {0}.aps_operaterecord SET " + "APSTaskPartID = :APSTaskPartID,"
						+ "perateID = :OperateID," + "OperateType = :OperateType," + "OperateTime = :OperateTime,"
						+ "Remark = :Remark,OperateLevel=:OperateLevel " + "WHERE ID = :ID;", wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wAPSOperateRecord.ID);
			wParamMap.put("APSTaskPartID", wAPSOperateRecord.APSTaskPartID);
			wParamMap.put("OperateID", wAPSOperateRecord.OperateID);
			wParamMap.put("OperateType", wAPSOperateRecord.OperateType);
			wParamMap.put("OperateTime", wAPSOperateRecord.OperateTime);
			wParamMap.put("Remark", wAPSOperateRecord.Remark);
			wParamMap.put("OperateLevel", wAPSOperateRecord.OperateLevel);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wAPSOperateRecord.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wAPSOperateRecord.setID(wResult);
			} else {
				wResult = wAPSOperateRecord.getID();
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
	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<APSOperateRecord> wList,
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
			for (APSOperateRecord wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = StringUtils.Format("delete from {1}.aps_operaterecord WHERE ID IN({0}) ;",
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
	public APSOperateRecord SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		APSOperateRecord wResult = new APSOperateRecord();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<APSOperateRecord> wList = SelectList(wLoginUser, wID, -1, -1, wErrorCode);
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
	public List<APSOperateRecord> SelectList(BMSEmployee wLoginUser, int wID, int wAPSTaskPartID, int wOperateID,
			OutResult<Integer> wErrorCode) {
		List<APSOperateRecord> wResultList = new ArrayList<APSOperateRecord>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = MessageFormat
					.format("SELECT * FROM {0}.aps_operaterecord WHERE  1=1  " + "and ( :wID <= 0 or :wID = ID ) "
							+ "and ( :wAPSTaskPartID <= 0 or :wAPSTaskPartID = APSTaskPartID ) "
							+ "and ( :wOperateID <= 0 or :wOperateID = OperateID );", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wAPSTaskPartID", wAPSTaskPartID);
			wParamMap.put("wOperateID", wOperateID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				APSOperateRecord wItem = new APSOperateRecord();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.APSTaskPartID = StringUtils.parseInt(wReader.get("APSTaskPartID"));
				wItem.OperateID = StringUtils.parseInt(wReader.get("OperateID"));
				wItem.OperateType = StringUtils.parseInt(wReader.get("OperateType"));
				wItem.OperateTime = StringUtils.parseCalendar(wReader.get("OperateTime"));
				wItem.Remark = StringUtils.parseString(wReader.get("Remark"));
				wItem.OperateLevel = StringUtils.parseInt(wReader.get("OperateLevel"));

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	private APSOperateRecordDAO() {
		super();
	}

	public static APSOperateRecordDAO getInstance() {
		if (Instance == null)
			Instance = new APSOperateRecordDAO();
		return Instance;
	}
}
