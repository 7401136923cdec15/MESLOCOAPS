package com.mes.loco.aps.server.serviceimpl.dao.oms;

import com.mes.loco.aps.server.service.mesenum.MESDBSource;
import com.mes.loco.aps.server.service.mesenum.MESException;
import com.mes.loco.aps.server.service.mesenum.OMSOrderType;
import com.mes.loco.aps.server.service.po.OutResult;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.fmc.FMCWorkspace;
import com.mes.loco.aps.server.service.po.fpc.FPCRoutePartPoint;
import com.mes.loco.aps.server.service.po.oms.OMSOrder;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.CoreServiceImpl;
import com.mes.loco.aps.server.serviceimpl.FMCServiceImpl;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;
import com.mes.loco.aps.server.serviceimpl.dao.oms.OMSOrderDAO;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSConstans;
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

public class OMSOrderDAO extends BaseDAO {
	private static Logger logger = LoggerFactory.getLogger(OMSOrderDAO.class);

	private static OMSOrderDAO Instance = null;

	public static OMSOrderDAO getInstance() {
		if (Instance == null)
			Instance = new OMSOrderDAO();
		return Instance;
	}

	public long Update(BMSEmployee wLoginUser, OMSOrder wOMSOrder, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			if (wOMSOrder == null) {
				return 0L;
			}
			String wSQL = "";
			if (wOMSOrder.getID() <= 0) {
				wSQL = StringUtils.Format("INSERT INTO {0}.oms_order(CommandID,ERPID,OrderNo,LineID,"
						+ "ProductID,BureauSectionID,PartNo,BOMNo,Priority,Status,PlanReceiveDate,"
						+ "RealReceiveDate,PlanFinishDate,RealStartDate,RealFinishDate,RealSendDate,"
						+ "Remark,CreateID,CreateTime,EditID,EditTime,AuditorID,AuditTime,Active,"
						+ "RouteID,TelegraphTime,TelegraphRealTime,TimeAway,CompletionTelegramTime,DriverOnTime,ToSegmentTime,"
						+ "ActualRepairStopTimes,TelegraphRepairStopTimes,InPlantStopTimes,"
						+ "OnTheWayStopTimes,PosterioriStopTimes,OrderType,ParentID) "
						+ "VALUES(:CommandID,:ERPID,:OrderNo,:LineID,:ProductID,:BureauSectionID,:PartNo,"
						+ ":BOMNo,:Priority,:Status,:PlanReceiveDate,:RealReceiveDate,:PlanFinishDate,"
						+ ":RealStartDate,:RealFinishDate,:RealSendDate,:Remark,:CreateID,:CreateTime,"
						+ ":EditID,:EditTime,:AuditorID,:AuditTime,:Active,:RouteID,"
						+ ":TelegraphTime,:TelegraphRealTime,:TimeAway,:CompletionTelegramTime,"
						+ ":DriverOnTime,:ToSegmentTime,:ActualRepairStopTimes,:TelegraphRepairStopTimes,"
						+ ":InPlantStopTimes,:OnTheWayStopTimes,:PosterioriStopTimes,:OrderType,:ParentID);",
						new Object[] { wInstance.Result });
				wOMSOrder.OrderNo = SelectSubOrderNo(wLoginUser, wOMSOrder.CommandID, wErrorCode);
			} else {
				wSQL = StringUtils.Format(
						"UPDATE {0}.oms_order SET CommandID=:CommandID,ERPID = :ERPID,OrderNo = :OrderNo,"
								+ "LineID = :LineID,ProductID = :ProductID,BureauSectionID = :BureauSectionID,"
								+ "PartNo = :PartNo,BOMNo = :BOMNo,Priority = :Priority,Status = :Status,"
								+ "PlanReceiveDate = :PlanReceiveDate,RealReceiveDate = :RealReceiveDate,"
								+ "PlanFinishDate = :PlanFinishDate,RealStartDate = :RealStartDate,"
								+ "RealFinishDate = :RealFinishDate,RealSendDate = :RealSendDate,"
								+ "Remark = :Remark,CreateID = :CreateID,CreateTime = :CreateTime,"
								+ "EditID = :EditID,EditTime = :EditTime,AuditorID = :AuditorID,"
								+ "AuditTime = :AuditTime,Active = :Active,RouteID=:RouteID,"
								+ "TelegraphTime=:TelegraphTime,TelegraphRealTime=:TelegraphRealTime,"
								+ "TimeAway=:TimeAway,CompletionTelegramTime=:CompletionTelegramTime,"
								+ "DriverOnTime=:DriverOnTime,ToSegmentTime=:ToSegmentTime,"
								+ "ActualRepairStopTimes=:ActualRepairStopTimes,"
								+ "TelegraphRepairStopTimes=:TelegraphRepairStopTimes,"
								+ "InPlantStopTimes=:InPlantStopTimes,OnTheWayStopTimes=:OnTheWayStopTimes,"
								+ "PosterioriStopTimes=:PosterioriStopTimes,"
								+ "OrderType=:OrderType,ParentID=:ParentID " + "WHERE ID = :ID;",
						new Object[] { wInstance.Result });
			}
			wSQL = DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("ID", Integer.valueOf(wOMSOrder.ID));
			wParamMap.put("CommandID", Integer.valueOf(wOMSOrder.CommandID));
			wParamMap.put("ERPID", Integer.valueOf(wOMSOrder.ERPID));
			wParamMap.put("OrderNo", wOMSOrder.OrderNo);
			wParamMap.put("LineID", Integer.valueOf(wOMSOrder.LineID));
			wParamMap.put("ProductID", Integer.valueOf(wOMSOrder.ProductID));
			wParamMap.put("BureauSectionID", Integer.valueOf(wOMSOrder.BureauSectionID));
			wParamMap.put("PartNo", wOMSOrder.PartNo);
			wParamMap.put("BOMNo", wOMSOrder.BOMNo);
			wParamMap.put("Priority", Integer.valueOf(wOMSOrder.Priority));
			wParamMap.put("Status", Integer.valueOf(wOMSOrder.Status));
			wParamMap.put("PlanReceiveDate", wOMSOrder.PlanReceiveDate);
			wParamMap.put("RealReceiveDate", wOMSOrder.RealReceiveDate);
			wParamMap.put("PlanFinishDate", wOMSOrder.PlanFinishDate);
			wParamMap.put("RealStartDate", wOMSOrder.RealStartDate);
			wParamMap.put("RealFinishDate", wOMSOrder.RealFinishDate);
			wParamMap.put("RealSendDate", wOMSOrder.RealSendDate);
			wParamMap.put("Remark", wOMSOrder.Remark);
			wParamMap.put("CreateID", Integer.valueOf(wOMSOrder.CreateID));
			wParamMap.put("CreateTime", wOMSOrder.CreateTime);
			wParamMap.put("EditID", Integer.valueOf(wOMSOrder.EditID));
			wParamMap.put("EditTime", wOMSOrder.EditTime);
			wParamMap.put("AuditorID", Integer.valueOf(wOMSOrder.AuditorID));
			wParamMap.put("AuditTime", wOMSOrder.AuditTime);
			wParamMap.put("Active", Integer.valueOf(wOMSOrder.Active));
			wParamMap.put("RouteID", Integer.valueOf(wOMSOrder.RouteID));
			wParamMap.put("TelegraphTime", wOMSOrder.TelegraphTime);
			wParamMap.put("TelegraphRealTime", wOMSOrder.TelegraphRealTime);

			wParamMap.put("TimeAway", wOMSOrder.TimeAway);
			wParamMap.put("CompletionTelegramTime", wOMSOrder.CompletionTelegramTime);
			wParamMap.put("DriverOnTime", wOMSOrder.DriverOnTime);
			wParamMap.put("ToSegmentTime", wOMSOrder.ToSegmentTime);
			wParamMap.put("ActualRepairStopTimes", wOMSOrder.ActualRepairStopTimes);
			wParamMap.put("TelegraphRepairStopTimes", wOMSOrder.TelegraphRepairStopTimes);
			wParamMap.put("InPlantStopTimes", wOMSOrder.InPlantStopTimes);
			wParamMap.put("OnTheWayStopTimes", wOMSOrder.OnTheWayStopTimes);
			wParamMap.put("PosterioriStopTimes", wOMSOrder.PosterioriStopTimes);

			wParamMap.put("OrderType", wOMSOrder.OrderType);
			wParamMap.put("ParentID", wOMSOrder.ParentID);

			GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
			MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource(wParamMap);

			this.nameJdbcTemplate.update(wSQL, (SqlParameterSource) mapSqlParameterSource,
					(KeyHolder) generatedKeyHolder);

			if (wOMSOrder.getID() <= 0) {
				wResult = generatedKeyHolder.getKey().intValue();
				wOMSOrder.setID(wResult);
			} else {
				wResult = wOMSOrder.getID();
			}
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResult;
	}

	public void DeleteList(BMSEmployee wLoginUser, List<OMSOrder> wList, OutResult<Integer> wErrorCode) {
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return;
			}

			if (wList == null || wList.size() <= 0) {
				return;
			}
			List<String> wIDList = new ArrayList<>();
			for (OMSOrder wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = StringUtils.Format("delete {1}.from oms_order WHERE ID IN({0}) ;", String.join(",", wIDList),
					wErrorCode);
			ExecuteSqlTransaction(wSql);
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
	}

	public OMSOrder SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		OMSOrder wResult = new OMSOrder();
		try {
			if (wID <= 0)
				return wResult;
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 1, 1);

			List<OMSOrder> wList = SelectList(wLoginUser, wID, -1, "", -1, -1, -1, "", "", -1, null, wBaseTime,
					wBaseTime, wBaseTime, wBaseTime, wErrorCode);
			if (wList == null || wList.size() != 1)
				return wResult;
			wResult = wList.get(0);
		} catch (Exception e) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(e.toString());
		}
		return wResult;
	}

	public List<OMSOrder> SelectList(BMSEmployee wLoginUser, int wID, int wCommandID, String wOrderNo, int wLineID,
			int wProductID, int wBureauSectionID, String wPartNo, String wBOMNo, int wActive,
			List<Integer> wStateIDList, Calendar wPreStartTime, Calendar wPreEndTime, Calendar wRelStartTime,
			Calendar wRelEndTime, OutResult<Integer> wErrorCode) {
		List<OMSOrder> wResultList = new ArrayList<>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResultList;
			}

			if (wStateIDList == null) {
				wStateIDList = new ArrayList<>();
			}
			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 1, 1);
			if (wPreStartTime == null || wPreStartTime.compareTo(wBaseTime) < 0)
				wPreStartTime = wBaseTime;
			if (wPreEndTime == null || wPreEndTime.compareTo(wBaseTime) < 0)
				wPreEndTime = wBaseTime;
			if (wPreStartTime.compareTo(wPreEndTime) > 0) {
				return wResultList;
			}
			if (wRelStartTime == null || wRelStartTime.compareTo(wBaseTime) < 0)
				wRelStartTime = wBaseTime;
			if (wRelEndTime == null || wRelEndTime.compareTo(wBaseTime) < 0)
				wRelEndTime = wBaseTime;
			if (wRelStartTime.compareTo(wRelEndTime) > 0) {
				return wResultList;
			}
			String wSQL = StringUtils.Format("SELECT t1.*,t2.WBSNo,t2.CustomerID,t2.ContactCode,t2.No,t2.LinkManID,"
					+ "t2.FactoryID,t2.BusinessUnitID,t2.FQTYPlan,t2.FQTYActual "
					+ "FROM {0}.oms_order t1 left join {0}.oms_command t2 on t1.CommandID=t2.ID "
					+ "WHERE  1=1  and ( :wID <= 0 or :wID = t1.ID ) "
					+ "and ( :wCommandID <= 0 or :wCommandID = t1.CommandID ) "
					+ "and ( :wOrderNo is null or :wOrderNo = '''' or :wOrderNo = t1.OrderNo ) "
					+ "and ( :wLineID <= 0 or :wLineID = t1.LineID ) "
					+ "and ( :wProductID <= 0 or :wProductID = t1.ProductID ) "
					+ "and ( :wBureauSectionID <= 0 or :wBureauSectionID = t1.BureauSectionID ) "
					+ "and ( :wPartNo is null or :wPartNo = '''' or :wPartNo = t1.PartNo ) "
					+ "and ( :wBOMNo is null or :wBOMNo = '''' or :wBOMNo = t1.BOMNo ) "
					+ "and ( :wStatus is null or :wStatus = '''' or t1.Status in ({1})) "
					+ "and ( :wPreStartTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wPreStartTime <= t1.PlanReceiveDate) "
					+ "and ( :wPreEndTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wPreEndTime >= t1.PlanReceiveDate) "
					+ "and ( :wRelStartTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wRelStartTime <= t1.RealFinishDate) "
					+ "and ( :wRelEndTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wRelEndTime >= t1.RealReceiveDate) "
					+ "and ( :wActive <= 0 or :wActive = t1.Active );",
					new Object[] { wInstance.Result,
							(wStateIDList.size() > 0) ? StringUtils.Join(",", wStateIDList) : "0" });
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wID", Integer.valueOf(wID));
			wParamMap.put("wCommandID", Integer.valueOf(wCommandID));
			wParamMap.put("wOrderNo", wOrderNo);
			wParamMap.put("wLineID", Integer.valueOf(wLineID));
			wParamMap.put("wProductID", Integer.valueOf(wProductID));
			wParamMap.put("wBureauSectionID", Integer.valueOf(wBureauSectionID));
			wParamMap.put("wPartNo", wPartNo);
			wParamMap.put("wBOMNo", wBOMNo);
			wParamMap.put("wActive", Integer.valueOf(wActive));
			wParamMap.put("wPreStartTime", wPreStartTime);
			wParamMap.put("wPreEndTime", wPreEndTime);
			wParamMap.put("wRelStartTime", wRelStartTime);
			wParamMap.put("wRelEndTime", wRelEndTime);
			wParamMap.put("wStatus", StringUtils.Join(",", wStateIDList));

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetVaue(wLoginUser, wResultList, wQueryResult, wErrorCode);
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResultList;
	}

	public List<OMSOrder> SelectListByIDList(BMSEmployee wLoginUser, List<Integer> wIDList,
			OutResult<Integer> wErrorCode) {
		List<OMSOrder> wResultList = new ArrayList<>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResultList;
			}

			if (wIDList == null || wIDList.size() <= 0) {
				return wResultList;
			}

			String wSQL = StringUtils.Format(
					"SELECT t1.*,t2.WBSNo,t2.CustomerID,t2.ContactCode,t2.No,t2.LinkManID,t2.FactoryID,"
							+ "t2.BusinessUnitID,t2.FQTYPlan,t2.FQTYActual FROM {0}.oms_order t1 left join {0}.oms_command t2 "
							+ "on t1.CommandID=t2.ID WHERE 1=1  and ( :wIDs is null or :wIDs = '''' or t1.ID in ({1}));",
					new Object[] { wInstance.Result, (wIDList.size() > 0) ? StringUtils.Join(",", wIDList) : "0" });
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wIDs", StringUtils.Join(",", wIDList));

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetVaue(wLoginUser, wResultList, wQueryResult, wErrorCode);
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResultList;
	}

	public List<Integer> SelectIDList(BMSEmployee wLoginUser, List<String> wPartNoList, OutResult<Integer> wErrorCode) {
		List<Integer> wResultList = new ArrayList<>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResultList;
			}

			if (wPartNoList == null || wPartNoList.size() <= 0) {
				return wResultList;
			}

			wPartNoList.removeIf(p -> StringUtils.isEmpty(p));

			if (wPartNoList.size() <= 0) {
				return wResultList;
			}

			String wSQL = StringUtils.Format(
					"SELECT ID FROM {0}.oms_order  WHERE 1=1 and Active=1  and (:wPartNos = '''' or PartNo in (''{1}''));",
					new Object[] { wInstance.Result,
							(wPartNoList.size() > 0) ? StringUtils.Join("','", wPartNoList) : "0" });
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wPartNos", StringUtils.Join(",", wPartNoList));

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				wResultList.add(StringUtils.parseInt(wReader.get("ID")));
			}
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResultList;
	}

	public List<OMSOrder> SelectFinishListByTime(BMSEmployee wLoginUser, Calendar wStartTime, Calendar wEndTime,
			OutResult<Integer> wErrorCode) {
		List<OMSOrder> wResultList = new ArrayList<>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
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

			String wSQL = StringUtils.Format("SELECT t1.*,t2.WBSNo,t2.CustomerID,t2.ContactCode,t2.No,t2.LinkManID,"
					+ "t2.FactoryID,t2.BusinessUnitID,t2.FQTYPlan,t2.FQTYActual "
					+ "FROM {0}.oms_order t1 left join {0}.oms_command t2 on t1.CommandID=t2.ID "
					+ "WHERE 1=1  and :wStartTime <= t1.RealFinishDate and t1.RealFinishDate <= :wEndTime "
					+ " and t1.Status in(5,6,7,8);", wInstance.Result);
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetVaue(wLoginUser, wResultList, wQueryResult, wErrorCode);
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResultList;
	}

	public List<OMSOrder> SelectList_RF(BMSEmployee wLoginUser, int wCustomerID, int wLineID, int wProductID,
			String wPartNo, int wActive, Calendar wStartTime, Calendar wEndTime, OutResult<Integer> wErrorCode) {
		List<OMSOrder> wResultList = new ArrayList<>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResultList;
			}

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 1, 1);
			if (wStartTime == null || wStartTime.compareTo(wBaseTime) < 0)
				wStartTime = wBaseTime;
			if (wEndTime == null || wEndTime.compareTo(wBaseTime) < 0) {
				wEndTime = wBaseTime;
			}
			if (wEndTime.compareTo(wStartTime) < 0) {
				return wResultList;
			}
			String wSQL = StringUtils
					.Format("SELECT t1.*,t2.WBSNo,t2.CustomerID,t2.ContactCode,t2.No,t2.LinkManID,t2.FactoryID,"
							+ "t2.BusinessUnitID,t2.FQTYPlan,t2.FQTYActual "
							+ "FROM {0}.oms_order t1 left join {0}.oms_command t2 on t1.CommandID=t2.ID  "
							+ "WHERE 1=1 and ( :wCustomerID <= 0 or :wCustomerID = t1.BureauSectionID ) "
							+ "and ( :wLineID <= 0 or :wLineID = t1.LineID ) "
							+ "and ( :wProductID <= 0 or :wProductID = t1.ProductID ) "
							+ "and ( :wPartNo is null or :wPartNo = '''' or :wPartNo = t1.PartNo ) "
							+ "and ( :wStartTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') "
							+ "or :wEndTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') "
							+ "or (:wStartTime <= t1.PlanReceiveDate and t1.PlanReceiveDate<=:wEndTime) "
							+ "or (:wStartTime <= t1.RealReceiveDate and t1.RealReceiveDate<=:wEndTime) "
							+ "or (:wStartTime <= t1.PlanFinishDate and t1.PlanFinishDate<=:wEndTime) "
							+ "or (:wStartTime <= t1.RealStartDate and t1.RealStartDate<=:wEndTime) "
							+ "or (:wStartTime <= t1.RealFinishDate and t1.RealFinishDate<=:wEndTime) "
							+ "or (:wStartTime <= t1.RealSendDate and t1.RealSendDate<=:wEndTime) ) "
							+ "and ( :wActive <= 0 or :wActive = t1.Active );", wInstance.Result);
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wCustomerID", Integer.valueOf(wCustomerID));
			wParamMap.put("wLineID", Integer.valueOf(wLineID));
			wParamMap.put("wProductID", Integer.valueOf(wProductID));
			wParamMap.put("wPartNo", wPartNo);
			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);
			wParamMap.put("wActive", Integer.valueOf(wActive));

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetVaue(wLoginUser, wResultList, wQueryResult, wErrorCode);
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResultList;
	}

	public List<OMSOrder> ConditionAll(BMSEmployee wLoginUser, int wProductID, int wLine, int wCustomerID,
			String wWBSNo, Calendar wStartTime, Calendar wEndTime, int wStatus, OutResult<Integer> wErrorCode) {
		List<OMSOrder> wResult = new ArrayList<>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 1, 1);
			if (wStartTime == null || wStartTime.compareTo(wBaseTime) < 0)
				wStartTime = wBaseTime;
			if (wEndTime == null || wEndTime.compareTo(wBaseTime) < 0) {
				wEndTime = wBaseTime;
			}
			if (wEndTime.compareTo(wStartTime) < 0) {
				return wResult;
			}
			if (StringUtils.isEmpty(wWBSNo)) {
				wWBSNo = "";
			}

			String wSQL = StringUtils.Format(
					"SELECT t1.*,t2.WBSNo,t2.CustomerID,t2.ContactCode,t2.No,t2.LinkManID,t2.FactoryID,"
							+ "t2.BusinessUnitID,t2.FQTYPlan,t2.FQTYActual FROM {0}.oms_order t1 , {0}.oms_command t2 "
							+ "where t1.CommandID=t2.ID and ( :wCustomerID <= 0 or :wCustomerID = t2.CustomerID ) "
							+ "and ( :wLineID <= 0 or :wLineID = t1.LineID ) "
							+ "and ( :wStatus <= 0 or :wStatus = t1.Status ) "
							+ "and ( :wProductID <= 0 or :wProductID = t1.ProductID ) "
							+ "and ( :wWBSNo is null or :wWBSNo = '''' or t2.WBSNo like ''%{1}%'') "
							+ "and ( :wStartTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') "
							+ "or :wEndTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') "
							+ "or (:wStartTime <= t1.PlanReceiveDate and t1.PlanReceiveDate<=:wEndTime) "
							+ "or (:wStartTime <= t1.RealReceiveDate and t1.RealReceiveDate<=:wEndTime) "
							+ "or (:wStartTime <= t1.PlanFinishDate and t1.PlanFinishDate<=:wEndTime) "
							+ "or (:wStartTime <= t1.RealStartDate and t1.RealStartDate<=:wEndTime) "
							+ "or (:wStartTime <= t1.RealFinishDate and t1.RealFinishDate<=:wEndTime) "
							+ "or (:wStartTime <= t1.CreateTime and t1.CreateTime<=:wEndTime) "
							+ "or (:wStartTime <= t1.EditTime and t1.EditTime<=:wEndTime) "
							+ "or (:wStartTime <= t1.RealSendDate and t1.RealSendDate<=:wEndTime) );",
					wInstance.Result, wWBSNo);
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wProductID", Integer.valueOf(wProductID));
			wParamMap.put("wLineID", Integer.valueOf(wLine));
			wParamMap.put("wCustomerID", Integer.valueOf(wCustomerID));
			wParamMap.put("wWBSNo", wWBSNo);
			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);
			wParamMap.put("wStatus", wStatus);

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetVaue(wLoginUser, wResult, wQueryResult, wErrorCode);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	public String SelectSubOrderNo(BMSEmployee wLoginUser, int wCommandID, OutResult<Integer> wErrorCode) {
		String wResult = "";
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

//			String wSQL = StringUtils
//					.Format("SELECT (select WBSNo from {0}.oms_command where ID={1}) as WBSNo, COUNT(*)+1 as MaxID"
//							+ " FROM  {0}.oms_command t2  left join  {0}.oms_order t1  on t1.CommandID = t2.ID"
//							+ " WHERE t1.CommandID={1};", new Object[] { wInstance.Result, wCommandID });

			String wSQL = StringUtils
					.Format("SELECT (select WBSNo from {0}.oms_command where ID={1}) as WBSNo, COUNT(*)+1 as MaxID"
							+ " FROM {0}.oms_order;", new Object[] { wInstance.Result, wCommandID });

			Map<String, Object> wParamMap = new HashMap<>();

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			int wMaxID = 0;
			String wWBSNo = "";
			for (Map<String, Object> wMap : wQueryResult) {
				if (wMap.containsKey("MaxID")) {
					wMaxID = StringUtils.parseInt(wMap.get("MaxID")).intValue();
				}
				if (wMap.containsKey("MaxID")) {
					wWBSNo = StringUtils.parseString(wMap.get("WBSNo"));
				}
			}

			if (wMaxID > 0 && StringUtils.isNotEmpty(wWBSNo)) {
				wResult = StringUtils.Format("{0}.{1}", wWBSNo, String.format("%03d", wMaxID));
			}
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据季度开始时间和结束时间和修程获取完成数
	 * 
	 * @param wLoginUser
	 * @param wQStart    季度开始时刻
	 * @param wQEnd      季度结束时刻
	 * @param wLineID    修程
	 * @param wErrorCode 错误码
	 * @return 订单完成数
	 */
	public int SelectCountByQuarter(BMSEmployee wLoginUser, Calendar wQStart, Calendar wQEnd, int wLineID,
			OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("select count(*) as Number From {0}.oms_order "
					+ "where :wStartTime <= RealFinishDate and RealFinishDate <= :wEndTime "
					+ "and LineID = :wLineID and Status in(5,6,7,8);", wInstance.Result);
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wStartTime", wQStart);
			wParamMap.put("wEndTime", wQEnd);
			wParamMap.put("wLineID", wLineID);

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				Integer wNumber = StringUtils.parseInt(wReader.get("Number"));
				if (wNumber > 0) {
					return wNumber;
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	public String CreateNo(int wNumber) {
		String wResult = "";
		try {
			wResult = StringUtils.Format("PO-{0}",
					new Object[] { String.format("%07d", new Object[] { Integer.valueOf(wNumber) }) });
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	private void SetVaue(BMSEmployee wLoginUser, List<OMSOrder> wResultList, List<Map<String, Object>> wQueryResult,
			OutResult<Integer> wErrorCode) {
		try {
			for (Map<String, Object> wReader : wQueryResult) {
				OMSOrder wItem = new OMSOrder();

				wItem.ID = StringUtils.parseInt(wReader.get("ID")).intValue();
				wItem.CommandID = StringUtils.parseInt(wReader.get("CommandID")).intValue();
				wItem.ERPID = StringUtils.parseInt(wReader.get("ERPID")).intValue();
				wItem.OrderNo = StringUtils.parseString(wReader.get("OrderNo"));
				wItem.LineID = StringUtils.parseInt(wReader.get("LineID")).intValue();
				wItem.ProductID = StringUtils.parseInt(wReader.get("ProductID")).intValue();
				wItem.BureauSectionID = StringUtils.parseInt(wReader.get("BureauSectionID")).intValue();
				wItem.PartNo = StringUtils.parseString(wReader.get("PartNo"));
				wItem.BOMNo = StringUtils.parseString(wReader.get("BOMNo"));
				wItem.Priority = StringUtils.parseInt(wReader.get("Priority")).intValue();
				wItem.Status = StringUtils.parseInt(wReader.get("Status")).intValue();
				wItem.PlanReceiveDate = StringUtils.parseCalendar(wReader.get("PlanReceiveDate"));
				wItem.RealReceiveDate = StringUtils.parseCalendar(wReader.get("RealReceiveDate"));
				wItem.PlanFinishDate = StringUtils.parseCalendar(wReader.get("PlanFinishDate"));
				wItem.RealStartDate = StringUtils.parseCalendar(wReader.get("RealStartDate"));
				wItem.RealFinishDate = StringUtils.parseCalendar(wReader.get("RealFinishDate"));
				wItem.RealSendDate = StringUtils.parseCalendar(wReader.get("RealSendDate"));
				wItem.Remark = StringUtils.parseString(wReader.get("Remark"));
				wItem.CreateID = StringUtils.parseInt(wReader.get("CreateID")).intValue();
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				wItem.EditID = StringUtils.parseInt(wReader.get("EditID")).intValue();
				wItem.EditTime = StringUtils.parseCalendar(wReader.get("EditTime"));
				wItem.AuditorID = StringUtils.parseInt(wReader.get("AuditorID")).intValue();
				wItem.AuditTime = StringUtils.parseCalendar(wReader.get("AuditTime"));
				wItem.Active = StringUtils.parseInt(wReader.get("Active")).intValue();
				wItem.RouteID = StringUtils.parseInt(wReader.get("RouteID")).intValue();
				wItem.TelegraphTime = StringUtils.parseCalendar(wReader.get("TelegraphTime"));
				wItem.TelegraphRealTime = StringUtils.parseCalendar(wReader.get("TelegraphRealTime"));

				wItem.LineName = APSConstans.GetFMCLineName(wItem.LineID);
				wItem.ProductNo = (APSConstans.GetFPCProduct(wItem.ProductID) == null) ? ""
						: (APSConstans.GetFPCProduct(wItem.ProductID)).ProductNo;
				wItem.BureauSection = APSConstans.GetCRMCustomerName(wItem.BureauSectionID);

				wItem.WBSNo = StringUtils.parseString(wReader.get("WBSNo"));
				wItem.CustomerID = StringUtils.parseInt(wReader.get("CustomerID")).intValue();
				wItem.Customer = APSConstans.GetCRMCustomerName(wItem.CustomerID);
				wItem.ContactCode = StringUtils.parseString(wReader.get("ContactCode"));
				wItem.No = StringUtils.parseString(wReader.get("No"));
				wItem.LinkManID = StringUtils.parseInt(wReader.get("LinkManID")).intValue();
				wItem.FactoryID = StringUtils.parseInt(wReader.get("FactoryID")).intValue();
				wItem.BusinessUnitID = StringUtils.parseInt(wReader.get("BusinessUnitID")).intValue();
				wItem.FQTYPlan = StringUtils.parseInt(wReader.get("FQTYPlan")).intValue();
				wItem.FQTYActual = StringUtils.parseInt(wReader.get("FQTYActual")).intValue();

				wItem.TimeAway = StringUtils.parseCalendar(wReader.get("TimeAway"));
				wItem.CompletionTelegramTime = StringUtils.parseCalendar(wReader.get("CompletionTelegramTime"));
				wItem.DriverOnTime = StringUtils.parseCalendar(wReader.get("DriverOnTime"));
				wItem.ToSegmentTime = StringUtils.parseCalendar(wReader.get("ToSegmentTime"));
				wItem.ActualRepairStopTimes = StringUtils.parseInt(wReader.get("ActualRepairStopTimes"));
				wItem.TelegraphRepairStopTimes = StringUtils.parseInt(wReader.get("TelegraphRepairStopTimes"));
				wItem.InPlantStopTimes = StringUtils.parseInt(wReader.get("InPlantStopTimes"));
				wItem.OnTheWayStopTimes = StringUtils.parseInt(wReader.get("OnTheWayStopTimes"));
				wItem.PosterioriStopTimes = StringUtils.parseInt(wReader.get("PosterioriStopTimes"));

				wItem.OrderType = StringUtils.parseInt(wReader.get("OrderType"));
				wItem.ParentID = StringUtils.parseInt(wReader.get("ParentID"));

				wItem.OrderTypeName = OMSOrderType.getEnumType(wItem.OrderType).getLable();

				if (wItem.RouteID == 0
						&& (APSConstans.GetFPCRoute(wItem.ProductID, wItem.LineID, wItem.CustomerID).ID > 0)
						&& StringUtils.isNotEmpty(wItem.PartNo)) {
					wItem.RouteID = APSConstans.GetFPCRoute(wItem.ProductID, wItem.LineID, wItem.CustomerID).ID;
					Update(wLoginUser, wItem, wErrorCode);
				}

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	public void Active(BMSEmployee wLoginUser, List<Integer> wIDList, int wActive, OutResult<Integer> wErrorCode) {
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return;
			}

			if (wIDList == null || wIDList.size() <= 0)
				return;
			for (Integer wItem : wIDList) {
				OMSOrder wOMSOrder = SelectByID(wLoginUser, wItem.intValue(), wErrorCode);
				if (wOMSOrder == null || wOMSOrder.ID <= 0) {
					continue;
				}
				if (wActive == 2 && wOMSOrder.Active != 1) {
					wErrorCode.set(Integer.valueOf(MESException.Logic.getValue()));
					return;
				}
				wOMSOrder.Active = wActive;
				long wID = Update(wLoginUser, wOMSOrder, wErrorCode);
				if (wID <= 0L)
					break;
			}
		} catch (Exception e) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(e.toString());
		}
	}

	public List<OMSOrder> SelectList(BMSEmployee wLoginUser, List<Integer> wCustomerList, List<Integer> wLineList,
			Calendar wStartTime, Calendar wEndTime, List<Integer> wStatusList, List<Integer> wProductList,
			String wPartNo, List<Integer> wActiveList, OutResult<Integer> wErrorCode) {
		List<OMSOrder> wResultList = new ArrayList<>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResultList;
			}

			if (wStatusList == null) {
				wStatusList = new ArrayList<>();
			}
			if (wCustomerList == null) {
				wCustomerList = new ArrayList<Integer>();
			}
			if (wLineList == null) {
				wLineList = new ArrayList<Integer>();
			}
			if (wActiveList == null) {
				wActiveList = new ArrayList<Integer>();
			}
			if (wProductList == null) {
				wProductList = new ArrayList<Integer>();
			}
			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1, 0, 0, 0);
			if (wStartTime == null) {
				wStartTime = wBaseTime;
			}
			if (wEndTime == null) {
				wEndTime = wBaseTime;
			}
			if (wStartTime.compareTo(wEndTime) > 0) {
				return wResultList;
			}

			String wSQL = StringUtils.Format("SELECT t1.*,t2.WBSNo,t2.CustomerID,t2.ContactCode,t2.No,t2.LinkManID,"
					+ "t2.FactoryID,t2.BusinessUnitID,t2.FQTYPlan,t2.FQTYActual "
					+ "FROM {0}.oms_order t1 left join {0}.oms_command t2 on t1.CommandID=t2.ID " + "WHERE  1=1 "
					+ "and ( :wPartNo is null or :wPartNo = '''' or t1.PartNo like ''%{1}%'' ) "
					+ "and ( :wLine is null or :wLine = '''' or t1.LineID in ({2})) "
					+ "and ( :wProduct is null or :wProduct = '''' or t1.ProductID in ({3})) "
					+ "and ( :wCustomer is null or :wCustomer = '''' or t2.CustomerID in ({4})) "
					+ "and ( :wActive is null or :wActive = '''' or t1.Active in ({5})) "
					+ "and ( :wStatus is null or :wStatus = '''' or t1.Status in ({6})) "
					+ "and ( :wStartTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wStartTime <= t1.RealReceiveDate) "
					+ "and ( :wEndTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wEndTime >= t1.RealReceiveDate);",
					wInstance.Result, wPartNo, (wLineList.size() > 0) ? StringUtils.Join(",", wLineList) : "0",
					(wProductList.size() > 0) ? StringUtils.Join(",", wProductList) : "0",
					(wCustomerList.size() > 0) ? StringUtils.Join(",", wCustomerList) : "0",
					(wActiveList.size() > 0) ? StringUtils.Join(",", wActiveList) : "0",
					(wStatusList.size() > 0) ? StringUtils.Join(",", wStatusList) : "0");
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wPartNo", wPartNo);
			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);
			wParamMap.put("wStatus", StringUtils.Join(",", wStatusList));
			wParamMap.put("wCustomer", StringUtils.Join(",", wCustomerList));
			wParamMap.put("wLine", StringUtils.Join(",", wLineList));
			wParamMap.put("wProduct", StringUtils.Join(",", wProductList));
			wParamMap.put("wActive", StringUtils.Join(",", wActiveList));

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetVaue(wLoginUser, wResultList, wQueryResult, wErrorCode);
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResultList;
	}

	/**
	 * 获取所有的在厂线上的订单ID集合，状态为“已进场”的。
	 * 
	 * @param wPartNoList
	 * @param wErrorCode
	 */
	public List<Integer> OMS_QueryInPlantCarList(BMSEmployee wLoginUser, List<String> wPartNoList,
			OutResult<Integer> wErrorCode) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"SELECT ID FROM {0}.oms_order where " + "PartNo in (''{1}'') and Status=3;", wInstance.Result,
					StringUtils.Join("','", wPartNoList));

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wOrderID = StringUtils.parseInt(wReader.get("ID"));
				if (wOrderID > 0) {
					wResult.add(wOrderID);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取所有的在厂线上的车号列表
	 * 
	 * @param wErrorCode
	 */
	public List<String> OMS_QueryPartNoListInPlant(BMSEmployee wLoginUser, OutResult<Integer> wErrorCode) {
		List<String> wResult = new ArrayList<String>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			ServiceResult<String> wInstance1 = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.EXC,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance1.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"SELECT t1.PartNo FROM {0}.fmc_workspace t1,{1}.lfs_storehouse t2 "
							+ "where t1.ParentID=t2.ID and t2.Type=2 and t1.PartNo != '''';",
					wInstance.Result, wInstance1.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				String wPartNo = StringUtils.parseString(wReader.get("PartNo"));
				if (StringUtils.isNotEmpty(wPartNo)) {
					wResult.add(wPartNo);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 清空台位上的车
	 */
	public void ClearCar(BMSEmployee wLoginUser, OMSOrder wOrder) {
		try {
			String wTransNo = wOrder.PartNo.replace(wOrder.ProductNo, wOrder.ProductNo + "[B]");
			String wBodyNo = wOrder.PartNo.replace(wOrder.ProductNo, wOrder.ProductNo + "[A]");
			String wPartNo = wOrder.PartNo;
			// ①获取台位列表
			List<FMCWorkspace> wWorkSpaceList = CoreServiceImpl.getInstance()
					.FMC_GetFMCWorkspaceList(wLoginUser, -1, -1, "", -1, 1).List(FMCWorkspace.class);
			// ②遍历台位列表HXD3C_1[A]#0001
			if (wWorkSpaceList != null && wWorkSpaceList.size() > 0) {
				for (FMCWorkspace wWorkSpace : wWorkSpaceList) {
					String wItemNo = "";
					String[] wStrs = wWorkSpace.PartNo.split("-");
					if (wStrs.length == 2) {
						wItemNo = wStrs[0];
//						wItemNo = StringUtils.Format("{0}#{1}", wStrs[0], wStrs[1].split("#")[1]);
					} else {
						wItemNo = wWorkSpace.PartNo;
					}
					// ③清空转向架④清空车体⑤清空整车HXD1C[A]#2232_A
					if (wItemNo.equals(wTransNo) || wItemNo.equals(wBodyNo) || wItemNo.equals(wPartNo)) {
						wWorkSpace.PartNo = "";

						wWorkSpace.ActualPartNoList.removeIf(p -> StringUtils.isNotEmpty(p) && ((!p.contains("-")
								&& (p.replace("[A]", "").equals(wPartNo) || p.replace("[B]", "").equals(wPartNo)))
								|| (p.contains("-")
										&& StringUtils
												.Format("{0}#{1}", p.split("-")[0].split("#")[0].replace("[A]", "")
														.replace("[B]", ""), p.split("-")[0].split("#")[1])
												.equals(wPartNo))));
						FMCServiceImpl.getInstance().FMC_BindFMCWorkspace(wLoginUser, wWorkSpace);
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 获取终检任务做完但未生成竣工确认单的订单ID集合
	 */
	public List<Integer> OMS_QueryNotGenerateConfirmFormOrderIDList(BMSEmployee wLoginUser,
			OutResult<Integer> wErrorCode) {
		List<Integer> wResult = new ArrayList<Integer>();
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

			String wSQL = StringUtils.Format(
					"select ID from {0}.oms_order t1 " + "where ID in(SELECT distinct OrderID FROM {0}.aps_taskstep "
							+ "where PartID in (select ID from {1}.fpc_part where PartType=3)) "
							+ "and ID not in (SELECT distinct OrderID FROM {0}.aps_taskstep "
							+ "where PartID in (select ID from {1}.fpc_part where PartType=3) and Status !=5) "
							+ "and 0 in (select count(*) from {0}.sfc_orderform where Type=1 and OrderID=t1.ID);",
					wInstance.Result, wInstance1.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wOrderID = StringUtils.parseInt(wReader.get("ID"));
				if (wOrderID > 0) {
					wResult.add(wOrderID);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据订单ID集合获取工位任务数据
	 */

	public List<OMSOrder> GetStationData(BMSEmployee wLoginUser, List<Integer> wOrderIDList,
			OutResult<Integer> wErrorCode) {
		List<OMSOrder> wResult = new ArrayList<OMSOrder>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wOrderIDList == null || wOrderIDList.size() <= 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("SELECT ID as  OrderID ,"
					+ "(select count(*)  from {0}.aps_taskpart where orderid=t.ID and ShiftPeriod=5 and Active=1 ) as StationTotalSize,"
					+ "(select count(*)  from {0}.aps_taskpart where orderid=t.ID and ShiftPeriod=5 and Active=1 and Status=5) as StationFinishSize"
					+ " FROM {0}.oms_order t where ID in ({1});", wInstance.Result,
					StringUtils.Join(",", wOrderIDList));

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				OMSOrder wItem = new OMSOrder();

				wItem.ID = StringUtils.parseInt(wReader.get("OrderID"));
				wItem.StationTotalSize = StringUtils.parseInt(wReader.get("StationTotalSize"));
				wItem.StationFinishSize = StringUtils.parseInt(wReader.get("StationFinishSize"));

				wResult.add(wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据WBS列表删除订单
	 */
	public Integer DeleteByWBSNoList(BMSEmployee wLoginUser, List<String> wWBSList, OutResult<Integer> wErrorCode) {
		Integer wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wWBSList == null || wWBSList.size() <= 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"delete from {0}.oms_order where " + "OrderNo in (''{1}'') and PartNo = '''' and ID>0;",
					wInstance.Result, StringUtils.Join("','", wWBSList));

			Map<String, Object> wParamMap = new HashMap<String, Object>();

//			System.out.println(wSQL);

			wSQL = this.DMLChange(wSQL);

			nameJdbcTemplate.update(wSQL, wParamMap);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据标准BOMID获取已进厂的订单列表
	 */
	public List<OMSOrder> GetListByBOMID(BMSEmployee wLoginUser, int wBOMID, OutResult<Integer> wErrorCode) {
		List<OMSOrder> wResult = new ArrayList<OMSOrder>();
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

			String wSQL = StringUtils.Format(
					"select t1.* from {0}.oms_order t1,{1}.mss_bom t2 "
							+ "where t1.LineID=t2.LineID and t1.ProductID=t2.ProductID "
							+ "and t1.BureauSectionID=t2.CustomerID and t1.PartNo !='''' "
							+ "and t2.ID=:wBOMID and t1.Active=1 and t1.Status>=3;",
					wInstance.Result, wInstance1.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wBOMID", wBOMID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetVaue(wLoginUser, wResult, wQueryResult, wErrorCode);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 判断该订单是否设置了当前标准BOM
	 */
	public boolean IsSettedCurrentStandardBOM(BMSEmployee wLoginUser, OMSOrder wOrder, OutResult<Integer> wErrorCode) {
		boolean wResult = false;
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

			String wSQL = StringUtils.Format("SELECT count(*) Number FROM {1}.mss_bom where "
					+ "IsStandard=1 and ProductID=:ProductID " + "and LineID=:LineID and CustomerID=:CustomerID;",
					wInstance.Result, wInstance1.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ProductID", wOrder.ProductID);
			wParamMap.put("LineID", wOrder.LineID);
			wParamMap.put("CustomerID", wOrder.CustomerID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wNumber = StringUtils.parseInt(wReader.get("Number"));
				if (wNumber > 0) {
					wResult = true;
					return wResult;
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 查询已添加过标准的工艺工序ID集合
	 */
	public List<Integer> GetSettedRoutePartPointIDList(BMSEmployee wLoginUser, OMSOrder wOrder,
			OutResult<Integer> wErrorCode) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			ServiceResult<String> wInstance1 = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.EXC,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance1.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"select distinct t1.ID from {0}.fpc_routepartpoint t1,{1}.ipt_standard t2 "
							+ "where t1.PartID=t2.PartID and t1.PartPointID=t2.PartPointID and t2.IsCurrent=1 "
							+ "and t2.ProductID=:ProductID and t2.LineID=:LineID and t1.RouteID=:RouteID;",
					wInstance.Result, wInstance1.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ProductID", wOrder.ProductID);
			wParamMap.put("LineID", wOrder.LineID);
			wParamMap.put("RouteID", wOrder.RouteID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wID = StringUtils.parseInt(wReader.get("ID"));
				if (wID > 0) {
					wResult.add(wID);
				}
			}

			// 查询引用的ID
			List<Integer> wList = GetRefStepIDList(wLoginUser, wOrder, wErrorCode);
			if (wList.size() > 0) {
				String wStepIDs = StringUtils.Join(",", wList);
				List<Integer> wIDList = GetRoutePartPointIDByRefStepIDs(wLoginUser, wOrder.RouteID, wStepIDs,
						wErrorCode);
				for (Integer wID : wIDList) {
					if (!wResult.stream().anyMatch(p -> p.intValue() == wID.intValue())) {
						wResult.add(wID);
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	public List<Integer> GetRefStepIDList(BMSEmployee wLoginUser, OMSOrder wOrder, OutResult<Integer> wErrorCode) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"SELECT StepIDs FROM {0}.ipt_standard "
							+ "WHERE LineID=:LineID AND ProductID=:ProductID AND IsCurrent=1 AND StepIDs != '''';",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("LineID", wOrder.LineID);
			wParamMap.put("ProductID", wOrder.ProductID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				String wStepIDs = StringUtils.parseString(wReader.get("StepIDs"));
				if (StringUtils.isNotEmpty(wStepIDs)) {
					String[] wStrs = wStepIDs.split(",");
					for (String wStr : wStrs) {
						int wStepID = StringUtils.parseInt(wStr);
						if (!wResult.stream().anyMatch(p -> p == wStepID)) {
							wResult.add(wStepID);
						}
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	public List<Integer> GetRoutePartPointIDByRefStepIDs(BMSEmployee wLoginUser, int wRouteID, String wStepIDs,
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
					"SELECT ID FROM {0}.fpc_routepartpoint WHERE routeid=:wRouteID AND partpointid IN ({1});",
					wInstance.Result, wStepIDs);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wRouteID", wRouteID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wID = StringUtils.parseInt(wReader.get("ID"));
				if (wID > 0) {
					wResult.add(wID);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 查询未设置标准的工艺工序集合
	 */
	public List<FPCRoutePartPoint> SelectNoSettedStandardList(BMSEmployee wLoginUser, OMSOrder wOrder,
			OutResult<Integer> wErrorCode) {
		List<FPCRoutePartPoint> wResult = new ArrayList<FPCRoutePartPoint>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = "";
			List<Integer> wList = GetSettedRoutePartPointIDList(wLoginUser, wOrder, wErrorCode);
			if (wList.size() > 0) {
				wSQL = StringUtils.Format(
						"select PartID,PartPointID from {0}.fpc_routepartpoint "
								+ "where RouteID=:RouteID and ID not in ({1});",
						wInstance.Result, StringUtils.Join(",", wList));
			} else {
				wSQL = StringUtils.Format(
						"select PartID,PartPointID from {0}.fpc_routepartpoint " + "where RouteID=:RouteID;",
						wInstance.Result);
			}

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("RouteID", wOrder.RouteID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				FPCRoutePartPoint wItem = new FPCRoutePartPoint();

				wItem.PartID = StringUtils.parseInt(wReader.get("PartID"));
				wItem.PartPointID = StringUtils.parseInt(wReader.get("PartPointID"));
				wItem.PartName = APSConstans.GetFPCPartName(wItem.PartID);
				wItem.PartPointName = APSConstans.GetFPCPartPointName(wItem.PartPointID);

				wResult.add(wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 禁用日计划
	 */
	public void DisableAPSTaskStep(BMSEmployee wLoginUser, List<Integer> wOrderIDList, OutResult<Integer> wErrorCode) {
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return;
			}

			String wSQL = StringUtils.Format("update {0}.aps_taskstep set Active=2 where OrderID in ({1});",
					wInstance.Result, StringUtils.Join(",", wOrderIDList));

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			nameJdbcTemplate.update(wSQL, wParamMap);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 获取派工任务ID集合
	 */
	public List<Integer> GetSFCTaskStepIDList(BMSEmployee wLoginUser, List<Integer> wOrderIDList,
			OutResult<Integer> wErrorCode) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"select ID from {0}.sfc_taskstep where  TaskStepID in "
							+ "(select ID from {0}.aps_taskstep where OrderID in ({1}));",
					wInstance.Result, StringUtils.Join(",", wOrderIDList));

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wID = StringUtils.parseInt(wReader.get("ID"));
				if (wID > 0) {
					wResult.add(wID);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 禁用派工计划
	 */
	public void DisableSFCTaskStep(BMSEmployee wLoginUser, List<Integer> wSFCTaskStepIDList,
			OutResult<Integer> wErrorCode) {
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return;
			}

			String wSQL = StringUtils.Format("update {0}.sfc_taskstep set Active=2 where ID in ({1});",
					wInstance.Result, StringUtils.Join(",", wSFCTaskStepIDList));

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			nameJdbcTemplate.update(wSQL, wParamMap);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 禁用检验单
	 */
	public void DisableSFCTaskIPT(BMSEmployee wLoginUser, List<Integer> wOrderIDList, OutResult<Integer> wErrorCode) {
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return;
			}

			String wSQL = StringUtils.Format("update {0}.sfc_taskipt set Active=2 where orderid in ({1}) and ID>0;",
					wInstance.Result, StringUtils.Join(",", wOrderIDList));

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			nameJdbcTemplate.update(wSQL, wParamMap);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 查询派工消息ID集合
	 */
	public List<Integer> GetPGMessageIDList(BMSEmployee wLoginUser, List<Integer> wOrderIDList,
			OutResult<Integer> wErrorCode) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			ServiceResult<String> wInstance1 = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance1.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"select ID from {0}.bfc_message  where ModuleID = 8103 and "
							+ "MessageID in (select ID from {2}.sfc_taskstep where "
							+ "TaskStepID in (select ID from {2}.aps_taskstep where OrderID in ({1})));",
					wInstance.Result, StringUtils.Join(",", wOrderIDList), wInstance1.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {

				int wID = StringUtils.parseInt(wReader.get("ID"));
				if (wID > 0) {
					wResult.add(wID);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 禁用消息
	 */
	public void DisableMessageByIDList(BMSEmployee wLoginUser, List<Integer> wPGMessageIDList,
			OutResult<Integer> wErrorCode) {
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return;
			}

			String wSQL = StringUtils.Format("update {0}.bfc_message set Active=4 where ID in ({1});", wInstance.Result,
					StringUtils.Join(",", wPGMessageIDList));

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			nameJdbcTemplate.update(wSQL, wParamMap);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 查询检验任务消息集合
	 */
	public List<Integer> GetIPTMessageIDList(BMSEmployee wLoginUser, List<Integer> wOrderIDList,
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
					"select ID from {0}.bfc_message  where ModuleID in( 1003,8112,8113,8114) and "
							+ "MessageID in (select ID from {0}.sfc_taskipt where orderid in ({1}));",
					wInstance.Result, StringUtils.Join(",", wOrderIDList));

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wID = StringUtils.parseInt(wReader.get("ID"));
				if (wID > 0) {
					wResult.add(wID);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 查询状态为4的，无计划的订单数据
	 */
	public List<Integer> OMS_QueryInPlantCarList_Repairing(BMSEmployee wLoginUser, List<String> wPartNoList,
			OutResult<Integer> wErrorCode) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"SELECT t1.ID FROM {0}.oms_order t1 " + "where t1.Status=4 and t1.PartNo in (''{1}'') "
							+ "and 0 in (select count(*) from {0}.aps_taskstep where OrderID=t1.ID);",
					wInstance.Result, StringUtils.Join("','", wPartNoList));

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wOrderID = StringUtils.parseInt(wReader.get("ID"));
				wResult.add(wOrderID);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据周计划，获取正在维修中的，订单ID集合
	 */
	public List<Integer> SelectIDListByWeekPlan(BMSEmployee wLoginUser, OutResult<Integer> wErrorCode) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("SELECT distinct t1.OrderID FROM {0}.aps_taskpart t1,{0}.oms_order t2 "
					+ "where t1.OrderID=t2.ID and t2.Status=4 and t1.ShiftPeriod=5 and t1.Active=1 and t1.Status in (0,1,2,3,4);",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wOrderID = StringUtils.parseInt(wReader.get("OrderID"));
				if (wOrderID > 0) {
					wResult.add(wOrderID);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	public List<Integer> SelectFinishedSelfOrderIDListByOrderType(BMSEmployee wLoginUser, int wOrderType,
			OutResult<Integer> wErrorCode) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("SELECT ID FROM {0}.oms_order "
					+ "where OrderType=:OrderType and Status in (5,6,7,8) and ParentID=0;", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("OrderType", wOrderType);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wID = StringUtils.parseInt(wReader.get("ID"));
				if (wID > 0) {
					wResult.add(wID);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 判断订单是否是非线上车
	 */
	public boolean IsOffLineBoarding(BMSEmployee wLoginUser, int wOrderID, OutResult<Integer> wErrorCode) {
		boolean wResult = true;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("select count(*) Number from {0}.aps_taskpart "
					+ "where ShiftPeriod=5 and Active=1 and OrderID=:OrderID;", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("OrderID", wOrderID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wNumber = StringUtils.parseInt(wReader.get("Number"));
				if (wNumber > 0) {
					return false;
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据订单ID获取最新的工艺路线ID
	 */
	public int GetRouteIDByOrderID(BMSEmployee wLoginUser, int orderID, OutResult<Integer> wErrorCode) {
		int wResult = 0;
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

			String wSQL = StringUtils.Format(
					"SELECT t1.ID FROM {1}.fpc_route t1,{0}.oms_order t2 "
							+ "where t1.ProductID=t2.ProductID and t1.LineID=t2.LineID "
							+ "and t1.CustomerID=t2.BureauSectionID and t1.IsStandard=1 and t2.ID=:OrderID;",
					wInstance.Result, wInstance1.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("OrderID", orderID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				wResult = StringUtils.parseInt(wReader.get("ID"));
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	public List<Integer> SelectListByYearTime(BMSEmployee wLoginUser, Calendar wSTime, Calendar wETime,
			OutResult<Integer> wErrorCode) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"SELECT ID FROM {0}.oms_order " + "where RealReceiveDate > :wSTime and  RealReceiveDate < :wETime;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wSTime", wSTime);
			wParamMap.put("wETime", wETime);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wID = StringUtils.parseInt(wReader.get("ID"));
				wResult.add(wID);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取最大的ERPID
	 */
	public int GetMaxERPID(BMSEmployee wLoginUser, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("SELECT Max(ERPID)+1 as ERPID FROM {0}.oms_order ;", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				wResult = StringUtils.parseInt(wReader.get("ERPID"));
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}
}
