package com.mes.loco.aps.server.serviceimpl.dao.andon;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mes.loco.aps.server.service.mesenum.MESDBSource;
import com.mes.loco.aps.server.service.mesenum.MESException;
import com.mes.loco.aps.server.service.mesenum.MTCStatus;
import com.mes.loco.aps.server.service.mesenum.OMSOrderStatus;
import com.mes.loco.aps.server.service.mesenum.SFCTurnOrderTaskStatus;
import com.mes.loco.aps.server.service.po.APIResult;
import com.mes.loco.aps.server.service.po.OutResult;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.andon.AndonDayPlanPartCashingRate;
import com.mes.loco.aps.server.service.po.andon.AndonDayReportTJ;
import com.mes.loco.aps.server.service.po.andon.AndonLocomotiveProductionStatus;
import com.mes.loco.aps.server.service.po.andon.AndonProduct;
import com.mes.loco.aps.server.service.po.andon.AndonScreen;
import com.mes.loco.aps.server.service.po.andon.AndonStoreHouse;
import com.mes.loco.aps.server.service.po.andon.AndonTaskStep;
import com.mes.loco.aps.server.service.po.andon.AndonWorkspace;
import com.mes.loco.aps.server.service.po.andon.AndonYearReport;
import com.mes.loco.aps.server.service.po.andon.OrderTimeInfo;
import com.mes.loco.aps.server.service.po.aps.APSTaskPart;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.exc.EXCCallTaskBPM;
import com.mes.loco.aps.server.service.po.fmc.FMCWorkspace;
import com.mes.loco.aps.server.service.po.mtc.MTCTask;
import com.mes.loco.aps.server.service.po.oms.OMSOrder;
import com.mes.loco.aps.server.service.po.rsm.RSMTurnOrderTask;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.APSServiceImpl;
import com.mes.loco.aps.server.serviceimpl.AndonServiceImpl;
import com.mes.loco.aps.server.serviceimpl.CoreServiceImpl;
import com.mes.loco.aps.server.serviceimpl.LFSServiceImpl;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;
import com.mes.loco.aps.server.serviceimpl.dao.fmc.FMCWorkspaceDAO;
import com.mes.loco.aps.server.serviceimpl.dao.mtc.MTCTaskDAO;
import com.mes.loco.aps.server.serviceimpl.dao.oms.OMSOrderDAO;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSConstans;

public class AndonDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(AndonDAO.class);

	private static AndonDAO Instance = null;

	public static AndonScreen Screen = new AndonScreen();

	private AndonDAO() {
		super();
	}

	public static AndonDAO getInstance() {
		if (Instance == null)
			Instance = new AndonDAO();
		return Instance;
	}

	/**
	 * 获取异常数
	 * 
	 * @return
	 */
	public int SelectExceptionCount(BMSEmployee wLoginUser, String wPartNo, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.EXC,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("SELECT count(*) as Number FROM {0}.exc_andon WHERE CarName=:CarName;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("CarName", wPartNo);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				wResult = StringUtils.parseInt(wReader.get("Number"));
				return wResult;
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取返修
	 * 
	 * @return
	 */
	public int SelectRepairCount(BMSEmployee wLoginUser, int wOrderID, int wStationID, Calendar wStartTime,
			OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.WDW,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1);
			if (wStartTime == null) {
				wStartTime = wBaseTime;
			}

			String wSQL = StringUtils.Format("SELECT count(*) as Number FROM {0}.rro_repairitem where "
					+ "OrderID=:OrderID and StationID=:StationID and Status not in(0,25);", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("OrderID", wOrderID);
			wParamMap.put("StationID", wStationID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				wResult = StringUtils.parseInt(wReader.get("Number"));
				return wResult;
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取未完成的返修数
	 * 
	 * @return
	 */
	public int SelectRepairCountNF(BMSEmployee wLoginUser, int wOrderID, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.WDW,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("SELECT count(*) as Number FROM {0}.rro_repairitem WHERE OrderID=:OrderID "
					+ "and Status not in (0,21,22,25);", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("OrderID", wOrderID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				wResult = StringUtils.parseInt(wReader.get("Number"));
				return wResult;
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取不合格评审数
	 * 
	 * @return
	 */
	public int SelectNcrCount(BMSEmployee wLoginUser, int wProductID, String wCarNumber, int wStationID,
			Calendar wStartTime, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.WDW,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1);
			if (wStartTime == null) {
				wStartTime = wBaseTime;
			}

			String wSQL = StringUtils.Format("SELECT count(*) as Number FROM {0}.ncr_task where "
					+ "CarTypeID=:CarTypeID " + "and CarNumber=:CarNumber "
					+ "and (StationID=:StationID or CloseStationID=:StationID) " + "and Status not in(0,12,21,22)",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("CarNumber", wCarNumber);
			wParamMap.put("CarTypeID", wProductID);
			wParamMap.put("StationID", wStationID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				wResult = StringUtils.parseInt(wReader.get("Number"));
				return wResult;
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取未完成的不合格评审数
	 * 
	 * @return
	 */
	public int SelectNcrCountNF(BMSEmployee wLoginUser, int wOrderID, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.WDW,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("SELECT count(*) as Number FROM {0}.ncr_task WHERE OrderID=:OrderID "
					+ "and Status not in (0,12,22,21);", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("OrderID", wOrderID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				wResult = StringUtils.parseInt(wReader.get("Number"));
				return wResult;
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据车号、工位编码获取正在发生的异常
	 */
	public int SelectExceptionCount(BMSEmployee wLoginUser, String wStationCode, String wPartNo,
			OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.EXC,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"SELECT count(*) as Number FROM " + "{0}.exc_calltaskbpm t1 left join "
							+ "{0}.exc_station t2 on t1.StationID=t2.ID "
							+ "where t1.PartNo=:CarName and t2.StationNo=:StationCode " + "and t1.Status in(1,2,3,22);",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("CarName", wPartNo);
			wParamMap.put("StationCode", wStationCode);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				wResult = StringUtils.parseInt(wReader.get("Number"));
				return wResult;
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据订单获取工位总数、工位完成数
	 */
	public OrderTimeInfo QueryStationProgress(BMSEmployee wLoginUser, int wOrderID, OutResult<Integer> wErrorCode) {
		OrderTimeInfo wResult = new OrderTimeInfo();
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

			String wSQL = StringUtils.Format("select (SELECT count(*) FROM {1}.fpc_routepart t1,{0}.oms_order t2 "
					+ "where t2.RouteID=t1.RouteID and t2.ID=:OrderID) as TotalNum,(SELECT count(distinct(PartID)) "
					+ "FROM {0}.aps_taskpart where OrderID=:OrderID and ShiftPeriod=5 "
					+ "and Status=5 and Active=1) as FinishNum;", wInstance.Result, wInstance1.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("OrderID", wOrderID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				wResult.STTotal = StringUtils.parseInt(wReader.get("TotalNum"));
				wResult.STFinish = StringUtils.parseInt(wReader.get("FinishNum"));
				return wResult;
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据订单获取第一个工位列表
	 */
	public List<Integer> QueryFirstStationList(BMSEmployee wLoginUser, int wOrderID, OutResult<Integer> wErrorCode) {
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
					"SELECT distinct(PartID) FROM {0}.fpc_routepart t1,{1}.oms_order t2 "
							+ "where t2.RouteID=t1.RouteID and t1.PrevPartID=0 and t2.ID=:OrderID;",
					wInstance.Result, wInstance1.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("OrderID", wOrderID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wPartID = StringUtils.parseInt(wReader.get("PartID"));
				if (wPartID > 0) {
					wResult.add(wPartID);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据订单获取当前工位列表
	 */
	public List<Integer> QueryCurrentStationList(BMSEmployee wLoginUser, int wOrderID, OutResult<Integer> wErrorCode) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils
					.Format("select * from {0}.sfc_turnordertask where ApplyTime in(select max(ApplyTime) "
							+ "FROM {0}.sfc_turnordertask where OrderID=:OrderID);", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("OrderID", wOrderID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wApplyStationID = StringUtils.parseInt(wReader.get("ApplyStationID"));
				int wTargetStationID = StringUtils.parseInt(wReader.get("TargetStationID"));
				int wStatus = StringUtils.parseInt(wReader.get("Status"));
				if (wStatus == SFCTurnOrderTaskStatus.Auditing.getValue() && wApplyStationID > 0) {
					wResult.add(wApplyStationID);
				} else if (wStatus == SFCTurnOrderTaskStatus.Passed.getValue() && wTargetStationID > 0) {
					wResult.add(wTargetStationID);
				}
			}

			// 去重
			wResult = wResult.stream().distinct().collect(Collectors.toList());
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据订单获取当前工位列表
	 */
	public List<Integer> QueryCurrentStationList_V2(BMSEmployee wLoginUser, int wOrderID,
			OutResult<Integer> wErrorCode) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			// ①根据订单ID集合获取转序单列表
			List<RSMTurnOrderTask> wTurnOrderList = QueryTurnOrderListByOrderIDs(wLoginUser, String.valueOf(wOrderID),
					wErrorCode);
			// ③遍历转序单，获取转序单列表中，申请工位不等于自己的目标工位的转序单(同订单下)
			List<RSMTurnOrderTask> wTurnOrderLessList = new ArrayList<RSMTurnOrderTask>();
			for (RSMTurnOrderTask wRSMTurnOrderTask : wTurnOrderList) {
				if (wTurnOrderList.stream().anyMatch(p -> p.ApplyStationID == wRSMTurnOrderTask.TargetStationID
						&& p.OrderID == wRSMTurnOrderTask.OrderID)) {
					continue;
				}
				wTurnOrderLessList.add(wRSMTurnOrderTask);
			}
			// ⑤根据筛选的转序单添加到返回集中
			List<RSMTurnOrderTask> wOTRList = wTurnOrderLessList.stream().filter(p -> p.OrderID == wOrderID)
					.collect(Collectors.toList());
			for (RSMTurnOrderTask wRSMTurnOrderTask : wOTRList) {
				if (wRSMTurnOrderTask.Status == 1) {
					wResult.add(wRSMTurnOrderTask.ApplyStationID);
				} else if (wRSMTurnOrderTask.Status == 2) {
					wResult.add(wRSMTurnOrderTask.TargetStationID);
				}
			}
			// ⑥去重
			if (wResult.size() > 0) {
				wResult = wResult.stream().distinct().collect(Collectors.toList());
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据订单和工位获取看板数据行
	 */
	public OrderTimeInfo QueryOrderTimeInfo(BMSEmployee wLoginUser, OMSOrder wOrder, int wStationID,
			OutResult<Integer> wErrorCode) {
		OrderTimeInfo wResult = new OrderTimeInfo();
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

			ServiceResult<String> wInstance2 = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.EXC,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance2.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			ServiceResult<String> wInstance3 = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.WDW,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance3.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wPartNo = wOrder.PartNo.replace("#", "[A]#");

			String wSQL = StringUtils.Format("select  "
					+ "	(select PartNo from {0}.oms_order where ID=:OrderID) as PartNo , "
					+ "	(SELECT t1.CustomerName FROM {2}.crm_customer t1,{0}.oms_order t2,{0}.oms_command t3 where t1.ID=t3.CustomerID and t3.ID=t2.CommandID and t2.ID=:OrderID) as CustomerName,"
					+ "	(SELECT t1.Name FROM {2}.fmc_line t1,{0}.oms_order t2 where t2.ID=:OrderID and t2.LineID=t1.ID) as LineName,"
					+ "	(SELECT RealReceiveDate FROM {0}.oms_order where ID=:OrderID) as InPlantTime,"
					+ "	(select to_days(now()) - to_days(RealReceiveDate) from {0}.oms_order where ID=:OrderID) as StopTime ,"
					+ "	(SELECT t3.Name FROM {2}.fmc_workspace t1,{0}.oms_order t2,{3}.lfs_storehouse t3 where (t1.PartNo=t2.PartNo or t1.PartNo = ''{1}'') and t2.ID=:OrderID and t1.ParentID=t3.ID limit 1) as StockName,"
					+ "    (SELECT t1.Name FROM {2}.fmc_workspace t1,{0}.oms_order t2 where (t1.PartNo=t2.PartNo or t1.PartNo = ''{1}'') and t2.ID=:OrderID limit 1) as PlaceName,"
					+ "    (SELECT Name FROM {2}.fpc_part where ID=:StationID) as StationName,"
					+ "    (SELECT count(*) FROM {0}.aps_taskstep where OrderID=:OrderID and PartID=:StationID and Status=5 and Active=1) as FinishNum,"
					+ "    (SELECT count(*) FROM {2}.fpc_routepartpoint t1,{0}.oms_order t2 where t2.RouteID=t1.RouteID and t1.PartID=:StationID and t2.ID=:OrderID) as TotalNum,"
					+ "    (SELECT count(*) FROM {4}.rro_repairitem where OrderID=:OrderID and StationID=:StationID and Status not in(0,25)) as ReworkNum,"
					+ "    (SELECT count(*)  FROM {4}.ncr_task where OrderID=:OrderID and (StationID=:StationID or CloseStationID=:StationID) and Status not in(0,12,21,22)) as NcrNum,"
					+ "    (SELECT count(*) FROM {3}.exc_calltaskbpm t1 , {3}.exc_station t2 ,{2}.fpc_part t3,{0}.oms_order t4 where  t1.StationID=t2.ID  and t3.ID=:StationID and t3.Code=t2.StationNo and t1.PartNo=t4.PartNo and t4.ID=:OrderID  and t1.Status in(1,2,3,22)) as ExcNum,"
					+ "	(SELECT Status FROM {0}.aps_taskpart where OrderID=:OrderID and PartID=:StationID and ShiftPeriod=5 and Active=1 limit 1) as Status,"
					+ "    (SELECT count(*) FROM {2}.fpc_routepart t1,{0}.oms_order t2 where t2.RouteID=t1.RouteID and t2.ID=:OrderID) as STTotal,"
					+ "    (SELECT count(distinct(PartID)) FROM {0}.aps_taskpart where OrderID=:OrderID and ShiftPeriod=5 and Status=5 and Active=1) as STFinish;",
					wInstance.Result, wPartNo, wInstance1.Result, wInstance2.Result, wInstance3.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("OrderID", wOrder.ID);
			wParamMap.put("StationID", wStationID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SimpleDateFormat wSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (Map<String, Object> wReader : wQueryResult) {
				wResult.CustomerName = StringUtils.parseString(wReader.get("CustomerName"));
				wResult.ExcNum = StringUtils.parseInt(wReader.get("ExcNum"));
				wResult.FinishNum = StringUtils.parseInt(wReader.get("FinishNum"));
				wResult.InPlantTime = StringUtils.parseCalendar(wReader.get("InPlantTime"));
				wResult.LineName = StringUtils.parseString(wReader.get("LineName"));
				wResult.NcrNum = StringUtils.parseInt(wReader.get("NcrNum"));
				wResult.OrderID = wOrder.ID;
				wResult.PartNo = StringUtils.parseString(wReader.get("PartNo"));
				wResult.PlaceName = StringUtils.parseString(wReader.get("PlaceName"));
				wResult.RealReceiveDate = wSDF.format(wResult.InPlantTime.getTime());
				wResult.ReworkNum = StringUtils.parseInt(wReader.get("ReworkNum"));
				wResult.StationName = StringUtils.parseString(wReader.get("StationName"));
				int wStatus = StringUtils.parseInt(wReader.get("Status"));
				wResult.Status = "未开工";
				if (wStatus == 5) {
					wResult.Status = "工位完工";
				} else if (wStatus == 4) {
					wResult.Status = "工位开工";
				}
				if (wResult.ExcNum > 0) {
					wResult.Status = "异常";
				}
				wResult.StockName = StringUtils.parseString(wReader.get("StockName"));
				wResult.StopTime = StringUtils.parseInt(wReader.get("StopTime"));
				int wTotalNum = StringUtils.parseInt(wReader.get("TotalNum"));
				wResult.SurplusNum = wTotalNum - wResult.FinishNum;
				wResult.STTotal = StringUtils.parseInt(wReader.get("STTotal"));
				wResult.STFinish = StringUtils.parseInt(wReader.get("STFinish"));
				return wResult;
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取台位车辆映射
	 */
	public Map<Integer, String> QueryWorkspaceCarMap(BMSEmployee wLoginUser, OutResult<Integer> wErrorCode) {
		Map<Integer, String> wResult = new HashMap<Integer, String>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("SELECT ID,PartNo FROM {0}.fmc_workspace where PartNo !='''' "
					+ "and not PartNo like ''%转运平车%'' and not PartNo like ''%假台车%'';", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wID = StringUtils.parseInt(wReader.get("ID"));
				String wPartNo = StringUtils.parseString(wReader.get("PartNo"));

				if (wID <= 0) {
					continue;
				}

				wResult.put(wID, wPartNo);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取车辆状态映射
	 */
	public Map<String, Integer> QueryCarStatusMap(BMSEmployee wLoginUser, String wOrderIDs,
			OutResult<Integer> wErrorCode) {
		Map<String, Integer> wResult = new HashMap<String, Integer>();
		try {
			// ①根据订单ID集合获取转序单列表
			List<RSMTurnOrderTask> wTurnOrderList = QueryTurnOrderListByOrderIDs(wLoginUser, wOrderIDs, wErrorCode);
			// ②根据订单ID集合获取工位任务列表
			List<APSTaskPart> wTaskPartList = QueryTaskPartListByOrderIDs(wLoginUser, wOrderIDs, wErrorCode);
			// ③遍历转序单，获取转序单列表中，申请工位不等于自己的目标工位的转序单(同订单下)
			List<RSMTurnOrderTask> wTurnOrderLessList = new ArrayList<RSMTurnOrderTask>();
			for (RSMTurnOrderTask wRSMTurnOrderTask : wTurnOrderList) {
				if (wTurnOrderList.stream().anyMatch(p -> p.ApplyStationID == wRSMTurnOrderTask.TargetStationID
						&& p.OrderID == wRSMTurnOrderTask.OrderID)) {
					continue;
				}
				wTurnOrderLessList.add(wRSMTurnOrderTask);
			}
			// ④根据订单ID集合获取订单列表
			List<OMSOrder> wOrderList = QueryOrderListByOrderIDs(wLoginUser, wOrderIDs, wErrorCode);
			// ⑤遍历订单列表，根据筛选的转序单和工位任务获取车辆状态，添加到返回集中
			List<Integer> wStatusList = null;
			for (OMSOrder wOMSOrder : wOrderList) {
				wStatusList = new ArrayList<Integer>();
				List<RSMTurnOrderTask> wOTRList = wTurnOrderLessList.stream().filter(p -> p.OrderID == wOMSOrder.ID)
						.collect(Collectors.toList());
				for (RSMTurnOrderTask wRSMTurnOrderTask : wOTRList) {
					if (wRSMTurnOrderTask.Status == 1) {
						if (wTaskPartList.stream().anyMatch(
								p -> p.OrderID == wOMSOrder.ID && p.PartID == wRSMTurnOrderTask.ApplyStationID)) {
							APSTaskPart wTaskPart = wTaskPartList.stream().filter(
									p -> p.OrderID == wOMSOrder.ID && p.PartID == wRSMTurnOrderTask.ApplyStationID)
									.findFirst().get();
							wStatusList.add(wTaskPart.Status);
						}
					} else if (wRSMTurnOrderTask.Status == 2) {
						if (wTaskPartList.stream().anyMatch(
								p -> p.OrderID == wOMSOrder.ID && p.PartID == wRSMTurnOrderTask.TargetStationID)) {
							APSTaskPart wTaskPart = wTaskPartList.stream().filter(
									p -> p.OrderID == wOMSOrder.ID && p.PartID == wRSMTurnOrderTask.TargetStationID)
									.findFirst().get();
							wStatusList.add(wTaskPart.Status);
						}
					}
				}

				if (wStatusList.size() > 0) {
					wResult.put(wOMSOrder.PartNo, Collections.min(wStatusList));
				} else {
					wResult.put(wOMSOrder.PartNo, 0);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取车辆状态映射
	 */
	public Map<String, Integer> QueryCarStatusMapNew(BMSEmployee wLoginUser, String wOrderIDs,
			OutResult<Integer> wErrorCode) {
		Map<String, Integer> wResult = new HashMap<String, Integer>();
		try {
			// ④根据订单ID集合获取订单列表
			List<OMSOrder> wOrderList = QueryOrderListByOrderIDs(wLoginUser, wOrderIDs, wErrorCode);
			// ⑤遍历订单列表，根据筛选的转序单和工位任务获取车辆状态，添加到返回集中
			for (OMSOrder wOMSOrder : wOrderList) {
				if (wOMSOrder.Status == OMSOrderStatus.Repairing.getValue()) {
					wResult.put(wOMSOrder.PartNo, 4);
				} else if (wOMSOrder.Status >= OMSOrderStatus.FinishedWork.getValue()) {
					wResult.put(wOMSOrder.PartNo, 5);
				} else {
					wResult.put(wOMSOrder.PartNo, 0);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据订单ID集合获取订单列表
	 */
	private List<OMSOrder> QueryOrderListByOrderIDs(BMSEmployee wLoginUser, String wOrderIDs,
			OutResult<Integer> wErrorCode) {
		List<OMSOrder> wResult = new ArrayList<OMSOrder>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("SELECT ID,PartNo,Status FROM {0}.oms_order where ID in({1});",
					wInstance.Result, wOrderIDs);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				OMSOrder wOMSOrder = new OMSOrder();

				wOMSOrder.ID = StringUtils.parseInt(wReader.get("ID"));
				wOMSOrder.Status = StringUtils.parseInt(wReader.get("Status"));
				wOMSOrder.PartNo = StringUtils.parseString(wReader.get("PartNo"));

				wResult.add(wOMSOrder);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据订单ID集合获取工位任务列表(周计划)
	 */
	private List<APSTaskPart> QueryTaskPartListByOrderIDs(BMSEmployee wLoginUser, String wOrderIDs,
			OutResult<Integer> wErrorCode) {
		List<APSTaskPart> wResult = new ArrayList<APSTaskPart>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("SELECT OrderID,PartID,Status FROM {0}.aps_taskpart "
					+ "where ShiftPeriod=5 and Active=1 and OrderID in({1});", wInstance.Result, wOrderIDs);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				APSTaskPart wItem = new APSTaskPart();

				wItem.OrderID = StringUtils.parseInt(wReader.get("OrderID"));
				wItem.PartID = StringUtils.parseInt(wReader.get("PartID"));
				wItem.Status = StringUtils.parseInt(wReader.get("Status"));

				wResult.add(wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据订单ID集合获取转序单列表
	 */
	private List<RSMTurnOrderTask> QueryTurnOrderListByOrderIDs(BMSEmployee wLoginUser, String wOrderIDs,
			OutResult<Integer> wErrorCode) {
		List<RSMTurnOrderTask> wResult = new ArrayList<RSMTurnOrderTask>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("SELECT OrderID,ApplyStationID,TargetStationID,Status "
					+ "FROM {0}.sfc_turnordertask where OrderID in({1});", wInstance.Result, wOrderIDs);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				RSMTurnOrderTask wItem = new RSMTurnOrderTask();

				wItem.OrderID = StringUtils.parseInt(wReader.get("OrderID"));
				wItem.ApplyStationID = StringUtils.parseInt(wReader.get("ApplyStationID"));
				wItem.TargetStationID = StringUtils.parseInt(wReader.get("TargetStationID"));
				wItem.Status = StringUtils.parseInt(wReader.get("Status"));

				wResult.add(wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 周期性刷新配置数据
	 */
	public boolean Andon_UpdateConfiguration(BMSEmployee wLoginUser) {
		boolean wUpdateConfiguration = false;
		try {
			if (!AndonDAO.Screen.Configuration) {
				// Step01：获取库位列表；
				APIResult wApiStoreHouseResult = LFSServiceImpl.getInstance().LFS_QueryStoreHouseList(wLoginUser);
				List<AndonStoreHouse> wStoreHouseList = wApiStoreHouseResult.List(AndonStoreHouse.class);
				if (wStoreHouseList == null || wStoreHouseList.size() <= 0)
					return wUpdateConfiguration;

				if (wStoreHouseList.size() > 0)
					AndonDAO.Screen.StoreHouseList = wStoreHouseList.stream().collect(Collectors.toList());

				// Step02：获取台位列表；
				APIResult wApiWorkspaceResult = CoreServiceImpl.getInstance().FMC_GetFMCWorkspaceList(wLoginUser, -1,
						-1, "", -1, 1);
				List<AndonWorkspace> wWorkspaceList = wApiWorkspaceResult.List(AndonWorkspace.class);
				if (wWorkspaceList == null || wWorkspaceList.size() <= 0)
					return wUpdateConfiguration;

				if (wWorkspaceList.size() > 0)
					AndonDAO.Screen.WorkspaceList = wWorkspaceList.stream().collect(Collectors.toList());

				// Step03：获取车型列表；
				APIResult wApiProductResult = CoreServiceImpl.getInstance().FPC_QueryProductList(wLoginUser, -1, -1);
				List<AndonProduct> wProductList = wApiProductResult.List(AndonProduct.class);
				if (wProductList == null || wProductList.size() <= 0)
					return wUpdateConfiguration;

				if (wProductList.size() > 0)
					AndonDAO.Screen.ProductList = wProductList.stream().collect(Collectors.toList());
			}
			wUpdateConfiguration = true;
			AndonDAO.Screen.Configuration = wUpdateConfiguration;
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wUpdateConfiguration;
	}

	/**
	 * 周期性刷新安灯业务数据
	 */
	public boolean Andon_UpdateBusiness(BMSEmployee wLoginUser) {
		boolean wUpdateBusiness = false;
		try {
			this.Andon_UpdateConfiguration(wLoginUser);

			if (!AndonDAO.Screen.Business) {
				OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
				// Step01：订单信息状态；
				AndonDAO.Screen.TrainItemList = AndonServiceImpl.getInstance()
						.Andon_QueryTrainOrderInfoListNew(wLoginUser).Result;
				// Step02：年度统计；
				AndonDAO.Screen.YearReport = AndonDAO.getInstance().GetYearReport(wLoginUser, wErrorCode);
				// Step03：异常信息；
				AndonDAO.Screen.EXCCallTaskBPMList = AndonDAO.getInstance().GetEXCCallTaskBPMList(wLoginUser,
						wErrorCode);
				// Step04：移车信息；
				Calendar wBaseTime = Calendar.getInstance();
				wBaseTime.set(2000, 0, 1, 0, 0, 0);

//				AndonDAO.Screen.MTCTaskList = WDWServiceImpl.getInstance()
//						.MTC_QueryEmployeeAllWeb(wLoginUser, wBaseTime, wBaseTime, -1, 0).List(MTCTask.class);

				AndonDAO.Screen.MTCTaskList = MTCTaskDAO.getInstance().SelectList(wLoginUser, null, -1, -1, -1, -1, -1,
						null,
						StringUtils.parseListArgs(MTCStatus.Completion.getValue(), MTCStatus.TaskCancel.getValue(),
								MTCStatus.Default.getValue(), MTCStatus.TaskReject.getValue()),
						-1, -1, -1, -1, "", wBaseTime, wBaseTime, wErrorCode);

				AndonDAO.Screen.OMSOrderList = OMSOrderDAO.getInstance().SelectList(wLoginUser, -1, -1, "", -1, -1, -1,
						"", "", 1,
						StringUtils.parseListArgs(OMSOrderStatus.EnterFactoryed.getValue(),
								OMSOrderStatus.Repairing.getValue(), OMSOrderStatus.FinishedWork.getValue(),
								OMSOrderStatus.ToOutChcek.getValue(), OMSOrderStatus.ToOutConfirm.getValue()),
						null, null, null, null, wErrorCode);
				// Step05：台车状态；
//				List<FMCWorkspace> wWorkSpaceList = FMCServiceImpl.getInstance()
//						.FMC_GetFMCWorkspaceList(wLoginUser, -1, -1, "", -2, 1).List(FMCWorkspace.class);
				List<FMCWorkspace> wWorkSpaceList = FMCWorkspaceDAO.getInstance().SelectList(wLoginUser, -1,
						wErrorCode);

				AndonDAO.Screen.FMCWorkspaceList = APSServiceImpl.getInstance().APS_QueryPartNoStatusNew(wLoginUser,
						wWorkSpaceList).Result;

				// 去除GB局段的车辆
				List<OMSOrder> wOList = OMSOrderDAO.getInstance().SelectList(wLoginUser, -1, -1, "", -1, -1, 43, "", "",
						-1, null, null, null, null, null, wErrorCode);
				for (FMCWorkspace wFMCWorkspace : AndonDAO.Screen.FMCWorkspaceList) {
					if (StringUtils.isEmpty(wFMCWorkspace.PartNo)) {
						continue;
					}
					if (!wOList.stream().anyMatch(p -> p.PartNo.equals(wFMCWorkspace.PartNo))) {
						continue;
					}
					wFMCWorkspace.PartNo = "";
					wFMCWorkspace.ActualPartNoList = new ArrayList<String>();
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wUpdateBusiness;
	}

	@SuppressWarnings("unused")
	private List<MTCTask> GetMTCTaskList(BMSEmployee wLoginUser, OutResult<Integer> wErrorCode) {
		List<MTCTask> wResult = new ArrayList<MTCTask>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.WDW,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("select * from {0}.mtc_task where Status in(1,2,3,4);", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				MTCTask wMTCTask = new MTCTask();
				wMTCTask.ID = StringUtils.parseInt(wReader.get("ID"));
				wMTCTask.FlowType = StringUtils.parseInt(wReader.get("FlowType"));
				wMTCTask.FlowID = StringUtils.parseInt(wReader.get("FlowID"));
				wMTCTask.Code = StringUtils.parseString(wReader.get("Code"));
				wMTCTask.PartNo = StringUtils.parseString(wReader.get("PartNo"));
				wMTCTask.PlaceID = StringUtils.parseInt(wReader.get("PlaceID"));
				wMTCTask.InformShift = StringUtils.parseInt(wReader.get("InformShift"));
				wMTCTask.TargetID = StringUtils.parseInt(wReader.get("TargetID"));
				wMTCTask.TargetStockID = StringUtils.parseInt(wReader.get("TargetStockID"));

				wMTCTask.TargetSID = StringUtils.parseInt(wReader.get("TargetSID"));
				wMTCTask.TargetSStockID = StringUtils.parseInt(wReader.get("TargetSStockID"));

				wMTCTask.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				wMTCTask.SubmitTime = StringUtils.parseCalendar(wReader.get("SubmitTime"));
				wMTCTask.UpFlowID = StringUtils.parseInt(wReader.get("UpFlowID"));
				wMTCTask.FollowerID = StringUtils
						.parseIntList(StringUtils.parseString(wReader.get("FollowerID")).split(",|;"));
				wMTCTask.Status = StringUtils.parseInt(wReader.get("Status"));
				wMTCTask.StatusText = StringUtils.parseString(wReader.get("StatusText"));
				wMTCTask.CustomerID = StringUtils.parseInt(wReader.get("CustomerID"));
				wMTCTask.ExpectedTime = StringUtils.parseCalendar(wReader.get("ExpectedTime"));
				wMTCTask.OrderID = StringUtils.parseInt(wReader.get("OrderID"));
				wMTCTask.Type = StringUtils.parseInt(wReader.get("Type"));
				wMTCTask.CarTypeID = StringUtils.parseInt(wReader.get("CarTypeID"));
				wMTCTask.CarType = APSConstans.GetFPCProductName(wMTCTask.CarTypeID);
				wMTCTask.DepartmentID = StringUtils.parseInt(wReader.get("DepartmentID"));
				wMTCTask.AreaID = StringUtils.parseString(wReader.get("AreaID"));
				wMTCTask.AreaName = APSConstans
						.GetBMSEmployeeName(StringUtils.parseIntList(wMTCTask.AreaID.split(",")));
				wMTCTask.UpFlowName = APSConstans.GetBMSEmployeeName(wMTCTask.UpFlowID);
				wMTCTask.FollowerName = APSConstans.GetBMSEmployeeName(wMTCTask.FollowerID);
				wMTCTask.DepartmentName = APSConstans.GetBMSDepartmentName(wMTCTask.DepartmentID);
				wResult.add(wMTCTask);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	private List<EXCCallTaskBPM> GetEXCCallTaskBPMList(BMSEmployee wLoginUser, OutResult<Integer> wErrorCode) {
		List<EXCCallTaskBPM> wResult = new ArrayList<EXCCallTaskBPM>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.EXC,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			// 今日时间
			Calendar wTodaySTime = Calendar.getInstance();
			wTodaySTime.set(Calendar.HOUR_OF_DAY, 0);
			wTodaySTime.set(Calendar.MINUTE, 0);
			wTodaySTime.set(Calendar.SECOND, 0);

			Calendar wTodayETime = Calendar.getInstance();
			wTodayETime.set(Calendar.HOUR_OF_DAY, 23);
			wTodayETime.set(Calendar.MINUTE, 59);
			wTodayETime.set(Calendar.SECOND, 59);

			String wSQL = StringUtils.Format("SELECT * FROM {0}.exc_calltaskbpm where :StartTime < CreateTime "
					+ "and :EndTime > CreateTime and Status not in(0,20,23);", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("StartTime", wTodaySTime);
			wParamMap.put("EndTime", wTodayETime);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				EXCCallTaskBPM wItem = new EXCCallTaskBPM();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.Code = StringUtils.parseString(wReader.get("Code"));
				wItem.FlowType = StringUtils.parseInt(wReader.get("FlowType"));
				wItem.FlowID = StringUtils.parseInt(wReader.get("FlowID"));
				wItem.UpFlowID = StringUtils.parseInt(wReader.get("UpFlowID"));
				wItem.FollowerID = StringUtils
						.parseIntList(StringUtils.parseString(wReader.get("FollowerID")).split(";"));
				wItem.Status = StringUtils.parseInt(wReader.get("Status"));
				wItem.StatusText = StringUtils.parseString(wReader.get("StatusText"));
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				wItem.SubmitTime = StringUtils.parseCalendar(wReader.get("SubmitTime"));
				wItem.StationType = StringUtils.parseInt(wReader.get("StationType"));
				wItem.StationID = StringUtils.parseInt(wReader.get("StationID"));
				wItem.ExceptionTypeID = StringUtils.parseInt(wReader.get("ExceptionTypeID"));
				wItem.ApplicantID = StringUtils.parseInt(wReader.get("ApplicantID"));
				wItem.ConfirmID = StringUtils.parseInt(wReader.get("ConfirmID"));
				wItem.OperatorID = StringUtils.parseString(wReader.get("OperatorID"));
				wItem.ApplicantTime = StringUtils.parseCalendar(wReader.get("ApplicantTime"));
				wItem.RespondLevel = StringUtils.parseInt(wReader.get("RespondLevel"));
				wItem.OnSite = StringUtils.parseInt(wReader.get("OnSite")) == 1 ? true : false;
				wItem.DisplayBoard = StringUtils.parseInt(wReader.get("DisplayBoard")) == 1 ? true : false;
				wItem.EditTime = StringUtils.parseCalendar(wReader.get("EditTime"));
				wItem.Remark = StringUtils.parseString(wReader.get("Remark"));
				wItem.Comment = StringUtils.parseString(wReader.get("Comment"));
				wItem.ImageList = StringUtils.parseString(wReader.get("ImageList"));
				wItem.ReportTimes = StringUtils.parseInt(wReader.get("ReportTimes"));
				wItem.ForwardTimes = StringUtils.parseInt(wReader.get("ForwardTimes"));
				wItem.ShiftID = StringUtils.parseInt(wReader.get("ShiftID"));
				wItem.PartNo = StringUtils.parseString(wReader.get("PartNo"));
				wItem.PlaceID = StringUtils.parseInt(wReader.get("PlaceID"));
				wItem.ExpireTime = StringUtils.parseCalendar(wReader.get("ExpireTime"));

				// 辅助属性
				wItem.UpFlowName = APSConstans.GetBMSEmployeeName(wItem.UpFlowID);
				wItem.FollowerName = APSConstans.GetBMSEmployeeName(wItem.FollowerID);
				wItem.Operators = APSConstans
						.GetBMSEmployeeName(StringUtils.parseIntList(wItem.OperatorID.split(",|;")));
				wItem.ApplyName = APSConstans.GetBMSEmployeeName((int) wItem.ApplicantID);
				wItem.ConfirmName = StringUtils.Format("{0}({1})",
						APSConstans.GetBMSEmployeeName((int) wItem.ConfirmID), APSConstans
								.GetBMSDepartmentName(APSConstans.GetBMSEmployee((int) wItem.ConfirmID).DepartmentID));

				wResult.add(wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	private AndonYearReport GetYearReport(BMSEmployee wLoginUser, OutResult<Integer> wErrorCode) {
		AndonYearReport wResult = new AndonYearReport();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
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

			ServiceResult<String> wInstance2 = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.WDW,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance2.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			int wYear = Calendar.getInstance().get(Calendar.YEAR);

			Calendar wStartYear = Calendar.getInstance();
			wStartYear.set(wYear, 0, 1, 0, 0, 0);

			Calendar wEndYear = Calendar.getInstance();
			wEndYear.set(wYear, 11, 31, 23, 59, 59);

			int wMonth = Calendar.getInstance().get(Calendar.MONTH);

			Calendar wStartMonth = Calendar.getInstance();
			wStartMonth.set(wYear, wMonth, 1, 0, 0, 0);

			Calendar wEndMonth = Calendar.getInstance();
			wEndMonth.set(wYear, wMonth + 1, 1, 0, 0, 0);

			// 今日时间
			Calendar wTodaySTime = Calendar.getInstance();
			wTodaySTime.set(Calendar.HOUR_OF_DAY, 0);
			wTodaySTime.set(Calendar.MINUTE, 0);
			wTodaySTime.set(Calendar.SECOND, 0);

			Calendar wTodayETime = Calendar.getInstance();
			wTodayETime.set(Calendar.HOUR_OF_DAY, 23);
			wTodayETime.set(Calendar.MINUTE, 59);
			wTodayETime.set(Calendar.SECOND, 59);

			String wSQL = StringUtils.Format(
					"select (SELECT count(*) FROM  {0}.oms_order "
							+ "where :StartYear < RealFinishDate and RealFinishDate < :EndYear "
							+ "and Status in(5,6,7,8)) AnnualNum, "
							+ "(SELECT count(*)  FROM  {0}.oms_order where :StartYear < RealFinishDate "
							+ "and RealFinishDate < :EndYear and Status in(5,6,7,8) and LineID=1) AnnualC5Num, "
							+ "(SELECT count(*)  FROM  {0}.oms_order where :StartYear < RealFinishDate "
							+ "and RealFinishDate < :EndYear and Status in(5,6,7,8) and LineID=2) AnnualC6Num, "
							+ "(SELECT count(*)  FROM  {0}.oms_order where :StartMonth < RealFinishDate "
							+ "and RealFinishDate < :EndMonth and Status in(5,6,7,8)) ThisMonthNum, "
							+ "(SELECT count(*)  FROM  {0}.oms_order where Status in(3,4,5,6,7)) LocoCar, "
							+ "(SELECT count(*)  FROM  {0}.oms_order where Status in(4)) RepairCar, "
							+ "(SELECT Count(*)  FROM  {0}.aps_taskpart WHERE StartTime<=:EndTime "
							+ "AND Status in(2,3,4) AND ShiftPeriod=5 and Active=1) stationTask, "
							+ "(SELECT count(*)  FROM {1}.exc_calltaskbpm where :StartTime < CreateTime "
							+ "and :EndTime > CreateTime and Status not in(0,23)) ExcTask, "
							+ "(SELECT count(*)  FROM {2}.ncr_task where CreateTime > :StartTime "
							+ "and CreateTime < :EndTime and Status not in(0,23)) ncrTask, "
							+ "(SELECT count(*)  FROM {2}.rro_repairitem where CreateTime > :StartTime "
							+ "and CreateTime < :EndTime and Status not in(0,23)) repairTask;",
					wInstance.Result, wInstance1.Result, wInstance2.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("StartYear", wStartYear);
			wParamMap.put("EndYear", wEndYear);
			wParamMap.put("StartMonth", wStartMonth);
			wParamMap.put("EndMonth", wEndMonth);
			wParamMap.put("StartTime", wTodaySTime);
			wParamMap.put("EndTime", wTodayETime);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {

				wResult.AnnualNum = StringUtils.parseInt(wReader.get("AnnualNum"));
				wResult.AnnualC5Num = StringUtils.parseInt(wReader.get("AnnualC5Num"));
				wResult.AnnualC6Num = StringUtils.parseInt(wReader.get("AnnualC6Num"));
				wResult.ThisMonthNum = StringUtils.parseInt(wReader.get("ThisMonthNum"));
				wResult.LocoCar = StringUtils.parseInt(wReader.get("LocoCar"));
				wResult.RepairCar = StringUtils.parseInt(wReader.get("RepairCar"));
				wResult.stationTask = StringUtils.parseInt(wReader.get("stationTask"));
				wResult.ExcTask = StringUtils.parseInt(wReader.get("ExcTask"));
				wResult.ncrTask = StringUtils.parseInt(wReader.get("ncrTask"));
				wResult.repairTask = StringUtils.parseInt(wReader.get("repairTask"));

				return wResult;
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	public void Andon_ResetConfiguration(BMSEmployee wLoginUser) {
		try {
			AndonDAO.Screen.Configuration = false;
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 根据订单查询未完工的工位计划
	 */
	public APSTaskPart SelectNotFinishTaskPart(BMSEmployee wLoginUser, int wOrderID, OutResult<Integer> wErrorCode) {
		APSTaskPart wResult = new APSTaskPart();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"select * from {0}.aps_taskpart "
							+ "where ShiftPeriod=5 and OrderID=:OrderID and Active=1 and Status not in (5) limit 1;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("OrderID", wOrderID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				wResult.ID = StringUtils.parseInt(wReader.get("ID"));
				wResult.PartID = StringUtils.parseInt(wReader.get("PartID"));
				wResult.PartName = APSConstans.GetFPCPartName(wResult.PartID);
				return wResult;
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 查询停时统计数据
	 */
	public AndonLocomotiveProductionStatus Andon_AsignStopTimes(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime, OutResult<Integer> wErrorCode) {
		AndonLocomotiveProductionStatus wResult = new AndonLocomotiveProductionStatus();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("select "
					+ "	(select CAST(AVG(PosterioriStopTimes) AS DECIMAL(10,1))  from {0}.oms_order where Status=8 and RealFinishDate > :wStartTime and RealFinishDate < :wEndTime and LineID=2) YHTS_C6,"
					+ "    (select CAST(AVG(PosterioriStopTimes) AS DECIMAL(10,1))  from {0}.oms_order where Status=8 and RealFinishDate > :wStartTime and RealFinishDate < :wEndTime and LineID=1) YHTS_C5,"
					+ "    (select CAST(AVG(ActualRepairStopTimes) AS DECIMAL(10,1))  from {0}.oms_order where Status=8 and RealFinishDate > :wStartTime and RealFinishDate < :wEndTime and LineID=2) JCTS_C6,"
					+ "    (select CAST(AVG(ActualRepairStopTimes) AS DECIMAL(10,1))  from {0}.oms_order where Status=8 and RealFinishDate > :wStartTime and RealFinishDate < :wEndTime and LineID=1) JCTS_C5;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				wResult.YHTS_C6 = StringUtils.parseDouble(wReader.get("YHTS_C6"));
				wResult.YHTS_C5 = StringUtils.parseDouble(wReader.get("YHTS_C5"));
				wResult.JCTS_C6 = StringUtils.parseDouble(wReader.get("JCTS_C6"));
				wResult.JCTS_C5 = StringUtils.parseDouble(wReader.get("JCTS_C5"));
				// 四舍五入保留1位小数
				wResult.YHTS_C6 = new BigDecimal(wResult.YHTS_C6).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
				wResult.YHTS_C5 = new BigDecimal(wResult.YHTS_C5).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
				wResult.JCTS_C6 = new BigDecimal(wResult.JCTS_C6).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
				wResult.JCTS_C5 = new BigDecimal(wResult.JCTS_C5).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 查询工区当月返工件数
	 */
	public int Andon_AsignRepairs(BMSEmployee wLoginUser, Calendar wStartMonth, Calendar wEndMonth, int wAreaID,
			OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.WDW,
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
					"SELECT count(*) Number FROM {0}.rro_repairitem t1,{1}.lfs_workareastation t2 "
							+ "where t1.StationID=t2.StationID and  t1.CreateTime > :wStartMonth and "
							+ "t1.CreateTime < :wEndMonth and t1.Status not in (0,21) "
							+ "and t2.WorkAreaID=:wAreaID and t1.FlowType != 2011;",
					wInstance.Result, wInstance1.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wStartMonth", wStartMonth);
			wParamMap.put("wEndMonth", wEndMonth);
			wParamMap.put("wAreaID", wAreaID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				wResult = StringUtils.parseInt(wReader.get("Number"));
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 查询全年统计数据
	 */
	public AndonLocomotiveProductionStatus AsignStatisticalData(BMSEmployee wLoginUser, Calendar wStartYear,
			Calendar wEndYear, Calendar wStartMonth, Calendar wEndMonth, OutResult<Integer> wErrorCode) {
		AndonLocomotiveProductionStatus wResult = new AndonLocomotiveProductionStatus();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("select "
					+ "	(select count(*)  from {0}.oms_order where RealFinishDate > :wStartYear and RealFinishDate < :wEndYear and Status in (5,6,7,8)) LJXJ_Year,"
					+ "    (select count(*)  from {0}.oms_order where RealFinishDate > :wStartYear and RealFinishDate < :wEndYear and Status in (5,6,7,8) and LineID=1) LJXJ_Year_C5,"
					+ "    (select count(*)  from {0}.oms_order where RealFinishDate > :wStartYear and RealFinishDate < :wEndYear and Status in (5,6,7,8) and LineID=2) LJXJ_Year_C6,"
					+ "    (select count(*)  from {0}.oms_order where RealFinishDate > :wStartMonth and RealFinishDate < :wEndMonth and Status in (8)) SJJF_Month,"
					+ "    (select count(*)  from {0}.oms_order where RealFinishDate > :wStartMonth and RealFinishDate < :wEndMonth and Status in (8) and LineID=1) SJJF_Month_C5,"
					+ "    (select count(*)  from {0}.oms_order where RealFinishDate > :wStartMonth and RealFinishDate < :wEndMonth and Status in (8) and LineID=2) SJJF_Month_C6;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wStartYear", wStartYear);
			wParamMap.put("wEndYear", wEndYear);
			wParamMap.put("wStartMonth", wStartMonth);
			wParamMap.put("wEndMonth", wEndMonth);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				wResult.LJXJ_Year = StringUtils.parseInt(wReader.get("LJXJ_Year"));
				wResult.LJXJ_Year_C5 = StringUtils.parseInt(wReader.get("LJXJ_Year_C5"));
				wResult.LJXJ_Year_C6 = StringUtils.parseInt(wReader.get("LJXJ_Year_C6"));
				wResult.SJJF_Month = StringUtils.parseInt(wReader.get("SJJF_Month"));
				wResult.SJJF_Month_C5 = StringUtils.parseInt(wReader.get("SJJF_Month_C5"));
				wResult.SJJF_Month_C6 = StringUtils.parseInt(wReader.get("SJJF_Month_C6"));
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 查询本月不合格评审单、异常单情况
	 */
	public AndonLocomotiveProductionStatus Andon_AsingNcrAndExc(BMSEmployee wLoginUser, Calendar wStartMonth,
			Calendar wEndMonth, OutResult<Integer> wErrorCode) {
		AndonLocomotiveProductionStatus wResult = new AndonLocomotiveProductionStatus();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.WDW,
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

			String wSQL = StringUtils.Format("select "
					+ "	(SELECT count(*)  FROM {0}.ncr_task where CreateTime > :wStartMonth and CreateTime < :wEndMonth and Status not in (0,21)) Total_Ncr,"
					+ "    (SELECT count(*)  FROM {0}.ncr_task where CreateTime > :wStartMonth and CreateTime < :wEndMonth and Status not in (0,21,12,22)) Doing_Ncr,"
					+ "    (select count(*)  from {1}.exc_calltaskbpm where CreateTime > :wStartMonth and CreateTime < :wEndMonth and Status not in (0,23)) Total_Exc,"
					+ "    (select count(*)  from {1}.exc_calltaskbpm where CreateTime > :wStartMonth and CreateTime < :wEndMonth and Status not in (0,23,20)) Doing_Exc;",
					wInstance.Result, wInstance1.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wStartMonth", wStartMonth);
			wParamMap.put("wEndMonth", wEndMonth);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				wResult.Total_Ncr = StringUtils.parseInt(wReader.get("Total_Ncr"));
				wResult.Doing_Ncr = StringUtils.parseInt(wReader.get("Doing_Ncr"));
				wResult.Total_Exc = StringUtils.parseInt(wReader.get("Total_Exc"));
				wResult.Doing_Exc = StringUtils.parseInt(wReader.get("Doing_Exc"));
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 查询各工区的完成情况
	 */
	public AndonLocomotiveProductionStatus Andon_AsignAreaProgress(BMSEmployee wLoginUser,
			OutResult<Integer> wErrorCode) {
		AndonLocomotiveProductionStatus wResult = new AndonLocomotiveProductionStatus();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
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

			String wSQL = StringUtils.Format("select "
					+ "	(SELECT count(*)  FROM {0}.aps_taskstep t1,{0}.aps_taskpart t2,{2}.lfs_workareastation t3  where t1.PartID=t3.StationID and t3.WorkAreaID=45 and t1.TaskPartID=t2.ID and t2.Status in (2,3,4,5) and t1.Active=1 and t2.Active=1 and t1.Status in (5) and t1.CreateTime > ''{1}'') FNumber1,"
					+ "    (SELECT count(*)  FROM {0}.aps_taskstep t1,{0}.aps_taskpart t2,{2}.lfs_workareastation t3  where t1.PartID=t3.StationID and t3.WorkAreaID=45 and t1.TaskPartID=t2.ID and t2.Status in (2,3,4,5) and t1.Active=1 and t2.Active=1 and t1.Status in (2,4) and t1.CreateTime > ''{1}'') NFNumber1,"
					+ "    (SELECT count(*)  FROM {0}.aps_taskstep t1,{0}.aps_taskpart t2,{2}.lfs_workareastation t3  where t1.PartID=t3.StationID and t3.WorkAreaID=46 and t1.TaskPartID=t2.ID and t2.Status in (2,3,4,5) and t1.Active=1 and t2.Active=1 and t1.Status in (5) and t1.CreateTime > ''{1}'') FNumber2,"
					+ "    (SELECT count(*)  FROM {0}.aps_taskstep t1,{0}.aps_taskpart t2,{2}.lfs_workareastation t3  where t1.PartID=t3.StationID and t3.WorkAreaID=46 and t1.TaskPartID=t2.ID and t2.Status in (2,3,4,5) and t1.Active=1 and t2.Active=1 and t1.Status in (2,4) and t1.CreateTime > ''{1}'') NFNumber2,"
					+ "    (SELECT count(*)  FROM {0}.aps_taskstep t1,{0}.aps_taskpart t2,{2}.lfs_workareastation t3  where t1.PartID=t3.StationID and t3.WorkAreaID=47 and t1.TaskPartID=t2.ID and t2.Status in (2,3,4,5) and t1.Active=1 and t2.Active=1 and t1.Status in (5) and t1.CreateTime > ''{1}'') FNumber3,"
					+ "    (SELECT count(*)  FROM {0}.aps_taskstep t1,{0}.aps_taskpart t2,{2}.lfs_workareastation t3  where t1.PartID=t3.StationID and t3.WorkAreaID=47 and t1.TaskPartID=t2.ID and t2.Status in (2,3,4,5) and t1.Active=1 and t2.Active=1 and t1.Status in (2,4) and t1.CreateTime > ''{1}'') NFNumber3,"
					+ "    (SELECT count(*)  FROM {0}.aps_taskstep t1,{0}.aps_taskpart t2,{2}.lfs_workareastation t3  where t1.PartID=t3.StationID and t3.WorkAreaID=48 and t1.TaskPartID=t2.ID and t2.Status in (2,3,4,5) and t1.Active=1 and t2.Active=1 and t1.Status in (5) and t1.CreateTime > ''{1}'') FNumber4,"
					+ "    (SELECT count(*)  FROM {0}.aps_taskstep t1,{0}.aps_taskpart t2,{2}.lfs_workareastation t3  where t1.PartID=t3.StationID and t3.WorkAreaID=48 and t1.TaskPartID=t2.ID and t2.Status in (2,3,4,5) and t1.Active=1 and t2.Active=1 and t1.Status in (2,4) and t1.CreateTime > ''{1}'') NFNumber4;",
					wInstance.Result, "2021-08-29 00:00:00", wInstance1.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				wResult.Done_One = StringUtils.parseInt(wReader.get("FNumber1"));
				wResult.NotDone_One = StringUtils.parseInt(wReader.get("NFNumber1"));

				wResult.Done_Two = StringUtils.parseInt(wReader.get("FNumber2"));
				wResult.NotDone_Two = StringUtils.parseInt(wReader.get("NFNumber2"));

				wResult.Done_Three = StringUtils.parseInt(wReader.get("FNumber3"));
				wResult.NotDone_Three = StringUtils.parseInt(wReader.get("NFNumber3"));

				wResult.Done_Four = StringUtils.parseInt(wReader.get("FNumber4"));
				wResult.NotDone_Four = StringUtils.parseInt(wReader.get("NFNumber4"));
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取车体工位ID集合
	 */
	public List<Integer> Andon_GetBodyStationIDList(BMSEmployee wLoginUser, OutResult<Integer> wErrorCode) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.EXC,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("SELECT StationID FROM {0}.lfs_workareastation where StationType=1;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wStationID = StringUtils.parseInt(wReader.get("StationID"));
				wResult.add(wStationID);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取工位ID集合(工位日计划)
	 */
	public List<Integer> Andon_QueryPartDayPlanIDList(BMSEmployee wLoginUser, OutResult<Integer> wErrorCode) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			Calendar wSTime = Calendar.getInstance();
			wSTime.set(wSTime.get(Calendar.YEAR), wSTime.get(Calendar.MONTH), wSTime.get(Calendar.DATE), 0, 0, 0);

			Calendar wETime = Calendar.getInstance();
			wETime.set(wSTime.get(Calendar.YEAR), wSTime.get(Calendar.MONTH), wSTime.get(Calendar.DATE), 23, 59, 59);

			String wSQL = StringUtils.Format("SELECT distinct PartID FROM {0}.aps_taskstep where Active=1 "
					+ "and ( (PlanStartTime>:wSTime and PlanStartTime < :wETime) "
					+ "or (EndTime>:wSTime and EndTime < :wETime) "
					+ "or ( PlanStartTime < :wSTime and Status in (2,4)) );", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wSTime", wSTime);
			wParamMap.put("wETime", wETime);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wPartID = StringUtils.parseInt(wReader.get("PartID"));
				wResult.add(wPartID);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 查询工位日计划兑现率
	 */
	public AndonDayPlanPartCashingRate Andon_QueryPartDayPlan(BMSEmployee wLoginUser, int wPartID,
			List<Integer> wOrderIDList, Calendar wSTime, Calendar wETime, OutResult<Integer> wErrorCode) {
		AndonDayPlanPartCashingRate wResult = new AndonDayPlanPartCashingRate();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

//			Calendar wSTime = wDate;
//			wSTime.set(wSTime.get(Calendar.YEAR), wSTime.get(Calendar.MONTH), wSTime.get(Calendar.DATE), 0, 0, 0);
//
//			Calendar wETime = wDate;
//			wETime.set(wSTime.get(Calendar.YEAR), wSTime.get(Calendar.MONTH), wSTime.get(Calendar.DATE), 23, 59, 59);

			String wSQL = StringUtils.Format("select "
					+ "	(select count(*) from {0}.aps_taskstep where Active=1 and PartID=:wPartID and Status in (5) "
					+ "and EndTime>:wSTime and EndTime < :wETime "
					+ "and ( :wOrderID is null or :wOrderID = '''' or OrderID in ({1})) " + ") FinishNumber,"
					+ "    (SELECT count(*) FROM {0}.aps_taskstep where Active=1 and PartID=:wPartID "
					+ "and ( :wOrderID is null or :wOrderID = '''' or OrderID in ({1})) "
					+ "and ( (PlanStartTime>:wSTime and PlanStartTime < :wETime) or (EndTime>:wSTime "
					+ "and EndTime < :wETime) " + "or ( PlanStartTime < :wSTime and Status in (2,4)) )) TotalNumber;",
					wInstance.Result, wOrderIDList.size() > 0 ? StringUtils.Join(",", wOrderIDList) : "0");

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wSTime", wSTime);
			wParamMap.put("wETime", wETime);
			wParamMap.put("wPartID", wPartID);
			wParamMap.put("wOrderID", StringUtils.Join(",", wOrderIDList));

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {

				int wFinishNumber = StringUtils.parseInt(wReader.get("FinishNumber"));
				int wTotalNumber = StringUtils.parseInt(wReader.get("TotalNumber"));

				double wRate = 0.0;
				if (wTotalNumber > 0) {
					wRate = new BigDecimal((double) wFinishNumber / wTotalNumber).setScale(2, BigDecimal.ROUND_HALF_UP)
							.doubleValue();
				}

				String wPartName = APSConstans.GetFPCPartName(wPartID);

				wResult = new AndonDayPlanPartCashingRate(wPartID, wPartName, wFinishNumber, wTotalNumber, wRate * 100);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 查询已排好序的工位集合
	 */
	public List<Integer> Andon_QuerySortedPartIDList(BMSEmployee wLoginUser, OutResult<Integer> wErrorCode) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"SELECT PartID FROM {0}.fpc_routepart where RouteID= 278 order by OrderID asc;", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wPartID = StringUtils.parseInt(wReader.get("PartID"));
				wResult.add(wPartID);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 工区筛工位
	 */
	public List<Integer> Andon_QueryAreaPartIDList(BMSEmployee wLoginUser, List<Integer> wAreaID,
			OutResult<Integer> wErrorCode) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.EXC,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"SELECT StationID FROM {0}.lfs_workareastation " + "where WorkAreaID in ({1}) and Active=1;",
					wInstance.Result, StringUtils.Join(",", wAreaID));

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wStationID = StringUtils.parseInt(wReader.get("StationID"));
				wResult.add(wStationID);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 班组筛选工位
	 */
	public List<Integer> Andon_QueryClassPartIDList(BMSEmployee wLoginUser, List<Integer> wClassID,
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
					"SELECT StationID FROM {0}.bms_workcharge " + "where ClassID in ({1}) and Active=1;",
					wInstance.Result, StringUtils.Join(",", wClassID));

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wStationID = StringUtils.parseInt(wReader.get("StationID"));
				wResult.add(wStationID);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取年度统计数据
	 */
	public AndonDayReportTJ Andon_QueryTJData(BMSEmployee wLoginUser, int wYear, int wMonth,
			OutResult<Integer> wErrorCode) {
		AndonDayReportTJ wResult = new AndonDayReportTJ();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			// 今年时间
			Calendar wYearStart = Calendar.getInstance();
			wYearStart.set(wYear, 0, 1, 0, 0, 0);
			Calendar wYearEnd = Calendar.getInstance();
			wYearEnd.set(wYear, 11, 31, 23, 59, 59);
			// 当月时间
			Calendar wMonthStart = Calendar.getInstance();
			wMonthStart.set(wYear, wMonth - 1, 1, 0, 0, 0);
			Calendar wMonthEnd = Calendar.getInstance();
			wMonthEnd.set(wYear, wMonth, 1, 0, 0, 0);

			String wSQL = StringUtils.Format("select "
					+ "	(SELECT count(*) FROM {0}.oms_order where RealFinishDate > :wYearStart and RealFinishDate < :wYearEnd and Status in (5,6,7,8)) LJXJ,"
					+ "    (SELECT count(*) FROM {0}.oms_order where RealFinishDate > :wYearStart and RealFinishDate < :wYearEnd and Status in (5,6,7,8) and LineID=1) LJXJ_C5,"
					+ "    (SELECT avg(ActualRepairStopTimes) FROM {0}.oms_order where RealFinishDate > :wYearStart and RealFinishDate < :wYearEnd and Status in (5,6,7,8) and LineID=1) JXTS_C5,"
					+ "    (SELECT count(*) FROM {0}.oms_order where RealFinishDate > :wYearStart and RealFinishDate < :wYearEnd and Status in (5,6,7,8) and LineID=2) LJXJ_C6,"
					+ "    (SELECT avg(ActualRepairStopTimes) FROM {0}.oms_order where RealFinishDate > :wYearStart and RealFinishDate < :wYearEnd and Status in (5,6,7,8) and LineID=2) JXTS_C6,"
					+ "    (SELECT count(*) FROM {0}.oms_order where RealFinishDate > :wMonthStart and RealFinishDate < :wMonthEnd and Status in (5,6,7,8)) BYJG,"
					+ "    (SELECT count(*) FROM {0}.oms_order where Status in (3,4,5,6,7) and Active=1) ZCJC,"
					+ "    (SELECT count(*) FROM {0}.oms_order where Status in (4) and Active=1) ZXJC;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wYearStart", wYearStart);
			wParamMap.put("wYearEnd", wYearEnd);
			wParamMap.put("wMonthStart", wMonthStart);
			wParamMap.put("wMonthEnd", wMonthEnd);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				wResult.LJXJ = StringUtils.parseInt(wReader.get("LJXJ"));
				wResult.LJXJ_C5 = StringUtils.parseInt(wReader.get("LJXJ_C5"));
				wResult.JXTS_C5 = StringUtils.parseDouble(wReader.get("JXTS_C5"));
				wResult.JXTS_C5 = new BigDecimal(wResult.JXTS_C5).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
				wResult.LJXJ_C6 = StringUtils.parseInt(wReader.get("LJXJ_C6"));
				wResult.JXTS_C6 = StringUtils.parseDouble(wReader.get("JXTS_C6"));
				wResult.JXTS_C6 = new BigDecimal(wResult.JXTS_C6).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
				wResult.BYJG = StringUtils.parseInt(wReader.get("BYJG"));
				wResult.ZCJC = StringUtils.parseInt(wReader.get("ZCJC"));
				wResult.ZXJC = StringUtils.parseInt(wReader.get("ZXJC"));
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 查询当月入厂数据
	 */
	public int Andon_SelectRC(BMSEmployee wLoginUser, int wYear, int wMonth, int wLineID,
			OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			// 当月时间
			Calendar wMonthStart = Calendar.getInstance();
			wMonthStart.set(wYear, wMonth - 1, 1, 0, 0, 0);
			Calendar wMonthEnd = Calendar.getInstance();
			wMonthEnd.set(wYear, wMonth, 1, 0, 0, 0);

			String wSQL = StringUtils
					.Format("select count(*) Number from {0}.oms_order where RealReceiveDate > :wMonthStart "
							+ "and RealReceiveDate < :wMonthEnd and LineID=:wLineID;", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wMonthStart", wMonthStart);
			wParamMap.put("wMonthEnd", wMonthEnd);
			wParamMap.put("wLineID", wLineID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				wResult = StringUtils.parseInt(wReader.get("Number"));
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取上台位的车数量
	 */
	public int Andon_SelectPlaceNumber(BMSEmployee wLoginUser, List<Integer> wOrderIDList,
			OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.WDW,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wOrderIDList == null || wOrderIDList.size() <= 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"SELECT count(distinct OrderID) Number FROM {0}.mtc_task where "
							+ "Status=5 and TargetSID in (12,16) and OrderID in ({1});",
					wInstance.Result, StringUtils.Join(",", wOrderIDList));

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				wResult = StringUtils.parseInt(wReader.get("Number"));
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	public Calendar Andon_SelectPlaceDate(BMSEmployee wLoginUser, OMSOrder wOMSOrder, OutResult<Integer> wErrorCode) {
		Calendar wBaseTime = Calendar.getInstance();
		wBaseTime.set(2000, 0, 1, 0, 0, 0);
		Calendar wResult = wBaseTime;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.WDW,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("SELECT SubmitTime FROM {0}.mtc_task where Status=5 "
					+ "and TargetSID in (12,16) and OrderID = :OrderID;", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("OrderID", wOMSOrder.ID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				wResult = StringUtils.parseCalendar(wReader.get("SubmitTime"));
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 通过工区查看未完成、已完成的工序任务详情
	 */
	public List<AndonTaskStep> SelectAreaStepDetails(BMSEmployee wLoginUser, int wStatus, int wWorkAreaID,
			Calendar wStartTime, Calendar wEndTime, OutResult<Integer> wErrorCode) {
		List<AndonTaskStep> wResult = new ArrayList<AndonTaskStep>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
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

			String wSQL = "";
			if (wStatus == 1) {
				wSQL = StringUtils.Format(
						"SELECT t2.WorkAreaID,t1.OrderID,t1.PartNo,t1.PartID,t1.StepID FROM "
								+ "{0}.aps_taskstep t1,{1}.lfs_workareastation t2 where t1.Active=1 "
								+ "and t1.PartID=t2.StationID and t2.WorkAreaID=:WorkAreaID and t1.Status in (5) "
								+ "and t1.EndTime > :wStartTime and t1.EndTime < :wEndTime;",
						wInstance.Result, wInstance1.Result);
			} else {
				wSQL = StringUtils.Format("SELECT t2.WorkAreaID,t1.OrderID,t1.PartNo,t1.PartID,t1.StepID FROM "
						+ "{0}.aps_taskstep t1,{1}.lfs_workareastation t2 "
						+ "where t1.Active=1 and t1.PartID=t2.StationID and t2.WorkAreaID=:WorkAreaID and t1.Status in (2,4);",
						wInstance.Result, wInstance1.Result);
			}

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("WorkAreaID", wWorkAreaID);
			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				AndonTaskStep wItem = new AndonTaskStep();

				wItem.WorkAreaID = StringUtils.parseInt(wReader.get("WorkAreaID"));
				wItem.WorkArea = APSConstans.GetBMSDepartmentName(wItem.WorkAreaID);
				wItem.OrderID = StringUtils.parseInt(wReader.get("OrderID"));
				wItem.PartNo = StringUtils.parseString(wReader.get("PartNo"));
				wItem.PartID = StringUtils.parseInt(wReader.get("PartID"));
				wItem.PartName = APSConstans.GetFPCPartName(wItem.PartID);
				wItem.StepID = StringUtils.parseInt(wReader.get("StepID"));
				wItem.StepName = APSConstans.GetFPCPartPointName(wItem.StepID);

				wResult.add(wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据工艺路线ID获取工位，顺序字典
	 */
	public Map<Integer, Integer> Andon_QueryPartOrderMap(BMSEmployee wLoginUser, int routeID,
			OutResult<Integer> wErrorCode) {
		Map<Integer, Integer> wResult = new HashMap<Integer, Integer>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"SELECT PartID,OrderID FROM {0}.fpc_routepart " + "where RouteID=:RouteID order by OrderID asc;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("RouteID", routeID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wPartID = StringUtils.parseInt(wReader.get("PartID"));
				int wOrderID = StringUtils.parseInt(wReader.get("OrderID"));
				wResult.put(wPartID, wOrderID);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}
}
