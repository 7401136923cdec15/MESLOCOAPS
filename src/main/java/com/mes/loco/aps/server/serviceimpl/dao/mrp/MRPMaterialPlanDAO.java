package com.mes.loco.aps.server.serviceimpl.dao.mrp;

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

import com.mes.loco.aps.server.service.mesenum.APSOutSourceType;
import com.mes.loco.aps.server.service.mesenum.APSReplaceType;
import com.mes.loco.aps.server.service.mesenum.MESDBSource;
import com.mes.loco.aps.server.service.mesenum.MESException;
import com.mes.loco.aps.server.service.mesenum.MRPMaterialPlanStatus;
import com.mes.loco.aps.server.service.mesenum.MSSMaterialType;
import com.mes.loco.aps.server.service.po.OutResult;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.mrp.MRPMaterialPlan;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSConstans;

public class MRPMaterialPlanDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(MRPMaterialPlanDAO.class);

	private static MRPMaterialPlanDAO Instance = null;

	private MRPMaterialPlanDAO() {
		super();
	}

	public static MRPMaterialPlanDAO getInstance() {
		if (Instance == null)
			Instance = new MRPMaterialPlanDAO();
		return Instance;
	}

	/**
	 * 添加或修改
	 * 
	 * @param wMRPMaterialPlan
	 * @return
	 */
	public int Update(BMSEmployee wLoginUser, MRPMaterialPlan wMRPMaterialPlan, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wMRPMaterialPlan == null)
				return 0;

			String wSQL = "";
			if (wMRPMaterialPlan.getID() <= 0) {
				wSQL = MessageFormat.format(
						"INSERT INTO {0}.mrp_materialplan(ProductID,LineID,CustomerID,OrderID,PartNo,"
								+ "PartID,StepID,MaterialID,MaterialName,MaterialNo,MaterialType,FQTY,"
								+ "DemandDate,WeekPlanSerialNo,Active,CreateTime,CreateID,ReplaceType,OutSourceType,Status,EditID,WMSPickDemandID,EditTime,WBSNo,AssessmentType) VALUES(:ProductID,:LineID,"
								+ ":CustomerID,:OrderID,:PartNo,:PartID,:StepID,:MaterialID,:MaterialName,:MaterialNo,"
								+ ":MaterialType,:FQTY,:DemandDate,:WeekPlanSerialNo,:Active,:CreateTime,:CreateID,:ReplaceType,:OutSourceType,:Status,:EditID,:WMSPickDemandID,:EditTime,:WBSNo,:AssessmentType);",
						wInstance.Result);
			} else {
				wSQL = MessageFormat.format(
						"UPDATE {0}.mrp_materialplan SET ProductID = :ProductID,LineID = :LineID,CustomerID = :CustomerID,"
								+ "OrderID = :OrderID,PartNo = :PartNo,PartID = :PartID,StepID = :StepID,MaterialID = :MaterialID,"
								+ "MaterialName = :MaterialName,MaterialNo = :MaterialNo,MaterialType = :MaterialType,FQTY = :FQTY,"
								+ "DemandDate = :DemandDate,WeekPlanSerialNo = :WeekPlanSerialNo,Active = :Active,CreateTime = :CreateTime,"
								+ "CreateID = :CreateID,ReplaceType=:ReplaceType,OutSourceType=:OutSourceType,Status=:Status,EditID=:EditID,WMSPickDemandID=:WMSPickDemandID,EditTime=:EditTime,WBSNo=:WBSNo,AssessmentType=:AssessmentType WHERE ID = :ID;",
						wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wMRPMaterialPlan.ID);
			wParamMap.put("ProductID", wMRPMaterialPlan.ProductID);
			wParamMap.put("LineID", wMRPMaterialPlan.LineID);
			wParamMap.put("CustomerID", wMRPMaterialPlan.CustomerID);
			wParamMap.put("OrderID", wMRPMaterialPlan.OrderID);
			wParamMap.put("PartNo", wMRPMaterialPlan.PartNo);
			wParamMap.put("PartID", wMRPMaterialPlan.PartID);
			wParamMap.put("StepID", wMRPMaterialPlan.StepID);
			wParamMap.put("MaterialID", wMRPMaterialPlan.MaterialID);
			wParamMap.put("MaterialName", wMRPMaterialPlan.MaterialName);
			wParamMap.put("MaterialNo", wMRPMaterialPlan.MaterialNo);
			wParamMap.put("MaterialType", wMRPMaterialPlan.MaterialType);
			wParamMap.put("FQTY", wMRPMaterialPlan.FQTY);
			wParamMap.put("DemandDate", wMRPMaterialPlan.DemandDate);
			wParamMap.put("WeekPlanSerialNo", wMRPMaterialPlan.WeekPlanSerialNo);
			wParamMap.put("Active", wMRPMaterialPlan.Active);
			wParamMap.put("CreateTime", wMRPMaterialPlan.CreateTime);
			wParamMap.put("CreateID", wMRPMaterialPlan.CreateID);
			wParamMap.put("ReplaceType", wMRPMaterialPlan.ReplaceType);
			wParamMap.put("OutSourceType", wMRPMaterialPlan.OutSourceType);
			wParamMap.put("Status", wMRPMaterialPlan.Status);
			wParamMap.put("EditID", wMRPMaterialPlan.EditID);
			wParamMap.put("WMSPickDemandID", wMRPMaterialPlan.WMSPickDemandID);
			wParamMap.put("EditTime", wMRPMaterialPlan.EditTime);
			wParamMap.put("WBSNo", wMRPMaterialPlan.WBSNo);
			wParamMap.put("AssessmentType", wMRPMaterialPlan.AssessmentType);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wMRPMaterialPlan.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wMRPMaterialPlan.setID(wResult);
			} else {
				wResult = wMRPMaterialPlan.getID();
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
	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<MRPMaterialPlan> wList,
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
			for (MRPMaterialPlan wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = MessageFormat.format("delete from {1}.mrp_materialplan WHERE ID IN({0}) ;",
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
	public MRPMaterialPlan SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		MRPMaterialPlan wResult = new MRPMaterialPlan();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<MRPMaterialPlan> wList = SelectList(wLoginUser, wID, -1, -1, -1, -1, -1, -1, -1, "", "", -1, "", -1,
					-1, -1, null, null, null, null, "", wErrorCode);
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
	public List<MRPMaterialPlan> SelectList(BMSEmployee wLoginUser, int wID, int wProductID, int wLineID,
			int wCustomerID, int wOrderID, int wPartID, int wStepID, int wMaterialID, String wMaterialName,
			String wMaterialNo, int wMaterialType, String wWeekPlanSerialNo, int wActive, int wReplaceType,
			int wOutSourceType, Calendar wCStartTime, Calendar wCEndTime, Calendar wDStartTime, Calendar wDEndTime,
			String wMaterial, OutResult<Integer> wErrorCode) {
		List<MRPMaterialPlan> wResultList = new ArrayList<MRPMaterialPlan>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1, 0, 0, 0);
			if (wCStartTime == null) {
				wCStartTime = wBaseTime;
			}
			if (wCEndTime == null) {
				wCEndTime = wBaseTime;
			}
			if (wCStartTime.compareTo(wCEndTime) > 0) {
				return wResultList;
			}

			if (wDStartTime == null) {
				wDStartTime = wBaseTime;
			}
			if (wDEndTime == null) {
				wDEndTime = wBaseTime;
			}
			if (wDStartTime.compareTo(wDEndTime) > 0) {
				return wResultList;
			}

			String wSQL = MessageFormat.format("SELECT t.* FROM {0}.mrp_materialplan t WHERE 1=1 "
					+ "and ( :wID <= 0 or :wID = t.ID ) " + "and ( :wProductID <= 0 or :wProductID = t.ProductID ) "
					+ "and ( :wLineID <= 0 or :wLineID = t.LineID ) "
					+ "and ( :wCustomerID <= 0 or :wCustomerID = t.CustomerID ) "
					+ "and ( :wOrderID <= 0 or :wOrderID = t.OrderID ) "
					+ "and ( :wPartID <= 0 or :wPartID = t.PartID ) " + "and ( :wStepID <= 0 or :wStepID = t.StepID ) "
					+ "and ( :wReplaceType < 0 or :wReplaceType = t.ReplaceType ) "
					+ "and ( :wOutSourceType < 0 or :wOutSourceType = t.OutSourceType ) "
					+ "and ( :wMaterialID <= 0 or :wMaterialID = t.MaterialID ) "
					+ "and ( :wMaterialName is null or :wMaterialName = '''' or :wMaterialName = t.MaterialName ) "
					+ "and ( :wMaterialNo is null or :wMaterialNo = '''' or :wMaterialNo = t.MaterialNo ) "
					+ "and ( :wMaterialType <= 0 or :wMaterialType = t.MaterialType ) "
					+ "and ( :wDStartTime <=str_to_date(''2010-01-01'', ''%Y-%m-%d'')  or :wDStartTime <=  t.DemandDate ) "
					+ "and ( :wDEndTime <=str_to_date(''2010-01-01'', ''%Y-%m-%d'')  or :wDEndTime >=  t.DemandDate ) "
					+ "and ( :wWeekPlanSerialNo is null or :wWeekPlanSerialNo = '''' or :wWeekPlanSerialNo = t.WeekPlanSerialNo ) "
					+ "and ( :wActive <= 0 or :wActive = t.Active ) "
					+ "and ( :wMaterial is null or :wMaterial = '''' or (t.MaterialName like ''%{1}%'' or t.MaterialNo like ''%{1}%'') ) "
					+ "and ( :wCStartTime <=str_to_date(''2010-01-01'', ''%Y-%m-%d'')  or :wCStartTime <=  t.CreateTime ) "
					+ "and ( :wCEndTime <=str_to_date(''2010-01-01'', ''%Y-%m-%d'')  or :wCEndTime >=  t.CreateTime );",
					wInstance.Result, wMaterial);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wProductID", wProductID);
			wParamMap.put("wLineID", wLineID);
			wParamMap.put("wCustomerID", wCustomerID);
			wParamMap.put("wOrderID", wOrderID);
			wParamMap.put("wPartID", wPartID);
			wParamMap.put("wStepID", wStepID);
			wParamMap.put("wMaterialID", wMaterialID);
			wParamMap.put("wMaterialName", wMaterialName);
			wParamMap.put("wMaterialNo", wMaterialNo);
			wParamMap.put("wMaterialType", wMaterialType);
			wParamMap.put("wWeekPlanSerialNo", wWeekPlanSerialNo);
			wParamMap.put("wActive", wActive);
			wParamMap.put("wCStartTime", wCStartTime);
			wParamMap.put("wCEndTime", wCEndTime);
			wParamMap.put("wDStartTime", wDStartTime);
			wParamMap.put("wDEndTime", wDEndTime);
			wParamMap.put("wReplaceType", wReplaceType);
			wParamMap.put("wOutSourceType", wOutSourceType);
			wParamMap.put("wMaterial", wMaterial);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SimpleDateFormat wSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			SimpleDateFormat wDateSDF = new SimpleDateFormat("yyyy-MM-dd");

			for (Map<String, Object> wReader : wQueryResult) {
				MRPMaterialPlan wItem = new MRPMaterialPlan();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.ProductID = StringUtils.parseInt(wReader.get("ProductID"));
				wItem.LineID = StringUtils.parseInt(wReader.get("LineID"));
				wItem.CustomerID = StringUtils.parseInt(wReader.get("CustomerID"));
				wItem.OrderID = StringUtils.parseInt(wReader.get("OrderID"));
				wItem.PartNo = StringUtils.parseString(wReader.get("PartNo"));
				wItem.PartID = StringUtils.parseInt(wReader.get("PartID"));
				wItem.StepID = StringUtils.parseInt(wReader.get("StepID"));
				wItem.MaterialID = StringUtils.parseInt(wReader.get("MaterialID"));
				wItem.MaterialName = StringUtils.parseString(wReader.get("MaterialName"));
				wItem.MaterialNo = StringUtils.parseString(wReader.get("MaterialNo"));
				wItem.MaterialType = StringUtils.parseInt(wReader.get("MaterialType"));
				wItem.FQTY = StringUtils.parseDouble(wReader.get("FQTY"));
				wItem.DemandDate = StringUtils.parseCalendar(wReader.get("DemandDate"));
				wItem.WeekPlanSerialNo = StringUtils.parseString(wReader.get("WeekPlanSerialNo"));
				wItem.Active = StringUtils.parseInt(wReader.get("Active"));
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				wItem.CreateID = StringUtils.parseInt(wReader.get("CreateID"));
				wItem.ReplaceType = StringUtils.parseInt(wReader.get("ReplaceType"));
				wItem.OutSourceType = StringUtils.parseInt(wReader.get("OutSourceType"));
				wItem.PartNo = StringUtils.parseString(wReader.get("PartNo"));
				wItem.EditTime = StringUtils.parseCalendar(wReader.get("EditTime"));
				wItem.EditTimeText = wSDF.format(wItem.EditTime.getTime());

				wItem.Status = StringUtils.parseInt(wReader.get("Status"));
				wItem.StatusText = MRPMaterialPlanStatus.getEnumType(wItem.Status).getLable();
				wItem.EditID = StringUtils.parseInt(wReader.get("EditID"));
				wItem.Editor = APSConstans.GetBMSEmployeeName(wItem.EditID);
				wItem.WMSPickDemandID = StringUtils.parseInt(wReader.get("WMSPickDemandID"));
				wItem.WBSNo = StringUtils.parseString(wReader.get("WBSNo"));
				wItem.AssessmentType = StringUtils.parseString(wReader.get("AssessmentType"));

				// 辅助属性
				wItem.ReplaceTypeText = APSReplaceType.getEnumType(wItem.ReplaceType).getLable();
				wItem.OutSourceTypeText = APSOutSourceType.getEnumType(wItem.OutSourceType).getLable();
				wItem.ProductNo = APSConstans.GetFPCProductName(wItem.ProductID);
				wItem.LineName = APSConstans.GetFMCLineName(wItem.LineID);
				wItem.CustomerName = APSConstans.GetCRMCustomerName(wItem.CustomerID);
				wItem.PartName = APSConstans.GetFPCPartName(wItem.PartID);
				wItem.StepName = APSConstans.GetFPCPartPointName(wItem.StepID);
				wItem.MaterialTypeText = MSSMaterialType.getEnumType(wItem.MaterialType).getLable();
				wItem.DemandDateText = wDateSDF.format(wItem.DemandDate.getTime());
				wItem.ActiveText = wItem.Active == 1 ? "激活" : "关闭";
				wItem.CreateTimeText = wSDF.format(wItem.CreateTime.getTime());
				wItem.Creator = APSConstans.GetBMSEmployeeName(wItem.CreateID);

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
				MRPMaterialPlan wMRPMaterialPlan = SelectByID(wLoginUser, wItem, wErrorCode);
				if (wMRPMaterialPlan == null || wMRPMaterialPlan.ID <= 0)
					continue;
				wMRPMaterialPlan.Active = wActive;
				long wID = Update(wLoginUser, wMRPMaterialPlan, wErrorCode);
				if (wID <= 0)
					break;
			}
		} catch (Exception e) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 禁用物料需求计划(必换件、委外必修件)
	 * 
	 * @param wLoginUser 登录信息
	 * @param orderID    订单ID
	 * @param partID     工位ID
	 * @param wErrorCode 错误信息
	 */
	public void DisableMainPlan(BMSEmployee wLoginUser, int orderID, int partID, OutResult<Integer> wErrorCode) {
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return;
			}

			String wSQL = StringUtils.Format("update {0}.mrp_materialplan set Active=2 "
					+ "where OrderID=:OrderID and PartID=:PartID and (ReplaceType=1 or OutSourceType=1) and ID>0;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("OrderID", orderID);
			wParamMap.put("PartID", partID);

			wSQL = this.DMLChange(wSQL);

			nameJdbcTemplate.update(wSQL, wParamMap);
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return;
	}

	public List<MRPMaterialPlan> SelectAutoList(BMSEmployee wLoginUser, Calendar wToday,
			OutResult<Integer> wErrorCode) {
		List<MRPMaterialPlan> wResult = new ArrayList<MRPMaterialPlan>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("SELECT distinct OrderID,PartID FROM {0}.mrp_materialplan "
					+ "where Active=1 and Status=1 and :wToday>=DemandDate;", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wToday", wToday);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				MRPMaterialPlan wItem = new MRPMaterialPlan();

				wItem.OrderID = StringUtils.parseInt(wReader.get("OrderID"));
				wItem.PartID = StringUtils.parseInt(wReader.get("PartID"));

				wResult.add(wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}
}
