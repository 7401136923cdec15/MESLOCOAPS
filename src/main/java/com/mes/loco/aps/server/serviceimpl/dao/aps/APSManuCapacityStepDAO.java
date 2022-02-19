package com.mes.loco.aps.server.serviceimpl.dao.aps;

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
import com.mes.loco.aps.server.service.po.aps.APSManuCapacityStep;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.fpc.FPCPart;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSConstans;

public class APSManuCapacityStepDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(APSManuCapacityStepDAO.class);

	private static APSManuCapacityStepDAO Instance = null;

	/**
	 * 添加或修改
	 * 
	 * @param wAPSManuCapacityStep
	 * @return
	 */
	public int Update(BMSEmployee wLoginUser, APSManuCapacityStep wAPSManuCapacityStep, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wAPSManuCapacityStep == null)
				return 0;

			String wSQL = "";
			if (wAPSManuCapacityStep.getID() <= 0) {
				wSQL = MessageFormat.format(
						"INSERT INTO {0}.aps_manucapacitystep(LineID,PartID,StepID,FQTY,Period,WorkHour,CreatorID,CreateTime,EditTime,EditorID,AuditTime,AuditorID,Status,Active,WorkHours) VALUES(:LineID,:PartID,:StepID,:FQTY,:Period,:WorkHour,:CreatorID,:CreateTime,:EditTime,:EditorID,:AuditTime,:AuditorID,:Status,:Active,:WorkHours);",
						wInstance.Result);
			} else {
				wSQL = MessageFormat.format(
						"UPDATE {0}.aps_manucapacitystep SET LineID = :LineID,PartID = :PartID,StepID = :StepID,FQTY = :FQTY,Period = :Period,WorkHour = :WorkHour,CreatorID = :CreatorID,CreateTime = :CreateTime,EditTime = :EditTime,EditorID = :EditorID,AuditTime = :AuditTime,AuditorID = :AuditorID,Status = :Status,Active = :Active,WorkHours = :WorkHours WHERE ID = :ID;",
						wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wAPSManuCapacityStep.ID);
			wParamMap.put("LineID", wAPSManuCapacityStep.LineID);
			wParamMap.put("PartID", wAPSManuCapacityStep.PartID);
			wParamMap.put("StepID", wAPSManuCapacityStep.StepID);
			wParamMap.put("FQTY", wAPSManuCapacityStep.FQTY);
			wParamMap.put("Period", wAPSManuCapacityStep.Period);
			wParamMap.put("WorkHour", wAPSManuCapacityStep.WorkHour);
			wParamMap.put("CreatorID", wAPSManuCapacityStep.CreatorID);
			wParamMap.put("CreateTime", wAPSManuCapacityStep.CreateTime);
			wParamMap.put("EditTime", wAPSManuCapacityStep.EditTime);
			wParamMap.put("EditorID", wAPSManuCapacityStep.EditorID);
			wParamMap.put("AuditTime", wAPSManuCapacityStep.AuditTime);
			wParamMap.put("AuditorID", wAPSManuCapacityStep.AuditorID);
			wParamMap.put("Status", wAPSManuCapacityStep.Status);
			wParamMap.put("Active", wAPSManuCapacityStep.Active);
			wParamMap.put("WorkHours", wAPSManuCapacityStep.WorkHours);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wAPSManuCapacityStep.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wAPSManuCapacityStep.setID(wResult);
			} else {
				wResult = wAPSManuCapacityStep.getID();
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
	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<APSManuCapacityStep> wList,
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
			for (APSManuCapacityStep wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = MessageFormat.format("delete from {1}.aps_manucapacitystep WHERE ID IN({0}) ;",
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
	public APSManuCapacityStep SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		APSManuCapacityStep wResult = new APSManuCapacityStep();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<APSManuCapacityStep> wList = SelectList(wLoginUser, wID, -1, -1, -1, -1, -1, wErrorCode);
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
	public List<APSManuCapacityStep> SelectList(BMSEmployee wLoginUser, int wID, int wLineID, int wPartID, int wStepID,
			int wStatus, int wActive, OutResult<Integer> wErrorCode) {
		List<APSManuCapacityStep> wResultList = new ArrayList<APSManuCapacityStep>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = MessageFormat.format("SELECT * FROM {0}.aps_manucapacitystep WHERE  1=1  "
					+ "and ( :wID <= 0 or :wID = ID ) " + "and ( :wLineID <= 0 or :wLineID = LineID ) "
					+ "and ( :wPartID <= 0 or :wPartID = PartID ) " + "and ( :wStepID <= 0 or :wStepID = StepID ) "
					+ "and ( :wStatus <= 0 or :wStatus = Status ) " + "and ( :wActive <= 0 or :wActive = Active );",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wLineID", wLineID);
			wParamMap.put("wPartID", wPartID);
			wParamMap.put("wStepID", wStepID);
			wParamMap.put("wStatus", wStatus);
			wParamMap.put("wActive", wActive);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				APSManuCapacityStep wItem = new APSManuCapacityStep();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.LineID = StringUtils.parseInt(wReader.get("LineID"));
				wItem.PartID = StringUtils.parseInt(wReader.get("PartID"));
				wItem.StepID = StringUtils.parseInt(wReader.get("StepID"));
				wItem.FQTY = StringUtils.parseInt(wReader.get("FQTY"));
				wItem.Period = StringUtils.parseDouble(wReader.get("Period"));
				wItem.WorkHour = StringUtils.parseDouble(wReader.get("WorkHour"));
				wItem.CreatorID = StringUtils.parseInt(wReader.get("CreatorID"));
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				wItem.EditTime = StringUtils.parseCalendar(wReader.get("EditTime"));
				wItem.EditorID = StringUtils.parseInt(wReader.get("EditorID"));
				wItem.AuditTime = StringUtils.parseCalendar(wReader.get("AuditTime"));
				wItem.AuditorID = StringUtils.parseInt(wReader.get("AuditorID"));
				wItem.Status = StringUtils.parseInt(wReader.get("Status"));
				wItem.Active = StringUtils.parseInt(wReader.get("Active"));
				wItem.WorkHours = StringUtils.parseDouble(wReader.get("WorkHours"));

				// ①修程
				wItem.LineName = APSConstans.GetFMCLineName(wItem.LineID);
				// ②工位
				wItem.PartName = APSConstans.GetFPCPartName(wItem.PartID);
				// ③工序
				wItem.StepName = APSConstans.GetFPCPartPointName(wItem.StepID);

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
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
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
				APSManuCapacityStep wAPSManuCapacityStep = SelectByID(wLoginUser, wItem, wErrorCode);
				if (wAPSManuCapacityStep == null || wAPSManuCapacityStep.ID <= 0)
					continue;
				wAPSManuCapacityStep.Active = wActive;
				long wID = Update(wLoginUser, wAPSManuCapacityStep, wErrorCode);
				if (wID <= 0)
					break;
			}
		} catch (Exception e) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(e.toString());
		}
		return wResult;
	}

	private APSManuCapacityStepDAO() {
		super();
	}

	public static APSManuCapacityStepDAO getInstance() {
		if (Instance == null)
			Instance = new APSManuCapacityStepDAO();
		return Instance;
	}

	/**
	 * 根据修程、工位获取产线单元明细工序列表
	 */
	public List<Integer> SelectLineUnitStepList(BMSEmployee wLoginUser, int wLineID, int wPartID,
			OutResult<Integer> wErrorCode) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"SELECT distinct UnitID FROM {0}.fmc_lineunit "
							+ "where LineID=:wLineID and ParentUnitID=:wPartID and LevelID=3 order by UnitID asc;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wLineID", wLineID);
			wParamMap.put("wPartID", wPartID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wUnitID = StringUtils.parseInt(wReader.get("UnitID"));
				wResult.add(wUnitID);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	public List<FPCPart> SelectListByOrder(BMSEmployee wLoginUser, int wOrderID, int wPartID,
			OutResult<Integer> wErrorCode) {
		List<FPCPart> wResult = new ArrayList<FPCPart>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("SELECT StepID,Status FROM {0}.aps_manucapacitystep "
					+ "where LineID in (SELECT LineID FROM {0}.oms_order where ID=:wOrderID) "
					+ "and PartID=:wPartID;", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wOrderID", wOrderID);
			wParamMap.put("wPartID", wPartID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				FPCPart wItem = new FPCPart();

				wItem.ID = StringUtils.parseInt(wReader.get("StepID"));
				wItem.FactoryID = StringUtils.parseInt(wReader.get("Status"));

				wResult.add(wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}
}
