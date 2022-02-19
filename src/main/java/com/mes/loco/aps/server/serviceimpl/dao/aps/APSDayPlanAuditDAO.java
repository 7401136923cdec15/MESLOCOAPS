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

import com.mes.loco.aps.server.service.mesenum.APSOperateType;
import com.mes.loco.aps.server.service.mesenum.APSShiftPeriod;
import com.mes.loco.aps.server.service.mesenum.BPMEventModule;
import com.mes.loco.aps.server.service.mesenum.BPMStatus;
import com.mes.loco.aps.server.service.mesenum.FMCShiftLevel;
import com.mes.loco.aps.server.service.mesenum.MESDBSource;
import com.mes.loco.aps.server.service.mesenum.MESException;
import com.mes.loco.aps.server.service.po.OutResult;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.aps.APSDayPlanAudit;
import com.mes.loco.aps.server.service.po.bfc.BFCAuditAction;
import com.mes.loco.aps.server.service.po.bfc.BFCAuditConfig;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.CoreServiceImpl;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;
import com.mes.loco.aps.server.serviceimpl.utils.MESServer;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSConstans;

public class APSDayPlanAuditDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(APSDayPlanAuditDAO.class);

	private static APSDayPlanAuditDAO Instance = null;

	/**
	 * 添加或修改
	 * 
	 * @param wAPSDayPlanAudit
	 * @return
	 */
	public int Update(BMSEmployee wLoginUser, APSDayPlanAudit wAPSDayPlanAudit, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wAPSDayPlanAudit == null)
				return 0;

			String wSQL = "";
			if (wAPSDayPlanAudit.getID() <= 0) {
				wSQL = MessageFormat.format(
						"INSERT INTO {0}.aps_dayplanaudit(AreaID,ShiftID,CreateID,CreateTime,AuditID,AuditTime,Status) "
								+ "VALUES(:AreaID,:ShiftID,:CreateID,:CreateTime,:AuditID,:AuditTime,:Status);",
						wInstance.Result);
			} else {
				wSQL = MessageFormat.format("UPDATE {0}.aps_dayplanaudit SET AreaID = :AreaID,ShiftID = :ShiftID,"
						+ "CreateID = :CreateID,CreateTime = :CreateTime,AuditID = :AuditID,AuditTime = :AuditTime,"
						+ "Status = :Status WHERE ID = :ID;", wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wAPSDayPlanAudit.ID);
			wParamMap.put("AreaID", wAPSDayPlanAudit.AreaID);
			wParamMap.put("ShiftID", wAPSDayPlanAudit.ShiftID);
			wParamMap.put("CreateID", wAPSDayPlanAudit.CreateID);
			wParamMap.put("CreateTime", wAPSDayPlanAudit.CreateTime);
			wParamMap.put("AuditID", wAPSDayPlanAudit.AuditID);
			wParamMap.put("AuditTime", wAPSDayPlanAudit.AuditTime);
			wParamMap.put("Status", wAPSDayPlanAudit.Status);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wAPSDayPlanAudit.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wAPSDayPlanAudit.setID(wResult);
			} else {
				wResult = wAPSDayPlanAudit.getID();
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 关闭过期的审批单
	 * 
	 * @param wAPSDayPlanAuditBPM
	 * @return
	 */
	public void CloseAreaAudit(BMSEmployee wLoginUser, int wAreaID, OutResult<Integer> wErrorCode) {
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.APS, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return;
			}

			String wSQL = StringUtils.Format(
					"UPDATE {0}.aps_dayplanaudit Set Status=:Status WHERE AreaID=:AreaID AND Status != :Status and ID>0;",
					wInstance.Result);

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("Status", BPMStatus.Audited.getValue());
			wParamMap.put("AreaID", wAreaID);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
	}

	/**
	 * 删除集合
	 * 
	 * @param wList
	 */
	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<APSDayPlanAudit> wList,
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
			for (APSDayPlanAudit wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = MessageFormat.format("delete from {1}.aps_dayplanaudit WHERE ID IN({0}) ;",
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
	public APSDayPlanAudit SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		APSDayPlanAudit wResult = new APSDayPlanAudit();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<APSDayPlanAudit> wList = SelectList(wLoginUser, wID, -1, -1, null, wErrorCode);
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
	public List<APSDayPlanAudit> SelectList(BMSEmployee wLoginUser, int wID, int wAreaID, int wShiftID,
			List<Integer> wStateIDList, OutResult<Integer> wErrorCode) {
		List<APSDayPlanAudit> wResultList = new ArrayList<APSDayPlanAudit>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			if (wStateIDList == null) {
				wStateIDList = new ArrayList<Integer>();
			}

			String wSQL = MessageFormat.format(
					"SELECT * FROM {0}.aps_dayplanaudit WHERE  1=1  " + "and ( :wID <= 0 or :wID = ID ) "
							+ "and ( :wAreaID <= 0 or :wAreaID = AreaID ) "
							+ "and ( :wShiftID <= 0 or :wShiftID = ShiftID ) "
							+ "and ( :wStatus is null or :wStatus = '''' or Status in ({1}));",
					wInstance.Result, wStateIDList.size() > 0 ? StringUtils.Join(",", wStateIDList) : "0");

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wAreaID", wAreaID);
			wParamMap.put("wShiftID", wShiftID);
			wParamMap.put("wStatus", StringUtils.Join(",", wStateIDList));

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				APSDayPlanAudit wItem = new APSDayPlanAudit();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.AreaID = StringUtils.parseInt(wReader.get("AreaID"));
				wItem.ShiftID = StringUtils.parseInt(wReader.get("ShiftID"));
				wItem.CreateID = StringUtils.parseInt(wReader.get("CreateID"));
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				wItem.AuditID = StringUtils.parseInt(wReader.get("AuditID"));
				wItem.AuditTime = StringUtils.parseCalendar(wReader.get("AuditTime"));
				wItem.Status = StringUtils.parseInt(wReader.get("Status"));

				wItem.AreaName = APSConstans.GetBMSDepartmentName(wItem.AreaID);
				wItem.Creator = APSConstans.GetBMSEmployeeName(wItem.CreateID);
				wItem.Auditor = APSConstans.GetBMSEmployeeName(wItem.AuditID);

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	public int Audit(BMSEmployee wLoginUser, APSDayPlanAudit wAPSDayPlanAudit, int wOperateType,
			OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			if (wAPSDayPlanAudit == null) {
				wErrorCode.set(MESException.Parameter.getValue());
				return wResult;
			}

			int wModuleID = BPMEventModule.SCDayAudit.getValue();

			BFCAuditConfig wCurrentConfig = CoreServiceImpl.getInstance()
					.BFC_CurrentConfig(wLoginUser, wModuleID, wAPSDayPlanAudit.ID, wLoginUser.getID())
					.Info(BFCAuditConfig.class);
			if (wCurrentConfig == null || wCurrentConfig.ID <= 0) {
				wErrorCode.set(MESException.UnPower.getValue());
				return wResult;
			}
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.APS,
					wCurrentConfig.FunctionID);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}
			if (wCurrentConfig.AuditActions != null
					&& wCurrentConfig.AuditActions.stream().filter(p -> p == wOperateType).count() == 0) {
				wErrorCode.set(MESException.UnPower.getValue());
				return wResult;
			}

			BFCAuditAction wAction = new BFCAuditAction();
			wAction.TaskID = wAPSDayPlanAudit.ID;
			wAction.EventModule = wCurrentConfig.EventModule;
			wAction.ConfigID = wCurrentConfig.ID;
			wAction.ConfigName = wCurrentConfig.Name;
			wAction.AuditorID = wLoginUser.getID();
			wAction.AuditorName = wLoginUser.getName();
			wAction.Result = wOperateType;

			switch (APSOperateType.getEnumType(wOperateType)) {
			case Reject:
			case Cancel:
				wAPSDayPlanAudit.Status = BPMStatus.Save.getValue();
				break;
			case Audit:
				wAPSDayPlanAudit.Status = BPMStatus.ToAudit.getValue();
				break;
			case Submit:
				wAPSDayPlanAudit.Status = BPMStatus.ToAudit.getValue();
				break;
			default:
				break;
			}

			// 提交Action
			int wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, Calendar.getInstance(), APSShiftPeriod.Day,
					FMCShiftLevel.Day, 0);
			CoreServiceImpl.getInstance().BFC_UpdateAction(wLoginUser, wAction,
					StringUtils.Format("{0} {1} {2}", BPMEventModule.getEnumType(wModuleID).getLable(),
							APSConstans.GetBMSDepartmentName(wAPSDayPlanAudit.AreaID), wShiftID));
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wOperateType == APSOperateType.Audit.getValue()) {
				wCurrentConfig = CoreServiceImpl.getInstance()
						.BFC_CurrentConfig(wLoginUser, wModuleID, wAPSDayPlanAudit.ID, wLoginUser.getID())
						.Info(BFCAuditConfig.class);
				if (wCurrentConfig == null || wCurrentConfig.ID <= 0) {
					wAPSDayPlanAudit.Status = BPMStatus.Audited.getValue();
				}
			}

			String wSQL = StringUtils.Format(
					"UPDATE {0}.aps_dayplanaudit  SET  Status = :Status,AuditID=:AuditID,AuditTime=now() WHERE ID = :ID;",
					wInstance.Result);

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wAPSDayPlanAudit.ID);
			wParamMap.put("Status", wAPSDayPlanAudit.Status);
			wParamMap.put("AuditID", wLoginUser.ID);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wAPSDayPlanAudit.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wAPSDayPlanAudit.setID(wResult);
			} else {
				wResult = wAPSDayPlanAudit.getID();
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResult;
	}

	private APSDayPlanAuditDAO() {
		super();
	}

	public static APSDayPlanAuditDAO getInstance() {
		if (Instance == null)
			Instance = new APSDayPlanAuditDAO();
		return Instance;
	}
}
