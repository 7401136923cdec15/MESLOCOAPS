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
import com.mes.loco.aps.server.service.po.aps.APSInstallation;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;

/**
 * 工位安装明细
 * 
 * @author YouWang·Peng
 * @CreateTime 2020-4-17 15:22:40
 * @LastEditTime 2020-4-17 15:22:43
 *
 */
public class APSInstallationDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(APSInstallationDAO.class);

	private static APSInstallationDAO Instance = null;
	
	/**
	 * 权限码
	 */
	private static int AccessCode = 501500;

	/**
	 * 添加或修改
	 * 
	 * @param wAPSInstallation
	 * @return
	 */
	public long Update(BMSEmployee wLoginUser, APSInstallation wAPSInstallation, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), AccessCode);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wAPSInstallation == null)
				return 0;

			String wSQL = "";
			if (wAPSInstallation.getID() <= 0) {
				wSQL = StringUtils.Format(
						"INSERT INTO {0}.aps_installation(LineID,PartID,MaterialID,"
								+ "ProductID,InstallCheckMode,CreatorID,CreateTime,"
								+ "EditorID,EditTime,AuditorID,AuditTime,Status) "
								+ "VALUES(:LineID,:PartID,:MaterialID,:ProductID,:InstallCheckMode,"
								+ ":CreatorID,:CreateTime,:EditorID,:EditTime,:AuditorID," + ":AuditTime,:Status);",
						wInstance.Result);
			} else {
				wSQL = StringUtils.Format(
						"UPDATE {0}.aps_installation SET LineID = :LineID,PartID = :PartID,"
								+ "MaterialID = :MaterialID,ProductID = :ProductID,"
								+ "InstallCheckMode = :InstallCheckMode,CreatorID = :CreatorID,"
								+ "CreateTime = :CreateTime,EditorID = :EditorID,EditTime = :EditTime,"
								+ "AuditorID = :AuditorID,AuditTime = :AuditTime,Status = :Status " + "WHERE ID = :ID;",
						wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wAPSInstallation.ID);
			wParamMap.put("LineID", wAPSInstallation.LineID);
			wParamMap.put("PartID", wAPSInstallation.PartID);
			wParamMap.put("MaterialID", wAPSInstallation.MaterialID);
			wParamMap.put("ProductID", wAPSInstallation.ProductID);
			wParamMap.put("InstallCheckMode", wAPSInstallation.InstallCheckMode);
//			wParamMap.put("IsInstall", wAPSInstallation.IsInstall ? 1 : 0);
//			wParamMap.put("IsThisPart", wAPSInstallation.IsThisPart ? 1 : 0);
			wParamMap.put("CreatorID", wAPSInstallation.CreatorID);
			wParamMap.put("CreateTime", wAPSInstallation.CreateTime);
			wParamMap.put("EditorID", wAPSInstallation.EditorID);
			wParamMap.put("EditTime", wAPSInstallation.EditTime);
			wParamMap.put("AuditorID", wAPSInstallation.AuditorID);
			wParamMap.put("AuditTime", wAPSInstallation.AuditTime);
			wParamMap.put("Status", wAPSInstallation.Status);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wAPSInstallation.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wAPSInstallation.setID(wResult);
			} else {
				wResult = wAPSInstallation.getID();
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
	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<APSInstallation> wList,
			OutResult<Integer> wErrorCode) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), AccessCode);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wList == null || wList.size() <= 0)
				return wResult;

			List<String> wIDList = new ArrayList<String>();
			for (APSInstallation wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = StringUtils.Format("delete from {1}.aps_installation WHERE ID IN({0}) ;",
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
	public APSInstallation SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		APSInstallation wResult = new APSInstallation();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<APSInstallation> wList = SelectList(wLoginUser, wID, -1, -1, -1, -1, wErrorCode);
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
	public List<APSInstallation> SelectList(BMSEmployee wLoginUser, int wID, int wLineID, int wPartID, int wMaterialID,
			int wProductID, OutResult<Integer> wErrorCode) {
		List<APSInstallation> wResultList = new ArrayList<APSInstallation>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = StringUtils.Format("SELECT * FROM {0}.aps_installation WHERE  1=1  "
					+ "and ( :wID <= 0 or :wID = ID ) " + "and ( :wLineID <= 0 or :wLineID = LineID ) "
					+ "and ( :wPartID <= 0 or :wPartID = PartID ) "
					+ "and ( :wMaterialID <= 0 or :wMaterialID = MaterialID ) "
					+ "and ( :wProductID <= 0 or :wProductID = ProductID );", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wLineID", wLineID);
			wParamMap.put("wPartID", wPartID);
			wParamMap.put("wMaterialID", wMaterialID);
			wParamMap.put("wProductID", wProductID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				APSInstallation wItem = new APSInstallation();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.LineID = StringUtils.parseInt(wReader.get("LineID"));
				wItem.PartID = StringUtils.parseInt(wReader.get("PartID"));
				wItem.MaterialID = StringUtils.parseInt(wReader.get("MaterialID"));
				wItem.ProductID = StringUtils.parseInt(wReader.get("ProductID"));
//				wItem.IsInstall = StringUtils.parseInt(wReader.get("IsInstall")) == 1 ? true : false;
//				wItem.IsThisPart = StringUtils.parseInt(wReader.get("IsThisPart")) == 1 ? true : false;
				wItem.InstallCheckMode = StringUtils.parseInt(wReader.get("InstallCheckMode"));
				wItem.CreatorID = StringUtils.parseInt(wReader.get("CreatorID"));
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				wItem.EditorID = StringUtils.parseInt(wReader.get("EditorID"));
				wItem.EditTime = StringUtils.parseCalendar(wReader.get("EditTime"));
				wItem.AuditorID = StringUtils.parseInt(wReader.get("AuditorID"));
				wItem.AuditTime = StringUtils.parseCalendar(wReader.get("AuditTime"));
				wItem.Status = StringUtils.parseInt(wReader.get("Status"));

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	private APSInstallationDAO() {
		super();
	}

	public static APSInstallationDAO getInstance() {
		if (Instance == null)
			Instance = new APSInstallationDAO();
		return Instance;
	}
}
