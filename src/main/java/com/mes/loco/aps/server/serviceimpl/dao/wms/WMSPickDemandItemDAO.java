package com.mes.loco.aps.server.serviceimpl.dao.wms;

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
import com.mes.loco.aps.server.service.po.wms.WMSPickDemandItem;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;

public class WMSPickDemandItemDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(WMSPickDemandItemDAO.class);

	private static WMSPickDemandItemDAO Instance = null;

	/**
	 * 添加或修改
	 * 
	 * @param wWMSPickDemandItem
	 * @return
	 */
	public int Update(BMSEmployee wLoginUser, WMSPickDemandItem wWMSPickDemandItem, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wWMSPickDemandItem == null)
				return 0;

			String wSQL = "";
			if (wWMSPickDemandItem.getID() <= 0) {
				wSQL = MessageFormat.format("INSERT INTO {0}.wms_pickdemanditem(DemandID,MaterialID,MaterialNo,"
						+ "MaterialName,FQTY,WBSNo,PartPointID,PartPointCode,PartPointName,"
						+ "RowNo,GroupFlag,ReplaceType,ReplaceTypeText,OutSourceType,OutSourceTypeText,AssessmentType,KittingFlag) "
						+ "VALUES(:DemandID,:MaterialID,:MaterialNo,:MaterialName,:FQTY,:WBSNo,:PartPointID,"
						+ ":PartPointCode,:PartPointName,:RowNo,:GroupFlag,:ReplaceType,:ReplaceTypeText,"
						+ ":OutSourceType,:OutSourceTypeText,:AssessmentType,:KittingFlag);", wInstance.Result);
			} else {
				wSQL = MessageFormat.format(
						"UPDATE {0}.wms_pickdemanditem SET DemandID = :DemandID,MaterialID = :MaterialID,"
								+ "MaterialNo = :MaterialNo,MaterialName = :MaterialName,FQTY = :FQTY,WBSNo = :WBSNo,"
								+ "PartPointID = :PartPointID,PartPointCode = :PartPointCode,PartPointName = :PartPointName,"
								+ "RowNo = :RowNo,GroupFlag = :GroupFlag,ReplaceType = :ReplaceType,ReplaceTypeText = :ReplaceTypeText,"
								+ "OutSourceType = :OutSourceType,OutSourceTypeText = :OutSourceTypeText,AssessmentType=:AssessmentType,KittingFlag=:KittingFlag WHERE ID = :ID;",
						wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wWMSPickDemandItem.ID);
			wParamMap.put("DemandID", wWMSPickDemandItem.DemandID);
			wParamMap.put("MaterialID", wWMSPickDemandItem.MaterialID);
			wParamMap.put("MaterialNo", wWMSPickDemandItem.MaterialNo);
			wParamMap.put("MaterialName", wWMSPickDemandItem.MaterialName);
			wParamMap.put("FQTY", wWMSPickDemandItem.FQTY);
			wParamMap.put("WBSNo", wWMSPickDemandItem.WBSNo);
			wParamMap.put("PartPointID", wWMSPickDemandItem.PartPointID);
			wParamMap.put("PartPointCode", wWMSPickDemandItem.PartPointCode);
			wParamMap.put("PartPointName", wWMSPickDemandItem.PartPointName);
			wParamMap.put("RowNo", wWMSPickDemandItem.RowNo);
			wParamMap.put("GroupFlag", wWMSPickDemandItem.GroupFlag);
			wParamMap.put("ReplaceType", wWMSPickDemandItem.ReplaceType);
			wParamMap.put("ReplaceTypeText", wWMSPickDemandItem.ReplaceTypeText);
			wParamMap.put("OutSourceType", wWMSPickDemandItem.OutSourceType);
			wParamMap.put("OutSourceTypeText", wWMSPickDemandItem.OutSourceTypeText);
			wParamMap.put("AssessmentType", wWMSPickDemandItem.AssessmentType);
			wParamMap.put("KittingFlag", wWMSPickDemandItem.KittingFlag);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wWMSPickDemandItem.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wWMSPickDemandItem.setID(wResult);
			} else {
				wResult = wWMSPickDemandItem.getID();
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
	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<WMSPickDemandItem> wList,
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
			for (WMSPickDemandItem wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = MessageFormat.format("delete from {1}.wms_pickdemanditem WHERE ID IN({0}) ;",
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
	public WMSPickDemandItem SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		WMSPickDemandItem wResult = new WMSPickDemandItem();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<WMSPickDemandItem> wList = SelectList(wLoginUser, wID, -1, -1, "", -1, wErrorCode);
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
	public List<WMSPickDemandItem> SelectList(BMSEmployee wLoginUser, int wID, int wDemandID, int wMaterialID,
			String wMaterialNo, int wPartPointID, OutResult<Integer> wErrorCode) {
		List<WMSPickDemandItem> wResultList = new ArrayList<WMSPickDemandItem>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = MessageFormat.format(
					"SELECT * FROM {0}.wms_pickdemanditem WHERE  1=1  and ( :wID <= 0 or :wID = ID ) and ( :wDemandID <= 0 or :wDemandID = DemandID ) and ( :wMaterialID <= 0 or :wMaterialID = MaterialID ) and ( :wMaterialNo is null or :wMaterialNo = '''' or :wMaterialNo = MaterialNo ) and ( :wPartPointID <= 0 or :wPartPointID = PartPointID );",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wDemandID", wDemandID);
			wParamMap.put("wMaterialID", wMaterialID);
			wParamMap.put("wMaterialNo", wMaterialNo);
			wParamMap.put("wPartPointID", wPartPointID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				WMSPickDemandItem wItem = new WMSPickDemandItem();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.DemandID = StringUtils.parseInt(wReader.get("DemandID"));
				wItem.MaterialID = StringUtils.parseInt(wReader.get("MaterialID"));
				wItem.MaterialNo = StringUtils.parseString(wReader.get("MaterialNo"));
				wItem.MaterialName = StringUtils.parseString(wReader.get("MaterialName"));
				wItem.FQTY = StringUtils.parseDouble(wReader.get("FQTY"));
				wItem.WBSNo = StringUtils.parseString(wReader.get("WBSNo"));
				wItem.PartPointID = StringUtils.parseInt(wReader.get("PartPointID"));
				wItem.PartPointCode = StringUtils.parseString(wReader.get("PartPointCode"));
				wItem.PartPointName = StringUtils.parseString(wReader.get("PartPointName"));
				wItem.RowNo = StringUtils.parseString(wReader.get("RowNo"));
				wItem.GroupFlag = StringUtils.parseString(wReader.get("GroupFlag"));
				wItem.ReplaceType = StringUtils.parseInt(wReader.get("ReplaceType"));
				wItem.ReplaceTypeText = StringUtils.parseString(wReader.get("ReplaceTypeText"));
				wItem.OutSourceType = StringUtils.parseInt(wReader.get("OutSourceType"));
				wItem.OutSourceTypeText = StringUtils.parseString(wReader.get("OutSourceTypeText"));
				wItem.AssessmentType = StringUtils.parseString(wReader.get("AssessmentType"));
				wItem.KittingFlag = StringUtils.parseString(wReader.get("KittingFlag"));

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	private WMSPickDemandItemDAO() {
		super();
	}

	public static WMSPickDemandItemDAO getInstance() {
		if (Instance == null)
			Instance = new WMSPickDemandItemDAO();
		return Instance;
	}

	public List<Integer> SelectDemandList(BMSEmployee wLoginUser, String wMaterial, OutResult<Integer> wErrorCode) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("SELECT distinct DemandID FROM {0}.wms_pickdemanditem where "
					+ "MaterialNo like ''%{1}%'' or MaterialName like ''%{1}%'';", wInstance.Result, wMaterial);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wDemandID = StringUtils.parseInt(wReader.get("DemandID"));
				if (wDemandID > 0 && !wResult.stream().anyMatch(p -> p.intValue() == wDemandID))
					wResult.add(wDemandID);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}
}
