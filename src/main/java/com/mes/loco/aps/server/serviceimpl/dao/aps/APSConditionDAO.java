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
import com.mes.loco.aps.server.service.po.aps.APSCondition;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;

public class APSConditionDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(APSConditionDAO.class);

	private static APSConditionDAO Instance = null;

	/**
	 * 添加或修改
	 * 
	 * @param wAPSCondition
	 * @return
	 */
	public int Update(BMSEmployee wLoginUser, APSCondition wAPSCondition, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wAPSCondition == null)
				return 0;

			String wSQL = "";
			if (wAPSCondition.getID() <= 0) {
				wSQL = StringUtils.Format("INSERT INTO {0}.aps_condition(OMSOrderPrioritys,LineOrders,"
						+ "CustomerOrders,ProductIDs,LimitMinutes,RedundantDays,hiftPeriod,EditID,"
						+ "EditTime) VALUES(:OMSOrderPrioritys,:LineOrders,:CustomerOrders,"
						+ ":ProductIDs,:LimitMinutes,:RedundantDays,:ShiftPeriod,:EditID,:EditTime);", wInstance.Result);
			} else {
				wSQL = StringUtils.Format("UPDATE {0}.aps_condition SET OMSOrderPrioritys = :OMSOrderPrioritys,"
						+ "LineOrders = :LineOrders,CustomerOrders = :CustomerOrders,"
						+ "ProductIDs = :ProductIDs,LimitMinutes = :LimitMinutes,RedundantDays=:RedundantDays,"
						+ "ShiftPeriod = :ShiftPeriod,EditID = :EditID,EditTime = :EditTime " + "WHERE ID = :ID;",
						wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			String wOMSOrderPrioritys = wAPSCondition.OMSOrderPrioritys == null
					|| wAPSCondition.OMSOrderPrioritys.size() <= 0 ? ""
							: StringUtils.Join(",", wAPSCondition.OMSOrderPrioritys);
			String wLineOrders = wAPSCondition.LineOrders == null || wAPSCondition.LineOrders.size() <= 0 ? ""
					: StringUtils.Join(",", wAPSCondition.LineOrders);
			String wCustomerOrders = wAPSCondition.CustomerOrders == null || wAPSCondition.CustomerOrders.size() <= 0
					? ""
					: StringUtils.Join(",", wAPSCondition.CustomerOrders);
			String wProductIDs = wAPSCondition.ProductIDs == null || wAPSCondition.ProductIDs.size() <= 0 ? ""
					: StringUtils.Join(",", wAPSCondition.ProductIDs);

			wParamMap.put("ID", wAPSCondition.ID);
			wParamMap.put("OMSOrderPrioritys", wOMSOrderPrioritys);
			wParamMap.put("LineOrders", wLineOrders);
			wParamMap.put("CustomerOrders", wCustomerOrders);
			wParamMap.put("ProductIDs", wProductIDs);
			wParamMap.put("LimitMinutes", wAPSCondition.LimitMinutes);
			wParamMap.put("RedundantDays", wAPSCondition.RedundantDays);
			wParamMap.put("ShiftPeriod", wAPSCondition.ShiftPeriod);
			wParamMap.put("EditID", wAPSCondition.EditID);
			wParamMap.put("EditTime", wAPSCondition.EditTime);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wAPSCondition.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wAPSCondition.setID(wResult);
			} else {
				wResult = wAPSCondition.getID();
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
	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<APSCondition> wList,
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
			for (APSCondition wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = StringUtils.Format("delete from {1}.aps_condition WHERE ID IN({0}) ;",
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
	public APSCondition SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		APSCondition wResult = new APSCondition();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<APSCondition> wList = SelectList(wLoginUser, wID, -1, wErrorCode);
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
	public List<APSCondition> SelectList(BMSEmployee wLoginUser, int wID, int wShiftPeriod,
			OutResult<Integer> wErrorCode) {
		List<APSCondition> wResultList = new ArrayList<APSCondition>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = StringUtils.Format("SELECT * FROM {0}.aps_condition WHERE  1=1  "
					+ "and ( :wID <= 0 or :wID = ID ) " + "and ( :wShiftPeriod <= 0 or :wShiftPeriod = ShiftPeriod );",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wShiftPeriod", wShiftPeriod);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				APSCondition wItem = new APSCondition();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));

				List<Integer> wOMSOrderPriorityList = new ArrayList<Integer>();
				List<Integer> wLineOrderList = new ArrayList<Integer>();
				List<Integer> wCustomerOrderList = new ArrayList<Integer>();
				List<Integer> wProductIDList = new ArrayList<Integer>();

				String wOMSOrderPrioritys = StringUtils.parseString(wReader.get("OMSOrderPrioritys"));
				String wLineOrders = StringUtils.parseString(wReader.get("LineOrders"));
				String wCustomerOrders = StringUtils.parseString(wReader.get("CustomerOrders"));
				String wProductIDs = StringUtils.parseString(wReader.get("ProductIDs"));

				if (StringUtils.isNotEmpty(wOMSOrderPrioritys)) {
					String[] wIDs = wOMSOrderPrioritys.split(",");
					for (String wItemID : wIDs) {
						wOMSOrderPriorityList.add(Integer.parseInt(wItemID));
					}
				}
				if (StringUtils.isNotEmpty(wLineOrders)) {
					String[] wIDs = wLineOrders.split(",");
					for (String wItemID : wIDs) {
						wLineOrderList.add(Integer.parseInt(wItemID));
					}
				}
				if (StringUtils.isNotEmpty(wCustomerOrders)) {
					String[] wIDs = wCustomerOrders.split(",");
					for (String wItemID : wIDs) {
						wCustomerOrderList.add(Integer.parseInt(wItemID));
					}
				}
				if (StringUtils.isNotEmpty(wProductIDs)) {
					String[] wIDs = wProductIDs.split(",");
					for (String wItemID : wIDs) {
						wProductIDList.add(Integer.parseInt(wItemID));
					}
				}

				wItem.OMSOrderPrioritys = wOMSOrderPriorityList;
				wItem.LineOrders = wLineOrderList;
				wItem.CustomerOrders = wCustomerOrderList;
				wItem.ProductIDs = wProductIDList;
				wItem.LimitMinutes = StringUtils.parseInt(wReader.get("LimitMinutes"));
				wItem.RedundantDays = StringUtils.parseInt(wReader.get("RedundantDays"));
				wItem.ShiftPeriod = StringUtils.parseInt(wReader.get("ShiftPeriod"));
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

	private APSConditionDAO() {
		super();
	}

	public static APSConditionDAO getInstance() {
		if (Instance == null)
			Instance = new APSConditionDAO();
		return Instance;
	}
}
