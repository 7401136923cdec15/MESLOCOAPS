package com.mes.loco.aps.server.serviceimpl.dao.fpc;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.mes.loco.aps.server.service.mesenum.MESDBSource;
import com.mes.loco.aps.server.service.mesenum.MESException;
import com.mes.loco.aps.server.service.po.OutResult;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.fpc.FPCRoute;
import com.mes.loco.aps.server.service.po.fpc.FPCRoutePartPoint;
import com.mes.loco.aps.server.service.po.lfs.LFSWorkAreaStation;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;

public class FPCRouteDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(FPCRouteDAO.class);

	private static FPCRouteDAO Instance = null;

	/**
	 * 查单条
	 */
	public FPCRoute SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		FPCRoute wResult = new FPCRoute();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<FPCRoute> wList = SelectList(wLoginUser, wID, -1, -1, -1, wErrorCode);
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
	public List<FPCRoute> SelectList(BMSEmployee wLoginUser, int wID, int wLineID, int wProductID, int wCustomerID,
			OutResult<Integer> wErrorCode) {
		List<FPCRoute> wResultList = new ArrayList<FPCRoute>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = MessageFormat.format("SELECT * FROM {0}.fpc_route WHERE  1=1  "
					+ "and ( :wID <= 0 or :wID = ID ) " + "and ( :wLineID <= 0 or :wLineID = LineID ) "
					+ "and ( :wProductID <= 0 or :wProductID = ProductID ) "
					+ "and ( :wCustomerID <= 0 or :wCustomerID = CustomerID );", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wLineID", wLineID);
			wParamMap.put("wProductID", wProductID);
			wParamMap.put("wCustomerID", wCustomerID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				FPCRoute wItem = new FPCRoute();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.VersionNo = StringUtils.parseString(wReader.get("VersionNo"));
				wItem.BusinessUnitID = StringUtils.parseInt(wReader.get("BusinessUnitID"));
				wItem.ProductTypeID = StringUtils.parseInt(wReader.get("ProductTypeID"));
				wItem.CreatorID = StringUtils.parseInt(wReader.get("CreatorID"));
				wItem.EditorID = StringUtils.parseInt(wReader.get("EditorID"));
				wItem.AuditorID = StringUtils.parseInt(wReader.get("AuditorID"));
				wItem.Status = StringUtils.parseInt(wReader.get("Status"));
				wItem.Active = StringUtils.parseInt(wReader.get("Active"));
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				wItem.EditTime = StringUtils.parseCalendar(wReader.get("EditTime"));
				wItem.AuditTime = StringUtils.parseCalendar(wReader.get("AuditTime"));
				wItem.Description = StringUtils.parseString(wReader.get("Description"));
				wItem.FactoryID = StringUtils.parseInt(wReader.get("FactoryID"));
				wItem.LineID = StringUtils.parseInt(wReader.get("LineID"));
				wItem.Name = StringUtils.parseString(wReader.get("Name"));
				wItem.ProductID = StringUtils.parseInt(wReader.get("ProductID"));
				wItem.CustomerID = StringUtils.parseInt(wReader.get("CustomerID"));
				wItem.IsStandard = StringUtils.parseInt(wReader.get("IsStandard"));

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	private FPCRouteDAO() {
		super();
	}

	public static FPCRouteDAO getInstance() {
		if (Instance == null)
			Instance = new FPCRouteDAO();
		return Instance;
	}

	/**
	 * 获取工区工位集合
	 */

	public List<LFSWorkAreaStation> SelectLFSWorkAreaStationList(BMSEmployee wLoginUser,
			OutResult<Integer> wErrorCode) {
		List<LFSWorkAreaStation> wResult = new ArrayList<LFSWorkAreaStation>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.EXC,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("SELECT * FROM {0}.lfs_workareastation;", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				LFSWorkAreaStation wItem = new LFSWorkAreaStation();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.WorkAreaID = StringUtils.parseInt(wReader.get("WorkAreaID"));
				wItem.StationID = StringUtils.parseInt(wReader.get("StationID"));
				wItem.CreateID = StringUtils.parseInt(wReader.get("CreateID"));
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				wItem.Active = StringUtils.parseInt(wReader.get("Active"));
				wItem.OrderNum = StringUtils.parseInt(wReader.get("OrderNum"));
				wItem.StationType = StringUtils.parseInt(wReader.get("StationType"));
				wItem.PageNumber = StringUtils.parseInt(wReader.get("PageNumber"));

				wResult.add(wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取工艺工序(根据订单和工位)
	 */
	@SuppressWarnings("unchecked")
	public List<FPCRoutePartPoint> SelectRoutePartPointList(BMSEmployee wLoginUser, String wOrderIDs, String wPartIDs,
			OutResult<Integer> wErrorCode) {
		List<FPCRoutePartPoint> wResult = new ArrayList<FPCRoutePartPoint>();
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
					"SELECT * FROM {0}.fpc_routepartpoint where RouteID in (SELECT RouteID FROM " + "{3}.oms_order "
							+ "where ID in ({1})) and PartID in ({2}) order by RouteID asc,OrderID asc;",
					wInstance.Result, wOrderIDs, wPartIDs, wInstance1.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				FPCRoutePartPoint wItem = new FPCRoutePartPoint();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.RouteID = StringUtils.parseInt(wReader.get("RouteID"));
				wItem.PartID = StringUtils.parseInt(wReader.get("PartID"));
				wItem.PartPointID = StringUtils.parseInt(wReader.get("PartPointID"));
				wItem.CreatorID = StringUtils.parseInt(wReader.get("CreatorID"));
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				wItem.OrderID = StringUtils.parseInt(wReader.get("OrderID"));
				wItem.PrevStepID = StringUtils.parseInt(wReader.get("PrevStepID"));
				wItem.NextStepIDMap = (Map<String, String>) JSON.parse(wReader.get("NextStepIDMap").toString());
				wItem.StandardPeriod = StringUtils.parseDouble(wReader.get("StandardPeriod"));
				wItem.ActualPeriod = StringUtils.parseDouble(wReader.get("ActualPeriod"));
				wItem.DefaultOrder = StringUtils.parseString(wReader.get("DefaultOrder"));
				wItem.Code = StringUtils.parseString(wReader.get("Code"));
				wItem.Version = StringUtils.parseString(wReader.get("Version"));

				wResult.add(wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取待同步的工艺路线
	 */
	public List<FPCRoute> GetToSynchronizedList(BMSEmployee wLoginUser, OutResult<Integer> wErrorCode) {
		List<FPCRoute> wResult = new ArrayList<FPCRoute>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"select ProductID,CustomerID,LineID from {0}.fpc_route where isstandard=1;", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				FPCRoute wItem = new FPCRoute();

				wItem.ProductID = StringUtils.parseInt(wReader.get("ProductID"));
				wItem.CustomerID = StringUtils.parseInt(wReader.get("CustomerID"));
				wItem.LineID = StringUtils.parseInt(wReader.get("LineID"));

				wResult.add(wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 通过车型、修程、局段获取已进厂、维修中，推送过给ERP的订单ID集合
	 */
	public List<Integer> GetMyOrderIDList(BMSEmployee wLoginUser, FPCRoute wFPCRoute, OutResult<Integer> wErrorCode) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("select t1.ID from {0}.oms_order t1,{0}.aps_bomlog t2 "
					+ "where t1.ID=t2.OrderID and t2.Status=1 and t1.Status in (3,4) "
					+ "and t1.ProductID=:ProductID and t1.LineID=:LineID and t1.BureauSectionID=:BureauSectionID "
					+ "and t1.RealSendDate < ''2010-1-1'';", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ProductID", wFPCRoute.ProductID);
			wParamMap.put("LineID", wFPCRoute.LineID);
			wParamMap.put("BureauSectionID", wFPCRoute.CustomerID);

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
	 * 获取最大的BOMID
	 */
	public int GetMaxBOMID(BMSEmployee wLoginUser, FPCRoute wFPCRoute, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils
					.Format("select max(ID) ID from {0}.mss_bom where productID=:ProductID and LineID=:LineID "
							+ "and CustomerID=:CustomerID and IsStandard=1;", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ProductID", wFPCRoute.ProductID);
			wParamMap.put("LineID", wFPCRoute.LineID);
			wParamMap.put("CustomerID", wFPCRoute.CustomerID);

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
}
