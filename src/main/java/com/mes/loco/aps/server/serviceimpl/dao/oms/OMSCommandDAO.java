package com.mes.loco.aps.server.serviceimpl.dao.oms;

import com.mes.loco.aps.server.service.mesenum.MESDBSource;
import com.mes.loco.aps.server.service.mesenum.MESException;
import com.mes.loco.aps.server.service.po.OutResult;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.oms.OMSCommand;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;
import com.mes.loco.aps.server.serviceimpl.dao.oms.OMSCommandDAO;
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

public class OMSCommandDAO extends BaseDAO {
	private static Logger logger = LoggerFactory.getLogger(OMSCommandDAO.class);

	private static OMSCommandDAO Instance = null;

	public static OMSCommandDAO getInstance() {
		if (Instance == null)
			Instance = new OMSCommandDAO();
		return Instance;
	}

	public int Update(BMSEmployee wLoginUser, OMSCommand wOMSCommand, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			if (wOMSCommand == null) {
				return 0;
			}
			String wSQL = "";
			if (wOMSCommand.getID() <= 0) {
				wSQL = StringUtils.Format(
						"INSERT INTO {0}.oms_command(CustomerID,ContactCode,No,Status,LinkManID,EditorID,EditTime,Active,AuditorID,AuditTime,CreatorID,CreateTime,FactoryID,BusinessUnitID,FQTYPlan,FQTYActual,WBSNo) VALUES(:CustomerID,:ContactCode,:No,:Status,:LinkManID,:EditorID,:EditTime,:Active,:AuditorID,:AuditTime,:CreatorID,:CreateTime,:FactoryID,:BusinessUnitID,:FQTYPlan,:FQTYActual,:WBSNo);",
						new Object[] {

								wInstance.Result });
			} else {
				wSQL = StringUtils.Format(
						"UPDATE {0}.oms_command SET CustomerID = :CustomerID,ContactCode = :ContactCode,No = :No,Status = :Status,LinkManID = :LinkManID,EditorID = :EditorID,EditTime = :EditTime,Active = :Active,AuditorID = :AuditorID,AuditTime = :AuditTime,CreatorID = :CreatorID,CreateTime = :CreateTime,FactoryID = :FactoryID,BusinessUnitID = :BusinessUnitID,FQTYPlan = :FQTYPlan,FQTYActual = :FQTYActual,WBSNo=:WBSNo WHERE ID = :ID;",
						new Object[] {

								wInstance.Result });
			}
			wSQL = DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("ID", Integer.valueOf(wOMSCommand.ID));
			wParamMap.put("CustomerID", Integer.valueOf(wOMSCommand.CustomerID));
			wParamMap.put("ContactCode", wOMSCommand.ContactCode);
			wParamMap.put("No", wOMSCommand.No);
			wParamMap.put("Status", Integer.valueOf(wOMSCommand.Status));
			wParamMap.put("LinkManID", Integer.valueOf(wOMSCommand.LinkManID));
			wParamMap.put("EditorID", Integer.valueOf(wOMSCommand.EditorID));
			wParamMap.put("EditTime", wOMSCommand.EditTime);
			wParamMap.put("Active", Integer.valueOf(wOMSCommand.Active));
			wParamMap.put("AuditorID", Integer.valueOf(wOMSCommand.AuditorID));
			wParamMap.put("AuditTime", wOMSCommand.AuditTime);
			wParamMap.put("CreatorID", Integer.valueOf(wOMSCommand.CreatorID));
			wParamMap.put("CreateTime", wOMSCommand.CreateTime);
			wParamMap.put("FactoryID", Integer.valueOf(wOMSCommand.FactoryID));
			wParamMap.put("BusinessUnitID", Integer.valueOf(wOMSCommand.BusinessUnitID));
			wParamMap.put("FQTYPlan", Integer.valueOf(wOMSCommand.FQTYPlan));
			wParamMap.put("FQTYActual", Integer.valueOf(wOMSCommand.FQTYActual));
			wParamMap.put("WBSNo", wOMSCommand.WBSNo);

			GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
			MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource(wParamMap);

			this.nameJdbcTemplate.update(wSQL, (SqlParameterSource) mapSqlParameterSource,
					(KeyHolder) generatedKeyHolder);

			if (wOMSCommand.getID() <= 0) {
				wResult = generatedKeyHolder.getKey().intValue();
				wOMSCommand.setID(wResult);
			} else {
				wResult = wOMSCommand.getID();
			}
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResult;
	}

	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<OMSCommand> wList,
			OutResult<Integer> wErrorCode) {
		ServiceResult<Integer> wResult = new ServiceResult<>(Integer.valueOf(0));
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			if (wList == null || wList.size() <= 0) {
				return wResult;
			}
			List<String> wIDList = new ArrayList<>();
			for (OMSCommand wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = StringUtils.Format("delete from {1}.oms_command WHERE ID IN({0}) ;",
					String.join(",", wIDList), wInstance.Result);
			ExecuteSqlTransaction(wSql);
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResult;
	}

	public OMSCommand SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		OMSCommand wResult = new OMSCommand();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			List<OMSCommand> wList = SelectList(wLoginUser, wID, -1, -1, -1, "", wErrorCode);
			if (wList == null || wList.size() != 1)
				return wResult;
			wResult = wList.get(0);
		} catch (Exception e) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(e.toString());
		}
		return wResult;
	}

	public List<OMSCommand> SelectList(BMSEmployee wLoginUser, int wID, int wActive, int wFactoryID, int wProductID,
			String wWBSNo, OutResult<Integer> wErrorCode) {
		List<OMSCommand> wResultList = new ArrayList<>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResultList;
			}

			String wSQL = StringUtils.Format("SELECT * FROM {0}.oms_command WHERE  1=1  "
					+ "and ( :wID <= 0 or :wID = ID ) " + "and ( :wFactoryID <= 0 or :wFactoryID = FactoryID ) "
					+ "and ( :wWBSNo is null or :wWBSNo = '''' or :wWBSNo = WBSNo ) "
					+ "and ( :wBusinessUnitID <= 0 or :wBusinessUnitID = BusinessUnitID );", wInstance.Result);
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wID", Integer.valueOf(wID));
			wParamMap.put("wActive", Integer.valueOf(wActive));
			wParamMap.put("wFactoryID", Integer.valueOf(wFactoryID));
			wParamMap.put("wWBSNo", wWBSNo);
			wParamMap.put("wBusinessUnitID", Integer.valueOf(wProductID));

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				OMSCommand wItem = new OMSCommand();

				wItem.ID = StringUtils.parseInt(wReader.get("ID")).intValue();
				wItem.CustomerID = StringUtils.parseInt(wReader.get("CustomerID")).intValue();
				wItem.ContactCode = StringUtils.parseString(wReader.get("ContactCode"));
				wItem.No = StringUtils.parseString(wReader.get("No"));
				wItem.Status = StringUtils.parseInt(wReader.get("Status")).intValue();
				wItem.LinkManID = StringUtils.parseInt(wReader.get("LinkManID")).intValue();
				wItem.EditorID = StringUtils.parseInt(wReader.get("EditorID")).intValue();
				wItem.EditTime = StringUtils.parseCalendar(wReader.get("EditTime"));
				wItem.Active = StringUtils.parseInt(wReader.get("Active")).intValue();
				wItem.AuditorID = StringUtils.parseInt(wReader.get("AuditorID")).intValue();
				wItem.AuditTime = StringUtils.parseCalendar(wReader.get("AuditTime"));
				wItem.CreatorID = StringUtils.parseInt(wReader.get("CreatorID")).intValue();
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				wItem.FactoryID = StringUtils.parseInt(wReader.get("FactoryID")).intValue();
				wItem.BusinessUnitID = StringUtils.parseInt(wReader.get("BusinessUnitID")).intValue();
				wItem.FQTYPlan = StringUtils.parseInt(wReader.get("FQTYPlan")).intValue();
				wItem.FQTYActual = StringUtils.parseInt(wReader.get("FQTYActual")).intValue();
				wItem.WBSNo = StringUtils.parseString(wReader.get("WBSNo"));

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResultList;
	}

	public ServiceResult<Integer> Active(BMSEmployee wLoginUser, List<Integer> wIDList, int wActive,
			OutResult<Integer> wErrorCode) {
		ServiceResult<Integer> wResult = new ServiceResult<>(Integer.valueOf(0));
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			if (wIDList == null || wIDList.size() <= 0)
				return wResult;
			for (Integer wItem : wIDList) {
				OMSCommand wOMSCommand = SelectByID(wLoginUser, wItem.intValue(), wErrorCode);
				if (wOMSCommand == null || wOMSCommand.ID <= 0) {
					continue;
				}
				if (wActive == 2 && wOMSCommand.Active != 1) {
					wErrorCode.set(Integer.valueOf(MESException.Logic.getValue()));
					return wResult;
				}
				wOMSCommand.Active = wActive;
				long wID = Update(wLoginUser, wOMSCommand, wErrorCode);
				if (wID <= 0L)
					break;
			}
		} catch (Exception e) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 判断主订单是否有子订单
	 */
	public boolean IsHasChild(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		boolean wResult = false;
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("SELECT count(*) as Number from {0}.oms_order where CommandID=:wID;",
					wInstance.Result);
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wID", Integer.valueOf(wID));

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			int wNumber = 0;
			for (Map<String, Object> wReader : wQueryResult) {
				wNumber = StringUtils.parseInt(wReader.get("Number"));
				break;
			}

			if (wNumber > 0) {
				wResult = true;
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 查询重复的WBS号
	 */
	public List<String> SelectRepeatWBSNoList(BMSEmployee wLoginUser, OutResult<Integer> wErrorCode) {
		List<String> wResult = new ArrayList<String>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("SELECT distinct t1.WBSNo FROM {0}.oms_command t1,"
					+ "{0}.oms_command t2 where t1.WBSNo=t2.WBSNo and t1.ID !=t2.ID "
					+ "group by t1.ID order by t1.WBSNo asc ;", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				String wWBSNo = StringUtils.parseString(wReader.get("WBSNo"));
				if (StringUtils.isNotEmpty(wWBSNo)) {
					wResult.add(wWBSNo);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}
}