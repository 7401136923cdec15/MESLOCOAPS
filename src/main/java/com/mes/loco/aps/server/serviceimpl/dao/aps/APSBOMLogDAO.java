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
import com.mes.loco.aps.server.service.po.aps.APSBOMLog;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;

public class APSBOMLogDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(APSBOMLogDAO.class);

	private static APSBOMLogDAO Instance = null;

	/**
	 * 添加或修改
	 * 
	 * @param wAPSBOMLog
	 * @return
	 */
	public int Update(BMSEmployee wLoginUser, APSBOMLog wAPSBOMLog, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wAPSBOMLog == null)
				return 0;

			String wSQL = "";
			if (wAPSBOMLog.getID() <= 0) {
				wSQL = MessageFormat.format(
						"INSERT INTO {0}.aps_bomlog(ProductID,ProductNo,LineID,LineName,CustomerID,CustomerName,OrderID,PartNo,WBSNo,Status,Msg,CreateTime) VALUES(:ProductID,:ProductNo,:LineID,:LineName,:CustomerID,:CustomerName,:OrderID,:PartNo,:WBSNo,:Status,:Msg,:CreateTime);",
						wInstance.Result);
			} else {
				wSQL = MessageFormat.format(
						"UPDATE {0}.aps_bomlog SET ProductID = :ProductID,ProductNo = :ProductNo,LineID = :LineID,LineName = :LineName,CustomerID = :CustomerID,CustomerName = :CustomerName,OrderID = :OrderID,PartNo = :PartNo,WBSNo = :WBSNo,Status = :Status,Msg = :Msg,CreateTime = :CreateTime WHERE ID = :ID;",
						wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wAPSBOMLog.ID);
			wParamMap.put("ProductID", wAPSBOMLog.ProductID);
			wParamMap.put("ProductNo", wAPSBOMLog.ProductNo);
			wParamMap.put("LineID", wAPSBOMLog.LineID);
			wParamMap.put("LineName", wAPSBOMLog.LineName);
			wParamMap.put("CustomerID", wAPSBOMLog.CustomerID);
			wParamMap.put("CustomerName", wAPSBOMLog.CustomerName);
			wParamMap.put("OrderID", wAPSBOMLog.OrderID);
			wParamMap.put("PartNo", wAPSBOMLog.PartNo);
			wParamMap.put("WBSNo", wAPSBOMLog.WBSNo);
			wParamMap.put("Status", wAPSBOMLog.Status);
			wParamMap.put("Msg", wAPSBOMLog.Msg);
			wParamMap.put("CreateTime", wAPSBOMLog.CreateTime);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wAPSBOMLog.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wAPSBOMLog.setID(wResult);
			} else {
				wResult = wAPSBOMLog.getID();
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
	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<APSBOMLog> wList,
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
			for (APSBOMLog wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = MessageFormat.format("delete from {1}.aps_bomlog WHERE ID IN({0}) ;",
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
	public APSBOMLog SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		APSBOMLog wResult = new APSBOMLog();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<APSBOMLog> wList = SelectList(wLoginUser, wID, -1, -1, -1, -1, -1, null, null, wErrorCode);
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
	public List<APSBOMLog> SelectList(BMSEmployee wLoginUser, int wID, int wProductID, int wLineID, int wCustomerID,
			int wOrderID, int wStatus, Calendar wStartTime, Calendar wEndTime, OutResult<Integer> wErrorCode) {
		List<APSBOMLog> wResultList = new ArrayList<APSBOMLog>();
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

			String wSQL = MessageFormat.format(
					"SELECT * FROM {0}.aps_bomlog WHERE  1=1  and ( :wID <= 0 or :wID = ID ) "
							+ "and ( :wProductID <= 0 or :wProductID = ProductID ) "
							+ "and ( :wLineID <= 0 or :wLineID = LineID ) and ( :wCustomerID <= 0 or :wCustomerID = CustomerID ) "
							+ "and ( :wOrderID <= 0 or :wOrderID = OrderID ) and ( :wStatus <= 0 or :wStatus = Status ) "
							+ "and ( :wStartTime <=str_to_date(''2010-01-01'', ''%Y-%m-%d'')  or :wStartTime <=  CreateTime ) "
							+ "and ( :wEndTime <=str_to_date(''2010-01-01'', ''%Y-%m-%d'')  or :wEndTime >=  CreateTime );",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wProductID", wProductID);
			wParamMap.put("wLineID", wLineID);
			wParamMap.put("wCustomerID", wCustomerID);
			wParamMap.put("wOrderID", wOrderID);
			wParamMap.put("wStatus", wStatus);
			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				APSBOMLog wItem = new APSBOMLog();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.ProductID = StringUtils.parseInt(wReader.get("ProductID"));
				wItem.ProductNo = StringUtils.parseString(wReader.get("ProductNo"));
				wItem.LineID = StringUtils.parseInt(wReader.get("LineID"));
				wItem.LineName = StringUtils.parseString(wReader.get("LineName"));
				wItem.CustomerID = StringUtils.parseInt(wReader.get("CustomerID"));
				wItem.CustomerName = StringUtils.parseString(wReader.get("CustomerName"));
				wItem.OrderID = StringUtils.parseInt(wReader.get("OrderID"));
				wItem.PartNo = StringUtils.parseString(wReader.get("PartNo"));
				wItem.WBSNo = StringUtils.parseString(wReader.get("WBSNo"));
				wItem.Status = StringUtils.parseInt(wReader.get("Status"));
				wItem.Msg = StringUtils.parseString(wReader.get("Msg"));
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	private APSBOMLogDAO() {
		super();
	}

	public static APSBOMLogDAO getInstance() {
		if (Instance == null)
			Instance = new APSBOMLogDAO();
		return Instance;
	}
}
