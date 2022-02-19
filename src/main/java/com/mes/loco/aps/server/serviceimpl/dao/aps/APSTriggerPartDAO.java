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
import com.mes.loco.aps.server.service.po.aps.APSTriggerPart;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSConstans;

public class APSTriggerPartDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(APSTriggerPartDAO.class);

	private static APSTriggerPartDAO Instance = null;

	/**
	 * 添加或修改
	 * 
	 * @param wAPSTriggerPart
	 * @return
	 */
	public int Update(BMSEmployee wLoginUser, APSTriggerPart wAPSTriggerPart, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wAPSTriggerPart == null)
				return 0;

			String wSQL = "";
			if (wAPSTriggerPart.getID() <= 0) {
				wSQL = MessageFormat.format(
						"INSERT INTO {0}.aps_triggerpart(ProductID,LineID,CustomerID,PartID,CreateID,CreateTime,Active) VALUES(:ProductID,:LineID,:CustomerID,:PartID,:CreateID,:CreateTime,:Active);",
						wInstance.Result);
				wAPSTriggerPart.CreateID = wLoginUser.ID;
				wAPSTriggerPart.CreateTime = Calendar.getInstance();
			} else {
				wSQL = MessageFormat.format(
						"UPDATE {0}.aps_triggerpart SET ProductID = :ProductID,LineID = :LineID,CustomerID = :CustomerID,PartID = :PartID,CreateID = :CreateID,CreateTime = :CreateTime,Active = :Active WHERE ID = :ID;",
						wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wAPSTriggerPart.ID);
			wParamMap.put("ProductID", wAPSTriggerPart.ProductID);
			wParamMap.put("LineID", wAPSTriggerPart.LineID);
			wParamMap.put("CustomerID", wAPSTriggerPart.CustomerID);
			wParamMap.put("PartID", wAPSTriggerPart.PartID);
			wParamMap.put("CreateID", wAPSTriggerPart.CreateID);
			wParamMap.put("CreateTime", wAPSTriggerPart.CreateTime);
			wParamMap.put("Active", wAPSTriggerPart.Active);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wAPSTriggerPart.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wAPSTriggerPart.setID(wResult);
			} else {
				wResult = wAPSTriggerPart.getID();
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
	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<APSTriggerPart> wList,
			OutResult<Integer> wErrorCode) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wList == null || wList.size() <= 0)
				return wResult;

			List<String> wIDList = new ArrayList<String>();
			for (APSTriggerPart wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = MessageFormat.format("delete from {1}.aps_triggerpart WHERE ID IN({0}) ;",
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
	public APSTriggerPart SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		APSTriggerPart wResult = new APSTriggerPart();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<APSTriggerPart> wList = SelectList(wLoginUser, wID, -1, -1, -1, -1, wErrorCode);
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
	public List<APSTriggerPart> SelectList(BMSEmployee wLoginUser, int wID, int wProductID, int wLineID,
			int wCustomerID, int wActive, OutResult<Integer> wErrorCode) {
		List<APSTriggerPart> wResultList = new ArrayList<APSTriggerPart>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = MessageFormat.format("SELECT * FROM {0}.aps_triggerpart WHERE  1=1  "
					+ "and ( :wID <= 0 or :wID = ID ) " + "and ( :wProductID <= 0 or :wProductID = ProductID ) "
					+ "and ( :wLineID <= 0 or :wLineID = LineID ) "
					+ "and ( :wCustomerID <= 0 or :wCustomerID = CustomerID ) "
					+ "and ( :wActive < 0 or :wActive = Active );", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wProductID", wProductID);
			wParamMap.put("wLineID", wLineID);
			wParamMap.put("wCustomerID", wCustomerID);
			wParamMap.put("wActive", wActive);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				APSTriggerPart wItem = new APSTriggerPart();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.ProductID = StringUtils.parseInt(wReader.get("ProductID"));
				wItem.LineID = StringUtils.parseInt(wReader.get("LineID"));
				wItem.CustomerID = StringUtils.parseInt(wReader.get("CustomerID"));
				wItem.PartID = StringUtils.parseInt(wReader.get("PartID"));
				wItem.CreateID = StringUtils.parseInt(wReader.get("CreateID"));
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				wItem.Active = StringUtils.parseInt(wReader.get("Active"));

				wItem.ProductNo = APSConstans.GetFPCProductName(wItem.ProductID);
				wItem.LineName = APSConstans.GetFMCLineName(wItem.LineID);
				wItem.CustomerName = APSConstans.GetCRMCustomerName(wItem.CustomerID);
				wItem.PartName = APSConstans.GetFPCPartName(wItem.PartID);
				wItem.CreateName = APSConstans.GetBMSEmployeeName(wItem.CreateID);

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
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
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
				APSTriggerPart wAPSTriggerPart = SelectByID(wLoginUser, wItem, wErrorCode);
				if (wAPSTriggerPart == null || wAPSTriggerPart.ID <= 0)
					continue;
				wAPSTriggerPart.Active = wActive;
				long wID = Update(wLoginUser, wAPSTriggerPart, wErrorCode);
				if (wID <= 0)
					break;
			}
		} catch (Exception e) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(e.toString());
		}
		return wResult;
	}

	private APSTriggerPartDAO() {
		super();
	}

	public static APSTriggerPartDAO getInstance() {
		if (Instance == null)
			Instance = new APSTriggerPartDAO();
		return Instance;
	}
}
