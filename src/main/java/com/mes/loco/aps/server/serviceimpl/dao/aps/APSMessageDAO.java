package com.mes.loco.aps.server.serviceimpl.dao.aps;

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
import com.mes.loco.aps.server.service.po.aps.APSMessage;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;

public class APSMessageDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(APSMessageDAO.class);

	private static APSMessageDAO Instance = null;

	/**
	 * 添加或修改
	 * 
	 * @param wAPSMessage
	 * @return
	 */
	public long Update(BMSEmployee wLoginUser, APSMessage wAPSMessage, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wAPSMessage == null)
				return 0;

			String wSQL = "";
			if (wAPSMessage.getID() <= 0) {
				wSQL = StringUtils.Format(
						"INSERT INTO {0}.aps_message(OrderID,Type,ProductNo,WorkShopID,"
								+ "LineID,PartID,MessageText,SubmitTime) VALUES(:OrderID,"
								+ ":Type,:ProductNo,:WorkShopID,:LineID,:PartID,:MessageText," + ":SubmitTime);",
						wInstance.Result);
			} else {
				wSQL = StringUtils.Format("UPDATE {0}.aps_message SET OrderID = :OrderID,Type = :Type,"
						+ "ProductNo = :ProductNo,WorkShopID = :WorkShopID,LineID = :LineID,"
						+ "PartID = :PartID,MessageText = :MessageText,SubmitTime = :SubmitTime" + " WHERE ID = :ID;",
						wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wAPSMessage.ID);
			wParamMap.put("OrderID", wAPSMessage.OrderID);
			wParamMap.put("Type", wAPSMessage.Type);
			wParamMap.put("ProductNo", wAPSMessage.ProductNo);
			wParamMap.put("WorkShopID", wAPSMessage.WorkShopID);
			wParamMap.put("LineID", wAPSMessage.LineID);
			wParamMap.put("PartID", wAPSMessage.PartID);
			wParamMap.put("MessageText", wAPSMessage.MessageText);
			wParamMap.put("SubmitTime", wAPSMessage.SubmitTime);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wAPSMessage.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wAPSMessage.setID(wResult);
			} else {
				wResult = wAPSMessage.getID();
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
	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<APSMessage> wList,
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
			for (APSMessage wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = StringUtils.Format("delete from {1}.aps_message WHERE ID IN({0}) ;",
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
	public APSMessage SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		APSMessage wResult = new APSMessage();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<APSMessage> wList = SelectList(wLoginUser, wID, -1, wErrorCode);
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
	public List<APSMessage> SelectList(BMSEmployee wLoginUser, int wID, int wOrderID, OutResult<Integer> wErrorCode) {
		List<APSMessage> wResultList = new ArrayList<APSMessage>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = StringUtils.Format("SELECT * FROM {0}.aps_message WHERE  1=1  "
					+ "and ( :wID <= 0 or :wID = ID ) " + "and ( :wOrderID <= 0 or :wOrderID = OrderID );",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wOrderID", wOrderID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				APSMessage wItem = new APSMessage();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.OrderID = StringUtils.parseInt(wReader.get("OrderID"));
				wItem.Type = StringUtils.parseInt(wReader.get("Type"));
				wItem.ProductNo = StringUtils.parseString(wReader.get("ProductNo"));
				wItem.WorkShopID = StringUtils.parseInt(wReader.get("WorkShopID"));
				wItem.LineID = StringUtils.parseInt(wReader.get("LineID"));
				wItem.PartID = StringUtils.parseInt(wReader.get("PartID"));
				wItem.MessageText = StringUtils.parseString(wReader.get("MessageText"));
				wItem.SubmitTime = StringUtils.parseCalendar(wReader.get("SubmitTime"));

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	/**
	 * 根据SubmitTime查询集合
	 * 
	 * @return
	 */
	public List<APSMessage> SelectListBySubmitTime(BMSEmployee wLoginUser, Calendar wSubmitTime,
			OutResult<Integer> wErrorCode) {
		List<APSMessage> wResultList = new ArrayList<APSMessage>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = StringUtils.Format(
					"SELECT * FROM {0}.aps_message WHERE  1=1  " + "and ( :wSubmitTime =  SubmitTime);",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wSubmitTime", wSubmitTime);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				APSMessage wItem = new APSMessage();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.OrderID = StringUtils.parseInt(wReader.get("OrderID"));
				wItem.Type = StringUtils.parseInt(wReader.get("Type"));
				wItem.ProductNo = StringUtils.parseString(wReader.get("ProductNo"));
				wItem.WorkShopID = StringUtils.parseInt(wReader.get("WorkShopID"));
				wItem.LineID = StringUtils.parseInt(wReader.get("LineID"));
				wItem.PartID = StringUtils.parseInt(wReader.get("PartID"));
				wItem.MessageText = StringUtils.parseString(wReader.get("MessageText"));
				wItem.SubmitTime = StringUtils.parseCalendar(wReader.get("SubmitTime"));

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	private APSMessageDAO() {
		super();
	}

	public static APSMessageDAO getInstance() {
		if (Instance == null)
			Instance = new APSMessageDAO();
		return Instance;
	}
}
