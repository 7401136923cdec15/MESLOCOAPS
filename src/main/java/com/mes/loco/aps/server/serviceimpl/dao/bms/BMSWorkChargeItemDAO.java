package com.mes.loco.aps.server.serviceimpl.dao.bms;

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
import com.mes.loco.aps.server.service.po.bms.BMSWorkChargeItem;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.bms.BMSProductClassPart;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSConstans;

/**
 * 班组工位车型配置工具类
 * 
 * @author YouWang·Peng
 */
public class BMSWorkChargeItemDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(BMSWorkChargeItemDAO.class);

	private static BMSWorkChargeItemDAO Instance = null;

	/**
	 * 添加或修改
	 * 
	 * @param wBMSWorkChargeItem
	 * @return
	 */
	public int Update(BMSEmployee wLoginUser, BMSWorkChargeItem wBMSWorkChargeItem, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wBMSWorkChargeItem == null)
				return 0;

			String wSQL = "";
			if (wBMSWorkChargeItem.getID() <= 0) {
				wSQL = MessageFormat.format(
						"INSERT INTO {0}.bms_workchargeitem(WorkChargeID,ProductID) VALUES(:WorkChargeID,:ProductID);",
						wInstance.Result);
			} else {
				wSQL = MessageFormat.format(
						"UPDATE {0}.bms_workchargeitem SET WorkChargeID = :WorkChargeID,ProductID = :ProductID WHERE ID = :ID;",
						wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wBMSWorkChargeItem.ID);
			wParamMap.put("WorkChargeID", wBMSWorkChargeItem.WorkChargeID);
			wParamMap.put("ProductID", wBMSWorkChargeItem.ProductID);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wBMSWorkChargeItem.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wBMSWorkChargeItem.setID(wResult);
			} else {
				wResult = wBMSWorkChargeItem.getID();
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
	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<BMSWorkChargeItem> wList,
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
			for (BMSWorkChargeItem wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = MessageFormat.format("delete from {1}.bms_workchargeitem WHERE ID IN({0}) ;",
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
	public BMSWorkChargeItem SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		BMSWorkChargeItem wResult = new BMSWorkChargeItem();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<BMSWorkChargeItem> wList = SelectList(wLoginUser, wID, -1, -1, wErrorCode);
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
	public List<BMSWorkChargeItem> SelectList(BMSEmployee wLoginUser, int wID, int wWorkChargeID, int wProductID,
			OutResult<Integer> wErrorCode) {
		List<BMSWorkChargeItem> wResultList = new ArrayList<BMSWorkChargeItem>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = MessageFormat
					.format("SELECT * FROM {0}.bms_workchargeitem WHERE  1=1  and ( :wID <= 0 or :wID = ID ) "
							+ "and ( :wWorkChargeID <= 0 or :wWorkChargeID = WorkChargeID ) "
							+ "and ( :wProductID <= 0 or :wProductID = ProductID );", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wWorkChargeID", wWorkChargeID);
			wParamMap.put("wProductID", wProductID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				BMSWorkChargeItem wItem = new BMSWorkChargeItem();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.WorkChargeID = StringUtils.parseInt(wReader.get("WorkChargeID"));
				wItem.ProductID = StringUtils.parseInt(wReader.get("ProductID"));
				wItem.ProductNo = APSConstans.GetFPCProductName(wItem.ProductID);

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	private BMSWorkChargeItemDAO() {
		super();
	}

	public static BMSWorkChargeItemDAO getInstance() {
		if (Instance == null)
			Instance = new BMSWorkChargeItemDAO();
		return Instance;
	}

	/**
	 * 获取车型、班组、工位集合
	 */
	public List<BMSProductClassPart> GetProductClassPartList(BMSEmployee wLoginUser, int departmentID,
			OutResult<Integer> wErrorCode) {
		List<BMSProductClassPart> wResult = new ArrayList<BMSProductClassPart>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			ServiceResult<String> wInstance1 = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance1.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("SELECT t1.StationID,t1.ClassID,t2.ProductID,t3.ProductNo FROM "
					+ "{1}.bms_workcharge t1,{0}.bms_workchargeitem t2,{1}.fpc_product t3 "
					+ "where t1.ID=t2.WorkChargeID and t2.ProductID=t3.ID and t1.classID=:departmentID and t1.Active=1;",
					wInstance.Result, wInstance1.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("departmentID", departmentID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				BMSProductClassPart wItem = new BMSProductClassPart();

				wItem.ProductID = StringUtils.parseInt(wReader.get("ProductID"));
				wItem.ProductNo = StringUtils.parseString(wReader.get("ProductNo"));
				wItem.ClassID = StringUtils.parseInt(wReader.get("ClassID"));
				wItem.PartID = StringUtils.parseInt(wReader.get("StationID"));

				wResult.add(wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}
}
