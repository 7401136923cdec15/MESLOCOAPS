package com.mes.loco.aps.server.serviceimpl.dao.aps;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
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
import com.mes.loco.aps.server.service.mesenum.APSTaskStatus;
import com.mes.loco.aps.server.service.mesenum.BPMEventModule;
import com.mes.loco.aps.server.service.mesenum.MESDBSource;
import com.mes.loco.aps.server.service.mesenum.MESException;
import com.mes.loco.aps.server.service.po.OutResult;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.aps.APSSchedulingVersion;
import com.mes.loco.aps.server.service.po.bfc.BFCAuditAction;
import com.mes.loco.aps.server.service.po.bfc.BFCAuditConfig;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.CoreServiceImpl;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;

public class APSSchedulingVersionDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(APSSchedulingVersionDAO.class);

	private static APSSchedulingVersionDAO Instance = null;

	private APSSchedulingVersionDAO() {
		super();
	}

	public static APSSchedulingVersionDAO getInstance() {
		if (Instance == null)
			Instance = new APSSchedulingVersionDAO();
		return Instance;
	}

	/**
	 * 添加或修改
	 * 
	 * @param wAPSSchedulingVersion
	 * @return
	 */
	public int Update(BMSEmployee wLoginUser, APSSchedulingVersion wAPSSchedulingVersion,
			OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wAPSSchedulingVersion == null)
				return 0;

			String wSQL = "";
			if (wAPSSchedulingVersion.getID() <= 0) {
				wSQL = StringUtils.Format("INSERT INTO {0}.aps_schedulingversion(VersionNo,"
						+ "APSShiftPeriod,TaskPartIDList,CreateID,CreateTime,StartTime,"
						+ "EndTime,Status) VALUES (:VersionNo,:APSShiftPeriod,:TaskPartIDList,:CreateID,"
						+ "now(),:StartTime," + ":EndTime,:Status);", wInstance.Result);
			} else {
				wSQL = StringUtils
						.Format("UPDATE {0}.aps_schedulingversion SET Status=:Status,TaskPartIDList = :TaskPartIDList,"
								+ " StartTime = :StartTime,  EndTime = :EndTime  WHERE ID = :ID;", wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wAPSSchedulingVersion.ID);
			wParamMap.put("VersionNo", wAPSSchedulingVersion.VersionNo);
			wParamMap.put("APSShiftPeriod", wAPSSchedulingVersion.APSShiftPeriod);
			wParamMap.put("TaskPartIDList", StringUtils.Join(",", wAPSSchedulingVersion.TaskPartIDList));
			wParamMap.put("CreateID", wAPSSchedulingVersion.CreateID);
			wParamMap.put("StartTime", wAPSSchedulingVersion.StartTime);
			wParamMap.put("EndTime", wAPSSchedulingVersion.EndTime);
			wParamMap.put("Status", wAPSSchedulingVersion.Status);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wAPSSchedulingVersion.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wAPSSchedulingVersion.setID(wResult);
			} else {
				wResult = wAPSSchedulingVersion.getID();
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResult;
	}

	public int Audit(BMSEmployee wLoginUser, APSSchedulingVersion wAPSSchedulingVersion, int wOperateType,
			OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			if (wAPSSchedulingVersion == null) {
				wErrorCode.set(MESException.Parameter.getValue());
				return wResult;
			}

			int wModuleID = 0;
			switch (APSShiftPeriod.getEnumType(wAPSSchedulingVersion.APSShiftPeriod)) {
			case Week:
				wModuleID = BPMEventModule.SCWeekAudit.getValue();
				break;
			case Month:
				wModuleID = BPMEventModule.SCMonthAudit.getValue();
				break;
			default:
				wErrorCode.set(MESException.Parameter.getValue());
				return wResult;
			}

			BFCAuditConfig wCurrentConfig = CoreServiceImpl.getInstance()
					.BFC_CurrentConfig(wLoginUser, wModuleID, wAPSSchedulingVersion.ID, wLoginUser.getID())
					.Info(BFCAuditConfig.class);
			if (wCurrentConfig == null || wCurrentConfig.ID <= 0) {
				wErrorCode.set(MESException.Logic.getValue());
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
				wErrorCode.set(MESException.Logic.getValue());
				return wResult;
			}

			BFCAuditAction wAction = new BFCAuditAction();
			wAction.TaskID = wAPSSchedulingVersion.ID;
			wAction.EventModule = wCurrentConfig.EventModule;
			wAction.ConfigID = wCurrentConfig.ID;
			wAction.ConfigName = wCurrentConfig.Name;
			wAction.AuditorID = wLoginUser.getID();
			wAction.AuditorName = wLoginUser.getName();
			wAction.Result = wOperateType;

			switch (APSOperateType.getEnumType(wOperateType)) {
			case Reject:
			case Cancel:
				wAPSSchedulingVersion.Status = APSTaskStatus.Saved.getValue();
				break;
			case Audit:
				wAPSSchedulingVersion.Status = APSTaskStatus.ToAudit.getValue();
				break;
			case Submit:
				wAPSSchedulingVersion.Status = APSTaskStatus.ToAudit.getValue();
				break;
			default:
				break;
			}

			// 提交Action
			String wNo = "";
			if (wAPSSchedulingVersion.APSShiftPeriod == APSShiftPeriod.Month.getValue()) {
				SimpleDateFormat wSDF = new SimpleDateFormat("yyyyMM");
				wNo = wSDF.format(Calendar.getInstance().getTime());
			} else if (wAPSSchedulingVersion.APSShiftPeriod == APSShiftPeriod.Week.getValue()) {
				Calendar wNow = Calendar.getInstance();
				SimpleDateFormat wSDF = new SimpleDateFormat("yyyyMM");
				wNo = wSDF.format(Calendar.getInstance().getTime());
				int wWeeks = wNow.get(Calendar.WEEK_OF_MONTH);
				wNo += wWeeks;
			}
			CoreServiceImpl.getInstance().BFC_UpdateAction(wLoginUser, wAction,
					StringUtils.Format("{0} {1}", BPMEventModule.getEnumType(wModuleID).getLable(), wNo));
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wOperateType == APSOperateType.Audit.getValue()) {
				wCurrentConfig = CoreServiceImpl.getInstance()
						.BFC_CurrentConfig(wLoginUser, wModuleID, wAPSSchedulingVersion.ID, wLoginUser.getID())
						.Info(BFCAuditConfig.class);
				if (wCurrentConfig == null || wCurrentConfig.ID <= 0) {
					wAPSSchedulingVersion.Status = APSTaskStatus.Audited.getValue();
				}
			}

			String wSQL = StringUtils.Format(
					"UPDATE {0}.aps_schedulingversion  SET   Status = :Status,AuditID=:AuditID,AuditTime=now()  WHERE ID = :ID;",
					wInstance.Result);

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wAPSSchedulingVersion.ID);
			wParamMap.put("Status", wAPSSchedulingVersion.Status);
			wParamMap.put("AuditID", wLoginUser.ID);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wAPSSchedulingVersion.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wAPSSchedulingVersion.setID(wResult);
			} else {
				wResult = wAPSSchedulingVersion.getID();
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
	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<APSSchedulingVersion> wList,
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
			for (APSSchedulingVersion wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = StringUtils.Format(
					"delete from {1}.aps_schedulingversion WHERE ID IN({0}) AND Status <= 1  ;",
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
	public APSSchedulingVersion SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		APSSchedulingVersion wResult = new APSSchedulingVersion();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<APSSchedulingVersion> wList = SelectList(wLoginUser, wID, -1, null, null, wErrorCode);
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
	public List<APSSchedulingVersion> SelectList(BMSEmployee wLoginUser, int wID, int wAPSShiftPeriod,
			Calendar wStartTime, List<Integer> wStatusList, OutResult<Integer> wErrorCode) {
		List<APSSchedulingVersion> wResultList = new ArrayList<APSSchedulingVersion>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1);
			if (wStartTime == null) {
				wStartTime = wBaseTime;
			}

			if (wStatusList == null) {
				wStatusList = new ArrayList<Integer>();
			}

			String wSQL = MessageFormat.format("SELECT * FROM {0}.aps_schedulingversion WHERE  1=1  "
					+ "and ( :wID <= 0 or :wID = ID ) "
					+ "and ( :wStartTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wStartTime >= StartTime) "
					+ "and ( :wStartTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wStartTime <= EndTime) "
					+ "and ( :wStatus is null or :wStatus = '''' or Status in ({1}))"
					+ "and ( :wAPSShiftPeriod <= 0 or :wAPSShiftPeriod = APSShiftPeriod );", wInstance.Result,
					wStatusList.size() > 0 ? StringUtils.Join(",", wStatusList) : "0");

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wAPSShiftPeriod", wAPSShiftPeriod);
			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wStatus", StringUtils.Join(",", wStatusList));

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetValue(wResultList, wQueryResult);
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	/**
	 * 赋值
	 */
	private void SetValue(List<APSSchedulingVersion> wResultList, List<Map<String, Object>> wQueryResult) {
		try {
			for (Map<String, Object> wReader : wQueryResult) {
				APSSchedulingVersion wItem = new APSSchedulingVersion();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.VersionNo = StringUtils.parseString(wReader.get("VersionNo"));
				wItem.APSShiftPeriod = StringUtils.parseInt(wReader.get("APSShiftPeriod"));
				wItem.TaskPartIDList = StringUtils
						.parseIntList(StringUtils.parseString(wReader.get("TaskPartIDList")).split(","));
				wItem.CreateID = StringUtils.parseInt(wReader.get("CreateID"));
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				wItem.StartTime = StringUtils.parseCalendar(wReader.get("StartTime"));
				wItem.EndTime = StringUtils.parseCalendar(wReader.get("EndTime"));
				wItem.Status = StringUtils.parseInt(wReader.get("Status"));
				wItem.AuditID = StringUtils.parseInt(wReader.get("AuditID"));
				wItem.AuditTime = StringUtils.parseCalendar(wReader.get("AuditTime"));

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}

	}

	/**
	 * 按照时间查询版本
	 * 
	 * @return
	 */
	public List<APSSchedulingVersion> SelectListByTime(BMSEmployee wLoginUser, int wAPSShiftPeriod, Calendar wStartTime,
			Calendar wEndTime, OutResult<Integer> wErrorCode) {
		List<APSSchedulingVersion> wResultList = new ArrayList<APSSchedulingVersion>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1);
			if (wStartTime == null) {
				wStartTime = wBaseTime;
			}
			if (wEndTime == null) {
				wEndTime = wBaseTime;
			}

			String wSQL = MessageFormat.format("SELECT * FROM {0}.aps_schedulingversion WHERE  1=1  "
					+ "and ( :wStartTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or CreateTime >= :wStartTime) "
					+ "and ( :wEndTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or CreateTime <= :wEndTime) "
					+ "and ( :wAPSShiftPeriod <= 0 or :wAPSShiftPeriod = APSShiftPeriod );", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wAPSShiftPeriod", wAPSShiftPeriod);
			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				APSSchedulingVersion wItem = new APSSchedulingVersion();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.VersionNo = StringUtils.parseString(wReader.get("VersionNo"));
				wItem.APSShiftPeriod = StringUtils.parseInt(wReader.get("APSShiftPeriod"));
				wItem.TaskPartIDList = StringUtils
						.parseIntList(StringUtils.parseString(wReader.get("TaskPartIDList")).split(","));
				wItem.CreateID = StringUtils.parseInt(wReader.get("CreateID"));
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				wItem.StartTime = StringUtils.parseCalendar(wReader.get("StartTime"));
				wItem.EndTime = StringUtils.parseCalendar(wReader.get("EndTime"));
				wItem.Status = StringUtils.parseInt(wReader.get("Status"));

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	/**
	 * 通过版本号获取排程版本数据
	 */
	public APSSchedulingVersion SelectByVersionNo(BMSEmployee wLoginUser, String wVersionNo,
			OutResult<Integer> wErrorCode) {
		APSSchedulingVersion wResult = new APSSchedulingVersion();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = MessageFormat.format(
					"SELECT * FROM {0}.aps_schedulingversion WHERE  1=1  "
							+ "and ( :wVersionNo is null or :wVersionNo = '''' or :wVersionNo = VersionNo);",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wVersionNo", wVersionNo);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			List<APSSchedulingVersion> wList = new ArrayList<APSSchedulingVersion>();
			SetValue(wList, wQueryResult);
			if (wList.size() > 0) {
				wResult = wList.get(0);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据工位ID查询版本号
	 */
	public String GetVersonNo(BMSEmployee wLoginUser, int wTaskPartID, OutResult<Integer> wErrorCode) {
		String wResult = "";
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("SELECT VersionNo FROM {0}.aps_schedulingversion where "
					+ "find_in_set( :wTaskPartID,replace(TaskPartIDList,'';'','',''));", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wTaskPartID", wTaskPartID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				wResult = StringUtils.parseString(wReader.get("VersionNo"));
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}
}